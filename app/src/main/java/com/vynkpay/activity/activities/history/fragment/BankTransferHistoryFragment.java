package com.vynkpay.activity.activities.history.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.vynkpay.adapter.DmtHistoryAdapter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.CashbackRewardModel;
import com.vynkpay.models.DmtHistoryModel;
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

public class BankTransferHistoryFragment extends Fragment {

    JSONObject userPreference;
    View view;
    @BindView(R.id.sortBy)
    NormalTextView sortBy;
    @BindView(R.id.noLayoutData)
    LinearLayout noLayoutData;
    @BindView(R.id.noDataText)
    NormalTextView noDataText;
    @BindView(R.id.planRecyclerView)
    RecyclerView planRecyclerView;
   // PreviousTransferAdapter previousTransferAdapter;
    //ArrayList<TransferMoneyModel> previousTransactionList = new ArrayList<>();
    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plans_rcg, container, false);
        ButterKnife.bind(BankTransferHistoryFragment.this, view);
        sortBy.setVisibility(View.GONE);
        dialog = M.showDialog(getActivity(), "", false, false);
        try {
            userPreference = new JSONObject(Prefes.getUserData(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        noDataText.setText("Bank transfer history is not found");

        gettingDMTHistory();

        return view;
    }

    String mSuccess, mMessage;
    ArrayList<CashbackRewardModel> cashbackRewardModelArrayList = new ArrayList<>();
    CashbackRewardAdapter cashbackRewardAdapter;
    private void previousTransferData() {
        dialog.show();
        cashbackRewardModelArrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.transactionHistory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("cashback_rewardLOGG", "onResponse: " + response);
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);

                                JSONArray jsonElements = j.getJSONArray("history");
                                for (int k = 0; k < jsonElements.length(); k++) {
                                    JSONObject jsonObject1 = jsonElements.getJSONObject(k);
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
                                    cashbackRewardAdapter = new CashbackRewardAdapter(getActivity(), cashbackRewardModelArrayList);
                                    planRecyclerView.setNestedScrollingEnabled(false);
                                    planRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    planRecyclerView.setAdapter(cashbackRewardAdapter);
                                    cashbackRewardAdapter.notifyDataSetChanged();
                                    noLayoutData.setVisibility(View.GONE);
                                    planRecyclerView.setVisibility(View.VISIBLE);
                                }else {
                                    noDataText.setText(mMessage);
                                    noLayoutData.setVisibility(View.VISIBLE);
                                    planRecyclerView.setVisibility(View.GONE);
                                }
                            }

                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                            dialog.dismiss();
                            noDataText.setText(mMessage);
                            noLayoutData.setVisibility(View.VISIBLE);
                            planRecyclerView.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        noDataText.setText(mMessage);
                        noLayoutData.setVisibility(View.VISIBLE);
                        planRecyclerView.setVisibility(View.GONE);
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


    Activity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=getActivity();
    }

    //getting dmt history
    ArrayList<DmtHistoryModel> dmtHistoryModelArrayList=new ArrayList<>();
    private void gettingDMTHistory() {
        dmtHistoryModelArrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.dmt_history_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        M.longLog("dmtHistoryLOG", response);
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String amount = jsonObject.optString("amount");
                                String charged_amt = jsonObject.optString("charged_amt");
                                String created_at = jsonObject.optString("created_at");
                                String status = jsonObject.optString("status");

                                dmtHistoryModelArrayList.add(new DmtHistoryModel(amount, charged_amt, created_at, status));
                            }

                            if (dmtHistoryModelArrayList.size()>0){
                                noLayoutData.setVisibility(View.GONE);
                                planRecyclerView.setVisibility(View.VISIBLE);
                            }else {
                                noLayoutData.setVisibility(View.VISIBLE);
                                planRecyclerView.setVisibility(View.GONE);
                            }

                            planRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            planRecyclerView.setAdapter(new DmtHistoryAdapter(activity, dmtHistoryModelArrayList));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            noLayoutData.setVisibility(View.VISIBLE);
                            planRecyclerView.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        noLayoutData.setVisibility(View.VISIBLE);
                        planRecyclerView.setVisibility(View.GONE);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(ApiParams.user_id, Prefes.getUserID(activity));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(getActivity().getApplicationContext()));
                return params;
            }

        };
        MySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
