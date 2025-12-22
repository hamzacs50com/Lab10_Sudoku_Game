package logic;

import java.util.List;


public class FlyweightVerifier {
    
    public static boolean isValid(int[][] board, List<int[]> pos, int[] val) {

        for (int i = 0; i < pos.size(); i++) {
            int r = pos.get(i)[0];
            int c = pos.get(i)[1];
            int v = val[i];

            for (int j = 0; j < pos.size(); j++) {
                if (i != j && val[j] == v) {
                    int r2 = pos.get(j)[0];
                    int c2 = pos.get(j)[1];
                    if (r == r2 || c == c2 || (r/3 == r2/3 && c/3 == c2/3))
                        return false;
                }
            }

            for (int k = 0; k < 9; k++)
                if (board[r][k] == v || board[k][c] == v)
                    return false;

            int sr = r - r % 3, sc = c - c % 3;
            for (int i2 = 0; i2 < 3; i2++)
                for (int j2 = 0; j2 < 3; j2++)
                    if (board[sr+i2][sc+j2] == v)
                        return false;
        }
        return true;
    }
}
