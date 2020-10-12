package model.exceptions;

public class NotFullyBlacklistedUserException extends Exception{

	public NotFullyBlacklistedUserException()
	{
		System.out.println("The entered user is not already fully Blacklisted. ");
	}
}
