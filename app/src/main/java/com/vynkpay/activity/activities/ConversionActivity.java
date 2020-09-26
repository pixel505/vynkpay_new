package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.vynkpay.R;
import com.vynkpay.adapter.ConversionAdapter;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityConversionBinding;
import com.vynkpay.fragment.BonusWalletFragment;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.CheckWaletOtp;
import com.vynkpay.retrofit.model.ConversionResponse;
import com.vynkpay.retrofit.model.SendWaletOtp;
import com.vynkpay.retrofit.model.SubmitConversionResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vynkpay.fragment.BonusWalletFragment.hideKeyboard;

public class ConversionActivity extends AppCompatActivity {
         ActivityConversionBinding binding;
         ConversionActivity activity;
         Dialog serverDialog;
         String miniAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_conversion);
        activity= ConversionActivity.this;
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        hideKeyboard(activity);
        serverDialog = M.showDialog(activity, "", false, false);
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText("Conversion");
        binding.conversionHeader.sendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.conversionHeader.searchUserET.getText().toString().isEmpty()){
                    Toast.makeText(activity, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                }

                else if(Float.parseFloat(binding.conversionHeader.searchUserET.getText().toString())<Float.parseFloat(miniAmount)){
                    Toast.makeText(activity, "Minimum Amount is "+Functions.CURRENCY_SYMBOL+miniAmount, Toast.LENGTH_SHORT).show();
                }
                else {
                    submit(binding.conversionHeader.searchUserET.getText().toString());
                }
            }
        });
        getConversionList();
    }

    private void submit(String amount) {
        serverDialog.show();
             MainApplication.getApiService().getWalletOtp(Prefes.getAccessToken(activity)).enqueue(new Callback<SendWaletOtp>() {
                 @Override
                 public void onResponse(Call<SendWaletOtp> call, Response<SendWaletOtp> response) {
                     if(response.isSuccessful() && response.body()!=null){
                         serverDialog.dismiss();
                         Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                         Dialog dialog = new Dialog(activity);
                         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                         dialog.setCancelable(false);
                         dialog.setCanceledOnTouchOutside(true);
                         dialog.setContentView(R.layout.otp_dialog);
                         NormalEditText otpEdt = dialog.findViewById(R.id.otpEditText);
                         NormalButton submit = dialog.findViewById(R.id.submitLn);
                         submit.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 if(otpEdt.getText().toString().isEmpty()){
                                     Toast.makeText(activity, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                                 }
                                 else {
                                        serverDialog.show();
                                        MainApplication.getApiService().checkotp(Prefes.getAccessToken(activity),otpEdt.getText().toString()).enqueue(new Callback<CheckWaletOtp>() {
                                            @Override
                                            public void onResponse(Call<CheckWaletOtp> call, Response<CheckWaletOtp> response) {
                                                if(response.isSuccessful() && response.body()!=null){
                                                     serverDialog.dismiss();
                                                     if(response.body().getStatus().equals("true")){
                                                         convertmoney(amount,dialog,submit);

                                                     }
                                                     else {
                                                         Toast.makeText(ConversionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                     }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<CheckWaletOtp> call, Throwable t) {
                                                  serverDialog.dismiss();
                                                Toast.makeText(ConversionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                 }
                             }
                         });
                         dialog.show();
                     }
                     else{
                         serverDialog.dismiss();
                         Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 }

                 @Override
                 public void onFailure(Call<SendWaletOtp> call, Throwable t) {
                   serverDialog.dismiss();
                     Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                 }
             });

    }

    private void convertmoney(String amount, Dialog dialog, NormalButton submit) {
        Toast.makeText(activity, "Converting Money", Toast.LENGTH_SHORT).show();
        submit.setClickable(false);
        MainApplication.getApiService().submitConversion(Prefes.getAccessToken(activity),"wr",amount).enqueue(new Callback<SubmitConversionResponse>() {
            @Override
            public void onResponse(Call<SubmitConversionResponse> call, Response<SubmitConversionResponse> response) {
                 if(response.isSuccessful() && response.body()!=null){

                     if(response.body().getStatus()){
                         Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                         dialog.dismiss();
                         getConversionList();
                         binding.conversionHeader.searchUserET.setText("");
                     }

                 }
            }

            @Override
            public void onFailure(Call<SubmitConversionResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void getConversionList() {
        serverDialog.show();
        MainApplication.getApiService().getConversions(Prefes.getAccessToken(activity)).enqueue(new Callback<ConversionResponse>() {
            @Override
            public void onResponse(Call<ConversionResponse> call, Response<ConversionResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    serverDialog.dismiss();
                    if(response.body().getStatus().equals("true")){
                        GridLayoutManager manager = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
                        ConversionAdapter adapter = new ConversionAdapter(activity, response.body().getData().getListing());
                        binding.conversionList.setLayoutManager(manager);
                        binding.conversionList.setAdapter(adapter);

                        if (response.body().getData().getListing().size() > 0) {
                            binding.noLayout.setVisibility(View.GONE);
                        } else {
                            binding.noLayout.setVisibility(View.VISIBLE);
                        }
                        binding.conversionHeader.availableBalanceTV.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getWalletBalance());
                        miniAmount=response.body().getData().getMinimumAmount();
                    }

                }
                else {
                    serverDialog.dismiss();
                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConversionResponse> call, Throwable t) {
          serverDialog.dismiss();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}