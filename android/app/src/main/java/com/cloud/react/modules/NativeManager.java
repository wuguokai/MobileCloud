package com.cloud.react.modules;


import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cloud.MainApplication;
import com.cloud.SecondActivity;
import com.cloud.common.BundleManager;
import com.cloud.common.HttpProcessCallBack;
import com.cloud.pojo.AppPojo;
import com.cloud.pojo.BundleUpdateRequestPojo;
import com.cloud.pojo.update.AppUpdatePojo;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.module.annotations.ReactModule;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hailor on 2017/6/28.
 */

@ReactModule(name = "UpdateAndroid")
public class NativeManager extends ReactContextBaseJavaModule {

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";


    public NativeManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "NativeManager";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }


    @ReactMethod
    public void show(String message, int duration) {
        ReactApplicationContext context = getReactApplicationContext();
        Intent intent = new Intent(context, SecondActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    //点击子模块
    @ReactMethod
    public void openBundle(String name, Callback callback) {
        final AppPojo appPojo = BundleManager.getBundleManager().getAppPojo(this.getCurrentActivity().getApplication());
        final AppUpdatePojo appUpdatePojo = BundleManager.getBundleManager().getAppUpdatePojo(this.getCurrentActivity().getApplication());
        if (appPojo.bundles.get(name) != null) {
            if (appUpdatePojo.bundles.get(name) != null) {
                //提示更新子模块
                callback.invoke("update");
            } else {
                //cache的子模块是最新版本，直接打开
                ((MainApplication) getCurrentActivity().getApplication()).setActivity(SecondActivity.class.getName(), appPojo.bundles.get(name).path);
                SecondActivity storedActivity = (SecondActivity) ((MainApplication) getCurrentActivity().getApplication()).getActivity(SecondActivity.class.getName());
                ReactApplicationContext context = getReactApplicationContext();
                Intent intent = new Intent(context, SecondActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                if (storedActivity != null) {
                    BundleManager.getBundleManager().loadBundle(storedActivity, new File(appPojo.bundles.get(name).path));
                }
            }
        } else {
            //提示下载子模块
            callback.invoke("new");
        }
    }

    //下载并打开子模块
    @ReactMethod
    public void downloadAndOpenBundle(String name, final Callback callback) {
        final AppPojo appPojo = BundleManager.getBundleManager().getAppPojo(this.getCurrentActivity().getApplication());
        final AppUpdatePojo appUpdatePojo = BundleManager.getBundleManager().getAppUpdatePojo(this.getCurrentActivity().getApplication());
        String targetVersion = "0";
        if (appUpdatePojo.bundles.get(name) != null) {
            //更新子模块
            targetVersion = appUpdatePojo.bundles.get(name).targetVersion;
        }
        BundleUpdateRequestPojo bundleUpdateRequestPojo = new BundleUpdateRequestPojo(appPojo.name, appPojo.version, appPojo.url, name, targetVersion);
        //调用updateBundle下载bundle
        BundleManager.getBundleManager().updateBundle(bundleUpdateRequestPojo, this.getCurrentActivity().getApplication(), new HttpProcessCallBack() {
            @Override
            public void progress(float progress) {
                Log.w("NativeManager", String.format("%f", progress));
                //Toast.makeText(getReactApplicationContext(), String.format("%f",progress)+"%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(Object object) {
                final File file = (File) object;
                Log.w("NativeManager", String.format("%s", file.getAbsolutePath()));
                //BundleManager.getBundleManager().loadBundle(getCurrentActivity(),file);
                callback.invoke("success", file.getAbsolutePath());
            }

            @Override
            public void failure(Object object) {
                Log.w("NativeManager", ((Exception) object).getMessage());
            }
        });
    }


    @ReactMethod
    public void checkMainUpdateAble(final Callback callback) {
        final AppPojo appPojo = BundleManager.getBundleManager().getAppPojo(this.getCurrentActivity().getApplication());
        //final AppUpdatePojo appUpdatePojo = BundleManager.getBundleManager().getAppUpdatePojo(this.getCurrentActivity().getApplication());
        BundleManager.getBundleManager().checkBundleConfigUpdate(this.getCurrentActivity().getApplication(), appPojo, new HttpProcessCallBack() {

            @Override
            public void progress(float progress) {

            }

            @Override
            public void success(Object object) {
                AppUpdatePojo appUpdatePojoResult = (AppUpdatePojo) object;
                if (appUpdatePojoResult.mainBundleUpdate != null) {
                    WritableMap resultData = new WritableNativeMap();
                    resultData.putString("name", appPojo.mainBundle.name);
                    resultData.putString("path", appPojo.mainBundle.path);
                    resultData.putString("version", appPojo.mainBundle.version);
                    callback.invoke(resultData, appUpdatePojoResult.mainBundleUpdate.targetVersion);
                }
            }

            @Override
            public void failure(Object object) {

            }
        });
    }

    @ReactMethod
    public void updateMain(final Callback callback) {
        final AppPojo appPojo = BundleManager.getBundleManager().getAppPojo(this.getCurrentActivity().getApplication());
        final AppUpdatePojo appUpdatePojo = BundleManager.getBundleManager().getAppUpdatePojo(this.getCurrentActivity().getApplication());
        BundleUpdateRequestPojo bundleUpdateRequestPojo = new BundleUpdateRequestPojo(appPojo.name, appPojo.version, appPojo.url, appPojo.mainBundle.name, appUpdatePojo.mainBundleUpdate.targetVersion);
        BundleManager.getBundleManager().updateBundle(bundleUpdateRequestPojo, this.getCurrentActivity().getApplication(), new HttpProcessCallBack() {
            @Override
            public void progress(float progress) {
                Log.w("NativeManager", String.format("%f", progress));
                //Toast.makeText(getReactApplicationContext(), String.format("%f",progress)+"%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(Object object) {
                final File file = (File) object;
                Log.w("NativeManager", String.format("%s", file.getAbsolutePath()));
                BundleManager.getBundleManager().loadBundle(getCurrentActivity(), file);
                callback.invoke("success", file.getAbsolutePath());
            }

            @Override
            public void failure(Object object) {
                Log.w("NativeManager", ((Exception) object).getMessage());
            }
        });
    }


}
