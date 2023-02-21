package br.com.hr.reactiveflashcards.api.exceptionhandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import br.com.hr.reactiveflashcards.domain.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class NotFoundHandler
        extends AbstractHandlerException<NotFoundException> {

    public NotFoundHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    Mono<Void> handlerException(ServerWebExchange exchange,
                                NotFoundException ex) {
        return Mono
                .fromCallable(() -> {
                    prepareExchange(exchange, NOT_FOUND);
                    return ex.getMessage();
                })
                .map(message -> buildError(NOT_FOUND, message))
                .doFirst(()
                        -> log.error(
                        "NotFoundException: ",
                        ex))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }
}
