package com.vynkpay.activity.activitiesnew.conversion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityConvertDetailBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.SendWaletOtp;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import java.text.NumberFormat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vynkpay.activity.activitiesnew.conversion.ConvertBonusMcashActivity.wallet_convert_charges;

public class ConvertDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityConvertDetailBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    Dialog serverDialog;
    String amount = "",convertedAmount="",payAmount="";
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_convert_detail);
        activity = ConvertDetailActivity.this;
        serverDialog = M.showDialog(ConvertDetailActivity.this, "", false, false);
        if (getIntent().hasExtra("amount")) {
            amount = getIntent().getStringExtra("amount");
        }
        binding.availableBalanceTV.setText(Functions.CURRENCY_SYMBOL+amount);
        //wallet_convert_charges
        if (getIntent().hasExtra("convertedAmount")){
            convertedAmount = getIntent().getStringExtra("convertedAmount");
        }
        //binding.tvConvertedAmount.setText(Functions.CURRENCY_SYMBOL+convertedAmount);
        String charges = wallet_convert_charges != null ? (!TextUtils.isEmpty(wallet_convert_charges) ? wallet_convert_charges:"0") :"0";
        float  percentage = Float.parseFloat(charges);
        Log.d("chargess",percentage+"//");
        float tFee = (percentage*Float.parseFloat(convertedAmount));
        float conversionFee = tFee/100;
        Log.d("cargesss",conversionFee+"//"+tFee);
        binding.tvConversionFee.setText(Functions.CURRENCY_SYMBOL+NumberFormat.getInstance().format(conversionFee));
        binding.tvBalance.setText(Functions.CURRENCY_SYMBOL+convertedAmount);
        float totalAmount = Float.parseFloat(convertedAmount)-conversionFee;
        payAmount = String.valueOf(NumberFormat.getInstance().format(totalAmount));
        binding.tvConvertedAmount.setText(Functions.CURRENCY_SYMBOL+ NumberFormat.getInstance().format(totalAmount));
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getString(R.string.convertmcashtext));
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });
        binding.submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.submitButton){
            Log.d("payAmount",payAmount);
            submit(payAmount);
        }
    }

    private void submit(String amount) {
        serverDialog.show();
        MainApplication.getApiService().getWalletOtp(Prefes.getAccessToken(activity)).enqueue(new Callback<SendWaletOtp>() {
            @Override
            public void onResponse(Call<SendWaletOtp> call, Response<SendWaletOtp> response) {
                if(response.isSuccessful() && response.body()!=null){
                    serverDialog.dismiss();
                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ConvertDetailActivity.this,ConversionOtpActivity.class).putExtra("amount",amount));
                    ConvertDetailActivity.this.finish();
                }
                else{
                    serverDialog.dismiss();
                    if (response.body() != null) {
                        Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SendWaletOtp> call, Throwable t) {
                serverDialog.dismiss();
                Toast.makeText(activity, t.getMessage()!=null ? t.getMessage() : "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}