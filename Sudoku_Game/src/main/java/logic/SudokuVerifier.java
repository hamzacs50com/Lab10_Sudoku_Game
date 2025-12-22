package logic;

/*
 * Performs the sequential verification mode.
 * Determines whether a board is:
 * - VALID
 * - INVALID
 * - INCOMPLETE
 * Checks rows, columns, and Boxes.
 */

public class SudokuVerifier {
    
    public static String verify(int[][] board) {
        boolean incomplete = false;

        for (int i = 0; i < 9; i++) {
            boolean[] r = new boolean[10];
            boolean[] c = new boolean[10];

            for (int j = 0; j < 9; j++) {
                int rv = board[i][j];
                int cv = board[j][i];

                if (rv == 0 || cv == 0) incomplete = true;

                if (rv != 0 && r[rv]) return "INVALID";
                if (cv != 0 && c[cv]) return "INVALID";

                if (rv != 0) r[rv] = true;
                if (cv != 0) c[cv] = true;
            }
        }

        for (int b = 0; b < 9; b++) {
            boolean[] seen = new boolean[10];
            for (int i = 0; i < 9; i++) {
                int r = (b / 3) * 3 + i / 3;
                int c = (b % 3) * 3 + i % 3;
                int v = board[r][c];
                if (v == 0) continue;
                if (seen[v]) return "INVALID";
                seen[v] = true;
            }
        }

        if (incomplete) return "INCOMPLETE";
        return "VALID";
    }
}
