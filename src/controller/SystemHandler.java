package controller;

import global.*;
import view.*;
import customUtils.*;
import model.entities.*;
import model.exceptions.*;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

// Commenting should be enough
// Methods should be in there own classes

public class SystemHandler {

	//Menu menu;
	HashMap<String, User> allUsersList = null;
	HashMap<String, Applicant> allApplicantsList = null;
	HashMap<String, Employer> allEmployersList = null;
	HashMap<String, MaintenanceStaff> allStaffList = null;
	HashMap<String, User> blacklistedUsers = new HashMap<String, User>();
	HashMap<String, JobCategory> allJobCategories = new HashMap<String, JobCategory>();

	String id;
	String userEmail;
	String password;
	String firstName;
	String lastName;
	String phoneNumber;
	
	Scanner input = new Scanner(System.in);
	ScannerUtil customScanner = ScannerUtil.createInstance().consoleReader();
	
	MaintenanceStaff staff = new MaintenanceStaff("Staff001", "maintenancestaff@mail.com", "test123", "System", "Admin", "415414478");
	
	
//	blacklistedUsers.put("E1", new Employer("","","","","",""));
//	//testing purpose
//	blacklistedUsers.put("E1", new Employer("E001", "E@mail.com", "Emp123", "Test" ,"Employer", "123"));
//	blacklistedUsers.put("S1", new Applicant("S001", "S@mail.com", "stud123", "Test" ,"Applicant", "123"));
//	
//	staff.viewBlackListedMembers(blacklistedUsers);
	
	public SystemHandler() {

		allUsersList = new HashMap<>();
		allApplicantsList = new HashMap<>();
		allEmployersList = new HashMap<>();
		allStaffList = new HashMap<>();

	}
	

		
	public void run() throws BadQualificationException, DuplicateEntryException {
		Menu menu = null;
		boolean quit = false;
		
		blacklistedUsers.put("E1", new Employer("E001", "E@mail.com", "Emp123", "Test" ,"Employer", "123"));
		blacklistedUsers.put("S1", new Applicant("S001", "S@mail.com", "stud123", "Test" ,"Applicant", "123",""));
		allUsersList.put("Staff001", staff);

		try {
			menu = new Menu("main_menu_options");
		} catch (FileNotFoundException e) {
			System.out.println("Main Menu File Missing");
		} catch (Exception e) {
			System.out.println(e);
		}

		do {
			System.out.println("====Student Casual Employment System====");

			String choice = menu.show();
			// read a value from the user
			// switch statement, call appropriate method.
			choice = choice.toUpperCase();
//			System.out.println("you just entered in " + choice);
//
//			if (choice.equals("1")) {
//				System.out.println("Option 1 processing");
//			}
//
//			if (choice.equalsIgnoreCase("q")) {
//				quit = true;
//			}
			switch(choice){
				case "1":
					register();
					break;

				case "2":
					login();
					break;

				case "Q":
					quit = true;
					System.out.println("Exiting program gracefully");
					break;

				default:
					System.out.println("Wrong option please check and enter again");
			}
			
		} while (!quit);
	}

	public void register(){
		boolean quit = false;
		Menu menu = null;



		try {
			menu = new Menu("register_menu_options");
		} catch (Exception e) {
			System.out.println();
		}

		do{
			System.out.println("====Registration Menu====");
			String choice = menu.show();
			choice = choice.toUpperCase();

			switch(choice){
				case "1":
					registerApplicant();
					break;

				case "2":
					Employer retEmp = registerEmployer();
					if(retEmp != null) {
						System.out.println("Employer registration successful");
						System.out.println(retEmp.toString());
					}
					break;

//				case "3":
//					//registerMaintenanceStaff();
//					break;

				case "Q":
					quit = true;

			}
		} while (!quit);

	}

	public void registerApplicant() {


		String applicantType;

		setUserDetails();

		boolean exitLoop = false;

		do{
			System.out.print("Enter the type of applicant: " +
					"\nI --> International" +
					"\nL --> Local" +
					"\nChoice: ");
			applicantType = Global.scanner.nextLine();
			if (applicantType.equalsIgnoreCase("L") || applicantType.equalsIgnoreCase("I")
			) {
				Applicant applicant = new Applicant(id, userEmail, password, firstName, lastName, phoneNumber, applicantType);
				allUsersList.put(id, applicant);
				allApplicantsList.put(id, applicant);
				System.out.println("Applicant account created successfully\n"+applicant.toString());
				exitLoop = true;

			} else {
				System.out.println("Wrong choice. Please try again");
			}
		} while(!exitLoop);

	}

	public void setUserDetails(){
		boolean idFound = false;
		do {
			System.out.print("Enter the User id: ");
			id = Global.scanner.nextLine();
			if (!freeIdCheck(id)){
				System.out.println("This user id is already taken");
				idFound = true;
			}
		}while(idFound);


		do {
			System.out.print("Enter the email: ");
			userEmail = Global.scanner.nextLine();
		} while (!freeEmailCheck(userEmail));

		System.out.print("Enter the password: ");
		password = Global.scanner.nextLine();

		System.out.print("Enter the First Name: ");
		firstName = Global.scanner.nextLine();

		System.out.print("Enter the Last Name: ");
		lastName = Global.scanner.nextLine();

		System.out.print("Enter the Phone Number: ");
		phoneNumber = Global.scanner.nextLine();
	}

	public boolean freeIdCheck(String id) {

		Set keySet = allUsersList.keySet();
		Iterator iterator =keySet.iterator();
		while(iterator.hasNext()){
			String key = (String)iterator.next();
			if ( key.equals(id)){
				return false;
			}
		}
		return true;
	}

	public boolean freeEmailCheck(String email){

		if(validEmailCheck(email)){
			Set keySet = allApplicantsList.keySet();
			Iterator iterator =keySet.iterator();
			while(iterator.hasNext()){
				String key = (String)iterator.next();
				if(allUsersList.get(key).getUserEmail().equals(email)){
					System.out.println("Account already exists");
					return false;
				}
			}
		} else {
			System.out.println("Mail format is wrong!! Please provide as shown example abc@xyz.com");
			return false;
		}
		return true;
	}

	//reference-->
	public boolean validEmailCheck(String email){
		String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
				"(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

		Pattern pattern = Pattern.compile(emailPattern);

		return pattern.matcher(email).matches();

	}

	public void login () throws BadQualificationException, DuplicateEntryException {

		String id;
		String password;

		System.out.print("Enter the User id: ");
		id =  Global.scanner.nextLine();

		System.out.print("Enter the password: ");
		password = Global.scanner.nextLine();

		if(!freeIdCheck(id)){
			if (allUsersList.get(id).getPassword().equals(password)){
				showUserMenu(allUsersList.get(id));
			} else {
				System.out.println("Wrong password");
			}
		} else {
			System.out.println("Wrong id");
		}
	}

	public void showUserMenu(User user) throws BadQualificationException, DuplicateEntryException {
		if (user instanceof Applicant){
			showApplicantMenu(((Applicant) user));
		} else if (user instanceof Employer){
			showEmployerMenu(((Employer) user));
		} else {
			showMaintenanceStaffMenu(staff);
		}
	}

	public void showApplicantMenu(Applicant applicant) throws BadQualificationException, DuplicateEntryException {
		boolean quit = false;
		Menu menu = null;

		try {
			menu = new Menu("applicant_menu_options");
		} catch (Exception e) {
			System.out.println();
		}

		do{
			System.out.println("===Applicant Menu of various options to check===");
			String choice = menu.show();
			choice = choice.toUpperCase();

			switch(choice){
				case "1":
					addUpdateQualification(applicant);
					break;

				case "2":
					registerEmployer();
					break;

				case "Q":
					quit = true;

			}
		} while (!quit);

	}

	public void addUpdateQualification(Applicant applicant) throws BadQualificationException, DuplicateEntryException {
		String operation = subMenu();

		if (operation.equals("add")){
			addQualification(applicant);
		} else if (operation.equals("update")){
			//updateQualification(applicant);
		} else if (operation.equals("view")){
			//showQualification(applicant);
		} else {
			System.out.println("Exiting Sub Menu");
			return ;
		}
	}

	public String subMenu(){
		Menu menu = null;

		System.out.println("Kindly Select an operation to perform");

		try {
			menu = new Menu("sub_menu_options\n");
		} catch (Exception e) {
			System.out.println();
		}

		boolean wrongOption = false;

		do {
			String choice = menu.show();
			choice = choice.toUpperCase();

			switch (choice) {
				case "1":
					return "add";

				case "2":
					return "update";

				case "3":
					return "view";

				case "4":
					return "exit";

				default:
					System.out.println("!! Wrong Option !! Kindly select the correct one");
					wrongOption = true;
			}
		} while(wrongOption);

		return null;
	}
	
	public void addQualification(Applicant applicant) throws BadQualificationException, DuplicateEntryException {

		String qualificationLevel;
		Date startDate;
		Date endDate;
		String fieldOfStudy;
		double marksObtained;

		System.out.print("Enter Below details for adding qualification\n");
		System.out.print("Qualification Level: ");
		qualificationLevel = Global.scanner.nextLine();
		System.out.print("Start Date(YYYY/MM/DD): ");
		startDate = getDateInput();
		System.out.print("End Date(YYYY/MM/DD): ");
		endDate = getDateInput();
		System.out.print("Field of Study: ");
		fieldOfStudy = Global.scanner.nextLine();
		System.out.print("Marks Obtained(in percentage): ");
		marksObtained = Global.scanner.nextDouble();

		Qualification qualification = new Qualification(qualificationLevel, startDate, endDate, fieldOfStudy, marksObtained);
		if (applicant.addQualifications(qualification)){
			System.out.println("Qualification add successfully");
			Global.scanner.nextLine();
		} else {
			System.out.println("!! Adding qualification failed !!");
		}

	}

	public Date getDateInput(){

		String datePattern = "((19|20)[0-9]{2})/((0?[1-9])|1[012])/((0?[1-9])|(1[0-9])|(2[0-9])|(3[01]))";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Pattern pattern = Pattern.compile(datePattern);

		Date date = null;
		boolean wrongDate;
		do {
			String dateUserInput = Global.scanner.nextLine();

			if(pattern.matcher(dateUserInput).matches()){
				wrongDate = false;
				try	{
					date = dateFormat.parse(dateUserInput);
				} catch (ParseException e){
					System.out.println(e);
				}
			} else {
				System.out.println("Wrong date format");
				wrongDate = true;
			}
		} while (wrongDate);

		return date;
	}

	public void showMaintenanceStaffMenu(MaintenanceStaff staff){
		boolean quit = false;
		Menu menu = null;

		try {
			menu = new Menu("maintenancestaff_menu_options");
		} catch (Exception e) {
			System.out.println();
		}

		do{
			String choice = menu.show();
			choice = choice.toUpperCase();

			switch(choice){
				case "1":
				{
					String title;
					System.out.println("Enter the title of the Job category: ");
					title = input.nextLine();
					allJobCategories.put(title, staff.addJobCategory(title));
				}
					break;

				case "2":
					staff.viewBlackListedMembers(blacklistedUsers);
					break;

				case "3":
				{
					String user,type;
					System.out.println("Enter the id of the user to be blacklisted:  ");
					user = input.nextLine();

					System.out.println("Blacklisting Types('P' - Provisional Blacklist, 'F' - Full Blacklist ");
					System.out.println("Enter the type (P/F) : ");
					type = input.nextLine();
					staff.blacklistUser(allUsersList.get(user), type);
					
				}
					//registerMaintenanceStaff();
					break;
				
				case "4":
				{
					String user;
					System.out.println("Enter the id of the user to be blacklisted:  ");
					user = input.nextLine();

					staff.revertBlacklistedUser(allUsersList.get(user));
					 
				}
					//registerMaintenanceStaff();
					break;

				case "Q":
					quit = true;

			}
		} while (!quit);

	}

	
	/**
	 * @author : Yogeshwar Girish Chaudhari
	 * Employer Menu Options
	 */
	
	public void showEmployerMenu(Employer employer){
		boolean quit = false;
		Menu menu = null;

		try {
			menu = new Menu("employer_menu_options");
		} catch (Exception e) {
			System.out.println();
		}

		do{
			String choice = menu.show();
			choice = choice.toUpperCase();

			switch(choice){
				case "1":
				{
					this.createAJob(employer);
				}
				break;

				case "2":
					HashMap<String, Applicant> matchingApplicants = this.searchApplicants(employer);
					
					if(matchingApplicants != null) {
						for (Applicant appRef : matchingApplicants.values()) {
							System.out.println(appRef.toString());
						}
					}
					
					break;

				case "3":
				{
					String user,type;
					System.out.println("Enter the id of the user to be blacklisted:  ");
					user = input.nextLine();

					System.out.println("Blacklisting Types('P' - Provisional Blacklist, 'F' - Full Blacklist ");
					System.out.println("Enter the type (P/F) : ");
					type = input.nextLine();
					staff.blacklistUser(allUsersList.get(user), type);
					
				}
					//registerMaintenanceStaff();
					break;

				case "Q":
					quit = true;

			}
		} while (!quit);

	}
	

	/**
	 * @author Yogeshwar Chaudhari
	 * Creates a new employer by asking basic details about the employer.
	 * @return Employer | Newly created employer
	 */
	public Employer registerEmployer() {
		
		String id = customScanner.readString("Enter ID : ");
		String email = customScanner.readString("Enter email : ");
		String password = customScanner.readString("Enter password : ");
		String companyName = customScanner.readString("Enter company name: ");
		
		Employer newEmployer = new Employer(id, email, password, companyName);
		this.allEmployersList.put(id, newEmployer);
		this.allUsersList.put(id, newEmployer);
		
		return newEmployer;
	}
	
	
	/**
	 * Employer login function
	 * Employer can only perform operations after logging into system
	 * It becomes tracking and updating easy since we already know for which employer we are performing the operations
	 * @return
	 */
	public Employer loginAsEmployer() {
		
		System.out.println("Login as employer");
		
		// Accept credentials
		String employerId = customScanner.readString("Enter ID");
		String enteredPassword = customScanner.readString("Enter Password");
		
		// Basic error checking of empty string
		if(employerId.isBlank() || enteredPassword.isBlank()) {
			System.err.print("Either ID or Password is empty. Please try again");
			return null;
		}else {
			
			// Find the employer with specified ID
			Employer employerRef = this.allEmployersList.get(employerId);
			
			if(employerRef != null) {
				
				// Password comparison
				if(employerRef.getPassword().contentEquals(password)) {
					
					// Matched : Success  : Return the Obj
					return employerRef;
				}
				else {
					return null;
				}
				
			}else {
				System.out.println("Invalid Employer ID. No Account Exists");
				return null;
			}
		}
		
	} // End of loginAsEmployer
	
	
	/**
	 * Employer can create job so that, applicants can be searched
	 * @param employer
	 * @return
	 */
	public void createAJob(Employer employer) {
		
		// Accepting basic job details
		String jobId = customScanner.readString("Job ID : ");
		String jobTitle = customScanner.readString("Job Title : ");
		String jobDesc = customScanner.readString("Please enter a short job description");
		
		// Create new job
		Job tempJob = new Job(jobId, jobTitle, jobDesc);
		
		// Add that job to the employer
		employer.createJob(tempJob);
		
		// Validate
		if(employer.getPostedJobs().get(jobId) != null) {
			System.out.println("Job has been successfully added your posted jobs");
		}
	}
	
	
	
	/**
	 * Employer can search through applicant's list
	 */
	public HashMap<String, Applicant> searchApplicants(Employer employer) {
		
		HashMap<String, Applicant> searchResults = new HashMap<String, Applicant>();
		
		// Blacklisting check
		if(employer.isBlackListed()) {
			System.out.println("Employer is blacklisted. Sorry. You can not proceed.");
			return null;
		}
		
		// Search Criteria
		String jobPreferencesStr = customScanner.readString("Pleaser enter prefered job categories (Seperate multiple categories using ,) :\n");
		String availability = customScanner.readString("Provide Availability");
		
		// Extracting individual job preferences
		String [] jobPreferencesArr = null;
		if(!jobPreferencesStr.isBlank() && !jobPreferencesStr.isEmpty()) {
			jobPreferencesArr = jobPreferencesStr.split(",");
		}

		// Iterating over the applicant's list to find the matching candidates 
		for(Applicant applicantRef : allApplicantsList.values()) {

			// Check the preferences and availability
			if(checkAppcntJobPreference(applicantRef, jobPreferencesArr) && checkAvailability(applicantRef)) {

				// Matching preference and availability
				searchResults.put(applicantRef.getId(), applicantRef);
			}
		}

		// Testing
		System.out.println(searchResults.keySet());
		return searchResults;
	}
	
	
	/**
	 * Checks if the applicant has desired job preference
	 * @param applicantRef
	 * @return
	 */
	public boolean checkAppcntJobPreference(Applicant applicantRef, String [] searchJobPreference) {

		// Check for each job preference supplied in search criteria
		for(int i = 0; i < searchJobPreference.length; i++) {

			// Check if applicant's job preferences matches with search criteria
			if(applicantRef.getJobPreferences().contains( searchJobPreference[i] )) {

				// match found
				return true;
			}
		}

		return false;

	} // end of checkAppcntJobPreference()

	
	/**
	 * Checks the applicant is available at the times, employer desires to be
	 * TODO : to be implemented
	 * @param applicantRef
	 * @return
	 */
	public boolean checkAvailability(Applicant applicantRef) {

		return true;
	}


	//	public boolean complaintApplicant(Applicant applcnt, String message) {
	//		
	//		// Basic error checking
	//		if(applcnt == null) {
	//			throw new NullApplicantException();
	//		}
	//		
	//		Complaints tempComplaint = new Complaints(this, applcnt, message);
	//		return tempComplaint;
	//	}


// Written by Abhishek
//	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//	Date date = null;
//	boolean wrongDate = false;
//		do {
//		String dateUserInput = Global.scanner.nextLine();
//
//		try {
//			date = dateFormat.parse(dateUserInput);
//		} catch (ParseException e){
//			System.out.println("Wrong date format");
//			wrongDate = true;
//		}
//
//	} while (wrongDate);
//
//		return date;

}

