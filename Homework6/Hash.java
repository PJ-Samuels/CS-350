package hw6;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
public class Hash {
    public static String hash(int to_hash)throws NoSuchAlgorithmException{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String val = Integer.toString(to_hash);
        byte[] bin = md5.digest(val.getBytes());
        BigInteger hashed_int = new BigInteger(1,bin);
        String hash = hashed_int.toString(16);
        for(int i = hash.length(); i < 32; i++ ){
            hash = "0"+hash;
        }
        return hash;
    }    
}