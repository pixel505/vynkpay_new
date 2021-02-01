package com.vynkpay.newregistration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.vynkpay.R;
import com.vynkpay.activity.activities.Signupnew;
import com.vynkpay.databinding.ActivityRegister1Binding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;


public class Register1Activity extends AppCompatActivity implements View.OnClickListener, PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityRegister1Binding binding;
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register1);
        binding.linCustomer.setOnClickListener(this);
        binding.linAffiliate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == binding.linCustomer){
            binding.linCustomer.setBackgroundResource(R.drawable.shadow_button);
            binding.linAffiliate.setBackgroundResource(R.drawable.register_default_button);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.linCustomer.setBackgroundResource(R.drawable.register_default_button);
                    binding.linAffiliate.setBackgroundResource(R.drawable.register_default_button);
                    startActivity(new Intent(Register1Activity.this,Register2Activity.class).putExtra("which","customer").putExtra("forType","signup"));
                }
            },300);
        }

        if (view == binding.linAffiliate){
            binding.linCustomer.setBackgroundResource(R.drawable.register_default_button);
            binding.linAffiliate.setBackgroundResource(R.drawable.shadow_button);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.linCustomer.setBackgroundResource(R.drawable.register_default_button);
                    binding.linAffiliate.setBackgroundResource(R.drawable.register_default_button);
                    Intent intent = new Intent(Register1Activity.this, Signupnew.class);
                    startActivity(intent);
                    //startActivity(new Intent(Register1Activity.this,Register2Activity.class).putExtra("which","affiliate"));
                }
            },300);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(Register1Activity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(Register1Activity.this,Register1Activity.this::finishAffinity);
        }
    }

    /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/

}
