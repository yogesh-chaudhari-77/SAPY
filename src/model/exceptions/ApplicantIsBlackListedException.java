package model.exceptions;

/*
 * This exception is thrown when and attempt is made to shortlist an applicant which is blacklisted
 */

public class ApplicantIsBlackListedException extends Exception {

	// 23-09-2020
	public ApplicantIsBlackListedException(String message) {
		super(message);
	}
	
	
	public ApplicantIsBlackListedException() {
		System.out.println("The applicant is blacklisted. Hence can't be shortlisted");
	}
	
}
