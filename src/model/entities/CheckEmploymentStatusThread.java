package model.entities;

import model.enums.EmploymentStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CheckEmploymentStatusThread implements Runnable{

    private HashMap<String, User> allUsersList;

    public CheckEmploymentStatusThread(HashMap<String, User> allUsersList){
        this.allUsersList = allUsersList;
    }

    @Override
    public void run() {
        Set<String> allUsers = allUsersList.keySet();
        for(String userId : allUsers){
            User user = allUsersList.get(userId);
            if(user instanceof Applicant){
                Applicant applicant = (Applicant) user;
                Date currDate = new Date();
                long noOfInactiveDays = findDifferenceInDays(currDate, applicant.getLastStatusUpdateDate());
                if(noOfInactiveDays > 14){
                    applicant.setEmploymentStatus(EmploymentStatus.UNKNOWN);
                }
            }
        }
    }

    /**
     * @param firstDate
     * @param secondDate
     * @return firstDate - secondDate as no of days in long
     */
    private long findDifferenceInDays(Date firstDate, Date secondDate){

        long differenceInMilliseconds = Math.abs(firstDate.getTime() - secondDate.getTime());
        long differenceInDays = TimeUnit.DAYS.convert(differenceInMilliseconds, TimeUnit.MILLISECONDS);

        return differenceInDays;
    }
}
