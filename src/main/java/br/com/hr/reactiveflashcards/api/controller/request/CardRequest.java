package br.com.hr.reactiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;

public record CardRequest(
    @JsonProperty("front") @NotBlank @Size(min = 1, max = 255) String front,
    @JsonProperty("back") @NotBlank @Size(min = 1, max = 255) String back) {

  @Builder(toBuilder = true)
  public CardRequest {}
}
