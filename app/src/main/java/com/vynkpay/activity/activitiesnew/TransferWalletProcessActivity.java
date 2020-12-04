package com.vynkpay.activity.activitiesnew;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.vynkpay.R;
import com.vynkpay.activity.PinActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityTransferWalletProcessBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetUserResponse;
import com.vynkpay.retrofit.model.TransferMoney;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferWalletProcessActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityTransferWalletProcessBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    NormalButton submitButton;
    Dialog dialog, serverDialog;
    RecyclerView countryRecycler;
    String userId = "";
    UserAdapter adapter;
    final int rcWallet = 12;
    String value = "",amount="",remarks="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_transfer_wallet_process);
        serverDialog = M.showDialog(TransferWalletProcessActivity.this, "", false, false);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        submitButton = findViewById(R.id.submitButton);
        binding.tvAvailableBal.setText(Functions.CURRENCY_SYMBOL+TranferWalletActivity.bonusBalance);
        toolbarTitle.setText(getString(R.string.transferWallet));
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitButton.setOnClickListener(this);
        binding.usernameET.setOnClickListener(this);
        initTextWatchers();
        try {
            binding.amountET.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(12,2)});
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void initTextWatchers() {
        binding.amountET.addTextChangedListener(new TextWatcher() {
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

        binding.remarkET.addTextChangedListener(new TextWatcher() {
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

        binding.amountET.setOnClickListener(this);
        binding.remarkET.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == submitButton) {
            if (TextUtils.isEmpty(binding.usernameET.getText().toString().trim())) {
                Toast.makeText(TransferWalletProcessActivity.this, "Please Select User", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.amountET.getText().toString().trim())) {
                Toast.makeText(TransferWalletProcessActivity.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();
            } else if (Float.parseFloat(binding.amountET.getText().toString().trim()) > Float.parseFloat(TranferWalletActivity.bonusBalance.trim())) {
                Toast.makeText(TransferWalletProcessActivity.this, "This amount is not available in wallet", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.remarkET.getText().toString().trim())) {
                Toast.makeText(TransferWalletProcessActivity.this, "Please Enter Remarks", Toast.LENGTH_SHORT).show();
            } else {
                transferMoneyByServer();
            }
        }

        if (view == binding.amountET) {
            if (view.getId() ==  binding.amountET.getId()) {
                binding.amountET.setCursorVisible(true);
                binding.remarkET.setCursorVisible(false);
            }
        }

        if (view == binding.remarkET) {
            if (binding.remarkET.getId() ==  binding.remarkET.getId()) {
                binding.remarkET.setCursorVisible(true);
                binding.remarkET.setCursorVisible(false);
            }
        }

        if (view == binding.usernameET) {
            binding.usernameET.setText("");
            dialog = new Dialog(TransferWalletProcessActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow()!=null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
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
                        MainApplication.getApiService().getUser(Prefes.getAccessToken(TransferWalletProcessActivity.this), arg0.toString(),"earning").enqueue(new Callback<GetUserResponse>() {
                            @Override
                            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().getStatus()) {

                                        countryRecycler.setVisibility(View.VISIBLE);
                                        GridLayoutManager manager = new GridLayoutManager(TransferWalletProcessActivity.this, 1, GridLayoutManager.VERTICAL, false);
                                        adapter = new UserAdapter(TransferWalletProcessActivity.this, response.body().getData(), binding.usernameET);
                                        countryRecycler.setLayoutManager(manager);
                                        countryRecycler.setAdapter(adapter);

                                    } else {
                                        Toast.makeText(TransferWalletProcessActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        countryRecycler.setVisibility(View.GONE);
                                    }
                                } else {
                                    Log.d("userResponse","response not successfull");
                                }
                            }

                            @Override
                            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                                Toast.makeText(TransferWalletProcessActivity.this, t.getMessage()!=null?t.getMessage():"Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            dialog.show();
        }
    }

    private void transferMoneyByServer() {
        startActivityForResult(new Intent(TransferWalletProcessActivity.this, PinActivity.class)
                .putExtra("type", "Wallet")
                .putExtra("isIndian",Prefes.getisIndian(TransferWalletProcessActivity.this))
                .putExtra("accessToken", Prefes.getAccessToken(TransferWalletProcessActivity.this)), rcWallet);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case rcWallet: {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        value = data.getStringExtra("edttext");
                        Log.e("MCahsFragment", "DataRetunred " + value);
                        serverDialog.show();
                        MainApplication.getApiService().transferMoney(Prefes.getAccessToken(TransferWalletProcessActivity.this), value, userId, amount, remarks).enqueue(new Callback<TransferMoney>() {
                            @Override
                            public void onResponse(Call<TransferMoney> call, Response<TransferMoney> response) {
                                if (response.isSuccessful()) {
                                    serverDialog.dismiss();
                                    if (response.body() != null) {
                                        Log.d("transferwallet",new Gson().toJson(response.body()));
                                        if(response.body().isStatus()){
                                            //startActivity(new Intent(TransferWalletProcessActivity.this, RequestSuccess.class).putExtra("msg",response.body().getMessage()).putExtra("typ","Bonus"));
                                            startActivity(new Intent(TransferWalletProcessActivity.this, TransferSuccessActivity.class).putExtra("msg",response.body().getMessage()).putExtra("typ","Bonus").putExtra("invoice_number",response.body().getInvoice_number()));
                                            TransferWalletProcessActivity.this.finish();
                                        } else {
                                            Toast.makeText(TransferWalletProcessActivity.this, response.body().getMessage()!=null?response.body().getMessage() : "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<TransferMoney> call, Throwable t) {
                                Log.d("transwallet",t.getMessage() != null ? t.getMessage() : "Error");
                                serverDialog.dismiss();
                            }
                        });
                    }
                }
            }
        }
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
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
            public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(TransferWalletProcessActivity.this).inflate(R.layout.country_list, parent, false);
                return new MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, int position) {
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

            class MyViewHolder extends RecyclerView.ViewHolder {
                TextView countryText;

                public MyViewHolder(View view) {
                    super(view);
                    countryText = view.findViewById(R.id.countryText);
                }
            }
        }

}



    /*
    binding.bonusHeader.searchUserET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    */