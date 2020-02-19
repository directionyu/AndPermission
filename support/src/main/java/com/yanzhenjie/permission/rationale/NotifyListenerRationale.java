/*
 * Copyright 2018 Zhenjie Yan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.permission.rationale;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.yanzhenjie.permission.R;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.view.SystemUtils;
import com.yanzhenjie.permission.view.ViewParams;


public class NotifyListenerRationale implements Rationale<Void> {

    @Override
    public void showRationale(final Context context, Void data, final RequestExecutor executor) {
        if (context instanceof Activity && !((Activity) context).isDestroyed()) {
            final FrameLayout mContainerView = new FrameLayout(context);

            int containerWidth = (int) getContainerViewParams(context).getWidth();

            int containerHeight = (int) getContainerViewParams(context).getHeight();

            FrameLayout.LayoutParams containerViewParams = new FrameLayout.LayoutParams(containerWidth, containerHeight);

            containerViewParams.gravity = Gravity.CENTER;

            mContainerView.setLayoutParams(containerViewParams);

            mContainerView.setBackground(context.getResources().getDrawable(R.mipmap.roy_permission_bg_a));

            getActivityRoot((Activity) context).addView(mContainerView);

            mContainerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    executor.execute();
                    getActivityRoot((Activity) context).removeView(mContainerView);
                }
            });


            int windowWidth = (int) getPermissionWindowParams(context).getWidth();
            int windowHeight = (int) getPermissionWindowParams(context).getHeight();

            FrameLayout.LayoutParams windowViewParams = new FrameLayout.LayoutParams(windowWidth, windowHeight);

            windowViewParams.gravity = Gravity.CENTER;

            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.layout_body, null);
            layout.setLayoutParams(windowViewParams);

            mContainerView.addView(layout);

        }
    }

    /**
     * 获取当前视图窗口根布局
     *
     * @param activity current activity
     * @return root id
     */
    private FrameLayout getActivityRoot(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 默认获取全屏尺寸
     *
     * @return default params
     */
    private ViewParams getContainerViewParams(Context context) {
        ViewParams params = new ViewParams();
        double screenWidth = SystemUtils.getScreenWidth(context.getApplicationContext());
        double screenHeight = (double) SystemUtils.getScreenHeight(context.getApplicationContext());
        int statusBarHeight = (int) SystemUtils.getStatusBarHeight(context.getApplicationContext());

        params.setScreenWidth(screenWidth);
        params.setScreenHeight(screenHeight);
        params.setStatusBarHeight(statusBarHeight);

        params.setWidth(screenWidth);
        params.setHeight(screenHeight);
        return params;
    }


    private ViewParams getPermissionWindowParams(Context context) {
        ViewParams params = new ViewParams();
        double screenWidth = SystemUtils.getScreenWidth(context.getApplicationContext()) * 0.8;
        double screenHeight = SystemUtils.getScreenHeight(context.getApplicationContext()) * 0.75;
        int statusBarHeight = SystemUtils.getStatusBarHeight(context.getApplicationContext());

        params.setScreenWidth(screenWidth);
        params.setScreenHeight(screenHeight);
        params.setStatusBarHeight(statusBarHeight);

        params.setWidth(screenWidth);
        params.setHeight(screenHeight);
        return params;
    }
}