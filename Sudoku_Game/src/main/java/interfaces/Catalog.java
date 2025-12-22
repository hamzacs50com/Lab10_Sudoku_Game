package interfaces;

/*
 * Data structure returned to the presentation layer at startup.
 * Indicates:
 * - When an incomplete game exists.
 * - When at least one game exists for each difficulty level.
 *
 * Used to drive GUI startup flow.
 */
public class Catalog {
    public boolean current;
    public boolean allModesExist;

    public Catalog(boolean current, boolean allModesExist) {
        this.current = current;
        this.allModesExist = allModesExist;
    }
}