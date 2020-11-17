package com.vynkpay.activity.recharge.insurance;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.activity.recharge.landline.activity.BillFetchActivity;
import com.vynkpay.activity.recharge.landline.activity.SelectLandLineOperatorActivity;
import com.vynkpay.activity.recharge.landline.events.EventBusLandline;
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
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.Calendar;
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

public class InsuranceActivity extends AppCompatActivity {
    @BindView(R.id.tvOperatorCircle)
    NormalTextView tvOperatorCircle;
    @BindView(R.id.etMobileNumber)
    NormalEditText etPolicyNumber;
    @BindView(R.id.etDateOfBirth)
    NormalTextView etDateOfBirth;
    @BindView(R.id.continueButton)
    NormalButton continueButton;
    Dialog dialog;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    JSONObject userPreference;
    String operatorName;
    String type = "11";
    String mSuccess, mMessage;
    DisposableSubscriber<JSONObject> d1;
    String _SPECIAL_OR_TOP_UP = "";
    private JSONObject jsonObject;
    private String operatorDetail;

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

    int _DEFAULT_CHECK_MAX, _DEFAULT_CHECK_MIN;

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
        setContentView(R.layout.activity_insurance_rcg);
        EventBus.getDefault().register(this);
        dialog = M.showDialog(InsuranceActivity.this, "", false, false);
        ButterKnife.bind(InsuranceActivity.this);
        setListeners();


        fetchingOperators();

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle.setText("Insurance");

        updateUI();
    }

    private void updateUI() {
        _SPECIAL_OR_TOP_UP = "";
        etPolicyNumber.setEnabled(false);
        etPolicyNumber.setHint("Enter policy number");
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
        MySingleton.getInstance(InsuranceActivity.this).addToRequestQueue(req);
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
                if (Prefes.getUserData(InsuranceActivity.this).equalsIgnoreCase("")) {
                    M.makeChecks(InsuranceActivity.this , LoginActivity.class);
                } else if (!(tvOperatorCircle.getText().toString().length() == 0)) {
                    if (etPolicyNumber.getText().toString().length() < _DEFAULT_CHECK_MIN || etPolicyNumber.getText().toString().length() > _DEFAULT_CHECK_MAX) {
                        Toast.makeText(InsuranceActivity.this, "Please enter valid " + _LABEL + "", Toast.LENGTH_SHORT).show();
                    } else {
                        if (etDateOfBirth.getText().length() == 0) {
                            Toast.makeText(InsuranceActivity.this, "Please enter valid date of birth", Toast.LENGTH_SHORT).show();
                        } else {
                            makeBillFetchRequest();
                        }
                    }
                } else {
                    Toast.makeText(InsuranceActivity.this, getString(R.string.valid_operator), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvOperatorCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsuranceActivity.this, SelectLandLineOperatorActivity.class);
                intent.putExtra(ApiParams.type, type);
                startActivity(intent);

            }
        });

        etDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePicker();
            }
        });
    }

    String var;

    private void getDatePicker() {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(InsuranceActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                /*      Your code   to get date and time    */
                selectedmonth = selectedmonth + 1;
                var = ("" + selectedyear + "-" + selectedmonth + "-" + selectedday);
                etDateOfBirth.setText(M.changeDateFormat(("" + selectedyear + "-" + selectedmonth + "-" + selectedday) + " " + "00:12:45"));
                if ((selectedday < 9 && selectedmonth < 9)) {
                    _DATE_OF_BIRTH = ("0" + selectedday + "-0" + selectedmonth + "-" + selectedyear);
                } else if ((selectedday < 9 || selectedmonth < 9)) {
                    if (selectedday < 9) {
                        _DATE_OF_BIRTH = ("0" + selectedday + "-" + selectedmonth + "-" + selectedyear);
                    } else {
                        _DATE_OF_BIRTH = ("" + selectedday + "-0" + selectedmonth + "-" + selectedyear);
                    }
                } else {
                    _DATE_OF_BIRTH = ("" + selectedday + "-" + selectedmonth + "-" + selectedyear);
                }

            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();

    }

    String _DATE_OF_BIRTH = "";

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
                                Intent intent = new Intent(InsuranceActivity.this, BillFetchActivity.class);
                                intent.putExtra("amount", amountFetch);
                                intent.putExtra("billDate", billDateFetch);
                                intent.putExtra("dueDate", dueDateFetch);
                                intent.putExtra("type", type);
                                intent.putExtra("label", _LABEL);
                                intent.putExtra("DOB", _DATE_OF_BIRTH + "@" + var);
                                intent.putExtra("partial_pay", partialPay);
                                intent.putExtra("landLineNumber", etPolicyNumber.getText().toString());
                                intent.putExtra("accountNumber", "10");
                                intent.putExtra("operatorName", operatorName);


                                intent.putExtra("operatorId", _SPECIAL_OR_TOP_UP);
                                intent.putExtra("operatorDetail", operatorDetail);


                                intent.putExtra("consumer_name", consumerName);
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
                        dialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.i(">>params", "getParams: " +
                        "mobile_number:" + etPolicyNumber.getText().toString() + "\n" +
                        "amount:" + "10" + "\n" +
                        "operator_id:" + _SPECIAL_OR_TOP_UP + "\n" +
                        "type:" + type + "\n" +
                        "account:" + _DATE_OF_BIRTH + "\n" +
                        "bill_fetch:" + "1");
                params.put("mobile_number", etPolicyNumber.getText().toString());
                params.put("amount", "10");
                params.put("operator_id", _SPECIAL_OR_TOP_UP);
                params.put("type", type);
                params.put("account", _DATE_OF_BIRTH);
                params.put("bill_fetch", "1");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(InsuranceActivity.this));
                params.put("Accept", "application/json");
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(stringRequest);
        //MySingleton.getInstance(InsuranceActivity.this).addToRequestQueue(stringRequest);
    }

    private void makeErrorDialog(String mMessage) {
        final Dialog errorDialog = M.inflateDialog(InsuranceActivity.this, R.layout.dialog_error_message_rcg);
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

    String _OPERATOR_IMAGE = "";

    private void makeRadioButton(String operatorName) {
        etPolicyNumber.setEnabled(true);
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

    String _OPERATOR_NAME = "";
    String _LABEL = "";

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(final EventBusLandline rechargeEventBus) {
        if (rechargeEventBus != null) {
            tvOperatorCircle.setText(rechargeEventBus.getOperator());
            operatorName = rechargeEventBus.getOperator();
            operatorName = rechargeEventBus.getOperator();
            makeRadioButton(operatorName);
            etPolicyNumber.setText("");
            _OPERATOR_NAME = rechargeEventBus.getOperator();


            etPolicyNumber.setEnabled(true);
            _LABEL = rechargeEventBus.getLabel().replaceAll("\\(.*\\)", "");
            etPolicyNumber.setHint(_LABEL);
            _DEFAULT_CHECK_MAX = Integer.valueOf(rechargeEventBus.getMaxLimit());
            _DEFAULT_CHECK_MIN = Integer.valueOf(rechargeEventBus.getMinLimit());

            etPolicyNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() > _DEFAULT_CHECK_MAX) {
                        s.replace(_DEFAULT_CHECK_MAX, etPolicyNumber.length(), "");
                    }
                }
            });
        }
    }
}
