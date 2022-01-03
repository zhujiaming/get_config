package com.zhujm.getconfig;

import android.content.Context;

import com.zhujm.getconfig.core.GecWebViewDataRequest;
import com.zhujm.getconfig.core.IDataRequest;
import com.zhujm.getconfig.test.CommonGecResultTester;
import com.zhujm.getconfig.test.GecResultTester;

/**
 * @author: zhujm
 * @data: 2021/12/26
 * @description:
 **/
public class GecTaskBuilder {
    private String script;
    private String targetUrl;
    private GecResultCallback resultCallback;
    private GecResultTester resultTester = new CommonGecResultTester();
    private long timeout = 5000l; //默认5s失败


    public String getScript() {
        return script;
    }

    public GecTaskBuilder setScript(String script) {
        this.script = script;
        return this;
    }

    public GecResultTester getResultTester() {
        return resultTester;
    }

    public GecTaskBuilder setResultTester(GecResultTester resultTester) {
        this.resultTester = resultTester;
        return this;
    }

    public GecTaskBuilder setResultCallback(GecResultCallback resultCallback) {
        this.resultCallback = resultCallback;
        return this;
    }

    public GecResultCallback getResultCallback() {
        return resultCallback;
    }


    public String getTargetUrl() {
        return targetUrl;
    }

    public GecTaskBuilder setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    public GecTaskBuilder setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    IDataRequest build(Context context) {
        return new GecWebViewDataRequest(context, this);
    }
}
