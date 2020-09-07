package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.util.Date;

public class Qualification {

    private String qualificationLevel;
    private Date startDate;
    private Date endDate;
    private String fieldOfStudy;
    private double marksObtained;

    public Qualification(String qualificationLevel, Date startDate, Date endDate, String fieldOfStudy, double marksObtained) {
        this.qualificationLevel = qualificationLevel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fieldOfStudy = fieldOfStudy;
        this.marksObtained = marksObtained;
    }

    public String getQualificationLevel() {
        return qualificationLevel;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
