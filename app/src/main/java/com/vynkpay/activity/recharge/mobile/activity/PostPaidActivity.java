package com.vynkpay.activity.recharge.mobile.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.vynkpay.activity.recharge.mobile.events.PlanRechargeBus;
import com.vynkpay.activity.recharge.mobile.events.RechargeEventBus;
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

public class PostPaidActivity extends AppCompatActivity {
    @BindView(R.id.tvOperatorCircle)
    NormalTextView tvOperatorCircle;
    @BindView(R.id.etMobileNumber)
    NormalEditText etMobileNumber;
    @BindView(R.id.etAmount)
    NormalEditText etAmount;
    @BindView(R.id.continueButton)
    NormalButton continueButton;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.openContacts)
    LinearLayout openContacts;
    @BindView(R.id.prepaidLayout)
    LinearLayout prepaidLayout;
    @BindView(R.id.fetchBillLayout)
    LinearLayout fetchBillLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    JSONObject userPreference;
    Dialog dialog;
    String type = "";

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpaid_rcg);

        EventBus.getDefault().register(this);
        dialog = M.showDialog(PostPaidActivity.this, "", false, false);
        ButterKnife.bind(PostPaidActivity.this);
        setListeners();

        type = getIntent().getStringExtra(ApiParams.type);
        Log.i(">>type", "onCreate: " + type);
        d1 = new DisposableSubscriber<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
                handleOperatorResponse(jsonObject);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.getMessage()!=null?t.getMessage():"Error");
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

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle.setText("Mobile Postpaid");

        tvOperatorCircle.setAlpha(.5f);
        tvOperatorCircle.setEnabled(false);
        etAmount.setEnabled(false);

        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 10 || s.length() > 10) {
                    tvOperatorCircle.setAlpha(.5f);
                    tvOperatorCircle.setEnabled(false);
                    tvOperatorCircle.setText("");
                    etAmount.setText("");
                    etAmount.setEnabled(false);
                } else if (s.length() == 10) {
                    //makeMobileNumberLookupRequest();
                    loadOperatorCircle();
                    tvOperatorCircle.setAlpha(1f);
                    tvOperatorCircle.setEnabled(true);
                    etAmount.setEnabled(true);
                }
                _SPECIAL_OR_TOP_UP = "";
                fetchBillLayout.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                M.removeZero(s);
            }

        });

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _PLAN = "";
                _VALIDITY = "";
                _TALKTIME = "";
            }

            @Override
            public void afterTextChanged(Editable s) {
                M.removeZero(s);
            }
        });

        fetchBillLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefes.getUserData(PostPaidActivity.this).equalsIgnoreCase("")) {
                    M.makeChecks(PostPaidActivity.this, LoginActivity.class);
                }else {
                    makeBillFetchRequest();
                }
            }
        });

    }

    private void loadOperatorCircle(){
        Intent intent = new Intent(PostPaidActivity.this, SelectOperatorActivity.class);
        intent.putExtra(ApiParams.type, type);
        startActivity(intent);
    }

    private void makeBillFetchRequest() {
        try {
            userPreference = new JSONObject(Prefes.getUserData(PostPaidActivity.this));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.recharge,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("fetchBillResponse", response);
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>billFetchResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString("success");
                            mMessage = jsonObject.optString("message");

                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject billObject = jsonObject.getJSONObject(ApiParams.data).getJSONObject(ApiParams.bill);
                                String amountFetch = billObject.getString(ApiParams.amount);
                                etAmount.setText(amountFetch);
                                etAmount.setSelection(etAmount.length());
                            } else {
                                if (jsonObject.has(ApiParams.error_code)) {
                                    if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_400)) {
                                        Iterator<?> keys = jsonObject.getJSONObject(ApiParams.errors).keys();
                                        while (keys.hasNext()) {
                                            String key = (String) keys.next();
                                            if (jsonObject.getJSONObject(ApiParams.errors).get(key) instanceof String) {
                                                String message = jsonObject.getJSONObject(ApiParams.errors).get(key).toString();
                                                M.dialogOk(PostPaidActivity.this, message, "");
                                            }
                                        }
                                    }
                                } else {
                                    M.dialogOk(PostPaidActivity.this, mMessage, "");
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
                params.put("mobile_number", etMobileNumber.getText().toString());
                params.put("amount", "10");
                params.put("operator_id", _SPECIAL_OR_TOP_UP);
                params.put("type", type);
                params.put("account", "10");
                params.put("bill_fetch", "1");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(PostPaidActivity.this));
                params.put("Accept", "application/json");

                Log.d("tokenOFBillFecth",  Prefes.getAccessToken(PostPaidActivity.this)+"//");
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(stringRequest);

        //MySingleton.getInstance(PostPaidActivity.this).addToRequestQueue(stringRequest);
    }

    private JSONObject jsonObject;

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
        Log.i(">>url", "getOperatorData: " + BuildConfig.APP_BASE_URL + URLS.operator + "?type=" + type);
        String Url = BuildConfig.APP_BASE_URL + URLS.operator + "?type=" + type;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Url, null, future, null);
        MySingleton.getInstance(PostPaidActivity.this).addToRequestQueue(req);
        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return future.get();
    }

    String operatorName="", circleName="",operatorID="",opUrl="",operatorDetail="";

    private void makeRechargeRequest() {

        Intent intent = new Intent(PostPaidActivity.this, RechargeConfirmationActivity.class);
        intent.putExtra("plan", _PLAN);
        intent.putExtra("talkTime", _TALKTIME);
        intent.putExtra("validity", _VALIDITY);
        intent.putExtra("operator", tvOperatorCircle.getText().toString());
        intent.putExtra("mobile", etMobileNumber.getText().toString());
        intent.putExtra("amount", etAmount.getText().toString());
        intent.putExtra("type", type);
        intent.putExtra("operator_id", operatorID);
        Log.e("operatorID",""+operatorID);
        Prefes.saveType(type,PostPaidActivity.this);
        Prefes.saveOperatorUrl(opUrl,PostPaidActivity.this);
        Prefes.savePhoneNumber(etMobileNumber.getText().toString(),PostPaidActivity.this);
        Prefes.saveOperatorID(_SPECIAL_OR_TOP_UP,PostPaidActivity.this);
        Prefes.saveCircle(circleName,PostPaidActivity.this);
        Prefes.saveOperatorDID(operatorDetail,PostPaidActivity.this);

        startActivity(intent);
    }

    private void setListeners() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefes.getUserData(PostPaidActivity.this).equalsIgnoreCase("")) {
                    M.makeChecks(PostPaidActivity.this, LoginActivity.class);
                } else if (etMobileNumber.getText().toString().length() == 0 || etMobileNumber.getText().toString().trim().length() == 0 || etMobileNumber.getText().toString().trim().length() < 10 || etMobileNumber.getText().toString().trim().length() > 10) {
                    Toast.makeText(PostPaidActivity.this, getString(R.string.valid_phone), Toast.LENGTH_SHORT).show();
                } else if (etAmount.getText().toString().length() == 0 || etAmount.getText().toString().trim().length() == 0 || Double.valueOf(etAmount.getText().toString()) > Double.valueOf(Integer.valueOf("500000"))) {
                    Toast.makeText(PostPaidActivity.this, getString(R.string.valid_amount), Toast.LENGTH_SHORT).show();
                } else {
                    makeRechargeRequest();
                }
            }
        });

        tvOperatorCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostPaidActivity.this, SelectOperatorActivity.class);
                intent.putExtra(ApiParams.type, type);
                startActivity(intent);
            }
        });


        openContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PostPaidActivity.this,
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PostPaidActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    startActivityForResult(intent, SELECT_CONTACT);
                }
            }
        });
        prepaidLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Intent intent = new Intent(PostPaidActivity.this, PrepaidActivity.class);
                        intent.putExtra(ApiParams.type, "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                };
                handler.postDelayed(runnable, SPLASH_TIME_OUT);
            }
        });
    }

    final static int SPLASH_TIME_OUT = 1000;
    Handler handler;
    Runnable runnable;
    final static int SELECT_CONTACT = 12, MY_PERMISSIONS_REQUEST_READ_CONTACTS = 122;
    String mSuccess, mMessage;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    startActivityForResult(intent, SELECT_CONTACT);
                }
                break;
            }
        }
    }

    DisposableSubscriber<JSONObject> d1;

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (reqCode) {
                case SELECT_CONTACT:
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            if (numbers.getCount() > 1) {
                                int i = 0;
                                String[] phoneNum = new String[numbers.getCount()];
                                String[] type = new String[numbers.getCount()];
                                String name = "";
                                while (numbers.moveToNext()) {
                                    name = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                    phoneNum[i] = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    type[i] = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(this.getResources(), numbers.getInt(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)), ""); // insert a type string in front of the number
                                    i++;
                                }
                                makeDialogForMultipleNumbers(phoneNum, type, name);
                            } else {
                                while (numbers.moveToNext()) {
                                    String num1 = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    num = num1.replaceAll("[^\\d.]", "");
                                    if (num.contains("+")) {
                                        etMobileNumber.setText(num.substring(3, num.length()).replaceAll("[^\\d.]", "").replace("91", ""));
                                    } else if (num.charAt(0) == 0) {
                                        etMobileNumber.setText(num.substring(1, num.length()).replaceAll("[^\\d.]", "").replace("0", ""));
                                    } else if (num.startsWith("0")) {
                                        String mContact = num.substring(num.indexOf("0") + 1);
                                        etMobileNumber.setText(mContact.replaceAll("[^\\d.]", "").replace("91", ""));
                                    } else {
                                        etMobileNumber.setText(num.replaceAll("[^\\d.]", "").replace("91", ""));
                                    }
                                    etMobileNumber.clearFocus();
                                    etMobileNumber.setSelection(etMobileNumber.length());
                                }
                            }
                        }
                    }
                    c.close();
                    break;
            }
        }
    }

    private void makeDialogForMultipleNumbers(String[] phoneNum, String[] type, String name) {
        final Dialog dialog = new Dialog(PostPaidActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_multiple_numbers_rcg);
        RadioGroup r = dialog.findViewById(R.id.multipleNumberGroup);
        NormalTextView displayName = dialog.findViewById(R.id.displayName);
        LinearLayout closeButton = dialog.findViewById(R.id.closeButton);

        displayName.setText(name);
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT; // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lWindowParams);
        for (int k = 0; k < phoneNum.length; k++) {
            RadioButton radioButton = new RadioButton(PostPaidActivity.this);
            radioButton.setText(type[k] + "\n" + phoneNum[k]);
            radioButton.setPadding(0, 15, 0, 15);
            radioButton.setId(k);
            r.addView(radioButton);
            radioButton.setChecked(k == 0);
        }
        dialog.show();
        r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadio = group.findViewById(group.getCheckedRadioButtonId());
                String num1 = checkedRadio.getText().toString();
                String num = num1.replaceAll("[^\\d.]", "");
                if (num.contains("+")) {
                    etMobileNumber.setText(num.substring(3, num.length()).replaceAll("[^\\d.]", "").replace("91", ""));
                } else if (num.charAt(0) == 0) {
                    etMobileNumber.setText(num.substring(1, num.length()).replaceAll("[^\\d.]", "").replace("0", ""));
                } else if (num.startsWith("0")) {
                    String mContact = num.substring(num.indexOf("0") + 1);
                    etMobileNumber.setText(mContact.replaceAll("[^\\d.]", "").replace("91", ""));
                } else {
                    etMobileNumber.setText(num.replaceAll("[^\\d.]", "").replace("91", ""));
                }
                etMobileNumber.clearFocus();
                etMobileNumber.setSelection(etMobileNumber.length());
                dialog.dismiss();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostPaidActivity.this, "No number selected", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void makeMobileNumberLookupRequest() {
        dialog.show();
        String url = BuildConfig.APP_BASE_URL + URLS.mobileLookup;// + "?" + ApiParams.mobile_number + "=" + etMobileNumber.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>numberFetch", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);
                                String operator = j.getString(ApiParams.operator);
                                String circle = j.getString(ApiParams.circle);
                                tvOperatorCircle.setText(operator + "-" + circle);
                                operatorName = operator;
                                circleName = circle;
                                makeRadioButton(operatorName);
                            } else {
                                if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_400)) {
                                    Iterator<?> keys = jsonObject.getJSONObject(ApiParams.errors).keys();
                                    while (keys.hasNext()) {
                                        String key = (String) keys.next();
                                        if (jsonObject.getJSONObject(ApiParams.errors).get(key) instanceof String) {
                                            String message = jsonObject.getJSONObject(ApiParams.errors).get(key).toString();
                                            M.dialogOk(PostPaidActivity.this, message, "Try again");
                                        }
                                    }
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
                params.put(ApiParams.mobile_number,etMobileNumber.getText().toString());
                params.put(ApiParams.type,"1");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };

        MySingleton.getInstance(PostPaidActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    String _SPECIAL_OR_TOP_UP = "";

    private void makeRadioButton(String operatorName) {
        try {
            mSuccess = this.jsonObject.optString(ApiParams.success);
            if (mSuccess.equalsIgnoreCase("true")) {
                JSONArray j = this.jsonObject.getJSONArray(ApiParams.data);
                for (int k = 0; k < j.length(); k++) {
                    JSONObject jsonObject1 = j.getJSONObject(k);
                    if (operatorName.equalsIgnoreCase(jsonObject1.getString(ApiParams.operator))) {

                        operatorID=jsonObject1.getString("id");


                        JSONArray jsonObject2 = jsonObject1.getJSONArray(ApiParams.operator_urls);
                        if (jsonObject2.length() > 1) {



                        } else {
                            for (int l = 0; l < jsonObject2.length(); l++) {
                                JSONObject object = jsonObject2.getJSONObject(l);
                                operatorDetail=object.getString("id");
                              //  _SPECIAL_OR_TOP_UP = object.getString("id");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.i(">>exception", "onResponse: " + e.getMessage());
            e.printStackTrace();
        }
        Log.i(">>id", "onResponse: " + _SPECIAL_OR_TOP_UP);

        if (_SPECIAL_OR_TOP_UP.equalsIgnoreCase("2")) {
            fetchBillLayout.setVisibility(View.VISIBLE);
        } else {
            fetchBillLayout.setVisibility(View.GONE);
        }

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onEvent(RechargeEventBus rechargeEventBus) {
        if (rechargeEventBus != null) {
            tvOperatorCircle.setText(rechargeEventBus.getOperator() + "-" + rechargeEventBus.getCircleName());
            operatorName = rechargeEventBus.getOperator();
            circleName = rechargeEventBus.getCircleName();
            Log.i(">>data", "inEventBus: " + operatorName + "-" + circleName + _SPECIAL_OR_TOP_UP);
            makeRadioButton(operatorName);
            if (_SPECIAL_OR_TOP_UP.equalsIgnoreCase("2")) {
                fetchBillLayout.setVisibility(View.VISIBLE);
            } else {
                fetchBillLayout.setVisibility(View.GONE);
            }
        }
    }

    String _VALIDITY = "";
    String _TALKTIME = "";
    String _PLAN = "";

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onPlanRechargeBus(final PlanRechargeBus planRechargeBus) {
        if (planRechargeBus != null) {
            etAmount.setText(planRechargeBus.getPlainAmount() + "");
            _VALIDITY = planRechargeBus.getValidity();
            _TALKTIME = planRechargeBus.getTalkTime();
            _PLAN = Prefes.getPlan(PostPaidActivity.this);
        }
    }

}
