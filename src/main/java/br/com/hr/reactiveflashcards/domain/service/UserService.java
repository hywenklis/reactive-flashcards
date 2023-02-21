package br.com.hr.reactiveflashcards.domain.service;

import br.com.hr.reactiveflashcards.domain.document.UserDocument;
import br.com.hr.reactiveflashcards.domain.repository.UserRepository;
import br.com.hr.reactiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public Mono<UserDocument> save(final UserDocument userDocument) {
        return Mono.just(userDocument)
                .flatMap(userRepository::save)
                .doFirst(() -> log.info("Try to save a follow user {}", userDocument));
    }

    public Mono<UserDocument> update(final UserDocument document) {
        return Mono.just(document)
                .flatMap(userDocument -> userQueryService.findById(userDocument.id()))
                .map(user -> document.toBuilder()
                        .createdAt(user.createdAt())
                        .updatedAt(user.updatedAt())
                        .build())
                .flatMap(userRepository::save)
                .doFirst(() -> log.info("Try to update a user with follow info {}", document));
    }
}
