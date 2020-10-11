package model.exceptions;

public class NullJobException extends Exception{
	
	public NullJobException(String msg) {
		super(msg);
	}
	
	public NullJobException() {
		super("Null Job Reference. Please try again.");
		System.out.println("Null Job Reference. Please try again.");
	}
	
}
