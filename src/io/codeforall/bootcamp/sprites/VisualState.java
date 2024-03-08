package io.codeforall.bootcamp.sprites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class VisualState {
    private String state;
    private String lockedState = "deadRIGHT";
    private boolean locked = false;
    private final HashSet<String> possibleActions;
    private final HashSet<String> possibleDirections;

    public VisualState(String startState, String[] possible) {
        this.state = startState;
        this.possibleActions = convertTo(possible, "action");
        this.possibleDirections = convertTo(possible, "direction");
    }

    private HashSet<String> convertTo(String[] possible, String identifier) {
        String regex = "[a-z]";
        if (identifier.equals("action")) {
            regex = "[A-Z]";
        }
        String out = "";

        for (String s : possible) {
            out += s.replaceAll(regex, " ");
        }
        out = out.strip();
        return new HashSet<>(Arrays.asList(out.split(" +")));

    }

    public void replace(String target, String replacement) {
        if (!possibleChange(replacement)){return;}
        state = state.replace(target, replacement);
    }

    public void setAction(String action) {
        for (String s : possibleActions) {
            replace(s, action);
        }
    }

    public void setDirection(String direction) {
        for (String s : possibleDirections) {
            replace(s, direction);
        }
    }

    public boolean hasAction(String action) {
        return possibleActions.contains(action);
    }

    public boolean actionIs(String action) {
        return getState().startsWith(action);
    }

    public String getState() {
        return (locked) ? lockedState : state;
    }

    public void lock() {
        lockedState = state;
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    private boolean possibleChange(String target) {
        return possibleActions.contains(target) || possibleDirections.contains(target);
    }
}
