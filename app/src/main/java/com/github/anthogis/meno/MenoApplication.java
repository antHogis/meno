package com.github.anthogis.meno;

import android.app.Application;

public class MenoApplication extends Application {
    private boolean firstStarted = true;

    public boolean isFirstStarted() {
        return firstStarted;
    }

    public void setFirstStarted(boolean firstStarted) {
        this.firstStarted = firstStarted;
    }
}
