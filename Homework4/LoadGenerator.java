package hw4;

import java.util.*;

/***************************************************/
/* CS-350 Fall 2022 - Homework 3 - Code Solution   */
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
	private String req_class;

	/* Construct a traffic source with a generic class */
	public LoadGenerator(Timeline timeline, Double lambda) {
		super(timeline);
		this.rate = lambda;
		this.req_class = "R";

		/* Insert the very first event into the timeline at time 0 */
		Request firstRequest = new Request(this, this.req_class);
		Event firstEvent = new Event(EventType.BIRTH, firstRequest, new Double(0), this);

		super.timeline.addEvent(firstEvent);
	}

	/* Construct a traffic source with a specific class */
	public LoadGenerator(Timeline timeline, Double lambda, String in_class) {
		super(timeline);
		this.rate = lambda;
		this.req_class = in_class;

		/* Insert the very first event into the timeline at time 0 */
		Request firstRequest = new Request(this, this.req_class);
		Event firstEvent = new Event(EventType.BIRTH, firstRequest, new Double(0), this);

		super.timeline.addEvent(firstEvent);
	}

	@Override
	void releaseRequest(Event evt) {
		Request curRequest = evt.getRequest();
		curRequest.recordArrival(evt.getTimestamp());

		/*
		 * When it's time to process as new arrival, generate the next
		 * arrival and request, and hand-off the current request to
		 * the next hop
		 */
		Request nextReq = new Request(this, this.req_class);
		Event nextEvent = new Event(EventType.BIRTH, nextReq,
				evt.getTimestamp() + Exp.getExp(this.rate), this);

		super.timeline.addEvent(nextEvent);

		assert super.next != null;
		super.next.receiveRequest(evt);
	}

	/*
	 * This is a special case. Technically, a request leaving a generator is a DEATH
	 * event at the generator. But for simplicity we can keep it as a BIRTH at the
	 * next element downstream.
	 */
	@Override
	void receiveRequest(Event evt) {
		releaseRequest(evt);
	}

	@Override
	Double getRate() {
		return this.rate;
	}
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
