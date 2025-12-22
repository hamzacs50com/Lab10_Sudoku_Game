package model;

public class UserAction {
    private final String action;

    public UserAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return action;
    }
}