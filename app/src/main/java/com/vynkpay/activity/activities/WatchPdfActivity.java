package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityWatchPdfBinding;

public class WatchPdfActivity extends AppCompatActivity {
    ActivityWatchPdfBinding binding;
    String pdfLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}