package testing;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import model.entities.*;
import model.enums.*;
import model.exceptions.*;

public class TestEmployer {

	private Employer employer;
	private Applicant applicant;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		employer = new Employer("E1", "employer@gmail.com", "qwerty", "Sample", "Employer", "123456789");
		//applicant = new Applicant("Sample", "User", "sampleuser@gmail.com", "international");
	}

	@After
	public void tearDown() throws Exception {
	}

	
	/*
	 * Positive test case
	 * Shortlist an applicant. Applicant is not present hence will be shortlisted
	 */
	
	@Test
	public void test_shortListApplicant() throws AlreadyPresentInYourShortListedListException, ApplicantIsBlackListedException, NullApplicantException {
		
		applicant.setId("A1");
		
		// Making sure that there were no Applicant in the list
		Assert.assertEquals(0, this.employer.getMyApplicantsList().size());
		
		this.employer.shortListCandidate(applicant);
		
		// Testing whether the employer has been added to the list
		Assert.assertEquals(1, this.employer.getMyApplicantsList().size());
	}
	
	
	/*
	 * Negative test case
	 * Blacklisted applicant is being shortlisted
	 * System throws ApplicantIsBlackListed exception
	 */
	
	@Test (expected = ApplicantIsBlackListedException.class)
	public void test_shortListBlackListedApplicant() throws AlreadyPresentInYourShortListedListException, ApplicantIsBlackListedException, NullApplicantException {
		
		applicant.setId("A1");
		/*
		Change this assignment as Blacklist status is a class in Applicant
		 */
		//applicant.setBlacklistStatus(BlacklistStatus.FULL_BLACKLISTED);
		
		this.employer.shortListCandidate(applicant);
	}
	
	
	/*
	 * Negative test case
	 * Applicant already present in employers shortlisted list. Still is being shortlisted again.
	 * System throws AlreadyPresentInYourShortListedList exception
	 */
	
	@Test (expected = AlreadyPresentInYourShortListedListException.class)
	public void test_shortListAlreadyPresentApplicant() throws AlreadyPresentInYourShortListedListException, ApplicantIsBlackListedException, NullApplicantException {
		
		// A1 is being shortlisted. 
		applicant.setId("A1");
		System.out.println(applicant.getId());
		this.employer.shortListCandidate(applicant);
		
		// Pushing A1 again. Throws exception
		this.employer.shortListCandidate(applicant);
	}
	
	
	/*
	 * Positive test case
	 * An email notication will be sent to the applicant.
	 * Expecting true status if no problem occured while sending the mail
	 */
	@Test
	public void test_sendEmailNotification() {
		
		applicant.setId("A1");
		boolean status = this.employer.sendEmailNotification(applicant, EmailNotificationType.SUCCESSFULLY_HIRED);
		Assert.assertTrue( status );
	}
	
	
	/*
	 * Positive test case
	 * Applicant be hired. 
	 */
	@Test
	public void test_hireApplicant() throws ApplicantNotPresentInMyApplicantsException, AlreadyPresentInYourShortListedListException, ApplicantIsBlackListedException, NullApplicantException {
		
		applicant.setId("A1");
		employer.shortListCandidate(applicant);
		employer.changeApplicantStatus(applicant, EmploymentStatus.EMPLOYED);
	}
	
	
	/*
	 * Negative test case
	 * Applicant is not present in employer's list. 
	 * Exception will be thrown as employer does not have access to this applicant
	 */
	@Test (expected = ApplicantNotPresentInMyApplicantsException.class)
	public void test_changeApplicantStatus() throws ApplicantNotPresentInMyApplicantsException, NullApplicantException {
		applicant.setId("A1");
		employer.changeApplicantStatus(applicant, EmploymentStatus.EMPLOYED);
	}
	

	/*
	 * Negative test case
	 * Testing error checking for null applicant object is passed to employer methods
	 */
	@Test (expected = NullApplicantException.class)
	public void test_changeApplicantStatusOfNullApplicant() throws ApplicantNotPresentInMyApplicantsException, NullApplicantException, AlreadyPresentInYourShortListedListException, ApplicantIsBlackListedException {
		
		employer.shortListCandidate(null);
		employer.changeApplicantStatus(null, EmploymentStatus.EMPLOYED);
	}
	
	
	/*
	 * Negative Test Case
	 * Calling scheduling interview time.
	 * TODO : Logic of this method yet to be implementated
	 */
	@Test
	public void test_setInterviewTime() {
		
		boolean status = employer.setInterviewTime();
		Assert.assertTrue(false);
	}
	
	
	/*
	 * Negative Test Case
	 * Saving interview results
	 * TODO : Logic of this method yet to be implementated
	 */
	@Test
	public void test_saveInterviewResults() {
		
		boolean status = employer.saveInterviewResults();
		Assert.assertTrue(false);
	}
	
	
	/*
	 * Positive Test Case
	 * Calling scheduling interview time.
	 * TODO : Logic of this method yet to be implementated
	 */
	@Test
	public void test_setInterviewTimeP() {
		
		boolean status = employer.setInterviewTime();
		Assert.assertTrue(true);
	}
	
	
	/*
	 * Positive Test Case
	 * Saving interview results
	 * TODO : Logic of this method yet to be implementated
	 */
	@Test
	public void test_saveInterviewResultsP() {
		
		boolean status = employer.saveInterviewResults();
		Assert.assertTrue(true);
	}
}
