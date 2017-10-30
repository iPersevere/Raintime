package com.dell.raintime.entity;

public class MyBitmapEntity {
    public float x;
    public float y;
    public float width;
    public float height;
    static int devide = 1;
    public int index = -1;

    @Override
    public String toString() {
        return "MyBitmap [x=" + x + ", y=" + y + ", width=" + width
                + ", height=" + height + ", devide=" + devide + ", index="
                + index + "]";
    }
}
