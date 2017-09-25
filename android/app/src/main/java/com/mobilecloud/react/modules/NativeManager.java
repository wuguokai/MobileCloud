package com.mobilecloud.react.modules;


import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.mobilecloud.MainApplication;
import com.mobilecloud.SecondActivity;
import com.mobilecloud.common.BundleManager;
import com.mobilecloud.common.HttpProcessCallBack;
import com.mobilecloud.pojo.AppPojo;
import com.mobilecloud.pojo.BundlePojo;
import com.mobilecloud.pojo.BundleUpdateRequestPojo;
import com.mobilecloud.pojo.update.AppUpdatePojo;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.mobilecloud.preload.PreLoadBundle;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WUGUOKAI on 2017/8/28.
 */

@ReactModule(name = "UpdateAndroid")
public class NativeManager extends ReactContextBaseJavaModule {

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
    private static Toast toast = null;


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

    //点击子模块
    @ReactMethod
    public void openBundle(final String name, /*Integer id,*/ Callback callback) {
        final AppPojo appPojo = BundleManager.getBundleManager().getAppPojo(this.getCurrentActivity().getApplication());
        final AppUpdatePojo appUpdatePojo = BundleManager.getBundleManager().getAppUpdatePojo(this.getCurrentActivity().getApplication());
        if (appPojo.getBundles().get(name) != null) {
            if(appUpdatePojo == null){
                callback.invoke("netError");
            }else if (appUpdatePojo.getBundlesUpdate().get(name) != null) {
                //提示更新子模块
                callback.invoke("update");
            } else {
                //cache的子模块是最新版本，直接打开
                ((MainApplication) getCurrentActivity().getApplication()).setActivity(SecondActivity.class.getName(), appPojo.getBundles().get(name).getPath());
//                SecondActivity storedActivity = (SecondActivity) ((MainApplication) getCurrentActivity().getApplication()).getActivity(SecondActivity.class.getName());
//                Log.w("activity", getCurrentActivity().getLocalClassName());
                /*if (storedActivity != null) {
                    BundleManager.getBundleManager().loadBundle(storedActivity, new File(appPojo.getBundles().get(name).getPath()));
                }*/
                ReactApplicationContext context = getReactApplicationContext();
                Intent intent = new Intent(context, SecondActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("bundleName", name);
                context.startActivity(intent);
            }
        } else {
            //提示下载子模块
            callback.invoke("new");
        }
    }

    //下载并打开子模块
    @ReactMethod
    public void downloadAndOpenBundle(String name, Integer bundleId, final Callback callback) {
        final AppPojo appPojo = BundleManager.getBundleManager().getAppPojo(this.getCurrentActivity().getApplication());
        final AppUpdatePojo appUpdatePojo = BundleManager.getBundleManager().getAppUpdatePojo(this.getCurrentActivity().getApplication());
        String targetVersion = "0";
        if(appUpdatePojo == null){
            callback.invoke("netError");
        }else if (appUpdatePojo.getBundlesUpdate().get(name) != null) {
            //更新子模块
            targetVersion = appUpdatePojo.getBundlesUpdate().get(name).getTargetVersion();
        }
        BundleUpdateRequestPojo bundleUpdateRequestPojo = new BundleUpdateRequestPojo(appPojo.getId(), appPojo.getName(), appPojo.getCurrentVersion(), appPojo.getUrl(), name, targetVersion, bundleId);
        //调用updateBundle下载bundle
        BundleManager.getBundleManager().updateBundle(bundleUpdateRequestPojo, this.getCurrentActivity().getApplication(), new HttpProcessCallBack() {
            @Override
            public void progress(float progress) {
                Log.w("NativeManager", "+++++"+String.format("%.2f", progress*100));
                Log.w("NativeManager", "+-+-+"+String.format("%f", progress));
                if(toast == null){
                    toast = Toast.makeText(getReactApplicationContext(), String.format("%.2f",progress*100)+'%', Toast.LENGTH_SHORT);
                }else{
                    if(progress!=100.000000){
                        toast.setText(String.format("%.2f",progress*100)+'%');
                    }
                }
                toast.show();
            }

            @Override
            public void success(Object object) {
                final File file = (File) object;
                Log.w("NativeManager", String.format("%s", file.getAbsolutePath()));
                //BundleManager.getBundleManager().loadBundle(getCurrentActivity(),file);
                if(toast == null){
                    toast = Toast.makeText(getReactApplicationContext(), String.format("download success"), Toast.LENGTH_SHORT);
                }else{
                    toast.setText(String.format("download success"));
                }
                callback.invoke("success", file.getAbsolutePath());
            }

            @Override
            public void failure(Object object) {
                callback.invoke("failed", "downLoad failed!");
                Log.w("NativeManager", ((Exception) object).getMessage());
            }
        });
    }



    /*@ReactMethod
    public void show(String message, int duration) {
        ReactApplicationContext context = getReactApplicationContext();
        Intent intent = new Intent(context, SecondActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }*/


    @ReactMethod
    public void checkMainUpdateAble(final Callback callback) {
        final AppPojo appPojo = BundleManager.getBundleManager().getAppPojo(this.getCurrentActivity().getApplication());
        final AppUpdatePojo appUpdatePojo = BundleManager.getBundleManager().getAppUpdatePojo(this.getCurrentActivity().getApplication());
        if (appUpdatePojo != null){
            if (appUpdatePojo.getMainBundleUpdate() != null){
                callback.invoke("本地版本："+appPojo.getMainBundle().getCurrentVersion()+",远程版本："+appUpdatePojo.getMainBundleUpdate().getTargetVersion()+"。");
            }
        }else {
            callback.invoke("netError");
        }
    }

    @ReactMethod
    public void updateMain(final Callback callback) {
        final AppPojo appPojo = BundleManager.getBundleManager().getAppPojo(this.getCurrentActivity().getApplication());
        final AppUpdatePojo appUpdatePojo = BundleManager.getBundleManager().getAppUpdatePojo(this.getCurrentActivity().getApplication());
        Integer bundleId = appPojo.getMainBundle().getId();
//        Integer bundleVersionId = appUpdatePojo.getMainBundleUpdate().getBundleVersionId();
        BundleUpdateRequestPojo bundleUpdateRequestPojo = new BundleUpdateRequestPojo(appPojo.getId(), appPojo.getName(), appPojo.getCurrentVersion(), appPojo.getUrl(), appPojo.getMainBundle().getName(), appUpdatePojo.getMainBundleUpdate().getTargetVersion(), bundleId);
        BundleManager.getBundleManager().updateBundle(bundleUpdateRequestPojo, this.getCurrentActivity().getApplication(), new HttpProcessCallBack() {

            public void progress(float progress) {
                Log.w("NativeManager", "+++++"+String.format("%.2f", progress*100));
                Log.w("NativeManager", "+-+-+"+String.format("%f", progress));
                if(toast == null){
                    toast = Toast.makeText(getReactApplicationContext(), String.format("%.2f",progress*100)+'%', Toast.LENGTH_SHORT);
                }else{
                    if(progress!=100.000000){
                        toast.setText(String.format("%.2f",progress*100)+'%');
                    }
                }
                toast.show();
            }

            @Override
            public void success(Object object) {
                final File file = (File) object;
                Log.w("NativeManager", String.format("%s", file.getAbsolutePath()));
                //BundleManager.getBundleManager().loadBundle(getCurrentActivity(),file);
                if(toast == null){
                    toast = Toast.makeText(getReactApplicationContext(), String.format("download success"), Toast.LENGTH_SHORT);
                }else{
                    toast.setText(String.format("download success"));
                }
                BundleManager.getBundleManager().loadBundle(getCurrentActivity(), file);
                callback.invoke("success", file.getAbsolutePath());
            }

            @Override
            public void failure(Object object) {
                callback.invoke("failed", "downLoad failed!");
                Log.w("NativeManager", ((Exception) object).getMessage());
            }
        });
    }

    @ReactMethod
    public void downloadIcon(final Callback callback){
        final AppPojo appPojo = BundleManager.getBundleManager().getAppPojo(this.getCurrentActivity().getApplication());
        String zipPath = BundleManager.getBundleManager().downloadIcon(appPojo, this.getCurrentActivity().getApplication());

        if (zipPath != null) {
            String path = zipPath.replace("icon.zip", "");
            callback.invoke(path);
        }
    }

    /**
     * 获取本地信息
     *
     * @param promise
     */
    @ReactMethod
    public void getLocalData(final Promise promise){
        final AppPojo appPojo = BundleManager.getBundleManager().getAppPojo(this.getCurrentActivity().getApplication());
        Map<String, BundlePojo> bundlePojoMap = appPojo.getBundles();
        String bundles = "";
        for (Map.Entry<String, BundlePojo> bundlePojoEntry:bundlePojoMap.entrySet()){
            String bundle = "{id:"+bundlePojoEntry.getValue().getId()+",name:"+bundlePojoEntry.getValue().getName();
            bundles = bundles+bundle;
        }

        Log.i("bundlePojoList", bundles);
        promise.resolve(bundles);
    }

    @ReactMethod
    public void getConfigData(final Promise promise){
        final AppPojo appPojo = BundleManager.getBundleManager().getAppPojo(this.getCurrentActivity().getApplication());
        WritableMap writableMap = Arguments.createMap();

        String serverUrl = appPojo.getUrl();
        writableMap.putString("serverUrl", serverUrl);

        String mainPath = appPojo.getMainBundle().getPath();
        String appPath = mainPath.replace("index/index.android.bundle", "");
        String iconPath = appPath+"icon/";
        writableMap.putString("iconPath", iconPath);

        promise.resolve(writableMap);
    }
}
