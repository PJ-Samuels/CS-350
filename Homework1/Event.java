public class Event implements Comparable<Event> {
    public String type;
    public int ID;
    public Double timestamp;
    public double lambda;
    
    public Event(String type, int ID,  Double timestamp,double lambda)
    {
        this.type = type;
        this.timestamp = timestamp;
        this.ID = ID;
        this.lambda = lambda;
    }
    public Double getTimestamp(){
        return this.timestamp;
    }
    @Override
    public int compareTo(Event other){
        return this.getTimestamp().compareTo(other.getTimestamp());
    }
}

