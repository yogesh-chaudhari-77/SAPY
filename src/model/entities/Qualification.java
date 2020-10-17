package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Qualification implements Serializable {

    private final String qualificationLevel;
    private final Date startDate;
    private final Date endDate;
    private final String fieldOfStudy;
    private final double marksObtained;

    public Qualification(String qualificationLevel, Date startDate, Date endDate, String fieldOfStudy, double marksObtained) {
        this.qualificationLevel = qualificationLevel;
        this.startDate = startDate;
        this.endDate = endDate;
        this.fieldOfStudy = fieldOfStudy;
        this.marksObtained = marksObtained;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
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

    public double getMarksObtained() { return marksObtained; }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String sDate = dateFormat.format(startDate);
        String eDate = dateFormat.format(endDate);

        return
                "\nQualification Level = " + qualificationLevel +
                "\nStart Date = " + sDate +
                "\nEnd Date = " + eDate +
                "\nField Of Study = " + fieldOfStudy +
                "\nMarks Obtained = " + marksObtained;
    }
}
