package model.entities;

import model.enums.AvailabilityType;

import java.util.HashMap;
import java.util.List;

public class UserAvailability {
    private JobCategory applicableJobCategory;
    private AvailabilityType availabilityType;
    private HashMap<String,Integer> period;

    public UserAvailability(JobCategory applicableJobCategory, AvailabilityType availabilityType, HashMap<String, Integer> period) {
        this.applicableJobCategory = applicableJobCategory;
        this.availabilityType = availabilityType;
        this.period = period;
    }

    public JobCategory getApplicableJobCategory() {
        return applicableJobCategory;
    }

    public void setApplicableJobCategory(JobCategory applicableJobCategory) {
        this.applicableJobCategory = applicableJobCategory;
    }

    public AvailabilityType getAvailabilityType() {
        return availabilityType;
    }

    public void setAvailabilityType(AvailabilityType availabilityType) {
        this.availabilityType = availabilityType;
    }

    public HashMap<String, Integer> getPeriod() {
        return period;
    }

    public void setPeriod(HashMap<String, Integer> period) {
        this.period = period;
    }
}
