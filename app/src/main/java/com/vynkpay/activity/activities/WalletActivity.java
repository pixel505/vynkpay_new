package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.adapter.CashbackRewardAdapter;
import com.vynkpay.adapter.WalletTransactionAdapter;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;
import com.vynkpay.utils.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    @BindView(R.id.addMoneyButton)
    NormalButton addMoneyButton;
    @BindView(R.id.startNowButton)
    NormalButton startNowButton;
    @BindView(R.id.addNewCardButton)
    NormalTextView addNewCardButton;
    @BindView(R.id.existingCardRecycler)
    RecyclerView existingCardRecycler;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.walletBalance)
    NormalTextView walletBalance;
    @BindView(R.id.noLayout)
    LinearLayout noLayout;
    @BindView(R.id.transactionsListView)
    ListView transactionsListView;
    @BindView(R.id.noDataTV)
    NormalTextView noDataTV;

    JSONObject userPreference;
    Dialog dialog1;
    String mSuccess, mMessage;
    String userID;
    CashbackRewardAdapter cashbackRewardAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_wallet_rcg);
        ButterKnife.bind(WalletActivity.this);
        dialog1 = M.showDialog(WalletActivity.this, "", false, false);
        //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
        setListeners();
        toolbarTitle.setText("Wallet");
        try {
            userPreference = new JSONObject(Prefes.getUserData(WalletActivity.this));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fetchWalletData(Prefes.getAccessToken(WalletActivity.this));

        View header = LayoutInflater.from(WalletActivity.this).inflate(R.layout.view_all_header_layout, null);
        LinearLayout viewAll = header.findViewById(R.id.viewAll);

        transactionsListView.addHeaderView(header);

        if (transactionsListView.getHeaderViewsCount()>1){
            transactionsListView.removeHeaderView(header);
        }

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllTransactionsActivity.walletTransactionsModelArrayList = walletTransactionsModelArrayList;
                startActivity(new Intent(WalletActivity.this, AllTransactionsActivity.class).putExtra("tabType", "mCash"));
            }
        });

        getTransactionsHistory(viewAll);

    }


    private void fetchWalletData(final String accessToken) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.wallet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>walletData", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data);
                                String balance = jsonObject1.getString("balance");
                                walletBalance.setText(Functions.CURRENCY_SYMBOL + balance);

                            } else {
                                M.dialogOk(WalletActivity.this, mMessage, "Error");
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
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, accessToken);
                return params;
            }

        };
        MySingleton.getInstance(WalletActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setListeners() {
        startNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WalletActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });


        addMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.launchActivity(WalletActivity.this, AddMoneyActivity.class);
            }
        });
    }

    public static ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList = new ArrayList<>();
    public void getTransactionsHistory(LinearLayout viewAll){
        walletTransactionsModelArrayList.clear();
        ApiCalls.getMcashTransactions(WalletActivity.this, Prefes.getAccessToken(WalletActivity.this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        JSONObject dataObject=jsonObject.getJSONObject("data");

                        JSONArray listingArray = dataObject.getJSONArray("listing");
                        for (int i=0; i<listingArray.length(); i++){
                            JSONObject object=listingArray.getJSONObject(i);
                            String id = object.getString("id");
                            String front_user_id = object.getString("front_user_id");
                            String user_id  = object.getString("user_id");
                            String type  = object.getString("type");
                            String payment_via = object.getString("payment_via");
                            String p_amount  = object.getString("p_amount");
                            String profit_type = object.getString("profit_type");
                            String mode  = object.getString("mode");
                            String transactionStatus  = object.getString("status");
                            String created_date  = object.getString("created_date");
                            String username = object.getString("username");
                            String email  = object.getString("email");
                            String phone  = object.getString("phone");
                            String name  = object.getString("name");
                            String paid_status  = object.getString("paid_status");
                            String balance  = object.getString("balance");
                            String frontusername = object.getString("frontusername");

                            walletTransactionsModelArrayList.add(new WalletTransactionsModel( id,  front_user_id,  user_id,  type,
                                    payment_via, p_amount,  profit_type,  mode,  transactionStatus,  created_date,  username,
                                    email,  phone,  name,  paid_status,  balance, frontusername));
                        }

                        Collections.reverse(walletTransactionsModelArrayList);

                        transactionsListView.setAdapter(new WalletTransactionAdapter(WalletActivity.this, walletTransactionsModelArrayList, false));
                    }

                    if (walletTransactionsModelArrayList.size()>5){
                        viewAll.setVisibility(View.VISIBLE);
                    }else {
                        viewAll.setVisibility(View.GONE);
                    }

                    if (walletTransactionsModelArrayList.size()>0){
                        noLayout.setVisibility(View.GONE);
                    }else {
                        noLayout.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(WalletActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(WalletActivity.this,WalletActivity.this::finishAffinity);
        }
    }
}
