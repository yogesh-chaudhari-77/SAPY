package model.entities;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.enums.*;
import model.exceptions.*;

public class Applicant extends User {

    private static int idCount = 0;
    private String applicantType;
    private String cvPath;
    private AvailabilityType availabilityType;
    private Date availabilityPeriodStart;
    private Date availabilityPeriodEnd;
    private int availabilityPerWeekInHours;
    private EmploymentStatus employmentStatus;
    private BlacklistStatus blacklistStatus;
    private int complaintsCount;
    private List<String> jobPreferences;
    private List<License> licenses;
    private List<Reference> references;
    private List<EmploymentRecord> employmentRecords;
    private List<Qualification> qualifications;
    private Blacklist blacklist = new Blacklist();

    public Applicant(String id,String email, String password, String firstName, String lastName, String phoneNumber, String applicantType) {
        super(id, email, password, firstName, lastName, phoneNumber);
        this.applicantType = applicantType;
        this.cvPath="";
        this.availabilityType= AvailabilityType.NOT_SET;
        this.availabilityPeriodStart= new Date();
        this.availabilityPeriodEnd= new Date();
        this.availabilityPerWeekInHours= -1;
        this.employmentStatus = EmploymentStatus.UNKNOWN;
        this.blacklistStatus= BlacklistStatus.NOT_BLACKLISTED;
        this.complaintsCount=0;
        this.jobPreferences= new ArrayList<>();
        licenses= new ArrayList<>();
        references= new ArrayList<>();
        employmentRecords= new ArrayList<>();
        qualifications= new ArrayList<>();

    }

    public boolean updateEmploymentRecords(EmploymentRecord record) throws BadEmployeeRecordException, DuplicateEntryException {
        boolean flag = true;
        for (EmploymentRecord currentRecord : employmentRecords){
            if (currentRecord.getCompanyName().equals(record.getCompanyName())
            && currentRecord.getStartDate().equals(record.getStartDate())) {
                flag = false;
            }
        }
        if (flag) {
            if (record.getStartDate().after(record.getEndDate()) || record.getStartDate().equals(record.getEndDate())) {
                throw new BadEmployeeRecordException("Start Date should be less then end date");
            } else {
                employmentRecords.add(record);
                return true;
            }
        } else{
                throw new DuplicateEntryException("This Employment record is already present");
        }
    }

    public boolean updateQualifications(Qualification qualification) throws BadQualificationException, DuplicateEntryException {
        boolean flag = true;

        for(Qualification currentQual:qualifications){
            if(currentQual.getQualificationLevel().equals(qualification.getQualificationLevel())){
                flag = true;
            }
        }
        if (flag) {
            if (qualification.getStartDate().after(qualification.getEndDate()) || qualification.getStartDate().equals(qualification.getEndDate())) {
                throw new BadQualificationException("Start Date should be less then end date");
            } else {
                qualifications.add(qualification);
                return true;
            }
        } else{
            throw new DuplicateEntryException("This qualification is already present");
        }

    }

    public boolean updateReferences(Reference reference) throws DuplicateEntryException{
        boolean flag = true;
        for(Reference currentReference : references){
            if (currentReference.getEmail().equals(reference.getEmail())){
                flag = false;
            }
        }
        if (flag){
            references.add(reference);
            return true;
        } else {
            throw new DuplicateEntryException("This Reference already present");
        }

    }

    public boolean updateLicenses(License license) throws DuplicateEntryException {
        boolean flag = true;

        for(License currentLicense : licenses) {
            if ( currentLicense.getId().equals(license.getId())){
                flag = false;
            }
        }

        if(flag){
            licenses.add(license);
            return true;
        } else {
            throw new DuplicateEntryException("This License already present");
        }
    }

    public boolean updateAvailability(AvailabilityType availabilityType, int hoursPerWeek){

        return true;
    }

    public boolean uploadCV(String cvPath) throws InvalidCVPathException{

        File file = new File(cvPath);
        if(file.exists()){
            this.cvPath =cvPath;
            return true;
        }else{
            throw new InvalidCVPathException();
        }


    }

    public String selectInterviewTiming(){

        return null;
    }

    public boolean replyToJobOffer(){

        return false;
    }

    public boolean changeBlacklistStatus(BlacklistStatus status){

        return false;
    }

    public boolean registerComplaint(Complaints complaint){


        return false;
    }

    public static int getIdCount() {
        return idCount;
    }

    public static void setIdCount(int idCount) {
        Applicant.idCount = idCount;
    }

    public String getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(String applicantType) {
        this.applicantType = applicantType;
    }

    public String getCvPath() {
        return cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }

    public AvailabilityType getAvailabilityType() {
        return availabilityType;
    }

    public void setAvailabilityType(AvailabilityType availabilityType) {
        this.availabilityType = availabilityType;
    }

    public Date getAvailabilityPeriodStart() {
        return availabilityPeriodStart;
    }

    public void setAvailabilityPeriodStart(Date availabilityPeriodStart) {
        this.availabilityPeriodStart = availabilityPeriodStart;
    }

    public Date getAvailabilityPeriodEnd() {
        return availabilityPeriodEnd;
    }

    public void setAvailabilityPeriodEnd(Date availabilityPeriodEnd) {
        this.availabilityPeriodEnd = availabilityPeriodEnd;
    }

    public int getAvailabilityPerWeekInHours() {
        return availabilityPerWeekInHours;
    }

    public void setAvailabilityPerWeekInHours(int availabilityPerWeekInHours) {
        this.availabilityPerWeekInHours = availabilityPerWeekInHours;
    }

    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public BlacklistStatus getBlacklistStatus() {
        return blacklistStatus;
    }

    public void setBlacklistStatus(BlacklistStatus blacklistStatus) {
        this.blacklistStatus = blacklistStatus;
    }

    public int getComplaintsCount() {
        return complaintsCount;
    }

    public void setComplaintsCount(int complaintsCount) {
        this.complaintsCount = complaintsCount;
    }

    public List<String> getJobPreferences() {
        return jobPreferences;
    }

    public void setJobPreferences(List<String> jobPreferences) {
        this.jobPreferences = jobPreferences;
    }

    public List<License> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }

    public List<Reference> getReferences() {
        return references;
    }



    public List<EmploymentRecord> getEmploymentRecords() {
        return employmentRecords;
    }

    public void setEmploymentRecords(List<EmploymentRecord> employmentRecords) {
        this.employmentRecords = employmentRecords;
    }

    public List<Qualification> getQualifications() {
        return qualifications;
    }


		
	//Blacklisting an user by setting type to 'P' or 'F'
	public void setBlacklistStatus(String type)
	{
		blacklist.setBlacklistStatus(type);
	}
	
	
	//Reactivating the blacklisted user
	public void removeBlacklistStatus(String type)
	{
		blacklist.setBlacklistStatus(type);
	}

}
