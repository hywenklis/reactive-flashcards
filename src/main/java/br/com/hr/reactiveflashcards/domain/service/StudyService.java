package br.com.hr.reactiveflashcards.domain.service;

import static java.util.Collections.*;

import br.com.hr.reactiveflashcards.domain.document.Card;
import br.com.hr.reactiveflashcards.domain.document.StudyDocument;
import br.com.hr.reactiveflashcards.domain.mapper.StudyDomainMapper;
import br.com.hr.reactiveflashcards.domain.repository.StudyRepository;
import br.com.hr.reactiveflashcards.domain.service.query.DeckQueryService;
import br.com.hr.reactiveflashcards.domain.service.query.UserQueryService;
import java.util.Collections;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class StudyService {

  private final UserQueryService userQueryService;
  private final DeckQueryService deckQueryService;
  private final StudyDomainMapper studyDomainMapper;
  private final StudyRepository studyRepository;

  public Mono<StudyDocument> start(final StudyDocument document) {
    return userQueryService.findById(document.userId())
        .flatMap(
            user -> deckQueryService.findById(document.studyDeck().deckId()))
        .flatMap(deck -> getCards(document, deck.cards()))
        .map(study
             -> study.toBuilder()
                    .questions(
                        singletonList(studyDomainMapper.generateRandomQuestion(
                            study.studyDeck().cards())))
                    .build())
        .doFirst(() -> log.info("Generating a first random question"))
        .flatMap(studyRepository::save)
        .doOnSuccess(study -> log.info("A follow study was save {}", study));
  }

  public Mono<StudyDocument> getCards(final StudyDocument document,
                                      final Set<Card> cards) {
    return Flux.fromIterable(cards)
        .doFirst(() -> log.info("Copy cards to new study"))
        .map(studyDomainMapper::toStudyCard)
        .collectList()
        .map(studyCards
             -> document.studyDeck()
                    .toBuilder()
                    .cards(Set.copyOf(studyCards))
                    .build())
        .map(studyDeck -> document.toBuilder().studyDeck(studyDeck).build());
  }
}
