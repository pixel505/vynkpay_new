package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.ErrorDialogModel;
import com.vynkpay.models.SuccessDialogModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TransferPreviewActivity extends AppCompatActivity {
    Dialog dialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.beneficiaryName)
    NormalTextView beneficiaryName;
    @BindView(R.id.benAccountNumber)
    NormalTextView benAccountNumber;
    @BindView(R.id.benIfsc)
    NormalTextView benIfsc;
    @BindView(R.id.amountToTransfer)
    NormalTextView amountToTransfer;
    @BindView(R.id.charges)
    NormalTextView etCharges;
    @BindView(R.id.totalTransfer)
    NormalTextView totalTransfer;
    @BindView(R.id.preTotalTransfer)
    NormalTextView preTotalTransfer;
    @BindView(R.id.benMobileNumber)
    NormalTextView benMobileNumber;
    @BindView(R.id.continueButton)
    NormalButton continueButton;
    @BindView(R.id.description)
    NormalTextView description;
    @BindView(R.id.laySeePlain)
    LinearLayout laySeePlain;
    @BindView(R.id.descriptionLinear)
    LinearLayout descriptionLinear;
    String status, beneficiary_id, ben_account, ben_ifsc, name, mobile, amount, mSuccess, mMessage, descriptionStr;
   // double EXTRA_CHARGES = 5.0;
    double AMOUNT = 0.0, finalAmount = 0.0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_transfer_rcg);
        dialog = M.showDialog(TransferPreviewActivity.this, "", false, false);
        ButterKnife.bind(TransferPreviewActivity.this);
        setListeners();
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle.setText("Confirm Money Transfer");

        laySeePlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TransferPreviewActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        status = getIntent().getStringExtra(ApiParams.status);
        beneficiary_id = getIntent().getStringExtra(ApiParams.beneficiary_id);
        ben_account = getIntent().getStringExtra(ApiParams.ben_account);
        ben_ifsc = getIntent().getStringExtra(ApiParams.ben_ifsc);
        name = getIntent().getStringExtra(ApiParams.name);
        mobile = getIntent().getStringExtra(ApiParams.mobile);
        amount = getIntent().getStringExtra(ApiParams.amount);
        descriptionStr = getIntent().getStringExtra(ApiParams.description);

        if (descriptionStr==null || descriptionStr.isEmpty()){
            descriptionLinear.setVisibility(View.GONE);
        }else {
            descriptionLinear.setVisibility(View.VISIBLE);
            description.setText(descriptionStr+"");
        }

        beneficiaryName.setText(name);
        benAccountNumber.setText(ben_account);
        benIfsc.setText(ben_ifsc);
        benMobileNumber.setText(mobile);
        AMOUNT = Double.parseDouble(amount);
        amountToTransfer.setText(Functions.CURRENCY_SYMBOL + " " + AMOUNT + "");
       // etCharges.setText(Functions.CURRENCY_SYMBOL + " " + EXTRA_CHARGES + "");
        totalTransfer.setText(Functions.CURRENCY_SYMBOL + " " + (AMOUNT ) + "");
        preTotalTransfer.setText(Functions.CURRENCY_SYMBOL + " " + (AMOUNT ) + "");
        finalAmount = AMOUNT;

        fetchWalletData();
    }

    double walletAmount=0.0;

    private void setListeners() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletAmount>=finalAmount){
                    transferFund();
                }else {
                    Toast.makeText(TransferPreviewActivity.this, "insufficient funds in wallet to transfer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void fetchWalletData() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.wallet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>walletData", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data);
                                String balance = jsonObject1.getString(ApiParams.balance);
                                 walletAmount = Double.parseDouble(balance);

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
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            M.dialogOk(TransferPreviewActivity.this, "Please check internet connection", "Error");
                        } else if (error instanceof AuthFailureError) {
                            M.dialogOk(TransferPreviewActivity.this, "You are not authorized", "Error");
                        } else if (error instanceof ServerError) {
                            M.dialogOk(TransferPreviewActivity.this, "Server issue", "Error");
                        } else if (error instanceof NetworkError) {
                            M.dialogOk(TransferPreviewActivity.this, "Please check internet connection", "Error");
                        } else if (error instanceof ParseError) {
                            M.dialogOk(TransferPreviewActivity.this, "Internal error", "Error");
                        }
                        dialog.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(TransferPreviewActivity.this));
                return params;
            }
        };
        MySingleton.getInstance(TransferPreviewActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void transferFund() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.fundTransfer,
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
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data).getJSONObject(ApiParams.transaction);
                                makeDialogSuccess(jsonObject1.getString("txn"), jsonObject1.getString("created_at"),
                                        jsonObject1.getString("amount")
                                        , mMessage, mSuccess, ben_account);
                            } else {
                                makeErrorDialog(mMessage);
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

                map.put(ApiParams.user_id, Prefes.getUserID(TransferPreviewActivity.this));
                map.put(ApiParams.beneficiary_id, beneficiary_id);
                map.put(ApiParams.amount, finalAmount + "");
                map.put(ApiParams.remark, descriptionStr);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(TransferPreviewActivity.this));
                return params;
            }

        };
        MySingleton.getInstance(TransferPreviewActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    ErrorDialogModel errorDialogModel;
    private void makeErrorDialog(String mMessage) {
        errorDialogModel = M.makeErrorDialog(TransferPreviewActivity.this);
        final Dialog errorDialog = errorDialogModel.getDialog();
        NormalTextView errorMessage = errorDialogModel.getErrorMessage();
        NormalButton okButton = errorDialogModel.getOkButton();
        LinearLayout closeButton = errorDialogModel.getCloseButton();
        errorMessage.setText(mMessage);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.makeNavigationToHome(errorDialog, TransferPreviewActivity.this);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.makeNavigationToHome(errorDialog, TransferPreviewActivity.this);
            }
        });
        errorDialog.show();
    }

    SuccessDialogModel successDialogModel;
    private void makeDialogSuccess(String txnId, String dateTime, String amount, String title, String mSuccess, String name) {
        successDialogModel = M.makeSuccessDialog(TransferPreviewActivity.this);
        successDialogModel.getTvMobileNumber().setText(name);
        double temp_planAmount = Double.parseDouble(amount);
        successDialogModel.getTvAmount().setText(Functions.CURRENCY_SYMBOL + " " + temp_planAmount + "");
        successDialogModel.getForWhatPaid().setText("Bank Transfer");
        successDialogModel.getTvTransactionId().setText(txnId.equals("") || txnId.equalsIgnoreCase("null") ? "" : txnId);
        successDialogModel.getTvDateTime().setText(M.changeDateTimeFormat(dateTime));
        successDialogModel.getTextResult().setText(title);
        if (mSuccess.equalsIgnoreCase("true")) {
            successDialogModel.getImageResult().setBackgroundDrawable(getResources().getDrawable(R.drawable.success));
        } else {
            successDialogModel.getImageResult().setBackgroundDrawable(getResources().getDrawable(R.drawable.failed));
        }
        successDialogModel.getOkButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                M.makeNavigationToHome(successDialogModel.getSuccessDialog(), TransferPreviewActivity.this);
            }
        });

        successDialogModel.getCloseButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.makeNavigationToHome(successDialogModel.getSuccessDialog(), TransferPreviewActivity.this);
            }
        });
        successDialogModel.getSuccessDialog().show();
    }
}
