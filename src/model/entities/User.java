package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.io.Serializable;

public abstract class User implements Serializable {

	private String id;

	private String userEmail;
	private String password;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	
	
	public User(String id, String userEmail, String password, String firstName, String lastName, String phoneNumber) {

		this.id = id;
		this.userEmail = userEmail;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		
	}
	
	/**
	 * Basic account creation
	 * Currently being used in employer class
	 */
	public User(String id, String userEmail, String password) {
		this.id = id;
		this.userEmail = userEmail;
		this.password = password;
	}

	public User(){
		this("","","","","","");
	}
	
	
	/*
	 * Employer is authenticated with account email and account password to be able to use the system
	 */
	
	public boolean authenticate() {
		
		return true;
	}
	
	
	// Getters - Setters Starts Here
	
	public String getId() {
		return id;
	}

	
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getUserEmail() {
		return userEmail;
	}
	
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	
	public String getPassword() {
		return password;
	}
	
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getFirstName() {
		return firstName;
	}
	
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	
	public String getLastName() {
		return lastName;
	}
	
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", userEmail='" + userEmail + '\'' +
				", password='" + password + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				'}';
	}
}
