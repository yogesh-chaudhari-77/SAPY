package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.text.SimpleDateFormat;
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

    public boolean getCurrentCompany(){
        return currentCompany;
    }

    @Override
    public String toString() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String sDate = formatter.format(startDate);
        String eDate;
        if(currentCompany) {
            eDate="";
        }else{
            eDate = formatter.format(endDate);
        }

        return "Company Name:\t" + companyName + "\n" +
                "Designation:\t" + designation + "\n" +
                "Start Date:\t" + sDate + "\n"+
                "EndDate:\t" + eDate + "\n" +
                "CurrentCompany:\t" + (currentCompany?"Yes":"No")+"\n";
    }
}
