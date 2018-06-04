package com.rawee.app;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

import com.rawee.app.login.GoogleApiHelper;

/**
 * Created by akshaykale on 2017/08/15.
 */

public class RaweeApplication extends Application {

    static RaweeApplication sInstance;

    private GoogleApiHelper googleApiHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        googleApiHelper = new GoogleApiHelper(getAppContext());

    }

    public static synchronized RaweeApplication getsInstance(){
        return sInstance;
    }

    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }

    public GoogleApiHelper getGoogleApiHelperInstance() {
        return this.googleApiHelper;
    }

    public static GoogleApiHelper getGoogleApiHelper() {
        return getsInstance().getGoogleApiHelperInstance();
    }
}
