package com.vynkpay.activity.activities.history.fragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.vynkpay.activity.activities.history.adapter.HistoryRechargesAdapter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.TransferMoneyModel;
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

public class RechargeHistoryFragment extends Fragment {
    View view;
    @BindView(R.id.sortBy)
    NormalTextView sortBy;
    @BindView(R.id.noLayoutData)
    LinearLayout noLayoutData;
    @BindView(R.id.noDataText)
    NormalTextView noDataText;
    @BindView(R.id.planRecyclerView)
    RecyclerView planRecyclerView;
    ArrayList<TransferMoneyModel> previousTransactionList = new ArrayList<>();
    Dialog dialog;
    HistoryRechargesAdapter previousTransferAdapter;
    JSONObject userPreference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plans_rcg, container, false);
        ButterKnife.bind(RechargeHistoryFragment.this, view);
        dialog = M.showDialog(getActivity(), "", false, false);
        sortBy.setVisibility(View.GONE);
        try {
            userPreference = new JSONObject(Prefes.getUserData(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        previousTransferData();
        previousTransferAdapter = new HistoryRechargesAdapter(getActivity(), previousTransactionList, 1, "recharge");
        planRecyclerView.setNestedScrollingEnabled(false);
        planRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        planRecyclerView.setAdapter(previousTransferAdapter);
        return view;
    }

    String mSuccess, mMessage;

    private void previousTransferData() {
        previousTransactionList.clear();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.recharge,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>transferMoney", "onResponse: " + response);
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject object = jsonObject.getJSONObject(ApiParams.data);
                                JSONArray jsonElements = object.getJSONArray("recharges");
                                for (int k = 0; k < jsonElements.length(); k++) {
                                    JSONObject jsonObject1 = jsonElements.getJSONObject(k);
                                    String amount = jsonObject1.getString(ApiParams.amount);
                                    String txn_status = jsonObject1.getString(ApiParams.status);
                                    String operator = jsonObject1.getString(ApiParams.operator);
                                    String id = jsonObject1.getString(ApiParams.id);
                                    String created_at = jsonObject1.getString(ApiParams.created_at);
                                    String conn_type = jsonObject1.getString(ApiParams.conn_type);
                                    String txn = jsonObject1.getString(ApiParams.txn);
                                    String mobileNumber = jsonObject1.has(ApiParams.mobile_number) ? jsonObject1.getString(ApiParams.mobile_number) : "";
                                    TransferMoneyModel transferMoneyModel = new TransferMoneyModel();
                                    transferMoneyModel.setDate(M.changeDateTimeFormat(created_at));
                                    transferMoneyModel.setAmount(amount);
                                    transferMoneyModel.setTcnId(txn);
                                    transferMoneyModel.setOperator(operator);
                                    transferMoneyModel.setStatus(txn_status);
                                    transferMoneyModel.setConn_type(conn_type);
                                    transferMoneyModel.setMobile(mobileNumber);
                                    transferMoneyModel.setId(id);
                                    previousTransactionList.add(transferMoneyModel);
                                }
                                previousTransferAdapter.notifyDataSetChanged();

                                if (previousTransactionList.size() > 0) {
                                    noLayoutData.setVisibility(View.GONE);
                                    planRecyclerView.setVisibility(View.VISIBLE);
                                } else {
                                    planRecyclerView.setVisibility(View.GONE);
                                    noLayoutData.setVisibility(View.VISIBLE);
                                    noDataText.setText("No Recharge/Bill history found");
                                }
                            } else {
                                planRecyclerView.setVisibility(View.GONE);
                                noLayoutData.setVisibility(View.VISIBLE);
                                noDataText.setText("No Recharge/Bill history found");
                            }
                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
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
                params.put(ApiParams.access_token, Prefes.getAccessToken(getActivity().getApplicationContext()));
                params.put("Accept", "application/json");
                return params;
            }

        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

}
