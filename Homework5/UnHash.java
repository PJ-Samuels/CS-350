import java.security.NoSuchAlgorithmException;
public class UnHash{
    public int unhash(String to_unhash) throws NoSuchAlgorithmException{
        Hash hash = new Hash();
        int iterater = 0;
        Boolean infinte_looper = true;
        while(infinte_looper){
            String temp_has = hash.hash(iterater);
            if(temp_has.equals(to_unhash))
                return iterater;
            iterater++;
        }
        return iterater;
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException{
        String input = args[0];
        UnHash unhashed = new UnHash();
        System.out.println(unhashed.unhash(input));
    }
}
