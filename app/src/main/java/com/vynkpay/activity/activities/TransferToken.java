package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.adapter.TransferHistoryAdapter;
import com.vynkpay.databinding.ActivityTransferTokenBinding;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.LogoutResponse;
import com.vynkpay.utils.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferToken extends AppCompatActivity implements View.OnClickListener {

    ActivityTransferTokenBinding binding;
    String tokensTransferTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transfer_token);

        binding.transferHistory.setOnClickListener(this);

        binding.agreeTV.setOnClickListener(this);
        binding.cancelTV.setOnClickListener(this);

        ApiCalls.getChainHistoryTransactions(this, Prefes.getAccessToken(this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")){
                        JSONObject data = jsonObject.getJSONObject("data");

                        Log.d("jhgsdasdasd", data.toString());

                        //tokenSymbol":"VYNC","tokensTransferTitle":"100 VYNK","days":30,
                        String tokenSymbol = data.getString("tokenSymbol");
                        String days = data.optString("days");
                        tokensTransferTitle = data.optString("tokensTransferTitle");

                        JSONArray listing = data.getJSONArray("listing");

                        boolean newTransfer = true;
                        for (int i=0; i<listing.length(); i++){
                            JSONObject object = listing.getJSONObject(i);
                            String statusString = object.optString("status");
                            String created_at = object.optString("created_at");
                            if (statusString.equals("3")){
                                newTransfer = false;
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    Date matchDate = format.parse(created_at);
                                    Calendar cal = GregorianCalendar.getInstance();
                                    cal.setTime(matchDate);
                                    cal.add(Calendar.DATE, 30);
                                    Date againTransferDate = cal.getTime();
                                    String dateString = Functions.changeDateFormat(againTransferDate.toString(), "EEE MMM dd HH:mm:ss ZZZZZ yyyy" , "MMM dd, yyyy");
                                    binding.messageTV.setText("You're allowed to transfer only "+tokensTransferTitle+" tokens in every "+days+" days , and You already did a transfer. Comeback on "+dateString);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                Date currentTime = Calendar.getInstance().getTime();
                                String todayTime = Functions.changeDateFormat(currentTime.toString(), "EEE MMM dd HH:mm:ss Z yyyy", "yyyy-MM-dd HH:mm:ss");
                                countDown(todayTime, created_at, days, tokensTransferTitle);
                                break;
                            }
                        }


                        if (newTransfer){
                            binding.messageTV.setText("You're allowed to transfer only "+tokensTransferTitle+" tokens in every "+days+" days ");
                            binding.timeLeftTV.setVisibility(View.GONE);
                            binding.agreeLayout.setVisibility(View.VISIBLE);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

        clicks();
    }

    private void clicks() {
        binding.backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.toolbarTitle.setText("Transfer Token");
    }

    public void countDown(String todayTime, String matchStartTime, String time, String token){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date todayDate = format.parse(todayTime);
            Date matchDate = format.parse(matchStartTime);

            Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(matchDate);
            cal.add(Calendar.DATE, Integer.parseInt(time));
            Date againTransferDate = cal.getTime();

            if (againTransferDate!=null && todayDate!=null){

                final long diffInSec = againTransferDate.getTime() - todayDate.getTime();

                if (diffInSec>0){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new CountDownTimer(diffInSec, 1000) {
                                public void onTick(long millisUntilFinished) {

                                    long totalSec = millisUntilFinished / 1000;
                                    long hour = totalSec / 3600;
                                    long min = (totalSec % 3600) / 60;
                                    long sec = totalSec % 60;

                                    String timeString;
                                    if (hour>23){
                                        long days=hour/24;
                                        timeString = String.format("%02d day(s) %02dm %02ds", days, min, sec);

                                    }else if (hour>0){
                                        timeString = String.format("%02dh %02dm %02ds", hour, min, sec);
                                    }else{
                                        if (min>0){
                                            timeString = String.format("%02dm %02ds", min, sec);
                                        }else {
                                            timeString = String.format("%02ds", sec);
                                        }
                                    }

                                    binding.timeLeftTV.setVisibility(View.VISIBLE);
                                    binding.agreeLayout.setVisibility(View.GONE);
                                    binding.timeLeftTV.setText(timeString);
                                }

                                public void onFinish() {
                                    // you can transfer token
                                    binding.messageTV.setText("You're allowed to transfer only "+token+" tokens in every "+time+" days ");
                                    binding.timeLeftTV.setVisibility(View.GONE);
                                    binding.agreeLayout.setVisibility(View.VISIBLE);
                                }
                            }.start();
                        }
                    });

                }else {
                    // you can transfer token
                    binding.messageTV.setText("You're allowed to transfer only "+token+" tokens in every "+time+" days ");
                    binding.timeLeftTV.setVisibility(View.GONE);
                    binding.agreeLayout.setVisibility(View.VISIBLE);

                }

            }



        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == binding.transferHistory){
            startActivity(new Intent(this, TokenTrnsfrHistory.class));
        }else if (v == binding.agreeTV){
            transferDialog();
        }else if (v == binding.cancelTV){
            finish();
        }
    }

    public void transferDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        View view = LayoutInflater.from(this).inflate(R.layout.transfer_token_layout, null);
        builder.setView(view);

        TextView transferToken = view.findViewById(R.id.transferToken);
        EditText addressTV = view.findViewById(R.id.addressTV);
        TextView transferTV = view.findViewById(R.id.transferTV);
        ProgressBar progressBar = view.findViewById(R.id.progress);

        transferTV.setVisibility(View.VISIBLE);
        transferToken.setText("Transfer Tokens: "+tokensTransferTitle);

        transferTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressTV.getText().toString().trim().isEmpty()){
                    Toast.makeText(TransferToken.this, "Please enter Tron address", Toast.LENGTH_SHORT).show();
                }else {
                    transferTV.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    ApiCalls.transferToken(TransferToken.this, Prefes.getAccessToken(TransferToken.this), addressTV.getText().toString().trim(), new VolleyResponse() {
                        @Override
                        public void onResult(String result, String status, String message) {
                            Log.d("sdasdkkkk", result);
                            progressBar.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                //{"status":true,"message":"Token Transferred Requested Successfully"}
                                if (jsonObject.getBoolean("status")){
                                    Toast.makeText(TransferToken.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(TransferToken.this, TokenTrnsfrHistory.class));
                                    finish();
                                }else {
                                    Toast.makeText(TransferToken.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);
        alertDialog.show();


        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
    }
}