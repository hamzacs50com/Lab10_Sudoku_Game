package storage;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class LogHandler {
    
    private final String logFilePath;

    public LogHandler(String rootPath) {
        this.logFilePath = Paths.get(rootPath, "current", "game.log").toString();
    }

    public void log(String entry) throws IOException {
        Files.write(Paths.get(logFilePath),
            (entry + System.lineSeparator()).getBytes(),
            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public void undo(int[][] board) throws IOException {
        Path path = Paths.get(logFilePath);
        if (!Files.exists(path)) return;

        List<String> lines = Files.readAllLines(path);
        if (lines.isEmpty()) return;

        String lastAction = lines.remove(lines.size() - 1);
        Files.write(path, lines); 
        parseAndRevert(board, lastAction);
    }

    private void parseAndRevert(int[][] board, String action) {
        try {
            String clean = action.replace("(", "").replace(")", "");
            String[] parts = clean.split(",");
            
            int r = Integer.parseInt(parts[0].trim());
            int c = Integer.parseInt(parts[1].trim());
            int prev = Integer.parseInt(parts[3].trim()); 

            board[r][c] = prev; 
        } catch (Exception e) {
            System.err.println("Failed to parse undo action: " + action);
        }
    }
    
    public void deleteLog() {
        new File(logFilePath).delete();
    }
}