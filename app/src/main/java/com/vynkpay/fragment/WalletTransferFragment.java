package com.vynkpay.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
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
import com.vynkpay.activity.activities.MoneyTransferActivity;
import com.vynkpay.activity.activities.history.AllRechargeHistoryActivity;
import com.vynkpay.adapter.PreviousTransferAdapter;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.events.UpdateBalanceEvent;
import com.vynkpay.events.UpdateWalletTransferFields;
import com.vynkpay.models.TransferMoneyModel;
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
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletTransferFragment extends Fragment {
    @BindView(R.id.previousRecharges)
    RecyclerView previousRecharges;
    @BindView(R.id.etMobileNumber)
    NormalEditText etMobileNumber;
    @BindView(R.id.etAmount)
    NormalEditText etAmount;
    @BindView(R.id.seeAllList)
    NormalTextView seeAllList;
    @BindView(R.id.transferNowButton)
    NormalButton transferNowButton;
    @BindView(R.id.noLayoutData)
    LinearLayout noLayoutData;

    Dialog dialog;
    JSONObject userPreference;
    PreviousTransferAdapter previousTransferAdapter;
    ArrayList<TransferMoneyModel> previousTransactionList = new ArrayList<>();
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wallet_rcg, container, false);
        ButterKnife.bind(WalletTransferFragment.this, view);
        EventBus.getDefault().register(this);
        dialog = M.showDialog(getActivity(), "", false, false);
        setListeners();
        try {
            userPreference = new JSONObject(Prefes.getUserData(getActivity()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
        etMobileNumber.addTextChangedListener(new TextWatcher() {
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

        previousTransferData();
        return view;
    }

    private void loadAdapter() {
        previousTransferAdapter = new PreviousTransferAdapter(getActivity(), previousTransactionList, 0, "WalletTransfer");
        previousRecharges.setNestedScrollingEnabled(false);
        previousRecharges.setLayoutManager(new LinearLayoutManager(getActivity()));
        previousRecharges.setAdapter(previousTransferAdapter);
        previousTransferAdapter.notifyDataSetChanged();
    }

    private void setListeners() {
        transferNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMobileNumber.getText().length() == 0 || etMobileNumber.getText().toString().trim().length() == 0 || etMobileNumber.getText().length() < 10 || etMobileNumber.getText().length() > 10) {
                    Toast.makeText(getActivity(), getString(R.string.valid_phone), Toast.LENGTH_SHORT).show();
                } else if (etAmount.getText().length() == 0 || etAmount.getText().length() > 6 || etAmount.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_amount), Toast.LENGTH_SHORT).show();
                } else if (Double.valueOf(etAmount.getText().toString()) > 500000 || Double.valueOf(etAmount.getText().toString()) == 500000) {
                    Toast.makeText(getActivity(), "Amount should be less than 500,000", Toast.LENGTH_SHORT).show();
                } else {
                    makeTransferAmountRequest();
                }
            }
        });

        seeAllList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllRechargeHistoryActivity.class);
                intent.putExtra("pos", 3);
                startActivity(intent);
            }
        });
    }

    String mSuccess, mMessage, userID;

    private void previousTransferData() {
        previousTransactionList.clear();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.transactionHistory,
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
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);
                                userID = j.getString("user_id");
                                JSONArray jsonElements = j.getJSONArray("history");
                                for (int k = 0; k < jsonElements.length(); k++) {
                                    TransferMoneyModel transferMoneyModel = new TransferMoneyModel();
                                    JSONObject jsonObject1 = jsonElements.getJSONObject(k);
                                    if (jsonObject1.getString(ApiParams.txn_type).equalsIgnoreCase("sent")) {
                                        String amount = jsonObject1.getString(ApiParams.amount);
                                        String txn_type = jsonObject1.getString(ApiParams.txn_type);
                                        String txn_status = jsonObject1.getString(ApiParams.txn_status);
                                        String txn = jsonObject1.getString(ApiParams.txn);
                                        String provider = jsonObject1.getString(ApiParams.provider);
                                        String mobile_number = jsonObject1.getString(ApiParams.mobile_number);
                                        String created_at = jsonObject1.getString(ApiParams.created_at);
                                        if (provider.equals("2")) {
                                            transferMoneyModel.setSentOrRecieve(txn_type);
                                            transferMoneyModel.setMobile(mobile_number);
                                            transferMoneyModel.setDate(M.changeDateTimeFormat(created_at));
                                            transferMoneyModel.setAmount(amount);
                                            transferMoneyModel.setProvider(provider);
                                            transferMoneyModel.setTxn(txn);
                                            transferMoneyModel.setStatus(txn_status);
                                            previousTransactionList.add(transferMoneyModel);
                                        }
                                    }

                                }
                                loadAdapter();
                                if (previousTransactionList.size() > 0) {
                                    noLayoutData.setVisibility(View.GONE);
                                    previousRecharges.setVisibility(View.VISIBLE);
                                } else {
                                    noLayoutData.setVisibility(View.VISIBLE);
                                    previousRecharges.setVisibility(View.GONE);
                                }
                            } else {
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

    private void makeTransferAmountRequest() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.transferAmount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>transferResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                etAmount.setText("");
                                JSONObject dataObject = jsonObject.getJSONObject(ApiParams.data);
                                String created_at = dataObject.getString("created_at");
                                String amount = dataObject.getString("amount");
                                String leftAmount = dataObject.getString("provider_balance");
                                String id = dataObject.getString("txn");
                                makeSuccessDialog(created_at, amount, id, mSuccess, leftAmount);
                            } else {
                                if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_400) || jsonObject.getString(ApiParams.error_code).equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_401)) {
                                    Iterator<?> keys = jsonObject.getJSONObject(ApiParams.errors).keys();
                                    while (keys.hasNext()) {
                                        String key = (String) keys.next();
                                        if (jsonObject.getJSONObject(ApiParams.errors).get(key) instanceof String) {
                                            String message = jsonObject.getJSONObject(ApiParams.errors).get(key).toString();
                                            makeErrorDialog(message);
                                        }
                                    }
                                }
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
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.amount, etAmount.getText().toString());
                params.put(ApiParams.mobile_number, etMobileNumber.getText().toString());
                return params;
            }

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

    NormalButton okButton;
    NormalTextView textResult, tvMobileNumber, tvAmount, tvTransactionId, tvDateTime;
    ImageView imageResult;
    Dialog dialog1;

    public boolean isMade;

    private void makeSuccessDialog(String date, String amount, String id, String mSuccess, final String leftAmount) {
        dialog1 = M.inflateDialog(getActivity(), R.layout.dialog_payment_result_rcg);
        okButton = dialog1.findViewById(R.id.okButton);
        imageResult = dialog1.findViewById(R.id.imageResult);
        textResult = dialog1.findViewById(R.id.textResult);
        tvMobileNumber = dialog1.findViewById(R.id.tvMobileNumber);
        tvAmount = dialog1.findViewById(R.id.tvAmount);
        tvTransactionId = dialog1.findViewById(R.id.tvTransactionId);
        tvDateTime = dialog1.findViewById(R.id.tvDateTime);
        tvDateTime.setText(M.changeDateTimeFormat(date));
        tvAmount.setText(Functions.CURRENCY_SYMBOL + amount);
        tvMobileNumber.setText(etMobileNumber.getText().toString());
        tvTransactionId.setText("txn" + id);
        if (mSuccess.equalsIgnoreCase("true")) {
            okButton.setText("Ok");
            imageResult.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_icon));
            textResult.setText("Transfer Successful");
            isMade = true;
        } else {
            okButton.setText("Ok");
            imageResult.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_icon));
            textResult.setText("Transfer Fail");
            isMade = false;
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                double planAmount = Double.parseDouble(leftAmount);
                EventBus.getDefault().postSticky(new UpdateBalanceEvent(planAmount));
                previousTransferData();
            }
        });
        dialog1.show();
    }

    private void makeErrorDialog(String mMessage) {
        final Dialog errorDialog = M.inflateDialog(getActivity(), R.layout.dialog_error_message_rcg);
        NormalTextView errorMessage = errorDialog.findViewById(R.id.errorMessage);
        NormalButton okButton = errorDialog.findViewById(R.id.okButton);
        LinearLayout closeButton = errorDialog.findViewById(R.id.closeButton);
        errorMessage.setText(mMessage);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorDialog.dismiss();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
            }
        });
        errorDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(UpdateWalletTransferFields updateWalletTransferFields) {
        if (updateWalletTransferFields != null) {
            etAmount.setText(updateWalletTransferFields.getAmount());
            etAmount.setSelection(etAmount.length());
            etMobileNumber.setText(updateWalletTransferFields.getMobileNumber());
            etMobileNumber.setSelection(etMobileNumber.length());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().removeAllStickyEvents();
        }
        EventBus.getDefault().unregister(this);
    }
}
