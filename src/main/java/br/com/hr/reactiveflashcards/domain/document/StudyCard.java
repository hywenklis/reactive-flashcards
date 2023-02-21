package br.com.hr.reactiveflashcards.domain.document;

import lombok.Builder;

public record StudyCard(
        String front,
        String back
) {

    @Builder(toBuilder = true)
    public StudyCard {}
}
