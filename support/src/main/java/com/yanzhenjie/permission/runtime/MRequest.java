/*
 * Copyright © Zhenjie Yan
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
package com.yanzhenjie.permission.runtime;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;
import com.yanzhenjie.permission.bridge.BridgeRequest;
import com.yanzhenjie.permission.bridge.RequestManager;
import com.yanzhenjie.permission.checker.DoubleChecker;
import com.yanzhenjie.permission.checker.PermissionChecker;
import com.yanzhenjie.permission.checker.StandardChecker;
import com.yanzhenjie.permission.source.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by YanZhenjie on 2016/9/9.
 */
class MRequest implements PermissionRequest, RequestExecutor, BridgeRequest.Callback {

    private static final PermissionChecker STANDARD_CHECKER = new StandardChecker();
    private static final PermissionChecker DOUBLE_CHECKER = new DoubleChecker();

    private Source mSource;

    private List<String> mPermissions;
    private Rationale<List<String>> mRationale = new Rationale<List<String>>() {
        @Override
        public void showRationale(Context context, List<String> data, RequestExecutor executor) {
            executor.execute();
        }
    };
    private Action<List<String>> mGranted;
    private Action<List<String>> mDenied;

    private String[] mDeniedPermissions;

    MRequest(Source source) {
        this.mSource = source;
    }

    @Override
    public PermissionRequest permission(String... permissions) {
        mPermissions = new ArrayList<>();
        mPermissions.addAll(Arrays.asList(permissions));
        return this;
    }

    @Override
    public PermissionRequest rationale(Rationale<List<String>> rationale) {
        this.mRationale = rationale;
        return this;
    }

    @Override
    public PermissionRequest onGranted(Action<List<String>> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public PermissionRequest onDenied(Action<List<String>> denied) {
        this.mDenied = denied;
        return this;
    }

    @Override
    public void start() {
        mPermissions = filterPermissions(mPermissions);

        List<String> deniedList = getDeniedPermissions(STANDARD_CHECKER, mSource, mPermissions);
        mDeniedPermissions = deniedList.toArray(new String[deniedList.size()]);
        if (mDeniedPermissions.length > 0) {
            List<String> rationaleList = getRationalePermissions(mSource, mDeniedPermissions);
            if (rationaleList.size() > 0) {
                mRationale.showRationale(mSource.getContext(), rationaleList, this);
            } else {
                execute();
            }
        } else {
            onCallback();
        }
    }

    /**
     * Filter the permissions you want to apply; remove unsupported and duplicate permissions.
     */
    public static List<String> filterPermissions(List<String> permissions) {
        permissions = new ArrayList<>(new HashSet<>(permissions));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            permissions.remove(Permission.READ_PHONE_NUMBERS);
            permissions.remove(Permission.ANSWER_PHONE_CALLS);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            permissions.remove(Permission.ACTIVITY_RECOGNITION);
            permissions.remove(Permission.ACCESS_BACKGROUND_LOCATION);
        }
        return permissions;
    }

    @Override
    public void execute() {
        BridgeRequest request = new BridgeRequest(mSource);
        request.setType(BridgeRequest.TYPE_PERMISSION);
        request.setPermissions(mDeniedPermissions);
        request.setCallback(this);
        RequestManager.get().add(request);
    }

    @Override
    public void cancel() {
        onCallback();
    }

    @Override
    public void onCallback() {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                return getDeniedPermissions(DOUBLE_CHECKER, mSource, mPermissions);
            }

            @Override
            protected void onPostExecute(List<String> deniedList) {
                if (deniedList.isEmpty()) {
                    callbackSucceed(mPermissions);
                } else {
                    callbackFailed(deniedList);
                }
            }

        }.execute();

    }

    /**
     * Callback acceptance status.
     */
    final void callbackSucceed(List<String> grantedList) {
        if (mGranted != null) {
            mGranted.onAction(grantedList);
        }
    }


    /**
     * Callback rejected state.
     */
    private void callbackFailed(List<String> deniedList) {
        if (mDenied != null) {
            mDenied.onAction(deniedList);
        }
    }

    public static List<String> getDeniedPermissions(PermissionChecker checker, Source source, List<String> permissions) {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (!checker.hasPermission(source.getContext(), permission)) {
                deniedList.add(permission);
            }
        }
        return deniedList;
    }

    /**
     * Get permissions to show rationale.
     */
    private static List<String> getRationalePermissions(Source source, String... permissions) {
        List<String> rationaleList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (source.isShowRationalePermission(permission)) {
                rationaleList.add(permission);
            }
        }
        return rationaleList;
    }
}