package hw3;                                  
import java.util.*;                             
                      
/***************************************************/                 
/* CS-350 Fall 2022 - Homework 2 - Code Solution   */                            
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
    private String type;//X or Y but need to change                
    private static int unique_IDX = 0;   
    private static int unique_IDY = 0;                              
    private HashMap<EventGenerator, Stats> stats = new HashMap<EventGenerator, Stats>();                            
    private Double length;  

    public Request (EventGenerator created_at, String type) {  
        this._at = created_at;  
        this.type = type;  
        if(type.equals("X")){
            this.id = unique_IDX; 
            unique_IDX++;
        }
        else if(type.equals("Y")){
            this.id = unique_IDY; 
            unique_IDY++;
        }                                                                          
                            
	this.stats.put(this._at, new Stats());                                  
    }                             
                      
    public void moveTo(EventGenerator at) {                 
	this._at = at;                            
	this.stats.put(this._at, new Stats());                            
    }  

    public EventGenerator where() {  
	return this._at;  
    }                                   
                               
    @Override                            
    public String toString() {      
        return this.type+this.id;                      
        //return "X" + this.id;                          
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
    }
  
    public void recordDeparture(Double ts) {  
	Stats curStats = this.stats.get(this._at);                                   
	curStats.departure = ts;                               
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
    public String getType(){
        return this.type;
    }
}                 
                            
/* END -- Q1BSR1QgUmVuYXRvIE1hbmN1c28= */                            
  

  
  
                                   
                               
                            
                                  
                             
                      
                 
                            
                            
  

  
  
                                   
                               
                            
