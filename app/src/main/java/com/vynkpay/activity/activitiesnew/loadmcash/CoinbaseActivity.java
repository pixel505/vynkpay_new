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
import com.vynkpay.activity.activities.WebviewActivityNew;
import com.vynkpay.utils.M;

public class CoinbaseActivity extends AppCompatActivity {

    Dialog dialog1;
    //https://www.mlm.pixelsoftwares.com/vynkpay/account/coinBaseAppWebView/app_success?ud=Payment%20verify%20successfully%20and%20added%20to%20Wallet
    //https://www.mlm.pixelsoftwares.com/vynkpay/account/coinBaseAppWebView/app_notFound?ud=You%20have%20paid%20a%20less%20amount%20than%20requested.%20Please%20send%20full%20amount%20or%20contact%20support

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                                //String _str = "https://www.mlm.pixelsoftwares.com/vynkpay/account/coinBaseAppWebView/app_success?ud=Payment%20verify%20successfully%20and%20added%20to%20Wallet";
                                //String message = _str.substring(_str.indexOf("=")+1,_str.length());
                                Toast.makeText(CoinbaseActivity.this, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CoinbaseActivity.this,LoadMcashSuccessActivity.class).putExtra("message",message.replace("%20"," ")));
                                CoinbaseActivity.this.finish();
                            }
                        }else if (url.contains("app_notFound")){
                            String _str = url;
                            if (_str.contains("=")) {
                                String message = _str.substring(_str.indexOf("=")+1,_str.length());
                                //String _str = "https://www.mlm.pixelsoftwares.com/vynkpay/account/coinBaseAppWebView/app_success?ud=Payment%20verify%20successfully%20and%20added%20to%20Wallet";
                                //String message = _str.substring(_str.indexOf("=")+1,_str.length());
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
}