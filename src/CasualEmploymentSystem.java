import java.util.HashMap;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;

import model.entities.*;
import controller.*;

public class CasualEmploymentSystem {

	HashMap<String, Applicant> allApplicantsList = null;
	HashMap<String, Employer> allEmployersList = null;
	HashMap<String, MaintenanceStaff> allStaffList = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		SystemHandler handler = new SystemHandler();
		handler.run();
		
	}

}
