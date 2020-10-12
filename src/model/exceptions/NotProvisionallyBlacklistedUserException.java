package model.exceptions;

public class NotProvisionallyBlacklistedUserException extends Exception{

	public NotProvisionallyBlacklistedUserException()
	{
		System.out.println("The entered user is not already Provisionally Blacklisted. ");
	}
}
