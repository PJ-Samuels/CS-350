
import java.util.*;
public class Timeline{
    PriorityQueue<Event> pq;

    public Timeline(){
        pq = new PriorityQueue<Event>();
    }
    public void addToTimeline(Event evtToAdd)
    {
        pq.add(evtToAdd);
    }

    public Event popNext(){
        return pq.poll();
    }
}
