import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Properties;

import model.exceptions.*;
import controller.*;

/*
	[1] Shah, A.
	Shah, A. (2019) Java Properties File: How to Read config.properties Values in Java? • Crunchify, Crunchify. Available at: https://crunchify.com/java-properties-file-how-to-read-config-properties-values-in-java/ (Accessed: 16 October 2020).
 */

public class CasualEmploymentSystem {

	public static void main(String[] args) throws BadQualificationException, DuplicateEntryException, DuplicateJobCategoryException, ParseException, NotFullyBlacklistedUserException, NotProvisionallyBlacklistedUserException, BlacklistedTimeNotElapsedException, FileNotFoundException, BadEntryException {

		SystemHandler handler = new SystemHandler();
		handler.run();
		
	}

}
