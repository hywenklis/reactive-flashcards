package br.com.hr.reactiveflashcards.api.exceptionhandler;

import static br.com.hr.reactiveflashcards.domain.exception.BaseErrorMessage.GENERIC_BAD_REQUEST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import br.com.hr.reactiveflashcards.api.controller.response.ErrorFieldResponse;
import br.com.hr.reactiveflashcards.api.controller.response.ProblemResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class WebExchangeBindHandler
        extends AbstractHandlerException<WebExchangeBindException> {

    private final MessageSource messageSource;

    public WebExchangeBindHandler(ObjectMapper objectMapper,
                                  MessageSource messageSource) {
        super(objectMapper);
        this.messageSource = messageSource;
    }

    @Override
    Mono<Void> handlerException(ServerWebExchange exchange,
                                WebExchangeBindException ex) {
        return Mono
                .fromCallable(() -> {
                    prepareExchange(exchange, BAD_REQUEST);
                    return GENERIC_BAD_REQUEST.message();
                })
                .map(message -> buildError(BAD_REQUEST, message))
                .flatMap(response -> buildParamsErrorMessage(response, ex))
                .doFirst(() -> log.error("WebExchangeBindException: ", ex))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    private Mono<ProblemResponse>
    buildParamsErrorMessage(final ProblemResponse response,
                            final WebExchangeBindException ex) {
        return Flux.fromIterable(ex.getAllErrors())
                .map(objectError
                        -> ErrorFieldResponse.builder()
                        .name(objectError instanceof FieldError fieldError
                                ? fieldError.getField()
                                : objectError.getObjectName())
                        .message(messageSource.getMessage(
                                objectError, LocaleContextHolder.getLocale()))
                        .build())
                .collectList()
                .map(problemResponses
                        -> response.toBuilder().fields(problemResponses).build());
    }
}
