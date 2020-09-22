package testing;

import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.SystemHandler;
import model.entities.Applicant;
import model.entities.Employer;
import model.entities.Job;

public class testSystemHandler {
	
	private SystemHandler sysHandler = new SystemHandler();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sysHandler.loadDummyDataForEmployeFunctions();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void searchApplicants() {
		
		Employer e = this.sysHandler.getAllEmployersList().get("e");
		HashMap<String, Applicant> searchResults = sysHandler.searchApplicants(e);
	}
	
}