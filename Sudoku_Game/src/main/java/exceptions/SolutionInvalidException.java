package exceptions;

/*
 * this exception Thrown when a provided source Sudoku solution is INVALID or INCOMPLETE during the game generation phase.
 */

public class SolutionInvalidException extends Exception {

    public SolutionInvalidException(String msg) {super(msg);}
    
}
