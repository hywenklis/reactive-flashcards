package br.com.hr.reactiveflashcards.domain.document;

import java.util.Set;
import lombok.Builder;

public record StudyDeck(String deckId, Set<StudyCard> cards) {

  @Builder(toBuilder = true)
  public StudyDeck {}
}
