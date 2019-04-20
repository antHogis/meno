package com.github.anthogis.meno;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

/**
 * Convenience class for global access of common operations.
 *
 * Provides access to common operations for all Activities and Fragments of this app.
 * Also provides a way to inspect and manipulate whether the application was just opened,
 * since this cannot be reliably done in an Activity.
 *
 * @author Anton Höglund
 * @version 1.3
 * @since 1.0
 */
public class MenoApplication extends Application {

    /**
     * The DatabaseHelper used in this app.
     */
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = new DatabaseHelper(this);
    }

    /**
     * Indicates whether or not the Application was just started. Needs to be set to false in an Activity for example.
     */
    private boolean firstStarted = true;

    /**
     * Returns firstStarted.
     * @return firstStarted.
     */
    public boolean isFirstStarted() {
        return firstStarted;
    }

    /**
     * Sets firstStarted.
     * @param firstStarted the value to set firstStarted to.
     */
    public void setFirstStarted(boolean firstStarted) {
        this.firstStarted = firstStarted;
    }

    /**
     * Returns the DatabaseHelper of the Application.
     * @return the DatabaseHelper.
     */
    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    /**
     * Vibrates the device for 500 milliseconds.
     */
    public void vibrateError() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }

    /**
     * Vibrates the device for 100 milliseconds.
     */
    public void vibrateSuccess() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }
}
