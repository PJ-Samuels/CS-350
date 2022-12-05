package hw7;
public class WorkUnit {

    boolean simple;
    int lowerBound;
    int upperBound;
    String hash;
    String result;

    public WorkUnit (String hash) {
	this.simple = true;
	this.hash = hash;
	this.result = null;
    }

    public WorkUnit (String hash, int lower, int upper) {
	this.simple = false;
	this.hash = hash;
	this.lowerBound = lower;
	this.upperBound = upper;
	this.result = null;	
    }

    public boolean isSimple() {
	return simple;
    }

    public String getHash() {
	return hash;
    }

    public int getLowerBound() {
	if (simple)
	    return 0;
	else
	    return lowerBound;
    }

    public int getUpperBound() {
	if (simple)
	    return Integer.MAX_VALUE;
	else
	    return upperBound;
    }

    public void setResult(String result) {
	this.result = result;
    }

    public String getResult() {
	return result;
    }
    
    @Override
    public String toString() {
	if (this.result != null)
	    return this.result;
	else
	    return this.hash;
    }
    
}
