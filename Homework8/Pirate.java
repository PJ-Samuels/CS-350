import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Pirate {
    private Dispatcher dispatch;
    private LinkedList<Integer> completed_hashes = new LinkedList<>();
    private LinkedList<Integer> hints = new LinkedList<>();
    private LinkedList<List<String>> hash_timeouts = new LinkedList<>();
    private UnHashWorker[] hash_workers;
    private int N;
    private String pirate_island;

    Pirate(int n, String str) {
        this.N = n;
        this.pirate_island = str;
    }
    public void initDispatcher(UnHashWorker[] hashwork) {
        this.dispatch = new Dispatcher(this.N, hashwork);
    }
    public Dispatcher getDispatcher() {
        return this.dispatch;
    }
    public void setWorkers(UnHashWorker[] t) {
        this.hash_workers = t;
    }

    public void findTreasure(LinkedList<String> h) {
        this.dispatch.generateHashes(h);
        this.dispatch.dispatch();
        this.setWorkers(this.dispatch.getWorkers());

        int counter = 0;
        boolean t = true;
        while (t) {
            for (UnHashWorker worker : this.hash_workers) {
                if (worker.fin == 1 && worker.solved == 0) {
                    LinkedList<Integer> temp_sol = worker.get_solved_hashes();
                    List<String> timeout = worker.get_timeout_hashes();
                    this.hash_timeouts.add(timeout);
                    this.completed_hashes.addAll(temp_sol);
                    worker.solved = 1;
                    counter++;
                }
            }
            if (counter == this.N) {
                break;
            }
        }
        List<Integer> viewed = new ArrayList<>();

        for (int cracked : this.completed_hashes) {
            this.hints.add(cracked);
        }
        int global_counter = 2;
        this.hash_workers = new UnHashWorker[this.N];
        for (int i = 0; i < this.N; i++) {
            hash_workers[i] = new UnHashWorker("Thread " + i, this.dispatch.timeout, 2);
            hash_workers[i].set_timeout_hashes(this.hash_timeouts.get(i));
            hash_workers[i].set_completed_hashes(completed_hashes);
            hash_workers[i].set_curr_solved_hashes(viewed);
            hash_workers[i].start();
        }

        while (t) {
            LinkedList<Integer> hintsp2 = new LinkedList<>();
            LinkedList<LinkedList<String>> unsolved_hash_pt2 = new LinkedList<>();
            int count = 0;
            while (count != N) {
                for (UnHashWorker worker : this.hash_workers) {
                    if((worker).fin == 1 && worker.solved == 0) {
                        worker.solved = 1;
                        count+=1;
                        hintsp2.addAll(worker.get_curr_hints());
                        unsolved_hash_pt2.add(worker.get_unsolved_hashes());
                    }
                }
            }
            this.hints.addAll(hintsp2);
            int temp_count = 0;
            for (LinkedList list : unsolved_hash_pt2) {
                if (list.isEmpty() == false) {
                    temp_count+=1;
                }
            }
            if (temp_count == 0) {
                break;
            }
            this.hash_workers = new UnHashWorker[this.N];
            int l = this.N;
            int i = 0;
            while(i < l){
                hash_workers[i] = new UnHashWorker("Thread " + i, this.dispatch.timeout, 2);
                hash_workers[i].set_timeout_hashes(unsolved_hash_pt2.get(i));
                hash_workers[i].set_completed_hashes(hintsp2);
                hash_workers[i].set_curr_solved_hashes(viewed);
                hash_workers[i].start();
                i+=1;

            }
            global_counter++;
        }

        Comparator<Integer> sorter = Integer::compare;
        this.hints.sort(sorter);

        String str = "";

        for (int hint_num : this.hints) {
            str += pirate_island.charAt(hint_num);
        }

        System.out.println(str);
    }

    public static void main(String[] args) {
        try {
            String hashPath = args[0];
            int N = Integer.parseInt(args[1]);
            String path = args[3];
            Float timeout = null;

            Path cipher = Path.of(path);
            String isle = Files.readString(cipher);
            UnHashWorker[] hash_workers = new UnHashWorker[N];
            Pirate pirate = new Pirate(N, isle);
            pirate.initDispatcher(hash_workers);
            Dispatcher dispatch = pirate.getDispatcher();


            if (args.length >= 3) {
                timeout = Float.parseFloat(args[2]);
                dispatch.setTimeout(timeout);
            }

            int i = 0;
            while(i < N){
                hash_workers[i] = new UnHashWorker("Thread " + i, timeout, 1);
                i+=1;
            }
            File file = new File(hashPath);
            Scanner scan = new Scanner(file);
            LinkedList<String> hashes = new LinkedList<>();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                hashes.add(line);
            }
            scan.close();

            pirate.findTreasure(hashes);
        } 
        catch (Exception e) {}
    }

}