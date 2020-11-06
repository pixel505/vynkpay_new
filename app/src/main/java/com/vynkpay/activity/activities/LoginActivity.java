package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vynkpay.BuildConfig;
import com.vynkpay.newregistration.Register1Activity;
import com.vynkpay.newregistration.Register2Activity;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.activity.PinActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.events.UpDateUIEvent;
import com.vynkpay.fcm.Config;
import com.vynkpay.fcm.SharedPrefManager;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.LoginResponse;
import com.vynkpay.retrofit.model.VerifyPinResponse;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.vynkpay.utils.UserSharedPreferences;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
  @BindView(R.id.etLoginText)
  NormalEditText etLoginText;
  @BindView(R.id.etPassword)
  NormalEditText etPassword;
  @BindView(R.id.passwordLayout)
  FrameLayout passwordLayout;
  @BindView(R.id.loginButton)
  NormalButton loginButton;
  @BindView(R.id.signUpText)
  NormalTextView signUpText;
  @BindView(R.id.forgotPasswordTV)
  TextView forgotPasswordTV;
  @BindView(R.id.loginascustomer)
  TextView loginascustomer;
  @BindView(R.id.showHideImage)
  ImageView showHideImage;
  private boolean isPasswordShown = false;
  /*@BindView(R.id.forgotPassWordText)
  NormalTextView forgotPassWordText;*/
  String var = "";


  String regex = "^[0-9]*$";
  Dialog dialog;
  boolean isWhat = false;
  UserSharedPreferences mShraredPref;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.login_new);
    mShraredPref=new UserSharedPreferences(LoginActivity.this);

    ButterKnife.bind(LoginActivity.this);
    //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
    setListeners();

    if (getIntent() != null) {
      if (getIntent().hasExtra("teee")){
        var = getIntent().getStringExtra("teee");
      }

    } else {
      var = "0000000";
    }

    Log.e("var",""+var);


    FirebaseInstanceId.getInstance().getInstanceId()
            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
              @Override
              public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                  Log.w("fcmTokenLOG", "getInstanceId failed", task.getException());
                  return;
                }

                // Get new Instance ID token
                if (task.getResult()!=null){
                  String token = task.getResult().getToken();
                  Log.d("fcmTokenLOG", token);
                  SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                  SharedPreferences.Editor editor = pref.edit();
                  editor.putString("regId", token);
                  editor.apply();
                }
              }
            });


    Log.i(">>token", "onCreate:" + SharedPrefManager.getInstance(LoginActivity.this).getDeviceToken());
    dialog = M.showDialog(LoginActivity.this, "", false, false);


    if (getIntent() != null) {
      etLoginText.setText(getIntent().getStringExtra("takeData"));
      etLoginText.setSelection(etLoginText.length());
    }

    M.showHidePassword(LoginActivity.this, isPasswordShown, etPassword, showHideImage);

    Log.d("fcmTokkken", SharedPrefManager.getInstance(LoginActivity.this).getDeviceToken());
  }


  private void setListeners() {

    forgotPasswordTV.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        popupWithdrawalAmount();
      }
    });


    signUpText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Intent intent = new Intent(LoginActivity.this, Signupnew.class);
        Intent intent = new Intent(LoginActivity.this, Register1Activity.class);
               /* intent.putExtra("url", "register");
                intent.putExtra("title", "");*/
        startActivity(intent);
      }
    });
    showHideImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        M.showHidePassword(LoginActivity.this, isPasswordShown = !isPasswordShown, etPassword, showHideImage);
      }
    });
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (etLoginText.getText().toString().trim().length()==0){
          Toast.makeText(LoginActivity.this, getString(R.string.pleaseEnterUserName), Toast.LENGTH_SHORT).show();
        }

        else if(etPassword.getText().toString().trim().length()==0){
          Toast.makeText(LoginActivity.this, getString(R.string.pleaseEnterUserpass), Toast.LENGTH_SHORT).show();

        }


        else {
          makeLoginRequestWithPassword();
        }
      }
    });

     /*   forgotPassWordText.setOnCl   ickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                intent.putExtra("url", "forget-password");
                intent.putExtra("title", "Reset Password");
                startActivity(intent);

                *//*dialog1 = M.inflateDialog(LoginActivity.this, R.layout.dialog_forgot);
                okButton = dialog1.findViewById(R.id.okButton);
                cancelButton = dialog1.findViewById(R.id.cancelButton);
                etEmail1 = dialog1.findViewById(R.id.etEmail);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etEmail1.getText().length() == 0 || etEmail1.getText().toString().trim().length() == 0) {
                            Toast.makeText(LoginActivity.this, getString(R.string.please_email), Toast.LENGTH_SHORT).show();
                        } else if (!M.validateEmail(etEmail1.getText().toString().trim())) {
                            Toast.makeText(LoginActivity.this, getString(R.string.valid_email), Toast.LENGTH_SHORT).show();
                        } else {
                            dialog1.dismiss();
                            makeResetPasswordRequest();
                        }
                    }
                });
                dialog1.show();*//*
            }
        });*/
    loginascustomer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(LoginActivity.this, Register2Activity.class).putExtra("which","customer").putExtra("forType","login"));
      }
    });

  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  Dialog dialog1;
  String mMessage="", mSuccess="";
  NormalEditText etEmail1;
  NormalButton okButton, cancelButton;

  private void makeLoginRequestWithPassword() {
    dialog.show();
    MainApplication.getApiService().loginMethod(etLoginText.getText().toString(),
            etPassword.getText().toString(),"1",
            SharedPrefManager.getInstance(LoginActivity.this).getDeviceToken()).enqueue(new Callback<LoginResponse>() {
      @Override
      public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
        dialog.dismiss();
        if(response.isSuccessful()){
          Log.d("loginresponse",new Gson().toJson(response.body()));
          if(response.body().getSuccess()){


            Intent intent = new Intent(LoginActivity.this, PinActivity.class);
            intent.putExtra("var", var);
            intent.putExtra("type", "login");
            intent.putExtra("accessToken", response.body().getData().getAccessToken());
            intent.putExtra("isIndian", response.body().getData().getIsindian());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finishAffinity();

          } else {
            Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
          }

        }
      }

      @Override
      public void onFailure(Call<LoginResponse> call, Throwable t) {
        dialog.dismiss();
        Toast.makeText(LoginActivity.this, t.getMessage()!=null?t.getMessage():"Error", Toast.LENGTH_SHORT).show();
      }
    });

  }

  /*{
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + "auth/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>LoginResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);

                            *//* {"success":true,"data":{"id":"2","user_id":"10",
                            "access_token":"af13eb84f0125f873b344c940cd0104bca9a06bd5644267832615d4dc510be651583237377",
                            "login_status":"1","fcm_token":null,"device_info":"Dalvik\/2.1.0 (Linux; U; Android 10; SM-M205F Build\/QP1A.190711.020)","email":"esamparkindia@esamparkindia.com","mobile_number":"9381983198","full_name":"eSampark India","remitter_id":"","balance":"0","imageurl":""},"message":"Logged in successfully."}
                             *//*

                            if (jsonObject.getString(ApiParams.success).equals("true")) {
                                JSONObject data=jsonObject.getJSONObject("data");
                                String user_id= data.getString("user_id");
                                Prefes.saveUserID(user_id, LoginActivity.this);
                                Prefes.saveUserData(jsonObject.getJSONObject(ApiParams.data).toString(), LoginActivity.this);

                                String token=data.getString("access_token");

                                *//*if (var.equals("1")) {
                                    finish();
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                                EventBus.getDefault().postSticky(new UpDateUIEvent(true));*//*

                                remitter_details(token, user_id);

                            } else {
                                EventBus.getDefault().postSticky(new UpDateUIEvent(false));
                                M.dialogOk(LoginActivity.this, mMessage, "Error");
                            }
                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                            EventBus.getDefault().postSticky(new UpDateUIEvent(false));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        EventBus.getDefault().postSticky(new UpDateUIEvent(false));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(ApiParams.email, etLoginText.getText().toString());
                map.put(ApiParams.password, etPassword.getText().toString());
                map.put(ApiParams.device_type, "1");
                map.put(ApiParams.device_id, "123");
                map.put(ApiParams.device_info, "1");
                map.put(ApiParams.fcm_token, SharedPrefManager.getInstance(LoginActivity.this).getDeviceToken());
                return map;
            }
        };
        MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }*/

  private void remitter_details(String token, String id) {
    dialog.show();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.remitter_details_URL,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                Log.d("sdsadasdasdasdasd", response);

                       /* {"status":true,"data":{"id":"7","exter_user_id":"503","remitter_id":"6860230","client_id":"7","fname":"Sukhdev",
                       "lname":"Singh","pin":"152124","mobile":"9463639564","status":"1",
                       "created_on":"2020-05-29 11:50:59","modified_on":"2020-05-29 17:00:39"}}
                        */

                try {
                  JSONObject jsonObject=new JSONObject(response);
                  if (jsonObject.getString("status").equals("true")){
                    JSONObject data=jsonObject.getJSONObject("data");
                    String remitter_id_string=data.optString("remitter_id");
                    M.upDateUserTrivialInfo(LoginActivity.this, ApiParams.remitter_id, remitter_id_string);
                  }


                  EventBus.getDefault().postSticky(new UpDateUIEvent(true));

                } catch (JSONException e) {
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
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ApiParams.access_token, token);
        return params;
      }

      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ApiParams.user_id, id);
        return params;
      }
    };

    MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
  }


  @Override
  protected void onResume() {

    if(dialog!=null) {
      dialog.dismiss();
    }
    super.onResume();
  }

  public void popupWithdrawalAmount(){
    AlertDialog.Builder builder= new AlertDialog.Builder(LoginActivity.this);
    View view= LayoutInflater.from(LoginActivity.this).inflate(R.layout.forgot_password_pin_layout, null);
    builder.setCancelable(true);
    builder.setView(view);

    AlertDialog dialog=builder.create();
    dialog.setCancelable(true);
    dialog.show();

    ProgressBar progressBar= view.findViewById(R.id.progress);
    EditText fieldET=view.findViewById(R.id.fieldET);
    TextView titleTV=view.findViewById(R.id.titleTV);
    TextView subTitleTV=view.findViewById(R.id.subTitleTV);

    titleTV.setText(getString(R.string.forgotPassword));
    subTitleTV.setText(getString(R.string.forgotPasswordDescription));
    Button button = view.findViewById(R.id.submitButton);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (fieldET.getText().toString().trim().isEmpty()){
          Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
        } else {
          progressBar.setVisibility(View.VISIBLE);
          button.setEnabled(false);
          ApiCalls.sendResetPasswordLink(LoginActivity.this, fieldET.getText().toString().trim(), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {

              progressBar.setVisibility(View.GONE);
              button.setEnabled(true);
              dialog.dismiss();

              try {

                JSONObject jsonObject=new JSONObject(result);
                Toast.makeText(LoginActivity.this, jsonObject.getString("message")+"", Toast.LENGTH_LONG).show();

              } catch (Exception e) {
                e.printStackTrace();
              }

            }

            @Override
            public void onError(String error) {
              progressBar.setVisibility(View.GONE);
              button.setEnabled(true);
              dialog.dismiss();
            }
          });
        }
      }
    });


    if (dialog.getWindow()!=null){
      dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
    }
  }



}
