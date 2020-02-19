package com.yanzhenjie.permission.view;

import android.view.Gravity;

import java.util.Random;

/**
 * Description:
 *
 * @author: roy
 * Time: 2018/7/16 上午11:40
 * Modifier:
 * Fix Description:
 * Version:
 */
public class ViewParams {
    /**
     * 宽
     */
    private double width;
    /**
     * 高
     */
    private double height;
    /**
     * x坐标
     */
    private int x;
    /**
     * y坐标
     */
    private int y;
    /**
     * 当前父窗口宽度
     */
    private int contentWidth;
    /**
     * 屏幕宽度
     */

    private double screenWidth;
    /**
     * 屏幕高度
     */
    private double screenHeight;

    /**
     * 状态栏高度
     */
    private int statusBarHeight;

    private int gravity;

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getContentWidth() {
        return contentWidth;
    }

    public void setContentWidth(int contentWidth) {
        this.contentWidth = contentWidth;
    }

    public double getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(double screenWidth) {
        this.screenWidth = screenWidth;
    }

    public double getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(double screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    public int getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }


    public ViewParams getDefaultViewParams() {
        ViewParams params = new ViewParams();
        int width = 0;
        int height = 0;
        int statusBarHeight = 20;
        params.setStatusBarHeight(statusBarHeight);
        int[] gravityArray = {Gravity.TOP | Gravity.START, Gravity.BOTTOM | Gravity.START, Gravity.TOP | Gravity.END, Gravity.BOTTOM | Gravity.END};
        int index = new Random().nextInt(4);
        params.setGravity(gravityArray[index]);

        params.setWidth(width);
        params.setHeight(height);

        return params;
    }


    public ViewParams getCustomerViewParams(int width, int height) {
        ViewParams params = new ViewParams();
        int statusBarHeight = 20;
        params.setStatusBarHeight(statusBarHeight);
        int[] gravityArray = {Gravity.TOP | Gravity.START, Gravity.BOTTOM | Gravity.START, Gravity.TOP | Gravity.END, Gravity.BOTTOM | Gravity.END};
        int index = new Random().nextInt(4);
        params.setGravity(gravityArray[index]);
        params.setWidth(width);
        params.setHeight(height);

        return params;
    }

    @Override
    public String toString() {
        return "ViewParams{" +
                "width=" + width +
                ", height=" + height +
                ", x=" + x +
                ", y=" + y +
                ", contentWidth=" + contentWidth +
                ", screenWidth=" + screenWidth +
                ", screenHeight=" + screenHeight +
                ", statusBarHeight=" + statusBarHeight +
                ", gravity=" + gravity +
                '}';
    }
}
