package hw3;                                  
import java.util.*;                             
                      
/***************************************************/                 
/* CS-350 Fall 2022 - Homework 2 - Code Solution   */                            
/* Author: Renato Mancuso (BU)                     */                            
/*                                                 */  
/* Description: This class implements the logic of */
/*   a single-processor server with an infinite    */  
/*   SJN request queue and exponent. distributed   */  
/*   service times, i.e. a x/M/1 server.           */                                   
/*                                                 */                               
/***************************************************/                            
                                  
class SJNServer extends EventGenerator {                             
                      
    private PriorityQueue<Request> theQueue = null;                 
    private Double servTime;                            
                            
    /* Statistics of this server --- to construct rolling averages */  
    private Double cumulQ = new Double(0);
    private Double cumulW = new Double(0);  
    private Double cumulTq = new Double(0);  
    private Double cumulTw = new Double(0);                                   
    private Double busyTime = new Double(0);                               
    private int snapCount = 0;                            
    private int servedReqs = 0;                                  
                             
    public SJNServer (Timeline timeline, Double servTime) {                      
	super(timeline);                 
                            
	/* Create the custom comparator for the queue */                            
	Comparator<Request> reqLengthComparator = new Comparator<Request>() {  
            @Override
            public int compare(Request r1, Request r2) {  
                if(r1.getServiceTime() <= r2.getServiceTime())  
		    return -1;                                   
		else                               
		    return 1;                            
            }                                  
        };                             
                      
	/* Now initialize the service queue */                 
	theQueue = new PriorityQueue<Request>(reqLengthComparator);                            
                            
	/* Initialize the average service time of this server */  
	this.servTime = servTime;
    }  
  
    /* Internal method to be used to simulate the beginning of service                                   
     * for a queued/arrived request. */                               
    private void __startService(Event evt, Request curRequest) {                            
	    Event nextEvent = new Event(EventType.DEATH, curRequest,                                  
					evt.getTimestamp() + curRequest.getServiceTime(), this);                             
                      
	    curRequest.recordServiceStart(evt.getTimestamp());                 
	    cumulTw += curRequest.getServiceStart() - curRequest.getArrival();                            
                            
	    /* Print the occurrence of this event */  
	    System.out.println(curRequest + " START: " + evt.getTimestamp());
  
	    super.timeline.addEvent(nextEvent);  
    }                                   
                               
    @Override                            
    void receiveRequest(Event evt) {                                  
	super.receiveRequest(evt);                             
                      
	Request curRequest = evt.getRequest();                 
                            
	curRequest.setServiceTime(Exp.getExp(1/this.servTime));                            
  
	/* Print the occurrence of this event */
	System.out.println(evt.getRequest() + " ARR: " + evt.getTimestamp()  
			   + " LEN: " + curRequest.getServiceTime());  
                                   
	curRequest.recordArrival(evt.getTimestamp());                               
                            
	/* Upon receiving the request, check the queue size and act                                  
	 * accordingly */                             
	if(theQueue.isEmpty()) {                      
	    __startService(evt, curRequest);                 
	}                            
                            
	theQueue.add(curRequest);  
    }
  
    @Override  
    void releaseRequest(Event evt) {                                   
	/* What request we are talking about? */                               
	Request curRequest = evt.getRequest();                            
                                  
	/* Remove the request from the server queue */                             
	boolean removedOkay = theQueue.remove(curRequest);                      
                 
	/* If the following is not true, something is wrong */                            
	assert removedOkay;                            
  
	curRequest.recordDeparture(evt.getTimestamp());
  
	/* Update busyTime */  
	busyTime += curRequest.getDeparture() - curRequest.getServiceStart();                                   
                               
	/* Update cumulative response time at this server */                            
	cumulTq += curRequest.getDeparture() - curRequest.getArrival();                                  
                             
	/* Update number of served requests */                      
	servedReqs++;                 
                            
	assert super.next != null;                            
	super.next.receiveRequest(evt);  

	/* Any new request to put into service?  */  
	if(!theQueue.isEmpty()) {  
	    Request nextRequest = theQueue.peek();                                   
                               
	    __startService(evt, nextRequest);                            
	}                                  
                             
    }                      
                 
    @Override                            
    Double getRate() {                            
	return 1/this.servTime;  
    }
	
  
    @Override  
    void executeSnapshot() {                                   
	snapCount++;                               
	cumulQ += theQueue.size();                            
	cumulW += Math.max(theQueue.size()-1, 0);                                  
    }                             
                      
    @Override                 
    void printStats(Double time) {                            
	System.out.println("UTIL: " + busyTime/time);                            
	System.out.println("QAVG: " + cumulQ/snapCount);  
	System.out.println("WAVG: " + cumulW/snapCount);
	System.out.println("TRESP: " + cumulTq/servedReqs);  
	System.out.println("TWAIT: " + cumulTw/servedReqs);  
    }                                   
                               
}                            
                                  
/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */                             
                      
                 
                            
                            
  

  
  
                                   
                               
                            
                                  
                             
                      
                 
                            
                            
  

  
  
                                   
                               
                            
