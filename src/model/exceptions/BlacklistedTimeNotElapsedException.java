package model.exceptions;

public class BlacklistedTimeNotElapsedException extends Exception{

	
	public BlacklistedTimeNotElapsedException()
	{
		System.out.println("The Fully blacklisted users cannot be reactivated until 3 months from date blacklisting has elapsed");
	}
}
