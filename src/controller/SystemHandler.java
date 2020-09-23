package controller;

import global.*;
import view.*;
import customUtils.*;
import model.entities.*;
import model.enums.AvailabilityType;
import model.exceptions.*;
import model.enums.*;


import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import com.sun.jdi.InvalidTypeException;

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

	// 23-09-2020
	List<Complaints> allComplaints = new ArrayList<Complaints>();

	String id;
	String userEmail;
	String password;
	String firstName;
	String lastName;
	String phoneNumber;

	Scanner input = new Scanner(System.in);
	ScannerUtil customScanner = ScannerUtil.createInstance().consoleReader();

	MaintenanceStaff staff = new MaintenanceStaff("Staff001", "maintenancestaff@mail.com", "test123", "System", "Admin", "415414478");



	public SystemHandler() {

		allUsersList = new HashMap<>();
		allApplicantsList = new HashMap<>();
		allEmployersList = new HashMap<>();
		allStaffList = new HashMap<>();

		try {
			loadDummyDataForEmployeFunctions();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}



	public void run() {
		Menu menu = null;
		boolean quit = false;


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

	public void login () {

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

	public void showUserMenu(User user) {
		if (user instanceof Applicant){
			showApplicantMenu(((Applicant) user));
		} else if (user instanceof Employer){

			// Blacklisting check - Before accessing any of the functionality.
			if( ((Employer) user).isBlackListed()) {
				System.out.println("Employer is blacklisted. Sorry. You can not proceed.");
				return;
			}

			showEmployerMenu(((Employer) user));
		} else {
			showMaintenanceStaffMenu(staff);
		}
	}

	public void showApplicantMenu(Applicant applicant) {
		boolean quit = false;
		Menu menu = null;

		try {
			menu = new Menu("applicant_menu_options");
		} catch (Exception e) {
			System.out.println();
		}

		do{
			System.out.println("\n===Applicant Menu of various options to check===");
			String choice = menu.show();
			choice = choice.toUpperCase();

			switch(choice){
				case "1":
					addUpdateQualification(applicant);
					break;

				case "2":
					registerEmployer();
					break;

				case "3":
					//employmentRecords
					addUpdateEmploymentHistory(applicant);
					break;

				case "4":
					addUpdateLicenses(applicant);
					break;

				case "5":
					uploadApplicantCV(applicant);
					break;

				case "6":
					addUpdateAvailability(applicant);

				case "Q":
					quit = true;

			}
		} while (!quit);

	}

	public void addUpdateQualification(Applicant applicant){
		System.out.println("****** Qualifications ******");
		String operation = subMenu();

		if (operation.equals("add")){
			addQualification(applicant);
		} else if (operation.equals("update")){
			//updateQualification(applicant);
		} else if (operation.equals("view")){
			showQualification(applicant);
		} else {
			System.out.println("Exiting Sub Menu");
			return ;
		}
	}
	public void addUpdateEmploymentHistory(Applicant applicant){
		boolean continueMessageDisplay = true;
		do {
			try {
				System.out.println("****** Employment Records ******");
				String operation = subMenu();
				if (operation.equals("add")) {
					addEmploymentHistory(applicant);
				} else if (operation.equals("update")) {
					//updateEmploymentHistory(applicant);
				} else if (operation.equals("view")) {
					showEmploymentRecords(applicant);
				} else {
					System.out.println("Exiting Sub Menu");
					return;
				}
			} catch (DuplicateEntryException duplicate) {
				System.out.println("Employment Record Already exists. Please try again.");
				//continueMessageDisplay= true;
			}
		}while (continueMessageDisplay);
	}

	public void uploadApplicantCV(Applicant applicant){
		System.out.println("***** Upload CV *****");
		String currentCV = applicant.getCvPath();
		if(currentCV == null){
			System.out.println("No CV uploaded.");
		}else{
			System.out.println("Current CV in the system: "+currentCV);
		}
		try {
			Menu menu = new Menu("cv_menu_options");
			boolean wrongOption = false;

			do {
				String choice = menu.show();
				choice = choice.toUpperCase();

				switch (choice) {
					case "1":
						if(addCVPathToApplicant(applicant)){
							System.out.println("CV uploaded successfully!!");
							return;
						}else {
							System.out.println("Path to CV does not exist. Please enter valid path");
							wrongOption = true;
						}
						break;

					case "Q":
						return;

					default:
						System.out.println("!! Wrong Option !! Kindly select the correct one");
						wrongOption = true;
				}
			} while(wrongOption);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean addCVPathToApplicant(Applicant applicant){
		System.out.print("Enter system path to your CV: ");
		String path= Global.scanner.nextLine();
		try {
			applicant.uploadCV(path);
			return true;
		} catch (InvalidCVPathException e) {
			return false;
		}
	}

	private void addUpdateAvailability(Applicant applicant){
		boolean continueMessageDisplay = true;
		do {
			try {
				System.out.println("****** Employment Records ******");
				String operation = subMenu();
				if (operation.equals("add")) {
					addAvailability(applicant);
				} else if (operation.equals("update")) {
					//updateAvailability(applicant);
				} else if (operation.equals("view")) {
					showEmploymentRecords(applicant);
				} else {
					System.out.println("Exiting Sub Menu");
					return;
				}
			} catch (DuplicateEntryException duplicate) {
				System.out.println("Availability Already exists. Please try again.");
				//continueMessageDisplay= true;
			}
		}while (continueMessageDisplay);

	}

	private void showEmploymentRecords(Applicant applicant){
		List<EmploymentRecord> allEmploymentHistory = applicant.getEmploymentHistory();

		if(allEmploymentHistory.size() == 0){
			System.out.println("No Employment Records present.");
		}else{
			for(int i=0; i<allEmploymentHistory.size();i++){
				System.out.println("Employment Record "+(i+1)+":");
				System.out.println(allEmploymentHistory.get(i).toString());
			}
		}

	}

	public String subMenu(){
		Menu menu = null;

		System.out.println("Kindly Select an operation to perform");

		try {
			menu = new Menu("sub_menu_options");
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

	public void addQualification(Applicant applicant) {

		String qualificationLevel;
		Date startDate;
		Date endDate;
		String fieldOfStudy;
		double marksObtained;

		System.out.print("Enter Below details for adding qualification\n");
		System.out.print("Qualification Level: ");
		qualificationLevel = Global.scanner.nextLine();
		System.out.print("Start Date(DD/MM/YYYY): ");
		startDate = getDateInput();
		System.out.print("End Date(DD/MM/YYYY): ");
		endDate = getDateInput();
		System.out.print("Field of Study: ");
		fieldOfStudy = Global.scanner.nextLine();
		System.out.print("Marks Obtained(in percentage): ");
		marksObtained = Global.scanner.nextDouble();


		Qualification qualification = new Qualification(qualificationLevel, startDate, endDate, fieldOfStudy, marksObtained);


		try{
			applicant.addQualifications(qualification);
			System.out.println("Qualification added successfully");
		} catch (BadQualificationException e){
			System.out.println(e);
			System.out.println("!! Adding qualification failed !!");
		} catch (DuplicateEntryException e){
			System.out.println(e);
			System.out.println("!! Adding qualification failed !!");
		}
	}

	public void showQualification(Applicant applicant){
		List<Qualification> qualifications = new ArrayList<Qualification>();
		qualifications = applicant.getQualifications();

		int i =1;
		for (Qualification qualification: qualifications){
			System.out.println("Qualification "+ i++ + ". " + qualification);
		}
	}

	public void addUpdateLicenses(Applicant applicant){
		String operation = subMenu();

		if (operation.equals("add")){
			addLicense(applicant);
		} else if (operation.equals("update")){
			//updateLicense(applicant);
		} else if (operation.equals("view")){
			showLicenses(applicant);
		} else {
			System.out.println("Exiting Sub Menu");
			return ;
		}

	}

	public void addLicense(Applicant applicant) {
		String type;
		String id;
		Date validTill;

		System.out.println("Enter the below details for adding License");
		System.out.print("License Type: ");
		type = Global.scanner.nextLine();
		System.out.print("License ID: ");
		id = Global.scanner.nextLine();
		System.out.print("Validity(DD/MM/YYYY): ");
		validTill = getDateInput();

		License license = new License(type, id, validTill);

		try {
			applicant.addLicenses(license);
			System.out.println("License added successfully");
		} catch (DuplicateEntryException e) {
			System.out.println(e);
			System.out.println("!! Adding License failed !!");
		}
	}

	public void showLicenses(Applicant applicant){
		List<License> licenses = new ArrayList<License>();
		licenses = applicant.getLicenses();

		int i =1;
		for (License license: licenses){
			System.out.println("License "+ i++ + ". " + license);
		}
	}

	public void addEmploymentHistory(Applicant applicant) throws DuplicateEntryException{

		String companyName;
		String designation;
		Date startDate;
		Date endDate;
		boolean currentCompany;

		System.out.println("Enter Below details for adding Employment Record\n");
		System.out.print("Company Name: ");
		companyName = Global.scanner.nextLine();
		System.out.print("Designation: ");
		designation = Global.scanner.nextLine();
		System.out.print("Start Date(DD/MM/YYYY): ");
		startDate = getDateInput();
		System.out.println("Are you still working in this company?(Y/N): ");
		String answer= Global.scanner.nextLine();
		if(answer.equalsIgnoreCase("y")){
			currentCompany= true;
			endDate= null;
		}else {
			currentCompany = false;
			System.out.print("End Date(DD/MM/YYYY): ");
			endDate = getDateInput();
		}

		EmploymentRecord newRecord= new EmploymentRecord(companyName, designation, startDate, endDate, currentCompany);

		try {
			applicant.addEmploymentRecords(newRecord);
			System.out.println("Employment Record added successfully");
		} catch (BadEmployeeRecordException e) {
			System.out.println(e.getMessage());
			System.out.println("Failed to add employment record. Please try again.");
		}

	}

	public void addAvailability(Applicant applicant) throws DuplicateEntryException{

		String companyName;
		String designation;
		Date startDate;
		Date endDate;
		boolean currentCompany;

		System.out.println("Enter Below details for adding Employment Record\n");
		System.out.print("Company Name: ");
		companyName = Global.scanner.nextLine();
		System.out.print("Designation: ");
		designation = Global.scanner.nextLine();
		System.out.print("Start Date(DD/MM/YYYY): ");
		startDate = getDateInput();
		System.out.println("Are you still working in this company?(Y/N): ");
		String answer= Global.scanner.nextLine();
		if(answer.equalsIgnoreCase("y")){
			currentCompany= true;
			endDate= null;
		}else {
			currentCompany = false;
			System.out.print("End Date(DD/MM/YYYY): ");
			endDate = getDateInput();
		}

		EmploymentRecord newRecord= new EmploymentRecord(companyName, designation, startDate, endDate, currentCompany);

		try {
			applicant.addEmploymentRecords(newRecord);
			System.out.println("Employment Record added successfully");
		} catch (BadEmployeeRecordException e) {
			System.out.println(e.getMessage());
			System.out.println("Failed to add employment record. Please try again.");
		}

	}

	public Date getDateInput(){

		String datePattern = "((0?[1-9])|(1[0-9])|(2[0-9])|(3[01]))/((0?[1-9])|1[012])/(19|20)[0-9]{2}";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

		BlacklistStatus blacklistStatus = BlacklistStatus.NOT_BLACKLISTED ;
		blacklistedUsers.put("E1", new Employer("E001", "E@mail.com", "Emp123", "Test" ,"Employer", "123"));
		blacklistedUsers.put("S1", new Applicant("S001", "S@mail.com", "stud123", "Test" ,"Applicant", "123",""));
		((Applicant)blacklistedUsers.get("S1")).setBlacklistStatus(blacklistStatus.PROVISIONAL_BLACKLISTED);
		((Employer)blacklistedUsers.get("E1")).setBlacklistStatus(blacklistStatus.FULL_BLACKLISTED);
		allUsersList.put("E001", blacklistedUsers.get("E1"));
		allUsersList.put("S001", blacklistedUsers.get("S1"));
		allUsersList.put("E002", new Employer("E002", "E2@mail.com", "Employer2", "Test" ,"Employer2", "123"));

		do{
			String choice = menu.show();
			choice = choice.toUpperCase();

			//mock testing




			switch(choice){
				case "1":
				{
					String title;

					System.out.println("Job categories avaialable in the System\n-------------------------------------------------------");
					for (String s: allJobCategories.keySet())
					{
						System.out.println (allJobCategories.get(s));
					}

					System.out.println("Enter the title of the Job category: ");
					title = input.nextLine();

					allJobCategories.put(title,staff.addJobCategory(title));

					System.out.println("Job categories avaialable in the System\n-------------------------------------------------------");
					for (String s: allJobCategories.keySet())
					{
						System.out.println (allJobCategories.get(s));
					}
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
					if(staff.blacklistUser(allUsersList.get(user), type))
						blacklistedUsers.put("E2", allUsersList.get(user));
				}
				//registerMaintenanceStaff();
				break;

				case "4":
				{
					String user;
					System.out.println("Enter the id of the user to be reactivated:  ");
					user = input.nextLine();

					if(staff.revertBlacklistedUser((User)(allUsersList.get("E001"))))
						blacklistedUsers.remove("E1");

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
				{
					HashMap<String, Applicant> matchingApplicants = this.searchApplicants(employer);

					if(matchingApplicants != null) {
						for (Applicant appRef : matchingApplicants.values()) {
							System.out.println(appRef.toString());
						}
					}
				}
				break;

				// Shortlisting an applicant
				case "3":
				{
					System.out.println("Shortlisting applicant started.");
					this.shortListApplicant(employer);
				}
				break;

				// Rank shortlisted applicant
				case "4" :
				{
					System.out.println("Rank Applicants For Specific Job");
					this.rankApplicants(employer);
				}
				break;

				// Set possible interview times
				case "5" :
				{
					System.out.println("Set Possible Interview Times");
					this.setPossibleInterviewTimes(employer);
				}
				break;


				// Registering the complaint about the applicant
				case "6" :
				{
					this.registerComplaintAgaintApplicnt(employer);
				}
				break;

				case "Q":
					quit = true;
			}
			break;

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
		String jobDesc = customScanner.readString("Description : ");


		// This is kind of accepting required userAvailability
//		printAllAvailabilityTypes();
//		int aTypePos = customScanner.readInt("Provide availability type [0-2] : ", 0, 2);
//		AvailabilityType aType = (AvailabilityType.values())[aTypePos];
//
//		int perWeekAvailability = customScanner.readInt("Provide Availability Per Week : ", 4, 80);
//
//		System.out.print("Starting From : ");
//		Date startingFrom = this.getDateInput();
//
//		System.out.print("End Date : ");
//		Date endingOn = this.getDateInput();
//
//		printJobCategories("Active"); String [] reqJobCategoriesIds =
//		inputJobCategories(); List<JobCategory> reqJobCateObjs = new ArrayList<JobCategory>();
//
//		for(String cId : reqJobCategoriesIds)
//		{
//			reqJobCateObjs.add( this.allJobCategories.get(cId) );
//		}
//
//		UserAvailability requirements = new UserAvailability(null, aType, perWeekAvailability, startingFrom, endingOn);
//		requirements.setApplicableJobCategories( reqJobCateObjs );


		// Create new job
		Job tempJob = new Job(jobId, jobTitle, jobDesc);

		// Add that job to the employer
		try {
			Job newJob = employer.createJob(tempJob);

			// Validate
			if(newJob != null) {

				System.out.println("Job has been successfully added");
				System.out.println(newJob.toString());
			}
		}catch(DuplicateJobIdException d) {
			System.err.println(d.getMessage());
		}


		printPostedJobs( employer.getId() );
	}



	/**
	 * Employer can search through applicant's list
	 */
	public HashMap<String, Applicant> searchApplicants(Employer employer) {

		HashMap<String, Applicant> searchResults = new HashMap<String, Applicant>();

		boolean jobCValidated = true;
		String [] jobPreferencesArr = null;
		do {
			// Search Criteria
			String jobPreferencesStr = customScanner.readString("Job Categories :\n");

			// Extracting individual job preferences
			jobPreferencesArr = null;

			if(!jobPreferencesStr.isBlank() && !jobPreferencesStr.isEmpty()) {
				jobPreferencesArr = jobPreferencesStr.split(",");
			}

			// 23-09-2020 - Validate entered job categories
//			for(int c = 0; c < jobPreferencesArr.length; c++) {
//				if (allJobCategories.get(jobPreferencesArr[c]) == null) {
//					jobCValidated = false;
//					break;
//				}
//			}

		}while(!jobCValidated);


		int perWeekAvailability = customScanner.readInt("Provide Availability Per Week : ", 4, 80);

		printAllAvailabilityTypes();

		int aTypePos = customScanner.readInt("Provide availability type [0-2] : ", 0, 2);
		AvailabilityType aType = (AvailabilityType.values())[aTypePos];

		System.out.println(aType);

		System.out.print("Starting From : ");
		Date startingFrom = getDateInput();
		System.out.print("Preferred Ending date : ");
		Date endingOn = getDateInput();


		// Iterating over the applicant's list to find the matching candidates
		for(Applicant applicantRef : allApplicantsList.values()) {

			// Check the preferences and availability
			if(checkUserAvailability(applicantRef, jobPreferencesArr, perWeekAvailability, aType, startingFrom, endingOn)) {

				// Matching preference and availability
				searchResults.put(applicantRef.getId(), applicantRef);
			}
		}

		// Testing
		System.out.println(searchResults.keySet());
		return searchResults;
	}


	/**
	 * This function assesses the eligibility of applicant against given search cateria.
	 * @param applcntRef : Current applicant under eligibility check
	 * @param jobPreferencesArr : Job categories required by employer
	 * @param perWeekAvailability : Number of hours availability required by employer
	 * @param aType : Intership, parttime full time requirement
	 * @param availableFrom : starting date of job
	 * @param availableTill : ending date of job
	 * @return
	 */
	public boolean checkUserAvailability(Applicant applcntRef, String [] jobPreferencesArr, int perWeekAvailability, AvailabilityType aType, Date availableFrom, Date availableTill) {

		boolean takeThisApplicnt = false;

		// Get this user's all availabilities
		List<UserAvailability> userAvailabilities = applcntRef.getUserAvailability();

		// If at least one availability matches with the search criteria, takeThisApplicnt
		for(int a = 0; a < userAvailabilities.size(); a++) {

			UserAvailability oneAvail = userAvailabilities.get(a);

			// Validating number of hours and job availability type (internship, part-time etc)
			if (oneAvail.getNoOfHoursAWeek() < perWeekAvailability || oneAvail.getAvailabilityType() != aType) {
				continue;
			}

			// Validating that the period covers start and end date of job requirement
			if (oneAvail.getPeriodStartDate().after(availableFrom) || oneAvail.getPeriodEndDate().before(availableTill))  {
				System.out.println(applcntRef.getId());
				System.out.println(oneAvail.getPeriodStartDate().toString() + " -- "+availableFrom);
				System.out.println(oneAvail.getPeriodEndDate().toString() + " -- "+availableTill);
				continue;
			}

			System.out.println(applcntRef.getId()+" -- "+oneAvail.getApplicableJobCategoriesIds());

			// If atleast one job preference matches with given requirment, then takeThisApplicnt
			for(String sp : jobPreferencesArr) {

				if(oneAvail.getApplicableJobCategoriesIds().contains(sp)) {
					takeThisApplicnt = true;
					System.out.println(true+" for "+applcntRef.getId());
					break;
				}
			}

			if(takeThisApplicnt == true) {
				break;
			}

		}

		return takeThisApplicnt;
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


	/**
	 * @author Yogeshwar Chaudhari
	 * Employer can shortlist a person based on job
	 * Enum : EmploymentStatus
	 */
	public void shortListApplicant(Employer employer) {

		Job jobRef = null;

		// shortlisting is possible only when employer has atleast one job to begine with.
		if(employer.getPostedJobs().size() == 0) {
			System.out.println("You do not have any posted jobs. Please create jobs to be able to shortlist applicants.");
			return;
		}

		printPostedJobs( employer.getId() );

		String jobId = customScanner.readString("Job ID : ");
		jobRef = employer.getPostedJobs().get(jobId);

		HashMap<String, Applicant> searchResults = searchApplicants(employer);

		printApplicantList(searchResults);

		String repeat;
		do{
			String applicantId = customScanner.readString("Applicant Id : ");

			// Check if the id is from the search results only
			Applicant applicntRef = (searchResults.containsKey(applicantId)) ? this.allApplicantsList.get( applicantId) : null;

			// Shorting the applicant for given job
			try {
				employer.shortListCandidate(jobRef, applicntRef);
			} catch (AlreadyPresentInYourShortListedListException | ApplicantIsBlackListedException | NullApplicantException | NullJobReferenceException e) {
				System.err.println(e.getMessage());
			}

			repeat = customScanner.readYesNo("Do you want to add one more ?");

		}while(repeat.equalsIgnoreCase("Y"));

		printShortListedApplicntsEmployer(employer);

	}


	/**
	 * 23-09-2020
	 * Employer can rank the applicant. Requires job, applicant, and their rank
	 * important for sending interview invitations
	 * @param employer
	 * @author Yogeshwar Chaudhari
	 */
	public void rankApplicants(Employer employer) {

		// Ranking is possible only employer has atleast one job to begine with.
		if(employer.getPostedJobs().size() == 0) {
			System.out.println("You do not have any posted jobs. Please create jobs to be able to rank applicants.");
			return;
		}

		// Print all jobs posted by this employer
		printPostedJobs( employer.getId() );

		System.out.println("Press Q + Enter to quit");

		String jobId = customScanner.readString("Please enter the job ID : ");
		if(jobId.equalsIgnoreCase("q"))
			return;

		Job jobRef = this.getAllEmployersList().get(employer.getId()).getPostedJobs().get(jobId);

		if(jobRef != null) {

			System.out.println("All Shortlisted Applicants.");
			printApplicantList(jobRef.getShortListedApplicants());

			for(String applicantId : jobRef.getShortListedApplicants().keySet()) {


				Applicant applicntRef = this.getAllApplicantsList().get(applicantId);

				int rank = customScanner.readInt(applicantId+ " -> Rank: ");

				// Rank 'applicantRef' for 'jobRef' with number 'rank'
				try {
					employer.rankApplicant(jobRef, applicntRef, rank);

				} catch (NullJobException | NullApplicantException  e) {
					System.out.println(e.getMessage());
				}
			}
		}else {

			// Could not find job with provided ID
			System.out.println("Job ID is invalid. Please try again.");
			rankApplicants(employer);
		}

		// printings the applicants with respective ranking - For validation purpose
		if(jobRef != null) {
			for(Map.Entry<Integer, Applicant> r : jobRef.getRankedApplicants().entrySet()) {
				System.out.println(r.getKey()+" => "+r.getValue().getId());
			}
		}

	}

	/**
	 * @author Yogeshwar Chaudhari
	 * Employer should be able to change the applicant's employement status
	 * Enum : EmploymentStatus
	 */

	public void changeApplicantStatus(Employer employer) {

	}


	/**
	 * 23-09-2020
	 * @author Yogeshwar Chaudhari
	 * Employer can register compaint against any applicant.
	 */
	public void registerComplaintAgaintApplicnt(Employer emp) {

		String appcntId = this.customScanner.readString("Please enter applicant ID: ");

		User applcntRef = this.allUsersList.get(appcntId);

		String message = customScanner.readString("*Enter Message : ");
		try {

			// Create new complaint
			Complaints newComplaint = emp.complaintApplicant(applcntRef, message);

			// Add to all complaint's list
			this.allComplaints.add(newComplaint);

			((Applicant)applcntRef).setComplaintCount( ((Applicant)applcntRef).getComplaintCount() + 1 );

			// Call provisionally blacklisting login here which accepts applicant's reference

		} catch (NullApplicantException | InvalidTypeException e) {
			System.err.println(e.getMessage());
		}

	}


	/**
	 * An employer can set possible interview times.
	 * Applicant selects one of them and interview will be carried out at timing
	 * @param e
	 */
	public void setPossibleInterviewTimes(Employer e) {

		// Employer selects a job for setting possible interview times
		printPostedJobs(e.getId());
		String jobId = customScanner.readString("Job ID : ");
		Job jobRef = null;

		do {
			jobRef = e.getPostedJobs().get(jobId) != null ? e.getPostedJobs().get(jobId) : null;
		} while (jobRef == null);
		// Job ID validation ends here

		printShortListedApplicntsForJob(e, jobRef);

		// Accept all possible interview times
		List<Date> myInterviewTimes = new ArrayList<Date>();
		String repeat;
		do {

			Date possibleDate = getDateInput();
			myInterviewTimes.add(possibleDate);

			jobRef.getAvailInterviewTimings().add(possibleDate);

			repeat = customScanner.readYesNo("Do you want to add one more ?");

		} while (repeat.equalsIgnoreCase("Y"));

		System.out.println("Possible interview times have been set. Please forward this to shortlisted applicants");
	}

	/**
	 * @author Yogeshwar Chaudhari
	 * Praparing data for demonstrating Employer functionalities
	 * @throws ParseException
	 */
	public void loadDummyDataForEmployeFunctions() throws ParseException {

		Employer e = new Employer("e", "e@gmail.com", "123", "Yosha");
		try {
			e.createJob(new Job("job1", "Developer", "Developer Desc"));
			e.createJob(new Job("job2", "Analyst", "Analyst Required"));
			e.createJob(new Job("job3", "Designer", "Designer Required"));
		}
		catch (DuplicateJobIdException e1) {
			e1.printStackTrace();
		}

		Applicant a1 = new Applicant("app1", "a@gmail.com", "123", "John", "Doe", "048888888", "l");
		Applicant a2 = new Applicant("app2", "b@gmail.com", "123", "Mark", "Brown", "048942879", "l");
		Applicant a3 = new Applicant("app3", "c@gmail.com", "123", "Johny", "Ive", "048734878", "l");
		Applicant a4 = new Applicant("app4", "d@gmail.com", "123", "Sim", "Corol", "043445873", "i");
		Applicant a5 = new Applicant("app5", "e@gmail.com", "123", "Dave", "Snow", "048734878", "i");
		Applicant a6 = new Applicant("app6", "f@gmail.com", "123", "June", "Last", "039847484", "i");

		// Create some job categories
		JobCategory j1 = new JobCategory("1", "Active", 1);
		JobCategory j2 = new JobCategory("2", "Active", 2);
		JobCategory j3 = new JobCategory("3", "Active", 3);

		// Adding for system level
		this.allJobCategories.put("C1", j1);
		this.allJobCategories.put("C2", j2);
		this.allJobCategories.put("C3", j3);

		// Updating user availability
		a1.getUserAvailability().add(new UserAvailability(j1, AvailabilityType.FULL_TIME, 40, (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020")), (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2021"))));
		a2.getUserAvailability().add(new UserAvailability(j2, AvailabilityType.PART_TIME, 20, (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020")), (new SimpleDateFormat("dd/MM/yyyy").parse("11/11/2021"))));
		a3.getUserAvailability().add(new UserAvailability(j3, AvailabilityType.INTERNSHIP, 30, (new SimpleDateFormat("dd/MM/yyyy").parse("10/11/2020")), (new SimpleDateFormat("dd/MM/yyyy").parse("10/03/2021"))));
		a4.getUserAvailability().add(new UserAvailability(j3, AvailabilityType.FULL_TIME, 40, (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020")), (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020"))));
		a5.getUserAvailability().add(new UserAvailability(j1, AvailabilityType.PART_TIME, 20, (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020")), (new SimpleDateFormat("dd/MM/yyyy").parse("23/12/2020"))));
		a6.getUserAvailability().add(new UserAvailability(j1, AvailabilityType.PART_TIME, 20, (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020")), (new SimpleDateFormat("dd/MM/yyyy").parse("20/10/2020"))));

		// Populating all users list with dummy users
		this.allUsersList.put("app1", a1);
		this.allUsersList.put("app2", a2);
		this.allUsersList.put("app3", a3);
		this.allUsersList.put("app4", a4);
		this.allUsersList.put("app5", a5);
		this.allUsersList.put("app6", a6);

		this.allApplicantsList.put("app1", a1);
		this.allApplicantsList.put("app2", a2);
		this.allApplicantsList.put("app3", a3);
		this.allApplicantsList.put("app4", a4);
		this.allApplicantsList.put("app5", a5);
		this.allApplicantsList.put("app6", a6);

		// Adding employer
		this.allUsersList.put("e", e);
		this.allEmployersList.put("e", e);

		printApplicantList();
		printPostedJobs(e.getId());
	}

	/**
	 * @author Yogeshwar Chaudhari
	 * Prints the list of all applicants in the system
	 */
	public void printApplicantList() {

		System.out.println("List of all applicants");

		System.out.format("%3s%10s%32s%10s\n", "---+","----------+","--------------------------------+","----------+");
		System.out.format("%-3s|%-10s|%-32s|%-10s|\n", "Sr", "ID", "Full Name", "JC Pref");
		System.out.format("%3s%10s%32s%10s\n", "---+","----------+","--------------------------------+","----------+");

		int i = 1;
		for(Applicant app : this.allApplicantsList.values()) {
			System.out.format("%-3s|%-10s|%-32s|%-10s|\n", i, app.getId(), app.getFirstName()+" "+app.getLastName(), app.getJobPreferences());
			System.out.format("%3s%10s%32s%10s\n", "---+","----------+","--------------------------------+","----------+");
			i++;
		}

	}

	/**
	 * 23-09-2020
	 * Prints the list of applicants when list is provided
	 * The provided list could be, shortlisted applicants, blacklisted applicants likewise
	 * @author Yogeshwar Chaudhari
	 */
	public void printApplicantList(HashMap<String, Applicant> applicantsList) {

		System.out.format("%3s%10s%32s%10s\n", "---+","----------+","--------------------------------+","----------+");
		System.out.format("%-3s|%-10s|%-32s|%-10s|\n", "Sr", "ID", "Full Name", "JC Pref");
		System.out.format("%3s%10s%32s%10s\n", "---+","----------+","--------------------------------+","----------+");

		int i = 1;
		for(Applicant app : applicantsList.values()) {
			System.out.format("%-3s|%-10s|%-32s|%-10s|\n", i, app.getId(), app.getFirstName()+" "+app.getLastName(), app.getJobPreferences());
			System.out.format("%3s%10s%32s%10s\n", "---+","----------+","--------------------------------+","----------+");
			i++;
		}

	}


	/**
	 * @author Yogeshwar Chaudhari
	 * Prints the list of all posted jobs for the test employer "e"
	 */
	public void printPostedJobs(String id) {

		Employer e = this.allEmployersList.get(id);

		System.out.println("List of All Posted Jobs By Employer : "+e.getId());
		System.out.format("%3s%10s%32s%20s\n", "---+","----------+","--------------------------------+","--------------------+");
		System.out.format("%-3s|%-10s|%-32s|%-20s|\n", "Sr", "ID", "Job Title", "Desc");
		System.out.format("%3s%10s%32s%20s\n", "---+","----------+","--------------------------------+","--------------------+");

		int i = 1;
		for(Job j : this.allEmployersList.get( id ).getPostedJobs().values()) {
			System.out.format("%-3s|%-10s|%-32s|%-20s|\n", i, j.getJobId(), j.getJobTitle(), j.getJobDesc());
			System.out.format("%3s%10s%32s%20s\n", "---+","----------+","--------------------------------+","--------------------+");
			i++;
		}
	}


	/**
	 * @author Yogeshwar Chaudhari
	 * Print ShortListed Applicants for every job from employer
	 */
	public void printShortListedApplicntsEmployer(Employer e) {

		System.out.println("List of all ShortListed Applicants For All Jobs");

		System.out.format("%3s%10s%32s%10s\n", "---+","----------+","--------------------------------+","----------+");
		System.out.format("%-3s|%-10s|%-32s|%-10s|\n", "Sr", "ID", "Full Name", "JC Pref");
		System.out.format("%3s%10s%32s%10s\n", "---+","----------+","--------------------------------+","----------+");

		for(Job jobRef : e.getPostedJobs().values()) {
			System.out.println(jobRef.getJobId()+" "+jobRef.getJobTitle());

			int i = 1;
			for(Applicant app : jobRef.getShortListedApplicants().values()) {
				System.out.format("%-3s|%-10s|%-32s|%-10s|\n", i, app.getId(), app.getFirstName()+" "+app.getLastName(), app.getJobPreferences());
				System.out.format("%3s%10s%32s%10s\n", "---+","----------+","--------------------------------+","----------+");
				i++;
			}
		}
	}

	/**
	 *
	 * @param e : Employer reference
	 * @param jobR : Job Reference
	 */
	public void printShortListedApplicntsForJob(Employer e, Job jobR) {

		System.out.println("List of all ShortListed Applicants For Job : "+jobR.getJobId());

		for(Job jobRef : e.getPostedJobs().values()) {

			if(jobRef.getJobId().equalsIgnoreCase( jobR.getJobId() )){
				System.out.println(jobRef.getJobId()+" "+jobRef.getJobTitle());

				System.out.format("%3s%10s%32s%10s\n", "---+","----------+","--------------------------------+","----------+");
				System.out.format("%-3s|%-10s|%-32s|%-10s|\n", "Sr", "ID", "Full Name", "JC Pref");
				System.out.format("%3s%10s%32s%10s\n", "---+","----------+","--------------------------------+","----------+");

				int i = 1;
				for(Applicant app : jobRef.getShortListedApplicants().values()) {
					System.out.format("%-3s|%-10s|%-32s|%-10s|\n", i, app.getId(), app.getFirstName()+" "+app.getLastName(), app.getJobPreferences());
					System.out.format("%3s%10s%32s%10s\n", "---+","----------+","--------------------------------+","----------+");
					i++;
				}
			}
		}
	}


	/**
	 * Prints all the availability types in the system.
	 * Can be used while making selection in case of search, or accepting user input
	 */
	public void printAllAvailabilityTypes(){

		AvailabilityType [] aTypes = AvailabilityType.values();

		for(int i = 0; i < aTypes.length; i++) {
			if(aTypes[i] != AvailabilityType.UNKNOWN) {
				System.out.println(i+" - "+aTypes[i]);
			}
		}
	}


	/**
	 * Prints out all the job actegories available in the system
	 *
	 */
	public void printJobCategories(String status) {

		for (String s: allJobCategories.keySet())
		{
			if(allJobCategories.get(s).getStatus().equalsIgnoreCase( status )) {
				System.out.println (allJobCategories.get(s));
			}
		}

	}

	/**
	 *
	 * @return
	 */
	public String[] inputJobCategories() {

		boolean jobCValidated = true;
		String [] jobPreferencesArr = null;
		do {
			// Search Criteria
			String jobPreferencesStr = customScanner.readString("Job Categories (, separated)  :\n");

			// Extracting individual job preferences
			jobPreferencesArr = null;

			if(!jobPreferencesStr.isBlank() && !jobPreferencesStr.isEmpty()) {
				jobPreferencesArr = jobPreferencesStr.split(",");
			}

			// 23-09-2020 - Validate entered job categories
//			for(int c = 0; c < jobPreferencesArr.length; c++) {
//				if (allJobCategories.get(jobPreferencesArr[c]) == null) {
//					System.err.println("Invalid job category "+jobPreferencesArr[c]." Try again.");
//					jobCValidated = false;
//					break;
//				}
//			}

		}while(!jobCValidated);

		return jobPreferencesArr;

	}

	public HashMap<String, Applicant> getAllApplicantsList() {
		return allApplicantsList;
	}



	public void setAllApplicantsList(HashMap<String, Applicant> allApplicantsList) {
		this.allApplicantsList = allApplicantsList;
	}



	public HashMap<String, Employer> getAllEmployersList() {
		return allEmployersList;
	}



	public void setAllEmployersList(HashMap<String, Employer> allEmployersList) {
		this.allEmployersList = allEmployersList;
	}



	public HashMap<String, JobCategory> getAllJobCategories() {
		return allJobCategories;
	}



	public void setAllJobCategories(HashMap<String, JobCategory> allJobCategories) {
		this.allJobCategories = allJobCategories;
	}


}

