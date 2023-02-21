package br.com.hr.reactiveflashcards.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.hr.reactiveflashcards.api.controller.request.UserRequest;
import br.com.hr.reactiveflashcards.api.controller.response.UserResponse;
import br.com.hr.reactiveflashcards.api.mapper.UserMapper;
import br.com.hr.reactiveflashcards.core.validation.MongoId;
import br.com.hr.reactiveflashcards.domain.service.UserService;
import br.com.hr.reactiveflashcards.domain.service.query.UserQueryService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {

  private final UserQueryService userQueryService;
  private final UserService userService;
  private final UserMapper userMapper;

  @PostMapping(consumes = APPLICATION_JSON_VALUE,
               produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(CREATED)
  public Mono<UserResponse>
  save(@Valid @RequestBody final UserRequest userRequest) {
    return Mono.just(userRequest)
        .map(userMapper::toDocument)
        .flatMap(userService::save)
        .doFirst(
            () -> log.info("Saving a user with follow data {}", userRequest))
        .map(userMapper::toResponse);
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE, value = "{id}")
  public Mono<UserResponse> findById(@PathVariable @Valid @MongoId(
      message = "{userController.id}") final String id) {
    return Mono.just(id)
        .flatMap(userQueryService::findById)
        .doFirst(() -> log.info("Finding a user with follow id {}", id))
        .map(userMapper::toResponse);
  }

  @PutMapping(consumes = APPLICATION_JSON_VALUE,
              produces = APPLICATION_JSON_VALUE, value = "{id}")
  public Mono<UserResponse>
  update(@PathVariable @Valid @MongoId(message = "userController.id")
         final String id, @Valid @RequestBody final UserRequest userRequest) {
    return Mono.just(userRequest)
        .zipWith(Mono.just(id), userMapper::toDocument)
        .flatMap(userService::update)
        .doFirst(()
                     -> log.info(
                         "Updating a user with follow info [body: {}, id: {}]",
                         userRequest, id))
        .map(userMapper::toResponse);
  }
}
