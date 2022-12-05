package hw7;
import java.io.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;

import java.nio.file.*;
public class Pirate {
    String file; 
    int numCPUs;
    int timeoutMillis;
    Dispatcher dispatched;
    LinkedList<WorkUnit> workQueue;
    LinkedList<WorkUnit> resQueue;
    Semaphore wqMutex;
    Semaphore rsMutex;

    public Pirate (String file, int N, int timeout) {
		this.file = file;
		this.numCPUs = N;
		this.timeoutMillis = timeout;
		workQueue = new LinkedList<WorkUnit>();
		resQueue = new LinkedList<WorkUnit>();	
		wqMutex = new Semaphore(1);
		rsMutex = new Semaphore(1);
        this.dispatched = new Dispatcher(workQueue,resQueue, N, timeout, wqMutex, rsMutex);	
    }

    private void queue_init() throws InterruptedException {
        Path path_to_file = Paths.get(file);
        if (Files.exists(path_to_file)) {
            File input_file = path_to_file.toFile();
            try (BufferedReader in = new BufferedReader(new FileReader(input_file)))
            {
                String hash_line;
                while((hash_line = in.readLine()) != null){
					WorkUnit work = new WorkUnit(hash_line);
					dispatched.insertWorker(work);
                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }	    
        } 	
    }

    private void work_init() throws InterruptedException {
		ArrayList<Integer> num = new ArrayList<Integer>();
		ArrayList<String> incomplete = new ArrayList<String>();
		for(int i = 0; i < resQueue.size(); i++)
		{
			String res = resQueue.get(i).getResult();
			if (res != null) {
				num.add(Integer.parseInt(res));
				System.out.println(res);
			}
			else{
				incomplete.add(resQueue.get(i).getHash());
			}
		}
		resQueue.clear();
		Collections.sort(num);
		int x ,y,z = 0;
		
		x = 0;
		int temp = num.size();

		while(x <temp-1){
			y= x+1;
			while(y< temp){
				z = 0;
				while(z < incomplete.size()){
					WorkUnit work = new WorkUnit(incomplete.get(z), num.get(x), num.get(y));
					dispatched.insertWorker(work);
					z++;
				}
				y++;
			}
			x++;
		}
    }

    private void printResult() throws InterruptedException {
		HashMap<String, Boolean> unfound = new HashMap<String, Boolean>();
		for(int i = 0; i < resQueue.size(); i++){
			String hash_str = resQueue.get(i).getHash();
			unfound.put(hash_str, true);
		}
		for(int i = 0; i < resQueue.size(); i++){
			String result = resQueue.get(i).getResult();
			String hash_str = resQueue.get(i).getHash();
			if (result != null) {
				System.out.println(result);
				unfound.remove(hash_str);
			}
		}
		for (String h : unfound.keySet()) {
			System.out.println(h);
		}	
    }
    
    public void treasureHunter () throws InterruptedException
    {
		//start
		queue_init();
		dispatched.dispatch();
		rsMutex.acquire();	
		
		//process and print
		work_init();
		rsMutex.release();
		dispatched.dispatch();
		rsMutex.acquire();
		printResult();

		//end
		rsMutex.release();
		dispatched.endWorkUnit();
	
    }

    public static void main(String[] args) throws InterruptedException
    {   
        String path_to_file = args[0];
		int N = Integer.parseInt(args[1]);
		int timeoutMillis = 100000;
		if (args.length > 2) {
			timeoutMillis = Integer.parseInt(args[2]);
		}
        Pirate pirateCrew = new Pirate(path_to_file, N, timeoutMillis);
        pirateCrew.treasureHunter();
    }
}
