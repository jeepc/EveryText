package com.jeepc.everytext;

import android.app.Application;
import android.content.Context;
import android.os.Build;

/**
 * Created by jeepc on 2017/3/31.
 */

public class EveryTextApp extends Application {
    private static Context mContext;

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
