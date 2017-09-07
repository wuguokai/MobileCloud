package com.mobilecloud.common;

/**
 * Created by hailor on 2017/6/29.
 */

public interface HttpProcessCallBack {
    public void progress(float progress);
    public void success(Object object);
    public void failure(Object object);
}
