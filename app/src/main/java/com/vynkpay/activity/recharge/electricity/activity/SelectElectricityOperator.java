package com.vynkpay.activity.recharge.electricity.activity;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.vynkpay.R;
import com.vynkpay.activity.recharge.electricity.adapter.ElectricityCircleAdapter;
import com.vynkpay.activity.recharge.electricity.model.ElectricityModel;
import com.vynkpay.custom.NormalEditText;
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

public class SelectElectricityOperator extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    NormalTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.circleList)
    RecyclerView circleRecyclerView;
    Dialog dialog;
    ArrayList<ElectricityModel> circleArrayList = new ArrayList<>();
    ArrayList<ElectricityModel> _temp = new ArrayList<>();
    String type;
    ElectricityCircleAdapter circleRechargeAdapter;
    String operator, operator_id;
    @BindView(R.id.etSearchLayout)
    LinearLayout etSearchLayout;
    @BindView(R.id.etSearch)
    NormalEditText etSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_rcg);
        ButterKnife.bind(SelectElectricityOperator.this);
        type = getIntent().getStringExtra(ApiParams.type);
        operator_id = getIntent().getStringExtra("operator_id");
        Log.i(">>operator", "onCreate: " + operator_id);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (type.equalsIgnoreCase(ApiParams.type_electricty)) {
            etSearchLayout.setVisibility(View.VISIBLE);
        } else {
            etSearchLayout.setVisibility(View.GONE);
        }
        toolbar_title.setText("Select district/type");
        //operator_id  = getIntent().getStringExtra("operator_id");
        operator = getIntent().getStringExtra("operator");

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    filterResults(s.toString().toLowerCase());
                } else {
                    bindRecyclerView(circleArrayList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog = M.showDialog(SelectElectricityOperator.this, "", true, true);
        makeOperatorFetchNetworkCall();
        bindRecyclerView(circleArrayList);

    }

    private void filterResults(String query) {
        _temp.clear();
        for (int k = 0; k < circleArrayList.size(); k++) {
            if (circleArrayList.get(k).getDivision().toLowerCase().contains(query)) {
                _temp.add(circleArrayList.get(k));
            }
        }
        bindRecyclerView(_temp);
    }

    public void bindRecyclerView(ArrayList<ElectricityModel> receive) {
        circleRechargeAdapter = new ElectricityCircleAdapter(SelectElectricityOperator.this, receive, operator, type ,operator_id);
        circleRecyclerView.setNestedScrollingEnabled(false);
        circleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        circleRecyclerView.setAdapter(circleRechargeAdapter);
    }

    String mSuccess, mMessage;

    private void makeOperatorFetchNetworkCall() {
        circleArrayList.clear();
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.electricity_board_detail + "?operator_id=" + operator_id,
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
                                    String id = jsonObject1.getString(ApiParams.id);
                                    String operator_id = jsonObject1.getString(ApiParams.operator_idd);
                                    String state = jsonObject1.getString(ApiParams.state);
                                    String division = jsonObject1.getString(ApiParams.division);
                                    String code = jsonObject1.getString(ApiParams.code);
                                    String status = jsonObject1.getString(ApiParams.status);
                                    ElectricityModel rechargeModel = new ElectricityModel();
                                    rechargeModel.setId(id);
                                    rechargeModel.setOperator_id(operator_id);
                                    rechargeModel.setState(state);
                                    rechargeModel.setDivision(division);
                                    rechargeModel.setCode(code);
                                    rechargeModel.setStatus(status);
                                    circleArrayList.add(rechargeModel);
                                }
                                circleRechargeAdapter.notifyDataSetChanged();
                            } else {
                                M.dialogOk(SelectElectricityOperator.this, mMessage, "Error!");
                            }
                        } catch (Exception e) {
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
        MySingleton.getInstance(SelectElectricityOperator.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
