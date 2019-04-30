package com.release.mvvm.ui.web_detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.release.mvvm.BR;
import com.release.mvvm.R;
import com.release.mvvm.databinding.ActivityEventDetailBinding;
import com.release.mvvm.ui.base.BaseActivity;
import com.release.mvvm.utils.LogUtils;

import static com.release.mvvm.ui.base.Constants.RECOMMEND_CTIME;
import static com.release.mvvm.ui.base.Constants.RECOMMEND_DESC;
import static com.release.mvvm.ui.base.Constants.RECOMMEND_HTML;
import static com.release.mvvm.ui.base.Constants.RECOMMEND_TITLE;

/**
 * @author Mr.release
 * @create 2019/4/25
 * @Describe
 */

public class WebDetailActivity extends BaseActivity<ActivityEventDetailBinding, WebDetailViewModel> {

    private String TAG = WebDetailActivity.class.getSimpleName();


    private WebSettings mWebSettings;
    boolean isConnected = true;//TODO：模拟网络连接

    public static void start(Context context, String title, String cTime, String desc, String html) {

        Intent intent = new Intent(context, WebDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(RECOMMEND_TITLE, title);
        intent.putExtra(RECOMMEND_CTIME, cTime);
        intent.putExtra(RECOMMEND_DESC, desc);
        intent.putExtra(RECOMMEND_HTML, html);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_right_entry, R.anim.hold);
    }

    @Override
    public int getLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_event_detail;
    }

    @Override
    public void initView() {
        initConfig();
        initWebView();
        initData();
    }


    @Override
    public void updateViews(boolean isRefresh) {
        showLoading();
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    public void initData() {

        String html = viewModel.setIntentData(getIntent());
        if (!TextUtils.isEmpty(html)) {
            binding.webView.loadUrl(html);
        }
    }

    private void initWebView() {
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                LogUtils.d(TAG, "shouldOverrideUrlLoading: ");
                return false;
            }

        });

        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                // 网页加载完成
                if (newProgress == 100)
                    hideLoading();
            }
        });

        binding.webView.addJavascriptInterface(new JsCallAndroid() {
            @JavascriptInterface
            @Override
            public void showActivity(String clazz) {
//                int userId = (int) SPUtil.getParam(Constants.USER_ID, 0);
//                if (userId == 0) {
//                    Toast.makeText(WebDetailActivity.this, getString(R.string.please_login), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                Intent intent = new Intent();
//                intent.setClassName(getPackageName(), getPackageName() + "." + clazz);
//                startActivity(intent);
//                finish();
            }
        }, "button");

    }


    public interface JsCallAndroid {
        public void showActivity(String clazz);
    }


    private void initConfig() {

        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//开启硬件加速
        mWebSettings = binding.webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setPluginState(WebSettings.PluginState.ON);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setDisplayZoomControls(false);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");

        //自适应屏幕
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);


        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        if (isConnected) {
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + "cacheWebView";
        mWebSettings.setAppCachePath(cacheDirPath);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebSettings.setJavaScriptEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebSettings.setJavaScriptEnabled(false);
    }

    @Override
    protected void onDestroy() {
        if (binding.webView != null) {
            binding.webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            binding.webView.clearHistory();

            ((ViewGroup) binding.webView.getParent()).removeView(binding.webView);
            binding.webView.destroy();
        }
        super.onDestroy();
    }
}
