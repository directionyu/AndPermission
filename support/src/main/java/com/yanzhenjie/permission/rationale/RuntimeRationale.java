package com.yanzhenjie.permission.rationale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

/**
 * Description:
 *
 * @author: roy
 * Time: 2018/8/16 下午2:41
 * Modifier:
 * Fix Description:
 * Version:
 */
public class RuntimeRationale implements Rationale<List<String>> {

    @Override
    public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
        if (context instanceof Activity && !((Activity) context).isDestroyed()) {
            new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle("Tips")
                    .setMessage("We need these permissions to run the application properly.")
                    .setPositiveButton("Resume", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executor.execute();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executor.cancel();
                        }
                    })
                    .show();

        }

    }
}
