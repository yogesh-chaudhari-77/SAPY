package model.exceptions;

public class AlreadyPresentInYourShortListedListException extends Exception {

	// 23-09-2020
	public AlreadyPresentInYourShortListedListException(String message) {
		super(message);
	}


	public AlreadyPresentInYourShortListedListException() {
		System.out.println("The applicant is already present in your shortlisted applicant's list.");
	}
}
