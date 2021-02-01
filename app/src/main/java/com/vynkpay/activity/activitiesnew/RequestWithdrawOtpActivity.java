package com.vynkpay.activity.activitiesnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityRequestWithdrawOtpBinding;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestWithdrawOtpActivity extends AppCompatActivity implements View.OnClickListener, PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityRequestWithdrawOtpBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    NormalEditText amountET;
    NormalButton submitButton;
    String amountWithdraw = "";
    Dialog serverDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_request_withdraw_otp);
        serverDialog = M.showDialog(RequestWithdrawOtpActivity.this, "", false, false);
        amountWithdraw = getIntent().getStringExtra("amountWithdraw");
        binding.tvWithdrawAmount.setText(Functions.CURRENCY_SYMBOL+amountWithdraw);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        amountET = findViewById(R.id.amountET);
        submitButton = findViewById(R.id.submitButton);
        toolbarTitle.setText(getString(R.string.withdrawalRequest));
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == submitButton){
            if (TextUtils.isEmpty(binding.otpET.getText().toString().trim())) {
                Toast.makeText(RequestWithdrawOtpActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
            } else {
                checkwalletOtp(amountWithdraw);
            }
            //startActivity(new Intent(RequestWithdrawOtpActivity.this,WithdrawnSubmitedActivity.class));
        }

    }

    public void checkwalletOtp(String amount){
        serverDialog.show();
        submitButton.setEnabled(false);
        ApiCalls.checkWalletOTP(RequestWithdrawOtpActivity.this, Prefes.getAccessToken(RequestWithdrawOtpActivity.this), binding.otpET.getText().toString().trim(), new VolleyResponse() {

            @Override
            public void onResult(String result, String status, String message) {
                            /*progressBar.setVisibility(View.GONE);
                            button.setEnabled(true);*/
                serverDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")) {

                        payNow(amount);

                        //popupPaymentMode(amount);
                    } else {
                        serverDialog.dismiss();
                        submitButton.setEnabled(true);
                        String msg = "Insufficient balance in wallet to convert";
                        Toast.makeText(RequestWithdrawOtpActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    serverDialog.dismiss();
                    submitButton.setEnabled(true);
                }
            }

            @Override
            public void onError(String error) {
                submitButton.setEnabled(true);
                serverDialog.dismiss();
            }

        });

    }

    public void payNow(String amount) {
        serverDialog.show();
        submitButton.setEnabled(false);

        String action = "wr";
        if (Functions.isIndian) {
            action = "wr";
        } else {
            action = WithdrawTypeActivity.withdrawType;
        }

        ApiCalls.withdrawalRequest(RequestWithdrawOtpActivity.this, Prefes.getAccessToken(RequestWithdrawOtpActivity.this), amount, action, new VolleyResponse() {

            @Override
            public void onResult(String result, String status, String message) {
                serverDialog.dismiss();
                submitButton.setEnabled(true);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")) {
                        startActivity(new Intent(RequestWithdrawOtpActivity.this,WithdrawnSubmitedActivity.class));
                        RequestWithdrawOtpActivity.this.finish();
                    } else {
                        Toast.makeText(RequestWithdrawOtpActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                serverDialog.dismiss();
                submitButton.setEnabled(true);
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(RequestWithdrawOtpActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(RequestWithdrawOtpActivity.this,RequestWithdrawOtpActivity.this::finishAffinity);
        }
    }
}