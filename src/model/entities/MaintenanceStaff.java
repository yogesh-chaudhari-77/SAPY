package model.entities;

import model.enums.*;
import model.exceptions.*;
import java.util.HashMap;

public class MaintenanceStaff extends User{
	
	
	
	
	public MaintenanceStaff(String id, String userEmail, String password, String firstName, String lastName, String phoneNumber)
	{
		super(id, userEmail, password, firstName, lastName, phoneNumber);
	}
	
	
	//Adding a new job category to the system
	public JobCategory addJobCategory(String newCategory)
	{
		return new JobCategory(newCategory,"Active");
	}
	
	
	//Viewing the list of all Blacklisted Users
	public void viewBlackListedMembers(HashMap<String, User> blacklistedUsers)
	{
		System.out.format("\n%60s\n","|----------------------------------------------------------|");
		System.out.format("|%-20s%-38s|\n","","BlackListed Users");
		System.out.format("%60s\n","|----------------------------------------------------------|");
		System.out.format("|%-20s|%-20s|%-16s|\n","User Id", "User Type", "Blacklisted Type");
		System.out.format("%20s%20s%16s\n","|--------------------|","--------------------|","----------------|");
		
		
		for (String s : blacklistedUsers.keySet())
		{
			if(blacklistedUsers.get(s) instanceof Employer)
				System.out.format("|%-20s|%-20s|%-16s|\n",blacklistedUsers.get(s).getId(), "Employer", "Provisional");
			else
				System.out.format("|%-20s|%-20s|%-16s|\n",blacklistedUsers.get(s).getId(), "Applicant", "Full");
		}
		System.out.format("%20s%20s%16s\n","|--------------------|","--------------------|","----------------|");
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
			((Employer) user).removeBlacklistStatus();
			return true;
		}
		else if (user instanceof Applicant)
		{
			((Applicant) user).removeBlacklistStatus();
			return true;
		}
		else
			return false;
	}

}
