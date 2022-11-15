package hw4;

import java.util.*;

/***************************************************/
/* CS-350 Fall 2022 - Homework 3 - Code Solution   */
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
    private static HashMap<String, Integer> unique_class_IDs = new HashMap<String, Integer>();
    private HashMap<EventGenerator, Stats> stats = new HashMap<EventGenerator, Stats>();
    private Double length;
    private String req_class;

    public Request(EventGenerator created_at, String in_class) {
        Integer uniqueID = 0;
        this._at = created_at;
        this.req_class = in_class;

        if (unique_class_IDs.containsKey(this.req_class)) {
            uniqueID = unique_class_IDs.get(this.req_class);
            uniqueID += 1;
        }

        unique_class_IDs.put(this.req_class, uniqueID);
        this.id = uniqueID;
        this.stats.put(this._at, new Stats());
    }

    public void moveTo(EventGenerator at) {
        this._at = at;
        if (!this.stats.containsKey(this._at)) {
            this.stats.put(this._at, new Stats());
        } else {
            Stats curStats = this.stats.get(this._at);
            curStats.runs += 1;
        }
    }

    public EventGenerator where() {
        return this._at;
    }

    @Override
    public String toString() {
        return this.req_class + this.id;
    }

    public String getReqClass() {
        return this.req_class;
    }

    public HashMap<EventGenerator, Stats> getStats() {
        return this.stats;
    }

    public void setServiceTime(Double servTime) {
        this.length = servTime;
    }

    public Double getServiceTime() {
        return this.length;
    }

    public void recordArrival(Double ts) {
        Stats curStats = this.stats.get(this._at);
        curStats.arrival = ts;
    }

    public void recordServiceStart(Double ts) {
        Stats curStats = this.stats.get(this._at);
        curStats.serviceStart = ts;
        curStats.cumulTw += curStats.serviceStart - curStats.arrival;
    }

    public void recordDeparture(Double ts) {
        Stats curStats = this.stats.get(this._at);
        curStats.departure = ts;
        curStats.cumulTq += curStats.departure - curStats.arrival;
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
