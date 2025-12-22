package model;

/*
 * this Model class representing a Sudoku game.
 * Encapsulates the 9x9 board state used by the application layer.
 * This class follows the MVC model separation.
 */

public class Game {
    public int[][] board;

    public Game(int[][] board) {
        this.board = board;
    }
}
