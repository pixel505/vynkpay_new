package com.vynkpay.activity.recharge.mobile.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
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
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.activity.recharge.mobile.events.PlanRechargeBus;
import com.vynkpay.activity.recharge.mobile.events.RechargeEventBus;
import com.vynkpay.custom.CustomRadioButton;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class PrepaidActivity extends AppCompatActivity {

    @BindView(R.id.prepaidLayout)
    LinearLayout prepaidLayout;
    @BindView(R.id.postPaidLayout)
    LinearLayout postPaidLayout;
    @BindView(R.id.prepaidText)
    NormalTextView prepaidText;
    @BindView(R.id.postPaidText)
    NormalTextView postPaidText;
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
    String type = "";
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rootLayoutForRadio)
    LinearLayout rootLayoutForRadio;
    @BindView(R.id.openContacts)
    LinearLayout openContacts;
    JSONObject userPreference;
    String accessToken;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().removeAllStickyEvents();
        }

        // Removal of EventBus
        EventBus.getDefault().unregister(this);
        if (d1 != null) {
            d1.dispose();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepaid_rcg);

        // Registration of EventBus
        EventBus.getDefault().register(this);
        dialog = M.showDialog(PrepaidActivity.this, "", false, false);
        ButterKnife.bind(PrepaidActivity.this);
        setListeners();

        d1 = new DisposableSubscriber<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
                handleOperatorResponse(jsonObject);
            }

            @Override
            public void onError(Throwable t) {
                // todo
                System.out.println(t.getMessage() != null ? t.getMessage() : "Error!");
            }

            @Override
            public void onComplete() {
                System.out.println("Success!");
            }
        };

        /**API calling method for RX java*/
        getOperatorCircle()
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

        toolbarTitle.setText("Mobile Prepaid");


        if(getIntent().getStringExtra("type")!=null) {
            type = getIntent().getStringExtra("type");
        }
        tvOperatorCircle.setAlpha(.5f);
        tvOperatorCircle.setEnabled(false);
        etAmount.setEnabled(false);
        layoutToBeVisible.setVisibility(View.GONE);
        rootLayoutForRadio.setVisibility(View.GONE);

        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _SPECIAL_OR_TOP_UP = "";
                if (s.length() < 10 || s.length() > 10) {
                    tvOperatorCircle.setAlpha(.5f);
                    tvOperatorCircle.setEnabled(false);
                    tvOperatorCircle.setText("");
                    etAmount.setText("");
                    etAmount.setEnabled(false);
                    rootLayoutForRadio.removeAllViews();
                    rootLayoutForRadio.invalidate();
                    rootLayoutForRadio.setVisibility(View.GONE);
                    layoutToBeVisible.setVisibility(View.GONE);
                } else if (s.length() == 10) {
                    makeMobileNumberLookupRequest();
                    loadAddCircle();
                    tvOperatorCircle.setAlpha(1f);
                    tvOperatorCircle.setEnabled(true);
                    etAmount.setEnabled(true);
                    rootLayoutForRadio.setVisibility(View.VISIBLE);
                    if ((tvOperatorCircle.getText().length() < 0)) {
                        layoutToBeVisible.setVisibility(View.GONE);
                    } else {
                        layoutToBeVisible.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                M.removeZero(s);
                etAmount.setKeyListener(DigitsKeyListener.getInstance(true, true));
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


    }

    private JSONObject jsonObject;

    private void handleOperatorResponse(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        Log.i(">>handledResponse", "handleOperatorResponse: " + jsonObject.toString());
    }

    /**Related to RX java*/
    public Flowable<JSONObject> getOperatorCircle() {
        return Flowable.defer( () -> Flowable.just(callOperatorApi()));
    }

    private JSONObject callOperatorApi() throws ExecutionException, InterruptedException, RuntimeException {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String Url = BuildConfig.APP_BASE_URL + URLS.operator + "?type=" + type;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Url, null, future, null);
        MySingleton.getInstance(PrepaidActivity.this).addToRequestQueue(req);
        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return future.get();
    }

    String operatorName, circleName,opUrl;

    private void makeRechargeRequest() {
        Intent intent = new Intent(PrepaidActivity.this, RechargeConfirmationActivity.class);
        intent.putExtra("plan", _PLAN);
        intent.putExtra("talkTime", _TALKTIME);
        intent.putExtra("validity", _VALIDITY);
        intent.putExtra("operator", tvOperatorCircle.getText().toString());
        intent.putExtra("mobile", etMobileNumber.getText().toString());
        intent.putExtra("amount", etAmount.getText().toString());
        intent.putExtra("type", type);
        intent.putExtra("operator_id", _SPECIAL_OR_TOP_UP);





        Prefes.saveType(type,PrepaidActivity.this);
        Prefes.saveOperatorUrl(opUrl,PrepaidActivity.this);
        Prefes.savePhoneNumber(etMobileNumber.getText().toString(),PrepaidActivity.this);
        Prefes.saveOperatorID(_SPECIAL_OR_TOP_UP,PrepaidActivity.this);
        Prefes.saveCircle(circleName,PrepaidActivity.this);
        Prefes.saveOperatorDID(operatorDetail,PrepaidActivity.this);




        startActivity(intent);
    }

    private void setListeners() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefes.getUserData(PrepaidActivity.this).equalsIgnoreCase("")) {
                    M.makeChecks(PrepaidActivity.this, LoginActivity.class);
                } else if (etMobileNumber.getText().toString().length() == 0 || etMobileNumber.getText().toString().trim().length() == 0 || etMobileNumber.getText().toString().trim().length() < 10 || etMobileNumber.getText().toString().trim().length() > 10) {
                    Toast.makeText(PrepaidActivity.this, getString(R.string.valid_phone), Toast.LENGTH_SHORT).show();
                } else if (etAmount.getText().toString().length() == 0 || etAmount.getText().toString().trim().length() == 0 || Double.valueOf(etAmount.getText().toString()) > Double.valueOf(Integer.valueOf("500000"))) {
                    Toast.makeText(PrepaidActivity.this, getString(R.string.valid_amount), Toast.LENGTH_SHORT).show();
                } else {
                    makeRechargeRequest();
                }
            }
        });

        tvOperatorCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrepaidActivity.this, SelectOperatorActivity.class);
                intent.putExtra(ApiParams.type, type);
                startActivity(intent);
            }
        });
        seePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrepaidActivity.this, SeePlanActivity.class);
                intent.putExtra(ApiParams.type, type);
                intent.putExtra(ApiParams.operator, operatorName);
                intent.putExtra(ApiParams.circle, circleName);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        openContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (ContextCompat.checkSelfPermission(PrepaidActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PrepaidActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, SELECT_CONTACT);
                }*/
               Log.d("calledd","pre");
               requestContactPermission();
            }
        });

        postPaidLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Intent intent = new Intent(PrepaidActivity.this, PostPaidActivity.class);
                        intent.putExtra(ApiParams.type, "2");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                };
                handler.postDelayed(runnable, SPLASH_TIME_OUT);
            }
        });
    }

    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("calledd","pre");
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_CONTACTS)) {

                    requestPermissions(
                            new String[]
                                    {android.Manifest.permission.READ_CONTACTS}
                            , MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                 /*   AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Read Contacts permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });
                    builder.show();*/
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                Log.d("calledd","pre1");
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, SELECT_CONTACT);
            }
        } else {
            Log.d("calledd","pre2");
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, SELECT_CONTACT);
        }
    }

    final static int SPLASH_TIME_OUT = 1000;
    Handler handler;
    Runnable runnable;
    final static int SELECT_CONTACT = 12, MY_PERMISSIONS_REQUEST_READ_CONTACTS = 122;
    String mSuccess, mMessage;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("calledd","pre3");
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, SELECT_CONTACT);
                } else {
                    Log.d("permission","notgranted");
                    //Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }

       /* switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
            }
        }*/
    }

    DisposableSubscriber<JSONObject> d1 ;

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

    RadioGroup r;

    private void makeDialogForMultipleNumbers(String[] phoneNum, String[] type, String name) {
        final Dialog dialog = new Dialog(PrepaidActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_multiple_numbers_rcg);
        RadioGroup r = dialog.findViewById(R.id.multipleNumberGroup);
        LinearLayout closeButton = dialog.findViewById(R.id.closeButton);
        NormalTextView displayName = dialog.findViewById(R.id.displayName);
        displayName.setText(name);
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT; // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lWindowParams);

        for (int k = 0; k < phoneNum.length; k++) {
            RadioButton radioButton = new RadioButton(PrepaidActivity.this);
            radioButton.setText(type[k] + "\n" + phoneNum[k]);
            radioButton.setPadding(0, 15, 0, 15);
            radioButton.setId(k);
            r.addView(radioButton);
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
                Toast.makeText(PrepaidActivity.this, "No number selected", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void loadAddCircle(){
        Intent intent = new Intent(PrepaidActivity.this, SelectOperatorActivity.class);
        intent.putExtra(ApiParams.type, type);
        startActivity(intent);
    }

    private void makeMobileNumberLookupRequest() {
        dialog.show();
        String url =  BuildConfig.APP_BASE_URL + URLS.mobileLookup;// + "?" + ApiParams.mobile_number + "=" + etMobileNumber.getText().toString();
        String SURL=url.replace(" ","%20");

        Log.d("SURLLLOG", SURL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("makrresp",jsonObject.toString());
                            mSuccess = jsonObject.getString(ApiParams.success);
                            mMessage = jsonObject.getString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);
                                String operator = j.getString(ApiParams.operator);
                                String circle = j.getString(ApiParams.circle);
                                tvOperatorCircle.setText(operator + "-" + circle);
                                operatorName = operator;
                                circleName = circle;
                                makeRadioButton(operatorName);
                            } else {
                                M.dialogOk(PrepaidActivity.this, mMessage, "Try again");
                                Log.d("SURLLLOG", mMessage);
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
                Log.d("SURLLLOG",etMobileNumber.getText().toString());
                return params;
            }

        };
        MySingleton.getInstance(PrepaidActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    String _SPECIAL_OR_TOP_UP = "";
    String operatorDetail = "";
    CustomRadioButton radioButton;
    RadioGroup radioGroup;
    boolean isUnderSpecialCategory = false;

    private void makeRadioButton(String operatorName) {
        try {
            mSuccess = this.jsonObject.optString(ApiParams.success);
            if (mSuccess.equalsIgnoreCase("true")) {
                JSONArray j = this.jsonObject.getJSONArray(ApiParams.data);
                for (int k = 0; k < j.length(); k++) {
                    JSONObject jsonObject1 = j.getJSONObject(k);
                    if (operatorName.equalsIgnoreCase(jsonObject1.getString("operator"))) {
                        Log.e("opid",""+jsonObject1.getString("id"));
                        JSONArray jsonObject2 = jsonObject1.getJSONArray(ApiParams.operator_urls);
                        radioGroup = new RadioGroup(PrepaidActivity.this);
                        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
                        radioGroup.setGravity(Gravity.CENTER_VERTICAL);

                        _SPECIAL_OR_TOP_UP=jsonObject1.getString("id");



                        if (jsonObject2.length() > 1) {
                            isUnderSpecialCategory = false;
                            for (int l = 0; l < jsonObject2.length(); l++) {
                                JSONObject object = jsonObject2.getJSONObject(l);
                                String id = object.getString(ApiParams.id);

                                Log.e("idis",""+object.getString("type"));
                                String radioType = object.getString(ApiParams.type);
                                radioButton = new CustomRadioButton(PrepaidActivity.this);
                                radioButton.setId(Integer.valueOf(id));
                                radioButton.setText(radioType);
                                radioButton.setTag(radioType + "");
                                radioGroup.addView(radioButton);
                                radioButton.setChecked(radioButton.getText().toString().equalsIgnoreCase("Top up"));

                                Log.e("value",""+radioButton.getText().toString());
                                opUrl=radioButton.getText().toString();


                            }
                            Log.e("value1",""+radioButton.getText().toString());

                            operatorDetail = radioGroup.getCheckedRadioButtonId() + "";
                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    etAmount.setText("");
                                operatorDetail = radioGroup.getCheckedRadioButtonId() + "";
                                    Log.e("operatorDetail",""+operatorDetail);
                                    Log.e("value2",""+radioButton.getText().toString());


                                }
                            });
                            rootLayoutForRadio.addView(radioGroup);
                        } else {
                            isUnderSpecialCategory = true;
                            for (int l = 0; l < jsonObject2.length(); l++) {
                                JSONObject object = jsonObject2.getJSONObject(l);
                                operatorDetail = object.getString("id");
                                Log.i(">>operatorId", "prepaid: " + operatorDetail);
                            }
                            rootLayoutForRadio.setVisibility(View.GONE);
                        }
                    }


                }
            }
        } catch (Exception e) {
            Log.i(">>exception", "onResponse: " + e.getMessage());
            e.printStackTrace();
        }

    }

    //Circle operator
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(RechargeEventBus rechargeEventBus) {
        if (rechargeEventBus != null) {
            tvOperatorCircle.setText(rechargeEventBus.getOperator() + "-" + rechargeEventBus.getCircleName());
            operatorName = rechargeEventBus.getOperator();
            circleName = rechargeEventBus.getCircleName();
            makeRadioButton(operatorName);
            Log.i(">>data", "inEventBus: " + operatorName + "-" + circleName);
        }
    }

    String _VALIDITY = "";
    String _TALKTIME = "";
    String _PLAN = "";

       //plans
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onPlanRechargeBus(final PlanRechargeBus planRechargeBus) {
        if (planRechargeBus != null) {
            Log.i(">>logi", "onPlanRechargeBus: " + planRechargeBus.getPlainAmount());
            etAmount.setText(planRechargeBus.getPlainAmount());
            etAmount.setSelection(etAmount.length());
            _VALIDITY = planRechargeBus.getValidity();
            _TALKTIME = planRechargeBus.getTalkTime();
            _PLAN = Prefes.getPlan(PrepaidActivity.this);
            if (!isUnderSpecialCategory) {
                if (_PLAN.equalsIgnoreCase("Top up") || _PLAN.equalsIgnoreCase("Full Talk Time")) {
                    RadioButton checkedRadioButton = radioGroup.findViewWithTag("Top up");
                    checkedRadioButton.setChecked(true);
                } else {
                    RadioButton checkedRadioButton = radioGroup.findViewWithTag("Special");
                    checkedRadioButton.setChecked(true);
                }

                //_SPECIAL_OR_TOP_UP = radioGroup.getCheckedRadioButtonId() + "";
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton checkedRadioButton = group.findViewById(checkedId);
                        boolean isChecked = checkedRadioButton.isChecked();
                        if (isChecked) {
                     //       _SPECIAL_OR_TOP_UP = checkedRadioButton.getId() + "";
                        }
                    }
                });
            }
        }
    }

}
