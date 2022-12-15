package hw1;
import java.lang.*;
import java.util.*; 

/***************************************************/
/* CS-350 Fall 2020 - Homework 1 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a chronologically sorted list of events.      */
/*   To test this code, provide in input two rates */
/*   for events of type A and B, respectively; and */
/*   the total length of the desired simulation    */
/*   time.                                         */
/*                                                 */
/***************************************************/

public class Timeline {

    private static PriorityQueue<Event> timeline;
    
    public static void main(String[] args) {
    
	double lambda_a = Double.valueOf(args[0]);
	double lambda_b = Double.valueOf(args[1]);
	double simtime = Double.valueOf(args[2]);
	timeline = new PriorityQueue<>();
    
	double now = 0;
	timeline.add(new Event("A", 0, now, lambda_a));
	timeline.add(new Event("B", 0, now, lambda_b));
    
	while (now < simtime) {
	    /* Get oldest event */
	    Event cur = timeline.poll();

	    /* Advance time */
	    now = cur.getTimestamp();
	
	    /* Print out the event */
	    System.out.println(cur.toString());

	    /* Generate next event of the same type */
	    timeline.add(new Event(cur));
	}

    }
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
