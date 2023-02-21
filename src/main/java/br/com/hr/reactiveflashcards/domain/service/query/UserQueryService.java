package br.com.hr.reactiveflashcards.domain.service.query;

import static br.com.hr.reactiveflashcards.domain.exception.BaseErrorMessage.USER_NOT_FOUND;

import br.com.hr.reactiveflashcards.domain.document.UserDocument;
import br.com.hr.reactiveflashcards.domain.exception.NotFoundException;
import br.com.hr.reactiveflashcards.domain.repository.UserRepository;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class UserQueryService {

  private final UserRepository userRepository;

  public Mono<UserDocument> findById(final String id) {
    return Mono.just(id)
        .flatMap(userRepository::findById)
        .doFirst(() -> log.info("Try to find user with id {}", id))
        .filter(Objects::nonNull)
        .switchIfEmpty(
            Mono.defer(()
                           -> Mono.error(new NotFoundException(
                               USER_NOT_FOUND.params("id", id).message()))));
  }

  public Mono<UserDocument> findByEmail(final String email) {
    return Mono.just(email)
            .flatMap(userRepository::findByEmail)
            .doFirst(() -> log.info("Try to find user with email {}", email))
            .filter(Objects::nonNull)
            .switchIfEmpty(
                    Mono.defer(()
                            -> Mono.error(new NotFoundException(
                            USER_NOT_FOUND.params("email", email).message()))));
  }
}
