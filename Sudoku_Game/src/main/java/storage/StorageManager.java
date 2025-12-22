package storage;

import java.io.*;
import java.nio.file.*;
import java.util.*;


public class StorageManager {
    
    private static final String ROOT = "storage";
    private final LogHandler logHandler; 

    public StorageManager() {
        for (String d : new String[]{"easy", "medium", "hard", "incomplete"}) {
            new File(ROOT, d).mkdirs();
        }
        this.logHandler = new LogHandler(ROOT); 
    }

    public void saveBoard(int[][] board, String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int val : row) sb.append(val).append(" ");
            sb.append("\n");
        }
        Files.write(Paths.get(path), sb.toString().getBytes());
    }

    public int[][] loadBoard(String path) throws IOException {
        int[][] board = new int[9][9];
        try (Scanner scanner = new Scanner(new File(path))) {
            for (int i = 0; i < 81; i++) {
                if (scanner.hasNextInt()) 
                    board[i / 9][i % 9] = scanner.nextInt();
            }
        }
        return board;
    }

    public void log(String entry) throws IOException {
        logHandler.log(entry);
    }

    public void undo(int[][] board) throws IOException {
        logHandler.undo(board);
    }

    public void deleteIncomplete() {
        try {
            Files.deleteIfExists(Paths.get(ROOT, "incomplete", "game.txt"));
            logHandler.deleteLog();
        } catch (IOException e) {
            System.gc(); 
        }
    }
    
    public void deleteSource(String path) {
        try { Files.deleteIfExists(Paths.get(path)); } catch (IOException e) { }
    }
}
