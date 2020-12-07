package com.achulkov.loftcoin;

import android.app.Application;
import android.os.StrictMode;

import com.achulkov.loftcoin.util.DebugTree;

import timber.log.Timber;

public class LoftApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
            Timber.plant(new DebugTree());
        }
    }

}
