package hw2;
import java.util.*;

/***************************************************/
/* CS-350 Fall 2020 - Homework 2 - Code Solution   */
/* Author: Renato Mancuso (BU)                     */
/*                                                 */
/* Description: This class implements the logic of */
/*   a single request flowing through multiple     */
/*   system resources. It keeps a set of           */
/*   statistics for each of traversed resource.    */
/*                                                 */
/***************************************************/

class Request {
    private EventGenerator _at;
    private int id;
    private static int unique_ID = 0;
    private HashMap<EventGenerator, Stats> stats = new HashMap<EventGenerator, Stats>();
    
    public Request (EventGenerator created_at) {
	this._at = created_at;
	this.id = unique_ID;
	unique_ID++;

	this.stats.put(this._at, new Stats());
    }

    public void moveTo(EventGenerator at) {
	this._at = at;
	this.stats.put(this._at, new Stats());
    }

    public EventGenerator where() {
	return this._at;
    }

    @Override
    public String toString() {
        return "R" + this.id;
    }
    
    public void recordArrival(Double ts) {
	Stats curStats = this.stats.get(this._at);
	curStats.arrival = ts;
    }

    public void recordServiceStart(Double ts) {
	Stats curStats = this.stats.get(this._at);
	curStats.serviceStart = ts;
    }

    public void recordDeparture(Double ts) {
	Stats curStats = this.stats.get(this._at);
	curStats.departure = ts;
    }

    public Double getArrival() {
	Stats curStats = this.stats.get(this._at);
	return curStats.arrival;
    }

    public Double getServiceStart() {
	Stats curStats = this.stats.get(this._at);
	return curStats.serviceStart;
    }

    public Double getDeparture() {
	Stats curStats = this.stats.get(this._at);
	return curStats.departure;
    }

}

/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */
