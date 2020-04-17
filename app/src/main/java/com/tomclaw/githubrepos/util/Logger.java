package com.tomclaw.githubrepos.util;

import android.util.Log;

import com.tomclaw.githubrepos.core.Config;

/**
 * Created by solkin on 20/01/2018.
 */
public class Logger {

    public void log(String message) {
        Log.d(Config.LOG_TAG, message);
    }

    public void log(String message, Throwable ex) {
        Log.d(Config.LOG_TAG, message, ex);
    }

}
