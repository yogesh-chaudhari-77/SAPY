package testing;

import model.entities.*;
import model.enums.ApplicantType;
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

    }

    @After
    public void tearDown() throws Exception {
    }

    //passing correct employment records
    @Test
    public void testValidEmploymentRecord() throws BadEmployeeRecordException, DuplicateEntryException, ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        EmploymentRecord newRecord = new EmploymentRecord("Woolies", "TeamMember", parser.parse("17/03/2019"),parser.parse("20/04/2020"), false );
        int currentRecordsCount= applicants.get(1).getEmploymentHistory().size();
        applicants.get(1).addEmploymentRecords(newRecord);
        assertEquals(currentRecordsCount+1, applicants.get(1).getEmploymentHistory().size());
//        assertTrue(applicants.get(1).updateEmploymentRecords(new EmploymentRecord("TestCompany",
//                "TestDesignation", new Date(2018, 02, 15), new Date(2018, 02, 16), false)));
    }

    //passing duplicate employment records
    @Test(expected = DuplicateEntryException.class)
    public void testInValidEmploymentRecord() throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("dd/MM/yyyy");
        EmploymentRecord newRecord = new EmploymentRecord("Woolies", "TeamMember", parser.parse("17/03/2019"),parser.parse("20/04/2020"), false );
        applicants.get(0).addEmploymentRecords(newRecord);

        //adding duplicate record
        applicants.get(0).addEmploymentRecords(newRecord);

//        applicants.get(1).updateEmploymentRecords(new EmploymentRecord("TestCompany",
//                "TestDesignation", new Date(2018, 02, 15), new Date(2018, 02, 16), false));
    }

    //passing correct references
    @Test
    public void testValidReference() throws Exception {
        assertTrue(applicants.get(1).updateReferences(new Reference("TestReference",
                "TestEmail", "testDesignation", "testCompany")));
    }

    //passing duplicate references
    @Test(expected = DuplicateEntryException.class)
    public void testInValidReference() throws Exception {
       applicants.get(1).updateReferences(new Reference("TestReference",
                "TestEmail", "testDesignation", "testCompany"));
        applicants.get(1).updateReferences(new Reference("TestReference",
                "TestEmail", "testDesignation", "testCompany"));
    }

    //passing correct License
    @Test
    public void testValidLicense() throws Exception {
        assertTrue(applicants.get(1).updateLicenses(new License("Driving", "Test123",
                new Date (2025,04, 25))));
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

    //passing complaint (this test case will fail as this functionality is not developed.
    @Test
    public void complaint() {

        assertTrue(applicants.get(1).registerComplaint(new Complaints(new Employer("E1", "testEmail", "testPassword", "TestFirstName", "testLastName", "023456777"),
                applicants.get(1), "Test Complaint Message")));
    }



    //Below commented code is written by my team member Prodip.

//    @Test(expected = InvalidCVPathException.class)
//    public void testInvalidPathForUploadCV() {
//        applicants.get(1).uploadCV("");
//    }
//
//    @Test
//    public void testNormalUploadCV() {
//        assertTrue(applicants.get(1).uploadCV("/Users/prodip/Desktop/Resume.pdf"));
//    }
//
//    @Test
//    public void testChangeBlackListStatus(){
//        applicants.get(0).changeBlacklistStatus(BlacklistStatus.PROVISIONAL_BLACKLISTED);
//        assertEquals(BlacklistStatus.PROVISIONAL_BLACKLISTED, applicants.get(0).getBlacklistStatus());
//    }
//
//    @Test
//    public void testInvalidAvailabilityUpdate(){
//        assertFalse(applicants.get(1).updateAvailability(AvailabilityType.PART_TIME, 40));
//    }
//
//    @Test
//    public void testValidAvailabilityUpdate(){
//        assertTrue(applicants.get(1).updateAvailability(AvailabilityType.FULL_TIME, 40));
//    }
}