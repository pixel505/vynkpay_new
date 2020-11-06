package com.vynkpay.activity.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.events.UpDateUIEvent;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.LogoutResponse;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class AccountActivity extends AppCompatActivity {
    @BindView(R.id.editImage)
    ImageView editImage;
    @BindView(R.id.editLayout)
    LinearLayout editLayout;
    @BindView(R.id.changePasswordLayout)
    LinearLayout changePasswordLayout;
    @BindView(R.id.userEmail)
    NormalEditText userEmail;
    @BindView(R.id.userName)
    NormalEditText userName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvEdit)
    NormalTextView tvEdit;
    @BindView(R.id.genderSpinner)
    Spinner genderSpinner;
    @BindView(R.id.userPhone)
    NormalTextView userPhone;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.submitButton)
    NormalButton submitButton;
    @BindView(R.id.stateET)
    NormalEditText stateET;
    @BindView(R.id.cityET)
    NormalEditText cityET;
    @BindView(R.id.postalCodeET)
    NormalEditText postalCodeET;
    @BindView(R.id.addressET)
    NormalEditText addressET;
    @BindView(R.id.referralIDTV)
    NormalTextView referralIDTV;
    @BindView(R.id.imageView)
    CircleImageView imageView;

    @BindView(R.id.chkNotification)
    SwitchCompat chkNotification;

    @BindView(R.id.linNotification)
    LinearLayout linNotification;


    boolean isEnabled = false;
    JSONObject userPreference;
    String accessToken;
    Dialog dialog1;

    String kyc_status;
    SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_rcg);
        ButterKnife.bind(AccountActivity.this);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);

        // //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
        accessToken = Prefes.getAccessToken(AccountActivity.this);
        dialog1 = M.showDialog(AccountActivity.this, "", false, false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        setListeners();
        toolbarTitle.setText("My Account");

        if (Prefes.getUserType(AccountActivity.this).equalsIgnoreCase("2")){
            linNotification.setVisibility(View.GONE);
        } else {
            linNotification.setVisibility(View.VISIBLE);
        }


        makeProfileRequest(accessToken);

        genderSpinner.setAdapter(M.makeSpinnerAdapterWhite(AccountActivity.this, genderArray));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvEdit.setText("Edit");
        submitButton.setVisibility(View.GONE);
    }

    Dialog dialog;
    private boolean isPasswordShown = false;
    private boolean isPasswordShown1 = false;
    private boolean isPasswordShown2 = false;
    String[] genderArray = {"Select Gender", "Male", "Female"};
    NormalButton okButton, cancelButton;
    NormalEditText oldPassword, confirmNewPassword, newPassword;
    ImageView showHideImage, showHideImage1, showHideImage2;

    private void updateAccountUI(boolean isUpdated) {
        if (isUpdated) {
            userName.setEnabled(false);
            stateET.setEnabled(false);
            cityET.setEnabled(false);
            postalCodeET.setEnabled(false);
            addressET.setEnabled(false);
            userName.setAlpha(.5f);
            stateET.setAlpha(.5f);
            cityET.setAlpha(.5f);
            postalCodeET.setAlpha(.5f);
            addressET.setAlpha(.5f);
            genderSpinner.setEnabled(false);
            genderSpinner.setClickable(false);
            genderSpinner.setAlpha(.5f);
            submitButton.setVisibility(View.GONE);
            editImage.setVisibility(View.GONE);
            tvEdit.setText("Edit");

        } else {
            userName.setEnabled(true);
            stateET.setEnabled(true);
            cityET.setEnabled(true);
            postalCodeET.setEnabled(true);
            addressET.setEnabled(true);
            userName.setAlpha(1f);
            if (userName.length() > 0) {
                userName.setSelection(userName.length());
            }

            stateET.setAlpha(1f);
            cityET.setAlpha(1f);
            postalCodeET.setAlpha(1f);
            addressET.setAlpha(1f);

            genderSpinner.setEnabled(true);
            genderSpinner.setClickable(true);
            genderSpinner.setAlpha(1f);
            submitButton.setVisibility(View.VISIBLE);
            editImage.setVisibility(View.VISIBLE);
            tvEdit.setText("Cancel");
        }
    }

    private void setListeners() {
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity(null)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                        .setRequestedSize(200, 200)
                        .setAspectRatio(1, 1)
                        .start(AccountActivity.this);
            }
        });

        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                updateAccountUI(isEnabled);
                isEnabled = !isEnabled;
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isEnabled) {
                            makeEditProfileRequest(accessToken);
                        }

                    }
                });
            }
        });

        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = M.inflateDialog(AccountActivity.this, R.layout.dialog_reset_rcg);
                okButton = dialog.findViewById(R.id.okButton);
                cancelButton = dialog.findViewById(R.id.cancelButton);
                oldPassword = dialog.findViewById(R.id.oldPassword);
                newPassword = dialog.findViewById(R.id.newPassword);
                showHideImage = dialog.findViewById(R.id.showHideImage);
                showHideImage1 = dialog.findViewById(R.id.showHideImage1);
                showHideImage2 = dialog.findViewById(R.id.showHideImage2);
                confirmNewPassword = dialog.findViewById(R.id.confirmNewPassword);
                M.showHidePassword(AccountActivity.this, isPasswordShown, oldPassword, showHideImage);
                M.showHidePassword(AccountActivity.this, isPasswordShown1, newPassword, showHideImage1);
                M.showHidePassword(AccountActivity.this, isPasswordShown2, confirmNewPassword, showHideImage2);

                showHideImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        M.showHidePassword(AccountActivity.this, isPasswordShown = !isPasswordShown, oldPassword, showHideImage);
                    }
                });

                showHideImage1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        M.showHidePassword(AccountActivity.this, isPasswordShown1 = !isPasswordShown1, newPassword, showHideImage1);
                    }
                });

                showHideImage2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        M.showHidePassword(AccountActivity.this, isPasswordShown2 = !isPasswordShown2, confirmNewPassword, showHideImage2);
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(oldPassword.getText().toString().trim())) {
                            Toast.makeText(AccountActivity.this, getString(R.string.old_password_fill), Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(newPassword.getText().toString().trim())) {
                            Toast.makeText(AccountActivity.this, getString(R.string.old_new_fill), Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(confirmNewPassword.getText().toString().trim())) {
                            Toast.makeText(AccountActivity.this, getString(R.string.old_confirm_fill), Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(oldPassword.getText().toString().trim())) {
                            Toast.makeText(AccountActivity.this, getString(R.string.old_not_valid_fill), Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(newPassword.getText().toString().trim())) {
                            Toast.makeText(AccountActivity.this, getString(R.string.new_not_valid_fill), Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(confirmNewPassword.getText().toString().trim())) {
                            Toast.makeText(AccountActivity.this, getString(R.string.confirm_not_valid_fill), Toast.LENGTH_SHORT).show();
                        } else if (newPassword.getText().length() < 6) {
                            Toast.makeText(AccountActivity.this, getString(R.string.new_not_valid_fill), Toast.LENGTH_SHORT).show();
                        } else if (!newPassword.getText().toString().equals(confirmNewPassword.getText().toString())) {
                            Toast.makeText(AccountActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            makeChangePasswordRequest(accessToken,
                                    oldPassword.getText().toString(),
                                    newPassword.getText().toString(),
                                    confirmNewPassword.getText().toString());
                        }

                    }
                });
                dialog.show();
            }
        });

        if (sp.getString("isSound","1").equalsIgnoreCase("1")){
            chkNotification.setChecked(true);
        }else {
            chkNotification.setChecked(false);
        }

        chkNotification.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                setChkNotification("1");
            } else {
                setChkNotification("0");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (data != null) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    Bitmap bitmap;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        Uri tempUri = getImageUri(AccountActivity.this, bitmap);
                        imageView.setImageURI(tempUri);
                        ApiCalls.updateProfile(AccountActivity.this, bitmap, accessToken, new VolleyResponse() {
                            @Override
                            public void onResult(String result, String status, String message) {
                                Log.d("uploadImageResponse", result);
                                Toast.makeText(AccountActivity.this, message+"", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void getDatePicker() {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(AccountActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                /*      Your code   to get date and time    */
                selectedmonth = selectedmonth + 1;
                //dob.setText(M.changeDateFormat(("" + selectedyear + "-" + selectedmonth + "-" + selectedday) + " " + "00:12:45"));
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();

        mDatePicker.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    mDatePicker.dismiss();
                }
            }
        });

    }

    String mSuccess, mMessage;
    String genderString;

    private void makeProfileRequest(final String accessToken) {
        dialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.user,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("accountResponseLOG", "onResponse: " + response);
                        try {
                            dialog1.dismiss();
                            JSONObject jsonObject = new JSONObject(response);

                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data);
                                genderString = jsonObject1.getString(ApiParams.gender);
                                String mobileNumberString = jsonObject1.getString(ApiParams.mobile_number);
                                String emailString = jsonObject1.getString(ApiParams.email);
                                String dobString = jsonObject1.getString(ApiParams.dob);
                                String fullNameString = jsonObject1.getString(ApiParams.full_name);
                                String referral_code = jsonObject1.getString("referral_code");
                                String image = jsonObject1.getString("image");
                                String address = jsonObject1.getString("address");
                                String city = jsonObject1.getString("city");
                                String state = jsonObject1.getString("state");
                                String zip_code = jsonObject1.getString("zip_code");
                                referralIDTV.setText(referral_code);

                                if (!jsonObject1.isNull("image")){
                                    if (!image.isEmpty()){
                                        Picasso.get().load(BuildConfig.BASE_URL+image).into(imageView);
                                    }
                                }

                                if (!jsonObject1.isNull("address")){
                                    addressET.setText(address);
                                }

                                if (!jsonObject1.isNull("city")){
                                    cityET.setText(city);
                                }

                                if (!jsonObject1.isNull("state")){
                                    stateET.setText(state);
                                }

                                if (!jsonObject1.isNull("zip_code")){
                                    postalCodeET.setText(zip_code);
                                }

                                stateET.setAlpha(.5f);
                                cityET.setAlpha(.5f);
                                postalCodeET.setAlpha(.5f);
                                addressET.setAlpha(.5f);

                                userPhone.setText(mobileNumberString.equals("null") ? "-" : mobileNumberString);
                                userPhone.setAlpha(.5f);
                                if (fullNameString.equals("null")) {
                                    userName.setHint("Name");
                                } else {
                                    userName.setText(fullNameString);
                                }

                                userName.setAlpha(.5f);
                                userEmail.setText(emailString.equals("null") ? "-" : emailString);
                                userEmail.setAlpha(.5f);

                                genderSpinner.setAlpha(.5f);
                                genderSpinner.setEnabled(false);
                                genderSpinner.setSelection(genderString.equalsIgnoreCase("null")
                                        || genderString.equalsIgnoreCase("") ?
                                        0 :
                                        genderString.equalsIgnoreCase("m") ? 1 : 2);
                                //gender.setText(genderString.equals("null") ? "N.A" : genderString)

                            } else {
                                M.dialogOk(AccountActivity.this, mMessage, "Error");
                            }
                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog1.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, accessToken);
                return params;
            }
        };
        MySingleton.getInstance(AccountActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void makeChangePasswordRequest(final String accessToken, final String old, final String newPass, final String confirmNew) {
        dialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.changePassword,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog1.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>changePassResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                Toast.makeText(AccountActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                                MainApplication.getApiService().logoutMethod(Prefes.getAccessToken(AccountActivity.this)).enqueue(new Callback<LogoutResponse>() {
                                    @Override
                                    public void onResponse(Call<LogoutResponse> call, retrofit2.Response<LogoutResponse> response) {
                                        if(response.isSuccessful()){
                                            Toast.makeText(AccountActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                                            Prefes.clear(AccountActivity.this);
                                            if(sp.getString("value","").equals("Global") && Prefes.getisIndian(AccountActivity.this).equals("NO")){
                                                startActivity(new Intent(AccountActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                finish();
                                            }

                                            else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(AccountActivity.this).equals("YES")){
                                                startActivity(new Intent(AccountActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                finish();
                                            }


                                            else if(sp.getString("value","").equals("India") && Prefes.getisIndian(AccountActivity.this).equals("YES")){
                                                startActivity(new Intent(AccountActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                finish();
                                            }

                                            else if(sp.getString("value","").equals("India") && Prefes.getisIndian(AccountActivity.this).equals("NO")){
                                                startActivity(new Intent(AccountActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<LogoutResponse> call, Throwable t) {
                                        Toast.makeText(AccountActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_400)) {
                                    Iterator<?> keys = jsonObject.getJSONObject(ApiParams.errors).keys();
                                    while (keys.hasNext()) {
                                        String key = (String) keys.next();
                                        if (jsonObject.getJSONObject(ApiParams.errors).get(key) instanceof String) {
                                            String message = jsonObject.getJSONObject(ApiParams.errors).get(key).toString();
                                            M.dialogOk(AccountActivity.this, message, "Try again");
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
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog1.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(ApiParams.old_password, old);
                map.put(ApiParams.password, newPass);
                map.put(ApiParams.cn_password, confirmNew);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, accessToken);
                return params;
            }

        };
        MySingleton.getInstance(AccountActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void makeEditProfileRequest(final String accessToken) {
        dialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.user,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog1.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>accountResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                Toast.makeText(AccountActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                                updateAccountUI(true);
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data);
                                String genderString = jsonObject1.getString(ApiParams.gender);
                                String mobileNumberString = jsonObject1.getString(ApiParams.mobile_number);
                                String emailString = jsonObject1.getString(ApiParams.email);
                                String dobString = jsonObject1.getString(ApiParams.dob);
                                String fullNameString = jsonObject1.getString(ApiParams.full_name);
                                userPhone.setText(mobileNumberString.equals("null") ? "-" : mobileNumberString);

                                if (fullNameString.equals("null")) {
                                    userName.setHint("Name");
                                } else {
                                    userName.setText(fullNameString);
                                }

                                userEmail.setText(emailString.equals("null") ? "-" : emailString);
                                genderSpinner.setSelection(genderString.equalsIgnoreCase("null") || genderString.equalsIgnoreCase("") ?
                                        0 :
                                        genderString.equalsIgnoreCase("m") ? 1 : 2);

                                Prefes.saveName(userName.getText().toString(), AccountActivity.this);
                                EventBus.getDefault().postSticky(new UpDateUIEvent(true));

                            } else {
                                M.dialogOk(AccountActivity.this, mMessage, "Error");
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
                        dialog1.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (genderSpinner.getSelectedItemPosition() == 0) {
                    genderStringParam = "";
                } else {
                    genderStringParam = genderSpinner.getSelectedItem().toString().equalsIgnoreCase("male") ? "m" : "f";
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.full_name, userName.getText().toString());
                params.put("city", cityET.getText().toString());
                params.put("state", stateET.getText().toString());
                params.put("zip_code", postalCodeET.getText().toString());
                params.put("address", addressET.getText().toString());
                return params;
            }

            String genderStringParam;

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, accessToken);
                return params;
            }

        };

        MySingleton.getInstance(AccountActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    void setChkNotification(String text){
        Log.d("nResponse",text);
        MainApplication.getApiService().notificationSoundOnOff(Prefes.getAccessToken(AccountActivity.this),text,"1").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    Log.d("nResponse", response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        Log.d("nResponse", jsonObject.toString());
                        sp.edit().putString("isSound",text).apply();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("nError",t.getMessage()!=null?t.getMessage():"Error");
            }
        });
    }

}
