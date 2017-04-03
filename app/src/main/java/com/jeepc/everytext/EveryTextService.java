package com.jeepc.everytext;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class EveryTextService extends AccessibilityService {

    public static final String TEXTVIEW = "android.widget.TextView";

    private Context mContext;

    private Timer mTimer;

    private Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(mContext, "xixi", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        if (source == null) {
            return;
        }

       /* AccessibilityNodeInfo rowNode = getListItemNodeInfo(source);
        if (rowNode == null) {
            return;
        }

        for (int i = 0; i < rowNode.getChildCount(); i++) {
            AccessibilityNodeInfo node = rowNode.getChild(i);
            if ("TextView".equals(node.getClassName())) {
                Log.i("text", node.getText().toString());
            }
        }*/
        for (int i = 0; i < source.getChildCount(); i++) {
            AccessibilityNodeInfo node = source.getChild(i);
            if (TEXTVIEW.equals(node.getClassName())) {
                Log.i("jeepctext", node.getText().toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                            "xixi");
                    node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                }
                //   node.setText(node.getText().toString()+1);
                //    node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT);
            }
        }

    }

    private AccessibilityNodeInfo getListItemNodeInfo(AccessibilityNodeInfo source) {

        AccessibilityNodeInfo current = source;

        while (true) {

            AccessibilityNodeInfo parent = current.getParent();

            if (parent == null) {

                return null;

            }

            if ("android.support.constraint.ConstraintLayout".equals(parent.getClassName())) { //找到TaskListView

                return current;

            }

            // NOTE: Recycle the infos. 记得回收AccessibilityNodeInfo

            AccessibilityNodeInfo oldCurrent = current;

            current = parent;

            oldCurrent.recycle();

        }

    }


    @Override
    public void onInterrupt() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       /* if (mTimer == null) {
            mTimer = new Timer();
            //每隔500毫秒执行刷新操作
            mTimer.schedule(new RefreshTask(),0, 500);
        }*/
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
      /*  mTimer.cancel();
        mTimer = null;
        FloatWindowManager.getInstance().closeFloatWindowSmall();*/
        super.onDestroy();
    }

    /**
     * 后台检测刷新任务
     */
    class RefreshTask extends TimerTask {

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void run() {
            if (AppUtil.getLatestPackage(mContext) == null) {
                //最近运行的app包名为null执行刷新小悬浮窗操作
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //已显示悬浮窗，更新小悬浮窗
                        FloatWindowManager.getInstance().updateFloatWindow();
                    }
                });
                return;
            }
            if (AppUtil.isHome(mContext)) {
                //当前在桌面上则显示或更新悬浮窗
                if (FloatWindowManager.getInstance().isFloatWindowShowing()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //已显示悬浮窗，更新小悬浮窗
                            FloatWindowManager.getInstance().updateFloatWindow();
                        }
                    });
                } else if (!FloatWindowManager.getInstance().isFloatWindowShowing()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //未显示悬浮窗，则显示小悬浮窗
                            FloatWindowManager.getInstance().showFloatWindow();
                        }
                    });
                }
            } else {
                //当前不在桌面则关闭所有悬浮窗
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowManager.getInstance().closeFloatWindow();
                    }
                });
            }
        }
    }
}
