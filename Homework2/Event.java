public class Event implements Comparable<Event> {
    public EventType type;
    public Double timestamp;
    //public Request rq;
    public int count;
    //with start request?
    public Event(EventType type,  Double timestamp, int count )
    {
        this.type = type;
        this.timestamp = timestamp;
        //this.rq = request;
        this.count = count;
    }

    //without start request
    /*public Event(Event evt, Double lambda) {
        super();
	    this.type = evt.type;
	    this.timestamp = evt.timestamp + Exp.getExp(1/lambda);
	    //this.rq = evt.rq;
        this.count = count;
    }*/


    public Double getTimestamp(){
        return this.timestamp;
    }
    public EventType getType() {
        return this.type;
    }
    /*public Request getRequest() {
        return this.rq;
    }*/
    public int getEventCount(){
        return this.count;
    }

    String toID(){
        String id = ""+type+count;
        return id;
    }
    @Override
    public int compareTo(Event other){
        return this.getTimestamp().compareTo(other.getTimestamp());
    }
}

