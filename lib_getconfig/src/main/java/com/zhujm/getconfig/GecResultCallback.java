package com.zhujm.getconfig;

/**
 * @author: zhujm
 * @data: 2021/12/26
 * @description:
 **/
public interface GecResultCallback {
    void onResult(String result);

    void onError(String error);
}
