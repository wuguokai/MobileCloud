package com.mobilecloud.pojo.update;

/**
 * Created by hailor on 2017/6/30.
 */

public class BundleUpdatePojo {
    private Integer id;//每个模块的id
    private String name;
    private String targetVersion;

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

    public String getTargetVersion() {
        return targetVersion;
    }

    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }

    @Override
    public String toString() {
        return "BundleUpdatePojo{" +
                "name='" + name + '\'' +
                ", targetVersion='" + targetVersion + '\'' +
                '}';
    }
}
