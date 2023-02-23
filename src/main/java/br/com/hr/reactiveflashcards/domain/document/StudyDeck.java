package br.com.hr.reactiveflashcards.domain.document;

import java.util.HashSet;
import java.util.Set;

import br.com.hr.reactiveflashcards.domain.document.StudyDocument.StudyDocumentBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

public record StudyDeck(String deckId, Set<StudyCard> cards) {

  public static StudyDeckBuilder builder() {
    return new StudyDeckBuilder();
  }

  public StudyDeckBuilder toBuilder() {
    return new StudyDeckBuilder();
  }

  @NoArgsConstructor
  @AllArgsConstructor
  public static class StudyDeckBuilder {
    private String deckId;
    private Set<StudyCard> cards = new HashSet<>();

    public StudyDeckBuilder deckId(final String deckId) {
      this.deckId = deckId;
      return this;
    }

    public StudyDeckBuilder cards(final Set<StudyCard> cards) {
      this.cards = cards;
      return this;
    }

    public StudyDeck build() {
      return new StudyDeck(deckId, cards);
    }
  }
}
