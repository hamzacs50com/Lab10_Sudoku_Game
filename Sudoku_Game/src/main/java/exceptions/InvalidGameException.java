package exceptions;

/*
 * this exception Thrown when an operation is performed on an invalid game state.
 * Commonly used by the solver when constraints are not the same.
 */

public class InvalidGameException extends Exception {

    public InvalidGameException(String msg) {super(msg);}
    
}
