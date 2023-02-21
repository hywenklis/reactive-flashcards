package br.com.hr.reactiveflashcards.api.exceptionhandler;

import static br.com.hr.reactiveflashcards.domain.exception.BaseErrorMessage.GENERIC_EXCEPTION;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GenericHandler extends AbstractHandlerException<Exception> {

  public GenericHandler(ObjectMapper objectMapper) { super(objectMapper); }

  @Override
  Mono<Void> handlerException(ServerWebExchange exchange, Exception ex) {
    return Mono
        .fromCallable(() -> {
          prepareExchange(exchange, INTERNAL_SERVER_ERROR);
          return GENERIC_EXCEPTION.message();
        })
        .map(message -> buildError(INTERNAL_SERVER_ERROR, message))
        .doFirst(() -> log.error("Exception: ", ex))
        .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
  }
}
