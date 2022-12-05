package hw7;
import java.io.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.*;
import java.nio.file.*;
public class Dispatcher {
	String fileName;
	int numCPUs;
	int timeoutMillis;
	ArrayList<UnHashWorker> workers;
	int count;
    int process;
    LinkedList<WorkUnit> workQueue;
    Semaphore wqSem;
    Semaphore wqMutex;
    LinkedList<WorkUnit> resQueue;
    Semaphore rsSem;
    Semaphore rsMutex;
    
    public Dispatcher (LinkedList<WorkUnit> workQueue, LinkedList<WorkUnit> resQueue, int N, int timeout,Semaphore wqMutex, Semaphore rsMutex)
	{
	    this.numCPUs = N;
	    this.timeoutMillis = timeout;
	    this.count = 0;       
	    this.workQueue = workQueue;
	    this.resQueue = resQueue;
	    this.wqMutex = wqMutex;
	    this.rsMutex = rsMutex;
	    workers = new ArrayList<UnHashWorker>();
	    wqSem = new Semaphore(0);
	    rsSem = new Semaphore(0);
	    for (int i = 0; i < N; ++i) {
			UnHashWorker worker = new UnHashWorker(workQueue, resQueue, wqSem, wqMutex, rsSem, rsMutex);
			worker.setTimeout(timeout);
			workers.add(worker);
			worker.start();
	    }
	}
    
    public void insertWorker(WorkUnit work) throws InterruptedException
	{
	    wqMutex.acquire();
	    workQueue.add(work);
	    count++;
	    wqSem.release();
	    wqMutex.release();
	}
    
    public void endWorkUnit ()
	{
	    for (UnHashWorker w : workers)
		{
			w.exitWorker();
		}
	    for (UnHashWorker w : workers)
		{
			wqSem.release();
	    }

	}
    private void processing() throws InterruptedException
	{
	    wqMutex.acquire();
	    rsMutex.acquire();

	    for (int i = process; i < resQueue.size(); i++) {
			WorkUnit temp_worker = resQueue.get(i);
			if (!temp_worker.isSimple() && temp_worker.getResult() != null) {
				int upper_bound = temp_worker.getUpperBound();
				int lower_bound = temp_worker.getLowerBound();
				for (int j = 0; j < workQueue.size(); j++) {
					WorkUnit work = workQueue.get(j);
					int checkup = work.getUpperBound();
					int checkdown = work.getLowerBound();
					if (checkup == upper_bound || checkdown == lower_bound|| checkdown == upper_bound || checkup == lower_bound){
						workQueue.remove(j--);
						count-=1;
					}
				}
			}
			process+=1;
	    }
	    rsMutex.release();
	    wqMutex.release();	    
	}
    
    public void dispatch () throws InterruptedException
	{
	    process = 0;
	    while(count-- > 0) {
			rsSem.acquire();
			processing();
	    }
	}
}
