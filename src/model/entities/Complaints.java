package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.io.Serializable;

public class Complaints implements Serializable {
	
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
	
	public String getComplaintOn()
	{
		return this.complaintOnUser.getId();
	}
	
	

	//* Complaint made by employer on applicant
	public Complaints(Employer emp, Applicant app, String message)
	{
		this.complaintOnUser = emp;
		this.complaintOnUser = app;
		this.complaintMessage = message;
	}

	public String toString() 
	{
		String printString;
		printString = "\n" + this.complainingUser.getId() + "," + this.complaintMessage;
		return printString;
	}
}
