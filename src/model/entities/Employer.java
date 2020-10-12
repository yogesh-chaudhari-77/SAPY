package model.entities;

//import com.mashape.unirest.http.exceptions.UnirestException;
import customUtils.EmailUtil;
import model.enums.*;
import model.exceptions.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

//import org.apache.commons.lang3.StringEscapeUtils;

import org.json.JSONObject;

import com.sun.jdi.InvalidTypeException;

//import javax.mail.MessagingException;
import javax.management.InvalidApplicationException;

/*
 * Class implements all employer related functionality
 */

public class Employer extends User {

	private String companyName = null;
	private String address = null;
	private String weburl = null;
	//private String contactEmail = null;


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
		//this.setContactEmail(updatedEmployerDetails.getString("contactEmail") );

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
	public Complaints complaintApplicant(Applicant applcnt, String message) throws NullApplicantException, InvalidTypeException {

		// Basic error checking
		if(applcnt == null) {
			throw new NullApplicantException("Invalid applicant ID. Please verify applicant ID and try again.");
		}

		Complaints tempComplaint = new Complaints(this, applcnt, message);
		return tempComplaint;
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
		if(applicant == null) {
			throw new NullApplicantException("Null applicant. Please double check the applicant ID and try again.");
		}

		if(jobRef == null) {
			throw new NullJobReferenceException("Invalid Job Id. Please try again.");
		}


		HashMap<String, JobApplication> shortListedApplicants = jobRef.getShortListedApplicants();

		// If an applicant is blacklisted, he/she should not be allowed to be shortlisted
		if(applicant.getBlacklistStatus() != null && applicant.getBlacklistStatus().blacklistStatus == BlacklistStatus.FULL_BLACKLISTED)
		{
			throw new ApplicantIsBlackListedException("Blacklisted applicants can't be shortlisted.");
		}

		// Add the applicant only if it is not already present
		if(! shortListedApplicants.containsKey(applicant.getId()) )
		{
			shortListedApplicants.put(applicant.getId(), new JobApplication(jobRef, applicant));
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


	/**
	 * Employer can record interview result, either success or failed
	 * @param jobRef
	 * @param applicant
	 * @param result
	 * @throws InvalidApplicationException
	 * @throws NullApplicantException
	 * @throws NullEntityException
	 */
	public void recordInterviewResults(Job jobRef, Applicant applicant, String result) throws InvalidApplicationException, NullApplicantException, NullEntityException {

		// Null applicant check
		if (applicant == null){
			throw new NullApplicantException("Invalid applicant ID :");
		}

		// Applicant not present in the shortlisted applicants list of the job
		if( ! jobRef.getShortListedApplicants().containsKey(applicant.getId()) ){
			throw new InvalidApplicationException(applicant.getId()+" has not been shortlisted for this job");
		}

		Interview interviewRef = jobRef.getShortListedApplicants().get(applicant.getId()).getInterviewRef();

		if(interviewRef == null){
			throw new NullEntityException("Can't find any interview for this applicant");
		}

		interviewRef.setResult(result);
	}


	/**
	 * Employer can create job offer for applicants.
	 * Select Job -> Enter applicant ID -> Offer created
	 * Applicant then need to accept or reject the offer
	 * @param jobRef
	 * @param applicant
	 * @throws InvalidApplicationException
	 * @throws NullApplicantException
	 * @throws NullEntityException
	 */
	public void createJobOffer(Job jobRef, Applicant applicant) throws InvalidApplicationException, NullApplicantException, NullEntityException {

		// Null applicant check
		if (applicant == null){
			throw new NullApplicantException("Invalid applicant ID :");
		}

		// Applicant not present in the shortlisted applicants list of the job
		if( ! jobRef.getShortListedApplicants().containsKey(applicant.getId()) ){
			throw new InvalidApplicationException(applicant.getId()+" has not been shortlisted for this job");
		}

		JobApplication jobApp = jobRef.getShortListedApplicants().get(applicant.getId());

		if(jobApp == null){
			throw new NullEntityException("No job applicantion against given applicant.");
		}

		jobApp.setOfferRef( new JobOffer("Congratulations ! "+this.getCompanyName()+" is happy to onboard you. Please read the employment details carefully before accepting / rejecting the offer.") );
	}


	/**
	 * Employer can blacklist an applicant, either provisionally or fully.
	 * @param applicant
	 * @param blacklistType
	 */
	public void blacklistApplicant(Applicant applicant, BlacklistStatus blacklistType){

		if(applicant != null){
			if(blacklistType instanceof BlacklistStatus){
				applicant.setBlacklistStatus( blacklistType );
			}else{
				System.err.println("Unexpected Blacklist type");
			}
		}else{
			System.err.println("Applicant is Null");
		}
	}


	/**
	 * Employer can send interview invites to the applicant,
	 * This invite is limited to per job and is sent automatically when employer ranks applicant for the job
	 * @param jobRef
	 * @param appRef
	 */
	public void sendInterviewInvite(Job jobRef, Applicant appRef) {

		String subject = "Applicant Update";
		String toEmail = appRef.getUserEmail().trim().strip();
		String toName = appRef.getFirstName();

//		StringBuilder matter = new StringBuilder();
//		matter.append("<p>Hello "+appRef.getFirstName()+",<BR/><BR/>");
//		matter.append("You have been shortlisted for the interview at "+this.companyName+"<BR/>");
//		matter.append("Job Title "+jobRef.getJobTitle()+"<BR/>");
//		matter.append("Desc "+jobRef.getJobDesc()+"<BR/>");
//		matter.append("You are among "+jobRef.getShortListedApplicants().size()+" first shortlisted applicants<BR/>");
//
//		matter.append("Please select any of the possible interview times from your SAPY dashboard.<BR/>");
//
//		matter.append("<ul>");
//		for(Date date : jobRef.getAvailInterviewTimings()){
//			matter.append("<li>"+date.toLocaleString()+"<li>");
//		}
//		matter.append("</ul>");
//		matter.append("</br> We wish you all the best.");
//		matter.append("</p>");

		StringBuilder matter = new StringBuilder();
		matter.append("Hello "+appRef.getFirstName()+",");
		matter.append("You have been shortlisted for the interview at "+this.companyName+"");
		matter.append("Job Title "+jobRef.getJobTitle()+"");
		matter.append("Desc "+jobRef.getJobDesc()+"");
		matter.append("You are among "+jobRef.getShortListedApplicants().size()+" first shortlisted applicants");

		matter.append("Please select any of the possible interview times from your SAPY dashboard.");

		matter.append("");
		for(Date date : jobRef.getAvailInterviewTimings()){
			matter.append(""+date.toLocaleString()+"");
		}
		matter.append("");
		matter.append("We wish you all the best.");
		matter.append("");
		matter.append("Best Regards,");
		matter.append("Team SAPY");

//		EmailObject newEmail = new EmailObject(toEmail, toName, subject, StringEscapeUtils.escapeHtml4(matter.toString()));
//		try {
//			EmailUtil.sendEmail(newEmail);
//			System.out.println(newEmail);
//		} catch (MessagingException | UnirestException e) {
//			e.printStackTrace();
//		}
	}

	/**
	 * Sends out offer letter, with employment details to the applicant, on being successfully accepted.
	 * @param jobRef - For which job the offer letter is being sent
	 * @param appRef - The applicant to which it will be sent
	 */
	public void sendJobOfferEmail(Job jobRef, Applicant appRef) {

		String subject = "Applicant Update - Job Offer";
		String toEmail = appRef.getUserEmail().trim().strip();
		String toName = appRef.getFirstName();

		StringBuilder matter = new StringBuilder();
		matter.append("Hello "+appRef.getFirstName()+", ");
		matter.append("Congratulations ! ");
		matter.append("Please find employment details below. ");
		matter.append("Job Title "+jobRef.getJobTitle()+". ");
		matter.append("Desc "+jobRef.getJobDesc()+". ");
		matter.append("");

		matter.append("Your employemnt Status : PENDING");
		matter.append("Please either accept or reject this offer from your SAPY dashboard. You will not shortlisted for other jobs until you change update your employment status.");

		matter.append("We wish you all the best. ");
		matter.append("");
		matter.append("Best Regards, ");
		matter.append("Team SAPY ");

//		EmailObject newEmail = new EmailObject(toEmail, toName, subject, StringEscapeUtils.escapeHtml4(matter.toString()));
//		try {
//			EmailUtil.sendEmail(newEmail);
//			System.out.println(newEmail);
//		} catch (MessagingException | UnirestException e) {
//			e.printStackTrace();
//		}
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


//	public String getContactEmail() {
//		return contactEmail;
//	}
//
//
//	public void setContactEmail(String contactEmail) {
//		this.contactEmail = contactEmail;
//	}


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
	public void setBlacklistStatus(String type) throws ParseException
	{
		blacklistStatus.setBlacklistStatus(type);
	}


	//Reactivating the blacklisted employer
	public void removeBlacklistStatus()
	{
		blacklistStatus.removeBlacklistStatus();
	}
	
	public Date getBlacklistStartDate()
	{
		return blacklistStatus.getStartDate();
	}
	
	public void setBlacklistStartDate(Date startDate) throws ParseException
	{
		blacklistStatus.setStartDate(startDate);
	}

}
