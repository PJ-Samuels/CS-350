public class Simulator{
    double lambda;
    double service_time;
    String policy;
    static double busy_time;
    static double queue_size;
    static double waiting_queue;
    static double montitor_evts;
    static double total_response_time;
    static double total_wait_time;
    static double total_requests;

    public Simulator(double lambda, double service_time, String policy){
        this.lambda = lambda;
        this.service_time = service_time;
        this.policy = policy;
    }
    
    public void simulate(double simtime){
        Timeline timeline = new Timeline();
        Double current_time = 0.0;
        Server serv = new Server(lambda, policy);
        EventGenerator eventGenerator = new EventGenerator(lambda, timeline, service_time);

        Event startBirth = new Event(EventType.BIRTH, current_time, 0);
        Event monitor = new Event(EventType.MONITOR, current_time, 0);
        timeline.addEvent(startBirth);
        timeline.addEvent(monitor);

        while(current_time < simtime){
            Event evt = timeline.popEvent();
            current_time = evt.getTimestamp();
            eventGenerator.releaseRequests(evt, serv);
        }

        double UTIL = busy_time/current_time;
        double QAVG = queue_size/montitor_evts;
        double WAVG = (queue_size-montitor_evts)/montitor_evts;
        double TRESP = total_response_time / total_requests;
        double TWAIT = (total_response_time-busy_time) / total_requests;

        System.out.println("UTIL: "+UTIL);
        System.out.println("QAVG: "+QAVG);
        System.out.println("WAVG: "+WAVG);
        System.out.println("TRESP: "+TRESP);
        System.out.println("TWAIT: "+TWAIT);
    }
    
    public static void main(String[] args){
        int simulation_length = Integer.parseInt(args[0]);
        double lambda = Double.parseDouble(args[1]);
        double service_time = Double.parseDouble(args[3]);
        String policy = args[5];
        Simulator simulation = new Simulator(lambda, service_time, policy);
        
        
        simulation.simulate(simulation_length);
    }

}