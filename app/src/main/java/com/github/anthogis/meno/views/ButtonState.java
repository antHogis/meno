package com.github.anthogis.meno.views;

/**
 * ButtonState contains values portraying the state of a StatefulButton.
 *
 * ButtonState contains values portraying the state of a StatefulButton. The state of a button
 * is meant to define the action a Button should take when clicked.
 *
 * @author Anton Höglund
 * @version 1.0
 * @since 1.0
 */
public enum ButtonState {

    /**
     * A state when the button should delete something when clicked.
     */
    DELETE("Delete"),

    /**
     * A state when the button should rename something when clicked.
     */
    RENAME("Rename"),

    /**
     * The default state of the button (DELETE).
     */
    DEFAULT(DELETE);

    /**
     * The text in the button, which reflects it's state.
     */
    private String text;

    /**
     * Constructs a ButtonState value with a text.
     * @param text the text of the state.
     */
    ButtonState(String text) {
        this.text = text;
    }

    /**
     * Constructs a ButtonState from another ButtonState.
     * @param other the other ButtonState.
     */
    ButtonState(ButtonState other) {
        this.text = other.text;
    }

    /**
     * Returns the text of the ButtonState.
     * @return the text of the ButtonState.
     */
    public String getText() {
        return text;
    }

    /**
     * Compares equality between this ButtonState and another.
     * @param other the other ButtonState.
     * @return true if the texts of the ButtonStates are equal.
     */
    public boolean equals(ButtonState other) {
        return this.text.equals(other.text);
    }
}
