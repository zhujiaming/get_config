package com.zhujm.getconfig.core;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.WeakHashMap;

/**
 * @author: zhujm
 * @data: 2021/12/26
 * @description:可复用webview实现
 **/
class WebViewCachePool {
    static WebViewCachePool _cachePool;

    static WebViewCachePool get() {
        if (_cachePool == null) {
            _cachePool = new WebViewCachePool();
        }
        return _cachePool;
    }

    ArrayList<WebView> pool = new ArrayList<>();

    /**
     * 获取webview
     * @param context
     * @return
     */
    WebView getWebView(Context context) {
        if (pool.isEmpty()) {
            pool.add(createWebView(new MutableContextWrapper(context)));
        }
        WebView webView = pool.remove(0);
        MutableContextWrapper mutableContextWrapper = (MutableContextWrapper) webView.getContext();
        mutableContextWrapper.setBaseContext(context);
        webView.clearHistory();
        webView.resumeTimers();
        return webView;
    }

    /**
     * 回收webview
     * @param webView
     */
    void recycle(WebView webView) {
        try {
            webView.stopLoading();
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            webView.pauseTimers();
            webView.setWebViewClient(null);
            webView.setWebChromeClient(null);
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!pool.contains(webView)) {
                pool.add(webView);
            }
        }

    }

    /**
     * 销毁webview
     */
    void destroy() {
        for (WebView webview : pool) {
            webview.removeAllViews();
            webview.destroy();
        }
        pool.clear();
    }

    WebView createWebView(Context context) {
        WebView webview = new WebView(context);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        return webview;
    }
 /*
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
*/
}
