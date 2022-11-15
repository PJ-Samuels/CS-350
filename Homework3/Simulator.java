package hw3;                                  
import java.lang.*;                             
import java.util.*;                      
                 
/***************************************************/                            
/* CS-350 Fall 2022 - Homework 2 - Code Solution   */                            
/* Author: Renato Mancuso (BU)                     */  
/*                                                 */
/* Description: This class implements the logic of */  
/*   a simulator where a single source of events   */  
/*   is connected to a single exit point, with a   */                                   
/*   single-processor server in the middle.        */                               
/*                                                 */                            
/***************************************************/                                  
                             
public class Simulator {                      
                 
    /* These are the resources that we intend to monitor */                            
    private LinkedList<EventGenerator> resources = new LinkedList<EventGenerator>();                            
  
    /* Timeline of events */
    private Timeline timeline = new Timeline();  
  
    /* Simulation time */                                   
    private Double now;                               
                            
    public void addMonitoredResource (EventGenerator resource) {                                  
	this.resources.add(resource);                             
    }                      
                 
    /* This method creates a new monitor in the simulator. To collect                            
     * all the necessary statistics, we need at least one monitor. */                            
    private void addMonitor() {  
	/* Scan the list of resources to understand the granularity of
	 * time scale to use */  
	Double monRate = Double.POSITIVE_INFINITY;  
                                   
	for (int i = 0; i < resources.size(); ++i) {                   
	    Double rate = resources.get(i).getRate();                    
	    if (monRate > rate) {                                  
			monRate = rate;                             
	    }                      
	}                 
                            
	/* If this fails, something is wrong with the way the                            
	 * resources have been instantiated */  
	assert !monRate.equals(Double.POSITIVE_INFINITY);
  
	/* Create a new monitor for this simulation */  
	Monitor monitor = new Monitor(timeline, monRate, resources);                                   
                               
    }                            
                                  
    public void simulate (Double simTime) {                             
                      
	/* Rewind time */                 
	now = 0.0;                            
                            
	/* Add monitor to the system */  
	addMonitor();
  
	/* Main simulation loop */  
	while(now < simTime) {                                   
	    /* Fetch event from timeline */                               
	    Event evt = timeline.popEvent();                            
                                  
	    /* Fast-forward time */                             
	    now = evt.getTimestamp();                      
                 
	    /* Extract block responsible for this event */                            
	    EventGenerator block = evt.getSource();                            
  
	    /* Handle event */
	    block.processEvent(evt);  
  
	}                                   
                               
	/* Print all the statistics */                            
	for (int i = 0; i < resources.size(); ++i) {                                  
	    resources.get(i).printStats(now);                             
	}                      
    }                 
                            
    /* Entry point for the entire simulation  */                            
    public static void main (String[] args) {  

	/* Parse the input parameters */  
	double simTime = Double.valueOf(args[0]);  
	double lambdaX = Double.valueOf(args[1]);                                   
	double lambdaY = Double.valueOf(args[2]);                               
	double servTimeX = Double.valueOf(args[3]);                            
	double servTimeY = Double.valueOf(args[4]);                                  
	String policy = args[5]; 
	Double pout = Double.valueOf(args[6]);                      
                      
	/* Create a new simulator instance */                 
	Simulator sim = new Simulator();                            
                            
	/* Create the traffic source */  
	//LoadGenerator trafficSource = new LoadGenerator(sim.timeline, lambda);
	LoadGenerator trafficSource = new LoadGenerator(sim.timeline, lambdaX, lambdaY);
  
	/* Create a new traffic sink */  
	LoadSink trafficSink = new LoadSink(sim.timeline);                                   
                               
	EventGenerator server = null;                            
	if (policy.equals("FIFO")) {                                  
	    /* Create new FIFO single-cpu processing server */                             
	    server = new FIFOServer(sim.timeline,servTimeX, servTimeY);
		System.out.println("HIT CODE");
		//server.setServTimeY(servTimeY);
		//server = new FIFOServer(sim.timeline, servTime, p_out);                     
	} else if (policy.equals("SJN")) {                 
	    /* Create new SJN single-cpu processing server */                            
	    server = new SJNServer(sim.timeline, servTimeY);                            
	}

	server.setServTimeX(servTimeX);
	/* Establish routing */  
	trafficSource.routeTo(server);                                   
	server.routeTo(trafficSink);                               
                            
	/* Add resources to be monitored */                                  
	sim.addMonitoredResource(server);                             
                      
	/* Kick off simulation */                 
	sim.simulate(simTime);                            
    }                            
  
}
  
/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */  
                                   
                               
                            
                                  
                             
                      
                 
                            
                            
  

  
  
                                   
                               
                            
