package com.wta.NewCloudApp.jiuwei99986.views.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.wta.NewCloudApp.jiuwei99986.Constants;
import com.wta.NewCloudApp.jiuwei99986.R;
import com.wta.NewCloudApp.jiuwei99986.base.BaseActivity;
import com.wta.NewCloudApp.jiuwei99986.utils.SharedPrefrenceManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 小小程序员 on 2017/9/15.
 */

public class WebActivity extends BaseActivity {
    @BindView(R.id.toolbar_back)
    ImageView toolbarBack;
    @BindView(R.id.tool_content)
    TextView toolContent;
    @BindView(R.id.webView)
    WebView webView;
    private String url;
    @Override
    protected int getLayoutId() {
        return R.layout.webview;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initWebView();

    }

    private void initWebView() {
        url= Constants.WEB_URL+getIntent().getStringExtra(Constants.TID);
        if (SharedPrefrenceManager.getInstance().getIsLogin()){
            url+="&"+Constants.USERID+"="+SharedPrefrenceManager.getInstance().getUserId();
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadingView();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                showContentView();
            }
        });
        webView.loadUrl(url);
    }

    @OnClick(R.id.toolbar_back)
    public  void onViewClick(View view){
        switch (view.getId()){
            case R.id.toolbar_back:
                finish();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.clearCache(true); //清空缓存
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }
}
