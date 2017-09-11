package com.mobilecloud.pojo;

/**
 * Created by hailor on 2017/6/30.
 */

public class BundlePojo {
    private Integer id;
    private String name;
    private String currentVersion;
    private String path;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "BundlePojo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentVersion='" + currentVersion + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
