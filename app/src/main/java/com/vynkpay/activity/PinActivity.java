package com.vynkpay.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vynkpay.fragment.MCashWalletFragment;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.events.UpDateUIEvent;
import com.vynkpay.fcm.SharedPrefManager;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.VerifyPinResponse;
import com.vynkpay.utils.KeyboardView;
import com.vynkpay.utils.M;
import com.vynkpay.utils.OnChangeKeys;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class PinActivity extends AppCompatActivity {

    @BindView(R.id.addPinBtn)
    NormalButton pinBtn;

    @BindView(R.id.pinEdt)
    NormalTextView pinEdt;

    Dialog dialog;

    String var, accessToken, isIndian;

    @BindView(R.id.keyboard)
    KeyboardView keyboardView;

    @BindView(R.id.forgotPINTV)
    TextView forgotPINTV;

    String type;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        ButterKnife.bind(this);
        dialog = M.showDialog(PinActivity.this, "", false, false);
        if (getIntent() != null) {
            var = getIntent().getStringExtra("var");
            type = getIntent().getStringExtra("type");
            accessToken = getIntent().getStringExtra("accessToken");
            isIndian = getIntent().getStringExtra("isIndian");
        }

        forgotPINTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWithdrawalAmount();
            }
        });

        pinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pinEdt.getText().toString().isEmpty()) {
                    Toast.makeText(PinActivity.this, "Please Enter Pin", Toast.LENGTH_SHORT).show();
                } else if (pinEdt.getText().toString().length() < 6) {
                    Toast.makeText(PinActivity.this, "Please Enter Valid Pin", Toast.LENGTH_SHORT).show();

                } else {

                    dialog.show();

                    MainApplication.getApiService().verifyPinMethod(accessToken,
                            pinEdt.getText().toString(), "1",
                            SharedPrefManager.getInstance(PinActivity.this).getDeviceToken())
                            .enqueue(new Callback<VerifyPinResponse>() {
                                @Override
                                public void onResponse(Call<VerifyPinResponse> call, retrofit2.Response<VerifyPinResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if (response.body().getSuccess()) {
                                            dialog.dismiss();
                                            String user_id = response.body().getData().getUserId();
                                            Prefes.saveUserID(user_id, PinActivity.this);
                                            Prefes.saveID(response.body().getData().getId(), PinActivity.this);
                                            Prefes.saveUserData(response.body().getData().toString(), PinActivity.this);
                                            Prefes.saveName(response.body().getData().getFullName(), PinActivity.this);
                                            Prefes.saveUserName(response.body().getData().getUsername(), PinActivity.this);
                                            Prefes.saveEmail(response.body().getData().getEmail(), PinActivity.this);
                                            Prefes.saveAccessToken(response.body().getData().getAccessToken(), PinActivity.this);
                                            Prefes.saveIm(response.body().getData().getImageurl(), PinActivity.this);
                                            Prefes.saveisIndian(isIndian, PinActivity.this);
                                            Prefes.saveImage(response.body().getData().getImageurl(), PinActivity.this);
                                            if (type.equals("Wallet")) {
                                                Intent intent = new Intent();
                                                intent.putExtra("edttext",  pinEdt.getText().toString());
                                                setResult(RESULT_OK, intent);
                                                finish();
                                            } else if (type.equals("login")) {
                                                if (var.equals("1")) {
                                                    finish();
                                                } else {
                                                    sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
                                                    Log.e("Pin",""+sp.getString("value",""));
                                                    Log.e("Pin",""+isIndian);

                                                    if(sp.getString("value","").equals("Global") && isIndian.equals("NO")){
                                                        startActivity(new Intent(PinActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                           finish();
                                                    }

                                                    else if(sp.getString("value","").equals("Global") && isIndian.equals("YES")){
                                                        startActivity(new Intent(PinActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                        finish();
                                                    }


                                                    else if(sp.getString("value","").equals("India") && isIndian.equals("YES")){
                                                        startActivity(new Intent(PinActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                        finish();
                                                    }

                                                    else if(sp.getString("value","").equals("India") && isIndian.equals("NO")){
                                                        startActivity(new Intent(PinActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                        finish();
                                                    }
                                                }
                                            }

                                            EventBus.getDefault().postSticky(new UpDateUIEvent(true));
                                        } else {
                                            dialog.dismiss();
                                            EventBus.getDefault().postSticky(new UpDateUIEvent(false));
                                            Toast.makeText(PinActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<VerifyPinResponse> call, Throwable t) {
                                    dialog.dismiss();
                                }
                            });
                }
            }
        });


        keyboardView.getInputText(new OnChangeKeys() {
            @Override
            public void keyValue(String value) {
                if (value.isEmpty()) {
                    pinEdt.setText(null);
                } else {
                    pinEdt.setText(value);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        if (dialog != null) {
            dialog.dismiss();
        }
        super.onResume();
    }

    public void popupWithdrawalAmount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PinActivity.this);
        View view = LayoutInflater.from(PinActivity.this).inflate(R.layout.forgot_password_pin_layout, null);
        builder.setCancelable(true);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();

        ProgressBar progressBar = view.findViewById(R.id.progress);
        EditText fieldET = view.findViewById(R.id.fieldET);
        TextView titleTV = view.findViewById(R.id.titleTV);
        TextView subTitleTV = view.findViewById(R.id.subTitleTV);

        titleTV.setText(getString(R.string.forgotPIN));
        subTitleTV.setText(getString(R.string.forgotPINDescription));
        Button button = view.findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldET.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PinActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    button.setEnabled(false);
                    ApiCalls.sendResetPINLink(PinActivity.this, fieldET.getText().toString().trim(), new VolleyResponse() {
                        @Override
                        public void onResult(String result, String status, String message) {
                            progressBar.setVisibility(View.GONE);
                            button.setEnabled(true);
                            dialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Toast.makeText(PinActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
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


        if (dialog.getWindow() != null) {
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
    }
}
