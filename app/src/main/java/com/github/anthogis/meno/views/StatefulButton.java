package com.github.anthogis.meno.views;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class StatefulButton extends AppCompatButton {
    private ButtonState state;

    public StatefulButton(Context context) {
        this(context, null);
    }

    public StatefulButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public StatefulButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.state = ButtonState.DEFAULT;
    }

    public ButtonState getState() {
        return state;
    }

    public void setState(ButtonState state) {
        this.state = state;
        setText(state.getText());
    }
}
