package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.io.Serializable;

public class Reference implements Serializable {

    private String firstname;
    private String lastname;
    private String email;
    private String designation;
    private String companyName;
    private String phoneNumber;

    public Reference(String firstname, String lastname, String email, String designation, String companyName, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.designation = designation;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Firstname: " + firstname + "\n" +
                "Lastname: " + lastname + "\n" +
                "Email: " + email + "\n" +
                "Designation: " + designation + "\n" +
                "CompanyName: " + companyName + "\n" +
                "Phone Number: " + phoneNumber + "\n";
    }
}
