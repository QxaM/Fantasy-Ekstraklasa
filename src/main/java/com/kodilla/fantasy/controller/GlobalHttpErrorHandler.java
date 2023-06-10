package com.kodilla.fantasy.controller;

import com.kodilla.fantasy.domain.exception.ElementNotFoundException;
import com.kodilla.fantasy.domain.exception.NotEnoughFundsException;
import com.kodilla.fantasy.domain.exception.SquadAlreadyFullException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<Object> handleElementNotFoundException(ElementNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SquadAlreadyFullException.class)
    public ResponseEntity<Object> handleSquadAlreadyFullException(SquadAlreadyFullException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NotEnoughFundsException.class)
    public ResponseEntity<Object> handleNotEnoughFundsException(NotEnoughFundsException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
