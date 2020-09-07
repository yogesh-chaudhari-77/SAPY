package model.exceptions;

/*
 * This exception is thrown when and attempt is made to shortlist an applicant which is blacklisted
 */

public class ApplicantIsBlackListedException extends Exception {

	public void ApplicantIsBlackListedException() {
		System.out.println("The applicant is blacklisted. Hence can't be shortlisted");
	}
	
}
