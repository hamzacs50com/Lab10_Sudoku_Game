package logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/*
 * this class is given in the lab PDF
 */
public class RandomPairs {
    private static final int MAX_COORD = 8; 
    
    private static final int MAX_UNIQUE_PAIRS = (MAX_COORD + 1) * (MAX_COORD + 1);
    private final Random random;

    public RandomPairs() {
        this.random = new Random();
    }

    public List<int[]> generateDistinctPairs(int n) {
        // [cite: 44-48] Safety check
        if (n < 0 || n > MAX_UNIQUE_PAIRS) {
             throw new IllegalArgumentException("n must be between 0 and " + MAX_UNIQUE_PAIRS);
        }

        Set<Integer> used = new HashSet<>();
        List<int[]> result = new ArrayList<>(n);
        
        while (result.size() < n) {
            // [cite: 53-55]
            int x = random.nextInt(MAX_COORD + 1); // Now generates 0..8
            int y = random.nextInt(MAX_COORD + 1); // Now generates 0..8
            
            int key = x * (MAX_COORD + 1) + y;
            
            if (used.add(key)) {
                result.add(new int[] { x, y });
            }
        }
        return result;
    }
}
