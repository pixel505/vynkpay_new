package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.vynkpay.BuildConfig;
import com.vynkpay.newregistration.Register1Activity;
import com.vynkpay.newregistration.Register2Activity;
import com.vynkpay.R;
import com.vynkpay.activity.PinActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.events.UpDateUIEvent;
import com.vynkpay.fcm.Config;
import com.vynkpay.fcm.SharedPrefManager;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.newregistration.SelectionActivity;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.LoginResponse;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
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
  @BindView(R.id.tvTextFor)
  TextView tvTextFor;

  private boolean isPasswordShown = false;
  /*@BindView(R.id.forgotPassWordText)
  NormalTextView forgotPassWordText;*/
  String var = "";

  String regex = "^[0-9]*$";
  Dialog dialog;
  boolean isWhat = false;
  UserSharedPreferences mShraredPref;
  SharedPreferences sp;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (M.isScreenshotDisable){
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }
    setContentView(R.layout.login_new);
    mShraredPref=new UserSharedPreferences(LoginActivity.this);

    ButterKnife.bind(LoginActivity.this);
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
    changingValues();
  }

  void changingValues(){
    sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
    if (sp.getString("value","").equalsIgnoreCase("India")){
      SpannableString iString=new SpannableString("Change your region to GLOBAL");
      iString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), 21, iString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
      tvTextFor.setText(iString);
    }else {
      SpannableString gString=new SpannableString("Change your region to INDIA");
      gString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), 21, gString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
      tvTextFor.setText(gString);
    }
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
        Intent intent = new Intent(LoginActivity.this, Register1Activity.class);
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

    tvTextFor.setOnClickListener(v -> {

      startActivity(new Intent(LoginActivity.this, SelectionActivity.class));
      LoginActivity.this.finish();

    });

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

  @Override
  protected void onResume() {

    if(dialog!=null) {
      dialog.dismiss();
    }
    super.onResume();
    MySingleton.getInstance(LoginActivity.this).setConnectivityListener(this);
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


  @Override
  public void onNetworkConnectionChanged(boolean isConnected) {
    if (isConnected){
      M.showUSBPopUp(LoginActivity.this,LoginActivity.this::finishAffinity);
    }
  }
}
