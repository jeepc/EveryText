package com.jeepc.everytext;

import android.content.Context;
import android.view.WindowManager;


public class FloatWindowManager {
    private static FloatWindowManager floatWindowManager;

    private static WindowManager windowManager;


    public static FloatWindowManager getInstance() {
        if (floatWindowManager == null) {
            floatWindowManager = new FloatWindowManager();
        }
        return floatWindowManager;
    }

    protected FloatWindowManager() {
    }

    private TextFloatWindow mTextFW;


    public void showFloatWindow() {
        if (mTextFW == null) {
            mTextFW = new TextFloatWindow(EveryTextApp.getContext());

        }
        getWindowManager().addView(mTextFW, mTextFW.layoutParams);
    }


    public void updateFloatWindow() {
        if (mTextFW != null) {
            mTextFW.update();
        }
    }


    public void closeFloatWindow() {
        if (mTextFW != null) {
            getWindowManager().removeView(mTextFW);
            mTextFW = null;
        }
    }


    public boolean isFloatWindowShowing() {
        return mTextFW != null;
    }


    private WindowManager getWindowManager() {
        if (windowManager == null) {
            windowManager = (WindowManager) EveryTextApp.getContext().getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }
}
