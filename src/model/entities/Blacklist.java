package model.entities;

import model.enums.*;
import model.exceptions.*;

import java.util.Date;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;  

public class Blacklist {

	
//	private String blacklistStatus = "";
	private Date startDate;
	private Date endDate;
	BlacklistStatus blacklistStatus;
	
//	DateTimeFormatter dateFormatter = new DateTimeFormatter("dd/MM/yyyy HH:mm:ss");
	
	//Blacklisting an user
	public void setBlacklistStatus(String type)
	{
		if (type.toUpperCase().contentEquals("P"))
			this.blacklistStatus = blacklistStatus.PROVISIONAL_BLACKLISTED;
		else
			this.blacklistStatus = blacklistStatus.FULL_BLACKLISTED;

		startDate = new Date();
		endDate = null;
	}
	
	
	//Reactivating an user
	public void removeBlacklistStatus(String type)
	{
		this.blacklistStatus = blacklistStatus.NOT_BLACKLISTED;
		endDate = new Date();
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public BlacklistStatus getBlacklistStatus() {
		return blacklistStatus;
	}


	public void setBlacklistStatus(BlacklistStatus blacklistStatus) {
		this.blacklistStatus = blacklistStatus;
	}
	
	
}
