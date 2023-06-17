package com.kodilla.fantasy.domain.exception;

public class PlayerAlreadyExistInSquadException extends Exception {
    public PlayerAlreadyExistInSquadException() {
        super("Player already exist in this squad!");
    }
}
