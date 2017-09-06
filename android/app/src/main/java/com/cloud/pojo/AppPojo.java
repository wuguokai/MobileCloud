package com.cloud.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hailor on 2017/6/30.
 */

public class AppPojo {
    public String name;
    public String version;
    public String url;
    public Boolean updateAble;
    public String targetVersion;
    public BundlePojo indexBundle ;
    public BundlePojo mainBundle;
    public Map<String,BundlePojo> bundles= new HashMap<String,BundlePojo>();
}
