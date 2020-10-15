package model.entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.enums.AvailabilityType;


public class UserAvailability {


    // 23-09-2020 - Multiple job categories can be specified by applicant
    private List<JobCategory> applicableJobCategories = new ArrayList<JobCategory>();;

    private AvailabilityType availabilityType;
    private int noOfHoursAWeek;

    // Added by Yogeshwar on 23-09-2020 - After discussion with Pradip
    // User availability also needs to store the period for which this is available
    private Date periodStartDate = null;
    private Date periodEndDate = null;

//    public UserAvailability(JobCategory applicableJobCategory, AvailabilityType availabilityType, int noOfHoursAWeek) {
//        this.availabilityType = availabilityType;
//        this.noOfHoursAWeek = noOfHoursAWeek;
//    }


    /**
     * Constructor with all fields
     */
    public UserAvailability(List<JobCategory> applicableJobCategory, AvailabilityType availabilityType, int noOfHoursAWeek, Date periodStartDate, Date periodEndDate) {
        this.availabilityType = availabilityType;
        this.noOfHoursAWeek = noOfHoursAWeek;
        this.periodStartDate = periodStartDate;
        this.periodEndDate = periodEndDate;
        this.applicableJobCategories = applicableJobCategory;
    }

    public int getNoOfHoursAWeek() {
        return noOfHoursAWeek;
    }

    public void setNoOfHoursAWeek(int noOfHoursAWeek) {
        this.noOfHoursAWeek = noOfHoursAWeek;
    }

    public AvailabilityType getAvailabilityType() {
        return availabilityType;
    }

    public void setAvailabilityType(AvailabilityType availabilityType) {
        this.availabilityType = availabilityType;
    }


    public List<JobCategory> getApplicableJobCategories() {
        return applicableJobCategories;
    }


    public void setApplicableJobCategories(List<JobCategory> applicableJobCategories) {
        this.applicableJobCategories = applicableJobCategories;
    }


    public Date getPeriodStartDate() {
        return periodStartDate;
    }


    public void setPeriodStartDate(Date periodStartDate) {
        this.periodStartDate = periodStartDate;
    }


    public Date getPeriodEndDate() {
        return periodEndDate;
    }


    public void setPeriodEndDate(Date periodEndDate) {
        this.periodEndDate = periodEndDate;
    }



    /**
     * 23-09-2020
     * @author Yogeshwar Chaudhari
     * @return : only ids of categories that are applicable to this availability
     */
    public List<String> getApplicableJobCategoriesIds() {

        List<String> onlyIds = new ArrayList<String>();

        for(int i = 0; i < this.getApplicableJobCategories().size(); i++) {
            onlyIds.add(this.getApplicableJobCategories().get(i).getId());
        }

        return onlyIds;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String sDate = dateFormat.format(periodStartDate);
        String eDate = dateFormat.format(periodEndDate);

        return
                "Applicable Job Categories = " + applicableJobCategories +
                "\nAvailability Type = " + availabilityType +
                "\nNumber Of HoursAWeek = " + noOfHoursAWeek +
                "\nPeriod Start Date = " + sDate +
                "\nPeriod End Date = " + eDate;
    }
}
