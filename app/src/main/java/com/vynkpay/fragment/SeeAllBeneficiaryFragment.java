package com.vynkpay.fragment;

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
import com.vynkpay.R;
import com.vynkpay.adapter.SeeAllBeneficiaryAdapter;
import com.vynkpay.events.UpdateDmtFields;
import com.vynkpay.models.BankModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SeeAllBeneficiaryFragment extends Fragment {

    View view;
    @BindView(R.id.seeAllBeneficiaryRecyclerView)
    RecyclerView seeAllBeneficiaryRecyclerView;
    @BindView(R.id.noLayout)
    LinearLayout noLayout;
    Dialog dialog;
    SeeAllBeneficiaryAdapter seeAllBeneficiaryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_see_all_beneficiary_rcg, container, false);
        ButterKnife.bind(SeeAllBeneficiaryFragment.this, view);
        dialog = M.showDialog(getActivity(), "", false, false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchAllBeneficiaryData();
        seeAllBeneficiaryAdapter = new SeeAllBeneficiaryAdapter(getActivity(), beneficiaryList, dialog, Prefes.getAccessToken(getActivity().getApplicationContext()));
        seeAllBeneficiaryRecyclerView.setNestedScrollingEnabled(false);
        seeAllBeneficiaryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        seeAllBeneficiaryRecyclerView.setAdapter(seeAllBeneficiaryAdapter);
    }


    Activity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=getActivity();
    }

    String mSuccess, mMessage;
    ArrayList<BankModel> beneficiaryList = new ArrayList<>();

    private void fetchAllBeneficiaryData() {
        beneficiaryList.clear();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.customerDetail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            Log.d(">>registrationResponse", "onResponse: " + response);
                            JSONObject jsonObject = new JSONObject(response);

                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONArray beneficiaryData = jsonObject.getJSONObject(ApiParams.data).getJSONArray(ApiParams.beneficiary);
                                for (int k = 0; k < beneficiaryData.length(); k++) {
                                    JSONObject innerJson = beneficiaryData.getJSONObject(k);
                                    String account = innerJson.getString(ApiParams.account);
                                    String name = innerJson.getString(ApiParams.name);
                                    String ifsc = innerJson.getString(ApiParams.ifsc);
                                    String bank = innerJson.getString(ApiParams.bank);
                                    String beneficiaryID = innerJson.getString(ApiParams.id);
                                    String mobile = innerJson.getString(ApiParams.mobile);
                                    String status = innerJson.optString("verification_status");
                                    BankModel bankModel = new BankModel();
                                    bankModel.setBankName(bank);
                                    bankModel.setStatus(status);
                                    bankModel.setMobile(mobile);
                                    bankModel.setBeneficiary_id(beneficiaryID);
                                    bankModel.setAccountHolderName(name);
                                    bankModel.setIfsc(ifsc);
                                    bankModel.setAccountNumber(account);
                                    beneficiaryList.add(bankModel);
                                }

                                if (beneficiaryList.size()<=0){
                                    noLayout.setVisibility(View.VISIBLE);
                                }else {
                                    noLayout.setVisibility(View.GONE);
                                }

                                seeAllBeneficiaryAdapter.notifyDataSetChanged();

                            } else {
                                M.dialogOk(getActivity(), mMessage, "Error");
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(getActivity().getApplicationContext()));

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.user_id, Prefes.getUserID(activity));
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(UpdateDmtFields updateDmtFields) {
        if (updateDmtFields != null) {
            if (updateDmtFields.isDeleted()) {
                fetchAllBeneficiaryData();
            }
        }
    }
}
