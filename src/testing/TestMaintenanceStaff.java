package testing;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import model.entities.*;
import model.enums.*;
import model.exceptions.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestMaintenanceStaff {
	MaintenanceStaff staff;
	Blacklist suspend;
	Applicant testApplicant;
	BlacklistStatus blacklistStatus;
	HashMap<String, User> allUsersList  = new HashMap<String, User>();
	HashMap<String, User> blacklistedUsers = new HashMap<String, User>();
	HashMap<String, JobCategory> allJobCategories = new HashMap<String, JobCategory>();
	SimpleDateFormat dateFormat;
	
	
	@Before
	public void setUp() throws Exception 
	{
		staff = new MaintenanceStaff("Staff001", "maintenancestaff@mail.com", "test123", "System", "Admin", "415414478");
		allUsersList.put("E001", new Employer("E001", "E@mail.com", "Emp123", "Company1","Test" ,"Employer", "123"));
		allUsersList.put("S001", new Applicant("S001", "S@mail.com", "stud123", "Test" ,"Applicant", "123",""));
		allUsersList.put("E002", new Employer("E002", "E2@mail.com", "Employer2","Company2", "Test" ,"Employer2", "123"));
		
		dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	// ** Positive Test Case for addJobCategory **//
	@Test
	public void testAddJobCategory() throws DuplicateJobCategoryException
	{
		assertFalse(allJobCategories.containsKey("Drivers"));
		allJobCategories.put("Drivers",staff.addJobCategory(allJobCategories,"Drivers"));
	}
	
	
	// ** Negative Test Case for addJobCategory **//
	@Test
	(expected = DuplicateJobCategoryException.class)
	public void testAddingDuplicateJobCategory() throws DuplicateJobCategoryException
	{
		
		allJobCategories.put("Testers",staff.addJobCategory(allJobCategories,"Testers"));
		allJobCategories.put("Testers",staff.addJobCategory(allJobCategories,"Testers"));
	}
	
	
	/* Positive Test Case for Blacklisting User Provisionally 
	 * 		-- Checking the pre condition, such that initially User is not blacklisted.
	 * 		-- Then Blacklisting the User provisionally by using type'P'
	 * 		-- Checking the post condition, now User should be provisionally blacklisted 
	 */
	@Test
	public void testBlackListingProvisoinally() throws ParseException
	{
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(), BlacklistStatus.NOT_BLACKLISTED);
		staff.blacklistUser(allUsersList.get("E001"), "P");
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(), BlacklistStatus.PROVISIONAL_BLACKLISTED);

	}
	
	/* Positive Test Case for Blacklisting User Fully 
	 * 		-- Checking the pre condition, such that initially User is not blacklisted.
	 * 		-- Then Blacklisting the User provisionally by using type'F'
	 * 		-- Checking the post condition, now User should be Fully blacklisted 
	 */
	@Test
	public void testBlackListingFully() throws ParseException
	{
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(), BlacklistStatus.NOT_BLACKLISTED);
		staff.blacklistUser(allUsersList.get("E001"), "F");
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(), BlacklistStatus.FULL_BLACKLISTED);

	}
	
	
		/* ******************************************************************************************    */
		/* 					Stage 2 Presentation - Positive & Negative Testcases 						 */
		/*					For the Use Case - "Reactivate Blacklisted User"							 */
		/* ******************************************************************************************    */
	
	
	/* Positive Test Case for Re-activating Fully Blacklisted User 
	 * 		-- Check the pre condition, such that  User is already fully blacklisted.
	 * 		-- Then perform the operation - reactivating the fully blacklisted user
	 * 		-- Check the post condition, now User should be reactivated
	 */
	
	@Test
	public void testReactivatingFullyBlackListedUser() throws NotFullyBlacklistedUserException, ParseException, NotProvisionallyBlacklistedUserException, BlacklistedTimeNotElapsedException
	{
		
		//Pre condition
		staff.blacklistUser(allUsersList.get("E001"), "F");
		((Employer)allUsersList.get("E001")).setBlacklistStartDate(dateFormat.parse("24/06/2020 15:42:42"));
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(), BlacklistStatus.FULL_BLACKLISTED);
		
		//Operation
		staff.revertBlacklistedUser(allUsersList.get("E001"),"F");
		
		//Post Condition
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(), BlacklistStatus.NOT_BLACKLISTED);
	}
	
	
	/* Positive Test Case for Re-activating Provisionally Blacklisted User 
	 * 		-- Check the pre condition, such that  User is already Provisionally blacklisted.
	 * 		-- Then perform the operation - reactivating the Provisionally blacklisted user
	 * 		-- Check the post condition, now User should be reactivated
	 */
	
	@Test
	public void testReactivatingProvisionallyBlackListedUser() throws NotFullyBlacklistedUserException, ParseException, NotProvisionallyBlacklistedUserException, BlacklistedTimeNotElapsedException
	{
		
		//Pre condition
		staff.blacklistUser(allUsersList.get("E001"), "P");
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(), BlacklistStatus.PROVISIONAL_BLACKLISTED);
		
		//Operation
		staff.revertBlacklistedUser(allUsersList.get("E001"),"P");
		
		//Post Condition
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(), BlacklistStatus.NOT_BLACKLISTED);
	}

	
	/* Negative Test Case for Re-activating Provisionally Blacklisted User 
	 * 		-- Check the pre condition, such that  User is not blacklisted.
	 * 		-- Then perform the operation of  reactivating the Provisionally blacklisted user on non blacklisted user
	 * 			which should throw NotProvisionallyBlacklistedUserException
	 * 		
	 */
	
	@Test
	(expected = NotProvisionallyBlacklistedUserException.class)
	public void testReactivatingNonBlackListedUser1() throws NotFullyBlacklistedUserException, ParseException, NotProvisionallyBlacklistedUserException, BlacklistedTimeNotElapsedException
	{
		
		//Pre condition
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(), BlacklistStatus.NOT_BLACKLISTED);
		
		//Operation - Exception is expected
		staff.revertBlacklistedUser(allUsersList.get("E001"),"P");
	}
	
	
	/* Negative Test Case for Re-activating Provisionally Blacklisted User 
	 * 		-- Check the pre condition, such that  User is not blacklisted.
	 * 		-- Then perform the operation of  reactivating the Fully blacklisted user on non blacklisted user
	 * 			which should throw NotFullyBlacklistedUserException
	 * 		
	 */
	
	@Test
	(expected = NotFullyBlacklistedUserException.class)
	public void testReactivatingNonBlackListedUser2() throws NotFullyBlacklistedUserException, ParseException, NotProvisionallyBlacklistedUserException, BlacklistedTimeNotElapsedException
	{
		
		//Pre condition
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(), BlacklistStatus.NOT_BLACKLISTED);
		
		//Operation - Exception is expected
		staff.revertBlacklistedUser(allUsersList.get("E001"),"F");
		
	}
	
	
	/* Negative Test Case for Re-activating Fully Blacklisted User, where the blacklisted period has not exceeded 3 months 
	 * 		-- Check the pre condition, such that  User is Fully blacklisted.
	 * 		-- Then perform the operation of  reactivating the fully blacklisted user before the duration of blacklisted period is less than 
	 * 			3 months ,which should throw BlacklistedTimeNotElapsedException
	 * 		
	 */
	
	@Test
	(expected = BlacklistedTimeNotElapsedException.class)
	public void testReactivatingUserBeforeTimeElapse() throws NotFullyBlacklistedUserException, ParseException, NotProvisionallyBlacklistedUserException, BlacklistedTimeNotElapsedException
	{
		
		//Pre condition
		staff.blacklistUser(allUsersList.get("E001"), "F");
		((Employer)allUsersList.get("E001")).setBlacklistStartDate(dateFormat.parse("15/09/2020 15:42:42"));
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(), BlacklistStatus.FULL_BLACKLISTED);
		
		
		//Operation - Exception is expected
		staff.revertBlacklistedUser(allUsersList.get("E001"),"F");
		
	}
}
