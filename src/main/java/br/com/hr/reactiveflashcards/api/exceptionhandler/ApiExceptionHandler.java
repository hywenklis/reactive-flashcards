package br.com.hr.reactiveflashcards.api.exceptionhandler;

import br.com.hr.reactiveflashcards.domain.exception.NotFoundException;
import br.com.hr.reactiveflashcards.domain.exception.ReactiveFlashcardsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;

@Component
@Order(-2)
@Slf4j
@AllArgsConstructor
public class ApiExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return Mono.error(ex)
                .onErrorResume(MethodNotAllowedException.class, e -> new MethodNotAllowedHandler(objectMapper).handlerException(exchange, e))
                .onErrorResume(NotFoundException.class, e -> new NotFoundHandler(objectMapper).handlerException(exchange, e))
                .onErrorResume(ConstraintViolationException.class, e -> new ConstraintViolationHandler(objectMapper).handlerException(exchange, e))
                .onErrorResume(WebExchangeBindException.class, e -> new WebExchangeBindHandler(objectMapper, messageSource).handlerException(exchange, e))
                .onErrorResume(ResponseStatusException.class, e -> new ResponseStatusHandler(objectMapper).handlerException(exchange, e))
                .onErrorResume(ReactiveFlashcardsException.class, e -> new ReactiveFlashcardsHandler(objectMapper).handlerException(exchange, e))
                .onErrorResume(Exception.class, e -> new GenericHandler(objectMapper).handlerException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e -> new JsonProcessingHandler(objectMapper).handlerException(exchange, e))
                .then();
    }
}
