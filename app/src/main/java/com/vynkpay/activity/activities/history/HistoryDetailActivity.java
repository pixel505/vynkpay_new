package com.vynkpay.activity.activities.history;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    NormalTextView toolbar_title;
    @BindView(R.id.tvDate)
    NormalTextView tvDate;
    @BindView(R.id.tvMobileNumber)
    NormalTextView tvMobileNumber;
    @BindView(R.id.tvBalance)
    NormalTextView tvBalance;
    @BindView(R.id.tvType)
    NormalTextView tvType;
    @BindView(R.id.tvStatus)
    NormalTextView tvStatus;
    @BindView(R.id.tvDescription)
    NormalTextView tvDescription;
    @BindView(R.id.tvRemark)
    NormalTextView tvRemark;
    @BindView(R.id.tvValidity)
    NormalTextView tvValidity;
    @BindView(R.id.tvPlan)
    NormalTextView tvPlan;
    @BindView(R.id.tvMobileNumberTc)
    NormalTextView tvMobileNumberTc;
    @BindView(R.id.whatIsFor)
    NormalTextView whatIsFor;
    @BindView(R.id.llValidityLayout)
    LinearLayout llValidityLayout;
    @BindView(R.id.llPlanLayout)
    LinearLayout llPlanLayout;
    JSONObject userPreference;
    String _TXN = "", _ID = "", _TITLE = "";
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail_rcg);
        ButterKnife.bind(HistoryDetailActivity.this);
        dialog = M.showDialog(HistoryDetailActivity.this, "", false, false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        try {
            userPreference = new JSONObject(Prefes.getUserData(HistoryDetailActivity.this));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        _TXN = getIntent().getStringExtra("txn");
        _ID = getIntent().getStringExtra("id");
        _TITLE = getIntent().getStringExtra("title");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        whatIsFor.setText(_TITLE);
        toolbar_title.setText("TXN ID - " + _TXN);
        fetchRechargeDetails();
    }

    String mSuccess, mMessage;

    private void fetchRechargeDetails() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.recharge + "/" + _ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>detailOfHistory", "onResponse: " + response);
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject object = jsonObject.getJSONObject(ApiParams.data).getJSONObject(ApiParams.recharges);
                                tvDate.setText(M.changeDateTimeFormat(object.getString(ApiParams.created_at)));
                                if (object.getString(ApiParams.status).equalsIgnoreCase("1")) {
                                    tvStatus.setText("Successful");
                                    tvStatus.setTextColor(getResources().getColor(R.color.green_status));
                                } else if (object.getString(ApiParams.status).equalsIgnoreCase("2")) {
                                    tvStatus.setText("Failed");
                                    tvStatus.setTextColor(getResources().getColor(R.color.color_red));
                                } else if (object.getString(ApiParams.status).equalsIgnoreCase("3")) {
                                    tvStatus.setText("Pending");
                                    tvStatus.setTextColor(getResources().getColor(R.color.color_red));
                                }
                                if (object.getString(ApiParams.description).equalsIgnoreCase("")) {
                                    tvDescription.setText("NA");
                                } else {
                                    tvDescription.setText(object.getString(ApiParams.description));
                                }
                                if (object.getString(ApiParams.plan_name).equalsIgnoreCase("")) {
//                                    if (object.getString(ApiParams.conn_type).equalsIgnoreCase(ApiParams.type_for_prepaid)
//                                            || (object.getString(ApiParams.conn_type).equalsIgnoreCase(ApiParams.type_for_postpaid))) {
//                                        tvPlan.setText("Recharge of " + (object.getString(ApiParams.operator)));
//                                    } else {
//                                        tvPlan.setText("Bill payment of " + (object.getString(ApiParams.operator)));
//                                    }
                                    llPlanLayout.setVisibility(View.GONE);
                                } else {
                                    tvPlan.setText(object.getString(ApiParams.plan_name));
                                }
                                if (object.getString(ApiParams.plan_validity).equalsIgnoreCase("")) {
                                    llValidityLayout.setVisibility(View.GONE);
                                } else {
                                    tvValidity.setText(object.getString(ApiParams.plan_validity).toLowerCase());
                                }
                                tvRemark.setText(object.getString("remark"));
                                if (object.getString(ApiParams.label).equalsIgnoreCase("")) {
                                    if (object.getString(ApiParams.conn_type).equalsIgnoreCase(ApiParams.type_for_prepaid) || object.getString(ApiParams.conn_type).equalsIgnoreCase(ApiParams.type_for_datacard_prepaid)) {
                                        tvMobileNumberTc.setText(object.getString(ApiParams.operator) + "(Prepaid)");
                                    } else if (object.getString(ApiParams.conn_type).equalsIgnoreCase(ApiParams.type_for_postpaid) || object.getString(ApiParams.conn_type).equalsIgnoreCase(ApiParams.type_for_datacard_postpaid)) {
                                        tvMobileNumberTc.setText(object.getString(ApiParams.operator) + "(Postpaid)");
                                    } else {
                                        tvMobileNumberTc.setText(object.getString(ApiParams.operator));
                                    }
                                } else {
                                    if (object.getString(ApiParams.conn_type).equalsIgnoreCase(ApiParams.type_for_prepaid) || object.getString(ApiParams.conn_type).equalsIgnoreCase(ApiParams.type_for_datacard_prepaid)) {
                                        tvMobileNumberTc.setText(object.getString(ApiParams.label) + "(Prepaid)");
                                    } else if (object.getString(ApiParams.conn_type).equalsIgnoreCase(ApiParams.type_for_postpaid) || object.getString(ApiParams.conn_type).equalsIgnoreCase(ApiParams.type_for_datacard_postpaid)) {
                                        tvMobileNumberTc.setText(object.getString(ApiParams.label) + "(Postpaid)");
                                    } else {
                                        tvMobileNumberTc.setText(object.getString(ApiParams.label));
                                    }
                                }
                                tvBalance.setText(Functions.CURRENCY_SYMBOL + object.getString("amount"));
                                tvType.setText(object.getString(ApiParams.conn_type).equals("1") ? "Prepaid" : "Postpaid");
                                tvMobileNumber.setText(object.getString(ApiParams.mobile_number));

                            } else {
                                M.dialogOk(HistoryDetailActivity.this, mMessage, "Error");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            dialog.dismiss();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(HistoryDetailActivity.this));
                params.put("Accept", "application/json");
                return params;
            }

        };
        MySingleton.getInstance(HistoryDetailActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
}
