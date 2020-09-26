package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.CashbackRewardModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CashbackRewardActivity extends AppCompatActivity {

    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.historyRecycler)
    RecyclerView historyRecycler;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.noLayout)
    LinearLayout noLayout;

    Dialog dialog1;
    String mSuccess, mMessage;
    CashbackRewardAdapter cashbackRewardAdapter;
    JSONObject userPreference;
    String userID;
    ArrayList<CashbackRewardModel> cashbackRewardModelArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashback_reward_rcg);
        dev();
    }

    private void dev() {
        ButterKnife.bind(CashbackRewardActivity.this);
        dialog1 = M.showDialog(CashbackRewardActivity.this, "", false, false);
        toolbarTitle.setText("Cashback History");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            userPreference = new JSONObject(Prefes.getUserData(CashbackRewardActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        previousTransferData();
    }


    private void previousTransferData() {
        dialog1.show();
        cashbackRewardModelArrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.cashback_reward,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("cashback_rewardLOGG", "onResponse: " + response);
                        try {
                            dialog1.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);

                                JSONArray jsonElements = j.getJSONArray("history");
                                for (int k = 0; k < jsonElements.length(); k++) {
                                    JSONObject jsonObject1 = jsonElements.getJSONObject(k);
                                    userID = jsonObject1.getString("user_id");
                                    String id = jsonObject1.getString("id");
                                    String user_id = jsonObject1.getString("user_id");
                                    String type = jsonObject1.getString("type");
                                    String amount = jsonObject1.getString("amount");
                                    String amount_of = jsonObject1.getString("amount_of");
                                    String profit_type = jsonObject1.getString("profit_type");
                                    String status = jsonObject1.getString("status");
                                    String created_date = jsonObject1.getString("created_date");
                                    String username = jsonObject1.getString("username");
                                    String email = jsonObject1.getString("email");
                                    String phone = jsonObject1.getString("phone");
                                    String full_name = jsonObject1.getString("full_name");
                                    String balance = jsonObject1.getString("balance");
                                    String detail = jsonObject1.getString("detail");
                                    cashbackRewardModelArrayList.add(new CashbackRewardModel( id,  user_id,  type,  amount,  amount_of,  profit_type,  status,  created_date,
                                            username,  email,  phone,  full_name,  balance,  detail));
                                }
                                if (cashbackRewardModelArrayList.size() > 0) {
                                    cashbackRewardAdapter = new CashbackRewardAdapter(CashbackRewardActivity.this, cashbackRewardModelArrayList);
                                    historyRecycler.setNestedScrollingEnabled(false);
                                    historyRecycler.setLayoutManager(new LinearLayoutManager(CashbackRewardActivity.this));
                                    historyRecycler.setAdapter(cashbackRewardAdapter);
                                    cashbackRewardAdapter.notifyDataSetChanged();
                                    historyRecycler.setVisibility(View.VISIBLE);
                                    noLayout.setVisibility(View.GONE);
                                }else {
                                    historyRecycler.setVisibility(View.GONE);
                                    noLayout.setVisibility(View.VISIBLE);
                                }
                            }else {
                                historyRecycler.setVisibility(View.GONE);
                                noLayout.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                            dialog1.dismiss();
                            historyRecycler.setVisibility(View.GONE);
                            noLayout.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog1.dismiss();
                        historyRecycler.setVisibility(View.GONE);
                        noLayout.setVisibility(View.VISIBLE);
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(CashbackRewardActivity.this));
                params.put("Accept", "application/json");
                return params;
            }
        };

        MySingleton.getInstance(CashbackRewardActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
