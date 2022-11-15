
import java.io.*;
import java.security.*;
import java.nio.file.*;

public class Dispatcher {
    public void dispatch (String fileName) throws NoSuchAlgorithmException{
        Path file_path = Paths.get(fileName);
        UnHash unhash = new UnHash();
        if (Files.exists(file_path)) {
            File hash_file= file_path.toFile();
            try (BufferedReader in = new BufferedReader(new FileReader(hash_file))){
                String input;               
                while((input = in.readLine()) != null){
                    int result = unhash.unhash(input);
                    System.out.println(result);
                }
            }
            catch (FileNotFoundException fnfe){
                fnfe.printStackTrace();
            }
            catch (IOException IOe){
                IOe.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws NoSuchAlgorithmException
    {
        String file_name = args[0];
        Dispatcher dispatched = new Dispatcher();
        dispatched.dispatch(file_name);
    }
}

