package model.entities;
import java.io.File;
import java.text.ParseException;
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

    // 07-10-2020 - Added by Yogeshwar - Employer needs to update employmentStatus if made an offer
    private EmploymentStatus employmentStatus;
    private Date lastStatusUpdateDate;
    private List<JobApplication> jobApplications;


    public Applicant(String id,String email, String password, String firstName, String lastName, String phoneNumber, String applicantType) {
        super(id, email, password, firstName, lastName, phoneNumber);
        this.employmentHistory = new ArrayList<>();
        this.userAvailability = new ArrayList<>();
        this.complaintCount = 0;
        this.licenses = new ArrayList<>();
        this.references = new ArrayList<>();
        this.qualifications = new ArrayList<>();
        this.blacklistStatus = new Blacklist();
        if(applicantType.equalsIgnoreCase("l")){
            this.applicantType = ApplicantType.LOCAL;
        }else {
            this.applicantType = ApplicantType.INTERNATIONAL;
        }
        this.cvPath = null;
        this.employmentStatus = EmploymentStatus.AVAILABLE;
        this.lastStatusUpdateDate = new Date();
        this.jobApplications = new ArrayList<>();
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
            if (validEmploymentRecord(record)){
                employmentHistory.add(record);
                return true;
            }
        } else{
            throw new DuplicateEntryException("This Employment record is already present");
        }
        return false;
    }

    public boolean validEmploymentRecord(EmploymentRecord record) throws BadEmployeeRecordException {

        if(!record.getCurrentCompany()) {
            if (record.getStartDate().after(record.getEndDate()) || record.getStartDate().equals(record.getEndDate())) {
                throw new BadEmployeeRecordException("Start Date should be less then end date");
            }
        }

        return true;
    }


    public boolean updateEmploymentRecords(EmploymentRecord record, int recordIndex) throws NoSuchRecordException, BadEmployeeRecordException {
        if (recordIndex > employmentHistory.size()){
            throw new NoSuchRecordException("No Such Employment Record Exists");
        }

        if (validEmploymentRecord(record)){
            employmentHistory.set(recordIndex,record);
            return true;
        }

        return false;
    }

    public boolean addQualifications(Qualification qualification) throws BadQualificationException, DuplicateEntryException, BadEntryException {
        boolean qualificationFound = false;

        for(Qualification currentQual:qualifications){
            if(currentQual.getQualificationLevel().equals(qualification.getQualificationLevel())){
                qualificationFound = true;
                break;
            }
        }
        if (!qualificationFound) {
            if (validQualification(qualification)) {
                qualifications.add(qualification);
                return true;
            }
        } else{
            throw new DuplicateEntryException("This qualification is already present");
        }
        return false;
    }

    public boolean validQualification(Qualification qualification) throws BadEntryException {

        if(qualification.getStartDate().after(qualification.getEndDate())
                || qualification.getStartDate().equals(qualification.getEndDate())){
            throw new BadEntryException("Start date must be less than end date");
        } else if (qualification.getMarksObtained() <= 0){
            throw new BadEntryException("Marks Obtained for the Qualification cannot be  equal to or less than 0 ");
        }
        return true;
    }

    public boolean updateQualifications(Qualification qualification, int recordIndex) throws NoSuchRecordException, BadEntryException {
        if (recordIndex > qualifications.size()){
            throw new NoSuchRecordException("No Such Qualification Exists");
        }

        if (validQualification(qualification)){
            qualifications.set(recordIndex,qualification);
            return true;
        }

        return false;
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

    public boolean updateReferences(Reference reference, int recordIndex) throws NoSuchRecordException {
        if (recordIndex > references.size()){
            throw new NoSuchRecordException("No Such Reference Exists");
        }

        references.set(recordIndex, reference);

        return true;
    }

    public boolean addLicenses(License license) throws DuplicateEntryException, BadEntryException {
        boolean licenseFound = false;

        for(License currentLicense : licenses) {
            if ( currentLicense.getId().equals(license.getId())){
                licenseFound = true;
                break;
            }
        }

        if (validLicense(license)){
            if(!licenseFound){
                licenses.add(license);
                return true;
            } else {
                throw new DuplicateEntryException("This License already present");
            }
        }
        return true;
    }

    public boolean validLicense(License license) throws BadEntryException {

        Date date = new Date();

        if (date.after(license.getValidTill()) || date.equals(license.getValidTill())){
            throw new BadEntryException("Validity date of the license should be more than current date.");
        }

        return true;
    }

    public boolean updateLicense(License license, int recordIndex) throws NoSuchRecordException, BadEntryException {
        if (recordIndex > licenses.size()){
            throw new NoSuchRecordException("No Such License Exists");
        }

        if (validLicense(license)){
            licenses.set(recordIndex,license);
            return true;
        }

        return false;

    }

    public boolean addAvailability(AvailabilityType availabilityType, List<JobCategory> jobCategories, int hoursPerWeek, Date periodStartDate, Date periodEndDate) throws DuplicateEntryException, BadEntryException {
        boolean duplicateAvailability= false;
        String nullObjectExceptionMessage= "";
        if(availabilityType == null){
            nullObjectExceptionMessage= "Availability Type passed points to null";
        }
        if(jobCategories == null){
            nullObjectExceptionMessage= "List of JobCategories passed points to null";
        }
        if(periodStartDate == null){
            nullObjectExceptionMessage= "Period Start Date passed points to null";
        }

        if(periodEndDate == null){
            nullObjectExceptionMessage= "Period End Date passed points to null";
        }

        if(!nullObjectExceptionMessage.isEmpty()){
            throw new NullObjectException(nullObjectExceptionMessage);
        }

        UserAvailability availability = new UserAvailability(jobCategories, availabilityType, hoursPerWeek, periodStartDate, periodEndDate);

        if(validAvailability(availability)){
            for(UserAvailability currentAvailability: userAvailability){
                if((currentAvailability.getAvailabilityType() == availability.getAvailabilityType())
                        && currentAvailability.getNoOfHoursAWeek() == availability.getNoOfHoursAWeek()){
                    duplicateAvailability= true;
                    break;
                }
            }

            if(duplicateAvailability) {
                throw new DuplicateEntryException("User Availability already present");
            }

            userAvailability.add(availability);
        }

        return true;
    }

    public boolean validAvailability(UserAvailability availability) throws BadEntryException {


        if(availability.getPeriodStartDate().after(availability.getPeriodEndDate())
                || availability.getPeriodStartDate().equals(availability.getPeriodEndDate())){
            throw new BadEntryException("Start date must be less than end date");
        }

        return true;
    }

    public boolean updateAvailability(UserAvailability availability, int recordIndex) throws BadEntryException, NoSuchRecordException {


        //UserAvailability availability = new UserAvailability(jobCategories, availabilityType, hoursPerWeek, periodStartDate, periodEndDate);

        if (recordIndex > userAvailability.size()){
            throw new NoSuchRecordException("No Such Job Preference");
        }

        if(validAvailability(availability)){
            //userAvailability.remove(recordIndex);
            userAvailability.set(recordIndex,availability);
            return true;
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

    public boolean selectInterviewTiming(int jobApplicationIndex, int preferredDateIndex){

        if(jobApplicationIndex > jobApplications.size()){
            return false;
        }
        JobApplication jobApplication = jobApplications.get(jobApplicationIndex);
        if(preferredDateIndex > jobApplication.getJobRef().getAvailInterviewTimings().size()){
            return false;
        }
        Date preferredDate = jobApplication.getJobRef().getAvailInterviewTimings().get(preferredDateIndex);
        Interview interview = new Interview(preferredDate);
        jobApplication.setInterview(interview);
        return true;
    }

    /**
     * Possible inputs for decision - (0 or 1)
     * 0 -> Rejected Offer
     * 1 -> Accepted Offer
     */
    public boolean replyToJobOffer(int jobApplicationIndex, int decision){
        if(jobApplicationIndex > jobApplications.size() || jobApplications.get(jobApplicationIndex).getOfferRef() == null){
            return false;
        }
        JobApplication jobApplication = jobApplications.get(jobApplicationIndex);
        boolean update = false;
        if(decision == 1){
            jobApplication.getOfferRef().setOfferStatus(OfferStatus.ACCEPTED);
            this.employmentStatus = EmploymentStatus.EMPLOYED;
            update = true;
        }else if(decision == 0){
            jobApplication.getOfferRef().setOfferStatus(OfferStatus.REJECTED);
            update = true;
        }
        return update;
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
	public void setBlacklistStatus(String type) throws ParseException
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
                //String preference = availability.getApplicableJobCategory().getCategoryTitle();
//                if(!jobPreferences.contains(preference)){
//                    jobPreferences.add(preference);
//                }
            }
        }

        return jobPreferences;
    }

    public void addJobApplication(JobApplication jobApplication){
        this.jobApplications.add(jobApplication);
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

    public List<JobApplication> getJobApplications() {
        return jobApplications;
    }

    public void setJobApplications(List<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

    public Date getBlacklistStartDate()
    {
        return blacklistStatus.getStartDate();
    }


    // 07-10-2020 - Yogeshwar Chaudhari - Employer needs employementStatus while searching, shortlisting and making an offer
    public EmploymentStatus getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(EmploymentStatus employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Date getLastStatusUpdateDate() {
        return lastStatusUpdateDate;
    }

    public void setLastStatusUpdateDate(Date lastStatusUpdateDate) {
        this.lastStatusUpdateDate = lastStatusUpdateDate;
    }
}
