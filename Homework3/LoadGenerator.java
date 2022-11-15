package hw3;                                  
                             
/***************************************************/                      
/* CS-350 Fall 2022 - Homework 2 - Code Solution   */                 
/* Author: Renato Mancuso (BU)                     */                            
/*                                                 */                            
/* Description: This class implements the logic of */  
/*   a traffic source from the outside of the      */
/*   simulated system. The inter-arrival of the    */  
/*   incoming traffic is exponentially distributed */  
/*                                                 */                                   
/***************************************************/                               
                            
class LoadGenerator extends EventGenerator {                                  
    private Double rate;
    private Double rateX;
	private Double rateY;                      
                 
    /* Construct a traffic source */                            
    public LoadGenerator (Timeline timeline, Double lambdaX, Double lambdaY) {                            
		super(timeline);  
		this.rateX = lambdaX;
		this.rateY = lambdaY;
		//check before 
		/* Insert the very first event into the timeline at time 0 */ 
		String type1 = "X";
		String type2 = "Y";
		Request firstRequestX = new Request(this, type1);                                   
		Event firstEventX = new Event(EventType.BIRTH, firstRequestX, 0.0, this); 

		Request firstRequestY = new Request(this, type2);                                   
		Event firstEventY = new Event(EventType.BIRTH, firstRequestY, 0.0, this); 
      
		super.timeline.addEvent(firstEventX);    
		super.timeline.addEvent(firstEventY);  
		                            
    }                             
                      
    @Override                 
    void releaseRequest(Event evt) {                            
	Request curRequest = evt.getRequest();                            
	curRequest.recordArrival(evt.getTimestamp());  

	/* When it's time to process as new arrival, generate the next  
	 * arrival and request, and hand-off the current request to  
	 * the next hop */                     
	String currtype = curRequest.getType();              
	Request nextReq = new Request(this, currtype);
	Event nextEvent;
	if(currtype.equals("X")){
		nextEvent = new Event(EventType.BIRTH, nextReq, evt.getTimestamp() + Exp.getExp(this.rateX), this); 
		this.rate = this.rateX;
	}  
	else{                              
		nextEvent = new Event(EventType.BIRTH, nextReq, evt.getTimestamp() + Exp.getExp(this.rateY), this);    
		this.rate = this.rateY;
	}                              
	super.timeline.addEvent(nextEvent);                      
                 
	assert super.next != null;                            
	super.next.receiveRequest(evt);                            
    }  

    /* This is a special case. Technically, a request leaving a generator is a DEATH event at the generator. But for simplicity we can keep it as a BIRTH at the next element downstream. */  
    @Override  
    void receiveRequest(Event evt) {                                   
    	 releaseRequest(evt);                               
    }                            
                                  
    // @Override                             
     Double getRate() {
	 	return this.rate;
                
    }

	// @Override      
	// Double getRateX(){
	// 	return this.rateX;
	// }    
	// @Override
	// Double getRateY(){
	// 	return this.rateY;
	// }                     
}                            
  
/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
  
  
                                   
                               
                            
                                  
                             
                      
                 
                            
                            
  

  
  
                                   
                               
                            
