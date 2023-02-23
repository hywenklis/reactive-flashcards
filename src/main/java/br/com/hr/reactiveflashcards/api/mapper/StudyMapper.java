package br.com.hr.reactiveflashcards.api.mapper;

import br.com.hr.reactiveflashcards.api.controller.request.StudyRequest;
import br.com.hr.reactiveflashcards.api.controller.response.QuestionResponse;
import br.com.hr.reactiveflashcards.domain.document.Question;
import br.com.hr.reactiveflashcards.domain.document.StudyDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudyMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "studyDeck.deckId", source = "deckId")
  @Mapping(target = "studyDeck.cards", ignore = true)
  @Mapping(target = "questions", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  StudyDocument toDocument(final StudyRequest studyRequest);

  QuestionResponse toResponse(final Question question);
}
