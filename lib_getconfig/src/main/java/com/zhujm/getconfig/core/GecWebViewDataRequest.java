package com.zhujm.getconfig.core;

import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhujm.getconfig.GecTaskBuilder;
import com.zhujm.getconfig.GetConfig;
import com.zhujm.getconfig.utils.Logg;

/**
 * @author: zhujm
 * @data: 2021/12/26
 * @description:
 **/
public class GecWebViewDataRequest implements IDataRequest {
    private static final String TAG = GetConfig.TAG + ":" + "DataRequest";
    private WebView mWebView;
    private Handler mHandler;
    private GecTaskBuilder mRequestBuilder;
    private long tryStartMill;
    private long delayTryInterval = 500;

    public GecWebViewDataRequest(Context context, GecTaskBuilder requestBuilder) {
        mHandler = new Handler(Looper.getMainLooper());
        mRequestBuilder = requestBuilder;
        mWebView = WebViewCachePool.get().getWebView(context);
        Logg.d(TAG, "webview:" + (mWebView == null ? "null" : mWebView.hashCode()));
        initRequester();
    }

    private void initRequester() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mWebView.setWebViewClient(new DRWebViewClient());
    }

    @Override
    public void request() {
        Logg.d(TAG, "request");
        tryStartMill = System.currentTimeMillis();
        mWebView.loadUrl(mRequestBuilder.getTargetUrl());
    }

    @Override
    public void release() {
        Logg.d(TAG, "release");
        WebViewCachePool.get().removeWebView(mWebView.getContext());
        mWebView.stopLoading();
        mWebView.destroy();
        mWebView = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    private void postOnResult(final String result) {
        if (mHandler == null) return;
        mHandler.removeCallbacksAndMessages(null);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mRequestBuilder.getResultCallback().onResult(result);
            }
        });
        mHandler = null;
    }

    private void postOnError(final String error) {
        if (mHandler == null) return;
        mHandler.removeCallbacksAndMessages(null);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mRequestBuilder.getResultCallback().onError(error);
            }
        });
        mHandler = null;
    }

    private void postDelayTryScript() {
        if (mHandler == null) return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tryScript();
            }
        }, delayTryInterval);
    }

    private void tryScript() {
        Logg.d(TAG, "tryScript");
        mWebView.evaluateJavascript(mRequestBuilder.getScript(), new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (mRequestBuilder.getResultTester().test(value)) {
                    Logg.d(TAG, "postOnResult:" + value);
                    postOnResult(value);
                } else {
                    boolean isTimeout = isTimeout();
                    if (isTimeout) {
                        postOnError("timeout");
                    } else {
                        postDelayTryScript();
                    }
                    Logg.d(GetConfig.TAG, "value is null " + isTimeout);
                }
            }
        });
    }

    private boolean isTimeout() {
        return System.currentTimeMillis() > tryStartMill + mRequestBuilder.getTimeout();
    }

    private class DRWebViewClient extends WebViewClient {
        private boolean onPageFinish = false;

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (onPageFinish) {
                return;
            }
            onPageFinish = true;
            Logg.d(TAG, "onPageFinished:" + url);
            tryScript();
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            Logg.d(TAG, "onReceivedHttpError:" + request.getUrl());
            postOnError("onReceivedHttpError:" + errorResponse.getReasonPhrase() + " " + errorResponse.getStatusCode());
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Logg.d(TAG, "onReceivedError:" + request.getUrl());
            String errorMsg = "onReceivedError";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                errorMsg += (error.getDescription() + " " + error.getErrorCode());
            }
            postOnError(errorMsg);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Logg.d(TAG, "shouldOverrideUrlLoading:" + request.getUrl());
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    }
}
