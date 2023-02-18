package br.com.hr.reactiveflashcards.domain.exception;

public class ReactiveFlashcardsException extends RuntimeException {

    public ReactiveFlashcardsException() {
    }

    public ReactiveFlashcardsException(String message) {
        super(message);
    }

    public ReactiveFlashcardsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReactiveFlashcardsException(Throwable cause) {
        super(cause);
    }
}
