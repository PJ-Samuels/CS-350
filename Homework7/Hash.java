package hw7;
import java.math.BigInteger;
import java.security.*;
public class Hash {
    public String hash(String numString) throws NoSuchAlgorithmException 
    {
        MessageDigest md5dig = MessageDigest.getInstance("MD5");
        byte[] hashBin = md5dig.digest(numString.getBytes());
        BigInteger hashInt = new BigInteger(1, hashBin);
        String retval = hashInt.toString(16);
        while (retval.length() < 32) {
            retval = "0" + retval;
        }
        return retval;
    }
}
