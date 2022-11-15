package hw4;

import java.lang.*;
import java.util.*;

/***************************************************/
/* CS-350 Fall 2022 - Homework 3 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a simulator where a single source of events   */
/*   is connected to a single exit point, with a   */
/*   single-processor server in the middle.        */
/*   As requests leave the server, they might      */
/*   request addl. time with a given probability   */
/*                                                 */
/***************************************************/

public class Simulator {

	/* These are the resources that we intend to monitor */
	private LinkedList<EventGenerator> resources_server0 = new LinkedList<EventGenerator>();
	private LinkedList<EventGenerator> resources_server1 = new LinkedList<EventGenerator>();
	private LinkedList<EventGenerator> resources_server2 = new LinkedList<EventGenerator>();

	/* Timeline of events */
	private Timeline timeline = new Timeline();

	/* Simulation time */
	private Double now;
	public void addMonitoredResource0(EventGenerator resource0){
		this.resources_server0.add(resource0);
	}
	public void addMonitoredResource1(EventGenerator resource1){
		this.resources_server1.add(resource1);
	}
	public void addMonitoredResource2(EventGenerator resource2){
		this.resources_server2.add(resource2);
	}

	/*
	 * This method creates a new monitor in the simulator. To collect
	 * all the necessary statistics, we need at least one monitor.
	 */
	private void addMonitor() {
		/*
		 * Scan the list of resources to understand the granularity of
		 * time scale to use
		 */
		Double monRate0 = Double.POSITIVE_INFINITY;
		for (int i = 0; i < resources_server0.size(); ++i) {
			Double rate = resources_server0.get(i).getRate();
			if (monRate0 > rate) {
				monRate0 = rate;
			}
		}

		Double monRate1 = Double.POSITIVE_INFINITY;
		for (int i = 0; i < resources_server1.size(); ++i) {
			Double rate = resources_server1.get(i).getRate();
			if (monRate1 > rate) {
				monRate1 = rate;
			}
		}

		Double monRate2 = Double.POSITIVE_INFINITY;
		for (int i = 0; i < resources_server2.size(); ++i) {
			Double rate = resources_server2.get(i).getRate();
			if (monRate2 > rate) {
				monRate2 = rate;
			}
		}

		assert !monRate0.equals(Double.POSITIVE_INFINITY);
		assert !monRate1.equals(Double.POSITIVE_INFINITY);
		assert !monRate2.equals(Double.POSITIVE_INFINITY);
		Monitor monitorServer0 = new Monitor(timeline, monRate0, resources_server0);
		Monitor monitorServer1 = new Monitor(timeline, monRate1, resources_server1);
		Monitor monitorServer2 = new Monitor(timeline, monRate2, resources_server2);

	}

	public void simulate(Double simTime) {
		/* Rewind time */
		now = 0.0;
		/* Add monitor to the system */
		addMonitor();
		/* Main simulation loop */
		while (now < simTime) {
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
		for (int i = 0; i < resources_server0.size(); ++i) {
			resources_server0.get(i).printStats(now);
		}
		for (int i = 0; i < resources_server1.size(); ++i) {
			resources_server1.get(i).printStats(now);
		}
		for (int i = 0; i < resources_server2.size(); ++i) {
			resources_server2.get(i).printStats(now);
		}
	}

	/* Entry point for the entire simulation */
	public static void main(String[] args) {

		/* Parse the input parameters */
		double simTime = Double.valueOf(args[0]);
		double lambda_x = Double.valueOf(args[1]);
		double lambda_y = Double.valueOf(args[2]);
		double servTime_XServ0 = Double.valueOf(args[3]);
		double servTime_yServ0 = Double.valueOf(args[4]);
		String policy = args[5];
		double prob_XS1 = Double.parseDouble(args[6]);
		double prob_YS1 = Double.parseDouble(args[7]);
		double servTime_Serv1 = Double.parseDouble(args[8]);
		double servTime_Serv2 = Double.parseDouble(args[9]);
		double limit_K = Double.parseDouble(args[10]);
		double prob_Return_S1 = Double.parseDouble(args[11]);
		double prob_Return_S2 = Double.parseDouble(args[12]);

		/* Create a new simulator instance */
		Simulator sim = new Simulator();

		/* Create the traffic sources */
		LoadGenerator trafficSourceX = new LoadGenerator(sim.timeline, lambda_x, "X");
		LoadGenerator trafficSourceY = new LoadGenerator(sim.timeline, lambda_y, "Y");

		/* Create a new traffic sink */
		LoadSink trafficSink = new LoadSink(sim.timeline, "X");
		trafficSink.registerReqClass("Y");

		FIFOServer server0 = null;
		EventGenerator serverSJN;
		FIFOServer server1 = null;
		FIFOServer server2 = null;
		if (policy.equals("FIFO")) {
			/* Create new FIFO single-cpu processing server */
			FIFOServer fifoServer = new FIFOServer(sim.timeline);
			fifoServer.registerReqClass("X", servTime_XServ0);
			fifoServer.registerReqClass("Y", servTime_yServ0);
			fifoServer.setName("S0");
			server0 = fifoServer;
			FIFOServer fifoServer1 = new FIFOServer(sim.timeline, servTime_Serv1, null);
			fifoServer1.registerReqClass("X", servTime_Serv1);
			fifoServer1.registerReqClass("Y", servTime_Serv1);
			fifoServer1.setName("S1");
			server1 = fifoServer1;
			Double lim;
			if (limit_K == 0) {
				lim = null;
			} else {
				lim = limit_K;
			}
			FIFOServer fifoServer2 = new FIFOServer(sim.timeline, servTime_Serv2, lim);
			fifoServer2.registerReqClass("X", servTime_Serv2);
			fifoServer2.registerReqClass("Y", servTime_Serv2);
			fifoServer2.setName("S2");
			server2 = fifoServer2;
		} else if (policy.equals("SJN")) {
			/* Create new SJN single-cpu processing server */
			SJNServer sjnServer = new SJNServer(sim.timeline);
			sjnServer.registerReqClass("X", servTime_XServ0);
			sjnServer.registerReqClass("Y", servTime_yServ0);
			sjnServer.setName(policy);
			serverSJN = sjnServer;
			FIFOServer fifoServer1 = new FIFOServer(sim.timeline, servTime_Serv1, null);
			fifoServer1.registerReqClass("X", servTime_Serv1);
			fifoServer1.registerReqClass("Y", servTime_Serv1);
			fifoServer1.setName("S1");
			server1 = fifoServer1;
			Double lim;
			if (limit_K == 0) {
				lim = null;
			} else {
				lim = limit_K;
			}
			FIFOServer fifoServer2 = new FIFOServer(sim.timeline, servTime_Serv2, lim);
			fifoServer2.registerReqClass("X", servTime_Serv2);
			fifoServer2.registerReqClass("Y", servTime_Serv2);
			fifoServer2.setName("S2");
			server2 = fifoServer2;
		}

		/* Make sure that the sink tracks request timing at the server */
		trafficSink.registerServer(server0, 0);
		trafficSink.registerServer(server1, 1);
		trafficSink.registerServer(server2, 2);

		/* Create split point at the exit of the server */
		RoutingNode splitPoint0 = new RoutingNode(sim.timeline, false);
		RoutingNode splitPoint1 = new RoutingNode(sim.timeline, true);
		RoutingNode splitPoint2 = new RoutingNode(sim.timeline, true);

		/* Establish routing */
		trafficSourceX.routeTo(server0);
		trafficSourceY.routeTo(server0);
		server0.routeTo(splitPoint0);
		splitPoint0.routeTo(server1, prob_XS1, prob_YS1);
		splitPoint0.routeTo(server2, 1 - prob_XS1, 1 - prob_YS1);
		server1.routeTo(splitPoint1);
		splitPoint1.routeTo(trafficSink, 1 - prob_Return_S1);
		splitPoint1.routeTo(server0, prob_Return_S1);
		server2.routeTo(splitPoint2);
		splitPoint2.routeTo(trafficSink, 1 - prob_Return_S2);
		splitPoint2.routeTo(server0, prob_Return_S2);
		
		///monitoring resorues for seperate servers
		sim.addMonitoredResource0(server0);
		sim.addMonitoredResource0(splitPoint0);
		sim.addMonitoredResource1(server1);
		sim.addMonitoredResource1(splitPoint1);
		sim.addMonitoredResource2(server2);
		sim.addMonitoredResource2(trafficSink);
		sim.addMonitoredResource2(splitPoint2);

		/* Kick off simulation */
		sim.simulate(simTime);
	}

}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
