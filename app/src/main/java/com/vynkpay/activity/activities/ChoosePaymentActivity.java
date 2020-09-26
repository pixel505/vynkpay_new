package com.vynkpay.activity.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityChoosePaymentBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetWalletResponse;
import com.vynkpay.retrofit.model.PayResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ChoosePaymentActivity extends AppCompatActivity {
    ActivityChoosePaymentBinding binding;
    ChoosePaymentActivity ac;
    String mBalance, mAmount;
    Dialog dialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_payment);
        ac = ChoosePaymentActivity.this;
        dialog1 = M.showDialog(ac, "", false, false);
        if (Functions.isIndian) {
        } else {
            binding.payOnline.setText("Bitcoin");
            binding.card1.setVisibility(View.GONE);
            binding.card2.setVisibility(View.GONE);
            binding.paycard.setVisibility(View.GONE);
        }
        binding.mcashBalance.setText("Available Balance"+" :"+Functions.CURRENCY_SYMBOL+Prefes.getCash(ChoosePaymentActivity.this));
        mBalance=Prefes.getCash(ChoosePaymentActivity.this);
        clicks();
        if (getIntent() != null) {
            binding.packagePrice.setText(Functions.CURRENCY_SYMBOL + getIntent().getStringExtra("purchaseprice"));
            mAmount = getIntent().getStringExtra("purchaseprice");
        }
    }
    private void clicks() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.choosepay);
        if(Functions.isIndian){
            binding.requestMCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ChoosePaymentActivity.this, ChoosePaymentActivityB.class).putExtra("am", getIntent().getStringExtra("purchaseprice")));
                    finish();
                }
            });
        }


        else {
            binding.requestMCash.setVisibility(View.GONE);
        }

        binding.choosepaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(binding.rd1.isChecked() || binding.rd2.isChecked())) {
                    Toast.makeText(ac, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.rd1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.rd2.setChecked(false);
                    double totalAmount = Double.parseDouble(mAmount);
                    double mbalance = Double.parseDouble(mBalance);
                    if (mbalance < totalAmount) {
                        binding.choosepaymentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(ac, "Low Wallet Balance", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        binding.choosepaymentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.show();
                                MainApplication.getApiService().pay(Prefes.getAccessToken(ac), getIntent().getStringExtra("ids")).enqueue(new Callback<PayResponse>() {
                                    @Override
                                    public void onResponse(Call<PayResponse> call, Response<PayResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            dialog1.dismiss();
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ac, ChoosePaymentActivityD.class));
                                            finish();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<PayResponse> call, Throwable t) {
                                        dialog1.dismiss();
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
        binding.rd2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.rd1.setChecked(false);
                  double totalAmount = Double.parseDouble(mAmount);
                    double mbalance = Double.parseDouble(mBalance);
                    binding.choosepaymentBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog1.show();
                            if(Functions.isIndian){
                                String url = "https://www.mlm.pixelsoftwares.com/vynkpay/account/coinBaseAppWebView/app_choose_payment?package_id="
                                        + getIntent().getStringExtra("ids") + "&alien=1&app_request=request_app&access_token=" + Prefes.getAccessToken(ac);
                                Log.e("ids", "" + url);
                                startActivity(new Intent(ChoosePaymentActivity.this, WebviewActivityNew.class)
                                        .putExtra("url", url)
                                        .putExtra("packageID", getIntent().getStringExtra("ids")));
                            }
                            else {
                                String url = "https://www.vynkpay.app/account/coinBaseAppWebView/app_choose_payment?package_id="
                                        + getIntent().getStringExtra("ids") + "&alien=1&app_request=request_app&access_token=" + Prefes.getAccessToken(ac);
                                Log.e("ids", "" + url);
                                startActivity(new Intent(ChoosePaymentActivity.this, WebviewActivityNew.class)
                                        .putExtra("url", url)
                                        .putExtra("packageID", getIntent().getStringExtra("ids")));
                            }


                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        dialog1.dismiss();
        super.onResume();
    }
}