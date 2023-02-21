package br.com.hr.reactiveflashcards.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.hr.reactiveflashcards.api.controller.request.DeckRequest;
import br.com.hr.reactiveflashcards.api.controller.response.DeckResponse;
import br.com.hr.reactiveflashcards.api.controller.response.UserResponse;
import br.com.hr.reactiveflashcards.api.mapper.DeckMapper;
import br.com.hr.reactiveflashcards.core.validation.MongoId;
import br.com.hr.reactiveflashcards.domain.service.DeckService;
import javax.validation.Valid;

import br.com.hr.reactiveflashcards.domain.service.query.DeckQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/decks")
@Slf4j
@AllArgsConstructor
public class DeckController {
  private final DeckService deckService;
  private final DeckMapper deckMapper;
  private final DeckQueryService deckQueryService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE,
               produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(CREATED)
  public Mono<DeckResponse>
  save(@Valid @RequestBody final DeckRequest deckRequest) {
    return Mono.just(deckRequest)
        .map(deckMapper::toDocument)
        .flatMap(deckService::save)
        .doFirst(
            () -> log.info("Saving a deck with follow data {}", deckRequest))
        .map(deckMapper::toResponse);
  }

    @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
    public Mono<DeckResponse> findById(@PathVariable @Valid @MongoId(
        message = "{deckController.id}") final String id) {
      return Mono.just(id)
          .flatMap(deckQueryService::findById)
          .doFirst(() -> log.info("Finding a deck with follow id {}", id))
          .map(deckMapper::toResponse);
    }
  //
  //  @PutMapping(consumes = APPLICATION_JSON_VALUE,
  //              produces = APPLICATION_JSON_VALUE, value = "{id}")
  //  public Mono<UserResponse>
  //  update(@PathVariable @Valid @MongoId(message = "userController.id")
  //         final String id, @Valid @RequestBody final UserRequest userRequest)
  //         {
  //    return Mono.just(userRequest)
  //        .zipWith(Mono.just(id), userMapper::toDocument)
  //        .flatMap(userService::update)
  //        .doFirst(()
  //                     -> log.info(
  //                         "Updating a user with follow info [body: {}, id:
  //                         {}]", userRequest, id))
  //        .map(userMapper::toResponse);
  //  }
  //
  //  @DeleteMapping("{id}")
  //  @ResponseStatus(NO_CONTENT)
  //  public Mono<Void> delete(@PathVariable @Valid @MongoId(
  //      message = "userController.id") final String id) {
  //    return Mono.just(id)
  //        .flatMap(userService::delete)
  //        .doFirst(() -> log.info("Deleting a user with follow id {}", id));
  //  }
}
