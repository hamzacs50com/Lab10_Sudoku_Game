package logic;

import exceptions.InvalidGameException;
import java.util.ArrayList;
import java.util.List;


public class SudokuSolver {
    
    public int[] solve(int[][] board) throws InvalidGameException {

        List<int[]> empty = new ArrayList<>();

        for (int i=0;i<9;i++)
            for (int j=0;j<9;j++)
                if (board[i][j]==0)
                    empty.add(new int[]{i,j});

        if (empty.size()!=5)
            throw new InvalidGameException("Exactly 5 empty cells required");

        PermutationIterator it = new PermutationIterator();

        while (it.hasNext()) {
            int[] comb = it.next();
            if (FlyweightVerifier.isValid(board, empty, comb))
                return comb;
        }
        throw new InvalidGameException("No solution");
    }
}
