import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Hash{
    public String hash(int to_hash) throws NoSuchAlgorithmException{
        MessageDigest MD5 = MessageDigest.getInstance("MD5");
        String values = Integer.toString(to_hash);
        byte[] bin = MD5.digest(values.getBytes());
        BigInteger hashed_int = new BigInteger(1,bin);
        String return_hash = hashed_int.toString(16);
        for(int i = return_hash.length(); i < 32; i++ ){
            return_hash = "0"+return_hash;
        }
        return return_hash;
    }
}