package br.com.hr.reactiveflashcards.domain.service;

import br.com.hr.reactiveflashcards.domain.document.UserDocument;
import br.com.hr.reactiveflashcards.domain.exception.EmailAlreadyUsedException;
import br.com.hr.reactiveflashcards.domain.exception.NotFoundException;
import br.com.hr.reactiveflashcards.domain.repository.UserRepository;
import br.com.hr.reactiveflashcards.domain.service.query.UserQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static br.com.hr.reactiveflashcards.domain.exception.BaseErrorMessage.EMAIL_ALREADY_USED;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public Mono<UserDocument> save(final UserDocument userDocument) {
        return Mono.just(userDocument)
                .flatMap(this::verifyEmail)
                .then(userRepository.save(userDocument))
                .doFirst(() -> log.info("Try to save a follow user {}", userDocument));
    }

    public Mono<UserDocument> update(final UserDocument document) {
        return Mono.just(document)
                .flatMap(this::verifyEmail)
                .then(userQueryService.findById(document.id()))
                .map(user
                        -> document.toBuilder()
                        .createdAt(user.createdAt())
                        .updatedAt(user.updatedAt())
                        .build())
                .flatMap(userRepository::save)
                .doFirst(()
                        -> log.info("Try to update a user with follow info {}",
                        document));
    }

    public Mono<Void> delete(final String id) {
        return Mono.just(id)
                .flatMap(userQueryService::findById)
                .flatMap(userRepository::delete)
                .doFirst(() -> log.info("Try to delete a user with follow id {}", id));
    }

    private Mono<Void> verifyEmail(final UserDocument userDocument) {
        return Mono.justOrEmpty(userDocument.email())
                .flatMap(userQueryService::findByEmail)
                .filter(stored -> stored.id().equals(userDocument.id()))
                .switchIfEmpty(Mono.error(new EmailAlreadyUsedException(EMAIL_ALREADY_USED.params(userDocument.email()).message())))
                .onErrorResume(NotFoundException.class, e -> Mono.empty())
                .then();
    }
}
