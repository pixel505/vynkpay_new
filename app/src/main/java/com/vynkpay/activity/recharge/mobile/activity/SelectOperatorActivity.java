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
import com.vynkpay.activity.recharge.mobile.adapter.PrepaidOperatorAdapter;
import com.vynkpay.activity.recharge.mobile.model.OperatorInnerModel;
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

public class SelectOperatorActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    NormalTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.operatorList)
    RecyclerView operatorRecyclerView;
    Dialog dialog;
    ArrayList<RechargeModel> oprtaorArrayList = new ArrayList<>();
    String type;
    PrepaidOperatorAdapter prepaidOperatorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_rcg);
        ButterKnife.bind(SelectOperatorActivity.this);
        dialog = M.showDialog(SelectOperatorActivity.this, "", true, true);
        type = getIntent().getStringExtra(ApiParams.type);
        Log.d("asdasd",type);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_title.setText("Operator");
        makeOperatorFetchNetworkCall();
        prepaidOperatorAdapter = new PrepaidOperatorAdapter(SelectOperatorActivity.this, oprtaorArrayList , type);
        operatorRecyclerView.setNestedScrollingEnabled(false);
        operatorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        operatorRecyclerView.setAdapter(prepaidOperatorAdapter);


    }

    String mSuccess, mMessage;

    ArrayList<OperatorInnerModel> operatorInnerModelArrayList = new ArrayList<>();

    private void makeOperatorFetchNetworkCall() {
        oprtaorArrayList.clear();
        operatorInnerModelArrayList.clear();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.operator + "?type=" + type,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>operatorList", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONArray j = jsonObject.getJSONArray(ApiParams.data);
                                for (int k = 0; k < j.length(); k++) {
                                    JSONObject jsonObject1 = j.getJSONObject(k);
                                    String operator = jsonObject1.getString("operator");
                                    String image_url = jsonObject1.getString("image_url");
                                    String operator_id = jsonObject1.getString("id");
                                    JSONArray jsonObject2 = jsonObject1.getJSONArray(ApiParams.operator_urls);
                                    RechargeModel rechargeModel = new RechargeModel();
                                    rechargeModel.setOperatorName(operator);
                                    rechargeModel.setOpratorImage(image_url.replaceAll("////", ""));
                                    rechargeModel.setOperatorId(operator_id);
                                    for (int l = 0; l < jsonObject2.length(); l++) {
                                        JSONObject object = jsonObject2.getJSONObject(l);
                                        String id = object.getString("id");
                                        String type = object.getString("type");
                                        String status = object.getString("status");
                                        OperatorInnerModel operatorInnerModel = new OperatorInnerModel();
                                        operatorInnerModel.setId(id);
                                        operatorInnerModel.setType(type);
                                        operatorInnerModel.setStatus(status);
                                        operatorInnerModelArrayList.add(operatorInnerModel);
                                    }
                                    rechargeModel.setOperatorInnerModelArrayList(operatorInnerModelArrayList);
                                    oprtaorArrayList.add(rechargeModel);
                                }
                                prepaidOperatorAdapter.notifyDataSetChanged();
                            } else {
                                M.dialogOk(SelectOperatorActivity.this, mMessage, "Error!");
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
                        Log.i(">>exception", "onErrorResponse: " + error.getMessage());
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
        MySingleton.getInstance(SelectOperatorActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
