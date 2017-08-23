package com.uma.umar.ui.webview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.uma.umar.ui.BaseActivity;
import com.uma.umar.R;

public class WebViewActivity extends BaseActivity {

    private static final String LICENSE_URL = "file:///android_asset/open_source_licenses.html";

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView mWebView = (WebView) findViewById(R.id.licenses_webview);
        mWebView.loadUrl(LICENSE_URL);
    }
}
