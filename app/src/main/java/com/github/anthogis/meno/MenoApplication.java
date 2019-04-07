package com.github.anthogis.meno;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

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

    public void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }
}
