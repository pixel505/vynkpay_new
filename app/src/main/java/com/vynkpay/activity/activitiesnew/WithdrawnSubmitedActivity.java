package com.vynkpay.activity.activitiesnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class WithdrawnSubmitedActivity extends AppCompatActivity implements View.OnClickListener, PlugInControlReceiver.ConnectivityReceiverListener {

    Toolbar toolbar;
    NormalTextView toolbarTitle;
    NormalButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_withdrawn_submited);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
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
            WithdrawnSubmitedActivity.this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(WithdrawnSubmitedActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(WithdrawnSubmitedActivity.this,WithdrawnSubmitedActivity.this::finishAffinity);
        }
    }
}