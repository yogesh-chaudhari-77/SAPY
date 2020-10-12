package testing;

import model.entities.*;
import model.enums.ApplicantType;
import model.enums.AvailabilityType;
import model.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ApplicantTest {

    List<Applicant> applicants;
    List<JobCategory> jobCategories;

    @Before
    public void setUp() throws Exception {
        Applicant a1= new Applicant("a1","a1@rmit.com","a123","Prodip", "Guha Roy","0412345678", "I");
        Applicant a2= new Applicant("a2","a2@rmit.com","a123","Abhishek", "Rana","0412345698", "I");
        Applicant a3= new Applicant("a3","a3@rmit.com","a123","Sriram", "Senthilnathan","04123445698", "I");
        Applicant a4= new Applicant("a4","a4@rmit.com","a123","Yogesh", "Chaudhari","04121245698", "l");

        applicants = new ArrayList<>();
        applicants.add(a1);
        applicants.add(a2);
        applicants.add(a3);
        applicants.add(a4);

        JobCategory jc1 = new JobCategory("Software Engineer", "Active", 1);
        JobCategory jc2 = new JobCategory("Store Manager", "Active", 2);
        JobCategory jc3 = new JobCategory("Picker/Packer", "Active", 3);

        //List<JobCategory> jobCategoryList = new ArrayList<>();

        jobCategories= new ArrayList<>();
        jobCategories.add(jc1);
        jobCategories.add(jc2);
        jobCategories.add(jc3);

       // UserAvailability availability = new UserAvailability(jobCategoryList, )

       // applicants.get(0).addAvailability();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testValidAddAvailability() throws ParseException, BadEntryException, DuplicateEntryException {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        int currentAvailabilityCount= applicants.get(0).getUserAvailability().size();
        List<JobCategory> applyingJobs= new ArrayList<>();
        applyingJobs.add(jobCategories.get(0));
        applyingJobs.add(jobCategories.get(1));
        applicants.get(0).addAvailability(AvailabilityType.PART_TIME, applyingJobs, 20,  parser.parse("20/10/2020"), parser.parse("20/01/2021"));
        assertEquals(currentAvailabilityCount+1, applicants.get(0).getUserAvailability().size());
    }

    @Test(expected = DuplicateEntryException.class)
    public void testDuplicateEntryOfAvailability() throws ParseException, BadEntryException, DuplicateEntryException{
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        List<JobCategory> applyingJobs= new ArrayList<>();
        applyingJobs.add(jobCategories.get(0));
        applicants.get(1).addAvailability(AvailabilityType.INTERNSHIP, applyingJobs, 40,  parser.parse("20/11/2020"), parser.parse("20/02/2021"));

        //adding duplicate entry
        applicants.get(1).addAvailability(AvailabilityType.INTERNSHIP, applyingJobs, 40,  parser.parse("20/11/2020"), parser.parse("20/02/2021"));
    }


    @Test
    public void testValidMultipleAddAvailability() throws ParseException, BadEntryException, DuplicateEntryException{
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        int currentAvailabilityCount= applicants.get(2).getUserAvailability().size();
        List<JobCategory> partTimeJobs= new ArrayList<>();
        partTimeJobs.add(jobCategories.get(1));
        partTimeJobs.add(jobCategories.get(2));

        List<JobCategory> intershipJobs= new ArrayList<>();
        intershipJobs.add(jobCategories.get(0));

        //Adding first availability
        applicants.get(2).addAvailability(AvailabilityType.PART_TIME, partTimeJobs, 20,  parser.parse("30/09/2020"), parser.parse("15/11/2021"));

        //Adding second availability
        applicants.get(2).addAvailability(AvailabilityType.INTERNSHIP, intershipJobs, 40, parser.parse("01/12/2020"), parser.parse("28/02/2021"));

        int changedAvailabilityCount = applicants.get(2).getUserAvailability().size();
        assertEquals(currentAvailabilityCount+2, changedAvailabilityCount);

        //Checking last added availability
        UserAvailability lastAdded = applicants.get(2).getUserAvailability().get(changedAvailabilityCount-1);
        assertEquals(AvailabilityType.INTERNSHIP, lastAdded.getAvailabilityType());
        assertEquals(40, lastAdded.getNoOfHoursAWeek());
    }

    @Test(expected = BadEntryException.class)
    public void testInvalidAvailabilty() throws ParseException, BadEntryException, DuplicateEntryException{
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        List<JobCategory> applyingJobs= new ArrayList<>();
        applyingJobs.add(jobCategories.get(0));

        //Trying to add availability where startDate > endDate
        applicants.get(0).addAvailability(AvailabilityType.FULL_TIME, applyingJobs, 40,  parser.parse("20/11/2020"), parser.parse("20/10/2020"));
    }


    //passing correct employment records
    @Test
    public void testValidEmploymentRecord() throws BadEmployeeRecordException, DuplicateEntryException, ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        EmploymentRecord newRecord = new EmploymentRecord("Woolies", "TeamMember", parser.parse("17/03/2019"),parser.parse("20/04/2020"), false );
        int currentRecordsCount= applicants.get(1).getEmploymentHistory().size();
        applicants.get(1).addEmploymentRecords(newRecord);
        assertEquals(currentRecordsCount+1, applicants.get(1).getEmploymentHistory().size());
    }

    //passing duplicate employment records
    @Test(expected = DuplicateEntryException.class)
    public void testInValidEmploymentRecord() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        EmploymentRecord newRecord = new EmploymentRecord("Woolies", "TeamMember", parser.parse("17/03/2019"),parser.parse("20/04/2020"), false );
        applicants.get(0).addEmploymentRecords(newRecord);

        //adding duplicate record
        applicants.get(0).addEmploymentRecords(newRecord);
    }

    @Test(expected = InvalidCVPathException.class)
    public void testInvalidPathForUploadCV() throws InvalidCVPathException {
        //providing invalid path, expecting exception
        applicants.get(1).uploadCV("");
    }

   @Test
    public void testNormalUploadCV() throws InvalidCVPathException {
       String cvPath = "/Users/prodip/Desktop/Resume.pdf";
       assertEquals(null, applicants.get(1).getCvPath());
       applicants.get(1).uploadCV(cvPath);
       assertEquals(cvPath, applicants.get(1).getCvPath());
   }

    //passing correct references
    @Test
    public void testValidReference() throws Exception {
        Reference newReference= new Reference("Harish", "Iyer", "h@google.com","Manager","Woolies","040122121");
        assertEquals(0, applicants.get(0).getReferences().size());
        applicants.get(0).addReferences(newReference);
        assertEquals(1, applicants.get(0).getReferences().size());

    }

    //passing duplicate references
    @Test(expected = DuplicateEntryException.class)
    public void testInValidReference() throws Exception {
        Reference newReference= new Reference("Harish", "Iyer", "h@google.com","Manager","Woolies","040122121");
        //adding first reference
        applicants.get(1).addReferences(newReference);
        //adding duplicate record
        applicants.get(1).addReferences(newReference);
    }

    //passing correct License
    @Test
    public void testValidLicense() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        License newLicense = new License("Driving Licence", "a1234",parser.parse("31/12/2021"));
        assertEquals(0,applicants.get(1).getLicenses().size());
        applicants.get(1).addLicenses(newLicense);
        assertEquals(1, applicants.get(1).getLicenses().size());
    }

    //passing two licenses and then getting count of complaints
    @Test (expected = DuplicateEntryException.class)
    public void testInValidLicense() throws Exception {
        applicants.get(1).addLicenses(new License("Driving", "Test123",
                new Date (2025,04, 25)));
        applicants.get(1).addLicenses(new License("Cooking", "Test123",
                new Date (2025,04, 25)));

    }

    //passing correct qualification
    @Test
    public void testValidQualification() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals(0, applicants.get(1).getQualifications().size());
        applicants.get(1).addQualifications(new Qualification("B.Tech",
                parser.parse("21/03/2012"), parser.parse("17/03/2016"), "Test Field", 93.3));
        assertEquals(1, applicants.get(1).getQualifications().size());
    }

    //passing qualification with start date greater than end date
    @Test(expected = BadQualificationException.class)
    public void testSameStartAndEndDateQualification() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        applicants.get(1).addQualifications(new Qualification("B.Tech",
                parser.parse("21/03/2012"), parser.parse("21/03/2012"), "Test Field", 93.3));
    }

}