package edu.hayes_rlynchburg.chesspuzzlelockscreen;

/**
 * Created by Ryan Hayes on 4/11/2017.
 */

public class Puzzle { // Added missing opening brace for class body

    private String initialLayout;
    private String finalLayout;
    private String name;

    // Constructor to initialize Puzzle object securely
    public Puzzle(String initialLayout, String finalLayout, String name) {
        this.initialLayout = initialLayout != null ? initialLayout : "";
        this.finalLayout = finalLayout != null ? finalLayout : "";
        this.name = name != null ? name : "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null) { // Validate input to prevent null assignment
            this.name = name;
        }
    }

    public String getFinalLayout() {
        return finalLayout;
    }

    public void setFinalLayout(String finalLayout) {
        if (finalLayout != null) { // Validate input to prevent null assignment
            this.finalLayout = finalLayout;
        }
    }

    public String getInitialLayout() {
        return initialLayout;
    }

    public void setInitialLayout(String initialLayout) {
        if (initialLayout != null) { // Validate input to prevent null assignment
            this.initialLayout = initialLayout;
        }
    }
}