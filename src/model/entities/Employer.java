package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONObject;

/*
 * Class implements all employer related functionality
 */

public class Employer extends User {

	private String companyName = null;
	private String address = null;
	private String weburl = null;
	private String contactEmail = null;	


	// List of applicants shortlisted and hired by this employer
	private HashMap<String, Applicant> myApplicantsList = new HashMap<String, Applicant>();

	// List of jobs posted by this employer. Draft
	private HashMap<String, Job> postedJobs = new HashMap<String, Job>();

	private Blacklist blacklistStatus = new Blacklist();

	private int complaintsCount = 0;


	/*
	 * Default constructor
	 */
	public Employer(String id, String userEmail, String password, String firstName, String lastName, String phoneNumber)
	{
		super(id, userEmail, password, firstName, lastName, phoneNumber);
		blacklistStatus.setBlacklistStatus(BlacklistStatus.NOT_BLACKLISTED);
	}

	/*
	 * 
	 */
	public Employer(String id, String userEmail, String password, String companyName)
	{
		super(id, userEmail, password);
		this.companyName = companyName;
		blacklistStatus.setBlacklistStatus(BlacklistStatus.NOT_BLACKLISTED);
	}

	/*
	 * Employer can update his own details
	 * @param : JSON object containing all details
	 */

	public Employer updateDetails(JSONObject updatedEmployerDetails) {

		this.setCompanyName( updatedEmployerDetails.getString("companyName") );
		this.setContactEmail(updatedEmployerDetails.getString("contactEmail") );

		return this; 
	}


	/*
	 * Employer can search throught the applicant list
	 * Employer can also specify the availability and job preference requirements
	 */
	public HashMap<String, Applicant> search(HashMap<String, Applicant> allApplicantsList, String [] searchJobPreference) {

		HashMap<String, Applicant> searchResults = new HashMap<String, Applicant>();


		// Iterating over the applicant's list to find the matching candidates 
		for(Applicant applicantRef : allApplicantsList.values()) {

			// Check the preferences and availability
			if(checkAppcntJobPreference(applicantRef, searchJobPreference) && checkAvailability(applicantRef)) {

				// Matching preference and availability
				searchResults.put(applicantRef.getId(), applicantRef);
			}
		}

		return searchResults;
	}
	// End of search();

	/**
	 * Checks if the applicant has desired job preference
	 * @param applicantRef
	 * @return
	 */
	public boolean checkAppcntJobPreference(Applicant applicantRef, String [] searchJobPreference) {

		// Check for each job preference supplied in search criteria
		for(int i = 0; i < searchJobPreference.length; i++) {

			// Check if applicant's job preferences matches with search criteria
			if(applicantRef.getJobPreferences().contains( searchJobPreference[i] )) {

				// match found
				return true;
			}
		}

		return false;

	} // end of checkAppcntJobPreference()

	/**
	 * Checks the applicant is available at the times, employer desires to be
	 * TODO : to be implemented
	 * @param applicantRef
	 * @return
	 */
	public boolean checkAvailability(Applicant applicantRef) {

		return true;
	}


	//	public boolean complaintApplicant(Applicant applcnt, String message) {
	//		
	//		// Basic error checking
	//		if(applcnt == null) {
	//			throw new NullApplicantException();
	//		}
	//		
	//		Complaints tempComplaint = new Complaints(this, applcnt, message);
	//		return tempComplaint;
	//	}

	/**
	 * A employer can create a job and candidates will shortlisted
	 * @param newJob
	 * @return
	 */
	public void createJob(Job newJob) {

		this.getPostedJobs().put(newJob.getJobId(), newJob);
	}

	/*
	 * Employer shortlists candidates by looking at their credentials and availability schedule
	 */

	public boolean shortListCandidate(Applicant applicant) throws AlreadyPresentInYourShortListedListException, ApplicantIsBlackListedException, NullApplicantException {

		// Basic error checking. 
		// TODO : Needs to perform more validations here like blacklisted status, current employment staus
		if(applicant == null) {
			throw new NullApplicantException();
		}


		HashMap<String, Applicant> shortListedApplicants = this.getMyApplicantsList();

		// If an applicant is blacklisted, he/she should not be allowed to be shortlisted
		if( applicant.getBlacklistStatus().blacklistStatus == BlacklistStatus.FULL_BLACKLISTED)
		{
			throw new ApplicantIsBlackListedException();
		}

		// Add the applicant only if it is not already present
		if(! shortListedApplicants.containsKey(applicant.getId()) ) 
		{
			shortListedApplicants.put(applicant.getId(), applicant);
		}
		else {
			throw new AlreadyPresentInYourShortListedListException();
		}

		return true;
	}

	/*
	 *	Employer sends email notification to shortlisted applicants 
	 */

	public boolean sendEmailNotification(Applicant applicant, EmailNotificationType notificationType) {

		if(notificationType == EmailNotificationType.SUCCESSFULLY_HIRED) {
			System.out.println("Congratulations "+applicant.getFirstName()+"! We are pleased to extend formal offer to you.");
		}

		return true;
	}


	/*
	 * Employer fixes interview time by looking at availability schedules of applicants/student
	 * TODO : Scheduled implementation
	 */

	public boolean setInterviewTime() {
		System.out.println("Interview has been scheduled");
		return true;
	}


	/*
	 * Employer saves the interview results. All records are written to the file for future reference
	 * TODO : Scheduled implementation
	 */
	public boolean saveInterviewResults() {

		System.out.println("Interview results have been sasved");
		return true;
	}


	/*
	 * Employer can change the employment status of the applicant / student. 
	 */
	public boolean changeApplicantStatus(Applicant applicant, EmploymentStatus newStatus) throws ApplicantNotPresentInMyApplicantsException, NullApplicantException {

		// Basic error checking. 
		// TODO : Needs to perform more validations here like blacklisted status, current employment staus
		if(applicant == null) {
			throw new NullApplicantException();
		}

		HashMap<String, Applicant> myApplicantsList = this.getMyApplicantsList();


		// Make sure the employer has access to this applicant details
		if(myApplicantsList.containsKey( applicant.getId() )) {

			// TODO : More error checking needs to be done here. Scheduled
			myApplicantsList.put(applicant.getId(), applicant);

		}else {

			throw new ApplicantNotPresentInMyApplicantsException();
		}

		return true;
	}


	/**
	 * Checks if the employer is blacklisted or not
	 * True means black listed and false means not
	 * @return : Boolean
	 */
	public boolean isBlackListed() {
		if( this.blacklistStatus.getBlacklistStatus() == BlacklistStatus.PROVISIONAL_BLACKLISTED) {
			return true;
		}else if( this.blacklistStatus.getBlacklistStatus() == BlacklistStatus.FULL_BLACKLISTED) {
			return true;
		}

		return false;
	}
	/*
	 * Getter setters starts here
	 */

	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getWeburl() {
		return weburl;
	}


	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}


	public String getContactEmail() {
		return contactEmail;
	}


	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}


	public HashMap<String, Applicant> getMyApplicantsList() {
		return myApplicantsList;
	}


	public void setMyApplicantsList(HashMap<String, Applicant> myApplicantsList) {
		this.myApplicantsList = myApplicantsList;
	}

	public HashMap<String, Job> getPostedJobs() {
		return postedJobs;
	}


	public void setPostedJobs(HashMap<String, Job> postedJobs) {
		this.postedJobs = postedJobs;
	}


	public Blacklist getBlacklistStatus() {
		return blacklistStatus;
	}

	public void setBlacklistStatus(Blacklist blacklistStatus) {
		this.blacklistStatus = blacklistStatus;
	}

	public int getComplaintsCount() {
		return complaintsCount;
	}


	public void setComplaintsCount(int complaintsCount) {
		this.complaintsCount = complaintsCount;
	}


	//Blacklisting the employer by setting type to 'P' or 'F'
	public void setBlacklistStatus(String type)
	{
		blacklistStatus.setBlacklistStatus(type);
	}


	//Reactivating the blacklisted employer
	public void removeBlacklistStatus()
	{
		blacklistStatus.removeBlacklistStatus();
	}
	
	
}
