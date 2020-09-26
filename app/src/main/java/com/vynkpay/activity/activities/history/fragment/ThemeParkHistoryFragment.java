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
import com.vynkpay.adapter.PreviousTransferAdapter;
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

public class ThemeParkHistoryFragment extends Fragment {
    String accessToken;
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
    PreviousTransferAdapter previousTransferAdapter;
    ArrayList<TransferMoneyModel> previousTransactionList = new ArrayList<>();
    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plans_rcg, container, false);
        ButterKnife.bind(ThemeParkHistoryFragment.this, view);
        sortBy.setVisibility(View.GONE);
        dialog = M.showDialog(getActivity(), "", false, false);
        try {
            userPreference = new JSONObject(Prefes.getUserData(getActivity()));
            accessToken = userPreference.getString(ApiParams.access_token);
            Log.i(">>access_token", "onCreate: " + accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        previousTransferData();
        previousTransferAdapter = new PreviousTransferAdapter(getActivity(), previousTransactionList, 1, "WalletHistoryFragment");
        planRecyclerView.setNestedScrollingEnabled(false);
        planRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        planRecyclerView.setAdapter(previousTransferAdapter);
        return view;
    }

    String mSuccess, mMessage;

    private void previousTransferData() {
        previousTransactionList.clear();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.historyThemePark,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>themeParkHistory", "onResponse: " + response);
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);
                                JSONArray jsonElements = j.getJSONArray("history");
                                for (int k = 0; k < jsonElements.length(); k++) {
                                    TransferMoneyModel transferMoneyModel = new TransferMoneyModel();
                                    JSONObject jsonObject1 = jsonElements.getJSONObject(k);
                                    String amount = jsonObject1.getString(ApiParams.amount);
                                    String txn_status = jsonObject1.getString(ApiParams.order_status);
                                    String txn = jsonObject1.getString(ApiParams.txn);
                                    String provider = jsonObject1.getString(ApiParams.provider);
                                    String created_at = jsonObject1.getString(ApiParams.created_at);
                                    transferMoneyModel.setProvider(provider);
                                    transferMoneyModel.setDate(M.changeDateTimeFormat(created_at));
                                    transferMoneyModel.setAmount(amount);
                                    transferMoneyModel.setTxn(txn);
                                    transferMoneyModel.setStatus(txn_status);
                                    previousTransactionList.add(transferMoneyModel);
                                }
                                previousTransferAdapter.notifyDataSetChanged();
                                if (previousTransactionList.size() > 0) {
                                    noLayoutData.setVisibility(View.GONE);
                                } else {
                                    noDataText.setText("No Wallet history found");
                                    noLayoutData.setVisibility(View.VISIBLE);


                                }
                            } else {
                                noDataText.setText("No Wallet history found");
                                noLayoutData.setVisibility(View.VISIBLE);
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
                params.put(ApiParams.access_token, accessToken);
                return params;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
