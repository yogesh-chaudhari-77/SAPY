package model.exceptions;

public class DuplicateJobIdException extends Exception {
	
	// 23-09-2020
	public DuplicateJobIdException(String message) {
		super(message);
	}
	
	public DuplicateJobIdException() {
		System.out.println("There is a job with provided job id. Please supply a unique job id and try again");
	}
}
