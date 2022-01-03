package com.zhujm.getconfig.core;

import android.content.Context;
import android.webkit.WebView;

import java.util.WeakHashMap;

/**
 * @author: zhujm
 * @data: 2021/12/26
 * @description:
 **/
class WebViewCachePool {
    static WebViewCachePool _cachePool;

    static WebViewCachePool get() {
        if (_cachePool == null) {
            _cachePool = new WebViewCachePool();
        }
        return _cachePool;
    }

    WeakHashMap<Context, WebView> webViewCachePool = new WeakHashMap<>();

    WebView getWebView(Context context) {
        WebView webView = webViewCachePool.get(context);
        if (webView != null) {
            return webView;
        } else {
            WebView wv = new WebView(context);
            webViewCachePool.put(context, wv);
            return wv;
        }
    }

    void removeWebView(Context context) {
        webViewCachePool.remove(context);
    }

}
