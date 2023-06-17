package com.kodilla.fantasy.domain.exception;

public class UserAlreadyInLeagueException extends Exception {
    public UserAlreadyInLeagueException() {
        super("User already entered this league!");
    }
}
