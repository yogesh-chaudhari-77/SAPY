package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.util.Date;

public class EmploymentRecord {

    private String companyName;
    private String designation;
    private Date startDate;
    private Date endDate;
    private boolean currentCompany;

    public EmploymentRecord(String companyName, String designation, Date startDate, Date endDate, boolean currentCompany) {
        this.companyName = companyName;
        this.designation = designation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentCompany = currentCompany;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Date getStartDate() { return startDate; }

    public Date getEndDate() {
        return endDate;
    }

}
