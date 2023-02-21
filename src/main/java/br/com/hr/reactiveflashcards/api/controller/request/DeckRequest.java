package br.com.hr.reactiveflashcards.api.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;

public record DeckRequest(
    @JsonProperty("name") @NotBlank @Size(min = 1, max = 255) String name,
    @JsonProperty("description") @NotBlank @Size(min = 1,
                                                 max = 255) String description,
    @JsonProperty("cards") @Valid @Size(min = 3) Set<CardRequest> cards) {

  @Builder(toBuilder = true)
  public DeckRequest {}
}
