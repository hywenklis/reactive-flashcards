package br.com.hr.reactiveflashcards.api.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.hr.reactiveflashcards.api.controller.request.StudyRequest;
import br.com.hr.reactiveflashcards.api.controller.response.QuestionResponse;
import br.com.hr.reactiveflashcards.api.mapper.StudyMapper;
import br.com.hr.reactiveflashcards.domain.service.StudyService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/studies")
@Slf4j
@AllArgsConstructor
public class StudyController {

  private final StudyService studyService;
  private final StudyMapper studyMapper;

  @PostMapping(consumes = APPLICATION_JSON_VALUE,
               produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(CREATED)
  public Mono<QuestionResponse>
  start(@Valid @RequestBody final StudyRequest studyRequest) {
    return studyService.start(studyMapper.toDocument(studyRequest))
        .doFirst(()
                     -> log.info("Try to create a study with follow request {}",
                                 studyRequest))
        .map(document
             -> studyMapper.toResponse(document.getLastQuestionPending()));
  }
}
