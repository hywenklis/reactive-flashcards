package br.com.hr.reactiveflashcards.api.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Builder;

public record QuestionResponse(@JsonProperty("asked") String asked,
                               @JsonProperty("askedIn")
                               OffsetDateTime askedIn) {

  @Builder(toBuilder = true)
  public QuestionResponse {}
}
