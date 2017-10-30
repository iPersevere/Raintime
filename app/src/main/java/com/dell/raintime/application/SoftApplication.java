package com.dell.raintime.application;

import android.app.Application;
import android.content.Context;


public class SoftApplication extends Application {
    public static Context applicationContext;
    private static SoftApplication instance;

    public int borderViewPosition;//设置边界的view位置，如果是边界的view则支持左滑切换到底部的菜单


    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
    }

    public static SoftApplication getInstance() {
        return instance;
    }


    public int getBorderViewPosition() {
        return borderViewPosition;
    }

    public void setBorderViewPosition(int borderViewPosition) {
        this.borderViewPosition = borderViewPosition;
    }


}
