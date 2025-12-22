package logic;

/*
 * this Iterator is used by the solver by generating all possible value combinations (1–9) for exactly 5 empty cells (max 9^5 permutations)..
 */

public class PermutationIterator {
    
    private final int[] cur = {1,1,1,1,1};
    private boolean hasNext = true;

    public boolean hasNext() { return hasNext; }

    public int[] next() {
        int[] out = cur.clone();
        inc();
        return out;
    }

    private void inc() {
        for (int i = 4; i >= 0; i--) {
            if (cur[i] < 9) {
                cur[i]++;
                return;
            }
            cur[i] = 1;
        }
        hasNext = false;
    }
}
