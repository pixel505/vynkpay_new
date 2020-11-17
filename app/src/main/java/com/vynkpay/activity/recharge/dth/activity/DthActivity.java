package com.vynkpay.activity.recharge.dth.activity;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.activity.recharge.dth.events.EventBusDth;
import com.vynkpay.activity.recharge.mobile.activity.RechargeConfirmationActivity;
import com.vynkpay.activity.recharge.mobile.events.PlanRechargeBus;
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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class DthActivity extends AppCompatActivity {
    @BindView(R.id.tvOperatorCircle)
    NormalTextView tvOperatorCircle;
    @BindView(R.id.etMobileNumber)
    NormalEditText etMobileNumber;
    @BindView(R.id.etAmount)
    NormalEditText etAmount;
    @BindView(R.id.seePlanButton)
    LinearLayout seePlanButton;
    @BindView(R.id.layoutToBeVisible)
    LinearLayout layoutToBeVisible;
    @BindView(R.id.continueButton)
    NormalButton continueButton;
    Dialog dialog;

    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    JSONObject userPreference;
    String accessToken;
    String operatorName;
    String type = "5";
    String mSuccess, mMessage;
    DisposableSubscriber<JSONObject> d1;
    String _SPECIAL_OR_TOP_UP = "";
    String _VALIDITY = "";
    String _TALK_TIME = "";
    String _PLAN = "";
    private JSONObject jsonObject;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
        if (d1 != null) {
            d1.dispose();
        }
    }

    int _DEFAULT_CHECK_MAX;
    int _DEFAULT_CHECK_MIN;

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
        setContentView(R.layout.activity_dth_rcg);
        EventBus.getDefault().register(this);
        dialog = M.showDialog(DthActivity.this, "", false, false);
        ButterKnife.bind(DthActivity.this);
        setListeners();
        try {
            userPreference = new JSONObject(Prefes.getUserData(DthActivity.this));
            accessToken = Prefes.getAccessToken(DthActivity.this);
            Log.i(">>accessToken", "onCreate: " + accessToken);
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

        toolbarTitle.setText("DTH");


        updateUI();

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _PLAN = "";
                _VALIDITY = "";
                _TALK_TIME = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
                M.removeZero(s);
            }
        });
    }

    private void updateUI() {
        _SPECIAL_OR_TOP_UP = "";
        etMobileNumber.setEnabled(false);
        etMobileNumber.setEnabled(false);
        etAmount.setEnabled(false);
        etMobileNumber.setText("");
        etAmount.setText("");
        tvOperatorCircle.setText("");
        if (tvOperatorCircle.getText().length() < 0 || tvOperatorCircle.getText().length() == 0) {
            etAmount.setEnabled(false);
            etMobileNumber.setEnabled(false);
        } else {
            etAmount.setEnabled(true);
            etMobileNumber.setEnabled(true);
        }
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
        MySingleton.getInstance(DthActivity.this).addToRequestQueue(req);
        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return future.get();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
String operatorDetail,opUrl;
    private void makeRechargeRequest() {
        Intent intent = new Intent(DthActivity.this, RechargeConfirmationActivity.class);
        intent.putExtra("plan", _PLAN);
        intent.putExtra("talkTime", _TALK_TIME);
        intent.putExtra("validity", _VALIDITY);
        intent.putExtra("operator", tvOperatorCircle.getText().toString());
        intent.putExtra("mobile", etMobileNumber.getText().toString());
        intent.putExtra("amount", etAmount.getText().toString());
        intent.putExtra("type", type);
        intent.putExtra("operator_id", _SPECIAL_OR_TOP_UP);
        intent.putExtra("label", _LABEL);


        Prefes.saveType(type, DthActivity.this);
        Prefes.saveOperatorUrl(opUrl,DthActivity.this);
        Prefes.savePhoneNumber(etMobileNumber.getText().toString(),DthActivity.this);
        Prefes.saveOperatorID(_SPECIAL_OR_TOP_UP,DthActivity.this);
        Prefes.saveCircle(operatorName,DthActivity.this);
        Prefes.saveOperatorDID(operatorDetail,DthActivity.this);


        startActivity(intent);
    }

    private void setListeners() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefes.getUserData(DthActivity.this).equalsIgnoreCase("")) {
                    M.makeChecks(DthActivity.this , LoginActivity.class);
                } else if (tvOperatorCircle.getText().length() == 0) {
                    Toast.makeText(DthActivity.this, getString(R.string.valid_operator), Toast.LENGTH_SHORT).show();
                } else if (etMobileNumber.getText().toString().length() == 0 || etMobileNumber.getText().toString().trim().length() == 0 || etMobileNumber.getText().toString().trim().length() < _DEFAULT_CHECK_MIN || etMobileNumber.getText().toString().trim().length() > _DEFAULT_CHECK_MAX) {
                    Toast.makeText(DthActivity.this, _LABEL.equals("") ? "Please enter valid customer id" : "Please enter valid " + _LABEL.toLowerCase(), Toast.LENGTH_SHORT).show();
                } else if (etAmount.getText().toString().length() == 0 || etAmount.getText().toString().trim().length() == 0 || Integer.valueOf(etAmount.getText().toString()) > 500000) {
                    Toast.makeText(DthActivity.this, getString(R.string.valid_amount), Toast.LENGTH_SHORT).show();
                } else {
                    makeRechargeRequest();
                }
            }
        });

        tvOperatorCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutToBeVisible.setVisibility(View.GONE);
                Intent intent = new Intent(DthActivity.this, SelectDthOperatorActivity.class);
                intent.putExtra(ApiParams.type, type);
                startActivity(intent);

            }
        });
        seePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DthActivity.this, SeeDthPlansActivity.class);
                intent.putExtra("type", type);
                intent.putExtra(ApiParams.operator, operatorName);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    private void makeRadioButton(String operatorName) {
        etAmount.setEnabled(true);
        etMobileNumber.setEnabled(true);
        try {
            mSuccess = this.jsonObject.optString(ApiParams.success);
            if (mSuccess.equalsIgnoreCase("true")) {
                JSONArray j = this.jsonObject.getJSONArray(ApiParams.data);
                for (int k = 0; k < j.length(); k++) {
                    JSONObject jsonObject1 = j.getJSONObject(k);
                    if (operatorName.equalsIgnoreCase(jsonObject1.getString("operator"))) {
                        _SPECIAL_OR_TOP_UP = jsonObject1.getString("id");

                        JSONArray jsonObject2 = jsonObject1.getJSONArray(ApiParams.operator_urls);
                        if (jsonObject2.length() > 1) {

                        } else {
                            for (int l = 0; l < jsonObject2.length(); l++) {
                                JSONObject object = jsonObject2.getJSONObject(l);
                                operatorDetail=object.getString("id");
                             //   _SPECIAL_OR_TOP_UP = object.getString("id");
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

    String _LABEL = "customer ID";

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(final EventBusDth rechargeEventBus) {
        if (rechargeEventBus != null) {
            tvOperatorCircle.setText(rechargeEventBus.getOperator());
            operatorName = rechargeEventBus.getOperator();
            etMobileNumber.setText("");
            _DEFAULT_CHECK_MAX = Integer.valueOf(rechargeEventBus.getMaxLimit());
            _DEFAULT_CHECK_MIN = Integer.valueOf(rechargeEventBus.getMinLimit());


            _LABEL = rechargeEventBus.getLabel().replace(" or ", "/") + "";

            Log.i(">>well", "onEvent: " + _DEFAULT_CHECK_MAX + "\n" + _DEFAULT_CHECK_MIN + "\n" + _LABEL + "\n" + rechargeEventBus.getOperator());
            if (rechargeEventBus.getLabel().replace(" or ", "/").contains("Customer ID")) {
                _LABEL = "Customer ID";
                etMobileNumber.setHint(_LABEL);
            } else if (rechargeEventBus.getLabel().replace(" or ", "/").contains("Smart Card Number")) {
                _LABEL = "Smart Card Number";
                etMobileNumber.setHint(_LABEL);
            } else {
                etMobileNumber.setHint(_LABEL);
            }
            etAmount.setText("");
            makeRadioButton(operatorName);
            etMobileNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() >= _DEFAULT_CHECK_MIN && s.length() <= _DEFAULT_CHECK_MAX) {
                        layoutToBeVisible.setVisibility(View.VISIBLE);
                        etAmount.setText("");
                    } else if (s.length() == 0) {
                        etAmount.setText("");
                    } else {
                        layoutToBeVisible.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() > _DEFAULT_CHECK_MAX) {
                        s.replace(_DEFAULT_CHECK_MAX, etMobileNumber.length(), "");
                    }
                }
            });
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onPlanRechargeBus(final PlanRechargeBus planRechargeBus) {
        if (planRechargeBus != null) {
            etAmount.setText(planRechargeBus.getPlainAmount() + "");
            _VALIDITY = planRechargeBus.getValidity();
            _TALK_TIME = planRechargeBus.getTalkTime();
            _PLAN = Prefes.getPlan(DthActivity.this);
        }
    }
}
