package com.vynkpay.activity.activitiesnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityTranferWalletBinding;
import com.vynkpay.fragment.VCashWalletFragment;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranferWalletActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityTranferWalletBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    Dialog serverDialog;
    public static String bonusBalance = "",vCashBalance="",mCashBalance="";
    String m_wallet_transfer_enable = "", v_wallet_transfer_enable = "", earning_wallet_transfer_enable = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_tranfer_wallet);
        serverDialog = M.showDialog(TranferWalletActivity.this, "", false, false);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getString(R.string.transferWallet));
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.crdBouns.setOnClickListener(this);
        binding.crdMCash.setOnClickListener(this);
        binding.crdVCash.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSettings();
        getBonusTransaction();
        getMCashTransaction();
        getVCashTransaction();
    }

    @Override
    public void onClick(View view) {
        if (view == binding.crdBouns){
            if (earning_wallet_transfer_enable.equalsIgnoreCase("true")){
                startActivity(new Intent(TranferWalletActivity.this,TransferWalletProcessActivity.class));
            } else {
                Toast.makeText(TranferWalletActivity.this,"disabled",Toast.LENGTH_SHORT).show();
            }
        }

        if (view == binding.crdMCash){
            if (m_wallet_transfer_enable.equalsIgnoreCase("true")) {
                startActivity(new Intent(TranferWalletActivity.this, TransferWalletMCashActivity.class));
            }else {
                Toast.makeText(TranferWalletActivity.this, "disabled", Toast.LENGTH_SHORT).show();
            }
        }

        if (view == binding.crdVCash){
            Log.d("vchasssh","click");
            if (v_wallet_transfer_enable.equalsIgnoreCase("true")) {
                startActivity(new Intent(TranferWalletActivity.this, TransferVcashActivity.class));
            } else {
                Toast.makeText(TranferWalletActivity.this,"disabled",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getSettings(){
        MainApplication.getApiService().getTransferSettings(Prefes.getAccessToken(TranferWalletActivity.this)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("settingsresponse",response.body());
                //{"status":true,"data":{"m_wallet_transfer_enable":true,"v_wallet_transfer_enable":true,"earning_wallet_transfer_enable":true},"message":"success"}
                try {
                    JSONObject respData = new JSONObject(response.body());
                    if (respData.getString("status").equalsIgnoreCase("true")){
                        JSONObject data = respData.getJSONObject("data");
                        m_wallet_transfer_enable = data.getString("m_wallet_transfer_enable");
                        v_wallet_transfer_enable = data.getString("v_wallet_transfer_enable");
                        earning_wallet_transfer_enable = data.getString("earning_wallet_transfer_enable");
                        if (respData.has("message")){
                            Log.d("settingsresponse",respData.getString("message"));
                        }
                    }else {
                        if (respData.has("message")){
                            Log.d("settingsresponse",respData.getString("message"));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("settingsresponse",t.getMessage()!=null?t.getMessage():"Error");
            }
        });
    }

    public void getBonusTransaction(){
        serverDialog.show();
        ApiCalls.getBonusTransactions(TranferWalletActivity.this, Prefes.getAccessToken(TranferWalletActivity.this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                serverDialog.dismiss();
                Log.d("btransactionZLog", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                        bonusBalance=dataObject.getString("walletBalance");
                        binding.tvBonusBalance.setText("Available Balance"+":"+ Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));
                    }else {
                        serverDialog.dismiss();
                        //finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                serverDialog.dismiss();
            }

        });
    }
    private void getVCashTransaction(){
        serverDialog.show();
        ApiCalls.getVcashTransactions(TranferWalletActivity.this, Prefes.getAccessToken(TranferWalletActivity.this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("vtransactionZLog", result + "//");
                serverDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        vCashBalance = dataObject.getString("walletBalance");
                        binding.tvVCashBalance.setText("Available Balance"+":"+ Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));

                        JSONArray listingArray = dataObject.getJSONArray("listing");
                        for (int i = 0; i < listingArray.length(); i++) {
                            JSONObject object = listingArray.getJSONObject(i);
                            String id = object.getString("id");
                            String front_user_id = object.getString("front_user_id");
                            String user_id = object.getString("user_id");
                            String type = object.getString("type");
                            String payment_via = object.getString("payment_via");
                            String p_amount = object.getString("p_amount");
                            String profit_type = object.getString("profit_type");
                            String mode = object.getString("mode");
                            String transactionStatus = object.getString("status");
                            String created_date = object.getString("created_date");
                            String username = object.getString("username");
                            String email = object.getString("email");
                            String phone = object.getString("phone");
                            String name = object.getString("name");
                            String paid_status = object.getString("paid_status");
                            String balance = object.getString("balance");
                            String frontusername = object.getString("frontusername");
                            VCashWalletFragment.walletTransactionsModelArrayList.add(new WalletTransactionsModel(id, front_user_id, user_id, type,
                                    payment_via, p_amount, profit_type, mode, transactionStatus, created_date, username,
                                    email, phone, name, paid_status, balance, frontusername));

                        }
                        Collections.reverse(VCashWalletFragment.walletTransactionsModelArrayList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                serverDialog.dismiss();
            }

        });
    }

    private void getMCashTransaction(){
        serverDialog.show();
        ApiCalls.getMcashTransactions(TranferWalletActivity.this, Prefes.getAccessToken(TranferWalletActivity.this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                serverDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                        mCashBalance=dataObject.getString("walletBalance");
                        binding.tvMCashBalance.setText("Available Balance"+":"+ Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                serverDialog.dismiss();
            }

        });
    }


}