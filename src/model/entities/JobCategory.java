package model.entities;

import model.enums.*;
import model.exceptions.*;

public class JobCategory {

	private String id;
	private String categoryTitle;
	private static int idNo = 1;
	private String status;
	
	public JobCategory(String newCategory, String status, int id)
	{
		this.id = "C"+id;
		this.categoryTitle = newCategory;
		this.status = status;
		idNo++;
	}
	
	
	public String toString()
	{
		return "Category id:\t " + this.id + "\t\tCategory Title:\t " + this.categoryTitle ;
	}

	public String getCategoryTitle() {
		return categoryTitle;
	}

	public String getId() {
		return id;
	}

	// 23-09-2020 Additional getter setters
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
}
