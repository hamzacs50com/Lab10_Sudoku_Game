package interfaces;

import exceptions.*;
import java.io.IOException;

/*
 * this Interface representing actions required by the Controller when interacting with the View.
 * this Exists to simulate separation between viewer and controller, each with its own data representation.
 */

public interface Controllable {

    Catalog getCatalog();

    int[][] getGame(char level) throws NotFoundException;

    void driveGames(int[][] source) throws SolutionInvalidException;

    boolean[][] verifyGame(int[][] game);

    int[][] solveGame(int[][] game) throws InvalidGameException;

    void logUserAction(String userAction) throws IOException;

    void undoLastMove() throws IOException;
}
