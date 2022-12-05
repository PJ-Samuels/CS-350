import java.io.*;
import java.util.*;

public class Dispatcher {
    private int N;
    private LinkedList<LinkedList<String>> hashes = new LinkedList<>();
    public Float timeout = null;
    private UnHashWorker[] worker;

    Dispatcher(int n, UnHashWorker[] t) {
        for (int i = 0; i < n; i++) {
            hashes.add(new LinkedList<String>());
        }
        this.N = n;
        this.worker = t;
    }

    public UnHashWorker[] getWorkers() {
        return this.worker;
    }

    public void generateHashes(LinkedList<String> str) {
        int num = 0;
        while (str.isEmpty() == false) {
            if (num == this.N) {
                num = 0;
            }
            hashes.get(num).add(str.removeFirst());
            num++;
        }
    }

    public void setTimeout(float f) {
        this.timeout = f;
    }

    public void dispatch() {
        for (int i = 0; i < this.N; i++) {
            worker[i].hash_to_unhash(hashes.get(i));
            worker[i].start();
        }
    }
}
