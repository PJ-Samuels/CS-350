package hw4;

import java.util.*;

/***************************************************/
/* CS-350 Fall 2022 - Homework 3 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a single-processor server with an infinite    */
/*   FIFO request queue and exponent. distributed  */
/*   service times, i.e. a x/M/1 server.           */
/*                                                 */
/***************************************************/

class FIFOServer extends EventGenerator {

	private LinkedList<Request> queue = new LinkedList<Request>();
	private HashMap<String, Double> classServTime;
	private String name = null;
	private Double limit_K = null;
	private int drop;

	/* Statistics of this server --- to construct rolling averages */
	private Double cumulQ = new Double(0);
	private Double cumulW = new Double(0);
	private Double busyTime = new Double(0);
	private int snapCount = 0;

	/* Construct a server with class-dependent service time */
	public FIFOServer(Timeline timeline) {
		super(timeline);
		/* Initialize the average service time of this server */
		this.classServTime = new HashMap<String, Double>();
	}

	/* Construct a server with class-independent service time */
	public FIFOServer(Timeline timeline, Double servTime, Double k) {
		super(timeline);

		/* Initialize the average service time of this server */
		this.classServTime = new HashMap<String, Double>();
		this.classServTime.put("R", servTime);
		this.limit_K = k;
	}

	/*
	 * Given a name to this server that will be used for the trace and
	 * statistics
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* Register parameters for per-class service time characteristics */
	public void registerReqClass(String req_class, Double Ts) {
		this.classServTime.put(req_class, Ts);
	}

	/*
	 * Internal method to be used to simulate the beginning of service
	 * for a queued/arrived request.
	 */
	private void __startService(Event evt, Request curRequest) {
		Event nextEvent = new Event(EventType.DEATH, curRequest,evt.getTimestamp() + curRequest.getServiceTime(), this);
		curRequest.recordServiceStart(evt.getTimestamp());
		/* Print the occurrence of this event */
		System.out.println(curRequest + " START " + this.name + ": " + evt.getTimestamp());

		super.timeline.addEvent(nextEvent);
	}
	//for loadsink accumulation
	public Double getCumulQ() {
		return this.cumulQ;
	}
	//for loadsink accumulation
	public Integer getSnapCount() {
		return this.snapCount;
	}

	@Override
	void receiveRequest(Event evt) {
		super.receiveRequest(evt);

		Request curRequest = evt.getRequest();

		/* Generate length that is dependent on request class */
		Double classTs = this.classServTime.get(curRequest.getReqClass());
		assert (classTs != null);
		curRequest.setServiceTime(Exp.getExp(1 / classTs));
		/* Print the occurrence of this event */
		System.out.println(evt.getRequest() + " ARR: " + evt.getTimestamp()+ " LEN: " + curRequest.getServiceTime());

		curRequest.recordArrival(evt.getTimestamp());

		/*
		 * Upon receiving the request, check the queue size and act
		 * accordingly
		 */
		if (limit_K == null) {
			if (queue.isEmpty())
				__startService(evt, curRequest);
			queue.add(curRequest);
		} 
		else{
			if (queue.size() <= limit_K){
				if (queue.isEmpty())
					__startService(evt, curRequest);
				queue.add(curRequest);
			}
			else{
				System.out.println(evt.getRequest() + " DROP " + this.name + ": " + evt.getTimestamp());
				this.drop += 1;
			}
		}
	}

	@Override
	void releaseRequest(Event evt) {
		/* What request we are talking about? */
		Request curRequest = evt.getRequest();

		/* Remove the request from the server queue */
		Request queueHead = queue.removeFirst();

		/* If the following is not true, something is wrong */
		assert curRequest == queueHead;

		curRequest.recordDeparture(evt.getTimestamp());

		/* Update busyTime */
		busyTime += curRequest.getDeparture() - curRequest.getServiceStart();

		assert super.next != null;
		super.next.receiveRequest(evt);

		/* Any new request to put into service? */
		if (!queue.isEmpty()) {
			Request nextRequest = queue.peekFirst();
			if (nextRequest != curRequest) {
				/*
				 * Only do this if we pick something else, otherwise
				 * the death has already been scheduled
				 */
				__startService(evt, nextRequest);
			}
		}
	}

	@Override
	Double getRate() {
		return 1 / Collections.min(classServTime.values());
	}

	@Override
	void executeSnapshot() {
		snapCount++;
		cumulQ += queue.size();
		cumulW += Math.max(queue.size() - 1, 0);
	}

	@Override
	void printStats(Double time) {
		System.out.println();
		System.out.println(this.name + " UTIL: " + busyTime / time);
		System.out.println(this.name + " QAVG: " + cumulQ / snapCount);
		System.out.println(this.name + " WAVG: " + cumulW / snapCount);
		if (limit_K != null) {
			System.out.println(this.name + " DROPPED: " + this.drop);
		}
	}

	@Override
	public String toString() {
		return (this.name != null ? this.name : "");
	}
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
