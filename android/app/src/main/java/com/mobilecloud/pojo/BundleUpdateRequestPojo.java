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
    public int bundleId;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetVerson() {
        return targetVerson;
    }

    public void setTargetVerson(String targetVerson) {
        this.targetVerson = targetVerson;
    }

    public int getBundleId() {
        return bundleId;
    }

    public void setBundleId(int bundleId) {
        this.bundleId = bundleId;
    }

    public BundleUpdateRequestPojo(String appName, String appVersion, String appUrl, String name, String targetVerson, int bundleId) {
        this.appName = appName;
        this.appVersion = appVersion;
        this.appUrl = appUrl;
        this.name = name;
        this.targetVerson = targetVerson;
        this.bundleId = bundleId;
    }
}
