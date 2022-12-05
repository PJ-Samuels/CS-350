import java.util.*;

public class UnHashWorker extends Thread {
    private Float timeout;
    private LinkedList<String> hashes_unhashed;
    private LinkedList<String> unsolved_hashes = new LinkedList<>();
    private LinkedList<Integer> solved_hashes = new LinkedList<>();
    private LinkedList<Integer> completed_hashes = new LinkedList<>();
    private LinkedList<Integer> current_hints = new LinkedList<>();
    private List<String> timeout_hashes = new ArrayList<>();
    private List<Integer> current_solved_hashes;
    public int fin = 0;
    public int solved = 0;
    private int global_counter;

    public UnHashWorker(String str, Float time, int i) {
        super(str);
        this.timeout = time;
        this.global_counter = i;
    }

    public void hash_to_unhash(LinkedList<String> hash_unhash) {
        this.hashes_unhashed = hash_unhash;
    }

    public LinkedList<Integer> get_solved_hashes() {
        return this.solved_hashes;
    }

    public List<String> get_timeout_hashes() {
        return this.timeout_hashes;
    }

    public LinkedList<String> get_unsolved_hashes() {
        return this.unsolved_hashes;
    }

    public LinkedList<Integer> get_curr_hints() {
        return this.current_hints;
    }

    public void set_completed_hashes(LinkedList<Integer> solved) {
        this.completed_hashes = solved;
    }

    public void set_curr_solved_hashes(List<Integer> curr) {
        this.current_solved_hashes = curr;
    }

    public void set_timeout_hashes(List<String> timedout) {
        this.timeout_hashes = timedout;
    }

    public void get_total_Hint(String timeoutHash) {
        Hash hasher = new Hash();

        for (int num : this.completed_hashes) {
            if (this.current_solved_hashes.contains(num)) {
            } 
            else {
                for (int num1 : this.completed_hashes) {
                    if (this.current_solved_hashes.contains(num1) || num1 <= num) {}
                    else {
                        for (int k = num + 1; k < num1; k++) {
                            String total_hint = num + ";" + k + ";" + num1;
                            String hash_hint = hasher.hash(total_hint);
                            if (hash_hint.equals(timeoutHash)) {
                                this.current_hints.add(k);
                                this.current_solved_hashes.add(num);
                                this.current_solved_hashes.add(num1);
                                return;
                            }
                        }
                    }
                }
            }
        }
        this.unsolved_hashes.add(timeoutHash);
    }

    @Override
    public void run() {
        if (this.global_counter == 1) {
            UnHash unhash = new UnHash();
            for (String hash : hashes_unhashed) {
                try{
                    this.solved_hashes.add(unhash.unhash(hash, this.timeout));
                } catch (Exception e) {
                    this.timeout_hashes.add(hash);
                }
            }
            this.fin = 1;
            this.global_counter = 2;
        } else if (this.global_counter == 2) {
            for (String timeoutHash : this.timeout_hashes) {
                this.get_total_Hint(timeoutHash);
            }
            this.fin = 1;
        }
    }
}

