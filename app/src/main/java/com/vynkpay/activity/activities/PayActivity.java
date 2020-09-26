package com.vynkpay.activity.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class PayActivity extends AppCompatActivity {
    @BindView(R.id.etMobileNumber)
    NormalEditText etMobileNumber;
    @BindView(R.id.etAmount)
    NormalEditText etAmount;
    @BindView(R.id.transferNowButton)
    NormalButton transferNowButton;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewAdded)
    LinearLayout viewAdded;
    @BindView(R.id.showView)
    LinearLayout showView;
    @BindView(R.id.scanner)
    ZXingScannerView scanner;
    Dialog dialog;
    JSONObject userPreference;
    ZXingScannerView l;
    @BindView(R.id.openContacts)
    LinearLayout openContacts;
    @BindView(R.id.scanLayout)
    LinearLayout scanLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_rcg);
        ButterKnife.bind(PayActivity.this);
        dialog = M.showDialog(PayActivity.this, "", false, false);
        //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
        setListeners();
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbarTitle.setText("Pay using VynkPay");
        try {
            userPreference = new JSONObject(Prefes.getUserData(PayActivity.this));
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
                if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                    s.clear();
                } else if (s.toString().length() == 1 && s.toString().startsWith(".")) {
                    s.clear();
                }
            }
        });
        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 10 || s.length() > 10) {
                    showView.setVisibility(View.GONE);
                } else {
                    showView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                    s.clear();
                } else if (s.toString().length() == 1 && s.toString().startsWith(".")) {
                    s.clear();
                }
            }
        });

        scanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAdded.setVisibility(View.VISIBLE);
                scanLayout.setVisibility(View.GONE);
                scanner.startCamera();
            }
        });

        scanner.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                viewAdded.setVisibility(View.GONE);
                scanLayout.setVisibility(View.VISIBLE);
                etMobileNumber.setText(result.getText()+"");
                Log.d("scannerHandlerLOG", result.getText()+"//");
            }
        });
    }

    String userID;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 122, SELECT_CONTACT = 12;

    private void setListeners() {
        transferNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMobileNumber.getText().length() == 0 || etMobileNumber.getText().toString().trim().length() == 0 || etMobileNumber.getText().length() < 10 || etMobileNumber.getText().length() > 10) {
                    Toast.makeText(PayActivity.this, getString(R.string.valid_phone), Toast.LENGTH_SHORT).show();
                } else if (etAmount.getText().length() == 0 || etAmount.getText().length() > 6 || etAmount.getText().toString().trim().length() == 0) {
                    Toast.makeText(PayActivity.this, getString(R.string.valid_amount), Toast.LENGTH_SHORT).show();
                } else if (Double.valueOf(etAmount.getText().toString()) > 500000 || Double.valueOf(etAmount.getText().toString()) == 500000) {
                    Toast.makeText(PayActivity.this, "Amount should be less than 500,000", Toast.LENGTH_SHORT).show();
                } else {
                    makeTransferAmountRequest();
                }
            }
        });
        openContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PayActivity.this,
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PayActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                    startActivityForResult(intent, SELECT_CONTACT);
                }
            }
        });
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
        final Dialog dialog = new Dialog(PayActivity.this);
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
            RadioButton radioButton = new RadioButton(PayActivity.this);
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
                Toast.makeText(PayActivity.this, "No number selected", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    String mSuccess, mMessage;

    private void makeTransferAmountRequest() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.transferAmount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>forgotPassword", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                etAmount.setText("");
                                JSONObject dataObject = jsonObject.getJSONObject(ApiParams.data);
                                String created_at = dataObject.getString("created_at");
                                String amount = dataObject.getString("amount");
                                String id = dataObject.getString("txn");
                                makeSuccessDialog(created_at, amount, id, mSuccess);
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
                params.put(ApiParams.access_token, Prefes.getAccessToken(PayActivity.this));
                params.put("Accept", "application/json");
                return params;
            }

        };
        MySingleton.getInstance(PayActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    NormalButton okButton;
    NormalTextView textResult, tvMobileNumber, tvAmount, tvTransactionId, tvDateTime;
    ImageView imageResult;
    Dialog dialog1;

    public boolean isMade;

    private void makeSuccessDialog(String date, String amount, String id, String mSuccess) {
        dialog1 = M.inflateDialog(PayActivity.this, R.layout.dialog_payment_result_rcg);
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
            imageResult.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_icon));
            textResult.setText("Transfer Successful");
            isMade = true;
        } else {
            imageResult.setBackgroundDrawable(getResources().getDrawable(R.drawable.app_icon));
            textResult.setText("Transfer Fail");
            isMade = false;
        }
        dialog1.show();
    }

    private void makeErrorDialog(String mMessage) {
        final Dialog errorDialog = M.inflateDialog(PayActivity.this, R.layout.dialog_error_message_rcg);
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
}
