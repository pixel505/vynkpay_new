package com.vynkpay.activity.themepark.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.activity.themepark.model.ThemeParkCategoryTicketModel;
import com.vynkpay.activity.themepark.model.ThemeParkTicketModel;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.SuccessDialogModel;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;
import com.vynkpay.utils.URLS;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemeParkFillInfoActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    Dialog dialog;
    ThemeParkTicketModel themeParkTicketModel;
    ThemeParkCategoryTicketModel themeParkCategoryTicketModel;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etFirstName)
    NormalEditText etFirstName;
    @BindView(R.id.etLastName)
    NormalEditText etLastName;
    @BindView(R.id.etDateOfTour)
    NormalTextView etDateOfTour;
    @BindView(R.id.etEmail)
    NormalEditText etEmail;
    @BindView(R.id.etMobileNumber)
    NormalEditText etMobileNumber;
    @BindView(R.id.etAddress1)
    NormalEditText etAddress1;
    @BindView(R.id.etAddress2)
    NormalEditText etAddress2;
    @BindView(R.id.etCity)
    NormalEditText etCity;
    @BindView(R.id.etState)
    NormalEditText etState;
    @BindView(R.id.etPostalCode)
    NormalEditText etPostalCode;
    @BindView(R.id.etAmount)
    NormalButton etAmount;
    @BindView(R.id.themeParkTitle)
    NormalTextView themeParkTitle;
    @BindView(R.id.noOfTickets)
    NormalTextView noOfTickets;
    @BindView(R.id.bookButton)
    NormalButton bookButton;
    @BindView(R.id.orendaWalletAmount)
    NormalTextView orendaWalletAmount;
    @BindView(R.id.payUPaid)
    NormalTextView payUPaid;
    @BindView(R.id.payUCheck)
    CheckBox payUCheck;
    @BindView(R.id.orendaWalletLayout)
    LinearLayout orendaWalletLayout;
    @BindView(R.id.orendaCheck)
    CheckBox orendaCheck;
    @BindView(R.id.walletBalance)
    NormalTextView walletBalance;
    String access_token, product_id, quantity, calculatedPrice, originalPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_ticket_fill_info_rcg);

        ButterKnife.bind(ThemeParkFillInfoActivity.this);
        setListeners();

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_title.setText("Book tickets");
        access_token = M.fetchUserTrivialInfo(ThemeParkFillInfoActivity.this, ApiParams.access_token);
        dialog = M.showDialog(ThemeParkFillInfoActivity.this, "", true, true);
        themeParkTicketModel = getIntent().getParcelableExtra("themeParkModel");
        product_id = getIntent().getStringExtra("product_id");

        quantity = getIntent().getStringExtra("quantity");
        calculatedPrice = getIntent().getStringExtra("calculatedPrice");
        originalPrice = getIntent().getStringExtra("originalPrice");
        themeParkCategoryTicketModel = getIntent().getParcelableExtra("themeParkCategoryModel");
        Log.i(">>values", "onCreate: " + themeParkTicketModel.getId() + "\n" +
                themeParkTicketModel.getTitle() + "\n" + calculatedPrice + quantity);

        etAmount.setText("Total Amount : " + Functions.CURRENCY_SYMBOL_USER + "" + calculatedPrice);
        themeParkTitle.setText("Theme Park : " + themeParkTicketModel.getTitle());
        noOfTickets.setText("No of tickets : " + quantity);
        etDateOfTour.setText("Tour Date : " + M.changeDateFormat(themeParkTicketModel.getVisitDate() + " 12:12:45"));

        fetchWalletData();
    }

    String mSuccess, mMessage;
    float payu, orenda, AMOUNT_FOR_PAY_U, AMOUNT_FOR_ORENDA;

    private void setListeners() {
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFirstName.getText().toString().trim().length() == 0 || etFirstName.length() == 0) {
                    Toast.makeText(ThemeParkFillInfoActivity.this, getString(R.string.valid_first_name), Toast.LENGTH_SHORT).show();
                } else if (etLastName.getText().toString().trim().length() == 0 || etLastName.length() == 0) {
                    Toast.makeText(ThemeParkFillInfoActivity.this, getString(R.string.valid_last_name), Toast.LENGTH_SHORT).show();
                } else if (etEmail.getText().toString().trim().length() == 0 || etEmail.length() == 0 || !M.validateEmail(etEmail.getText().toString().trim())) {
                    Toast.makeText(ThemeParkFillInfoActivity.this, getString(R.string.valid_email), Toast.LENGTH_SHORT).show();
                } else if (etMobileNumber.getText().toString().trim().length() == 0 || etMobileNumber.length() == 0 || etMobileNumber.length() < 10) {
                    Toast.makeText(ThemeParkFillInfoActivity.this, getString(R.string.valid_phone), Toast.LENGTH_SHORT).show();
                } else if (etAddress1.getText().toString().trim().length() == 0 || etAddress1.length() == 0) {
                    Toast.makeText(ThemeParkFillInfoActivity.this, getString(R.string.please_enter_address), Toast.LENGTH_SHORT).show();
                } else if (etAddress2.getText().toString().trim().length() == 0 || etAddress2.length() == 0) {
                    Toast.makeText(ThemeParkFillInfoActivity.this, getString(R.string.please_enter_address), Toast.LENGTH_SHORT).show();
                } else if (etCity.getText().toString().trim().length() == 0 || etCity.length() == 0) {
                    Toast.makeText(ThemeParkFillInfoActivity.this, getString(R.string.please_enter_city), Toast.LENGTH_SHORT).show();
                } else if (etState.getText().toString().trim().length() == 0 || etState.length() == 0) {
                    Toast.makeText(ThemeParkFillInfoActivity.this, getString(R.string.please_enter_state), Toast.LENGTH_SHORT).show();
                } else if (etPostalCode.getText().toString().trim().length() == 0 || etPostalCode.length() == 0) {
                    Toast.makeText(ThemeParkFillInfoActivity.this, getString(R.string.please_enter_code), Toast.LENGTH_SHORT).show();
                } else if (!(payUCheck.isChecked()) && !(orendaCheck.isChecked())) {
                    Toast.makeText(ThemeParkFillInfoActivity.this, getString(R.string.valid_payment_method), Toast.LENGTH_SHORT).show();
                } else {
                    if (!payUCheck.isChecked() && orendaCheck.isChecked()) {
                        orenda = Float.parseFloat(orendaWalletAmount.getText().toString().replace(Functions.CURRENCY_SYMBOL_USER + " ", ""));
                        payu = 0;
                        AMOUNT_FOR_ORENDA = payu + orenda;
                        bookTicket(calculatedPrice);
                    } else {
                        if (payUCheck.isChecked()) {
                            payu = Float.parseFloat(payUPaid.getText().toString().replace(Functions.CURRENCY_SYMBOL_USER + " ", ""));
                        } else {
                            payu = 0;
                        }
                        if (orendaCheck.isChecked()) {
                            orenda = Float.parseFloat(orendaWalletAmount.getText().toString().replace(Functions.CURRENCY_SYMBOL_USER + " ", ""));
                        } else {
                            orenda = 0;
                        }
                        AMOUNT_FOR_PAY_U = payu;
                        makePaymentFromPayUFirst(AMOUNT_FOR_PAY_U);
                    }
                }
            }
        });
    }

    private void makePaymentFromPayUFirst(final float AMOUNT_FOR_PAYU) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.add_money_url,
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
                                String amount = jsonObject1.getString(ApiParams.amount);
                                String productinfo = jsonObject1.getString(ApiParams.productinfo);
                                String surl = jsonObject1.getString(ApiParams.surl);
                                String furl = jsonObject1.getString(ApiParams.furl);
                                String key = jsonObject1.getString(ApiParams.key);
                                String txnid = jsonObject1.getString(ApiParams.txnid);
                                String firstname = jsonObject1.getString(ApiParams.firstname);
                                String email = jsonObject1.getString(ApiParams.email);
                                String phone = jsonObject1.getString(ApiParams.phone);
                                String hash = jsonObject1.getString(ApiParams.hash);
                                String vas_for_mobile_sdk_hash = jsonObject1.getString(ApiParams.vas_for_mobile_sdk_hash);
                                String payment_related_details_for_mobile_sdk_hash = jsonObject1.getString(ApiParams.payment_related_details_for_mobile_sdk_hash);
                                String get_merchant_ibibo_codes_hash = jsonObject1.getString(ApiParams.get_merchant_ibibo_codes_hash);

                               ///

                                String merchantId = jsonObject1.getString("merchantId");
                                boolean mode = jsonObject1.getBoolean("DEBUG");


                            } else {
                                M.dialogOk(ThemeParkFillInfoActivity.this, mMessage, "Error");
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
                map.put(ApiParams.amount, AMOUNT_FOR_PAYU + "");
                map.put(ApiParams.productinfo, ApiParams.mobile_recharge);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, access_token);
                return params;
            }

        };
        MySingleton.getInstance(ThemeParkFillInfoActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(ThemeParkFillInfoActivity.this).setConnectivityListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivityLOGG", "request code " + requestCode + " resultcode " + resultCode);

    }


    private void respondToPayResponse(final JSONObject payUResponse) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.pay_u_sucsess_final,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>ourResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                bookTicket(Float.parseFloat(calculatedPrice) + "");
                            } else {
                                if (jsonObject.has(ApiParams.error_code)) {
                                    if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_400)) {
                                        Iterator<?> keys = jsonObject.getJSONObject(ApiParams.errors).keys();
                                        while (keys.hasNext()) {
                                            String key = (String) keys.next();
                                            if (jsonObject.getJSONObject(ApiParams.errors).get(key) instanceof String) {
                                                String message = jsonObject.getJSONObject(ApiParams.errors).get(key).toString();
                                                makeErrorDialog(message, jsonObject.getString(ApiParams.error_code));
                                            }
                                        }
                                    } else if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_409)) {
                                        makeErrorDialog(mMessage, jsonObject.getString(ApiParams.error_code));
                                    }else if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase("402")){
                                        makeErrorDialog(mMessage, jsonObject.getString(ApiParams.error_code));
                                    }
                                } else {
                                    makeErrorDialog(mMessage, "000");
                                }
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
                map.put(ApiParams.payu_response, payUResponse.toString());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, access_token);
                return params;
            }
        };
        MySingleton.getInstance(ThemeParkFillInfoActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void bookTicket(final String amount) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.themeParkBookTicket,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>ticketBookResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data).getJSONObject(ApiParams.transaction);
                                makeDialogSuccess(jsonObject1.getString("txn"), jsonObject1.getString("created_at"),
                                        jsonObject1.getString("amount")
                                        , mMessage, mSuccess, themeParkTicketModel.getTitle());
                            } else {
                                makeErrorDialog(mMessage, "000");
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
                Map<String, String> map = new HashMap<>();
                map.put(ApiParams.date_of_tour, themeParkTicketModel.getVisitDate());
                map.put(ApiParams.beneficiary_first_name, etFirstName.getText().toString());
                map.put(ApiParams.beneficiary_last_name, etLastName.getText().toString());
                map.put(ApiParams.email, etEmail.getText().toString());
                map.put(ApiParams.beneficiary_mobile, etMobileNumber.getText().toString());
                map.put(ApiParams.beneficiary_address_1, etAddress1.getText().toString());
                map.put(ApiParams.beneficiary_address_2, etAddress2.getText().toString());
                map.put(ApiParams.beneficiary_city, etCity.getText().toString());
                map.put(ApiParams.beneficiary_state, etState.getText().toString());
                map.put(ApiParams.beneficiary_country_id, "IND");
                map.put(ApiParams.beneficiary_post_code, etPostalCode.getText().toString());
                map.put(ApiParams.product_id, product_id);
                map.put(ApiParams.tour_options_id, themeParkTicketModel.getId());
                map.put(ApiParams.tour_options_title, themeParkTicketModel.getTitle());
                map.put(ApiParams.pricing_id, themeParkCategoryTicketModel.getTiketCategoryId());
                map.put(ApiParams.pricing_qty, quantity);
                map.put(ApiParams.pricing_name, themeParkCategoryTicketModel.getTicketCategoryName());
                map.put(ApiParams.amount, amount);
                //map.put(ApiParams.time , );
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, access_token);
                return params;
            }

        };
        MySingleton.getInstance(ThemeParkFillInfoActivity.this).
                addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    Dialog errorDialog;

    private void makeErrorDialog(String mMessage, final String errorCode) {
        errorDialog = M.inflateDialog(ThemeParkFillInfoActivity.this, R.layout.dialog_error_message_rcg);
        NormalTextView errorMessage = errorDialog.findViewById(R.id.errorMessage);
        NormalButton okButton = errorDialog.findViewById(R.id.okButton);
        LinearLayout closeButton = errorDialog.findViewById(R.id.closeButton);
        errorMessage.setText(mMessage);

        if (errorCode.equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_400)) {
            okButton.setText("Home");
        } else {
            okButton.setText("Try Again");
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (errorCode.equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_400)) {
                    Intent homeIntent = new Intent(ThemeParkFillInfoActivity.this, HomeActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homeIntent);
                    finish();
                } else {
                    errorDialog.dismiss();
                }
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
            }
        });
        if (errorDialog != null) {
            errorDialog.show();
        }
    }

    SuccessDialogModel successDialogModel;

    private void makeDialogSuccess(String txnId, String dateTime, String amount, String title, String mSuccess, String name) {
        successDialogModel = M.makeSuccessDialog(ThemeParkFillInfoActivity.this);
        successDialogModel.getTvMobileNumber().setText(name);
        double temp_planAmount = Double.parseDouble(amount);
        successDialogModel.getTvAmount().setText(Functions.CURRENCY_SYMBOL_USER + " " + temp_planAmount + "");
        successDialogModel.getForWhatPaid().setText("Theme Park");
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
                M.makeNavigationToHome(successDialogModel.getSuccessDialog(), ThemeParkFillInfoActivity.this);
            }
        });

        successDialogModel.getCloseButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.makeNavigationToHome(successDialogModel.getSuccessDialog(), ThemeParkFillInfoActivity.this);
            }
        });

        successDialogModel.getSuccessDialog().show();
    }


    double PLAIN_AMOUNT, WALLET_BALANCE;

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
                                WALLET_BALANCE = Double.parseDouble(balance);
                                walletBalance.setVisibility(View.VISIBLE);
                                walletBalance.setText("Wallet Balance : " + Functions.CURRENCY_SYMBOL_USER + " " + WALLET_BALANCE + "");

                                PLAIN_AMOUNT = Double.parseDouble(calculatedPrice);

                                if (WALLET_BALANCE == 0) {
                                    orendaCheck.setEnabled(false);
                                    orendaWalletLayout.setAlpha(.5f);
                                    orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL_USER + " " + 0 + "");
                                    payUPaid.setText(Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + "");
                                    payUCheck.setChecked(true);
                                    orendaCheck.setChecked(false);

                                    payUCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));

                                            } else {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                            }
                                        }
                                    });


                                } else if (WALLET_BALANCE < PLAIN_AMOUNT) {
                                    orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL_USER + " " + WALLET_BALANCE + "");
                                    payUPaid.setText(Functions.CURRENCY_SYMBOL_USER + " " + (PLAIN_AMOUNT - WALLET_BALANCE) + "");
                                    payUCheck.setChecked(true);
                                    orendaCheck.setChecked(true);

                                    payUCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(true);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                                payUPaid.setText(Functions.CURRENCY_SYMBOL_USER + " " + (PLAIN_AMOUNT - WALLET_BALANCE) + "");
                                                bookButton.setEnabled(true);
                                                bookButton.setAlpha(1.0f);

                                            } else {
                                                if (orendaCheck.isChecked()) {
                                                    payUCheck.setChecked(false);
                                                    orendaCheck.setChecked(true);
                                                    orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                                    payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                                    bookButton.setEnabled(false);
                                                    bookButton.setAlpha(.5f);

                                                } else {
                                                    payUCheck.setChecked(true);
                                                }

                                            }
                                        }
                                    });
                                    orendaCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(true);
                                                orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL_USER + " " + WALLET_BALANCE + "");
                                                payUPaid.setText(Functions.CURRENCY_SYMBOL_USER + " " + (PLAIN_AMOUNT - WALLET_BALANCE) + "");
                                            } else {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                            }
                                        }
                                    });


                                } else if (WALLET_BALANCE >= PLAIN_AMOUNT) {
                                    orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + "");
                                    payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0) + "");
                                    payUCheck.setChecked(false);
                                    orendaCheck.setChecked(true);

                                    payUCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                            } else {
                                                payUCheck.setChecked(false);
                                                orendaCheck.setChecked(true);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                            }
                                        }
                                    });
                                    orendaCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(false);
                                                orendaCheck.setChecked(true);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                            } else {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                            }
                                        }
                                    });
                                }

                            } else {
                                M.dialogOk(ThemeParkFillInfoActivity.this, mMessage, "Error");
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
                            M.dialogOk(ThemeParkFillInfoActivity.this, "Please check internet connection", "Error");
                        } else if (error instanceof AuthFailureError) {
                            M.dialogOk(ThemeParkFillInfoActivity.this, "You are not authorized", "Error");
                        } else if (error instanceof ServerError) {
                            M.dialogOk(ThemeParkFillInfoActivity.this, "Server issue", "Error");
                        } else if (error instanceof NetworkError) {
                            M.dialogOk(ThemeParkFillInfoActivity.this, "Please check internet connection", "Error");
                        } else if (error instanceof ParseError) {
                            M.dialogOk(ThemeParkFillInfoActivity.this, "Internal error", "Error");
                        }
                        dialog.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, access_token);
                return params;
            }
        };

        MySingleton.getInstance(ThemeParkFillInfoActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(ThemeParkFillInfoActivity.this,ThemeParkFillInfoActivity.this::finishAffinity);
        }
    }
}
