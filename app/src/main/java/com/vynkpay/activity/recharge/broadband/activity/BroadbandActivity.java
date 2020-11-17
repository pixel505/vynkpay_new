package com.vynkpay.activity.recharge.broadband.activity;

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

public class BroadbandActivity extends AppCompatActivity {
    @BindView(R.id.tvOperatorCircle)
    NormalTextView tvOperatorCircle;
    @BindView(R.id.etMobileNumber)
    NormalEditText etLandLineNumber;
    @BindView(R.id.continueButton)
    NormalButton continueButton;
    Dialog dialog;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    JSONObject userPreference;
    String operatorName;
    String type = ApiParams.type_broadband;
    String mSuccess, mMessage;
    DisposableSubscriber<JSONObject> d1;
    String _SPECIAL_OR_TOP_UP = "";
    private JSONObject jsonObject;
    private int _WHERE_TO_GO = -1;
     String operatorDetail;

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
        setContentView(R.layout.activity_broadband_rcg);
        EventBus.getDefault().register(this);
        dialog = M.showDialog(BroadbandActivity.this, "", false, false);
        ButterKnife.bind(BroadbandActivity.this);
        setListeners();
        try {
            userPreference = new JSONObject(Prefes.getUserData(BroadbandActivity.this));

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

        toolbarTitle.setText("Broadband");
        updateUI();
    }

    private void updateUI() {
        _SPECIAL_OR_TOP_UP = "";
        etLandLineNumber.setEnabled(false);
        etLandLineNumber.setHint("Enter account/directory number");
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
        MySingleton.getInstance(BroadbandActivity.this).addToRequestQueue(req);
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
                if (Prefes.getUserData(BroadbandActivity.this).equalsIgnoreCase("")) {
                    M.makeChecks(BroadbandActivity.this, LoginActivity.class);
                } else if (!(tvOperatorCircle.getText().toString().length() == 0)) {
                    if (etLandLineNumber.getText().toString().length() < _DEFAULT_CHECK_MIN || etLandLineNumber.getText().toString().length() > _DEFAULT_CHECK_MAX) {
                        Toast.makeText(BroadbandActivity.this, "Please enter valid " + _LABEL + "", Toast.LENGTH_SHORT).show();
                    } else {
                        makeBillFetchRequest();
                    }
                } else {
                    Toast.makeText(BroadbandActivity.this, getString(R.string.valid_operator), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvOperatorCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BroadbandActivity.this, SelectLandLineOperatorActivity.class);
                intent.putExtra(ApiParams.type, type);
                startActivity(intent);

            }
        });
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
                                Intent intent = new Intent(BroadbandActivity.this, BillFetchActivity.class);
                                intent.putExtra("amount", amountFetch);
                                intent.putExtra("billDate", billDateFetch);
                                intent.putExtra("dueDate", dueDateFetch);
                                intent.putExtra("type", type);
                                intent.putExtra("label", _LABEL);
                                intent.putExtra("partial_pay", partialPay);
                                intent.putExtra("landLineNumber", etLandLineNumber.getText().toString());
                                intent.putExtra("accountNumber", "10");
                                intent.putExtra("operatorName", operatorName);
                                intent.putExtra("operatorId", _SPECIAL_OR_TOP_UP);
                                intent.putExtra("operatorDetail", operatorDetail);
                                intent.putExtra("consumer_name", consumerName);
                                intent.putExtra("operatorImage", BuildConfig.ImageBaseURl +
                                        operatorImage.replaceAll("////", ""));
                                startActivity(intent);


                            } else {
                                if (jsonObject.has(ApiParams.error_code)) {
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
                Log.i(">>value", "getParams: " +
                        etLandLineNumber.getText().toString() + "\n" +
                        "10" + "\n" + _SPECIAL_OR_TOP_UP + "\n" + type + "\n" + "1");
                params.put(ApiParams.mobile_number, etLandLineNumber.getText().toString());
                params.put("amount", "10");
                params.put("operator_id", _SPECIAL_OR_TOP_UP);
                params.put(ApiParams.type, type);
                params.put("bill_fetch", "1");







                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.i(">>operatorId", "getHeaders: " + Prefes.getAccessToken(BroadbandActivity.this));
                params.put(ApiParams.access_token, Prefes.getAccessToken(BroadbandActivity.this));
                params.put("Accept", "application/json");
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(stringRequest);

        //MySingleton.getInstance(BroadbandActivity.this).addToRequestQueue(stringRequest);
    }

    private void makeErrorDialog(String mMessage) {
        final Dialog errorDialog = M.inflateDialog(BroadbandActivity.this, R.layout.dialog_error_message_rcg);
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
        etLandLineNumber.setEnabled(true);
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

                        Log.e("_SPECIAL_OR_TOP_UP",""+jsonObject1.getString("id"));

                        if (!(jsonObject2.length() > 1)) {
                            for (int l = 0; l < jsonObject2.length(); l++) {
                                JSONObject object = jsonObject2.getJSONObject(l);
                                _SPECIAL_OR_TOP_UP = object.getString("id");

                                Log.e("_SPECIAL_OR_TOP_UP",""+_SPECIAL_OR_TOP_UP);


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
    String _LABEL = "account/directory number";

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(final EventBusLandline rechargeEventBus) {
        if (rechargeEventBus != null) {
            tvOperatorCircle.setText(rechargeEventBus.getOperator());
            operatorName = rechargeEventBus.getOperator();
            operatorName = rechargeEventBus.getOperator();
            makeRadioButton(operatorName);
            etLandLineNumber.setText("");
            _OPERATOR_NAME = rechargeEventBus.getOperator();
            Log.i(">>id", "onEvent: " + _SPECIAL_OR_TOP_UP);
            switch (_SPECIAL_OR_TOP_UP) {
                case "50": //CONN-ECT
                    _WHERE_TO_GO = 0;
                    etLandLineNumber.setEnabled(true);
                    _LABEL = "Directory Number";
                    etLandLineNumber.setHint("Directory Number");
                    _DEFAULT_CHECK_MAX = Integer.valueOf(rechargeEventBus.getMaxLimit());
                    _DEFAULT_CHECK_MIN = Integer.valueOf(rechargeEventBus.getMinLimit());
                    break;
                case "52": //HATH-WAY
                    _WHERE_TO_GO = 0;
                    _LABEL = "Customer ID";
                    etLandLineNumber.setEnabled(true);
                    etLandLineNumber.setHint("Customer ID");
                    _DEFAULT_CHECK_MAX = Integer.valueOf(rechargeEventBus.getMaxLimit());
                    _DEFAULT_CHECK_MIN = Integer.valueOf(rechargeEventBus.getMinLimit());
                    break;
            }
            etLandLineNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() > _DEFAULT_CHECK_MAX) {
                        s.replace(_DEFAULT_CHECK_MAX, etLandLineNumber.length(), "");
                    }
                }
            });
        }
    }
}
