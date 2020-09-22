package testing;
import static org.junit.Assert.*;

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
	HashMap<String, User> allUsersList  = new HashMap<String, User>();;
	HashMap<String, User> blacklistedUsers = new HashMap<String, User>();
	HashMap<String, JobCategory> allJobCategories = new HashMap<String, JobCategory>();
	
//	((Applicant)blacklistedUsers.get("S1")).setBlacklistStatus(blacklistStatus.PROVISIONAL_BLACKLISTED);
//	((Employer)blacklistedUsers.get("E1")).setBlacklistStatus(blacklistStatus.FULL_BLACKLISTED);
//	allUsersList.put("E001", blacklistedUsers.get("E1"));
//	allUsersList.put("S001", blacklistedUsers.get("S1"));
	


	
	@Before
	public void setUp() throws Exception 
	{
		staff = new MaintenanceStaff("Staff001", "maintenancestaff@mail.com", "test123", "System", "Admin", "415414478");
		allUsersList.put("E001", new Employer("E001", "E@mail.com", "Emp123", "Test" ,"Employer", "123"));
		allUsersList.put("S001", new Applicant("S001", "S@mail.com", "stud123", "Test" ,"Applicant", "123",""));
		allUsersList.put("E002", new Employer("E002", "E2@mail.com", "Employer2", "Test" ,"Employer2", "123"));
		
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
	public void testBlackListingProvisoinally()
	{
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(),blacklistStatus.NOT_BLACKLISTED);
		staff.blacklistUser(allUsersList.get("E001"), "P");
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(),blacklistStatus.PROVISIONAL_BLACKLISTED);

	}
	
	/* Positive Test Case for Blacklisting User Fully 
	 * 		-- Checking the pre condition, such that initially User is not blacklisted.
	 * 		-- Then Blacklisting the User provisionally by using type'F'
	 * 		-- Checking the post condition, now User should be Fully blacklisted 
	 */
	@Test
	public void testBlackListingFully()
	{
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(),blacklistStatus.NOT_BLACKLISTED);
		staff.blacklistUser(allUsersList.get("E001"), "F");
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(),blacklistStatus.FULL_BLACKLISTED);

	}
	
	/* Positive Test Case for Re-activating Blacklisted User Fully 
	 * 		-- Checking the pre condition, such that  User is already blacklisted.
	 * 		-- Then Blacklisting the User provisionally by using type'F'
	 * 		-- Checking the post condition, now User should be reactivated
	 */
	@Test
	public void testRevertBlackListedUser()
	{
		
		staff.blacklistUser(allUsersList.get("E001"), "F");
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(),blacklistStatus.FULL_BLACKLISTED);
		staff.revertBlacklistedUser(allUsersList.get("E001"));
		assertEquals(((Employer)allUsersList.get("E001")).getBlacklistStat(),blacklistStatus.NOT_BLACKLISTED);
	}

}
