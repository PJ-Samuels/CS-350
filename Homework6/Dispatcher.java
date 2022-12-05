package hw6;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.security.NoSuchAlgorithmException;

public class Dispatcher extends Thread {
    public static ArrayList<String> list_of_hashes = new ArrayList<String>();
    public static int time = 0;
    public static boolean queue_status = false;
    public static boolean queue_head_status = true;

    public static void list_add(String hash_file) {
        BufferedReader in;
        try{
            in = new BufferedReader(new FileReader(hash_file));
            String hash_string = in.readLine();
            while (hash_string != null){
                list_of_hashes.add(hash_string);
                hash_string = in.readLine();
            }
            in.close();
        }
        catch(IOException IOe){
            IOe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String hash_file = args[0];
        int number_of_threads = Integer.parseInt(args[1]);
        list_add(hash_file);
        try{
            time = Integer.parseInt(args[2]);
        } 
        catch (Exception e){}
        
        int counter = 0;
        do{
            Dispatcher dispatch_thread = new Dispatcher();
            dispatch_thread.start();
            counter +=1;
        }
        while(counter < number_of_threads);
    }

    public void run(){
        while(!queue_status){
            if(queue_head_status){
                int size = list_of_hashes.size();
                if(size > 0){
                    queue_head_status = false;
                    String temp = list_of_hashes.remove(0);
                    queue_head_status = true;
                    int val = 0;
                    try {
                        val = time == 0 ? UnHash.unhash(temp) : UnHash.unhash(temp, time);
                    }catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    if(val == -1)
                        System.out.println(temp);
                    else
                        System.out.println(val);
                }
                else
                    queue_status = true;
            }
        }
    }
    
}
