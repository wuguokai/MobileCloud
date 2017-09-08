package com.mobilecloud.pojo.update;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hailor on 2017/6/30.
 */

public class AppUpdatePojo {
    public String name;
    public String currentVersion;
    public Boolean updateAble;
    public String url;
    public String targetVersion;
    public BundleUpdatePojo indexBundleUpdate ;
    public BundleUpdatePojo mainBundleUpdate;
    public Map<String,BundleUpdatePojo> bundles= new HashMap<String,BundleUpdatePojo>();
}
