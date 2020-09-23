package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONObject;

import com.sun.jdi.InvalidTypeException;

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


	/**
	 * Employer registers complaint against any applicant
	 * @param applcnt
	 * @param message
	 * @return Complaints : The newly formed complaint
	 * @throws NullApplicantException : Thrown when a null reference applicant has been passed
	 * @throws InvalidTypeException : Thrown when, passed reference is not of Applicant type
	 */
	public Complaints complaintApplicant(User applcnt, String message) throws NullApplicantException, InvalidTypeException {

		// Basic error checking
		if(applcnt == null) {
			throw new NullApplicantException("Invalid applicant ID. Please verify applicant ID and try again.");
		}

		if(applcnt instanceof Applicant) {
			Complaints tempComplaint = new Complaints(this, (Applicant)applcnt, message);
			return tempComplaint;
		}else {
			throw new InvalidTypeException("Supplied user is not applicant");
		}
	}

	/**
	 * A employer can create a job and candidates will shortlisted
	 * @param newJob
	 * @return
	 * @throws DuplicateJobIdException
	 */
	public Job createJob(Job newJob) throws DuplicateJobIdException {

		if(this.getPostedJobs().containsKey( newJob.getJobId() ) ){
			throw new DuplicateJobIdException();
		}


		this.getPostedJobs().put(newJob.getJobId(), newJob);

		// Returning newly created job
		return this.getPostedJobs().get(newJob.getJobId());
	}

	/*
	 * Employer shortlists candidates by looking at their credentials and availability schedule
	 */

	public boolean shortListCandidate(Job jobRef, Applicant applicant) throws AlreadyPresentInYourShortListedListException, ApplicantIsBlackListedException, NullApplicantException, NullJobReferenceException {

		// Basic error checking.
		// TODO : Needs to perform more validations here like blacklisted status, current employment staus
		if(applicant == null) {
			throw new NullApplicantException("Null applicant. Please double check the applicant ID and try again.");
		}

		if(jobRef == null) {
			throw new NullJobReferenceException("Invalid Job Id. Please try again.");
		}


		HashMap<String, Applicant> shortListedApplicants = jobRef.getShortListedApplicants();

		// If an applicant is blacklisted, he/she should not be allowed to be shortlisted
		if(applicant.getBlacklistStatus() != null && applicant.getBlacklistStatus().blacklistStatus == BlacklistStatus.FULL_BLACKLISTED)
		{
			throw new ApplicantIsBlackListedException("Blacklisted applicants can't be shortlisted.");
		}

		// Add the applicant only if it is not already present
		if(! shortListedApplicants.containsKey(applicant.getId()) )
		{
			shortListedApplicants.put(applicant.getId(), applicant);
		}
		else {
			throw new AlreadyPresentInYourShortListedListException("The applicant is already present in your shortlisted applicant's list.");
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


	/**
	 * @author Yogeshwar Chaudhari
	 * Ranks the given applicant, for the specific job
	 * @param jobRef : Job to be ranked for
	 * @param applicntRef : Applicant who will be ranked
	 * @param rank : Integer number - rank
	 * @throws NullJobException : Thrown when job does not exists in the system
	 * @throws NullApplicantException : Thrown when applicant does not exists in the system
	 */
	public void rankApplicant(Job jobRef, Applicant applicntRef, int rank) throws NullJobException, NullApplicantException {

		// Basic error checking
		if(jobRef == null)
			throw new NullJobException();

		if(applicntRef == null)
			throw new NullApplicantException();

		// If the rank has already been occupied, it will be overridden.
		jobRef.rankApplicant(applicntRef, rank);
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


	public BlacklistStatus getBlacklistStat()
	{
		return this.blacklistStatus.getBlacklistStatus();
	}


	public void setBlacklistStatus(BlacklistStatus blacklistStatus)
	{
		this.blacklistStatus.setBlacklistStatus(blacklistStatus);
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

	public Date getStartDate()
	{
		return blacklistStatus.getStartDate();
	}

}
