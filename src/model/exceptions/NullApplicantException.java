package model.exceptions;

/*
 * Thrown when an null applicant object is passed to the method
 */
public class NullApplicantException extends Exception {

	// 23-09-2020
	public NullApplicantException(String msg) {
		super(msg);
	}

	public NullApplicantException() {
		System.out.println("Null applicant has been passed. Please try again.");
	}

}
