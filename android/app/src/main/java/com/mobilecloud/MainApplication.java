package com.mobilecloud;

import android.app.Activity;
import android.app.Application;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.ReactRootView;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.mobilecloud.common.BundleManager;
import com.mobilecloud.ext.ExtReactApplication;
import com.mobilecloud.react.modules.NativePackage;
import com.reactnative.ivpusic.imagepicker.PickerPackage;
import com.rt2zz.reactnativecontacts.ReactNativeContacts;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WUGUOKAI on 2017/8/28.
 */

public class MainApplication extends Application implements ExtReactApplication/*, ReactApplication*/ {

    private  final  Map<String, ReactNativeHost> mReactNativeHostMap = new HashMap<String, ReactNativeHost>();

    private  final Map<String, Activity> activityHashMap = new HashMap<String, Activity>();
    private  final Map<String, String> activityPathHashMap = new HashMap<String, String>();

    @Override
    public ReactNativeHost getReactNativeHost(Activity reactActivity) {
        final String activityKey = reactActivity.getClass().getName();
        if(activityHashMap.get(activityKey)==null){
          activityHashMap.put(activityKey,reactActivity);
        }
        try {
            BundleManager bundleManager = BundleManager.getBundleManager();
            String token = bundleManager.getAppPojo(this).getToken();
            if(token != null ){
                bundleManager.setToken(token);
            }
        }catch (NullPointerException npe){
            npe.printStackTrace();
        }


        String bundleName = reactActivity.getIntent().getStringExtra("bundleName");
        ReactNativeHost reactNativeHost ;
        if (reactActivity instanceof MainActivity){
            reactNativeHost = mReactNativeHostMap.get(activityKey);
        }else {
            reactNativeHost = mReactNativeHostMap.get(bundleName);
        }

        if (reactNativeHost != null) {
            return reactNativeHost;
        } else {
          if(reactActivity instanceof MainActivity){
            final String mainBundle = BundleManager.getBundleManager().loadMainBundle(this);
            mReactNativeHostMap.put(activityKey, new ReactNativeHost(this) {
              @Override
              public boolean getUseDeveloperSupport() {
                return BuildConfig.DEBUG;
              }

              @Override
              protected List<ReactPackage> getPackages() {
                return Arrays.<ReactPackage>asList(
                        new MainReactPackage(),
                            new ReactNativeContacts(),
                            new PickerPackage(),
                            new NativePackage()
                );
              }
              @Override
              protected String getJSBundleFile() {
                return mainBundle;
              }
              @Override
              protected String getJSMainModuleName() {
                return null;
              }
            });
              return mReactNativeHostMap.get(activityKey);
          }else{
                mReactNativeHostMap.put(bundleName, new ReactNativeHost(this) {
                    @Override
                    public boolean getUseDeveloperSupport() {
                    return BuildConfig.DEBUG;
                    }

                    @Override
                    protected List<ReactPackage> getPackages() {
                        return Arrays.<ReactPackage>asList(
                            new MainReactPackage(),
                                new PickerPackage(),
                                new NativePackage(),
                                new ReactNativeContacts()
                    );
                    }
                    @Override
                    protected String getJSBundleFile() {
                        return activityPathHashMap.get(activityKey);
                    }
                    @Override
                    protected String getJSMainModuleName() {
                        return null;
                    }
                });
              return mReactNativeHostMap.get(bundleName);
          }
        }
    }


    public Activity getActivity(String activityKey){
        return activityHashMap.get(activityKey);
    }

    public void setActivity(String activityKey,String path){
        activityPathHashMap.put(activityKey,path);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
    }



  //已经重写，不需要这块代码
   /* private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(), new NativePackage()
            );
        }
    };*/

   /* @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }*/
}
