package com.mobilecloud.pojo;

/**
 * Created by hailor on 2017/7/1.
 */

public class BundleUpdateRequestPojo {
    public String appName;
    public String appVersion;
    public String appUrl;
    public String name;
    public String targetVerson;
    public Long bundleId;

    public BundleUpdateRequestPojo(String appName, String appVersion, String appUrl, String name, String targetVerson, Long bundleId) {
        this.appName = appName;
        this.appVersion = appVersion;
        this.appUrl = appUrl;
        this.name = name;
        this.targetVerson = targetVerson;
        this.bundleId = bundleId;
    }
}
