package model.exceptions;

/**
 * @author Yogeshwar Chaudhari
 * Usually thrown when the null entity is found. Like null interviewRef, null jobRef etc
 */
public class NullEntityException extends Exception{

    public NullEntityException(String msg){
        super(msg);
    }
}
