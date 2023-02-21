package br.com.hr.reactiveflashcards.api.controller.response;

import br.com.hr.reactiveflashcards.api.controller.request.CardRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.Builder;

public record DeckResponse(@JsonProperty("id") String id,
                           @JsonProperty("name") String name,
                           @JsonProperty("description") String description,
                           @JsonProperty("cards") Set<CardRequest> cards) {

  @Builder(toBuilder = true)
  public DeckResponse {}
}
