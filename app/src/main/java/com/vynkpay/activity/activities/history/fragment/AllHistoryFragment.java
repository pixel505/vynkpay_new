package com.vynkpay.activity.activities.history.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.vynkpay.adapter.CashbackRewardAdapter;
import com.vynkpay.adapter.PreviousTransferAdapter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.CashbackRewardModel;
import com.vynkpay.models.TransferMoneyModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.Functions;
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

public class AllHistoryFragment extends Fragment {
    View view;
    @BindView(R.id.noLayoutData)
    LinearLayout noLayoutData;
    @BindView(R.id.noDataText)
    NormalTextView noDataText;
    @BindView(R.id.rechargeTitleTV)
    NormalTextView rechargeTitleTV;
    @BindView(R.id.walletTitleTV)
    NormalTextView walletTitleTV;
    @BindView(R.id.recyclerView)
    RecyclerView rechargeRecyclerView;
    @BindView(R.id.transactionsRecyclerView)
    RecyclerView transactionsRecyclerView;
    Activity activity;
    ArrayList<TransferMoneyModel> previousTransactionList = new ArrayList<>();
    Dialog dialog;
    HistoryRechargesAdapter previousTransferAdapter;

    JSONObject userPreference;


    PreviousTransferAdapter walletTransferAdapter;
    ArrayList<TransferMoneyModel> walletTransactionList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.all_transactions_history_layout_rcg, container, false);
        ButterKnife.bind(AllHistoryFragment.this, view);
        dev();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=getActivity();
    }

    private void dev() {
        noDataText.setText("No history found");
        rechargeRecyclerView.setLayoutManager(Functions.layoutManager(activity, Functions.VERTICAL, 0));
        transactionsRecyclerView.setLayoutManager(Functions.layoutManager(activity, Functions.VERTICAL, 0));
        try {
            userPreference = new JSONObject(Prefes.getUserData(activity));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        previousTransferData();
        previousTransferAdapter = new HistoryRechargesAdapter(getActivity(), previousTransactionList, 1, "recharge");
        rechargeRecyclerView.setAdapter(previousTransferAdapter);


        //Wallet transactions
        walletTransferData();
        walletTransferAdapter = new PreviousTransferAdapter(getActivity(), walletTransactionList, 1 , "WalletHistoryFragment");
        transactionsRecyclerView.setAdapter(walletTransferAdapter);
    }

    String mSuccess, mMessage;

    private void previousTransferData() {
        previousTransactionList.clear();
        dialog = M.showDialog(getActivity(), "", false, false);
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
                                    rechargeTitleTV.setVisibility(View.VISIBLE);
                                }
                            } else {
                                noLayoutData.setVisibility(View.VISIBLE);
                                rechargeTitleTV.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                            dialog.dismiss();
                            noLayoutData.setVisibility(View.VISIBLE);
                            rechargeTitleTV.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        noLayoutData.setVisibility(View.VISIBLE);
                        rechargeTitleTV.setVisibility(View.GONE);
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(getContext()));
                params.put("Accept", "application/json");
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    ArrayList<CashbackRewardModel> cashbackRewardModelArrayList = new ArrayList<>();
    CashbackRewardAdapter cashbackRewardAdapter;
    private void walletTransferData() {
        cashbackRewardModelArrayList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.transactionHistory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("cashback_rewardLOGG", "onResponse: " + response);
                        try {
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
                                    cashbackRewardAdapter = new CashbackRewardAdapter(activity, cashbackRewardModelArrayList);
                                    transactionsRecyclerView.setNestedScrollingEnabled(false);
                                    transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
                                    transactionsRecyclerView.setAdapter(cashbackRewardAdapter);
                                    cashbackRewardAdapter.notifyDataSetChanged();
                                    walletTitleTV.setVisibility(View.VISIBLE);
                                }else {
                                    walletTitleTV.setVisibility(View.GONE);
                                }
                            }

                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                            walletTitleTV.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        walletTitleTV.setVisibility(View.GONE);
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(getContext()));
                params.put("Accept", "application/json");
                return params;
            }
        };

        MySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
