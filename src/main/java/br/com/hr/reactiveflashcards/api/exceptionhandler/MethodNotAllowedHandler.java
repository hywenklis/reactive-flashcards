package br.com.hr.reactiveflashcards.api.exceptionhandler;

import static br.com.hr.reactiveflashcards.domain.exception.BaseErrorMessage.GENERIC_METHOD_NOT_ALLOW;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class MethodNotAllowedHandler
        extends AbstractHandlerException<MethodNotAllowedException> {

    public MethodNotAllowedHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Mono<Void> handlerException(ServerWebExchange exchange,
                                       MethodNotAllowedException ex) {
        return Mono
                .fromCallable(() -> {
                    prepareExchange(exchange, METHOD_NOT_ALLOWED);
                    return GENERIC_METHOD_NOT_ALLOW
                            .params(Objects.requireNonNull(exchange.getRequest().getMethod())
                                    .name())
                            .message();
                })
                .map(message -> buildError(METHOD_NOT_ALLOWED, message))
                .doFirst(()
                        -> log.error(
                        "MethodNotAllowedException: Method [{}] is not allowed at [{}]",
                        exchange.getRequest().getMethod(),
                        exchange.getRequest().getPath().value(), ex))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }
}
