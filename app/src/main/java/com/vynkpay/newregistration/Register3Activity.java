package com.vynkpay.newregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.google.gson.Gson;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.databinding.ActivityRegister3Binding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.OtpVerifyLoginResponse;
import com.vynkpay.retrofit.model.OtpVerifyResponse;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register3Activity extends AppCompatActivity implements View.OnClickListener, PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityRegister3Binding binding;
    String which = "",temp_id="",type="",isIndian="";
    Dialog dialog;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register3);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
        dialog = M.showDialog(Register3Activity.this, "", false, false);
        if (getIntent().hasExtra("which")){
            which = getIntent().getStringExtra("which");
        }
        if (getIntent().hasExtra("temp_id")){
            temp_id = getIntent().getStringExtra("temp_id");
            Log.d("temp_id",temp_id);
        }
        if (getIntent().hasExtra("type")){
            type = getIntent().getStringExtra("type");
        }
        if(sp.getString("value", "").equalsIgnoreCase("Global")){
            binding.tvTitleText.setText(getString(R.string.verifymmail));
            binding.tvTextVerify.setText(getString(R.string.onetimepasswordemail));
        } else {
            binding.tvTitleText.setText(getString(R.string.verifymobile));
            binding.tvTextVerify.setText(getString(R.string.onetimepasswordphone));
        }

       /* if (which != null) {
            if (which.equalsIgnoreCase("customer")){
                binding.tvTitleText.setText(getString(R.string.verifymobile));
                binding.tvTextVerify.setText(getString(R.string.onetimepasswordphone));
            } else {
                binding.tvTitleText.setText(getString(R.string.verifymmail));
                binding.tvTextVerify.setText(getString(R.string.onetimepasswordemail));
            }
        }*/
        binding.submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.submitButton){
            //startActivity(new Intent(Register3Activity.this,Register4Activity.class));
            if (TextUtils.isEmpty(binding.etOtpText.getText().toString().trim())){
                Toast.makeText(Register3Activity.this,"Please enter otp",Toast.LENGTH_SHORT).show();
            } else {
                //onVerifyOtp(binding.etOtpText.getText().toString().trim());
                if (type.equalsIgnoreCase("register")){
                    onVerifyOtp(binding.etOtpText.getText().toString().trim());
                } else {
                    onVerifyOtpLogin(binding.etOtpText.getText().toString());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(Register3Activity.this).setConnectivityListener(this);
    }

    void onVerifyOtpLogin(String otp){
        dialog.show();
        Log.d("loginparams",temp_id+"//"+otp);
        MainApplication.getApiService().otpLoginVerifyMethod(temp_id,otp).enqueue(new Callback<OtpVerifyLoginResponse>() {

            @Override
            public void onResponse(Call<OtpVerifyLoginResponse> call, Response<OtpVerifyLoginResponse> response) {
                if(response.isSuccessful()) {
                    Log.d("otpVerifyResponseLogin",new Gson().toJson(response.body()));
                    if (response.body().getStatus().equalsIgnoreCase("1")) {
                        dialog.dismiss();
                        sp.edit().putString("referalCodeC","").apply();
                        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
                        Log.e("Pin",""+sp.getString("value","")+"//from"+isIndian);
                        Prefes.saveUserID(response.body().getData().getUId(),Register3Activity.this);
                        Prefes.saveAccessToken(response.body().getData().getAccessToken(), Register3Activity.this);
                        Log.d("accesstoken",Prefes.getAccessToken(Register3Activity.this));
                        isIndian = response.body().getData().getIsindian();
                        Prefes.saveUserData(response.body().getData().toString(), Register3Activity.this);
                        Prefes.saveisIndian(isIndian,Register3Activity.this);
                        Prefes.saveUserType(response.body().getUserType(),Register3Activity.this);
                        Log.d("userType",Prefes.getUserType(Register3Activity.this));

                        if(sp.getString("value","").equals("Global") && isIndian.equalsIgnoreCase("NO")){
                            startActivity(new Intent(Register3Activity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finishAffinity();
                        } else if(sp.getString("value","").equals("Global") && isIndian.equalsIgnoreCase("YES")){
                            startActivity(new Intent(Register3Activity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finishAffinity();
                        } else if(sp.getString("value","").equals("India") && isIndian.equalsIgnoreCase("YES")){
                            startActivity(new Intent(Register3Activity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finishAffinity();
                        } else if(sp.getString("value","").equals("India") && isIndian.equalsIgnoreCase("NO")){
                            startActivity(new Intent(Register3Activity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finishAffinity();
                        }else {
                            Log.d("elsee","block");
                        }
                        Toast.makeText(Register3Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        dialog.dismiss();
                        Toast.makeText(Register3Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(Register3Activity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpVerifyLoginResponse> call, Throwable t) {
                Toast.makeText(Register3Activity.this, t.getMessage()!=null ? t.getMessage():"Error", Toast.LENGTH_SHORT).show();
            }

        });
    }


    void onVerifyOtp(String otp){
        dialog.show();
        MainApplication.getApiService().otpVerifyMethod(temp_id,otp).enqueue(new Callback<OtpVerifyResponse>() {

            @Override
            public void onResponse(Call<OtpVerifyResponse> call, Response<OtpVerifyResponse> response) {
                if(response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        dialog.dismiss();
                        Log.d("otpVerifyResponse",new Gson().toJson(response.body()));
                        sp.edit().putString("referalCodeC","").apply();
                        Prefes.saveUserID(response.body().getData().getUserId(),Register3Activity.this);
                        Prefes.saveAccessToken(response.body().getData().getAccessToken(), Register3Activity.this);
                        Log.d("accesstoken",Prefes.getAccessToken(Register3Activity.this));
                        Prefes.saveUserData(response.body().getData().toString(), Register3Activity.this);
                        isIndian = response.body().getData().getIsindian();
                        Prefes.saveisIndian(isIndian,Register3Activity.this);
                        Log.e("Pin",""+sp.getString("value","")+"//from"+isIndian);
                        Prefes.saveUserType(response.body().getData().getUserType(),Register3Activity.this);
                        Log.d("userType",Prefes.getUserType(Register3Activity.this));

                        if(sp.getString("value","").equals("Global") && isIndian.equalsIgnoreCase("NO")){
                            startActivity(new Intent(Register3Activity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finishAffinity();
                        } else if(sp.getString("value","").equals("Global") && isIndian.equalsIgnoreCase("YES")){
                            startActivity(new Intent(Register3Activity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finishAffinity();
                        } else if(sp.getString("value","").equals("India") && isIndian.equalsIgnoreCase("YES")){
                            startActivity(new Intent(Register3Activity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finishAffinity();
                        } else if(sp.getString("value","").equals("India") && isIndian.equalsIgnoreCase("NO")){
                            startActivity(new Intent(Register3Activity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finishAffinity();
                        }else {
                            Log.d("elsee","block");
                        }
                        Toast.makeText(Register3Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        /*Intent intent=new Intent(Register3Activity.this, RechargeSuccess.class).putExtra("which",which);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finishAffinity();*/

                    } else {
                        dialog.dismiss();
                        Toast.makeText(Register3Activity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(Register3Activity.this, "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<OtpVerifyResponse> call, Throwable t) {
                Toast.makeText(Register3Activity.this, t.getMessage()!=null ? t.getMessage():"Error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(Register3Activity.this,Register3Activity.this::finishAffinity);
        }
    }


    //@Field("country_code") String country_code,@Field("umobile") String u_password

}