package model.entities;

import java.util.Date;

/**
 * @author Yogeshwar Chaudhari
 * Applicant can create interview after selected one of the many available times provided by employer
 */
public class Interview {

    String result;
    Date interviewDate;


    public Interview(Date interviewDate){
        result = "";
        this.interviewDate = interviewDate;
    }


    // Accessor methods starts here
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(Date interviewDate) {
        this.interviewDate = interviewDate;
    }
    // Accessor methods ends here
}
