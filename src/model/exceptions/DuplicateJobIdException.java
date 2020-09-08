package model.exceptions;

public class DuplicateJobIdException extends Exception {
	public DuplicateJobIdException() {
		System.out.println("There is a job with provided job id. Please supply a unique job id and try again");
	}
}
