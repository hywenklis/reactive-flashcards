package br.com.hr.reactiveflashcards.api.mapper;

import br.com.hr.reactiveflashcards.api.controller.request.UserRequest;
import br.com.hr.reactiveflashcards.api.controller.response.UserResponse;
import br.com.hr.reactiveflashcards.domain.document.UserDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  UserDocument toDocument(final UserRequest userRequest);

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  UserDocument toDocument(final UserRequest userRequest, final String id);

  UserResponse toResponse(final UserDocument userRequest);
}
