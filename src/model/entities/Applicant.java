package model.entities;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.enums.*;
import model.exceptions.*;

public class Applicant extends User {

    private List<EmploymentRecord> employmentHistory;
    private List <UserAvailability> userAvailability;
    private int complaintCount;
    private List<License> licenses;
    private List<Reference> references;
    private List<Qualification> qualifications;
    private Blacklist blacklistStatus;
    private ApplicantType applicantType;
    private String cvPath;


    public Applicant(String id,String email, String password, String firstName, String lastName, String phoneNumber, String applicantType) {
        super(id, email, password, firstName, lastName, phoneNumber);
        this.employmentHistory = new ArrayList<>();
        this.userAvailability = new ArrayList<>();
        this.complaintCount = 0;
        this.licenses = new ArrayList<>();
        this.references = new ArrayList<>();
        this.qualifications = new ArrayList<>();
        //this.blacklistStatus = null;
        this.blacklistStatus = new Blacklist();
        if(applicantType.equalsIgnoreCase("l")){
            this.applicantType = ApplicantType.LOCAL;
        }else {
            this.applicantType = ApplicantType.INTERNATIONAL;
        }
        this.cvPath = null;
    }

    public boolean addEmploymentRecords(EmploymentRecord record) throws BadEmployeeRecordException, DuplicateEntryException {
        boolean employmentRecordFound = false;
        for (EmploymentRecord currentRecord : employmentHistory){
            if (currentRecord.getCompanyName().equals(record.getCompanyName())
            && currentRecord.getStartDate().equals(record.getStartDate())) {
                employmentRecordFound = true;
                break;
            }
        }
        if (!employmentRecordFound) {
            System.out.println();
            if (!record.getCurrentCompany() && (record.getStartDate().after(record.getEndDate()) || record.getStartDate().equals(record.getEndDate()))) {
                throw new BadEmployeeRecordException("Start Date should be less then end date");
            } else {
                employmentHistory.add(record);
                return true;
            }
        } else{
            throw new DuplicateEntryException("This Employment record is already present");
        }
    }

    public boolean updateEmploymentRecords(EmploymentRecord oldRecord, EmploymentRecord newRecord){
        for(int i=0; i<employmentHistory.size();i++){
            if (employmentHistory.get(i).getCompanyName().equals(oldRecord.getCompanyName())) {
                employmentHistory.remove(i);
                employmentHistory.set(i,newRecord);
                return true;
            }
        }
        return false;
    }

    public boolean addQualifications(Qualification qualification) throws BadQualificationException, DuplicateEntryException {
        boolean qualificationFound = false;

        for(Qualification currentQual:qualifications){
            if(currentQual.getQualificationLevel().equals(qualification.getQualificationLevel())){
                qualificationFound = true;
                break;
            }
        }
        if (!qualificationFound) {
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

    public boolean addReferences(Reference reference) throws DuplicateEntryException{
        boolean referenceFound = false;

        for(Reference currentReference : references){
            if (currentReference.getEmail().equals(reference.getEmail())){
                referenceFound = true;
                break;
            }
        }

        if (!referenceFound){
            references.add(reference);
            return true;
        } else {
            throw new DuplicateEntryException("This Reference already present");
        }

    }

    public boolean addLicenses(License license) throws DuplicateEntryException {
        boolean licenseFound = false;

        for(License currentLicense : licenses) {
            if ( currentLicense.getId().equals(license.getId())){
                licenseFound = true;
                break;
            }
        }

        if(!licenseFound){
            licenses.add(license);
            return true;
        } else {
            throw new DuplicateEntryException("This License already present");
        }
    }

    public boolean addAvailability(AvailabilityType availabilityType, JobCategory jobCategory, int hoursPerWeek) throws DuplicateEntryException {
        boolean duplicateAvailability= false;
        for(UserAvailability availability: userAvailability){
            if((availability.getAvailabilityType() == availabilityType)
                    && (availability.getApplicableJobCategory().getId().equalsIgnoreCase(jobCategory.getId()))
                    && availability.getNoOfHoursAWeek() == hoursPerWeek){
                duplicateAvailability= true;
                break;
            }
        }

        if(!duplicateAvailability){
            userAvailability.add(new UserAvailability(jobCategory, availabilityType, hoursPerWeek));
        }else{
            throw new DuplicateEntryException("User Availability already present");
        }
        return false;
    }

    public boolean updateAvailability(UserAvailability oldRecord, UserAvailability newRecord){

        for(int index=0; index<userAvailability.size();index++){
            UserAvailability availability= userAvailability.get(index);
            if((availability.getAvailabilityType() == oldRecord.getAvailabilityType())
                    && (availability.getApplicableJobCategory().getId().equalsIgnoreCase(oldRecord.getApplicableJobCategory().getId()))
                    && availability.getNoOfHoursAWeek() == oldRecord.getNoOfHoursAWeek()){
                userAvailability.remove(index);
                userAvailability.set(index,newRecord);
                return true;
            }
        }
        return false;
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

    public List<Qualification> getQualifications() {
        return qualifications;
    }


		
	//Blacklisting an user by setting type to 'P' or 'F'
	public void setBlacklistStatus(String type)
	{
		blacklistStatus.setBlacklistStatus(type);
	}
	
	
	//Reactivating the blacklisted user
	public void removeBlacklistStatus()
	{
		blacklistStatus.removeBlacklistStatus();
	}

    /**
     * Extra function to preserve functionality
     * Must be removed later
     */
    public List<String> getJobPreferences(){
        List<String> jobPreferences= new ArrayList<>();
        for(UserAvailability availability : userAvailability){
            if(availability.getAvailabilityType() != AvailabilityType.UNKNOWN) {
                String preference = availability.getApplicableJobCategory().getCategoryTitle();
                if(!jobPreferences.contains(preference)){
                    jobPreferences.add(preference);
                }
            }
        }

        return jobPreferences;
    }


    /**
     * Getters and Setters beyond this point
     * No Functions Beyond this point
     */
    public List<EmploymentRecord> getEmploymentHistory() {
        return employmentHistory;
    }

    public void setEmploymentHistory(List<EmploymentRecord> employmentHistory) {
        this.employmentHistory = employmentHistory;
    }

    public List<UserAvailability> getUserAvailability() {
        return userAvailability;
    }

    public void setUserAvailability(List<UserAvailability> userAvailability) {
        this.userAvailability = userAvailability;
    }

    public int getComplaintCount() {
        return complaintCount;
    }

    public void setComplaintCount(int complaintCount) {
        this.complaintCount = complaintCount;
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

    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    public void setQualifications(List<Qualification> qualifications) {
        this.qualifications = qualifications;
    }

    public Blacklist getBlacklistStatus() {
        return blacklistStatus;
    }

    public void setBlacklistStatus(BlacklistStatus blacklistStatus) {
        this.blacklistStatus.setBlacklistStatus(blacklistStatus);
    }

    public ApplicantType getApplicantType() {
        return applicantType;
    }

    public void setApplicantType(ApplicantType applicantType) {
        this.applicantType = applicantType;
    }

    public String getCvPath() {
        return cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }


    public BlacklistStatus getBlacklistStat()
	{
		return this.blacklistStatus.getBlacklistStatus();
	}
		
	

	
	public Date getStartDate()
	{
		return blacklistStatus.getStartDate();
	}
	
	
}
