package exceptions;

/*
 * this exception Thrown when a requested Sudoku game cannot be found.
 * Used during game loading for difficulty or incomplete states.
 */
public class NotFoundException extends Exception {

    public NotFoundException(String msg) {super(msg);} 
    
}
