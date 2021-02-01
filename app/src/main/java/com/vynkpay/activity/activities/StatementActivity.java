package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityStatementBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.StatementResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatementActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    ActivityStatementBinding binding;
    StatementActivity ac;
    Dialog serverDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_statement);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_statement);
        ac = StatementActivity.this;
        serverDialog = M.showDialog(StatementActivity.this, "", false, false);
        clicks();
    }

    private void clicks() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.statement);
        getStatements();
    }

    private void getStatements() {
         serverDialog.show();
         MainApplication.getApiService().getstatement(Prefes.getAccessToken(ac),Prefes.getUserID(ac)).enqueue(new Callback<StatementResponse>() {

             @Override
             public void onResponse(Call<StatementResponse> call, Response<StatementResponse> response) {
                if (response.isSuccessful() && response.body()!=null){
                     serverDialog.dismiss();
                    if(response.body().getStatus().equals("true")){
                        binding.sizeText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getTotalEarning());
                        binding.activeText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getBonusWalletBalance());
                        binding.inactiveText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getBonusWalletReceived());
                        binding.incomeText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getBonusWalletSend());
                        binding.earningText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getReferralBonus());
                        binding.activeDirecttext.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getGenerationBonus());
                        binding.bonusText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getGlobalPoolBonus());
                        binding.globalText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getLeadershipBonus());
                        binding.ambassadarText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getAmbassadorBonus());
                        binding.appraisalText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getAppraisalBonus());
                        binding.chairmanText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getChairmanBonus());
                    }
                    else {
                        Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    serverDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<StatementResponse> call, Throwable t) {
                 serverDialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(StatementActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(StatementActivity.this,StatementActivity.this::finishAffinity);
        }
    }
}