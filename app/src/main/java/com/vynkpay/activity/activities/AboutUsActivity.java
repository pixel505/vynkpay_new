package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Dialog dialog;
    String url, title;
    int TIME_OUT = 3500;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_web_rcg);

        ButterKnife.bind(AboutUsActivity.this);
        dialog = M.showDialog(AboutUsActivity.this, "", false, false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        Intent intent=getIntent();
        String page="";
        String title="";
        if (intent!=null){
            if (intent.hasExtra("page")){
                page = intent.getStringExtra("page");
                title = intent.getStringExtra("title");
                toolbarTitle.setText(title);

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                dialog.show();

                webView.loadUrl(BuildConfig.BASE_URL + page+"?device=app");
            }
        }


        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                Log.d("nowURLLRR", url+"///");
                view.loadUrl(url);
                return false;
            }
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        };
        handler.postDelayed(runnable, TIME_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(AboutUsActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(AboutUsActivity.this,AboutUsActivity.this::finishAffinity);
        }
    }
}
