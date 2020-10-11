package model.entities;
import model.enums.OfferStatus;
import java.util.Date;

/**
 * @author Yogeshwar Chaudhari
 * Model class implementing the job offer related functionality.
 * When employer has finalized the candidate, then he releases job offer to the applicant.
 * Applicant can accept or reject the offer - represented by OfferStatus
 */
public class JobOffer {

    String employementDetails;
    Date offeredOn;
    OfferStatus offerStatus;
    Date acceptedOn;

    public JobOffer(String employementDetails){
        this.employementDetails = employementDetails;
        offeredOn = new Date();
        offerStatus = OfferStatus.PENDING;
    }

    public String getEmployementDetails() {
        return employementDetails;
    }

    public void setEmployementDetails(String employementDetails) {
        this.employementDetails = employementDetails;
    }

    public Date getOfferedOn() {
        return offeredOn;
    }

    public void setOfferedOn(Date offeredOn) {
        this.offeredOn = offeredOn;
    }

    public OfferStatus getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(OfferStatus offerStatus) {
        this.offerStatus = offerStatus;
    }

    public Date getAcceptedOn() {
        return acceptedOn;
    }

    public void setAcceptedOn(Date acceptedOn) {
        this.acceptedOn = acceptedOn;
    }
}
