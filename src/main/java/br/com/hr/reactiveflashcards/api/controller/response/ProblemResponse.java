package br.com.hr.reactiveflashcards.api.controller.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;

@JsonInclude(NON_NULL)
public record ProblemResponse(Integer status, OffsetDateTime timestamp,
                              String errorDescription,
                              List<ErrorFieldResponse> fields) {

  @Builder(toBuilder = true)
  public ProblemResponse {}
}
