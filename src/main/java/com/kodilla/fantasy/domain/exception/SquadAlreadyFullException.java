package com.kodilla.fantasy.domain.exception;

public class SquadAlreadyFullException extends Exception {
    public SquadAlreadyFullException() {
        super("Squad is already full, cannot add another player");
    }
}
