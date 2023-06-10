package com.kodilla.fantasy.domain.exception;

public class NotEnoughFundsException extends Exception {
    public NotEnoughFundsException() {
        super("Not enough funds left to add this player!");
    }
}
