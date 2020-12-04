package com.vynkpay.activity.activitiesnew;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityRequestWithdrawnBinding;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;

public class RequestWithdrawnActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityRequestWithdrawnBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    NormalEditText amountET;
    NormalButton submitButton;
    public static String availableBalance= "0";
    Dialog serverDialog;
    public static ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_request_withdrawn);
        serverDialog = M.showDialog(RequestWithdrawnActivity.this, "", false, false);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        amountET = findViewById(R.id.amountET);
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
        getBonusTransaction();
        try {
            binding.amountET.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(12,2)});
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getBonusTransaction(){
        serverDialog.show();
        ApiCalls.getBonusTransactions(RequestWithdrawnActivity.this, Prefes.getAccessToken(RequestWithdrawnActivity.this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("bonusTransaction", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        serverDialog.dismiss();
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                        availableBalance=dataObject.getString("walletBalance");
                        binding.availableBalanceTV.setText(Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));
                        JSONArray listingArray = dataObject.getJSONArray("listing");
                        for (int i=0; i<listingArray.length(); i++){
                            JSONObject object=listingArray.getJSONObject(i);
                            String id = object.getString("id");
                            String front_user_id = object.getString("front_user_id");
                            String user_id  = object.getString("user_id");
                            String type  = object.getString("type");
                            String payment_via = object.getString("payment_via");
                            String p_amount  = object.getString("p_amount");
                            String profit_type = object.getString("profit_type");
                            String mode  = object.getString("mode");
                            String transactionStatus  = object.getString("status");
                            String created_date  = object.getString("created_date");
                            String username = object.getString("username");
                            String email  = object.getString("email");
                            String phone  = object.getString("phone");
                            String name  = object.getString("name");
                            String paid_status  = object.getString("paid_status");
                            String balance  = object.getString("balance");
                            String frontusername = object.getString("frontusername");

                            walletTransactionsModelArrayList.add(new WalletTransactionsModel( id,  front_user_id,  user_id,  type,
                                    payment_via, p_amount,  profit_type,  mode,  transactionStatus,  created_date,  username,
                                    email,  phone,  name,  paid_status,  balance, frontusername));
                        }

                        Collections.reverse(walletTransactionsModelArrayList);

                    } else {
                        serverDialog.dismiss();
                        Toast.makeText(RequestWithdrawnActivity.this, jsonObject.getString("message")+"", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        if (view == submitButton){
            if (amountET.getText().toString().trim().isEmpty()) {
                Toast.makeText(RequestWithdrawnActivity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
            } else if (Float.parseFloat(amountET.getText().toString().trim()) <= 0) {
                Toast.makeText(RequestWithdrawnActivity.this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
            } else if (Float.parseFloat(amountET.getText().toString().trim()) > Float.parseFloat(availableBalance.trim())) {
                Toast.makeText(RequestWithdrawnActivity.this, "This amount is not available in wallet", Toast.LENGTH_SHORT).show();
            } else {
                requestForOtp(amountET.getText().toString());
            }
        }
    }

    public void requestForOtp(String amount){
        submitButton.setEnabled(false);
        serverDialog.show();
        ApiCalls.sendWalletOTP(RequestWithdrawnActivity.this, Prefes.getAccessToken(RequestWithdrawnActivity.this), new VolleyResponse() {

            @Override
            public void onResult(String result, String status, String message) {
                serverDialog.dismiss();
                submitButton.setEnabled(true);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Toast.makeText(RequestWithdrawnActivity.this, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();
                    if (jsonObject.getString("status").equals("true")) {
                        startActivity(new Intent(RequestWithdrawnActivity.this,RequestWithdrawOtpActivity.class).putExtra("amountWithdraw",amount));
                        RequestWithdrawnActivity.this.finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                submitButton.setEnabled(true);
                serverDialog.dismiss();
            }

        });

    }

}