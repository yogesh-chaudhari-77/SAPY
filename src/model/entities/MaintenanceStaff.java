package model.entities;

import model.enums.*;
import model.exceptions.*;
import java.util.HashMap;

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
				System.out.format("|%-20s|%-20s|%-36s|%-30s|\n",blacklistedUsers.get(s).getId(), "Employer", ((Employer)blacklistedUsers.get(s)).getBlacklistStat(),((Employer)blacklistedUsers.get(s)).getStartDate());
			}
			else
			{
				if (((Applicant)blacklistedUsers.get(s)).getBlacklistStat() != blacklistStatus.NOT_BLACKLISTED)
				System.out.format("|%-20s|%-20s|%-36s|%-30s|\n",blacklistedUsers.get(s).getId(), "Applicant", ((Applicant)blacklistedUsers.get(s)).getBlacklistStat(),((Applicant)blacklistedUsers.get(s)).getStartDate());
			}
		}
		System.out.format("%20s%20s%36s%30s\n","|--------------------|","--------------------|","------------------------------------|","------------------------------|");
	}
	
	
	// Blacklisting the User(Employer/Applicant) either provisionally or fully based on type
	public boolean blacklistUser(User user,String type)
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
	public boolean revertBlacklistedUser(User user)
	{
		
		if (user instanceof Employer)
		{
			System.out.println("Before\n Employer ID: " + ((Employer)user).getId() + " Blacklist Status: " + ((Employer)user).getBlacklistStat());
			((Employer) user).removeBlacklistStatus();
			System.out.println("After \n Employer ID: " + ((Employer)user).getId() + " Blacklist Status: " + ((Employer)user).getBlacklistStat());
			return true;
		}
		else if (user instanceof Applicant)
		{
			if(((Applicant)user).getBlacklistStat() != null)
				System.out.println("Before\n Applicant ID: " + ((Applicant)user).getId() + " Blacklist Status: " + ((Applicant)user).getBlacklistStat());
			((Applicant) user).removeBlacklistStatus();
			System.out.println("After\n Applicant ID: " + ((Applicant)user).getId() + " Blacklist Status: " + ((Applicant)user).getBlacklistStat());

			return true;
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
