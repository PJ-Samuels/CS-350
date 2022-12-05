import java.security.*;
import java.math.BigInteger;

public class Hash {
    ///string
    String hash(String numString) {
        try {
            MessageDigest md5dig = MessageDigest.getInstance("MD5");
            byte[] hashbin = md5dig.digest(numString.getBytes());
            BigInteger tempNum = new BigInteger(1, hashbin);
            String retval = tempNum.toString(16);
            if (retval.length() < 32) {
                while (retval.length() < 32) {
                    retval = "0" + retval;
                }
            }
            return retval;
        } catch (Exception e) {
            return "";
        }
    }
}
