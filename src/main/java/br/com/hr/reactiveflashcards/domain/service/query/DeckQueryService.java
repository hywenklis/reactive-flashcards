package br.com.hr.reactiveflashcards.domain.service.query;

import br.com.hr.reactiveflashcards.domain.document.DeckDocument;
import br.com.hr.reactiveflashcards.domain.exception.NotFoundException;
import br.com.hr.reactiveflashcards.domain.repository.DeckRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static br.com.hr.reactiveflashcards.domain.exception.BaseErrorMessage.DECK_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor
public class DeckQueryService {

  private final DeckRepository deckRepository;

  public Mono<DeckDocument> findById(final String id) {
    return Mono.just(id)
        .flatMap(deckRepository::findById)
        .doFirst(() -> log.info("Try to find deck with id {}", id))
        .filter(Objects::nonNull)
        .switchIfEmpty(
            Mono.defer(()
                           -> Mono.error(new NotFoundException(
                               DECK_NOT_FOUND.params(id).message()))));
  }
}
