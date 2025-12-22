package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameCanvas extends JPanel {
    
    private final JTextField[][] cells = new JTextField[9][9];

    public GameCanvas() {
        setLayout(new GridLayout(9, 9));
        initializeGrid();
    }

    private void initializeGrid() {
        Font font = new Font("Monospaced", Font.BOLD, 20);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(font);

                int top = (r % 3 == 0) ? 4 : 1;
                int left = (c % 3 == 0) ? 4 : 1;
                int bottom = (r == 8) ? 4 : 1;
                int right = (c == 8) ? 4 : 1;
                cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
                
                cell.putClientProperty("row", r);
                cell.putClientProperty("col", c);

                cells[r][c] = cell;
                add(cell);
            }
        }
    }

    public void updateGrid(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = board[r][c];
                JTextField cell = cells[r][c];
                               
                if (val == 0) {
                    cell.setText("");
                    cell.setEditable(true);
                    cell.setBackground(Color.WHITE);
                } else {
                    cell.setText(String.valueOf(val));
                    cell.setEditable(false);
                    cell.setBackground(new Color(220, 220, 220)); // Light Gray
                }
            }
        }
    }

    public void addCellListener(ActionListener listener) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cells[r][c].addActionListener(listener);
            }
        }
    }
    
    public int[][] getBoard() {
        int[][] board = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String text = cells[r][c].getText().trim();
                try {
                    // If empty, it's 0. If text, parse it.
                    board[r][c] = text.isEmpty() ? 0 : Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    board[r][c] = 0; // Treat bad input as empty
                }
            }
        }
        return board;
    }
}