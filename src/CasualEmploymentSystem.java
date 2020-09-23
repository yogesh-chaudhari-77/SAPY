import java.text.ParseException;
import java.util.HashMap;


import model.exceptions.BadEmployeeRecordException;
import model.exceptions.BadQualificationException;
import model.exceptions.BlacklistedTimeNotElapsedException;
import model.exceptions.DuplicateEntryException;
import model.exceptions.DuplicateJobCategoryException;
import model.exceptions.NotFullyBlacklistedUserException;
import model.exceptions.NotProvisionallyBlacklistedUserException;
import model.entities.*;
import controller.*;

public class CasualEmploymentSystem {

	HashMap<String, Applicant> allApplicantsList = null;
	HashMap<String, Employer> allEmployersList = null;
	HashMap<String, MaintenanceStaff> allStaffList = null;
	
	public static void main(String[] args) throws BadQualificationException, DuplicateEntryException, DuplicateJobCategoryException, ParseException, NotFullyBlacklistedUserException, NotProvisionallyBlacklistedUserException, BlacklistedTimeNotElapsedException {
		// TODO Auto-generated method stub
		
		
		SystemHandler handler = new SystemHandler();
		handler.run();
		
	}

}
