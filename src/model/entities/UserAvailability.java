package model.entities;

import model.enums.AvailabilityType;


public class UserAvailability {
    private JobCategory applicableJobCategory;
    private AvailabilityType availabilityType;
    private int noOfHoursAWeek;

    public UserAvailability(JobCategory applicableJobCategory, AvailabilityType availabilityType, int noOfHoursAWeek) {
        this.applicableJobCategory = applicableJobCategory;
        this.availabilityType = availabilityType;
        this.noOfHoursAWeek = noOfHoursAWeek;
    }

    public JobCategory getApplicableJobCategory() {
        return applicableJobCategory;
    }

    public void setApplicableJobCategory(JobCategory applicableJobCategory) {
        this.applicableJobCategory = applicableJobCategory;
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


}
