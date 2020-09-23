package model.exceptions;

public class NullJobReferenceException extends Exception {

	
	public NullJobReferenceException(String message) {
		super(message);
	}
	
	
	public NullJobReferenceException() {
		System.err.println("Null Job Reference Supplied");
	}
}
