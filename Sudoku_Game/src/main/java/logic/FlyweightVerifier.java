package logic;

import java.util.List;

public class FlyweightVerifier implements Runnable {
    
    private final int[][] board;
    private String result;

    public FlyweightVerifier(int[][] board) {
        this.board = board;
    }

    @Override
    public void run() {
        this.result = SudokuVerifier.verify(board);
    }

    public String getResult() {
        return result;
    }

 
    public static boolean isValid(int[][] originalBoard, List<int[]> positions, int[] values) {
        for (int i = 0; i < positions.size(); i++) {
            for (int j = i + 1; j < positions.size(); j++) {
                if (values[i] == values[j]) {
                    if (areInSameUnit(positions.get(i), positions.get(j))) return false;
                }
            }
            int r = positions.get(i)[0];
            int c = positions.get(i)[1];
            if (!isSafeOnBoard(originalBoard, r, c, values[i])) return false;
        }
        return true;
    }

    private static boolean areInSameUnit(int[] p1, int[] p2) {
        return (p1[0] == p2[0]) || (p1[1] == p2[1]) || 
               ((p1[0]/3 == p2[0]/3) && (p1[1]/3 == p2[1]/3));
    }

    private static boolean isSafeOnBoard(int[][] board, int row, int col, int val) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == val) return false;
            if (board[i][col] == val) return false;
        }
        int boxR = row - row % 3;
        int boxC = col - col % 3;
        for (int i = 0; i < 3; i++) 
            for (int j = 0; j < 3; j++) 
                if (board[boxR+i][boxC+j] == val) return false;
        return true;
    }
}