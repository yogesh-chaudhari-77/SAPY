package model.entities;

import model.enums.ApplicationStatus;

import java.util.Date;

/**
 * @author Yogeshwar Chaudhari
 * Model class implementing logic for job application class.
 * For every candidate shortlisted by the employer, a job application is created.
 */
public class JobApplication {

    Job jobRef;
    Applicant applicantRef;
    Date dateAdded;
    ApplicationStatus applnStatus;
    int rank;
    Interview interviewRef = null;
    JobOffer offerRef = null;

    // Constructor
    public JobApplication(Job jobRef, Applicant appRef){
        this.jobRef = jobRef;
        this.applicantRef = appRef;
        dateAdded = new Date();
        applnStatus = ApplicationStatus.ONGOING;
        rank = -1;
    }

    // Getter-Setters starts here

    public Job getJobRef() {
        return jobRef;
    }

    public void setJobRef(Job jobRef) {
        this.jobRef = jobRef;
    }

    public Applicant getApplicantRef() {
        return applicantRef;
    }

    public void setApplicantRef(Applicant applicantRef) {
        this.applicantRef = applicantRef;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public ApplicationStatus getApplnStatus() {
        return applnStatus;
    }

    public void setApplnStatus(ApplicationStatus applnStatus) {
        this.applnStatus = applnStatus;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Interview getInterviewRef() {
        return interviewRef;
    }

    public void setInterviewRef(Interview interviewRef) {
        this.interviewRef = interviewRef;
    }

    public JobOffer getOfferRef() {
        return offerRef;
    }

    public void setOfferRef(JobOffer offerRef) {
        this.offerRef = offerRef;
    }

    // Gettee-Setters ends here


    @Override
    public String toString() {
        return "JobApplication{" +
                "jobRef=" + jobRef +
                ", applicantRef=" + applicantRef +
                ", dateAdded=" + dateAdded +
                ", applnStatus=" + applnStatus +
                ", rank=" + rank +
                '}';
    }
}
