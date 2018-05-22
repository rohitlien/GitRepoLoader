package com.rohit.gitrepoloader.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rohit.gitrepoloader.R;
import com.rohit.gitrepoloader.utils.GitLoaderTools;

public class RepoWebActivity extends AppCompatActivity {

    private WebView myWebView;
    private RelativeLayout closeBtn, ll_progress;
    private TextView progress_txt;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_web);

        initViews();

    }

    private void initViews() {
        myWebView = (WebView) findViewById(R.id.myWebView);
        closeBtn = (RelativeLayout) findViewById(R.id.closeBtn);
        ll_progress = (RelativeLayout) findViewById(R.id.ll_progress);
        progress_txt = (TextView) findViewById(R.id.progress_txt);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
            if (url == null || url.isEmpty()) {
                GitLoaderTools.showToast(getResources().getString(R.string.error_msg));
                finish();
                return;
            }
        } else {
            GitLoaderTools.showToast(getResources().getString(R.string.error_msg));
            finish();
            return;
        }
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(url);
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                try {
                    progress_txt.setText("" + progress + "%");
                    if (progress > 97) {
                        ll_progress.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                }
            }
        });

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
