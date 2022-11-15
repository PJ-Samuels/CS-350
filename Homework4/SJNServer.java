package hw4;

import java.util.*;

/***************************************************/
/* CS-350 Fall 2022 - Homework 3 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a single-processor server with an infinite    */
/*   SJN request queue and exponent. distributed   */
/*   service times, i.e. a x/M/1-SJN server.       */
/*                                                 */
/***************************************************/

class SJNServer extends EventGenerator {

	private PriorityQueue<Request> theQueue = null;
	private HashMap<String, Double> classServTime;
	private String name = null;

	/* Statistics of this server --- to construct rolling averages */
	private Double cumulQ = new Double(0);
	private Double cumulW = new Double(0);
	private Double busyTime = new Double(0);
	private int snapCount = 0;

	/* Construct a server with class-dependent service time */
	public SJNServer(Timeline timeline) {
		super(timeline);

		/* Create the custom comparator for the queue */
		Comparator<Request> reqLengthComparator = new Comparator<Request>() {
			@Override
			public int compare(Request r1, Request r2) {
				if (r1.getServiceTime() <= r2.getServiceTime())
					return -1;
				else
					return 1;
			}
		};

		/* Now initialize the service queue */
		theQueue = new PriorityQueue<Request>(reqLengthComparator);

		/* Initialize the average service time of this server */
		this.classServTime = new HashMap<String, Double>();
	}

	/* Construct a server with class-independent service time */
	public SJNServer(Timeline timeline, Double servTime) {
		super(timeline);

		/* Create the custom comparator for the queue */
		Comparator<Request> reqLengthComparator = new Comparator<Request>() {
			@Override
			public int compare(Request r1, Request r2) {
				if (r1.getServiceTime() <= r2.getServiceTime())
					return -1;
				else
					return 1;
			}
		};

		/* Now initialize the service queue */
		theQueue = new PriorityQueue<Request>(reqLengthComparator);

		/* Initialize the average service time of this server */
		this.classServTime = new HashMap<String, Double>();
		this.classServTime.put("R", servTime);
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
		Event nextEvent = new Event(EventType.DEATH, curRequest,
				evt.getTimestamp() + curRequest.getServiceTime(), this);

		curRequest.recordServiceStart(evt.getTimestamp());

		/* Print the occurrence of this event */
		System.out.println(curRequest + " START: " + evt.getTimestamp());

		super.timeline.addEvent(nextEvent);
	}

	@Override
	void receiveRequest(Event evt) {
		super.receiveRequest(evt);

		Request curRequest = evt.getRequest();

		/* Generate length that is dependent on request class */
		Double classTs = this.classServTime.get(curRequest.getReqClass());
		assert (classTs != null && classTs != 0);
		curRequest.setServiceTime(Exp.getExp(1 / classTs));

		/* Print the occurrence of this event */
		System.out.println(evt.getRequest() + " ARR: " + evt.getTimestamp()
				+ " LEN: " + curRequest.getServiceTime());

		curRequest.recordArrival(evt.getTimestamp());

		/*
		 * Upon receiving the request, check the queue size and act
		 * accordingly
		 */
		if (theQueue.isEmpty()) {
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
		assert (removedOkay);

		curRequest.recordDeparture(evt.getTimestamp());

		/* Update busyTime */
		busyTime += curRequest.getDeparture() - curRequest.getServiceStart();

		assert super.next != null;
		super.next.receiveRequest(evt);

		/* Any new request to put into service? */
		if (!theQueue.isEmpty()) {
			Request nextRequest = theQueue.peek();
			if (theQueue.size() > 1 || nextRequest != curRequest) {
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
		return 1 / Collections.min(this.classServTime.values());
	}

	@Override
	void executeSnapshot() {
		snapCount++;
		cumulQ += theQueue.size();
		cumulW += Math.max(theQueue.size() - 1, 0);
	}

	@Override
	void printStats(Double time) {
		System.out.println("UTIL: " + busyTime / time);
		System.out.println("QAVG: " + cumulQ / snapCount);
		System.out.println("WAVG: " + cumulW / snapCount);
	}

	@Override
	public String toString() {
		return (this.name != null ? this.name : "");
	}

}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
