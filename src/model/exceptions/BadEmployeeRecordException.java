package model.exceptions;

public class BadEmployeeRecordException extends Exception {
    public BadEmployeeRecordException(String error) {
        super(error);
    }
}
