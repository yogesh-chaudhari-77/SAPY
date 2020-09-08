package model.entities;

import model.enums.*;
import model.exceptions.*;
import java.util.HashMap;

public class MaintenanceStaff extends User{
	
	
	private int catgId = 0;

	public MaintenanceStaff(String id, String userEmail, String password, String firstName, String lastName, String phoneNumber)
	{
		super(id, userEmail, password, firstName, lastName, phoneNumber);
	}
	
	
	//Adding a new job category to the system
	public JobCategory addJobCategory(String newCategory)
	{
		catgId++;
		return new JobCategory(newCategory,"Active", catgId);
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
			if(blacklistedUsers.get(s) instanceof Employer)
				System.out.format("|%-20s|%-20s|%-36s|%-30s|\n",blacklistedUsers.get(s).getId(), "Employer", ((Employer)blacklistedUsers.get(s)).getBlacklistStat(),((Employer)blacklistedUsers.get(s)).getStartDate());
			else
				System.out.format("|%-20s|%-20s|%-36s|%-30s|\n",blacklistedUsers.get(s).getId(), "Applicant", ((Applicant)blacklistedUsers.get(s)).getBlacklistStat(),((Applicant)blacklistedUsers.get(s)).getStartDate());
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

}
