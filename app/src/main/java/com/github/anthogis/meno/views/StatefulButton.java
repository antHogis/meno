package com.github.anthogis.meno.views;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * A button which contains a modifiable ButtonState.
 *
 * A button which contains a modifiable ButtonState, which provides a way to determine the action of
 * a button depending on it's state. The ButtonState also determines the text of the Button.
 * For example, when editing an ExpenseCategory, the ButtonState of the Button is changed from DELETE
 * to EDIT if the user changes the name of the ExpenseCategory. The action executed on the click of the
 * button is then determined based on the ButtonState of the Button.
 *
 * @author Anton Höglund
 * @version 1.0
 * @since 1.0
 */
public class StatefulButton extends AppCompatButton {

    /**
     * The state of the Button.
     */
    private ButtonState state;

    /**
     * Calls constructor with params Context, AttributeSet.
     * @param context see Android documentation for AppCompatButton.
     */
    public StatefulButton(Context context) {
        this(context, null);
    }

    /**
     * Calls constructor with params Context, AttributeSet, int.
     * @param context see Android documentation for AppCompatButton.
     * @param attrs see Android documentation for AppCompatButton.
     */
    public StatefulButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    /**
     * Constructs this instance with super constructor with the same parameters. Sets default ButtonState.
     *
     * @param context see Android documentation for AppCompatButton.
     * @param attrs see Android documentation for AppCompatButton.
     * @param defStyleAttr see Android documentation for AppCompatButton.
     */
    public StatefulButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.state = ButtonState.DEFAULT;
    }

    /**
     * Returns the ButtonState of this button.
     *
     * @return the ButtonState of this button.
     */
    public ButtonState getState() {
        return state;
    }

    /**
     * Sets the ButtonState of this Button.
     * @param state the ButtonState of this Button.
     */
    public void setState(ButtonState state) {
        this.state = state;
        setText(state.getText());
    }
}
