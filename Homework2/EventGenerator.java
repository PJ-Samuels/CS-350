public class EventGenerator {
    protected Timeline timeline;
    protected EventGenerator nextEvent;
    public Double lambda;
    public Double serv_time;


    public EventGenerator(Double lambda, Timeline timeline, Double serv_time) {
        this.lambda = lambda;
        this.timeline = timeline;
        this.serv_time = serv_time;
    }
    public void releaseRequests(Event evt, Server serv){
        int evtType;
        if(evt.type == EventType.BIRTH){
            evtType =1;
        }
        else if(evt.type == EventType.DEATH){
            evtType = 2;
        }
        else{
            evtType = 3;
        }

        if(evtType == 1){
            double arr = evt.getTimestamp();
            double evt_service_time = Exp.getExp(1/this.serv_time);
            Request addReq = new Request(arr, evt_service_time, evt.getEventCount());
            serv.receiveRequest(addReq);
            Event new_evt = new Event(EventType.BIRTH, Exp.getExp(lambda)+evt.getTimestamp(), evt.getEventCount()+1);
            System.out.println("X"+addReq.getRequestCount()+" ARR: "+arr+ " LEN: "+evt_service_time);

            timeline.addEvent(new_evt);
            
            if(serv.getQueueSize() == 1){
                Request request = serv.startingRequest();
                request.setRequestStartTime(evt.getTimestamp());
                System.out.println("X"+request.getRequestCount()+" START: "+request.getRequestStartTime());
                Event newDeath = new Event(EventType.DEATH, evt.getTimestamp()+request.getRequestDuration(), evt.getEventCount()+1);
                timeline.addEvent(newDeath);
            }
        }

        else if(evtType == 2){
            Request request = serv.releaseRequest();
            System.out.println("X"+request.getRequestCount()+" DONE: "+evt.getTimestamp());
            serv.releaseRequest();
            Simulator.busy_time+=request.getRequestDuration();
            Simulator.total_requests+=1;
            Simulator.total_response_time += ((request.getRequestStartTime()+request.getRequestDuration()) - request.getRequestArrivalTime());
            

            if(!serv.real_request_queue.isEmpty()){
                Request new_request = serv.startingRequest();
                new_request.setRequestStartTime(request.getRequestFinishTime());
                System.out.println("X"+new_request.getRequestCount()+" START: "+evt.getTimestamp());
                Event newDeath = new Event(EventType.DEATH, evt.getTimestamp()+new_request.getRequestDuration(), evt.getEventCount()+1);
                timeline.addEvent(newDeath);
            }
        }
        else if(evtType == 3)
        {
            double Ts = Exp.getExp(lambda)+evt.getTimestamp();
            Event mon = new Event(EventType.MONITOR, Ts, evt.getEventCount()+1);
            timeline.addEvent(mon);
            if(serv.real_request_queue != null)
            {
                Simulator.queue_size += serv.real_request_queue.size();
                Simulator.montitor_evts += 1;
                Simulator.waiting_queue += serv.real_request_queue.size()-1;
            }
        }
    }
}
