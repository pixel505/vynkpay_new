package com.vynkpay.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.AddBeneficiaryActivity;
import com.vynkpay.activity.activities.TransferPreviewActivity;
import com.vynkpay.adapter.DmtHistoryAdapter;
import com.vynkpay.custom.NormalBoldTextView;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.events.DmtRegisterEvent;
import com.vynkpay.events.ListingBeneficiaryEvent;
import com.vynkpay.models.DmtHistoryModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BankTransferFragment extends Fragment {

    @BindView(R.id.actualLayout)
    LinearLayout actualLayout;

    @BindView(R.id.buttonsLinear)
    LinearLayout buttonsLinear;

    @BindView(R.id.continueAsButton)
    Button continueAsButton;

    @BindView(R.id.startWithButton)
    Button startWithButton;

    @BindView(R.id.etDescription)
    NormalEditText etDescription;
    @BindView(R.id.etAmount)
    NormalEditText etAmount;
    @BindView(R.id.etBeneficiaryName)
    NormalTextView etBeneficiaryName;
    @BindView(R.id.llBeneficiaryLayout)
    FrameLayout llBeneficiaryLayout;
    @BindView(R.id.addBeneficiaryButton)
    NormalButton addBeneficiaryButton;
    @BindView(R.id.transferNowButton)
    NormalButton transferNowButton;
    @BindView(R.id.llToBeShownDetails)
    LinearLayout llToBeShownDetails;
    @BindView(R.id.tvBeneficiaryAccountNumber)
    NormalTextView tvBeneficiaryAccountNumber;
    @BindView(R.id.tvBeneficiaryMobile)
    NormalTextView tvBeneficiaryMobile;
    @BindView(R.id.tvBeneficiaryIFSC)
    NormalTextView tvBeneficiaryIFSC;
    @BindView(R.id.tvBeneficiaryVerificationStatus)
    NormalTextView tvBeneficiaryVerificationStatus;

    Dialog dialog;
    String  remitter_id;
    View view;

    @BindView(R.id.registerUserLayout)
    LinearLayout registerUserLayout;
    @BindView(R.id.etFirstName)
    NormalEditText etFirstName;
    @BindView(R.id.etLastName)
    NormalEditText etLastName;
    @BindView(R.id.etPin)
    NormalEditText etPin;
    @BindView(R.id.etPhone)
    NormalEditText etPhone;
    @BindView(R.id.submitButton)
    NormalButton submitButton;

    @BindView(R.id.etOtp)
    NormalEditText etOtp;
    @BindView(R.id.verifyButton)
    NormalButton verifyButton;
    @BindView(R.id.otpLinear)
    LinearLayout otpLinear;
    @BindView(R.id.dmtHistoryRecyclerView)
    RecyclerView dmtHistoryRecyclerView;
    @BindView(R.id.thTV)
    NormalBoldTextView thTV;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().removeAllStickyEvents();
        }
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bank_rcg, container, false);
        ButterKnife.bind(BankTransferFragment.this, view);
        EventBus.getDefault().register(this);
        dialog = M.showDialog(getActivity(), "", false, false);
        setListeners();
        remitter_id = M.fetchUserTrivialInfo(getActivity(), ApiParams.remitter_id);
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                M.removeZero(s);
            }
        });

        Log.i(">>id", "onCreateView:" + remitter_id);
        if (remitter_id == null || remitter_id.equals("null") || remitter_id.isEmpty()) {
            actualLayout.setVisibility(View.GONE);
            buttonsLinear.setVisibility(View.VISIBLE);

        } else {
            actualLayout.setVisibility(View.VISIBLE);
            buttonsLinear.setVisibility(View.GONE);
        }

        continueAsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFirstName.getText().toString().trim().length() == 0
                        || etLastName.getText().toString().trim().length() == 0
                        || etPin.getText().toString().trim().length() == 0
                        || etPhone.getText().toString().isEmpty()) {
                    registerUserLayout.setVisibility(View.VISIBLE);
                    buttonsLinear.setVisibility(View.GONE);
                }else {
                    registerUserOnDmt();
                }
            }
        });

        startWithButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFirstName.setText("");
                etLastName.setText("");
                etPin.setText("");
                etPhone.setText("");
                registerUserLayout.setVisibility(View.VISIBLE);
                actualLayout.setVisibility(View.GONE);
                buttonsLinear.setVisibility(View.GONE);
            }
        });

        getUserDetail();
        dmtHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        gettingDMTHistory();
    }

    public void getUserDetail(){
        String url=BuildConfig.APP_BASE_URL + URLS.user_details_URL;
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getUserDetailsss", response);
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    String fname=jsonObject.optString("fname");
                    String lname=jsonObject.optString("lname");
                    String pin=jsonObject.optString("pin");
                    String phone=jsonObject.optString("phone");

                    etFirstName.setText(fname);
                    etLastName.setText(lname);
                    etPin.setText(pin);
                    etPhone.setText(phone);
                    continueAsButton.setText("Continues as "+fname);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap =new HashMap<>();
                hashMap.put("user_id", Prefes.getUserID(activity));
                return hashMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap =new HashMap<>();
                hashMap.put("access_token", Prefes.getAccessToken(getActivity().getApplicationContext()));
                return hashMap;
            }
        };

        MySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    boolean verified;
    Activity activity;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=getActivity();
    }

    String beneficiary_id = "";
    String ben_account, ben_ifsc, name, mobile;
    private void setListeners() {
        addBeneficiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), AddBeneficiaryActivity.class);
                intent.putExtra(ApiParams.typeOfSelection, "0"); //for opening new beneficiary
                startActivityForResult(intent, 0);
            }
        });
        etBeneficiaryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), AddBeneficiaryActivity.class);
                intent.putExtra(ApiParams.typeOfSelection, "1"); //for opening existing beneficiary list
                startActivityForResult(intent, 1);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFirstName.length() == 0 || etFirstName.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_first_name), Toast.LENGTH_SHORT).show();
                } else if (etLastName.length() == 0 || etLastName.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_last_name), Toast.LENGTH_SHORT).show();
                } else if (etPin.length() == 0 || etPin.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_pin), Toast.LENGTH_SHORT).show();
                } else if (etPhone.getText().toString().trim().length()<10){
                    Toast.makeText(getActivity(), getString(R.string.valid_phone), Toast.LENGTH_SHORT).show();
                }else if (etPhone.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), getString(R.string.please_phone), Toast.LENGTH_SHORT).show();
                }else {
                    registerUserOnDmt();
                }
            }
        });

        transferNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etBeneficiaryName.length() == 0 || etBeneficiaryName.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_beneficiary), Toast.LENGTH_SHORT).show();
                } else if (etAmount.length() == 0 || etAmount.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_amount), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), TransferPreviewActivity.class);
                    intent.putExtra(ApiParams.ben_account, ben_account);
                    intent.putExtra(ApiParams.ben_ifsc, ben_ifsc);
                    intent.putExtra(ApiParams.name, name);
                    intent.putExtra(ApiParams.mobile, mobile);
                    intent.putExtra(ApiParams.beneficiary_id, beneficiary_id);
                    intent.putExtra(ApiParams.status, status);
                    intent.putExtra(ApiParams.amount, etAmount.getText().toString());
                    intent.putExtra(ApiParams.description, etDescription.length() == 0 ? "" : etDescription.getText().toString());
                    startActivity(intent);
                }
            }
        });

        //otp verification
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOtp.getText().toString().trim().isEmpty()){
                    Toast.makeText(activity, "Please enter OTP", Toast.LENGTH_SHORT).show();
                }else if (etOtp.getText().toString().trim().length()<4){
                    Toast.makeText(activity, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                }else {
                    verifyRequest();
                }
            }
        });
    }

    private void verifyRequest() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.dmtCustVerification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);

                            Log.i(">>otpResponse", "onResponse: " + jsonObject.toString());

                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {

                                otpLinear.setVisibility(View.GONE);

                                M.upDateUserTrivialInfo(getActivity(), ApiParams.remitter_id, remitter_id_string);

                                EventBus.getDefault().post(new DmtRegisterEvent(remitter_id_string, true));

                            } else {
                                JSONObject pay_response= jsonObject.getJSONObject("pay_response");
                                String OPERATOR_ERROR_MESSAGE=pay_response.optString("OPERATOR_ERROR_MESSAGE");
                                Toast.makeText(activity, OPERATOR_ERROR_MESSAGE+"", Toast.LENGTH_SHORT).show();
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
                        Log.i(">>exception", "onResponse: " + error.getMessage());
                        dialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(ApiParams.otp, etOtp.getText().toString());
                map.put(ApiParams.user_id, Prefes.getUserID(activity));
                map.put(ApiParams.remitter_id, remitter_id);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(getActivity().getApplicationContext()));
                return params;
            }

        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    String status;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(ListingBeneficiaryEvent event) {
        if (event != null) {
            llToBeShownDetails.setVisibility(View.VISIBLE);
            status = event.getStatus();
            beneficiary_id = event.getBeneficiary_id();
            ben_account = event.getBen_account();
            ben_ifsc = event.getBen_ifsc();
            name = event.getAccountHolderName();
            mobile = event.getMobile_number();
            etBeneficiaryName.setText(name);
            tvBeneficiaryAccountNumber.setText(ben_account);
            tvBeneficiaryIFSC.setText(ben_ifsc);
            if (!status.equals("")) {
                tvBeneficiaryVerificationStatus.setText("Verified");
            } else {
                tvBeneficiaryVerificationStatus.setTextColor(getActivity().getResources().getColor(R.color.color_red));
                tvBeneficiaryVerificationStatus.setText("Not Verified");
            }
            tvBeneficiaryMobile.setText(mobile);
        }
    }

    Intent intent;
    String mSuccess, mMessage, remitter_id_string;
    private void registerUserOnDmt() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.dmtRegister,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(">>registrationResponse", "onResponse: " + response);
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject data=jsonObject.getJSONObject("data");
                                 remitter_id_string = data.getString("remitter_id");
                                 remitter_id=remitter_id_string;
                                String is_verified=data.getString("is_verified");

                                if (is_verified.equals("1")){
                                    verified=true;
                                }else {
                                    verified=false;
                                }

                                 if (verified){
                                     M.upDateUserTrivialInfo(getActivity(), ApiParams.remitter_id, remitter_id_string);
                                     EventBus.getDefault().post(new DmtRegisterEvent(remitter_id_string, true));
                                 }else {
                                     actualLayout.setVisibility(View.GONE);
                                     registerUserLayout.setVisibility(View.GONE);
                                     buttonsLinear.setVisibility(View.GONE);
                                     otpLinear.setVisibility(View.VISIBLE);
                                 }

                            }else {
                                JSONObject pay_response=jsonObject.getJSONObject("pay_response");
                                String OPERATOR_ERROR_MESSAGE=pay_response.optString("OPERATOR_ERROR_MESSAGE");
                                M.dialogOk(getActivity(), OPERATOR_ERROR_MESSAGE, "Error");
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

                map.put(ApiParams.user_id, Prefes.getUserID(activity));
                map.put(ApiParams.fname, etFirstName.getText().toString());
                map.put(ApiParams.lname, etLastName.getText().toString());
                map.put(ApiParams.pin, etPin.getText().toString());
                map.put(ApiParams.phone, etPhone.getText().toString());

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(getActivity().getApplicationContext()));
                return params;
            }

        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onEvent(DmtRegisterEvent event) {
        if (event != null) {
            if (event.isChangeLayout()) {
                actualLayout.setVisibility(View.VISIBLE);
                registerUserLayout.setVisibility(View.GONE);
                buttonsLinear.setVisibility(View.GONE);
            }
        }
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
                                thTV.setVisibility(View.VISIBLE);
                            }else {
                                thTV.setVisibility(View.GONE);
                            }
                            dmtHistoryRecyclerView.setAdapter(new DmtHistoryAdapter(activity, dmtHistoryModelArrayList));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            thTV.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        thTV.setVisibility(View.GONE);
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
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
