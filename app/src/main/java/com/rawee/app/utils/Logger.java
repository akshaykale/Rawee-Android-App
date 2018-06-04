package com.rawee.app.utils;

import android.util.Log;

public class Logger {

    static Logger sInstance;

    private boolean DEBUG_MODE = true;

    private Logger() {
    }

    /**
     * @return Logger
     */
    public static synchronized Logger getsInstance() {
        if (sInstance == null) {
            sInstance = new Logger();
        }
        return sInstance;
    }

    /**
     * @param mode bool
     */
    public void debugMode(boolean mode) {
        DEBUG_MODE = mode;
    }

    /**
     * @return bool
     */
    public boolean debugMode() {
        return DEBUG_MODE;
    }

    public void logD(String tag, String value) {
        if (debugMode())
            Log.d(tag, value);
    }
    public void logE(String tag, String value) {
        if (debugMode())
            Log.e(tag, value);
    }
    public void logW(String tag, String value, Exception e) {
        if (debugMode())
            Log.w(tag, value, e);
    }

    public void logDb(String tag, String value) {
        if (!debugMode()) {

        }
    }
}
