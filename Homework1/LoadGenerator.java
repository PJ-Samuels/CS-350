public class LoadGenerator{
    static Timeline time = new Timeline();
    public static void releaseRequest(Event evt){
        //evt.printEvent();
        Event newEvent = new Event(evt.type, evt.ID+1, evt.timestamp+ Exp.getExp(evt.lambda), evt.lambda);
        time.addToTimeline(newEvent);

    }

     public static void main( String[] args){
        double lambdaA = Double.valueOf(args[0]);
        double lambdaB = Double.valueOf(args[1]);
        double simTime = Double.valueOf(args[2]);

        double curtime = 0.0;

        Event event1 = new Event("A", 0, curtime, lambdaA);
        Event event2 = new Event("B", 0, curtime, lambdaB);


        time.addToTimeline(event1);
        time.addToTimeline(event2);

    
        while(curtime < simTime)
        {
            Event top = time.popNext();
            System.out.println(top.type+top.ID+ ": "+top.timestamp);
            curtime = top.getTimestamp();
            releaseRequest(top);
        } 

    }
}