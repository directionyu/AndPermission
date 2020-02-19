package com.yanzhenjie.permission.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.permission.R;

/**
 * Description:
 *
 * @author: roy
 * Time: 2019-06-25 11:28
 * Modifier:
 * Fix Description:
 * Version:
 */
public class EnableNotificationAccessActivity extends Activity {

    ImageView mIcon;
    SwitchCompat mSwitchCompat;
    TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_notification_access);

        findViewById(R.id.layout_base).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableNotificationAccessActivity.this.finish();
            }
        });
        mIcon = findViewById(R.id.img_icon);
        mIcon.setImageDrawable(getApkIcon(this));

        mSwitchCompat =findViewById(R.id.bt_switch);
        mSwitchCompat.setChecked(true);

        mTextView =findViewById(R.id.roy_app_name);
        mTextView.setText(getApkNameRes(this));
    }


    public static Drawable getApkIcon(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);

            if (info != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                return appInfo.loadIcon(pm);

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("ApkIconLoader", e.toString());
        }
        return null;
    }

    public static int getApkNameRes(Context context){
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);

            if (info != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                return appInfo.labelRes;

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("ApkIconLoader", e.toString());
        }
        return -1;
    }

}
