package com.github.anthogis.meno;

import android.app.Application;

public class MenoApplication extends Application {

    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = new DatabaseHelper(this);
    }

    private boolean firstStarted = true;

    public boolean isFirstStarted() {
        return firstStarted;
    }

    public void setFirstStarted(boolean firstStarted) {
        this.firstStarted = firstStarted;
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}
