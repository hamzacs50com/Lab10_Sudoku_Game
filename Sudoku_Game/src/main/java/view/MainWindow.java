package view;

import interfaces.Viewable;
import interfaces.Catalog;
import model.DifficultyEnum;
import model.Game;
import exceptions.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {

    private final Viewable controller;
    private final GameCanvas canvas; 
    private JButton btnVerify, btnSolve, btnUndo;
    private int[][] currentBoardData;

    public MainWindow(Viewable controller) {
        this.controller = controller;
        this.canvas = new GameCanvas(); 
        initUI();
    }

    private void initUI() {
        setTitle("Sudoku Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        add(canvas, BorderLayout.CENTER);

        canvas.addCellListener(e -> {
            JTextField source = (JTextField) e.getSource();
            int r = (int) source.getClientProperty("row");
            int c = (int) source.getClientProperty("col");
            handleUserInput(r, c, source.getText());
        });

        JPanel controls = new JPanel();
        btnVerify = new JButton("Verify");
        btnSolve = new JButton("Solve (Auto)");
        btnUndo = new JButton("Undo");
        
        btnSolve.setEnabled(false); 

        btnVerify.addActionListener(e -> onVerify());
        btnSolve.addActionListener(e -> onSolve());
        btnUndo.addActionListener(e -> onUndo());

        controls.add(btnVerify);
        controls.add(btnSolve);
        controls.add(btnUndo);
        add(controls, BorderLayout.SOUTH);
    }

    public void start() {
        setVisible(true);
        try {
            Catalog catalog = controller.getCatalog();
            if (catalog.current) {
                if(confirm("Unfinished game found. Continue?")) {
                    loadGame(DifficultyEnum.INCOMPLETE);
                } else {
                    handleNewGameFlow(catalog);
                }
            } else {
                handleNewGameFlow(catalog);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void handleNewGameFlow(Catalog catalog) throws Exception {
        if (catalog.allModesExist) {
            String[] options = {"EASY", "MEDIUM", "HARD"};
            int choice = JOptionPane.showOptionDialog(this, "Select Difficulty", "New Game",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            
            if(choice != -1) {
                DifficultyEnum d = DifficultyEnum.valueOf(options[choice]);
                loadGame(d);
            }
        } else {
            String path = JOptionPane.showInputDialog(this, "Enter path to solved Sudoku file:");
            if (path != null && !path.trim().isEmpty()) {
                int[][] source = parseFile(path); 
                controller.driveGames(new Game(source)); 
                JOptionPane.showMessageDialog(this, "Games Generated! Please pick a level.");
                handleNewGameFlow(new Catalog(false, true)); 
            }
        }
    }

    private void loadGame(DifficultyEnum level) throws NotFoundException {
        Game game = controller.getGame(level);
        updateGrid(game.board);
    }

    private void updateGrid(int[][] board) {
        this.currentBoardData = board;
        
        canvas.updateGrid(board);
        
        checkSolveButtonState();
    }

    private void handleUserInput(int r, int c, String text) {
        try {
            int val = 0;
            if (!text.trim().isEmpty()) {
                val = Integer.parseInt(text.trim());
            }
            
            int prev = currentBoardData[r][c];
            
            if (val != prev) {
                currentBoardData[r][c] = val;
            
                String logEntry = String.format("(%d, %d, %d, %d)", r, c, val, prev);
                controller.logUserAction(logEntry);
            
                checkSolveButtonState();
            }
        } catch (NumberFormatException | IOException ex) {}
    }
    
    private void checkSolveButtonState() {
        int empty = 0;
        for(int[] row : currentBoardData) {
            for(int v : row) if(v == 0) empty++;
        }
        btnSolve.setEnabled(empty == 5);
    }

    private void onVerify() {
        this.currentBoardData = canvas.getBoard();
        
        String status = controller.verifyGame(new Game(currentBoardData));
        if ("VALID".equals(status)) {
            JOptionPane.showMessageDialog(this, "Congratulations! Puzzle Solved.");
        } else {
            JOptionPane.showMessageDialog(this, "Status: " + status);
        }
    }

    private void onSolve() {
        try {
            this.currentBoardData = canvas.getBoard();
            
            // Double check count locally just in case button state lagged
            int zeros = 0;
            for(int[] r : currentBoardData) for(int v : r) if(v==0) zeros++;
            
            if (zeros != 5) {
                JOptionPane.showMessageDialog(this, "You must have exactly 5 empty cells to use Auto Solve.\nCurrent empty: " + zeros);
                return;
            }

            int[] solution = controller.solveGame(new Game(currentBoardData));
            
            // Fill solution into empty spots
            int idx = 0;
            for(int r=0; r<9; r++) {
                for(int c=0; c<9; c++) {
                    if(currentBoardData[r][c] == 0) {
                        currentBoardData[r][c] = solution[idx++];
                    }
                }
            }
            updateGrid(currentBoardData); 
            JOptionPane.showMessageDialog(this, "Solved automatically!");
            
        } catch (InvalidGameException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    
    private void onUndo() {
        try {
            controller.undoLastMove();
            loadGame(DifficultyEnum.INCOMPLETE);
        } catch (NotFoundException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Undo failed: " + ex.getMessage());
        }
    }

    private int[][] parseFile(String path) throws IOException {
        int[][] b = new int[9][9];
        try (java.util.Scanner s = new java.util.Scanner(new File(path))) {
            for(int i=0; i<81; i++) {
                if(s.hasNextInt()) b[i/9][i%9] = s.nextInt();
            }
        }
        return b;
    }
    
    private boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}