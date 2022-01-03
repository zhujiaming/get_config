package com.zhujm.getconfig;

import android.content.Context;
import android.webkit.WebView;

import com.zhujm.getconfig.BuildConfig;
import com.zhujm.getconfig.core.IDataRequest;

/**
 * @author: zhujm
 * @data: 2021/12/25
 * @description:
 **/
public class GetConfig {
    public static final String TAG = "G_E_T_C";
    private Context mContext;
    private IDataRequest dataRequester;
    private static boolean debug = BuildConfig.DEBUG;

    public static void setDebug(boolean debug) {
        GetConfig.debug = debug;
        WebView.setWebContentsDebuggingEnabled(debug);
    }

    public GetConfig(Context mContext) {
        this.mContext = mContext;
    }

    public void destroy() {
        if (this.dataRequester != null) {
            this.dataRequester.release();
        }
    }

    public void process(GecTaskBuilder requestBuilder) {
        this.dataRequester = requestBuilder.build(mContext);
        this.dataRequester.request();
    }
}
