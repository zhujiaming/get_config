package com.zhujm.getconfig.test;

import android.text.TextUtils;

/**
 * @author: zhujm
 * @data: 2021/12/28
 * @description:
 **/
public class CommonGecResultTester implements GecResultTester {
    @Override
    public boolean test(String value) {
        return !TextUtils.isEmpty(value) && !TextUtils.equals("null", value) && !TextUtils.equals("\"\"", value);
    }
}
