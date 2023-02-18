package br.com.hr.reactiveflashcards.api.controller;

import br.com.hr.reactiveflashcards.api.controller.request.UserRequest;
import br.com.hr.reactiveflashcards.api.controller.response.UserResponse;
import br.com.hr.reactiveflashcards.api.mapper.UserMapper;
import br.com.hr.reactiveflashcards.domain.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public Mono<UserResponse> save(@Valid @RequestBody final UserRequest userRequest) {
        return Mono.just(userRequest)
                .map(userMapper::toDocument)
                .flatMap(userService::save)
                .doFirst(() -> log.info("=== Saving a user with follow data {}", userRequest))
                .map(userMapper::toResponse);
    }
}
