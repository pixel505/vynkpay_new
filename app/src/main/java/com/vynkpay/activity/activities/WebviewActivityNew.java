package com.vynkpay.activity.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.PayResponse;
import com.vynkpay.utils.M;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WebviewActivityNew extends AppCompatActivity {
    Dialog dialog1;
    String pID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_webview_new);
        dialog1 = M.showDialog(WebviewActivityNew.this, "", false, false);
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
        if (intent!=null){
            pID = intent.getStringExtra("packageID");
            Log.d("sdsdasdPAckagID", pID+"");
        }
        if (getIntent().getStringExtra("url") != null) {
            WebView browser = findViewById(R.id.webView);
            browser.getSettings().setJavaScriptEnabled(true);
            // Set WebView client
            browser.setWebChromeClient(new WebChromeClient());
            browser.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.contains("http")){
                        if (url.contains("app_success")){
                            //call api here and finish
                            paidUserCall(pID);
                        }else if (url.contains("app_notFound")){
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
    public void paidUserCall(String packageID){
        dialog1.show();
        MainApplication.getApiService().pay(Prefes.getAccessToken(WebviewActivityNew.this), packageID).enqueue(new Callback<PayResponse>() {
            @Override
            public void onResponse(Call<PayResponse> call, Response<PayResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dialog1.dismiss();
                    Toast.makeText(WebviewActivityNew.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(WebviewActivityNew.this, HomeActivity.class));
                    finishAffinity();
                }
            }
            @Override
            public void onFailure(Call<PayResponse> call, Throwable t) {
                Log.d("Error", t.getMessage() != null ? t.getMessage( ) : "Error");
                dialog1.dismiss();
            }
        });
    }
}