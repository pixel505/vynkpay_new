package com.vynkpay.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vynkpay.activity.PinActivity;
import com.vynkpay.activity.activities.RequestSuccess;
import com.vynkpay.activity.activitiesnew.RequestWithdrawnActivity;
import com.vynkpay.activity.activitiesnew.TransferWalletProcessActivity;
import com.vynkpay.activity.activitiesnew.WithdrawTypeActivity;
import com.vynkpay.adapter.ECashAdapter;
import com.vynkpay.adapter.WithdrawalTypeAdapter;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetUserResponse;
import com.vynkpay.retrofit.model.TransferMoney;
import com.vynkpay.retrofit.model.WithdrawalTypeTesponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.AllTransactionsActivity;
import com.vynkpay.activity.activities.WithdrawRequestListActivity;
import com.vynkpay.adapter.WalletTransactionAdapter;
import com.vynkpay.databinding.FragmentBonusWalletBinding;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.M;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BonusWalletFragment extends AppCompatActivity {
    FragmentBonusWalletBinding binding;
    public static ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList = new ArrayList<>();
    public static String availableBalance = "0";
    Dialog dialog, serverDialog;
    String userId;
    final int rcWallet = 12;
    String value, amount, remarks;
    RecyclerView countryRecycler;
    BonusWalletFragment.UserAdapter adapter;
    BonusWalletFragment activity;
    String withdrawType = "";
    boolean isClicked = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_bonus_wallet);
        activity=BonusWalletFragment.this;
        //Toast.makeText(activity, "Hello", Toast.LENGTH_SHORT).show();
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
        //binding.toolbarLayout.toolbarTitlenew.setText("Bonus Wallet");
        binding.toolbarLayout.toolbarTitlenew.setText("Cashback");
        dev();
    }

    private void dev() {

        binding.bonusHeader.amountNew2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("MCashWaleet", "AmoutText " + charSequence.toString());
                amount = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.bonusHeader.remarkNew2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("MCashWaleet", "RemrkTExt " + charSequence.toString());
                remarks = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.bonusHeader.amountNew2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() ==  binding.bonusHeader.amountNew2.getId()) {
                    binding.bonusHeader.amountNew2.setCursorVisible(true);
                    binding.bonusHeader.remarkNew2.setCursorVisible(false);
                }
            }
        });

        binding.bonusHeader.remarkNew2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() ==  binding.bonusHeader.remarkNew2.getId()) {
                    binding.bonusHeader.remarkNew2.setCursorVisible(true);
                    binding.bonusHeader.amountNew2.setCursorVisible(false);
                }
            }
        });

        binding.bonusHeader.searchUserET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.bonusHeader.searchUserET.setText("");
                dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.country_dialog);
                countryRecycler = dialog.findViewById(R.id.countryRecycler);
                EditText searchEdt = dialog.findViewById(R.id.searchEdt);
                searchEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        if (arg0.toString().isEmpty()) {
                            Log.e("name", "issssssssssssssss" + arg0.toString());
                            countryRecycler.setVisibility(View.GONE);
                        }
                        if (arg0.toString().length() >= 3) {
                            MainApplication.getApiService().getUser(Prefes.getAccessToken(activity), arg0.toString()).enqueue(new Callback<GetUserResponse>() {
                                @Override
                                public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus()) {

                                            countryRecycler.setVisibility(View.VISIBLE);
                                            GridLayoutManager manager = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
                                            adapter = new BonusWalletFragment.UserAdapter(activity, response.body().getData(),binding.bonusHeader.searchUserET);
                                            countryRecycler.setLayoutManager(manager);
                                            countryRecycler.setAdapter(adapter);

                                        } else {
                                            Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            countryRecycler.setVisibility(View.GONE);
                                        }
                                    } else {
                                    }
                                }

                                @Override
                                public void onFailure(Call<GetUserResponse> call, Throwable t) {
                                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                dialog.show();
            }
        });


        binding.bonusHeader.sendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BonusWalletFragment.this, TransferWalletProcessActivity.class));
              /*if (binding.bonusHeader.searchUserET.getText().toString().isEmpty()) {
                    Toast.makeText(activity, "Please Select User", Toast.LENGTH_SHORT).show();
                } else if ( binding.bonusHeader.amountNew2.getText().toString().isEmpty()) {
                    Toast.makeText(activity, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                } else if (binding.bonusHeader.remarkNew2.getText().toString().isEmpty()) {
                    Toast.makeText(activity, "Please Enter Remarks", Toast.LENGTH_SHORT).show();
                } else {
                    transferMoneyByServer();
                }*/
            }
        });


        binding.bonusHeader.filterLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

               getBonusTransaction();

               //popupWithdrawalAmountIntern();

    }



    public void popupWithdrawalAmountIntern() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.withdraw_type, null);
        builder.setCancelable(true);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
        ProgressBar progressBar = view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView withdrawalRecycler=view.findViewById(R.id.withdrawalRecycler);
        NormalTextView amountET =view.findViewById(R.id.amountET);
        withdrawalRecycler.setVisibility(View.GONE);
        LinearLayout linWithdrawnType=view.findViewById(R.id.linWithdrawnType);
        linWithdrawnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClicked){
                    withdrawalRecycler.setVisibility(View.VISIBLE);
                    isClicked = false;
                }else {
                    withdrawalRecycler.setVisibility(View.GONE);
                    isClicked = true;
                }

            }
        });
        amountET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClicked){
                    withdrawalRecycler.setVisibility(View.VISIBLE);
                    isClicked = false;
                }else {
                    withdrawalRecycler.setVisibility(View.GONE);
                    isClicked = true;
                }
            }
        });
        MainApplication.getApiService().getWithdrawalType(Prefes.getAccessToken(activity)).enqueue(new Callback<WithdrawalTypeTesponse>() {
            @Override
            public void onResponse(Call<WithdrawalTypeTesponse> call, Response<WithdrawalTypeTesponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Log.d("withdrawType",new Gson().toJson(response.body()));
                    progressBar.setVisibility(View.GONE);
                    if(response.body().getStatus().equals("true")){
                        GridLayoutManager manager = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
                        WithdrawalTypeAdapter adapter = new WithdrawalTypeAdapter(activity, response.body().getData().getWithdrawalType());
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
                    }

                    else{
                        progressBar.setVisibility(View.GONE);
                    }
                }

                else{
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<WithdrawalTypeTesponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });


        Button button = view.findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (withdrawType.isEmpty()) {
                    Toast.makeText(activity, "Please select withdrawal Type", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    popupWithdrawalAmount();
               /*     progressBar.setVisibility(View.VISIBLE);
                    button.setEnabled(false);
                    ApiCalls.sendWalletOTP(activity, Prefes.getAccessToken(activity), new VolleyResponse() {
                        @Override
                        public void onResult(String result, String status, String message) {
                            progressBar.setVisibility(View.GONE);
                            button.setEnabled(true);
                            dialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Toast.makeText(activity, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();
                                if (jsonObject.getString("status").equals("true")) {
                                    //popupOTPCall(amount);
                                    popupWithdrawalAmount();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            progressBar.setVisibility(View.GONE);
                            button.setEnabled(true);
                            dialog.dismiss();
                        }
                    });*/
                }
            }
        });


        if (dialog.getWindow() != null) {
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
    }


    public void popupWithdrawalAmount() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.withdraw_request_amount_layout, null);
        builder.setCancelable(true);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
        ProgressBar progressBar = view.findViewById(R.id.progress);
        EditText amountET = view.findViewById(R.id.amountET);
        Button button = view.findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountET.getText().toString().trim().isEmpty()) {
                    Toast.makeText(activity, "Please enter amount", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(amountET.getText().toString().trim()) <= 0) {
                    Toast.makeText(activity, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                } else if (Float.parseFloat(amountET.getText().toString().trim()) > Float.parseFloat(availableBalance.trim())) {
                    Toast.makeText(activity, "This amount is not available in wallet", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    button.setEnabled(false);
                    ApiCalls.sendWalletOTP(activity, Prefes.getAccessToken(activity), new VolleyResponse() {
                        @Override
                        public void onResult(String result, String status, String message) {
                            progressBar.setVisibility(View.GONE);
                            button.setEnabled(true);
                            dialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Toast.makeText(activity, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();
                                if (jsonObject.getString("status").equals("true")) {
                                    popupOTPCall(amountET.getText().toString().trim());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            progressBar.setVisibility(View.GONE);
                            button.setEnabled(true);
                            dialog.dismiss();
                        }
                    });
                }
            }
        });


        if (dialog.getWindow() != null) {
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
    }

    public void popupOTPCall(final String amount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.withdraw_request_otp_layout, null);
        builder.setCancelable(true);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();

        //Toast.makeText(BonusWalletFragment.this, amount, Toast.LENGTH_SHORT).show();

        EditText otpET = view.findViewById(R.id.otpET);
        ProgressBar progressBar = view.findViewById(R.id.progress);
        Button button = view.findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpET.getText().toString().trim().isEmpty()) {
                    Toast.makeText(activity, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    button.setEnabled(false);
                    ApiCalls.checkWalletOTP(activity, Prefes.getAccessToken(activity), otpET.getText().toString().trim(), new VolleyResponse() {
                        @Override
                        public void onResult(String result, String status, String message) {
                            /*progressBar.setVisibility(View.GONE);
                            button.setEnabled(true);*/
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getString("status").equals("true")) {

                                    payNow(amount, progressBar, dialog, button);

                                    //popupPaymentMode(amount);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    button.setEnabled(true);
                                    Toast.makeText(activity, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                                button.setEnabled(true);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            progressBar.setVisibility(View.GONE);
                            button.setEnabled(true);
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
    }

    public void popupWithdrawalSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.withdraw_request_success_layout, null);
        builder.setCancelable(false);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();


        Button button = view.findViewById(R.id.submitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(activity, WithdrawRequestListActivity.class));
                activity.finish();
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
    }

    public void payNow(String amount, ProgressBar progressBar, Dialog dialog, Button button) {
        progressBar.setVisibility(View.VISIBLE);
        button.setEnabled(false);

        String action = "wr";
        if (Functions.isIndian) {
            action = "wr";
        }else {
            action = withdrawType;
        }

        ApiCalls.withdrawalRequest(activity, Prefes.getAccessToken(activity), amount, action, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                progressBar.setVisibility(View.GONE);
                button.setEnabled(true);
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")) {
                        popupWithdrawalSuccess();
                    } else {
                        Toast.makeText(activity, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                button.setEnabled(true);
                dialog.dismiss();
            }
        });
    }

    public void popupPaymentMode(final String amount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.withdraw_payment_methods_layout, null);
        builder.setCancelable(true);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();

        RadioButton impsRadio = view.findViewById(R.id.impsRadio);
        RadioButton neftRadio = view.findViewById(R.id.neftRadio);
        Button button = view.findViewById(R.id.submitButton);
        ProgressBar progressBar = view.findViewById(R.id.progress);

        impsRadio.setChecked(true);
        neftRadio.setChecked(false);


        progressBar.setVisibility(View.VISIBLE);
        ApiCalls.updatePaymentMode(activity, Prefes.getAccessToken(activity), "0", new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                progressBar.setVisibility(View.GONE);
                neftRadio.setChecked(false);
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
            }
        });


        impsRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        neftRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    progressBar.setVisibility(View.VISIBLE);
                    ApiCalls.updatePaymentMode(activity, Prefes.getAccessToken(activity), "1", new VolleyResponse() {
                        @Override
                        public void onResult(String result, String status, String message) {
                            progressBar.setVisibility(View.GONE);
                            impsRadio.setChecked(false);
                        }

                        @Override
                        public void onError(String error) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                button.setEnabled(false);

                ApiCalls.withdrawalRequest(activity, Prefes.getAccessToken(activity), amount, "wr", new VolleyResponse() {
                    @Override
                    public void onResult(String result, String status, String message) {
                        progressBar.setVisibility(View.GONE);
                        button.setEnabled(true);
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("status").equals("true")) {
                                popupWithdrawalSuccess();
                            } else {
                                Toast.makeText(activity, jsonObject.getString("message") + "", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(String error) {
                        progressBar.setVisibility(View.GONE);
                        button.setEnabled(true);
                        dialog.dismiss();
                    }
                });


            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
    }

    private void transferMoneyByServer() {
        startActivityForResult(new Intent(activity, PinActivity.class)
                .putExtra("type", "Wallet")
                .putExtra("accessToken", Prefes.getAccessToken(activity)), rcWallet);


    }


    private class UserAdapter extends RecyclerView.Adapter<BonusWalletFragment.UserAdapter.MyViewHolder> {
        Context context;
        List<GetUserResponse.Datum> mList;
        List<GetUserResponse.Datum> searchedItemModelArrayList;
        NormalEditText sear;


        public UserAdapter(Context applicationContext, List<GetUserResponse.Datum> data, NormalEditText searchET) {
            this.context = applicationContext;
            this.mList = data;
            this.searchedItemModelArrayList = new ArrayList<>();
            this.searchedItemModelArrayList.addAll(mList);
            this.sear=searchET;

        }

        @Override
        public BonusWalletFragment.UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_list, parent, false);

            return new BonusWalletFragment.UserAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(BonusWalletFragment.UserAdapter.MyViewHolder holder, int position) {
            GetUserResponse.Datum data = mList.get(position);
            holder.countryText.setText(data.getText());
            holder.countryText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    sear.setText(data.getText());
                    userId = data.getId();
                    // countryId=data.getId();


                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            mList.clear();
            if (charText.length() == 0) {
                mList.addAll(searchedItemModelArrayList);
            } else {
                for (GetUserResponse.Datum wp : searchedItemModelArrayList) {
                    if (wp.getText().toLowerCase(Locale.getDefault()).contains(charText)) {
                        mList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView countryText;

            public MyViewHolder(View view) {
                super(view);
                countryText = view.findViewById(R.id.countryText);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case rcWallet: {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        value = data.getStringExtra("edttext");
                        Log.e("MCahsFragment", "DataRetunred " + value);
                        serverDialog.show();
                        MainApplication.getApiService().transferMoney(Prefes.getAccessToken(activity), value, userId, amount, remarks).enqueue(new Callback<TransferMoney>() {
                            @Override
                            public void onResponse(Call<TransferMoney> call, Response<TransferMoney> response) {
                                if (response.isSuccessful()) {
                                    serverDialog.dismiss();
                                    if(response.body().isStatus()){
                                        startActivity(new Intent(activity, RequestSuccess.class).putExtra("msg",response.body().getMessage()).putExtra("typ","Bonus"));
                                    } else {
                                        Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<TransferMoney> call, Throwable t) {
                                serverDialog.dismiss();
                            }
                        });
                    }
                }
            }
        }
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    public void getBonusTransaction(){
        serverDialog.show();
        ApiCalls.getBonusTransactions(activity, Prefes.getAccessToken(activity), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("transactionZLog", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        serverDialog.dismiss();
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                          availableBalance=dataObject.getString("walletBalance");
                        binding.bonusHeader.availableBalanceTV.setText(Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));
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

                            WalletTransactionAdapter adapter = new WalletTransactionAdapter(activity, walletTransactionsModelArrayList, false);
                            binding.transactionsListView.setAdapter(adapter);




                            if (walletTransactionsModelArrayList.size() > 5) {
                                binding.bonusHeader.viewAll.setVisibility(View.VISIBLE);
                            } else {
                                binding.bonusHeader.viewAll.setVisibility(View.GONE);
                            }
                            if (walletTransactionsModelArrayList.size() > 0) {
                                binding.noLayout.setVisibility(View.GONE);
                            } else {
                                binding.noLayout.setVisibility(View.VISIBLE);
                            }
                            binding.bonusHeader.  viewAll.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AllTransactionsActivity.walletTransactionsModelArrayList = walletTransactionsModelArrayList;
                                    startActivity(new Intent(activity, AllTransactionsActivity.class).putExtra("tabType", "bonus"));
                                }
                            });

                            binding.bonusHeader.requestLinear.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Functions.isIndian) {
                                        //popupWithdrawalAmount();
                                        startActivity(new Intent(BonusWalletFragment.this, RequestWithdrawnActivity.class));
                                    }else {
                                        //popupWithdrawalAmountIntern();
                                        startActivity(new Intent(BonusWalletFragment.this, WithdrawTypeActivity.class));
                                    }
                                }
                            });
                        }

                        Collections.reverse(walletTransactionsModelArrayList);

                    }else {
                        serverDialog.dismiss();
                        Toast.makeText(activity, jsonObject.getString("message")+"", Toast.LENGTH_SHORT).show();
                        finish();
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
    protected void onResume() {
        binding.bonusHeader.searchUserET.setText("");
        binding.bonusHeader.amountNew2.setText("");
        binding.bonusHeader.remarkNew2.setText("");

        dev();
        super.onResume();
    }
}
