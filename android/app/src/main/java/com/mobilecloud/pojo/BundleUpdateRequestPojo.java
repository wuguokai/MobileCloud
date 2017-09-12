package com.mobilecloud.pojo;

/**
 * Created by hailor on 2017/7/1.
 */

public class BundleUpdateRequestPojo {
    private Integer appId;//app的id
    private String appName;
    private String appVersion;
    private String appUrl;
    private String name;
    private String targetVerson;
    private Integer bundleId;//模块的id
//    private Integer bundleVersionId;//模块对应版本的id

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

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

   /* public Integer getBundleVersionId() {
        return bundleVersionId;
    }

    public void setBundleVersionId(Integer bundleVersionId) {
        this.bundleVersionId = bundleVersionId;
    }*/

    public Integer getBundleId() {
        return bundleId;
    }

    public void setBundleId(Integer bundleId) {
        this.bundleId = bundleId;
    }

    public BundleUpdateRequestPojo(Integer appId, String appName, String appVersion, String appUrl, String name, String targetVerson, Integer bundleId/*, Integer bundleVersionId*/) {
        this.appId = appId;
        this.appName = appName;
        this.appVersion = appVersion;
        this.appUrl = appUrl;
        this.name = name;
        this.targetVerson = targetVerson;
        this.bundleId = bundleId;
//        this.bundleVersionId = bundleVersionId;
    }

    @Override
    public String toString() {
        return "BundleUpdateRequestPojo{" +
                "appId=" + appId +
                ", appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", appUrl='" + appUrl + '\'' +
                ", name='" + name + '\'' +
                ", targetVerson='" + targetVerson + '\'' +
//                ", bundleVersionId=" + bundleVersionId +
                '}';
    }
}
