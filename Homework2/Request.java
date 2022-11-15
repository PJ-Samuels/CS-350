public class Request {
    int req_id;
    double arrival_time;
    double start_time;
    double duration;
    int count;

    //dont need to declare?
    public Request (double arr, double duration, int count){
        this.arrival_time = arr;
        this.duration = duration;
        this.count = count;
    }

    //public Request(){
        /*this.arrival_time = -1;
        this.start_time = -1;
        this.finish_time = -1;*/
    //}

    //declared but not in the Request object since they will be updated by the DEATH event in Event generator 
    public void setRequestArrivalTime (double t){
        this.arrival_time = t;
    }
    public void setRequestStartTime (double t){
        this.start_time = t;
    }

    //dont need anymore
    /*public void setRequestFinishTime (double t){
        this.finish_time = t;
    }*/

    public Double getRequestArrivalTime() {
        return this.arrival_time;
    }
    public Double getRequestStartTime() {
        return this.start_time;
    }
    //total time of request
    public Double getRequestDuration() {
        return this.duration;
    }
    public int getRequestCount(){
        return this.count;
    }
    public double getRequestFinishTime(){
        double total_time = start_time+duration;
        return total_time;
    }

}
