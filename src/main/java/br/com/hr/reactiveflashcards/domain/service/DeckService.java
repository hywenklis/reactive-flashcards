package br.com.hr.reactiveflashcards.domain.service;

import br.com.hr.reactiveflashcards.domain.document.DeckDocument;
import br.com.hr.reactiveflashcards.domain.repository.DeckRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;

    public Mono<DeckDocument> save(final DeckDocument deckDocument) {
        return Mono.just(deckDocument)
                .flatMap(deckRepository::save)
                .doFirst(() -> log.info("Try to save a follow deck {}", deckDocument));
    }
}
