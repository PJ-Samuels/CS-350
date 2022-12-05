package hw7;
import java.lang.*;
import java.util.*;
import java.util.concurrent.*;
import java.security.*;
public class UnHashWorker extends Thread {
    static int __classID = 0;
    int threadID;
    int timeoutMillis;
    LinkedList<WorkUnit> workQueue;
    Semaphore wqSem; 
    Semaphore wqMutex;
    LinkedList<WorkUnit> resQueue;
    Semaphore rsSem;
    Semaphore rsMutex;
    volatile boolean stopThread = false;
    
    public UnHashWorker (LinkedList<WorkUnit> workQueue, LinkedList<WorkUnit> resQueue, Semaphore wqSem, Semaphore wqMutex,Semaphore rsSem, Semaphore rsMutex)
    {
		super();
		this.workQueue = workQueue;
		this.resQueue = resQueue;
		this.wqSem = wqSem;
		this.rsSem = rsSem;
		this.wqMutex = wqMutex;
		this.rsMutex = rsMutex;	
		
		this.timeoutMillis = 10000;

		this.threadID = ++__classID;
    }
    

    public WorkUnit timedUnhash (WorkUnit input)
    {
		WorkUnit result = input;
		long timeStart = System.currentTimeMillis();

		String prefix = "";
		String suffix = "";

		if (!input.isSimple()) {
			prefix += input.getLowerBound() + ";";
			suffix += ";" + input.getUpperBound();
		}


        Hash hasher = new Hash();
		String to_unhash = input.getHash();	
	
        for(int cur = input.getLowerBound()+1; cur < input.getUpperBound(); ++cur) {
	    String numString = prefix + Integer.toString(cur) + suffix;
            String tmpHash = "";

	    try {
			tmpHash = hasher.hash(numString);
        } catch (NoSuchAlgorithmException ex) {
			result.setResult("???");
			break;
	    }
	    
        if(tmpHash.equals(to_unhash)) {
			result.setResult(numString);
			break;
	    }

	    if (System.currentTimeMillis() > timeStart + this.timeoutMillis) {
		result.setResult(null);
		break;
	    }
        }

	return result;
	
    }
    
    public void run () 
    {
		while(!this.stopThread) {

			WorkUnit work = null, result = null;

			try {
				wqSem.acquire();
			} catch (InterruptedException e) {
			}
			
			if (this.stopThread) {
				break;
			}
			
			try {	    
				wqMutex.acquire();
			} catch (InterruptedException e) {
			}
			
			try {
				work = workQueue.remove();
			} catch (NoSuchElementException e) {}

			wqMutex.release();
	    
			if (work != null) {
			result = timedUnhash(work);

			try {
				rsMutex.acquire();
			} catch (InterruptedException e) {
			}
			
			resQueue.add(result);
			rsMutex.release();
			rsSem.release();
			}
		}
    }

    public void exitWorker () {
	this.stopThread = true;
    }

    public void setTimeout(int timeout) {
	this.timeoutMillis = timeout;
    }
    
}

