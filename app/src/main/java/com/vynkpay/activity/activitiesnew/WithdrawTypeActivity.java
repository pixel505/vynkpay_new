package com.vynkpay.activity.activitiesnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.vynkpay.R;
import com.vynkpay.adapter.WithdrawalTypeAdapter;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.WithdrawalTypeTesponse;
import com.vynkpay.utils.M;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawTypeActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    NormalTextView toolbarTitle;
    NormalButton submitButton;
    public static String availableBalance= "";
    Dialog serverDialog;
    RecyclerView withdrawalRecycler;
    NormalTextView amountET;
    LinearLayout linWithdrawnType;
    boolean isClicked = true;
    public static String withdrawType = "";
    List<String> withdrawalTypelist = new ArrayList<>();
    String messageStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_type);
        serverDialog = M.showDialog(WithdrawTypeActivity.this, "", false, false);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        submitButton = findViewById(R.id.submitButton);
        withdrawalRecycler = findViewById(R.id.withdrawalRecycler);
        withdrawalRecycler.setVisibility(View.GONE);
        amountET = findViewById(R.id.amountET);
        linWithdrawnType = findViewById(R.id.linWithdrawnType);
        toolbarTitle.setText(getString(R.string.withdrawalRequest));
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitButton.setOnClickListener(this);
        linWithdrawnType.setOnClickListener(this);
        amountET.setOnClickListener(this);
        getWihdrawType();
    }

    public void getWihdrawType(){
        withdrawalTypelist.clear();
        serverDialog.show();
        MainApplication.getApiService().getWithdrawalType(Prefes.getAccessToken(WithdrawTypeActivity.this)).enqueue(new Callback<WithdrawalTypeTesponse>() {
            @Override
            public void onResponse(Call<WithdrawalTypeTesponse> call, Response<WithdrawalTypeTesponse> response) {
                serverDialog.dismiss();
                if(response.isSuccessful() && response.body()!=null){
                    Log.d("withdrawType",new Gson().toJson(response.body()));
                    serverDialog.dismiss();
                    if(response.body().getStatus().equals("true")){
                        GridLayoutManager manager = new GridLayoutManager(WithdrawTypeActivity.this, 1, GridLayoutManager.VERTICAL, false);
                        withdrawalTypelist.addAll(response.body().getData().getWithdrawalType());
                        WithdrawalTypeAdapter adapter = new WithdrawalTypeAdapter(WithdrawTypeActivity.this, response.body().getData().getWithdrawalType());
                        withdrawalRecycler.setLayoutManager(manager);
                        withdrawalRecycler.setAdapter(adapter);
                        adapter.setOnItemClickListener(new WithdrawalTypeAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(String wType) {
                                //
                                withdrawType = wType;
                                amountET.setText(withdrawType);
                                withdrawalRecycler.setVisibility(View.GONE);
                                //Toast.makeText(BonusWalletFragment.this, withdrawType, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else{
                        messageStr = response.body().getMessage()!=null?response.body().getMessage():"You need to purchase first to access this page";
                        String message = response.body().getMessage()!=null?response.body().getMessage():"You need to purchase first to access this page";
                        Toast.makeText(WithdrawTypeActivity.this, messageStr+"", Toast.LENGTH_SHORT).show();
                        serverDialog.dismiss();
                    }
                }

                else{
                    serverDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<WithdrawalTypeTesponse> call, Throwable t) {
                serverDialog.dismiss();
            }

        });
    }

    @Override
    public void onClick(View view) {
        if (view == submitButton){
            if (withdrawType.isEmpty()) {
                Toast.makeText(WithdrawTypeActivity.this, "Please select withdrawal Type", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(WithdrawTypeActivity.this,RequestWithdrawnActivity.class).putExtra("withdrawType",withdrawType));
            }
        }

        if (view == linWithdrawnType){
            if (withdrawalTypelist.size()>0) {
                if (isClicked) {
                    withdrawalRecycler.setVisibility(View.VISIBLE);
                    isClicked = false;
                } else {
                    withdrawalRecycler.setVisibility(View.GONE);
                    isClicked = true;
                }
            } else {
                Toast.makeText(WithdrawTypeActivity.this, ""+messageStr, Toast.LENGTH_SHORT).show();
            }
        }

        if (view == amountET){
            if (withdrawalTypelist.size()>0) {
                if (isClicked) {
                    withdrawalRecycler.setVisibility(View.VISIBLE);
                    isClicked = false;
                } else {
                    withdrawalRecycler.setVisibility(View.GONE);
                    isClicked = true;
                }
            } else {
                Toast.makeText(WithdrawTypeActivity.this, ""+messageStr, Toast.LENGTH_SHORT).show();
            }
        }
    }

}