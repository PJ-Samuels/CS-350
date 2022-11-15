package hw4;

import java.util.*;

/***************************************************/
/* CS-350 Fall 2022 - Homework 3 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a traffic sink. Any request that arrives at   */
/*   the sink is effectively released from the     */
/*   system.                                       */
/*                                                 */
/***************************************************/

class LoadSink extends EventGenerator {

	private HashMap<String, Double> cumulTq = new HashMap<String, Double>();
	private HashMap<String, Double> cumulTw = new HashMap<String, Double>();
	private HashMap<String, Integer> servedReqs = new HashMap<String, Integer>();
	private FIFOServer server_S0 = null;
	private FIFOServer server_S1 = null;
	private FIFOServer server_S2 = null;
	private ArrayList<FIFOServer> list = new ArrayList<>();
	private int totServed = 0;
	private int totRuns = 0;

	public LoadSink(Timeline timeline, String reqClass) {
		super(timeline);

		if (reqClass == null) {
			this.cumulTq.put("R", new Double(0));
			this.cumulTw.put("R", new Double(0));
			this.servedReqs.put("R", new Integer(0));
		} else {
			this.cumulTq.put(reqClass, new Double(0));
			this.cumulTw.put(reqClass, new Double(0));
			this.servedReqs.put(reqClass, new Integer(0));
		}
	}

	public void registerReqClass(String reqClass) {
		this.cumulTq.put(reqClass, new Double(0));
		this.cumulTw.put(reqClass, new Double(0));
		this.servedReqs.put(reqClass, new Integer(0));
	}

	public void registerServer(FIFOServer resource, int num) {
		if(num == 0){
			this.server_S0 = resource;
			list.add(this.server_S0);
		}
		else if (num == 1){
			this.server_S1 = resource;
			list.add(this.server_S1);
		}
		else{
			this.server_S2 = resource;
			list.add(this.server_S2);
		}

	}


	@Override
	void receiveRequest(Event evt) {
		super.receiveRequest(evt);
		Request curReq = evt.getRequest();

		/* Print the occurrence of this event */
		System.out.println(curReq + " DONE: " + evt.getTimestamp());

		/* Retrieve per-class statistics at the monitored server */
		String reqClass = curReq.getReqClass();
		HashMap<EventGenerator, Stats> curStats = curReq.getStats();
		Double totalTq;
		Double totalTw;
		Integer totalRuns;
		//Arraylist for easier access/readability
		FIFOServer s0 = list.get(0);
		FIFOServer s1 = list.get(1);
		FIFOServer s2 = list.get(2);


		try {
			totalTq = curStats.get(s1).cumulTq + curStats.get(s0).cumulTq;
			totalTw = curStats.get(s1).cumulTw + curStats.get(s0).cumulTw;
			totalRuns = curStats.get(s1).runs + curStats.get(s0).runs;
		} catch (Exception e) {
			totalTq = curStats.get(s2).cumulTq + curStats.get(s0).cumulTq;
			totalTw = curStats.get(s2).cumulTw + curStats.get(s0).cumulTw;
			totalRuns = curStats.get(s2).runs + curStats.get(s0).runs;
		}

		/* Now aggregate per class */
		this.cumulTq.put(reqClass, this.cumulTq.get(reqClass) + totalTq);
		this.cumulTw.put(reqClass, this.cumulTw.get(reqClass) + totalTw);
		this.servedReqs.put(reqClass, this.servedReqs.get(reqClass) + 1);
		this.totServed += 1;
		this.totRuns += totalRuns;
	}

	@Override
	void printStats(Double time) {
		System.out.println();
		for (String className : this.servedReqs.keySet()) {
			int served = servedReqs.get(className);
			System.out.println("TRESP " + className + ": " + cumulTq.get(className) / served);
			System.out.println("TWAIT " + className + ": " + cumulTw.get(className) / served);
		}
		Double totalQ = (this.server_S0.getCumulQ() / this.server_S0.getSnapCount())+ (this.server_S1.getCumulQ() / this.server_S1.getSnapCount())+ (this.server_S2.getCumulQ() / this.server_S2.getSnapCount());
		System.out.println("QAVG: " + totalQ);

	}

}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
