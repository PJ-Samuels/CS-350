import java.util.*;
public class Server {
    //Timeline timeline;
    LinkedList<Request> real_request_queue = new LinkedList<>();
    //Double service_time;
    String policy;
    Double lambda;
    Request current_request;
    public Server(Double lambda, String policy)
    {
        this.lambda = lambda;
        this.policy = policy;
        //this.service_time = service_time;
    }
    public Request releaseRequest(){
        real_request_queue.remove(current_request);
        Request val_holder = current_request;
        current_request = null;
        return val_holder;

    }
    void receiveRequest(Request rq){
        this.real_request_queue.add(rq);
        
    }
    Request startingRequest()
    {
        String option1 = "FIFO";
        String option2 = "SJN";
        if(policy.equals(option1)){
            current_request = real_request_queue.get(0);
        }
        else if(policy.equals(option2)){
            Request minimum_request = real_request_queue.get(0);
            for(int i = 0; i < real_request_queue.size(); i++) {
                if (real_request_queue.get(i).getRequestDuration() < minimum_request.getRequestDuration()){
                    minimum_request = real_request_queue.get(i);
                }
            }
            current_request = minimum_request;
        }
        return current_request;
    }
    
    public int getQueueSize(){
        return real_request_queue.size();
    }
}
