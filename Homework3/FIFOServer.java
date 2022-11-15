package hw3;                                  
import java.util.*;                             
                      
/***************************************************/                 
/* CS-350 Fall 2022 - Homework 2 - Code Solution   */                            
/* Author: Renato Mancuso (BU)                     */                            
/*                                                 */  
/* Description: This class implements the logic of */
/*   a single-processor server with an infinite    */  
/*   FIFO request queue and exponent. distributed  */  
/*   service times, i.e. a x/M/1 server.           */                                   
/*                                                 */                               
/***************************************************/                            
                                  
class FIFOServer extends EventGenerator {                             
                      
    private LinkedList<Request> theQueue = new LinkedList<Request>();                 
	private Double servTime; 
	private double servTimeX; 
	private double servTimeY;                            
                            
    /* Statistics of this server --- to construct rolling averages */  
    private Double cumulQ = 0.0;
    private Double cumulW = 0.0;  
    //private Double cumulTq = 0.0;
	private Double cumulTqX = 0.0;
	private Double cumulTqY = 0.0;  
    //private Double cumulTw = 0.0; 
	private Double cumulTwX = 0.0;
	private Double cumulTwY = 0.0;
	private Double busyTime = 0.0;                                    
    private Double busyTimeX = 0.0;    
	private Double busyTimeY = 0.0;                           
    private int snapCount = 0;                            
    //private int servedReqs = 0;   
	private int servedReqsX = 0;  
	private int servedReqsY = 0;         

                             
    public FIFOServer (Timeline timeline, double servTimeX, double servTimeY) {                      
		super(timeline);                 
                            
	/* Initialize the average service time of this server */   
		this.servTimeX = servTimeX;
		this.servTimeY = servTimeY;
		//this.servTime = servTime;

    }
  
    /* Internal method to be used to simulate the beginning of service  
     * for a queued/arrived request. */                                   
    private void __startService(Event evt, Request curRequest) {                               
	    Event nextEvent = new Event(EventType.DEATH, curRequest,evt.getTimestamp() + curRequest.getServiceTime(), this);                                  
        String tempType = curRequest.getType();
	    curRequest.recordServiceStart(evt.getTimestamp());  
		if(tempType.equals("X")){
			cumulTwX += curRequest.getServiceStart() - curRequest.getArrival();
			servTime = servTimeX;
		}
		else{
			cumulTwY += curRequest.getServiceStart() - curRequest.getArrival();
			servTime = servTimeY;
		}
	                     
                            
	    /* Print the occurrence of this event */                            
	    System.out.println(curRequest + " START: " + evt.getTimestamp());  
	    super.timeline.addEvent(nextEvent);  
    }  
                                   
    @Override                               
    void receiveRequest(Event evt) {                            
		super.receiveRequest(evt);                                  	
		Request curRequest = evt.getRequest();                      

		if(curRequest.getType().equals("X")){
			//System.out.println("ServTimeX:"+ servTimeX);
			servTime = servTimeX;
			curRequest.setServiceTime(Exp.getExp(1/this.servTime));
		}                         
		else if(curRequest.getType().equals("Y")){
			//System.out.println("ServTimeY:"+ servTimeY);
			servTime = servTimeY;
			curRequest.setServiceTime(Exp.getExp(1/this.servTime));      
		}
		//curRequest.setServiceTime(Exp.getExp(1/this.servTimeX));      
								
		/* Print the occurrence of this event */  
		System.out.println(evt.getRequest() + " ARR: " + evt.getTimestamp()+ " LEN: " + curRequest.getServiceTime());  
	
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
		Request queueHead = theQueue.removeFirst();                             
						
		/* If the following is not true, something is wrong */                 
		assert curRequest == queueHead;                            
								
		curRequest.recordDeparture(evt.getTimestamp());  

		/* Update busyTime */  
		//busyTime += curRequest.getDeparture() - curRequest.getServiceStart();  
									
		/* Update cumulative response time at this server */      
		if(curRequest.getType().equals("X")){
			busyTimeX += curRequest.getDeparture() - curRequest.getServiceStart();  
			cumulTqX += curRequest.getDeparture() - curRequest.getArrival();
			servedReqsX++;   
		}                         
		else if(curRequest.getType().equals("Y")){
			busyTimeY += curRequest.getDeparture() - curRequest.getServiceStart();  
			cumulTqY += curRequest.getDeparture() - curRequest.getArrival();  
			servedReqsY++; 
		}
		/* Update cumulative response time at this server */                               
		//cumulTq += curRequest.getDeparture() - curRequest.getArrival();                            
									
		/* Update number of served requests */                             
		//servedReqs++;                      
					
		assert super.next != null;                            
		super.next.receiveRequest(evt);                            
	
		/* Any new request to put into service?  */
		if(!theQueue.isEmpty()) {  
			Request nextRequest = theQueue.peekFirst();  
									
			__startService(evt, nextRequest);                               
		}                            
                                  
    }                             
	@Override
	void printTest(){
		
	}
	@Override
	void setServTimeX(double servTimeX){
		this.servTimeX = servTimeX;
		this.servTime = servTimeX;
		//System.out.println("Test1");
	}
	@Override
	void setServTimeY(double servTimeY){
		this.servTimeY = servTimeY;
		this.servTime = servTimeY;
		//System.out.println("Test2");
	}
	@Override                 
    Double getRate()
	 {               
		return 1/this.servTime;                            
	
	}
	// double setServTimeX(double servTimeX){
	// 	this.servTimeX = servTimeX;
	// 	this.servTime = servTimeX;
	// 	return this.servTime;
	// }
	// double setServTimeY(double servTimeY){
	// 	this.servTimeY = servTimeY;
	// 	this.servTime = servTimeY;
	// 	return this.servTime;
	// }
    @Override  
    void executeSnapshot() {  
	snapCount++;                                   
	cumulQ += theQueue.size();                               
	cumulW += Math.max(theQueue.size()-1, 0);                            
    }                                  
                             
    @Override                      
    void printStats(Double time) {                 
	System.out.println("UTIL: " + (busyTimeX+busyTimeY)/time);                            
	System.out.println("QAVG: " + cumulQ/snapCount);                            
	System.out.println("WAVG: " + cumulW/snapCount);  
	System.out.println("TRESPX: " + cumulTqX/servedReqsX);
	System.out.println("TWAITX: " + (cumulTwX - busyTimeX)/servedReqsX); 
	System.out.println("TRESPY: " + cumulTqY/servedReqsY);
	System.out.println("TWAITY: " + (cumulTwY - busyTimeY)/servedReqsY);  


    }  
                                   
}                               
                            
/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */                                  
                             
                      
                 
                            
                            
  

  
  
                                   
                               
                            
                                  
                             
                      
                 
                            
                            
  

  
  
                                   
                               
                            
