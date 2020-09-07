package model.exceptions;

public class DuplicateEntryException extends Exception {
    public DuplicateEntryException(String error) {
        super(error);
    }
}
