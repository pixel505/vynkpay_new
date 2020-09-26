package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.events.UpdateBalanceEvent;
import com.vynkpay.fragment.BankTransferFragment;
import com.vynkpay.fragment.WalletTransferFragment;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoneyTransferActivity extends AppCompatActivity {
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;

    @BindView(R.id.walletTV)
    NormalTextView walletTV;

    @BindView(R.id.bankTV)
    NormalTextView bankTV;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bankLayout)
    LinearLayout bankLayout;
    @BindView(R.id.walletLayout)
    LinearLayout walletLayout;
    Dialog dialog;
    JSONObject userPreference;
    @BindView(R.id.walletBalance)
    NormalTextView walletBalance;
    String mSuccess, mMessage;

    private void fetchWalletData() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.wallet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>walletData", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data);
                                String balance = jsonObject1.getString(ApiParams.balance);
                                double planAmount = Double.parseDouble(balance);
                                walletBalance.setText("Wallet Balance : " + Functions.CURRENCY_SYMBOL + planAmount);
                            } else {
                                M.dialogOk(MoneyTransferActivity.this, mMessage, "Error");
                            }
                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            M.dialogOk(MoneyTransferActivity.this, "Please check internet connection", "Error");
                        } else if (error instanceof AuthFailureError) {
                            M.dialogOk(MoneyTransferActivity.this, "You are not authorized", "Error");
                        } else if (error instanceof ServerError) {
                            M.dialogOk(MoneyTransferActivity.this, "Server issue", "Error");
                        } else if (error instanceof NetworkError) {
                            M.dialogOk(MoneyTransferActivity.this, "Please check internet connection", "Error");
                        } else if (error instanceof ParseError) {
                            M.dialogOk(MoneyTransferActivity.this, "Internal error", "Error");
                        }
                        dialog.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(MoneyTransferActivity.this));
                return params;
            }
        };
        MySingleton.getInstance(MoneyTransferActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void inflateFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money_rcg);
        ButterKnife.bind(MoneyTransferActivity.this);
        EventBus.getDefault().register(this);
        dialog = M.showDialog(MoneyTransferActivity.this, "", false, false);
        //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
        setListener();
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle.setText("Transfer Money");
        try {
            userPreference = new JSONObject(Prefes.getUserData(MoneyTransferActivity.this));
            Log.i(">>access_token", "onCreate: " + userPreference.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fetchWalletData();
       // inflateFragment(new WalletTransferFragment());

        bankLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_yellow));
        walletTV.setTextColor(ContextCompat.getColor(MoneyTransferActivity.this, R.color.white));
        bankTV.setTextColor(ContextCompat.getColor(MoneyTransferActivity.this, R.color.colorPrimary));
        walletLayout.setBackgroundDrawable(null);
        inflateFragment(new BankTransferFragment());
    }

    public void setListener() {
        walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_yellow));
                bankLayout.setBackgroundDrawable(null);
                bankTV.setTextColor(ContextCompat.getColor(MoneyTransferActivity.this, R.color.white));
                walletTV.setTextColor(ContextCompat.getColor(MoneyTransferActivity.this, R.color.colorPrimary));
                inflateFragment(new WalletTransferFragment());
            }
        });

        bankLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_yellow));
                walletTV.setTextColor(ContextCompat.getColor(MoneyTransferActivity.this, R.color.white));
                bankTV.setTextColor(ContextCompat.getColor(MoneyTransferActivity.this, R.color.colorPrimary));
                walletLayout.setBackgroundDrawable(null);
                inflateFragment(new BankTransferFragment());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(UpdateBalanceEvent event) {
        if (event != null) {
            walletBalance.setText("Wallet Balance : " + Functions.CURRENCY_SYMBOL + event.getBalance());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
