package hw6;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
public class UnHash {
    public static int unhash(String to_unhash, int timeout_threshold) throws NoSuchAlgorithmException{
        Date start_date = new Date();
        double start_time = start_date.getTime();
        String hash = "";
        int temp = 0;
        while(!(hash.equals(to_unhash))){
            Date current_date = new Date();
            double current_time = current_date.getTime();
            double total_time = current_time - start_time;
            if(total_time < timeout_threshold){
                hash = Hash.hash(temp);
                temp+=1;
            }
            else
                return -1;            
        }
        return temp -=1;
    }   

    public static int unhash(String to_unhash) throws NoSuchAlgorithmException{
        String hash = "";
        int temp = 0;
        while(!hash.equals(to_unhash)){
            hash = Hash.hash(temp);
            temp+=1;   
        }
        return temp-=1;
    }   
}
