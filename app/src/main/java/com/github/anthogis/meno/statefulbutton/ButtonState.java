package com.github.anthogis.meno.statefulbutton;

public enum ButtonState {
    DELETE("Delete"),
    RENAME("Rename"),
    DEFAULT(DELETE);

    private String text;

    ButtonState(String text) {
        this.text = text;
    }

    ButtonState(ButtonState other) {
        this.text = other.text;
    }

    public String getText() {
        return text;
    }
}
