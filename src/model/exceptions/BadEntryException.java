package model.exceptions;

public class BadEntryException extends Exception{
    public BadEntryException(String error) {
        super(error);
    }
}
