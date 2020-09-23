package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MaintenanceStaff extends User{
	
	private JobCategory jobcategory;
	BlacklistStatus blacklistStatus;

	private int catgId = 1;

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
			throw new DuplicateJobCategoryException();
		}
		
	}
	
	
	//Viewing the list of all Blacklisted Users
	public void viewBlackListedMembers(HashMap<String, User> blacklistedUsers)
	{
		System.out.format("\n%111s\n","|-------------------------------------------------------------------------------------------------------------|");
		System.out.format("|%-20s%-89s|\n","","BlackListed Users");
		System.out.format("%111s\n","|-------------------------------------------------------------------------------------------------------------|");
		System.out.format("|%-20s|%-20s|%-36s|%-30s|\n","User Id", "User Type", "Blacklisted Type", "Blacklisted Date");
		System.out.format("%20s%20s%36s%30s\n","|--------------------|","--------------------|","------------------------------------|","------------------------------|");
		
		
		for (String s : blacklistedUsers.keySet())
		{
			if(blacklistedUsers.get(s) instanceof Employer )
			{
				if (((Employer)blacklistedUsers.get(s)).getBlacklistStat() != blacklistStatus.NOT_BLACKLISTED)
				System.out.format("|%-20s|%-20s|%-36s|%-30s|\n",blacklistedUsers.get(s).getId(), "Employer", ((Employer)blacklistedUsers.get(s)).getBlacklistStat(),((Employer)blacklistedUsers.get(s)).getBlacklistStartDate());
			}
			else
			{
				if (((Applicant)blacklistedUsers.get(s)).getBlacklistStat() != blacklistStatus.NOT_BLACKLISTED)
				System.out.format("|%-20s|%-20s|%-36s|%-30s|\n",blacklistedUsers.get(s).getId(), "Applicant", ((Applicant)blacklistedUsers.get(s)).getBlacklistStat(),((Applicant)blacklistedUsers.get(s)).getBlacklistStartDate());
			}
		}
		System.out.format("%20s%20s%36s%30s\n","|--------------------|","--------------------|","------------------------------------|","------------------------------|");
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
		
		
//		if (user instanceof Employer)
//		{
//			System.out.println("Before\n Employer ID: " + ((Employer)user).getId() + " Blacklist Status: " + ((Employer)user).getBlacklistStat());
//			((Employer) user).removeBlacklistStatus();
//			System.out.println("After \n Employer ID: " + ((Employer)user).getId() + " Blacklist Status: " + ((Employer)user).getBlacklistStat());
//			return true;
//		}
//		else if (user instanceof Applicant)
//		{
//			if(((Applicant)user).getBlacklistStat() != null)
//				System.out.println("Before\n Applicant ID: " + ((Applicant)user).getId() + " Blacklist Status: " + ((Applicant)user).getBlacklistStat());
//			((Applicant) user).removeBlacklistStatus();
//			System.out.println("After\n Applicant ID: " + ((Applicant)user).getId() + " Blacklist Status: " + ((Applicant)user).getBlacklistStat());
//
//			return true;
//		}
//		else
//		{
//			System.out.println("False");
//			return false;
//
//		}
	}

	
	//Reactivating provisionally blacklisted user
	public boolean revertProvisionallyBlacklistedUser(User user) throws NotProvisionallyBlacklistedUserException 
	{
		if (user instanceof Employer)
		{
//			System.out.println("Before\n Employer ID: " + ((Employer)user).getId() + " Blacklist Status: " + ((Employer)user).getBlacklistStat());
			Employer emp = ((Employer) user);
			if (emp.getBlacklistStat() == blacklistStatus.PROVISIONAL_BLACKLISTED)
			{
				emp.removeBlacklistStatus();
//			System.out.println("After \n Employer ID: " + ((Employer)user).getId() + " Blacklist Status: " + ((Employer)user).getBlacklistStat());
				return true;
			}
			else
				throw new NotProvisionallyBlacklistedUserException();
		}
			
		else if (user instanceof Applicant)
		{
			Applicant app = ((Applicant) user);
			if(app.getBlacklistStat() == blacklistStatus.PROVISIONAL_BLACKLISTED)
			{
//				System.out.println("Before\n Applicant ID: " + ((Applicant)user).getId() + " Blacklist Status: " + ((Applicant)user).getBlacklistStat());
				app.removeBlacklistStatus();
//			System.out.println("After\n Applicant ID: " + ((Applicant)user).getId() + " Blacklist Status: " + ((Applicant)user).getBlacklistStat());
				return true;
			}
			else
				throw new NotProvisionallyBlacklistedUserException();
		}
		else
		{
			System.out.println("False");
			return false;

		}
	}

	
	//Reactivating Fully blacklisted user
		public boolean revertFullyBlacklistedUser(User user) throws NotFullyBlacklistedUserException, ParseException, BlacklistedTimeNotElapsedException 
		{
			if (user instanceof Employer)
			{
//				System.out.println("Before\n Employer ID: " + ((Employer)user).getId() + " Blacklist Status: " + ((Employer)user).getBlacklistStat());
				Employer emp = ((Employer) user);
				if (emp.getBlacklistStat() == blacklistStatus.FULL_BLACKLISTED)
				{
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Date blacklistedDate = emp.getBlacklistStartDate();
					Date currentDate = dateFormat.parse(dateFormat.format(new Date()));
					
//					Date currentDate = dateFormat.parse("23/10/2020 15:42:42");

					long differnceInMillies = Math.abs(currentDate.getTime() - blacklistedDate.getTime());
				    long difference = TimeUnit.DAYS.convert(differnceInMillies, TimeUnit.MILLISECONDS);

//				    System.out.println("Time difference: " + difference);
					
					if(difference > 90)
						emp.removeBlacklistStatus();
					else
						throw new BlacklistedTimeNotElapsedException();
//				System.out.println("After \n Employer ID: " + ((Employer)user).getId() + " Blacklist Status: " + ((Employer)user).getBlacklistStat());
					return true;
				}
				else
					throw new NotFullyBlacklistedUserException();
			}
				
			else if (user instanceof Applicant)
			{
				Applicant app = ((Applicant) user);
				
				if (app.getBlacklistStat() == blacklistStatus.FULL_BLACKLISTED)
				{
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Date blacklistedDate = app.getBlacklistStartDate();
					Date currentDate = dateFormat.parse(dateFormat.format(new Date()));
					
					long differnceInMillies = Math.abs(currentDate.getTime() - blacklistedDate.getTime());
				    long difference = TimeUnit.DAYS.convert(differnceInMillies, TimeUnit.MILLISECONDS);

//				    System.out.println("Time difference: " + difference);
					
				if(difference > 90)
					app.removeBlacklistStatus();
				else
					throw new BlacklistedTimeNotElapsedException();
//				System.out.println("After \n Employer ID: " + ((Employer)user).getId() + " Blacklist Status: " + ((Employer)user).getBlacklistStat());
					return true;
				}
				else
					throw new NotFullyBlacklistedUserException();
				
			}
			else
			{
				System.out.println("False");
				return false;

			}
		}
	
	//Generation the reports to tune the system
	public void generateReport()
	{
		//Yet to implement
	}
}
