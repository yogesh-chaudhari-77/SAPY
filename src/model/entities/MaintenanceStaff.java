package model.entities;

import model.enums.*;
import global.*;
import model.exceptions.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MaintenanceStaff extends User implements Serializable {
	
	private JobCategory jobcategory;
	private int catgId = 4;

	public MaintenanceStaff(String id, String userEmail, String password, String firstName, String lastName, String phoneNumber)
	{
		super(id, userEmail, password, firstName, lastName, phoneNumber);
	}
	
	
	//Adding a new job category to the system
	public JobCategory addJobCategory(HashMap<String, JobCategory> allJobCategories,String newCategory) throws DuplicateJobCategoryException
	{
		if (allJobCategories.get(newCategory) == null)
		{
			jobcategory =  new JobCategory(newCategory,"Active", catgId);
			catgId++;
			return jobcategory;
		}
		else
		{
			throw new DuplicateJobCategoryException("Duplication Attempt: This Job Category  '" + newCategory + "' already exist in the system!");
		}
		
	}

	
	// Blacklisting the User(Employer/Applicant) either provisionally or fully based on type
	public boolean blacklistUser(User user,String type) throws ParseException
	{
		
		if (user instanceof Employer)
		{
			((Employer) user).setBlacklistStatus(type);
			return true;
		}
		else if (user instanceof Applicant)
		{
			((Applicant) user).setBlacklistStatus(type);
			return true;
		}
		else
			return false;
	}
	
	
	//Reactivating the blacklisted User(Employer/Applicant)
	public boolean revertBlacklistedUser(User user, String blacklistType) throws NotFullyBlacklistedUserException, ParseException, NotProvisionallyBlacklistedUserException, BlacklistedTimeNotElapsedException
	{
		
		boolean status = false;
		if (blacklistType.toUpperCase() == "P")
			status = revertProvisionallyBlacklistedUser(user);
		else 
			status = revertFullyBlacklistedUser(user);
		
		return status;
	}

	
	//Reactivating provisionally blacklisted user
	public boolean revertProvisionallyBlacklistedUser(User user) throws NotProvisionallyBlacklistedUserException 
	{
		if (user instanceof Employer)
		{
			Employer emp = ((Employer) user);
			
			BlacklistStatus status = emp.getBlacklistStat();
			
			if (status == BlacklistStatus.PROVISIONAL_BLACKLISTED)
			{
				emp.removeBlacklistStatus();
				return true;
			}
			else
				throw new NotProvisionallyBlacklistedUserException();
		}
			
		else 
		{
			Applicant app = ((Applicant) user);
			
			BlacklistStatus status = app.getBlacklistStat();
			
			if(status == BlacklistStatus.PROVISIONAL_BLACKLISTED)
			{
				app.removeBlacklistStatus();
				return true;
			}
			else
				throw new NotProvisionallyBlacklistedUserException();
		}
		
	}

	
	//Reactivating Fully blacklisted user
	public boolean revertFullyBlacklistedUser(User user) throws NotFullyBlacklistedUserException, ParseException, BlacklistedTimeNotElapsedException 
	{
		if (user instanceof Employer)
		{
			Employer emp = ((Employer) user);
			BlacklistStatus status = emp.getBlacklistStat();

			if (status == BlacklistStatus.FULL_BLACKLISTED)
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date blacklistedDate = emp.getBlacklistStartDate();
				Date currentDate = dateFormat.parse(dateFormat.format(new Date()));

				long differnceInMillies = Math.abs(currentDate.getTime() - blacklistedDate.getTime());
				long duration = TimeUnit.DAYS.convert(differnceInMillies, TimeUnit.MILLISECONDS);


				if(duration > 90)
					emp.removeBlacklistStatus();
				else
					throw new BlacklistedTimeNotElapsedException();
				//	System.out.println("After \n Employer ID: " + ((Employer)user).getId() + " Blacklist Status: " + ((Employer)user).getBlacklistStat());
				return true;
			}
			else
				throw new NotFullyBlacklistedUserException();
		}

		else 
		{
			Applicant app = ((Applicant) user);

			if (app.getBlacklistStat() == BlacklistStatus.FULL_BLACKLISTED)
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date blacklistedDate = app.getBlacklistStartDate();
				Date currentDate = dateFormat.parse(dateFormat.format(new Date()));

				long differnceInMillies = Math.abs(currentDate.getTime() - blacklistedDate.getTime());
				long duration = TimeUnit.DAYS.convert(differnceInMillies, TimeUnit.MILLISECONDS);

				if(duration > 90)
					app.removeBlacklistStatus();
				else
					throw new BlacklistedTimeNotElapsedException();

				return true;
			}
			else
				throw new NotFullyBlacklistedUserException();

		}
		
	}
	
//	//Generation the reports to tune the system
//	public void generateReport(HashMap<String, User>allUsersList, HashMap<String, Employer>allEmployersList, HashMap<String, Applicant>allApplicantsList, List<Complaints> allComplaints,HashMap<String, JobCategory>allJobCategories)
//	{
//		int choice = 0;
//		this.allUsersList = allUsersList;
//		this.allComplaints = allComplaints;
//		this.allApplicantsList = allApplicantsList;
//		this.allEmployersList = allEmployersList;
//		do
//		{
//			System.out.println("The Following reports can be generated in the System."
//					+ "\n 1. List of employers making offers, number of offers made in the specified period"
//					+ "\n 2. List of complaints about specific applicant or employer"
//					+ "\n 3. Jobs offered and accepted by a specific applicant"
//					+ "\n 4. All past offers for a particular Job Category" 
//					+ "\n 5. Exit"
//					+ "\n Enter your choice (1,2,3,4,5) : ");
//			try
//			{
//				choice = Integer.parseInt(Global.scanner.nextLine());
//			}
//			catch (Exception e)
//			{
//				System.out.println("You have entered an invlid input.Please try again!");
//			}
//				
//		} while (choice < 1 && choice > 5);
//		
////		System.out.println("Choice: " + choice);
////		System.out.println("allUsersList: " + this.allUsersList);
////		if (this.allComplaints != null)
////		System.out.println("allComplaints: " + this.allComplaints);
//
//		
//		switch (choice)
//		{
//		case 1 : generateReport1();
//				break;
//		case 2 :
//				{
//					String user;
//					System.out.println("Enter the id of the user :  ");
//					user = Global.scanner.nextLine();
//					if (this.allUsersList.get(user) == null)
//						System.out.println("The entered user does not exist");
//					else
//						generateReport2(user);	
//					break;
//				}
//						
//		case 3 : 
//				{
//					String user;
//					System.out.println("Enter the id of the Applicant :  ");
//					user = Global.scanner.nextLine();
//					if (this.allUsersList.get(user) == null)
//						System.out.println("The entered user does not exist");
//					else
//						generateReport3(user);
//					break;
//				}
//		case 4 : 
//			{
//				String jobID;
//				System.out.println("Enter the id of the Job Category :  ");
//				jobID = Global.scanner.nextLine();
//				if (!allJobCategories.containsKey(jobID))
//					System.out.println("The entered Job Category goes not exist in the system");
//				else
//					generateReport4(jobID, allJobCategories);
//				break;
//				
//			}
//		case 5 : break;
//		
//		default: System.out.println("In defualt");
//		}
//		//Yet to implement
//	}


	public void generateReport1(HashMap<String, Employer>allEmployersList, Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		
		TreeMap<String, Integer> empOfferCount = new TreeMap<String, Integer>();
			
		
		for (Employer e: allEmployersList.values())
		{
			HashMap<String, Job> postedJobs = e.getPostedJobs();
			int offerCount = 0;
			
			for (Job j: postedJobs.values())
			{
				HashMap<String, JobApplication> shortlistedApplicants = j.getShortListedApplicants();
				for (JobApplication app: shortlistedApplicants.values())
				{
					if (app.getOfferRef() != null)
					{
						if (app.getOfferRef().getOfferedOn().compareTo(fromDate) >= 0)
							offerCount++;
					}
						
				}
			}
			
			//Change it  to >0 later
			if (offerCount > 0)
				empOfferCount.put(e.getId(),offerCount);
		}
		
		
		String filename = "src/reports/OfferedEmployers(SpecificPeriod).csv";
		PrintWriter pw;
		
		try 
		{
			pw = new PrintWriter(new BufferedWriter (new FileWriter(filename,true)));
			pw.println("List of Employers who made Offers:, From:  " + fromDate + ", To: " + toDate);
			pw.println("Employer ID, No. of Offers");
			for (String e: empOfferCount.descendingKeySet())
			{
					pw.println(e + "," + empOfferCount.get(e));
			}
			System.out.println("\nReport: " + filename + " is successfully generated. ");
			pw.close();
		} 
		catch (Exception e1) 
		{
			System.out.println ("\nError occured in generating report file:  " + filename);
			System.err.println(e1);
		}
		
	}
	
	
	//Report containing list of complaints of particular user(Employer/Applicant)
	public void generateReport2(HashMap<String, User>allUsersList,List<Complaints> allComplaints,String user) 
	{
		// TODO Auto-generated method stub
		if(allUsersList.containsKey(user))
		{
			String filename = "src/reports/ComplaintsOn" + user + ".csv";
			PrintWriter pw;
			
			try 
			{
				pw = new PrintWriter(new BufferedWriter (new FileWriter(filename)));
				pw.println("Complaints on the User ,"+ user);
				pw.println("Complaint MadeBy, Complaint Message");
				for (Complaints c: allComplaints)
				{
					if (c.getComplaintOn().contentEquals(user))
					{
						pw.println(c);
					}
				}
				System.out.println("\nReport: " + filename + " is successfully generated. ");
				pw.close();
			} 
			catch (Exception e1) 
			{
				System.out.println ("\nError occured in generating report file:  " + filename);
				System.err.println(e1);
			}
			
		}		
	}
	
	
	
	public void generateReport3(HashMap<String, Applicant>allApplicantsList,HashMap<String, Employer>allEmployersList,String user) 
	{
		// TODO Auto-generated method stub
		String appID = allApplicantsList.get(user).getId();
		
		int offeredCount = 0;
		int acceptedCount = 0;
		ArrayList<String> offeredJobIds = new ArrayList<String>();
		ArrayList<String> acceptedJobIds = new ArrayList<String>();

		
		for (Employer e: allEmployersList.values())
		{
			HashMap<String, Job> postedJobs = e.getPostedJobs();
			
			
			for (Job j: postedJobs.values())
			{
				HashMap<String, JobApplication> shortlistedApplicants = j.getShortListedApplicants();
				for (JobApplication app: shortlistedApplicants.values())
				{
					if (app.getApplicantRef().getId().contentEquals(user))
					{
						if (app.getOfferRef() != null)
						{
							offeredCount++;
							offeredJobIds.add(j.getJobId());
							
							if (app.getOfferRef().getOfferStatus() == OfferStatus.ACCEPTED)
							{
								acceptedCount++;
								acceptedJobIds.add(j.getJobId());
							}
						}
					}
				}
						
			}
		}
		

		String filename = "src/reports/Applicant_"  + user + "_OfferHistory" + ".csv";
		PrintWriter pw;
		
		try 
		{
			pw = new PrintWriter(new BufferedWriter (new FileWriter(filename)));
			pw.println("List of Jobs offered to and accepted by Applicant " + user);
			pw.println("No. of Offers = " + offeredCount + "," + "No. of accepted Offers = " + acceptedCount);
			pw.println("List of Jobs offered(Job IDs)");
			
			for (String s: offeredJobIds)
			{
				pw.println(s);
			}
			
			pw.println("\n\n\nList of Jobs Accepted(Job IDs)");
			
			for (String s: acceptedJobIds)
			{
				pw.println(s);
			}

			System.out.println("\nReport: " + filename + " is successfully generated. ");
			pw.close();
		} 
		catch (Exception e1) 
		{
			System.out.println ("\nError occured in generating report file:  " + filename);
			System.err.println(e1);
		}		
	}
	
	
	public void generateReport4(String jobID, HashMap<String, JobCategory> allJobCategories, HashMap<String, Employer> allEmployersList) 
	{
		// TODO Auto-generated method stub
		
		String jobCategoryTitle = allJobCategories.get(jobID).getCategoryTitle();
		String filename = "src/reports/OffersForCategory"  + jobCategoryTitle + ".csv";
		PrintWriter pw;
		
		
		try 
		{
			pw = new PrintWriter(new BufferedWriter (new FileWriter(filename)));
			pw.println("List of Jobs offered in the Job Category: " + jobCategoryTitle);
			pw.println("Offer To,Offerd By,EmploymentDetails");
			
			for (Employer e: allEmployersList.values())
			{
				HashMap<String, Job> postedJobs = e.getPostedJobs();
				
				
				for (Job j: postedJobs.values())
				{
					HashMap<String, JobApplication> shortlistedApplicants = j.getShortListedApplicants();
					for (JobApplication app: shortlistedApplicants.values())
					{
						if (app.getOfferRef() != null)
						{
							if (app.getJobRef().getJobCategoryID().contentEquals(jobID))
								pw.println(app.getApplicantRef().getId() + "," + e.getId() + "," + app.getOfferRef().getEmployementDetails());
						}
					}
				}
			}
			System.out.println("\nReport: " + filename + " is successfully generated. ");
			pw.close();
		} 
		catch (Exception e1) 
		{
			System.out.println ("\nError occured in generating report file:  " + filename);
			System.err.println(e1);
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
}
