package br.com.hr.reactiveflashcards.api.mapper;

import br.com.hr.reactiveflashcards.api.controller.request.DeckRequest;
import br.com.hr.reactiveflashcards.api.controller.response.DeckResponse;
import br.com.hr.reactiveflashcards.domain.document.DeckDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeckMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DeckDocument toDocument(final DeckRequest userRequest);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    DeckDocument toDocument(final DeckRequest userRequest, final String id);

    DeckResponse toResponse(final DeckDocument userRequest);
}
