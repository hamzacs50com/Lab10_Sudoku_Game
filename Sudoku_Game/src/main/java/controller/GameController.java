package controller;

import exceptions.*;
import interfaces.*;
import java.io.*;
import java.util.*;
import logic.*;
import model.DifficultyEnum;
import model.Game;
import model.UserAction; 
import storage.StorageManager;

public class GameController implements Viewable, Controllable {

    private final StorageManager storage = new StorageManager();
    private final RandomPairs rng = new RandomPairs();
    private final SudokuSolver solver = new SudokuSolver();

    private int[][] currentBoard; 

    @Override
    public Catalog getCatalog() {
        boolean hasUnfinished = new File("storage/incomplete/game.txt").exists();
        boolean hasAll = hasFiles("easy") && hasFiles("medium") && hasFiles("hard");
        return new Catalog(hasUnfinished, hasAll);
    }

    private boolean hasFiles(String dir) {
        File f = new File("storage/" + dir);
        return f.exists() && f.listFiles() != null && f.listFiles().length > 0;
    }

    /**
     * VIEWABLE Implementation: Returns a Game Object
     * @param level
     * @return 
     * @throws exceptions.NotFoundException
     */
    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        return new Game(loadRawBoard(level)); 
    }

    /**
     * CONTROLLABLE Implementation: Returns int[][] and takes a char
     * @param level
     * @return 
     * @throws exceptions.NotFoundException
     */
    @Override
    public int[][] getGame(char level) throws NotFoundException {
        DifficultyEnum d = (level == 'h' || level == 'H') ? DifficultyEnum.HARD :
                           (level == 'm' || level == 'M') ? DifficultyEnum.MEDIUM : 
                           DifficultyEnum.EASY;
        return loadRawBoard(d);
    }

  private int[][] loadRawBoard(DifficultyEnum d) throws NotFoundException {
        try {
            int[][] loadedBoard;
            if (d == DifficultyEnum.INCOMPLETE) {
                loadedBoard = storage.loadBoard("storage/incomplete/game.txt");
            } else {
                File f = new File("storage/" + d.name().toLowerCase());
                if (!f.exists() || f.listFiles() == null || f.listFiles().length == 0) {
                     throw new IOException("No files found");
                }
                String sourcePath = f.listFiles()[0].getPath();
                loadedBoard = storage.loadBoard(sourcePath);
                
                storage.deleteSource(sourcePath);
            }

            this.currentBoard = loadedBoard;
            storage.saveBoard(this.currentBoard, "storage/incomplete/game.txt");

            return loadedBoard;
        } catch (IOException e) {
            throw new NotFoundException("Game not found for level: " + d);
        }
    }

    /**
     *
     * @param source
     * @throws SolutionInvalidException
     */
    @Override
    public void driveGames(Game source) throws SolutionInvalidException {
        driveGames(source.board); 
    }

    /**
     *
     * @param source
     * @throws SolutionInvalidException
     */
    @Override
    public void driveGames(int[][] source) throws SolutionInvalidException {
        if (!SudokuVerifier.verify(source).equals("VALID"))
            throw new SolutionInvalidException("Source solution is INVALID or INCOMPLETE");

        try {
            generate(source, DifficultyEnum.EASY, 10);
            generate(source, DifficultyEnum.MEDIUM, 25);
            generate(source, DifficultyEnum.HARD, 20);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generate(int[][] src, DifficultyEnum d, int remove) throws IOException {
        int[][] copy = deepCopy(src);
        
        List<int[]> holes = rng.generateDistinctPairs(remove); 

        for (int[] coord : holes) {
            int r = coord[0];
            int c = coord[1];
            copy[r][c] = 0;
        }

        storage.saveBoard(
            copy,
            "storage/" + d.name().toLowerCase() + "/game_" + System.currentTimeMillis() + ".txt"
        );
    }

    /**
     *
     * @param game
     * @return
     */
    @Override
    public String verifyGame(Game game) {
        String status = SudokuVerifier.verify(game.board);
        if (status.equals("VALID")) {
            storage.deleteIncomplete();
        }
        return status;
    }

    /**
     *
     * @param game
     * @return
     */
    @Override
    public boolean[][] verifyGame(int[][] game) {
        boolean[][] result = new boolean[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = game[r][c];
                if (val != 0 && !isSafe(game, r, c, val)) {
                    result[r][c] = false;
                } else {
                    result[r][c] = true;
                }
            }
        }
        return result;
    }

    private boolean isSafe(int[][] board, int row, int col, int val) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == val && i != col) return false;
            if (board[i][col] == val && i != row) return false;
        }
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[boxRow + i][boxCol + j] == val && 
                   (boxRow + i != row || boxCol + j != col)) return false;
            }
        }
        return true;
    }

    /**
     *
     * @param game
     * @return
     * @throws InvalidGameException
     */

    @Override
    public int[] solveGame(Game game) throws InvalidGameException {
        return solver.solve(game.board);
    }

    /**
     *
     * @param game
     * @return
     * @throws InvalidGameException
     */
    @Override
    public int[][] solveGame(int[][] game) throws InvalidGameException {
        solver.solve(game); 
        return game;
    }

    /**
     *
     * @param action
     * @throws IOException
     */
    @Override
    public void logUserAction(String action) throws IOException {
        storage.log(action);
        if (currentBoard != null) {
            storage.saveBoard(currentBoard, "storage/incomplete/game.txt");
        }
    }

    public void logUserAction(UserAction userAction) throws IOException {
        logUserAction(userAction.toString());
    }

    /**
     *
     * @throws IOException
     */
    @Override
    public void undoLastMove() throws IOException {
        if (currentBoard != null) {
            storage.undo(currentBoard);
            storage.saveBoard(currentBoard, "storage/incomplete/game.txt");
        }
    }

    private int[][] deepCopy(int[][] b) {
        int[][] n = new int[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(b[i], 0, n[i], 0, 9);
        return n;
    }
}