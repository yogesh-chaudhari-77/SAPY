package model.entities;

import model.enums.*;
import model.exceptions.*;

public class JobCategory {

	private String id;
	private String categoryTitle;
	private int idNo = 1;
	private String status;
	
	public JobCategory(String newCategory, String status)
	{
		this.id = "C"+idNo;
		this.categoryTitle = newCategory;
		this.status = status;
		idNo++;
	}

	public String getCategoryTitle() {
		return categoryTitle;
	}
}
