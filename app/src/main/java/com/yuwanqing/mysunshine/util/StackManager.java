package com.yuwanqing.mysunshine.util;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by yuwanqing on 2017-04-11.
 */
public class StackManager {
    /**
     * Stack中对应的Activity列表（也可以写作<StackActivity>)
     */
    private static Stack mActivityStack;
    private static StackManager mInstance;
    /**
     *@描述 获取栈管理的工具
     */
    public static StackManager getStackManager() {
        if(mInstance == null) {
            mInstance = new StackManager();
        }
        return mInstance;
    }
    /**
     * 推出栈顶Activity
     */
    public void popActivity(Activity activity) {
        if(activity != null) {
            activity.finish();
            mActivityStack.remove(activity);
            activity = null;
        }
    }
    /**
     * 获得当前栈顶Activity
     */
    public Activity currentActivity() {
        //lastElement()获取最后个子元素，这里是栈顶的Activity
        if(mActivityStack == null || mActivityStack.size() == 0) {
            return null;
        }
        Activity activity = (Activity) mActivityStack.lastElement();
        return activity;
    }
    /**
     * 将当前Activity推入栈中
     */
    public void pushActivity(Activity activity) {
        if(mActivityStack == null) {
            mActivityStack = new Stack();
        }
        mActivityStack.add(activity);
    }
    /**
     * 弹出指定的clsss所在栈顶部中所有Activity
     */
    /**
     * 弹出栈中所有Activity
     */
    public void popAllActivitys(){
        while(true) {
            Activity activity = currentActivity();
            if(activity == null) {
                break;
            }
            popActivity(activity);
        }
    }
}
