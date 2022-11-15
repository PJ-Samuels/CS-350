package hw4;

import java.util.*;

/***************************************************/
/* CS-350 Fall 2022 - Homework 3 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a single-entry multiple-exits routing node    */
/*                                                 */
/***************************************************/

class RoutingNode extends EventGenerator {

	private HashMap<EventGenerator, Double> routingTable = new HashMap<EventGenerator, Double>();
	private HashMap<EventGenerator, Double> routingTableX = new HashMap<EventGenerator, Double>();
	private HashMap<EventGenerator, Double> routingTableY = new HashMap<EventGenerator, Double>();
	private boolean route;

	public RoutingNode(Timeline timeline, boolean temp) {
		super(timeline);

		this.route = temp;
		if(this.route == true){
			this.routingTableX = null;
			this.routingTableY = null;
		} 
		else
			this.routingTable = null;

	}

	@Override
	public void routeTo(EventGenerator next) {
		routeTo(next, 0.0);
	}

	public void routeTo(EventGenerator next, Double probability_of_X, Double probability_of_Y) {
		/*
		 * Always assume that the same destination does not exist
		 * twice in the routing table
		 */
		assert !routingTable.containsKey(next);

		/* Add destination to routing table */

		routingTableX.put(next, probability_of_X);
		routingTableY.put(next, probability_of_Y);

		/*
		 * Perform a sanity check that the total probability has not
		 * exceeded 1
		 */
		Double totalX = new Double(0);

		for (Map.Entry<EventGenerator, Double> entry : routingTableX.entrySet()) {
			totalX += entry.getValue();
		}

		assert totalX <= 1;

		Double totalY = new Double(0);

		for (Map.Entry<EventGenerator, Double> entry : routingTableY.entrySet()) {
			totalY += entry.getValue();
		}

		assert totalY <= 1;
	}

	public void routeTo(EventGenerator next, Double probability) {
		/*
		 * Always assume that the same destination does not exist
		 * twice in the routing table
		 */
		assert !routingTable.containsKey(next);
		/* Add destination to routing table */
		routingTable.put(next, probability);
		/*
		 * Perform a sanity check that the total probability has not
		 * exceeded 1
		 */
		Double totalP = new Double(0);

		for (Map.Entry<EventGenerator, Double> entry : routingTable.entrySet()) {
			totalP += entry.getValue();
		}

		assert totalP <= 1;
	}

	@Override
	void receiveRequest(Event evt) {
		Request curRequest = evt.getRequest();

		/* Find out where to route to with a rand roll */
		Double rand = Math.random();

		/* Identify the destination with CDF calculation */
		Double cumulP = new Double(0);

		EventGenerator nextHop = null;

		HashMap<EventGenerator, Double> currentRoutingTable;

		if (route == false) {
			if (evt.getRequest().getReqClass().equals("X")) 
				currentRoutingTable = routingTableX;
			else 
				currentRoutingTable = routingTableY;
		} 
		else
			currentRoutingTable = routingTable;

		for (Map.Entry<EventGenerator, Double> entry : currentRoutingTable.entrySet()) {
			cumulP += entry.getValue();

			if (rand < cumulP) {
				nextHop = entry.getKey();
				break;
			}
		}

		assert nextHop != null;

		nextHop.receiveRequest(evt);
	}
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
