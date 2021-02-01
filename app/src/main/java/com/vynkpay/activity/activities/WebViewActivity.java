package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
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

        ButterKnife.bind(WebViewActivity.this);
        dialog = M.showDialog(WebViewActivity.this, "", false, false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbarTitle.setText(title);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        dialog.show();

        if (title.equalsIgnoreCase("Train Booking")){
            webView.loadUrl("https://www.irctc.co.in/nget/train-search");
        }else {
            webView.loadUrl(BuildConfig.WEB_URL + url);
        }

      /*  webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d("onJSAlertLOGGG=", "url="+url);
                Log.d("onJSAlertLOGGG=", "message="+message);
                Log.d("onJSAlertLOGGG=", "result="+result.toString());
                return true;
            }
        });
*/
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                return false; // then it is not handled by default action
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
        MySingleton.getInstance(WebViewActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(WebViewActivity.this,WebViewActivity.this::finishAffinity);
        }
    }
}
