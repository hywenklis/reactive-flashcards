package br.com.hr.reactiveflashcards.domain.repository;

import br.com.hr.reactiveflashcards.domain.document.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository
    extends ReactiveMongoRepository<UserDocument, String> {

  Mono<UserDocument> findByEmail(final String email);
}
