package br.com.hr.reactiveflashcards.api.exceptionhandler;

import static br.com.hr.reactiveflashcards.domain.exception.BaseErrorMessage.GENERIC_BAD_REQUEST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import br.com.hr.reactiveflashcards.api.controller.response.ErrorFieldResponse;
import br.com.hr.reactiveflashcards.api.controller.response.ProblemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ConstraintViolationHandler
    extends AbstractHandlerException<ConstraintViolationException> {

  public ConstraintViolationHandler(ObjectMapper objectMapper) {
    super(objectMapper);
  }

  @Override
  Mono<Void> handlerException(ServerWebExchange exchange,
                              ConstraintViolationException ex) {
    return Mono
        .fromCallable(() -> {
          prepareExchange(exchange, BAD_REQUEST);
          return GENERIC_BAD_REQUEST.message();
        })
        .map(message -> buildError(BAD_REQUEST, message))
        .flatMap(response -> buildParamsErrorMessage(response, ex))
        .doFirst(() -> log.error("ConstraintViolationException: ", ex))
        .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
  }

  private Mono<ProblemResponse>
  buildParamsErrorMessage(final ProblemResponse response,
                          final ConstraintViolationException ex) {
    return Flux.fromIterable(ex.getConstraintViolations())
        .map(constraintViolation
             -> ErrorFieldResponse.builder()
                    .name(((PathImpl)constraintViolation.getPropertyPath())
                              .getLeafNode()
                              .toString())
                    .message(constraintViolation.getMessage())
                    .build())
        .collectList()
        .map(problemResponses
             -> response.toBuilder().fields(problemResponses).build());
  }
}
