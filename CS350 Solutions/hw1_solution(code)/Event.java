package hw1;
import java.lang.*;

/***************************************************/
/* CS-350 Fall 2020 - Homework 1 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a single event that can be sorted in time.    */
/*   Each event is uniquely identified via an ID   */
/*   and a type. Each event also has a timestamp.  */
/*                                                 */
/***************************************************/

public class Event implements Comparable<Event> {

    public String type;
    public Double ts;
    public int id;
    public Double lambda;

    /* Verbose constructor  */
    public Event(String type, int id, Double ts, Double lambda) {
        super();
        this.type = type;
        this.id = id;
        this.ts = ts;
	this.lambda = lambda;
    }

    /* Generate next event given a previous event of the same type */
    public Event(Event evt) {
        super();
	this.type = evt.type;
	this.id = evt.id + 1;
	this.lambda = evt.lambda;
	this.ts = evt.ts + Exp.getExp(evt.lambda);
    }
    
    @Override
    public int compareTo(Event evt) {
        return this.getTimestamp().compareTo(evt.getTimestamp());
    }

    /* timestamp getter */
    public Double getTimestamp() {
	return this.ts;
    }

    /* type getter */
    public String getType() {
	return this.type;
    }

    /* ID getter */
    public int getId() {
	return this.id;
    }

    @Override
    public String toString() {
        return type + id + ": " + ts;
    }
}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
