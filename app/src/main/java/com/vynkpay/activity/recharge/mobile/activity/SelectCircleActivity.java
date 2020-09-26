package com.vynkpay.activity.recharge.mobile.activity;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.recharge.mobile.adapter.CircleRechargeAdapter;
import com.vynkpay.activity.recharge.mobile.model.RechargeModel;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectCircleActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    NormalTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.circleList)
    RecyclerView circleRecyclerView;
    Dialog dialog;
    ArrayList<RechargeModel> circleArrayList = new ArrayList<>();
    String type;
    CircleRechargeAdapter circleRechargeAdapter;
    String operator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_rcg);
        ButterKnife.bind(SelectCircleActivity.this);
        type = getIntent().getStringExtra(ApiParams.type);
        Log.d("asdasd",type);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_title.setText("Circle");
        operator = getIntent().getStringExtra("operator");
        //operator_id  = getIntent().getStringExtra("operator_id");
        dialog = M.showDialog(SelectCircleActivity.this, "", true, true);
        makeOperatorFetchNetworkCall();
        circleRechargeAdapter = new CircleRechargeAdapter(SelectCircleActivity.this, circleArrayList, operator,type);
        circleRecyclerView.setNestedScrollingEnabled(false);
        circleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        circleRecyclerView.setAdapter(circleRechargeAdapter);


    }

    String mSuccess, mMessage;
    String append;

    private void makeOperatorFetchNetworkCall() {
        circleArrayList.clear();
        dialog.show();
        if (type.equalsIgnoreCase("1") || type.equalsIgnoreCase("2")) {
            append = URLS.circle;
        } else if (type.equalsIgnoreCase("6") || type.equalsIgnoreCase("7")) {
            append = URLS.datacard_circle;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + append,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>changePassResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONArray j = jsonObject.getJSONArray(ApiParams.data);
                                for (int k = 0; k < j.length(); k++) {
                                    JSONObject jsonObject1 = j.getJSONObject(k);
                                    String circle_id = jsonObject1.getString("id");
                                    String circleName = jsonObject1.getString("name");
                                    String isactive = jsonObject1.getString("isactive");
                                    RechargeModel rechargeModel = new RechargeModel();
                                    rechargeModel.setCircleName(circleName);
                                    rechargeModel.setCircleId(circle_id);
                                    rechargeModel.setCircleId(circle_id);
                                    rechargeModel.setIsCricleActive(isactive);
                                    circleArrayList.add(rechargeModel);
                                }
                                circleRechargeAdapter.notifyDataSetChanged();
                            } else {
                                M.dialogOk(SelectCircleActivity.this, mMessage, "Error!");
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
                        dialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(ApiParams.type, type);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        MySingleton.getInstance(SelectCircleActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
