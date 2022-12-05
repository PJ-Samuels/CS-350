public class UnHash {
    int unhash(String to_unhash, Float time) throws InterruptedException {
        Hash hasher = new Hash();
        long start = System.currentTimeMillis();
    
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            String hashedVal = hasher.hash(Integer.toString(i));
            if (to_unhash.equals(hashedVal)) {
                return i;
            }
            long end = System.currentTimeMillis();
            if (time != null) {
                if (end - start > time) {
                    throw new InterruptedException("Timeout");
                }
            }
        }
        return -1;
    }
}
