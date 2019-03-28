package com.github.anthogis.meno.views;

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

    public boolean equals(ButtonState other) {
        return this.text.equals(other.text);
    }
}
