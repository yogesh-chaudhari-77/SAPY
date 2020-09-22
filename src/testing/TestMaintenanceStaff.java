package testing;
import static org.junit.Assert.*;
import model.entities.*;
import model.enums.*;
import model.exceptions.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestMaintenanceStaff {
	MaintenanceStaff testStaff;
	Blacklist suspend;
	Applicant testApplicant;

	
	@Before
	public void setUp() throws Exception 
	{
		testStaff = new MaintenanceStaff("testid", "testuserEmail", "testpassword", "Tester", "1", "1236345");
		//testApplicant = new Applicant("Applicant", "1", "testemail@a.com", "S");
		suspend = new Blacklist();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	// ** Positive Test Case for addJobCategory **//
	@Test
	public void testAddJobCategory() throws DuplicateJobCategoryException
	{
		testStaff.addJobCategory("Drivers");
		// assertTrue(testStaff.checkJobCategory("Drivers"));
	}
	
	
	// ** Negative Test Case for addJobCategory **//
	@Test
	(expected = DuplicateJobCategoryException.class)
	public void testAddingDuplicateJobCategory() throws DuplicateJobCategoryException
	{
		testStaff.addJobCategory("Drivers");
		testStaff.addJobCategory("Drivers");
	}
	
	
	/* Positive Test Case for Blacklisting Members 
	 * Checking the count of blacklisted members after blacklisting a new member
	 */
	@Test
	public void testBlackListMember()
	{
		//int beforeCount = suspend.provisonallyBlacklistedMembers.size();

		//suspend.add((AbstractUser)testApplicant, "Testing Purpose", "P");

		//int afterCount = suspend.provisonallyBlacklistedMembers.size();

		//assertEquals(afterCount - beforeCount, 1);

	}

}
