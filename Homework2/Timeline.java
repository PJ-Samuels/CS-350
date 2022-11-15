import java.util.LinkedList;
public class Timeline {
    private LinkedList<Event> timeline = new LinkedList<Event>();
    public void addEvent(Event evt){
        for(int i = 0; i < timeline.size(); i++)
        {
            double temp1 = evt.getTimestamp();
            double temp2 = timeline.get(i).getTimestamp();
            //for SJN v FIFO differentiation
            if(temp1 < temp2){
                //System.out.println("Added "+evtToAdd.getID());
                timeline.add(i, evt);
                return;
            }
        }
        timeline.add(evt);
    }

    public Event popEvent(){
        return timeline.removeFirst();
    }
}
