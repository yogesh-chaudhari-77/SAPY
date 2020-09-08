package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Job {
	
	// System identifier
	private String jobId;
	
	// Job heading, A short indentifier
	private String jobTitle;
	
	// Short textual description about the job
	private String jobDesc;			
	
	// When is the job posted
	private SimpleDateFormat jobPostedDateTime;		
	
	// Active or Inactive
	private PostedJobJStatus jobStatus;
	
	
	// List of shortlisted applicant for this job
	private HashMap<String, Applicant> shortListedApplicants;
	
	
	public Job() {
		jobId = "";
		jobTitle = "";
		jobDesc = "";
		jobPostedDateTime = new SimpleDateFormat();
		jobStatus = PostedJobJStatus.ACTIVE;
		shortListedApplicants = new HashMap<String, Applicant>();
	}
	
	public Job(String jobTitle, String jobDesc) {
		this.jobId = 
		this.jobTitle = jobTitle;
		this.jobDesc = jobDesc;
		this.jobPostedDateTime = new SimpleDateFormat();
		this.jobStatus = PostedJobJStatus.ACTIVE;
		shortListedApplicants = new HashMap<String, Applicant>();
	}

	
	public Job(String jobId, String jobTitle, String jobDesc) {
		this.jobId = jobId;
		this.jobTitle = jobTitle;
		this.jobDesc = jobDesc;
		this.jobPostedDateTime = new SimpleDateFormat();
		this.jobStatus = PostedJobJStatus.ACTIVE;
		shortListedApplicants = new HashMap<String, Applicant>();
	}
	
	// Getter - Setters Starts Here
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	public SimpleDateFormat getJobPostedDateTime() {
		return jobPostedDateTime;
	}

	public void setJobPostedDateTime(SimpleDateFormat jobPostedDateTime) {
		this.jobPostedDateTime = jobPostedDateTime;
	}

	public PostedJobJStatus getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(PostedJobJStatus jobStatus) {
		this.jobStatus = jobStatus;
	}

	public HashMap<String, Applicant> getShortListedApplicants() {
		return shortListedApplicants;
	}

	public void setShortListedApplicants(HashMap<String, Applicant> shortListedApplicants) {
		this.shortListedApplicants = shortListedApplicants;
	}

	@Override
	public String toString() {
		return "Job [jobId=" + jobId + ", jobTitle=" + jobTitle + ", jobDesc=" + jobDesc + ", jobPostedDateTime="
				+ jobPostedDateTime + ", jobStatus=" + jobStatus + "]";
	}
	
	
}
