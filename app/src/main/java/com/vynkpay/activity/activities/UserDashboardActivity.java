package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDashboardActivity extends AppCompatActivity {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Dialog dialog;
    String url, title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_rcg);

        ButterKnife.bind(UserDashboardActivity.this);
        dialog = M.showDialog(UserDashboardActivity.this, "", false, false);

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

        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        getContent(url);
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            HashMap<String, String> hashMap=new HashMap<>();
            hashMap.put(ApiParams.access_token, Prefes.getAccessToken(UserDashboardActivity.this));
            view.loadUrl(url, hashMap);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dialog.dismiss();
        }
    }

    RequestQueue requestQueue;
    public void getContent(String url){
        dialog.show();
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(UserDashboardActivity.this);
        }

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                HashMap<String, String> hashMap=new HashMap<>();
                hashMap.put(ApiParams.access_token, Prefes.getAccessToken(UserDashboardActivity.this));
                webView.loadUrl(url, hashMap);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(ApiParams.access_token, Prefes.getAccessToken(UserDashboardActivity.this));
                return map;
            }
        };

        requestQueue.add(stringRequest);
    }
}
