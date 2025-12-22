package interfaces;

import model.DifficultyEnum;
import exceptions.*;
import java.io.IOException;
import model.Game;

/*
 * this Interface representing actions required by the View (GUI) and Implemented by the Controller.
 * It abstracts application logic away from the presentation layer.
 */

public interface Viewable {

    Catalog getCatalog();
    
    Game getGame(DifficultyEnum level) throws NotFoundException;    
    
    void driveGames(Game source) throws SolutionInvalidException;

    String verifyGame(Game game);

    int[] solveGame(Game game) throws InvalidGameException;

    void logUserAction(String userAction) throws IOException;

    void undoLastMove() throws IOException;
}
