package com.vynkpay.activity.activitiesnew.loadmcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import com.vynkpay.R;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class CoinbaseActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    Dialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_coinbase);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        dialog1 = M.showDialog(CoinbaseActivity.this, "", false, false);
        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbarTitle.setText("Payment");
        Intent intent=getIntent();
        if (getIntent().getStringExtra("url") != null) {
            WebView browser = findViewById(R.id.webView);
            browser.getSettings().setJavaScriptEnabled(true);
            // Set WebView client
            browser.setWebChromeClient(new WebChromeClient());
            browser.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.d("urll",url);
                    if (url.contains("http")){
                        if (url.contains("app_success")){
                            //call api here and finish
                            //paidUserCall(pID);
                            String _str = url;
                            if (_str.contains("=")) {
                                String message = _str.substring(_str.indexOf("=")+1,_str.length());
                                Toast.makeText(CoinbaseActivity.this, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CoinbaseActivity.this,LoadMcashSuccessActivity.class).putExtra("message",message.replace("%20"," ")));
                                CoinbaseActivity.this.finish();
                            }
                        }else if (url.contains("app_notFound")){
                            String _str = url;
                            if (_str.contains("=")) {
                                String message = _str.substring(_str.indexOf("=")+1,_str.length());
                                Toast.makeText(CoinbaseActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        }else {
                            view.loadUrl(url);
                            return false;
                        }
                    }
                    return true;
                }
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    if (url.contains("http")){
                        super.onPageStarted(view, url, favicon);
                    }else {
                        view.goBack();
                    }
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    if (url.contains("http")){
                        super.onPageFinished(view, url);
                    }
                }
                @Override
                public void onLoadResource(WebView view, String url) {
                    // TODO Auto-generated method stub
                    super.onLoadResource(view, url);
                }
            });
            browser.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                    Log.d("onJsAlertdata", "" + url+"  11");
                    Log.d("onJsAlertdata", "" + result.toString()+" 22");
                    Log.d("onJsAlertdata", "" + message+" 33");
                    return super.onJsAlert(view, url, message, result);
                }
            });
            // Load the webpage
            browser.loadUrl(getIntent().getStringExtra("url"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(CoinbaseActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(CoinbaseActivity.this,CoinbaseActivity.this::finishAffinity);
        }
    }
}