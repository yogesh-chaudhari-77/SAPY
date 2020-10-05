package model.entities;

import java.text.SimpleDateFormat;
import java.util.*;

import model.enums.PostedJobJStatus;

public class Job {

	// System identifier
	private String jobId;

	// Job heading, A short indentifier
	private String jobTitle;

	// Short textual description about the job
	private String jobDesc;

	// When is the job posted
	private Date jobPostedDateTime;

	// Active or Inactive
	private PostedJobJStatus jobStatus;

	// List of shortlisted applicant for this job
	private HashMap<String, Applicant> shortListedApplicants;

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
		shortListedApplicants = new HashMap<String, Applicant>();
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
		shortListedApplicants = new HashMap<String, Applicant>();
		rankedApplicants = new LinkedHashMap<Integer, Applicant>();

		// 24-09-2020
		availInterviewTimings = new ArrayList<Date>();
	}


	public Job(String jobId, String jobTitle, String jobDesc) {
		this.jobId = jobId;
		this.jobTitle = jobTitle;
		this.jobDesc = jobDesc;
		this.jobPostedDateTime = new Date();
		this.jobStatus = PostedJobJStatus.ACTIVE;
		shortListedApplicants = new HashMap<String, Applicant>();
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

		this.getRankedApplicants().put(rank, applicntRef);

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

	public HashMap<String, Applicant> getShortListedApplicants() {
		return shortListedApplicants;
	}

	public void setShortListedApplicants(HashMap<String, Applicant> shortListedApplicants) {
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
}
