package com.example.heroesandroid.heroes.auxiliaryclasses.boardexception;

public class BoardException extends Exception {

    public BoardException(final BoardExceptionTypes error) {
        super(error.getErrorType());
    }

}
