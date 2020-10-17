package model.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import model.enums.PostedJobJStatus;

public class Job implements Serializable {

	// System identifier
	private String jobId;

	// Job heading, A short indentifier
	private String jobTitle;

	// Short textual description about the job
	private String jobDesc;

	// ID of the Job category
	private String jobCategoryID;

	// When is the job posted
	private Date jobPostedDateTime;

	// Active or Inactive
	private PostedJobJStatus jobStatus;

	// List of shortlisted applicant for this job
	private HashMap<String, JobApplication> shortListedApplicants;

	// List of applicants with ranking
	private LinkedHashMap<Integer, Applicant> rankedApplicants;

	// Maintains the list of possible interview times
	private List<Date> availInterviewTimings;

	public Job() {
		jobId = "";
		jobTitle = "";
		jobDesc = "";
		jobPostedDateTime = new Date();
		jobStatus = PostedJobJStatus.ACTIVE;
		shortListedApplicants = new HashMap<String, JobApplication>();
		rankedApplicants = new LinkedHashMap<Integer, Applicant>();

		// 24-09-2020
		availInterviewTimings = new ArrayList<Date>();
	}

	public Job(String jobTitle, String jobDesc) {
		this.jobId =
				this.jobTitle = jobTitle;
		this.jobDesc = jobDesc;
		this.jobPostedDateTime = new Date();
		this.jobStatus = PostedJobJStatus.ACTIVE;
		shortListedApplicants = new HashMap<String, JobApplication>();
		rankedApplicants = new LinkedHashMap<Integer, Applicant>();

		// 24-09-2020
		availInterviewTimings = new ArrayList<Date>();
	}


	public Job(String jobId, String jobTitle, String jobDesc, String jobCategory) {
		this.jobId = jobId;
		this.jobTitle = jobTitle;
		this.jobDesc = jobDesc;
		this.jobCategoryID = jobCategory;
		this.jobPostedDateTime = new Date();
		this.jobStatus = PostedJobJStatus.ACTIVE;
		shortListedApplicants = new HashMap<String, JobApplication>();
		rankedApplicants = new LinkedHashMap<Integer, Applicant>();

		// 24-09-2020
		availInterviewTimings = new ArrayList<Date>();
	}


	/**
	 * This will store the ranking of the shortlisted applicants.
	 * Ranking all candidates may not be necessary. But Interview invitations must be sent to highly ranked candidates.
	 * @param applicntRef : Reference of the applicant
	 * @param rank : The rank that will be assinged to this applicant
	 */
	public void rankApplicant(Applicant applicntRef, int rank) {

		//Changed by Prodip to add jobApplication to applicant
		JobApplication applicantJobApplication = this.shortListedApplicants.get(applicntRef.getId());
		applicantJobApplication.setRank(rank);
		applicntRef.addJobApplication(applicantJobApplication);
		//this.shortListedApplicants.get(applicntRef.getId()).setRank(rank);
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
	
	public String getJobCategoryID()
	{
		return this.jobCategoryID;
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

	public Date getJobPostedDateTime() {
		return jobPostedDateTime;
	}

	public void setJobPostedDateTime(Date jobPostedDateTime) {
		this.jobPostedDateTime = jobPostedDateTime;
	}

	public PostedJobJStatus getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(PostedJobJStatus jobStatus) {
		this.jobStatus = jobStatus;
	}

	public HashMap<String, JobApplication> getShortListedApplicants() {
		return shortListedApplicants;
	}

	public void setShortListedApplicants(HashMap<String, JobApplication> shortListedApplicants) {
		this.shortListedApplicants = shortListedApplicants;
	}

	public LinkedHashMap<Integer, Applicant> getRankedApplicants() {
		return rankedApplicants;
	}

	public void setRankedApplicants(LinkedHashMap<Integer, Applicant> rankedApplicants) {
		this.rankedApplicants = rankedApplicants;
	}

	public List<Date> getAvailInterviewTimings() {
		return availInterviewTimings;
	}

	public void setAvailInterviewTimings(List<Date> availInterviewTimings) {
		this.availInterviewTimings = availInterviewTimings;
	}

	@Override
	public String toString() {
		return "Job [jobId=" + jobId + ", jobTitle=" + jobTitle + ", jobDesc=" + jobDesc + ", jobPostedDateTime="
				+ jobPostedDateTime + ", jobStatus=" + jobStatus + "]";
	}

	/**
	 * 17-10-2020 - Adds the applicant to the jobs shortlisted members list
	 * @param applicant
	 */
	public JobApplication shortListApplicant(Applicant applicant) {
		JobApplication ja = new JobApplication(this, applicant);
		shortListedApplicants.put(applicant.getId(), ja);
		return ja;
	}
}
