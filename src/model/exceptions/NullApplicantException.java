package model.exceptions;

/*
 * Thrown when an null applicant object is passed to the method
 */
public class NullApplicantException extends Exception {

	public NullApplicantException() {
		System.out.println("Null applicant has been passed. Please try again.");
	}
	
}
