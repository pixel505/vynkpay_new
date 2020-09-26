package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.databinding.ActivityAffiliateBinding;
import com.vynkpay.fragment.MCashWalletFragment;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetTicketDepartment;
import com.vynkpay.retrofit.model.GetUserPackageResponse;
import com.vynkpay.retrofit.model.GetUserResponse;
import com.vynkpay.retrofit.model.PaidItemResponse;
import com.vynkpay.retrofit.model.SendWaletOtp;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AffiliateActivity extends AppCompatActivity  {
    ActivityAffiliateBinding binding;
    AffiliateActivity ac;
    Dialog dialog,dialog1;
    RecyclerView countryRecycler;
    UsrAdapter adapter;
    String userId,orgId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_affiliate);
        ac = AffiliateActivity.this;
        getMCashTransaction();
        dialog1 = M.showDialog(ac, "", false, false);
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(ac,WalletNewActivity.class));
               finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText("Affiliate Activation");
        clicks();

    }
    private void getMCashTransaction(){
            MCashWalletFragment.walletTransactionsModelArrayList.clear();
            ApiCalls.getMcashTransactions(ac, Prefes.getAccessToken(ac), new VolleyResponse() {
                @Override
                public void onResult(String result, String status, String message) {
                    //  Log.d("tmcashtrrr", result+"//");
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("status").equals("true")){
                            JSONObject dataObject=jsonObject.getJSONObject("data");
                          binding.walletText.setText(Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));



                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onError(String error) {
                }
            });
        }
    private void clicks() {
        binding.searchUserEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.searchUserEdt.setText("");
                dialog = new Dialog(ac);
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
                            MainApplication.getApiService().getUserAf(Prefes.getAccessToken(ac), arg0.toString()).enqueue(new Callback<GetUserResponse>() {
                                @Override
                                public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body().getStatus()) {
                                            countryRecycler.setVisibility(View.VISIBLE);
                                            GridLayoutManager manager = new GridLayoutManager(ac, 1, GridLayoutManager.VERTICAL, false);
                                            adapter = new AffiliateActivity.UsrAdapter(ac, response.body().getData(), binding.searchUserEdt);
                                            countryRecycler.setLayoutManager(manager);
                                            countryRecycler.setAdapter(adapter);
                                        } else {
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            countryRecycler.setVisibility(View.GONE);
                                        }
                                    } else {
                                    }
                                }

                                @Override
                                public void onFailure(Call<GetUserResponse> call, Throwable t) {
                                    Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                dialog.show();
            }
        });

        binding.getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.searchUserEdt.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Select User", Toast.LENGTH_SHORT).show();
                }

                else if(binding.packageText.getText().toString().isEmpty()){
                    Toast.makeText(ac, "No Package Found Select another User", Toast.LENGTH_SHORT).show();

                }
                else{
                      dialog1.show();
                      MainApplication.getApiService().getWalletOtp(Prefes.getAccessToken(ac)).enqueue(new Callback<SendWaletOtp>() {
                        @Override
                        public void onResponse(Call<SendWaletOtp> call, Response<SendWaletOtp> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                dialog1.dismiss();
                                Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Dialog dialog = new Dialog(ac);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dialog.setCancelable(false);
                                dialog.setCanceledOnTouchOutside(true);
                                dialog.setContentView(R.layout.otp_dialog);
                                NormalEditText otpEdt = dialog.findViewById(R.id.otpEditText);
                                NormalButton submit = dialog.findViewById(R.id.submitLn);

                                submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (otpEdt.getText().toString().isEmpty()) {
                                            Toast.makeText(ac, "Please Enter OTP", Toast.LENGTH_SHORT).show();

                                        } else {
                                            dialog1.show();
                                            MainApplication.getApiService().payitem(Prefes.getAccessToken(ac), userId, orgId, otpEdt.getText().toString()).enqueue(new Callback<PaidItemResponse>() {
                                                @Override
                                                public void onResponse(Call<PaidItemResponse> call, Response<PaidItemResponse> response) {
                                                    if (response.isSuccessful() && response.body() != null) {
                                                        dialog1.dismiss();
                                                        if (response.body().getStatus()) {
                                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                           startActivity(new Intent(ac,AccountAccessActivity.class));
                                                           finish();

                                                        } else {
                                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<PaidItemResponse> call, Throwable t) {
                                                    dialog1.dismiss();
                                                    Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    }
                                });


                                dialog.show();


                            }
                            else {
                                dialog1.dismiss();
                                Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SendWaletOtp> call, Throwable t) {
                            dialog1.dismiss();
                            Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
            }
            }
        });


    }
    private void getPackage() {
        MainApplication.getApiService().getUserPackage(Prefes.getAccessToken(ac), userId).enqueue(new Callback<GetUserPackageResponse>() {
            @Override
            public void onResponse(Call<GetUserPackageResponse> call, Response<GetUserPackageResponse> response) {
                if (response.isSuccessful() && response != null) {
                    if (response.body().getStatus()) {
                        binding.packageTextt.setVisibility(View.VISIBLE);
                        binding.packageCard.setVisibility(View.VISIBLE);
                         binding.packageText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().get(0).getTotalAmount());
                         binding.amountText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().get(0).getPrice());
                         binding.pointsText.setText("("+response.body().getData().get(0).getPoints()+" "+"points"+")");
                         orgId=response.body().getData().get(0).getId();
                    } else {
                         binding.packageCard.setVisibility(View.GONE);
                        binding.packageTextt.setVisibility(View.GONE);

                    }
                } else {
                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetUserPackageResponse> call, Throwable t) {
                Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private class UsrAdapter extends RecyclerView.Adapter<AffiliateActivity.UsrAdapter.MyViewHolder> {
        Context context;
        List<GetUserResponse.Datum> mList;
        List<GetUserResponse.Datum> searchedItemModelArrayList;
        NormalEditText searc;


        public UsrAdapter(Context applicationContext, List<GetUserResponse.Datum> data, NormalEditText searchET) {
            this.context = applicationContext;
            this.mList = data;
            this.searchedItemModelArrayList = new ArrayList<>();
            this.searchedItemModelArrayList.addAll(mList);
            this.searc = searchET;

        }

        @Override
        public AffiliateActivity.UsrAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_list, parent, false);
            return new AffiliateActivity.UsrAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AffiliateActivity.UsrAdapter.MyViewHolder holder, int position) {
            GetUserResponse.Datum data = mList.get(position);
            holder.countryText.setText(data.getText());
            holder.countryText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    searc.setText(data.getText());
                    userId = data.getId();
                    getPackage();

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
    protected void onResume() {
        if(dialog1!=null){
            dialog1.dismiss();
        }
        super.onResume();
    }
}