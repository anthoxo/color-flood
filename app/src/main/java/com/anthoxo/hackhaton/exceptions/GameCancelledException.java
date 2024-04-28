package com.anthoxo.hackhaton.exceptions;

public class GameCancelledException extends Exception {
    public GameCancelledException(Exception ex) {
        super(ex);
    }
}
