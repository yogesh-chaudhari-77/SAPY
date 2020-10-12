package model.exceptions;

public class NoSuchRecordException extends Exception{
    public NoSuchRecordException(String error) {
        super(error);
    }
}
