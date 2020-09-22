package model.entities;

import model.enums.*;
import model.exceptions.*;

public class Complaints {
	
	private String complaintMessage;
	User complainingUser;
	User complaintOnUser;

	public Complaints() 
	{
		complainingUser = null;
		complaintOnUser = null;
	}
	
	
	//* Complaint made by applicant on employer
	public Complaints(Applicant app, Employer emp,String message)
	{
		this.complainingUser = app;
		this.complaintOnUser = emp;
		this.complaintMessage = message;
	}
	

	//* Complaint made by employer on applicant
	public Complaints(Employer emp, Applicant app, String message)
	{
		this.complaintOnUser = emp;
		this.complaintOnUser = app;
		this.complaintMessage = message;
	}

}
