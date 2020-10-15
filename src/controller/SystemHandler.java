package controller;

//import com.mashape.unirest.http.exceptions.UnirestException;
import global.*;
import view.*;
import customUtils.*;
import model.entities.*;
import model.enums.AvailabilityType;
import model.exceptions.*;
import model.enums.*;


import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.sun.jdi.InvalidTypeException;

//import javax.mail.MessagingException;
import javax.management.InvalidApplicationException;

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

		
	public void run() throws BadQualificationException, DuplicateEntryException, DuplicateJobCategoryException, ParseException, NotFullyBlacklistedUserException, NotProvisionallyBlacklistedUserException, BlacklistedTimeNotElapsedException {
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

	public void register() throws ParseException {
		boolean quit = false;
		Menu menu = null;



		try {
			menu = new Menu("register_menu_options");
		} catch (Exception e) {
			System.out.println();
		}


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
					showEmployerMenu(retEmp);
					break;

				case "Q":
					break;

			}


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
				showApplicantMenu(applicant);
			} else {
				System.out.println("Wrong choice. Please try again");
			}
		} while(!exitLoop);

	}

	public void setUserDetails(){
		boolean idFound;
		do {
			idFound = false;
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

	public void login () throws BadQualificationException, DuplicateEntryException, DuplicateJobCategoryException, ParseException, NotFullyBlacklistedUserException, NotProvisionallyBlacklistedUserException, BlacklistedTimeNotElapsedException {

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

	public void showUserMenu(User user) throws BadQualificationException, DuplicateEntryException, DuplicateJobCategoryException, ParseException, NotFullyBlacklistedUserException, NotProvisionallyBlacklistedUserException, BlacklistedTimeNotElapsedException {
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
					addUpdateReferences(applicant);
					break;

				case "3":
					addUpdateEmploymentHistory(applicant);
					break;

				case "4":
					addUpdateLicenses(applicant);
					break;

				case "5":
					uploadApplicantCV(applicant);
					break;

				case "6":
					addUpdateJobPreferences(applicant);
					break;

				case "9":
					selectInterviewTiming(applicant);
					break;

				case "10":
					respondToJobOffer(applicant);
					break;

				case "11":
					changeCredentials(applicant);
					break;

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
			updateQualification(applicant);
		} else if (operation.equals("view")){
			showQualification(applicant);
		} else {
			System.out.println("Exiting Sub Menu");
			return ;
		}
	}

	public void addUpdateReferences(Applicant applicant){

	}

	public void addUpdateEmploymentHistory(Applicant applicant){
		boolean continueMessageDisplay = true;
		do {
			try {
				System.out.println("\n****** Employment Records ******");
				String operation = subMenu();
				if (operation.equals("add")) {
					addEmploymentHistory(applicant);
				} else if (operation.equals("update")) {
					updateEmploymentHistory(applicant);
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

	public void updateEmploymentHistory(Applicant applicant){
		String printStatement;

		if(!showEmploymentRecords(applicant)){
			return;
		}
		int numOfRecords = applicant.getEmploymentHistory().size();
		printStatement = "Enter the Employment record number to update : ";
		System.out.print(printStatement);
		int recordNo = validInput(printStatement, numOfRecords) - 1;

		String companyName, designation;
		Date startDate, endDate;
		boolean currentCompany;

		EmploymentRecord updateEmpRec = applicant.getEmploymentHistory().get(recordNo);

		companyName = updateEmpRec.getCompanyName();
		designation = updateEmpRec.getDesignation();
		startDate = updateEmpRec.getStartDate();
		endDate = updateEmpRec.getEndDate();
		currentCompany = updateEmpRec.getCurrentCompany();

		boolean runLoop = true;

		do{
			System.out.println("\n===Details that can be updated===");
			System.out.println("1. Company Name\n" +
					"2. Start Date\n" +
					"3. End Date\n" +
					"4. Designation\n" +
					"5. Current Company Status\n" +
					"U. Update and exit.\n" +
					"Q. Exit without updating");
			System.out.print("Enter Option to update : ");
			String choice = Global.scanner.nextLine();

			switch(choice){
				case "1":
					System.out.print("Enter the Company name to update : ");
					companyName = Global.scanner.nextLine();
					break;
				case "2":
					System.out.print("Enter the start date to update : ");
					startDate = getDateInput();
					break;
				case "3":
					System.out.print("Enter the end date to update : ");
					endDate = getDateInput();
					break;
				case "4":
					System.out.print("Enter the Designation to update : ");
					designation = Global.scanner.nextLine();
					break;
				case "5":
					boolean exitLoop = true;
					do{
						System.out.print("Are you still working in this company?(Y/N): ");
						String input= Global.scanner.nextLine();
						if(input.equalsIgnoreCase("y")) {
							currentCompany = true;
							endDate = null;
							exitLoop = true;
						} else if (input.equalsIgnoreCase("n")){
							currentCompany = false;
							System.out.print("Enter the end date to update : ");
							endDate = getDateInput();
							exitLoop = true;
						} else {
							System.out.println("Wrong Input!! Enter Y or N");
							exitLoop = false;
						}
					} while (!exitLoop);
					break;
				case "U":
					EmploymentRecord updateEmpRecord = new EmploymentRecord(companyName, designation, startDate, endDate, currentCompany);
					try {
						applicant.updateEmploymentRecords(updateEmpRecord, recordNo);
						System.out.println("Employment Record updated successfully.");
						System.out.println(applicant.getEmploymentHistory().get(recordNo));
					} catch (NoSuchRecordException | BadEmployeeRecordException e) {
						System.out.println(e);
					}
					runLoop = false;
					break;
				case "Q":
					System.out.println("Exiting without updating.");
					runLoop = false;
				default:
					System.out.println("Wrong input!!!\nKindly check option and try again.");
					break;
			}
		} while (runLoop);
	}

	public void updateQualification(Applicant applicant){
		String printStatement;
		List<Qualification> qualifications = applicant.getQualifications();
		int numOfQualifications = qualifications.size();

		if (numOfQualifications == 0){
			System.out.println("No Qualification Exists. Please Add a Qualification to profile.");
			return;
		}

		int recordNo;
		for (int i =0 ; i < numOfQualifications; i++){
			recordNo = i+1;
			System.out.println(recordNo+". "+ qualifications.get(i));
		}

		printStatement = "Enter the qualification number to update : ";
		System.out.print(printStatement);

		recordNo = validInput(printStatement, numOfQualifications) - 1;
		String qualificationLevel, fieldOfStudy;
		double marks;
		Date startDate, endDate;
		Qualification updateQual = qualifications.get(recordNo);

		qualificationLevel = updateQual.getQualificationLevel();
		fieldOfStudy = updateQual.getFieldOfStudy();
		marks = updateQual.getMarksObtained();
		startDate = updateQual.getStartDate();
		endDate = updateQual.getEndDate();

		boolean exitLoop = false;

		do{
			System.out.println("\n===Details that can be updated===");
			System.out.println("1. Qualification Level\n" +
							"2. Start Date\n" +
							"3. End Date\n" +
							"4. Field of Study\n" +
							"5. Marks Obtained\n" +
							"U. Update and exit.\n" +
							"Q. Exit without updating");
			System.out.print("Enter Option to update : ");
			Global.scanner.nextLine();
			String choice = Global.scanner.nextLine();

			switch(choice){
				case "1":
					System.out.print("Enter the Qualification to update : ");
					qualificationLevel = Global.scanner.nextLine();
					break;
				case "2":
					System.out.print("Enter the start date to update : ");
					startDate = getDateInput();
					break;
				case "3":
					System.out.print("Enter the end date to update : ");
					endDate = getDateInput();
					break;
				case "4":
					System.out.print("Enter the Field of Study to update : ");
					fieldOfStudy = Global.scanner.nextLine();
					break;
				case "5":
					printStatement = "Enter the marks Obtained : ";
					System.out.print(printStatement);
					marks = doubleInput(printStatement);
				case "U":
					Qualification updateQualification = new Qualification(qualificationLevel, startDate, endDate, fieldOfStudy, marks);
					try {
						applicant.updateQualifications(updateQualification, recordNo);
						System.out.println("Qualification updated successfully.");
						System.out.println(applicant.getQualifications().get(recordNo));
					} catch (NoSuchRecordException | BadEntryException e) {
						System.out.println(e);
					}
					exitLoop = true;
					break;
				case "Q":
					System.out.println("Exiting without updating.");
					exitLoop = true;
				default:
					System.out.println("Wrong input!!!\nKindly check option and try again.");
					break;

			}
		}while(!exitLoop);
	}

	public int validInput(String printStatement, int maxValue){
		int intInput = 0;
		boolean flag = false;
		do {
			try {
				intInput = Global.scanner.nextInt();
				if (intInput > maxValue || intInput <= 0){
					System.out.println("Invalid Record Number!!\n Please check list of options.\n"+ printStatement);
					flag = false;
				} else {
					flag = true;
				}

			}
			catch (InputMismatchException e) {
				input.nextLine();
				System.out.print("\nInvalid Input Type!!\nProgram is expecting an integer input.\n"+ printStatement);
			}
		} while (!flag);
		return intInput;
	}

	public double doubleInput(String printStatement){
		double doubleInput = 0.0;
		boolean doubleFlag = false;
		do {
			try {
				doubleInput = Global.scanner.nextDouble();
				doubleFlag = true;
			}
			catch(InputMismatchException e) {
				Global.scanner.nextLine();
				System.out.print("\nInvalid Input Type!!\nApplication is expecting an decimal(double) input.\n" + printStatement);
			}
		} while (!doubleFlag);
		return doubleInput;
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

	private void addUpdateJobPreferences(Applicant applicant){
		boolean continueMessageDisplay = true;
		do {
			try {
				System.out.println("\n****** Job Preferences ******");
				String operation = subMenu();
				if (operation.equals("add")) {
					addJobPreference(applicant);
				} else if (operation.equals("update")) {
					updateJobPreference(applicant);
				} else if (operation.equals("view")) {
					showJobPreferences(applicant);
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

	private boolean showEmploymentRecords(Applicant applicant){
		List<EmploymentRecord> allEmploymentHistory = applicant.getEmploymentHistory();

		if(allEmploymentHistory.size() == 0){
			System.out.println("No Employment Records present. Please add Employment history to profile.");
			return false;
		}else{
			for(int i=0; i<allEmploymentHistory.size();i++){
				System.out.println("Employment Record "+(i+1)+":");
				System.out.println(allEmploymentHistory.get(i).toString());
			}
			return true;
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
		} catch (BadQualificationException | DuplicateEntryException | BadEntryException e){
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
			updateLicense(applicant);
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
		} catch (DuplicateEntryException | BadEntryException e) {
			System.out.println(e);
			System.out.println("!! Adding License failed !!");
		}
	}

	public boolean showLicenses(Applicant applicant){
		List<License> licenses = new ArrayList<License>();
		licenses = applicant.getLicenses();

		if (licenses.size() == 0){
			System.out.println("No License Exist. Please add License to the profile.");
			return false;
		}
		int i =1;
		for (License license: licenses){
			System.out.println("License "+ i++ + ". " + license);
		}
		return true;
	}

	public void updateLicense(Applicant applicant){
		String printStatement;

		if(!showLicenses(applicant)){
			return;
		}
		int numOfLicenses = applicant.getLicenses().size();
		printStatement = "Enter the License number to update : ";
		System.out.print(printStatement);
		int recordNo = validInput(printStatement, numOfLicenses) - 1;

		String type, id;
		Date validTill;

		License updateLicense = applicant.getLicenses().get(recordNo);

		type = updateLicense.getType();
		id = updateLicense.getId();
		validTill = updateLicense.getValidTill();

		boolean runLoop = true;

		do {
			System.out.println("\n===Details that can be updated===");
			System.out.println("1. Type\n" +
					"2. Identification Number\n" +
					"3. Validity\n" +
					"U. Update and exit.\n" +
					"Q. Exit without updating");
			System.out.print("Enter Option to update : ");
			String choice = Global.scanner.nextLine();

			switch(choice){
				case "1":
					System.out.print("Enter the Type to update : ");
					type = Global.scanner.nextLine();
					break;
				case "2":
					System.out.print("Enter the ID to update : ");
					id = Global.scanner.nextLine();
					break;
				case "3":
					System.out.print("Enter the Valid till date to update : ");
					validTill = getDateInput();
					break;
				case "U":
					License newLicense = new License(type,id, validTill);
					try {
						applicant.updateLicense(newLicense, recordNo);
						System.out.println("License updated successfully.");
						System.out.println(applicant.getLicenses().get(recordNo));
					} catch (NoSuchRecordException | BadEntryException e) {
						System.out.println(e);
					}
					runLoop = false;
					break;
				case "Q":
					System.out.println("Exiting without updating.");
					runLoop = false;
				default:
					System.out.println("Wrong input!!!\nKindly check option and try again.");
					break;
			}

		} while (runLoop);

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

	public AvailabilityType getAvailabilityType(){
		boolean runLoop = true;
		do{
			System.out.print("Enter the Job Role: " +
					"\nP --> Part time" +
					"\nF --> Full Time" +
					"\nI --> Internship" +
					"\nChoice: ");
			String input = Global.scanner.nextLine();
			input.toUpperCase();
			switch(input){
				case "P":
					return AvailabilityType.PART_TIME;
				case "F":
					return AvailabilityType.FULL_TIME;
				case "I":
					return AvailabilityType.INTERNSHIP;
				default:
					System.out.println("Invalid Input!!\n Kindly try again");
					break;
			}
		}while(runLoop);
		return null;
	}

	public int getNoOfHours(){
		System.out.print("Number of Hours per week: ");
		 return Global.scanner.nextInt();
	}

	public Date dateForAvailability(String date){
		System.out.print(date);
		return getDateInput();
	}

	public ArrayList<JobCategory> getJobCategoriesInputs(ArrayList arrayList){

		ArrayList<JobCategory> selectedJobCategories = arrayList;
		boolean repeatFlag = false;
		boolean wrongFlag = false;

		for (String key: allJobCategories.keySet()){
			System.out.println(allJobCategories.get(key));
		}
		System.out.println("Add categories comma sepearted: ");
		String options = Global.scanner.nextLine();

		List<String> ids = Arrays.asList(options.split(","));

		for(String id : ids){
			if (allJobCategories.get(id) != null){
				if (!repetitiveID(id, selectedJobCategories))
				 selectedJobCategories.add(allJobCategories.get(id));
				else
				 repeatFlag = true;
			} else {
				wrongFlag = true;
			}
		}
		if (repeatFlag){
			System.out.println("Same job category entered more than once");
		} else if(wrongFlag){
			System.out.println("Wrong categories entered");
		}
		return selectedJobCategories;
	}

	public ArrayList<JobCategory> getJobCategories(ArrayList arrayList){

		ArrayList<JobCategory> applicableJobCategories  = new ArrayList<>();
		boolean exit;
		do{
			exit = true;
			applicableJobCategories = getJobCategoriesInputs(arrayList);

			if (applicableJobCategories.size() == 0){
				System.out.println("No Job categories entered");
				String input = customScanner.readYesNo("Would you like to enter job categories again(yes/no): ");
				if(input.equalsIgnoreCase("yes")){
					exit = false;
				} else {
					System.out.println("Taking no Job categories for now. You can update later");
					applicableJobCategories = null;
				}
			} else {
				System.out.println("Selected Job Categories");
				for (JobCategory category: applicableJobCategories){
					System.out.println(category.getId());
				}
				System.out.println("Adding the Job Preference in the profile");
			}
		} while(!exit);
		return applicableJobCategories;
	}

	public boolean repetitiveID(String id, List<JobCategory> jobCategories){

		if (jobCategories.isEmpty()) return false;

		for(JobCategory category: jobCategories){
			if (id.equals(category.getId())){
				return true;
			}
		}
		return false;
	}

	public void addJobPreference(Applicant applicant) throws DuplicateEntryException{

		ArrayList<JobCategory> applicableJobCategories = new ArrayList<>();
		AvailabilityType availabilityType;
		int noOfHoursAWeek;
		Date periodStartDate = null;
		Date periodEndDate = null;

		System.out.println("Enter Below details for adding Job Preference\n");

		availabilityType = getAvailabilityType();
		noOfHoursAWeek = getNoOfHours();
		Global.scanner.nextLine();
		periodStartDate = dateForAvailability("Start Date: ");
		periodEndDate = dateForAvailability("End Date: ");
		applicableJobCategories = getJobCategories(applicableJobCategories);

		try {
			applicant.addAvailability(availabilityType, applicableJobCategories, noOfHoursAWeek, periodStartDate, periodEndDate);
			System.out.println("Job Preference added successfully. Below are the list of Job Preferences:-");
			showJobPreferences(applicant);
		} catch (BadEntryException | DuplicateEntryException e) {
			System.out.println(e);
			System.out.println("Adding Job Preference failed!!");
		}

	}

	public boolean showJobPreferences(Applicant applicant){
		List<UserAvailability> allJobPreferences = applicant.getUserAvailability();

		if(allJobPreferences.size() == 0){
			System.out.println("No Job preferences present. Please add job preferences to your profile.");
			return false;
		}else{
			for(int i=0; i<allJobPreferences.size();i++){
				System.out.println("Job Preference "+(i+1)+":");
				System.out.println(allJobPreferences.get(i).toString());
			}
			return true;
		}
	}

	public void updateJobPreference(Applicant applicant){
		String printStatement;

		if(!showJobPreferences(applicant)){
			return;
		}
		int numOfRecords = applicant.getUserAvailability().size();
		printStatement = "Enter the Job Preference number to update : ";
		System.out.print(printStatement);
		int recordNo = validInput(printStatement, numOfRecords) - 1;

		UserAvailability updateAvailability = applicant.getUserAvailability().get(recordNo);

		List<JobCategory> applicableJobCategories = updateAvailability.getApplicableJobCategories();
		AvailabilityType availabilityType = updateAvailability.getAvailabilityType();
		int noOfHoursAWeek = updateAvailability.getNoOfHoursAWeek();
		Date periodStartDate = updateAvailability.getPeriodStartDate();
		Date periodEndDate = updateAvailability.getPeriodEndDate();

		boolean runLoop = true;

		do{
			System.out.println("\n===Details that can be updated===");
			System.out.println("1. Availability Type\n" +
					"2. Start Date\n" +
					"3. End Date\n" +
					"4. Working hours per week\n" +
					"5. Edit Job Categories\n" +
					"U. Update and exit.\n" +
					"Q. Exit without updating");
			System.out.print("Enter Option to update : ");
			String choice = Global.scanner.nextLine();

			switch(choice){
				case "1":
					availabilityType = getAvailabilityType();
					break;
				case "2":
					periodStartDate = dateForAvailability("Period Start Date: ");
					break;
				case "3":
					periodEndDate = dateForAvailability("Period End Date: ");

					break;
				case "4":
					noOfHoursAWeek = getNoOfHours();
					break;
				case "5":
					applicableJobCategories = getJobCategories((ArrayList) applicableJobCategories);
					break;
				case "U":
					UserAvailability availability = new UserAvailability(applicableJobCategories, availabilityType, noOfHoursAWeek, periodStartDate, periodEndDate);
					try {
						applicant.updateAvailability(availability, recordNo);
						System.out.println("Job Preference updated successfully.");
						System.out.println(applicant.getUserAvailability().get(recordNo));
					} catch (NoSuchRecordException |  BadEntryException e) {
						System.out.println(e);
					}
					runLoop = false;
					break;
				case "Q":
					System.out.println("Exiting without updating.");
					runLoop = false;
				default:
					System.out.println("Wrong input!!!\nKindly check option and try again.");
					break;
			}
		} while (runLoop);
	}

	public void selectInterviewTiming(Applicant applicant){
		List<JobApplication> jobApplications = applicant.getJobApplications();
		if(jobApplications.size() == 0){
			System.out.println("You currently have no pending Interview Timings");
		}else{
			System.out.println("You have "+ jobApplications.size()+" pending interview(s)");
			StringBuilder builder = new StringBuilder();
			for(int i=0; i< jobApplications.size(); i++){
				builder.append(i+1).append(" : ").append(jobApplications.get(i).getJobRef().getJobTitle()).append("\n");
			}
			System.out.println(builder.toString());
			String choice;
			do {
				System.out.print("Enter the Job number for which you want to select interview timing: ");
				choice = Global.scanner.nextLine();
				if (!isInt(choice) || (Integer.valueOf(choice) > jobApplications.size())) {
					System.out.println("Wrong Choice. Please try again.");
				}else{
					break;
				}
			}while(true);

			JobApplication selectedJobApplication = jobApplications.get(Integer.valueOf(choice)-1);
			int dateIndex = fetchPreferredInterViewDate(selectedJobApplication);
			if(dateIndex != -1){
				boolean updateSuccessful = applicant.selectInterviewTiming(Integer.valueOf(choice)-1, dateIndex);
				if(updateSuccessful){
					System.out.println("Job Interview has been set for "+ new SimpleDateFormat("dd/MM/yyyy").format(selectedJobApplication.getJobRef().getAvailInterviewTimings().get(dateIndex)));
				}else{
					System.out.println("Error setting Job Interview Time. Please try again later.");
				}
			}
		}
	}

	public int fetchPreferredInterViewDate(JobApplication selectedJobApplication){

		List<Date> availableDates = selectedJobApplication.getJobRef().getAvailInterviewTimings();
		if(availableDates.size() == 0){
			System.out.println("No dates available");
			return -1;
		}else{
			StringBuilder builder = new StringBuilder();
			builder.append("The following date(s) are available for the interview\n");
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			for(int i=0; i<availableDates.size(); i++){
				builder.append(i+1).append(" : ").append(formatter.format(availableDates.get(i))).append("\n");
			}
			System.out.println(builder.toString());
			String choice;
			do {
				System.out.print("Enter the number corresponding to your preferred date (Enter Q to go back to the previous menu): ");
				choice = Global.scanner.nextLine();
				if(choice.equalsIgnoreCase("q")){
					return -1;
				}else if (!isInt(choice) || (Integer.valueOf(choice) > availableDates.size())) {
					System.out.println("Wrong Choice. Please try again.");
				}else{
					break;
				}
			}while(true);

			return Integer.valueOf(choice)-1;
		}

	}

	private boolean isInt(String value){
		try {
			Integer.valueOf(value);
			return true;
		}catch (NumberFormatException e){
			return false;
		}
	}

	public void respondToJobOffer(Applicant applicant){

		List<JobApplication> jobApplications = applicant.getJobApplications();
		if(applicant.getEmploymentStatus() == EmploymentStatus.EMPLOYED){
			System.out.println("Employed applicants cannot accept more job offers");
			return;
		}
		if(jobApplications.size() == 0){
			System.out.println("You currently have no pending Job Offer(s)");
		}else{
			boolean jobOffersFound= false;
			StringBuilder builder = new StringBuilder();
			builder.append("You have the following pending Job Offer(s)\n");
			for(int i=0; i< jobApplications.size(); i++){
				JobApplication currApplication = jobApplications.get(i);
				if(currApplication.getOfferRef() != null && currApplication.getOfferRef().getOfferStatus() == OfferStatus.PENDING) {
					builder.append(i + 1).append(" : ").append(currApplication.getJobRef().getJobTitle()).append("\n");
					jobOffersFound = true;
				}
			}
			if(!jobOffersFound){
				System.out.println("You currently have no pending Job Offer(s)");
				return;
			}
			System.out.println(builder.toString());
			String choice;
			do {
				System.out.print("Enter the Job number (or Enter Q to go back to the previous menu): ");
				choice = Global.scanner.nextLine();
				if(choice.equalsIgnoreCase("q")){
					return;
				}else if (!isInt(choice) || (Integer.valueOf(choice) > jobApplications.size())) {
					System.out.println("Wrong Choice. Please try again.");
				}else{
					break;
				}
			}while(true);

			JobApplication selectedJobApplication = jobApplications.get(Integer.valueOf(choice)-1);
			int decision = fetchJobOfferChoice(selectedJobApplication);
			if(decision != -1){
				boolean updateSuccessful = applicant.replyToJobOffer(Integer.valueOf(choice)-1, decision);
				if(updateSuccessful && applicant.getEmploymentStatus() == EmploymentStatus.EMPLOYED){
					System.out.println("Congratulations!! You have accepted this job");
				}else if(updateSuccessful && applicant.getEmploymentStatus() != EmploymentStatus.EMPLOYED){
					System.out.println("You have successfully responded to the JobOffer");
				}else{
					System.out.println("Error setting Job Interview Time. Please try again later.");
				}
			}else{
				respondToJobOffer(applicant);
			}
		}
	}

	public void changeCredentials(Applicant applicant){

		String printStatement;

		System.out.println("1. Change User id\n" +
				"2. Change password\n" +
				"3. Go back to previous menu");
		printStatement = "Enter the option to change : ";
		System.out.print(printStatement);
		int option = validInput(printStatement, 2);

		switch(option) {
			case 1:
				changeUserId(applicant);
				break;
			case 2:
				changePassword(applicant);
				break;
			case 3:
				break;
			default:
				System.out.println("Wrong option selected!! Kindly try again");
				break;
		}
	}

	public void changeUserId(Applicant applicant){
		boolean idFound;
		do {
			idFound = false;
			Global.scanner.nextLine();
			System.out.print("Enter the new User id: ");
			id = Global.scanner.nextLine();
			if (!freeIdCheck(id)){
				System.out.println("This user id is already taken");
				idFound = true;
			}
		}while(idFound);
		allUsersList.remove(id);
		allApplicantsList.remove(id);
		applicant.setId(id);
		allUsersList.put(applicant.getId(), applicant);
		allApplicantsList.put(applicant.getId(),applicant);
		System.out.println("User id changed to : "+ applicant.getId());

	}

	public void changePassword(Applicant applicant){
		String statement = "\nNew Password must be different than the current password.\n";
		System.out.println(statement +
				"You must enter the current password to authenticate to make this change");
		Global.scanner.nextLine();
		String currentPassword = authenticate(applicant);
		boolean runLoop;
		do{
			runLoop = true;

			System.out.print("Enter new password : ");
			String newPassword = Global.scanner.nextLine();
			if (newPassword.equals(currentPassword)){
				System.out.println(statement);
			} else {
				applicant.setPassword(newPassword);
				System.out.println("**Password Change successful**");
				runLoop = false;
			}
		}while(runLoop);
	}

	public String authenticate(Applicant applicant){
		boolean runLoop;
		do{
			runLoop = true;
			System.out.print("Enter current password : ");
			String password = Global.scanner.nextLine();
			if (password.equals(applicant.getPassword())){
				return password;
			} else {
				System.out.println("Wrong Password");
			}
		} while(runLoop);
		return null;
	}

	/**
	 * This function prompts user to accept or reject a JobOffer
	 * Possible return value:
	 * 1-> Accept,
	 * 0-> Reject,
	 * -1-> Undecided or force quit
	 */
	public int fetchJobOfferChoice(JobApplication selectedJobApplication){

		StringBuilder builder = new StringBuilder();
		builder.append("Job Offer for ").append(selectedJobApplication.getJobRef().getJobTitle()).append("\n");
		builder.append("1 : Accept\n").append("2 : Reject\n").append("3 : Decide Later");
		System.out.println(builder.toString());
		String choice;
		do {
			System.out.print("Enter the your choice: ");
			choice = Global.scanner.nextLine();
			if (!isInt(choice) || (Integer.valueOf(choice) > 3)) {
				System.out.println("Wrong Choice. Please try again.");
			}else{
				break;
			}
		}while(true);

		int decision= -1;

		switch (Integer.valueOf(choice)){
			case 1: decision= 1;
					break;
			case 2: decision= 0;
					break;
		}
		return decision;
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

	public void showMaintenanceStaffMenu(MaintenanceStaff staff) throws DuplicateJobCategoryException, NotFullyBlacklistedUserException, ParseException, NotProvisionallyBlacklistedUserException, BlacklistedTimeNotElapsedException
	{
		boolean quit = false;
		Menu menu = null;

		try {
			menu = new Menu("maintenancestaff_menu_options");
		} catch (Exception e) {
			System.out.println();
		}

		BlacklistStatus blacklistStatus = BlacklistStatus.NOT_BLACKLISTED ;


		allUsersList.put("E001", new Employer("E001", "E@mail.com", "Emp123", "Test" ,"Employer", "123"));
		allUsersList.put("E003", new Employer("E003", "E@mail.com", "Emp123", "Test" ,"Employer", "123"));
		allUsersList.put("E002", new Employer("E002", "E2@mail.com", "Employer2", "Test" ,"Employer2", "123"));
		Complaints testComplaint = new Complaints((Applicant)this.allUsersList.get("app6"),(Employer)this.allUsersList.get("E001") , "Test Complaint");

		allEmployersList.put(allUsersList.get("E001").getId(),(Employer)allUsersList.get("E001"));
		allEmployersList.put(allUsersList.get("E002").getId(),(Employer)allUsersList.get("E002"));
		allEmployersList.put(allUsersList.get("E003").getId(),(Employer)allUsersList.get("E003"));

		// Create some job categories
		JobCategory j1 = new JobCategory("Category1", "Active", 1);
		JobCategory j2 = new JobCategory("Category2", "Active", 2);
		JobCategory j3 = new JobCategory("Category3", "Active", 3);

		// Adding for system level
		this.allJobCategories.put("C1", j1);
		this.allJobCategories.put("C2", j2);
		this.allJobCategories.put("C3", j3);
		
		// Add to all complaint's list
		this.allComplaints.add(testComplaint);

		

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
					
					
						
					allJobCategories.put(title,staff.addJobCategory(allJobCategories,title));
					
					
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
				
				//Reactivating Blacklisted User
				case "4":
				{
					String user;
					int blacklistType;
					String type;
					System.out.println("Enter the blacklisted type of User to be reactivated. " +
										"\n 1. 'P' - Provisionally Blacklisted user \n 2.'F' - Fully Blacklisted user " +
										"Enter your choice ( 1 / 2 ) : ");
					try
					{
						blacklistType = Integer.parseInt(input.nextLine());
						if (blacklistType < 1 && blacklistType > 2)
							throw new Exception();

						if (blacklistType == 1)
							type = "P";
						else
							type = "F";
						
						System.out.println("Enter the id of the user to be reactivated:  ");
						user = input.nextLine();
				
						if(staff.revertBlacklistedUser((User)(allUsersList.get(user)), type))
							blacklistedUsers.remove("E1");
					}
					catch(Exception e)
					{
						System.out.println("You have entered an invalid blacklist type. Please try again");
					}
					 
				}
				//registerMaintenanceStaff();
				break;

				case "5" :
				{
					staff.generateReport(this.allUsersList, this.allEmployersList, this.allApplicantsList,this.allComplaints, this.allJobCategories);
				}
				case "Q":
					quit = true;

			}
		} while (!quit);

	}


	/**
	 * @author : Yogeshwar Girish Chaudhari
	 * Employer Menu Options
	 * @throws ParseException 
	 */
	
	public void showEmployerMenu(Employer employer) throws ParseException{
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

				// Search Applicants
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

				// Record Interview Results
				case "7" :
				{
					this.recordInterviewResults(employer);
				}
				break;

				// Create Job Offers
				case "8" :
				{
					this.createJobOffer(employer);
				}
				break;

				case "10" :
				{
//					try {
//						EmailUtil.sendEmail(new EmailObject("","","",""));
//					} catch (MessagingException | UnirestException e) {
//						e.printStackTrace();
//					}
				}

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
		
		System.out.println("Job categories avaialable in the System\n-------------------------------------------------------");
		
		for (String s: allJobCategories.keySet())
		{
			System.out.println (allJobCategories.get(s));
		}
		
		String jobCategory = customScanner.readString("Job Category ID : ");
		if(this.allJobCategories.get(jobCategory) != null)
		{	
		


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
		Job tempJob = new Job(jobId, jobTitle, jobDesc, jobCategory);

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
		else
			System.out.println("Entered Job category does not exist.Please try again");
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

			// Applicants with pending status cant be viewed for shortlisted by the employer
			if(applicantRef.getEmploymentStatus() == EmploymentStatus.PENDING){
				continue;
			}
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

		printShortListedApplicntsForJob(employer, jobRef);

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
			printShortlistedApplicants(jobRef.getShortListedApplicants());

			for(String applicantId : jobRef.getShortListedApplicants().keySet()) {

				Applicant applicntRef = this.getAllApplicantsList().get(applicantId);

				int rank = customScanner.readInt(applicantId+ " -> Rank: ");

				// Rank 'applicantRef' for 'jobRef' with number 'rank'
				try {
					employer.rankApplicant(jobRef, applicntRef, rank);

					// On successfully ranking applicant, employer sends out interview invite
					employer.sendInterviewInvite(jobRef, applicntRef);

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
			printShortlistedApplicants(jobRef.getShortListedApplicants());
		}

	}


	/**
	 * Employer can blackList an applicant.
	 * A list of blacklist status will be shown, and employer can choose any one of them
	 * @param emp
	 */
	public void blackListApplicantByEmp(Employer emp){

		String appcntId = this.customScanner.readString("Please enter applicant ID: ");

		Applicant applicant = this.getAllApplicantsList().get(appcntId);

		printAllBlackListStatus();

		int bTypePos = customScanner.readInt("BlackList Level [0-1] : ", 0, 1);
		BlacklistStatus bType = (BlacklistStatus.values())[bTypePos];

		emp.blacklistApplicant(applicant, bType);
	}


	/**
	 * 23-09-2020
	 * @author Yogeshwar Chaudhari
	 * Employer can register compaint against any applicant.
	 */
	public void registerComplaintAgaintApplicnt(Employer emp) {

		String appcntId = this.customScanner.readString("Please enter applicant ID: ");

		Applicant applcntRef = (Applicant) this.allUsersList.get(appcntId);

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

		String enteredDates = this.customScanner.readString("Please enter all possible dates.");
		String [] singleDates = enteredDates.split(",");

		for(String dateStr : singleDates){
			Date possibleDate = validateDate(dateStr);
			jobRef.getAvailInterviewTimings().add(possibleDate);
		}

		System.out.println("Possible interview times have been set. Please forward this to shortlisted applicants");
	}


	/**
	 * Validates the string date and returns the date Object
	 * @param dateStr
	 */
	public Date validateDate(String dateStr){
		String datePattern = "((0?[1-9])|(1[0-9])|(2[0-9])|(3[01]))/((0?[1-9])|1[012])/(19|20)[0-9]{2}";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Pattern pattern = Pattern.compile(datePattern);

		Date date = null;

		if(pattern.matcher(dateStr).matches()){
			try	{
				date = dateFormat.parse(dateStr);
			} catch (ParseException e){
				System.err.println(e);
			}
		}else{
			System.err.println("Invalid date format. dd/MM/yyyy");
		}

		return date;
	}

	/**
	 * Records the interview results.
	 */
	public void recordInterviewResults(Employer e){

		printPostedJobs(e.getId());

		Job jobRef = null;

		do {
			String jobId = customScanner.readString("Job ID : ");
			jobRef = e.getPostedJobs().get(jobId) != null ? e.getPostedJobs().get(jobId) : null;
		} while (jobRef == null);
		// Job ID validation ends here

		printShortListedApplicntsForJob(e, jobRef);

		String exit = "N";

		do{

			String results = customScanner.readString("Enter Applicant ID and Result : [app1 success app2 fail]");

			// Format expected = app1 success app2 failed app3 success
			StringTokenizer resultTokens = new StringTokenizer(results);

			// Loop over all applicants and their results
			while ( resultTokens.hasMoreTokens() ){
				String applntId = resultTokens.nextToken();
				String result = resultTokens.nextToken();

				// Result is something else. No updation done
				if(! result.equalsIgnoreCase("success") && ! result.equalsIgnoreCase("fail")){
					System.err.println(result+" is not a valid result. [success/fail]");
					continue;
				}

				// Otherwise shortlist
				try {
					e.recordInterviewResults(jobRef, (Applicant) this.allUsersList.get(applntId), result);
				} catch (InvalidApplicationException | NullApplicantException | NullEntityException excep) {
					System.err.println(excep.getMessage());
				}

			}

			exit = customScanner.readYesNo("Exit ? [y/n]");

		}while (exit.trim().strip().equalsIgnoreCase("y"));
	}


	/**
	 * Creates the job offers only when we have interview results
	 */
	public void createJobOffer(Employer e){

		printPostedJobs(e.getId());

		Job jobRef = null;

		do {
			String jobId = customScanner.readString("Job ID : ");
			jobRef = e.getPostedJobs().get(jobId) != null ? e.getPostedJobs().get(jobId) : null;
		} while (jobRef == null);
		// Job ID validation ends here

		printShortListedApplicntsForJob(e, jobRef);

		String applntIds = customScanner.readString("Applicant IDs : [app1 app2]");

		StringTokenizer applntIdTokens = new StringTokenizer(applntIds);

		while(applntIdTokens.hasMoreTokens()){

			try {
				e.createJobOffer(jobRef, (Applicant) this.allUsersList.get( applntIdTokens.nextToken() ));

				// On successing at interview, automated email notification will be sent to the applicant
				e.sendJobOfferEmail(jobRef, (Applicant) this.allUsersList.get( applntIdTokens.nextToken() ));

			} catch (InvalidApplicationException | NullApplicantException | NullEntityException excep) {
				System.err.println(excep.getMessage());
			}
		}


	}


	/**
	 * @author Yogeshwar Chaudhari
	 * Praparing data for demonstrating Employer functionalities
	 * @throws ParseException
	 */
	public void loadDummyDataForEmployeFunctions() throws ParseException {

		Employer e = new Employer("e", "chaudhari.yogesh20@gmail.com", "123", "Yosha");
		try {
			e.createJob(new Job("job1", "Developer", "Developer Desc","C1"));
			e.createJob(new Job("job2", "Analyst", "Analyst Required", "C1"));
			e.createJob(new Job("job3", "Designer", "Designer Required", "C2"));

			List<Date> jobInterviewTimes = new ArrayList<Date>();
			jobInterviewTimes.add(new Date("01/10/2020"));
			jobInterviewTimes.add(new Date("02/10/2020"));
			jobInterviewTimes.add(new Date("03/10/2020"));

			e.getPostedJobs().get("job1").setAvailInterviewTimings(jobInterviewTimes);

			jobInterviewTimes.add(new Date("05/10/2020"));
			jobInterviewTimes.add(new Date("06/10/2020"));

			e.getPostedJobs().get("job2").setAvailInterviewTimings(jobInterviewTimes);

			jobInterviewTimes.add(new Date("08/10/2020"));
			jobInterviewTimes.add(new Date("09/10/2020"));

			e.getPostedJobs().get("job3").setAvailInterviewTimings(jobInterviewTimes);

		}
		catch (DuplicateJobIdException e1) {
			e1.printStackTrace();
		}

		Applicant a1 = new Applicant("app1", "chaudhari.yogesh20@gmail.com", "123", "John", "Doe", "048888888", "l");
		Applicant a2 = new Applicant("app2", "chaudhari.yogesh20@gmail.com", "123", "Mark", "Brown", "048942879", "l");
		Applicant a3 = new Applicant("app3", "chaudhari.yogesh20@gmail.com", "123", "Johny", "Ive", "048734878", "l");
		Applicant a4 = new Applicant("app4", "chaudhari.yogesh20@gmail.com", "123", "Sim", "Corol", "043445873", "i");
		Applicant a5 = new Applicant("app5", "chaudhari.yogesh20@gmail.com", "123", "Dave", "Snow", "048734878", "i");
		Applicant a6 = new Applicant("app6", "chaudhari.yogesh20@gmail.com", "123", "June", "Last", "039847484", "i");

		JobApplication ja = new JobApplication(e.getPostedJobs().get("job1"), a5);
		ja.setRank(1);
		e.getPostedJobs().get("job1").getShortListedApplicants().put("app5", ja);

		// Create some job categories
		JobCategory j1 = new JobCategory("Category1", "Active", 1);
		JobCategory j2 = new JobCategory("Category2", "Active", 2);
		JobCategory j3 = new JobCategory("Category3", "Active", 3);

		// Adding for system level
		this.allJobCategories.put("C1", j1);
		this.allJobCategories.put("C2", j2);
		this.allJobCategories.put("C3", j3);

		// Updating user availability
//		a1.getUserAvailability().add(new UserAvailability(j1, AvailabilityType.FULL_TIME, 40, (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020")), (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2021"))));
//		a2.getUserAvailability().add(new UserAvailability(j2, AvailabilityType.PART_TIME, 20, (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020")), (new SimpleDateFormat("dd/MM/yyyy").parse("11/11/2021"))));
//		a3.getUserAvailability().add(new UserAvailability(j3, AvailabilityType.INTERNSHIP, 30, (new SimpleDateFormat("dd/MM/yyyy").parse("10/11/2020")), (new SimpleDateFormat("dd/MM/yyyy").parse("10/03/2021"))));
//		a4.getUserAvailability().add(new UserAvailability(j3, AvailabilityType.FULL_TIME, 40, (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020")), (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020"))));
//		a5.getUserAvailability().add(new UserAvailability(j1, AvailabilityType.PART_TIME, 20, (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020")), (new SimpleDateFormat("dd/MM/yyyy").parse("23/12/2020"))));
//		a6.getUserAvailability().add(new UserAvailability(j1, AvailabilityType.PART_TIME, 20, (new SimpleDateFormat("dd/MM/yyyy").parse("20/09/2020")), (new SimpleDateFormat("dd/MM/yyyy").parse("20/10/2020"))));

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


		//Added by Prodip Guha Roy for testing
		Applicant a10 = new Applicant("app10", "chaudhari.yogesh20@gmail.com", "123", "John", "Snow", "048888888", "l");
		Applicant a11 = new Applicant("app11", "chaudhari.yogesh20@gmail.com", "123", "Jane", "Doe", "048888888", "l");
		Employer employer = new Employer("emp1", "chaudhari.yogesh20@gmail.com", "123", "Yosha");
		this.allUsersList.put("app10", a10);
		this.allApplicantsList.put("app10", a10);
		this.allUsersList.put("app11", a11);
		this.allApplicantsList.put("app11", a11);
		this.allUsersList.put("emp1", employer);
		this.allEmployersList.put("emp1", employer);

		try {
			employer.createJob(new Job("job1", "Store Manager", "Store Manager role at Woolies","C1"));
			employer.createJob(new Job("job2", "Team Member", "Team Member role at Woolies", "C1"));

			List<Date> jobInterviewTimes = new ArrayList<Date>();
			jobInterviewTimes.add(new Date("01/10/2020"));
			jobInterviewTimes.add(new Date("02/10/2020"));
			jobInterviewTimes.add(new Date("03/10/2020"));

			jobInterviewTimes.add(new Date("05/10/2020"));
			jobInterviewTimes.add(new Date("06/10/2020"));

			employer.getPostedJobs().get("job2").setAvailInterviewTimings(jobInterviewTimes);
			employer.getPostedJobs().get("job1").setAvailInterviewTimings(jobInterviewTimes);

			employer.shortListCandidate(employer.getPostedJobs().get("job1"), a10);
			employer.rankApplicant(employer.getPostedJobs().get("job1"), a10, 1);

			employer.shortListCandidate(employer.getPostedJobs().get("job2"), a11);
			employer.rankApplicant(employer.getPostedJobs().get("job2"), a11, 1);

			employer.createJobOffer(employer.getPostedJobs().get("job2"), a11);

		}
		catch (DuplicateJobIdException | AlreadyPresentInYourShortListedListException | ApplicantIsBlackListedException | NullApplicantException | NullJobReferenceException | NullJobException | InvalidApplicationException | NullEntityException e1) {
			e1.printStackTrace();
		}




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
	 * Prints all the shortlisted Applicants
	 * @param jobApplicantions
	 */
	public void printShortlistedApplicants(HashMap<String, JobApplication> jobApplicantions){
		System.out.format("%3s%10s%32s%10s%10s\n", "---+","----------+","--------------------------------+","----------+","----------+");
		System.out.format("%-3s|%-10s|%-32s|%-10s|%-10s|\n", "Sr", "ID", "Full Name", "JC Pref", "Rank");
		System.out.format("%3s%10s%32s%10s%10s\n", "---+","----------+","--------------------------------+","----------+","----------+");

		int i = 1;
		for(JobApplication ja : jobApplicantions.values()) {
			System.out.format("%-3s|%-10s|%-32s|%-10s|%-10s|\n", i, ja.getApplicantRef().getId(), ja.getApplicantRef().getFirstName()+" "+ja.getApplicantRef().getLastName(), ja.getApplicantRef().getJobPreferences(), ja.getRank());
			System.out.format("%3s%10s%32s%10s%10s\n", "---+","----------+","--------------------------------+","----------+","----------+");
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
			for(JobApplication ja : jobRef.getShortListedApplicants().values()) {
				System.out.format("%-3s|%-10s|%-32s|%-10s|\n", i, ja.getApplicantRef().getId(), ja.getApplicantRef().getFirstName()+" "+ja.getApplicantRef().getLastName(), ja.getApplicantRef().getJobPreferences());
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

				System.out.format("%3s%10s%32s%10s%10s\n", "---+","----------+","--------------------------------+","----------+","----------+");
				System.out.format("%-3s|%-10s|%-32s|%-10s|%-10s|\n", "Sr", "ID", "Full Name", "JC Pref", "Rank");
				System.out.format("%3s%10s%32s%10s%10s\n", "---+","----------+","--------------------------------+","----------+","----------+");

				int i = 1;
				for(JobApplication ja : jobRef.getShortListedApplicants().values()) {
					System.out.format("%-3s|%-10s|%-32s|%-10s|%-10s|\n", i, ja.getApplicantRef().getId(), ja.getApplicantRef().getFirstName()+" "+ja.getApplicantRef().getLastName(), ja.getApplicantRef().getJobPreferences(), ja.getRank());
					System.out.format("%3s%10s%32s%10s%10s\n", "---+","----------+","--------------------------------+","----------+","----------+");
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
	 * Prints all blacklisted status types in the system. except not_blacklisted
	 * Can be used while making selection in case of employer trying to blacklist someone
	 */
	public void printAllBlackListStatus(){

		BlacklistStatus [] bTypes = BlacklistStatus.values();

		for(int i = 0; i < bTypes.length; i++) {
			if(bTypes[i] != BlacklistStatus.NOT_BLACKLISTED) {
				System.out.println(i+" - "+bTypes[i]);
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

