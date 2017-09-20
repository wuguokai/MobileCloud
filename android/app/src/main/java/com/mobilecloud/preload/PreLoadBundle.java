package com.mobilecloud.preload;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.facebook.react.ReactRootView;
import com.mobilecloud.SecondActivity;
import com.mobilecloud.ext.ExtReactApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WUGUOKAI on 2017/9/20.
 */

public class PreLoadBundle {
    private static final Map<String, ReactRootView> BUNDLE_CACHE = new HashMap<>();

    public static void preLoad(Activity activity, String appKey, String bundleName){
        ReactRootView reactRootView = BUNDLE_CACHE.get(bundleName);
        if (reactRootView == null){
            reactRootView = new ReactRootView(activity);
            reactRootView.startReactApplication(
                    ((ExtReactApplication) activity.getApplication()).getReactNativeHost(activity).getReactInstanceManager(),
                    appKey,
                    null);
            BUNDLE_CACHE.put(bundleName, reactRootView);
        }
    }

    public static ReactRootView getRootView(String bundleName){
        return BUNDLE_CACHE.get(bundleName);
    }

    public static void destoryViewGroup(String bundleName){
        try {
            ReactRootView rootView = BUNDLE_CACHE.get(bundleName);
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } catch (Throwable e) {
            Log.e("ReactNativePreLoader", e.getMessage());
        }
    }
}
