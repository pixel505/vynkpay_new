package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityWatchPdfBinding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class WatchPdfActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    ActivityWatchPdfBinding binding;
    String pdfLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_watch_pdf);
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.supportchat);


        //show progress bar
        binding.porgressBar.setVisibility(View.VISIBLE);



        //get link of pdf file from intent
        pdfLink = getIntent().getStringExtra("pdfLink");
        Log.e("pdf",""+pdfLink);

        //find id of webview
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);


        //show pdf file in webview
        binding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+pdfLink);
        binding.webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {

                //after page is done loading hide the progress bar
                binding.porgressBar.setVisibility(View.GONE);
            }
        });

        //find ids of back button


    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(WatchPdfActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(WatchPdfActivity.this,WatchPdfActivity.this::finishAffinity);
        }
    }
}