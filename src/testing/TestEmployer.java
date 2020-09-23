package testing;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.SystemHandler;
import model.entities.*;
import model.enums.*;
import model.exceptions.*;

public class TestEmployer {

	private Employer employer;
	private Applicant applicant;
	private SystemHandler sysHandler;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

		sysHandler = new SystemHandler();
		sysHandler.loadDummyDataForEmployeFunctions();

		// Creating an employer
		employer = new Employer("E1", "employer@gmail.com", "qwerty", "Sample", "Employer", "123456789");

		// With certain jobs
		employer.createJob(new Job("job1", "Developer", "Developer Desc"));
		employer.createJob(new Job("job2", "Analyst", "Analyst Required"));
		employer.createJob(new Job("job3", "Designer", "Designer Required"));

		// Creating an applicant
		applicant = new Applicant("A1", "a@gmail.com", "123", "John", "Doe", "048888888", "l");
	}

	@After
	public void tearDown() throws Exception {
	}


	/*
	 * Positive test case
	 * Shortlist an applicant. Applicant is not present hence will be shortlisted
	 */

	@Test
	public void test_shortListApplicant() throws AlreadyPresentInYourShortListedListException, ApplicantIsBlackListedException, NullApplicantException, NullJobReferenceException {

		Applicant applicntRef = this.applicant;
		Job jobRef = employer.getPostedJobs().get("job1");

		// Making sure that there were no Applicant in the list
		int initialSize = jobRef.getShortListedApplicants().size();

		this.employer.shortListCandidate(jobRef, applicntRef);

		// Testing if the applicant has really been added to shortlisted list
		Assert.assertEquals(initialSize + 1, jobRef.getShortListedApplicants().size());
	}


	/*
	 * Negative test case
	 * Blacklisted applicant is being shortlisted
	 * System throws ApplicantIsBlackListed exception
	 */

	@Test (expected = ApplicantIsBlackListedException.class)
	public void test_shortListBlackListedApplicant() throws AlreadyPresentInYourShortListedListException, ApplicantIsBlackListedException, NullApplicantException, NullJobReferenceException {

		// Job Reference Against which we are shortlisting
		Job jobRef = employer.getPostedJobs().get("job1");

		// Fully blacklisting an applicant
		Blacklist b = new Blacklist();
		b.setBlacklistStatus("F");

		applicant.setBlacklistStatus( "F" );

		this.employer.shortListCandidate(jobRef, applicant);
	}


	/*
	 * Negative test case
	 * Applicant already present in employers shortlisted list. Still is being shortlisted again.
	 * System throws AlreadyPresentInYourShortListedList exception
	 */

	@Test (expected = AlreadyPresentInYourShortListedListException.class)
	public void test_shortListAlreadyPresentApplicant() throws AlreadyPresentInYourShortListedListException, ApplicantIsBlackListedException, NullApplicantException, NullJobReferenceException {

		Applicant applicntRef = this.applicant;
		Job jobRef = employer.getPostedJobs().get("job1");


		System.out.println(applicntRef.getId());

		this.employer.shortListCandidate(jobRef, applicant);

		// Pushing same applicant again. Throws exception
		this.employer.shortListCandidate(jobRef, applicant);
	}



	/*
	 * Negative test case
	 * Testing error checking for null applicant object is passed to employer methods
	 */
	@Test (expected = NullApplicantException.class)
	public void test_changeApplicantStatusOfNullApplicant() throws ApplicantNotPresentInMyApplicantsException, NullApplicantException, AlreadyPresentInYourShortListedListException, ApplicantIsBlackListedException, NullJobReferenceException {

		Job jobRef = employer.getPostedJobs().get("job1");
		Applicant applicntRef = null;

		employer.shortListCandidate(null, applicntRef);
		employer.changeApplicantStatus(null, EmploymentStatus.EMPLOYED);
	}


	/**
	 * @author Yogeshwar Chaudhari
	 * Employer can post a job against which employer can interview/hire the applicants
	 * Test case Type : Positive
	 * @throws DuplicateJobIdException
	 */
	@Test
	public void test_createJob() throws DuplicateJobIdException {

		// Creating a job job
		Job j1 = new Job("j1","Test Job", "Test Job Description");

		// Validating the state before the
		Assert.assertEquals("Validating Posted Jobs List Count Before : ", 3, this.employer.getPostedJobs().size());

		this.employer.createJob(j1);

		Assert.assertEquals("Validating Posted Jobs List Count Before : ", 4, this.employer.getPostedJobs().size());

		// Also validate that the added job that is being added is exactly what is being supplied
		Assert.assertEquals("validating Obj", j1, this.employer.getPostedJobs().get("j1"));
	}


	/**
	 * @author Yogeshwar Chaudhari
	 * Employer accidently supplies duplicate job id, Making sure that employer does not accidcently overrwrite exisiting job
	 * Test case Type : Positive
	 * @throws DuplicateJobIdException
	 */
	@Test (expected = DuplicateJobIdException.class)
	public void test_createJobDuplicateId() throws DuplicateJobIdException {

		// Creating a job job
		Job j1 = new Job("j1","Test Job", "Test Job Description");

		// Validating the state before the
		Assert.assertEquals("Jobs Count Before : ", 3, this.employer.getPostedJobs().size());

		this.employer.createJob(j1);

		Assert.assertEquals("Jobs Count After: ", 4, this.employer.getPostedJobs().size());

		Job j2 = new Job("j1","Test Job 2", "Test Job Description 2");

		// Expecting exception because IDs are same
		this.employer.createJob(j2);

	}

}
