package model.entities;

import model.enums.*;
import model.exceptions.*;

public class Reference {

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

}
