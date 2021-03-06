package com.vynkpay.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.events.UpDateUIEvent;
import com.vynkpay.fcm.SharedPrefManager;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.VerifyPinResponse;
import com.vynkpay.utils.KeyboardView;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.OnChangeKeys;
import com.vynkpay.utils.PlugInControlReceiver;

import org.greenrobot.eventbus.EventBus;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class RePinActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    @BindView(R.id.addPinBtn)
    NormalButton pinBtn;

    @BindView(R.id.pinEdt)
    NormalTextView pinEdt;

    Dialog dialog;

    String var,accessToken;

    @BindView(R.id.keyboard)
    KeyboardView keyboardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_pin);
        ButterKnife.bind(this);
        dialog = M.showDialog(RePinActivity.this, "", false, false);
        if(getIntent()!=null){
            var=getIntent().getStringExtra("var");
            accessToken=getIntent().getStringExtra("accessToken");
        }

        pinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pinEdt.getText().toString().isEmpty() ){
                    Toast.makeText(RePinActivity.this, "Please Enter Pin", Toast.LENGTH_SHORT).show();
                } else if(pinEdt.getText().toString().length()<6){
                    Toast.makeText(RePinActivity.this, "Please Enter Valid Pin", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    MainApplication.getApiService().verifyPinMethod(accessToken,
                            pinEdt.getText().toString(),"1",
                            SharedPrefManager.getInstance(RePinActivity.this).getDeviceToken())
                            .enqueue(new Callback<VerifyPinResponse>() {
                                @Override
                                public void onResponse(Call<VerifyPinResponse> call, retrofit2.Response<VerifyPinResponse> response) {
                                    if(response.isSuccessful() && response.body()!=null){
                                        if( response.body().getSuccess() ){
                                            dialog.dismiss();

                                            String user_id= response.body().getData().getUserId();
                                            Prefes.saveUserID(user_id, RePinActivity.this);
                                            Prefes.saveUserData(response.body().getData().toString(), RePinActivity.this);
                                            Prefes.saveName(response.body().getData().getFullName(), RePinActivity.this);
                                            Prefes.saveUserName(response.body().getData().getUsername(), RePinActivity.this);
                                            Prefes.saveEmail(response.body().getData().getEmail(), RePinActivity.this);
                                            Prefes.saveAccessToken(response.body().getData().getAccessToken(), RePinActivity.this);
                                            Prefes.saveIm(response.body().getData().getImageurl(), RePinActivity.this);
                                            if (var.equals("1")) {
                                                finish();
                                            } else {
                                                Intent intent = new Intent(RePinActivity.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                            EventBus.getDefault().postSticky(new UpDateUIEvent(true));
                                        } else {
                                            dialog.dismiss();
                                            EventBus.getDefault().postSticky(new UpDateUIEvent(false));
                                            Toast.makeText(RePinActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<VerifyPinResponse> call, Throwable t) {
                                    dialog.dismiss();
                                    Log.d("errorErr",t.getMessage()!=null ? t.getMessage() : "");
                                }
                            });
                }
            }
        });


        keyboardView.getInputText(new OnChangeKeys() {
            @Override
            public void keyValue(String value) {
                if (value.isEmpty()){
                    pinEdt.setText(null);
                }else {
                    pinEdt.setText(value);
                }
            }
        });

    }

    @Override
    protected void onResume() {

        if(dialog!=null){
            dialog.dismiss();
        }

        super.onResume();
        MySingleton.getInstance(RePinActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(RePinActivity.this,RePinActivity.this::finishAffinity);
        }
    }
}
