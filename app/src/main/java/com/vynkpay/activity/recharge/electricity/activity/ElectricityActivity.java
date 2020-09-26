package com.vynkpay.activity.recharge.electricity.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.activity.recharge.dth.activity.DthActivity;
import com.vynkpay.activity.recharge.electricity.model.EventBusElectric;
import com.vynkpay.activity.recharge.landline.activity.BillFetchActivity;
import com.vynkpay.activity.recharge.landline.activity.LandlineRechargeConfirmationActivity;
import com.vynkpay.activity.recharge.landline.activity.SelectLandLineOperatorActivity;
import com.vynkpay.activity.recharge.landline.events.EventBusLandline;
import com.vynkpay.activity.recharge.mobile.activity.RechargeConfirmationActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
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
import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ElectricityActivity extends AppCompatActivity {

    @BindView(R.id.tvOperatorCircle)
    NormalTextView tvOperatorCircle;
    @BindView(R.id.etMobileNumber)
    NormalEditText etConsumerNumber;
    @BindView(R.id.continueButton)
    NormalButton continueButton;
    Dialog dialog;

    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.tvSubDivision)
    NormalTextView tvSubDivision;
    @BindView(R.id.etFurtherMobileNumber)
    NormalEditText etFurtherMobileNumber;
    @BindView(R.id.tvSubDivisionLayout)
    RelativeLayout tvSubDivisionLayout;
    @BindView(R.id.etFurtherMobileNumberLayout)
    RelativeLayout etFurtherMobileNumberLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.llAmountLayout)
    LinearLayout llAmountLayout;
    @BindView(R.id.etPartialAmount)
    NormalEditText etPartialAmount;
    JSONObject userPreference;
    String operatorName;
    String type = ApiParams.type_electricty;
    String mSuccess, mMessage;
    DisposableSubscriber<JSONObject> d1;
    String _SPECIAL_OR_TOP_UP = "";
    int _DEFAULT_CHECK_MAX, _DEFAULT_CHECK_MIN;
    String _OPERATOR_IMAGE = "";
    String _OPERATOR_NAME = "";
    String _OUTER_OPERATOR_ID;
    String _LABEL = "account/directory number";
    String _DIVISION_NAME_CODE;
    private JSONObject jsonObject;
    private int _WHERE_TO_GO = 0;
    private boolean subDivisionIsShowing = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().removeAllStickyEvents();
        }
        EventBus.getDefault().unregister(this);
        if (d1 != null) {
            d1.dispose();
        }
    }

    private void fetchingOperators() {
        d1 = new DisposableSubscriber<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
                handleOperatorResponse(jsonObject);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                System.out.println("Success!");
            }
        };
        newGetOperatorData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d1);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_rcg);
        EventBus.getDefault().register(this);
        dialog = M.showDialog(ElectricityActivity.this, "", false, false);
        ButterKnife.bind(ElectricityActivity.this);
        setListeners();
        try {
            userPreference = new JSONObject(Prefes.getUserData(ElectricityActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fetchingOperators();
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle.setText("Electricity");
        updateUI();
    }

    private void updateUI() {
        etConsumerNumber.setEnabled(false);
    }

    private void handleOperatorResponse(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Flowable<JSONObject> newGetOperatorData() {
        return Flowable.defer(new Callable<Publisher<? extends JSONObject>>() {
            @Override
            public Publisher<? extends JSONObject> call() throws Exception {
                return Flowable.just(getOperatorData());
            }
        });
    }

    private JSONObject getOperatorData() throws ExecutionException, InterruptedException, RuntimeException {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String Url = BuildConfig.APP_BASE_URL + URLS.operator + "?type=" + type;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Url, null, future, null);
        MySingleton.getInstance(ElectricityActivity.this).addToRequestQueue(req);
        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return future.get();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setListeners() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefes.getUserData(ElectricityActivity.this).equalsIgnoreCase("")) {
                    M.makeChecks(ElectricityActivity.this , LoginActivity.class);
                } else if (!(tvOperatorCircle.getText().toString().length() == 0)) {
                    if (_WHERE_TO_GO == 1) {
                        if (etConsumerNumber.getText().toString().length() < _DEFAULT_CHECK_MIN || etConsumerNumber.getText().toString().length() > _DEFAULT_CHECK_MAX) {
                            Toast.makeText(ElectricityActivity.this, "Please enter valid " + _LABEL + "", Toast.LENGTH_SHORT).show();
                        } else if (etPartialAmount.length() == 0 || etPartialAmount.getText().toString().trim().length() == 0) {
                            Toast.makeText(ElectricityActivity.this, getString(R.string.valid_amount), Toast.LENGTH_SHORT).show();
                        } else {
                            makeDirectRequest();
                        }
                    } else {
                        if (subDivisionIsShowing) {
                            if (tvSubDivision.length() == 0) {
                                Toast.makeText(ElectricityActivity.this, "Please select sub division first", Toast.LENGTH_SHORT).show();
                            } else if (etConsumerNumber.getText().toString().length() < _DEFAULT_CHECK_MIN || etConsumerNumber.getText().toString().length() > _DEFAULT_CHECK_MAX) {
                                Toast.makeText(ElectricityActivity.this, "Please enter valid " + _LABEL + "", Toast.LENGTH_SHORT).show();
                            } else {
                                makeBillFetchRequest();
                            }
                        } else {
                            if (etFurtherMobileNumberLayout.getVisibility() == View.VISIBLE) {
                                if (etConsumerNumber.getText().toString().length() < _DEFAULT_CHECK_MIN || etConsumerNumber.getText().toString().length() > _DEFAULT_CHECK_MAX) {
                                    Toast.makeText(ElectricityActivity.this, "Please enter valid " + _LABEL + "", Toast.LENGTH_SHORT).show();
                                } else if (etFurtherMobileNumber.getText().toString().length() < 10 || etConsumerNumber.getText().toString().length() > 10) {
                                    Toast.makeText(ElectricityActivity.this, getString(R.string.valid_phone), Toast.LENGTH_SHORT).show();
                                } else {
                                    makeBillFetchRequest();
                                }
                            } else {
                                if (etConsumerNumber.getText().toString().length() < _DEFAULT_CHECK_MIN || etConsumerNumber.getText().toString().length() > _DEFAULT_CHECK_MAX) {
                                    Toast.makeText(ElectricityActivity.this, "Please enter valid " + _LABEL + "", Toast.LENGTH_SHORT).show();
                                } else {
                                    makeBillFetchRequest();
                                }
                            }
                        }
                    }

                } else {
                    Toast.makeText(ElectricityActivity.this, getString(R.string.valid_operator), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvOperatorCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etFurtherMobileNumber.setText("");
                Intent intent = new Intent(ElectricityActivity.this, SelectLandLineOperatorActivity.class);
                intent.putExtra(ApiParams.type, type);
                startActivity(intent);
            }
        });
        tvSubDivisionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElectricityActivity.this, SelectElectricityOperator.class);
                intent.putExtra(ApiParams.type, type);
                intent.putExtra("operator_id", _OUTER_OPERATOR_ID);
                startActivity(intent);
            }
        });
    }
  String operatorDetail,opUrl;
    private void makeDirectRequest() {
        Intent intent = new Intent(ElectricityActivity.this, RechargeConfirmationActivity.class);
        intent.putExtra("amount", etPartialAmount.getText().toString());
        intent.putExtra("billDate", "");
        intent.putExtra("dueDate", "");
        intent.putExtra("type", type);
        intent.putExtra("label", _LABEL);
        intent.putExtra("landLineNumber", etConsumerNumber.getText().toString());
        intent.putExtra("accountNumber", "10");
        intent.putExtra("amount", etPartialAmount.getText().toString());
        intent.putExtra("operatorName", _OPERATOR_NAME);
        intent.putExtra("operatorId", _SPECIAL_OR_TOP_UP);
        intent.putExtra("operatorImage", _OPERATOR_IMAGE);

        Prefes.saveType(type, ElectricityActivity.this);
        Prefes.saveOperatorUrl(opUrl,ElectricityActivity.this);
        Prefes.savePhoneNumber(etConsumerNumber.getText().toString(),ElectricityActivity.this);
        Prefes.saveOperatorID(_SPECIAL_OR_TOP_UP,ElectricityActivity.this);
        Prefes.saveCircle(operatorName,ElectricityActivity.this);
        Prefes.saveOperatorDID(operatorDetail,ElectricityActivity.this);




        startActivity(intent);
    }

    private void makeBillFetchRequest() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.recharge,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>billFetchResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString("success");
                            mMessage = jsonObject.optString("message");
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject billObject = jsonObject.getJSONObject(ApiParams.data).getJSONObject(ApiParams.bill);
                                String amountFetch = billObject.getString(ApiParams.amount);
                                String billDateFetch = billObject.getString(ApiParams.bill_date);
                                String dueDateFetch = billObject.getString(ApiParams.due_date);
                                String operatorImage = billObject.getString(ApiParams.operator_logo);
                                String consumerName = billObject.getString(ApiParams.consumer_name);
                                String partialPay = billObject.getString(ApiParams.partial_pay);
                                Intent intent = new Intent(ElectricityActivity.this, BillFetchActivity.class);
                                intent.putExtra("amount", amountFetch);
                                intent.putExtra("billDate", billDateFetch);
                                intent.putExtra("dueDate", dueDateFetch);
                                intent.putExtra("type", type);
                                intent.putExtra("label", _LABEL);
                                intent.putExtra("partial_pay", partialPay);
                                intent.putExtra("landLineNumber", etConsumerNumber.getText().toString());
                                intent.putExtra("accountNumber", _DIVISION_NAME_CODE.equals("") ? etFurtherMobileNumber.getText().toString() : _DIVISION_NAME_CODE);
                                intent.putExtra("operatorName", operatorName);
                                intent.putExtra("consumer_name", consumerName);


                                intent.putExtra("operatorId", _SPECIAL_OR_TOP_UP);
                                intent.putExtra("operatorDetail", operatorDetail);


                                intent.putExtra("operatorImage", BuildConfig.ImageBaseURl + operatorImage.replaceAll("////", ""));
                                startActivity(intent);
                            } else {
                                if (jsonObject.has(ApiParams.error_code)) {
                                    if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_400)) {
                                        Iterator<?> keys = jsonObject.getJSONObject(ApiParams.errors).keys();
                                        while (keys.hasNext()) {
                                            String key = (String) keys.next();
                                            if (jsonObject.getJSONObject(ApiParams.errors).get(key) instanceof String) {
                                                String message = jsonObject.getJSONObject(ApiParams.errors).get(key).toString();
                                                makeErrorDialog(message);
                                            }
                                        }
                                    } else if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_401)) {
                                        makeErrorDialog(mMessage);
                                    }
                                } else {
                                    makeErrorDialog(mMessage);
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
                        Log.i(">>exception", "onResponse: " + error.getMessage());
                        dialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String account = _DIVISION_NAME_CODE.equals("") ? etFurtherMobileNumber.getText().toString() : _DIVISION_NAME_CODE;
                Log.i(">>value", "getParams: " +
                        etConsumerNumber.getText().toString() + "\n" +
                        "10" + "\n" + _SPECIAL_OR_TOP_UP + "\n" + type + "\n" + "1" + "\n" + account);

                params.put(ApiParams.mobile_number, etConsumerNumber.getText().toString());
                params.put("amount", "10");
                params.put("operator_id", _SPECIAL_OR_TOP_UP);
                params.put(ApiParams.type, type);
                params.put("bill_fetch", "1");
                params.put("account", account);  // division Code or  division Name (torrent) or Mobile number dakshin
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(ElectricityActivity.this));
                params.put("Accept", "application/json");
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(stringRequest);

        //MySingleton.getInstance(ElectricityActivity.this).addToRequestQueue(stringRequest);
    }

    private void makeErrorDialog(String mMessage) {
        final Dialog errorDialog = M.inflateDialog(ElectricityActivity.this, R.layout.dialog_error_message_rcg);
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

    private void makeRadioButton(String operatorName) {
        etConsumerNumber.setEnabled(true);
        try {
            mSuccess = this.jsonObject.optString(ApiParams.success);
            if (mSuccess.equalsIgnoreCase("true")) {
                JSONArray j = this.jsonObject.getJSONArray(ApiParams.data);
                for (int k = 0; k < j.length(); k++) {
                    JSONObject jsonObject1 = j.getJSONObject(k);
                    if (operatorName.equalsIgnoreCase(jsonObject1.getString(ApiParams.operator))) {

                        _OPERATOR_IMAGE = jsonObject1.getString(ApiParams.image_url);
                        JSONArray jsonObject2 = jsonObject1.getJSONArray(ApiParams.operator_urls);
                        operatorDetail = jsonObject1.getString("id");

                        if (!(jsonObject2.length() > 1)) {
                            for (int l = 0; l < jsonObject2.length(); l++) {
                                JSONObject object = jsonObject2.getJSONObject(l);
                                _SPECIAL_OR_TOP_UP = object.getString("id");

                                //_SPECIAL_OR_TOP_UP = object.getString(ApiParams.id);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i(">>exception", "onResponse: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(final EventBusLandline rechargeEventBus) {
        if (rechargeEventBus != null) {
            tvOperatorCircle.setText(rechargeEventBus.getOperator());
            operatorName = rechargeEventBus.getOperator();
            makeRadioButton(operatorName);
            etConsumerNumber.setText("");
            _LABEL = rechargeEventBus.getLabel().replaceAll("\\(.*\\)", "");
            etConsumerNumber.setHint(_LABEL);
            _OPERATOR_NAME = rechargeEventBus.getOperator();
            _OUTER_OPERATOR_ID = rechargeEventBus.getOuter();
            _DEFAULT_CHECK_MAX = Integer.valueOf(rechargeEventBus.getMaxLimit());
            _DEFAULT_CHECK_MIN = Integer.valueOf(rechargeEventBus.getMinLimit());
            Log.i(">>special", "onEvent: " + _OUTER_OPERATOR_ID);
            switch (_OUTER_OPERATOR_ID) {
                case "94":
                    _WHERE_TO_GO = 0;
                    subDivisionIsShowing = true;
                    tvSubDivisionLayout.setVisibility(View.VISIBLE);
                    etFurtherMobileNumberLayout.setVisibility(View.GONE);
                    tvSubDivisionLayout.setEnabled(true);
                    break;
                case "99":
                    _WHERE_TO_GO = 0;
                    subDivisionIsShowing = true;
                    tvSubDivisionLayout.setVisibility(View.VISIBLE);
                    etFurtherMobileNumberLayout.setVisibility(View.GONE);
                    tvSubDivisionLayout.setEnabled(true);
                    break;
                case "113":
                    _WHERE_TO_GO = 0;
                    subDivisionIsShowing = true;
                    tvSubDivisionLayout.setVisibility(View.VISIBLE);
                    etFurtherMobileNumberLayout.setVisibility(View.GONE);
                    tvSubDivisionLayout.setEnabled(true);
                    break;
                case "84":
                    _WHERE_TO_GO = 0;
                    subDivisionIsShowing = false;
                    tvSubDivisionLayout.setVisibility(View.GONE);
                    etFurtherMobileNumberLayout.setVisibility(View.VISIBLE);
                    break;

                case "119":
                    _WHERE_TO_GO = 0;
                    subDivisionIsShowing = false;
                    tvSubDivisionLayout.setVisibility(View.GONE);
                    etFurtherMobileNumberLayout.setVisibility(View.VISIBLE);
                    break;

                default:
                    subDivisionIsShowing = false;
                    tvSubDivisionLayout.setVisibility(View.GONE);
                    etFurtherMobileNumberLayout.setVisibility(View.GONE);
            }


            switch (_SPECIAL_OR_TOP_UP) {
                case "147":
                    subDivisionIsShowing = false;
                    _WHERE_TO_GO = 1;
                    tvSubDivisionLayout.setVisibility(View.GONE);
                    etFurtherMobileNumberLayout.setVisibility(View.GONE);
                    llAmountLayout.setVisibility(View.VISIBLE);
                    break;
                case "162":
                    _WHERE_TO_GO = 1;
                    subDivisionIsShowing = false;
                    tvSubDivisionLayout.setVisibility(View.GONE);
                    etFurtherMobileNumberLayout.setVisibility(View.GONE);
                    llAmountLayout.setVisibility(View.VISIBLE);
                    break;
                case "170":
                    _WHERE_TO_GO = 1;
                    subDivisionIsShowing = false;
                    tvSubDivisionLayout.setVisibility(View.GONE);
                    etFurtherMobileNumberLayout.setVisibility(View.GONE);
                    llAmountLayout.setVisibility(View.VISIBLE);
                    break;
                case "182":
                    _WHERE_TO_GO = 1;
                    subDivisionIsShowing = false;
                    tvSubDivisionLayout.setVisibility(View.GONE);
                    etFurtherMobileNumberLayout.setVisibility(View.GONE);
                    llAmountLayout.setVisibility(View.VISIBLE);
                    break;
                default:
                    llAmountLayout.setVisibility(View.GONE);
            }
            _DIVISION_NAME_CODE = "";
            etConsumerNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() > _DEFAULT_CHECK_MAX) {
                        s.replace(_DEFAULT_CHECK_MAX, etConsumerNumber.length(), "");
                    }
                }
            });
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEventBusElectric(final EventBusElectric rechargeEventBus) {
        if (rechargeEventBus != null) {
            tvSubDivision.setText(rechargeEventBus.getDivision());
            switch (_OUTER_OPERATOR_ID) {
                case "94":
                    _DIVISION_NAME_CODE = rechargeEventBus.getCode();
                    break;
                case "99":
                    _DIVISION_NAME_CODE = rechargeEventBus.getCode();
                    break;
                case "113":
                    _DIVISION_NAME_CODE = rechargeEventBus.getDivision();
                    break;
                case "84":
                    _DIVISION_NAME_CODE = "";
                    break;
            }
        }
    }
}
