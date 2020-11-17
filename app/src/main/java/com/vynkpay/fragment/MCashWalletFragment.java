package com.vynkpay.fragment;

import android.app.Activity;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vynkpay.activity.PinActivity;
import com.vynkpay.activity.activities.AffiliateActivity;
import com.vynkpay.activity.activities.RequestSuccess;
import com.vynkpay.activity.activitiesnew.TransferWalletMCashActivity;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetUserResponse;
import com.vynkpay.retrofit.model.TransferMoney;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.AllTransactionsActivity;
import com.vynkpay.adapter.WalletTransactionAdapter;
import com.vynkpay.databinding.FragmentMcashWalletBinding;
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
public class MCashWalletFragment extends AppCompatActivity {



    FragmentMcashWalletBinding binding;
    public static ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList = new ArrayList<>();
    public static String availableBalance;
    ArrayList<String> mList = new ArrayList<String>();
    ArrayList<String> mListId = new ArrayList<String>();
    String categoryString, orgId;
    Dialog dialog, serverDialog;
    String userId;
    final int rcWallet = 12;
    String value, amount, remarks;
    RecyclerView countryRecycler;
    MCashWalletFragment.UserAdapter adapter;
    MCashWalletFragment activity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_mcash_wallet);
        activity=MCashWalletFragment.this;
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
        if (Prefes.getUserType(activity).equalsIgnoreCase("2")){
            //binding.toolbarLayout.toolbarTitlenew.setText("Cashback");
            //binding.mheader.tvMcashWallet.setText("Cashback");
            binding.mheader.affilitiateFrame.setVisibility(View.GONE);
        } else {
            //
            //binding.mheader.tvMcashWallet.setText(getString(R.string.mCashWallet));
            binding.mheader.affilitiateFrame.setVisibility(View.VISIBLE);
        }
        binding.toolbarLayout.toolbarTitlenew.setText("MCash Wallet");

        dev();
    }

    private void dev() {
      //  View header = LayoutInflater.from(activity).inflate(R.layout.mcash_header_layout, null);
        serverDialog = M.showDialog(activity, "", false, false);
        if(getIntent()!=null){
            binding.mheader.availableBalanceTV.setText(Functions.CURRENCY_SYMBOL+getIntent().getStringExtra("balancWalletM"));
        }        if(Functions.isIndian){
            binding.mheader.affilitiateFrame.setVisibility(View.GONE);
        }

        else {
            binding.mheader.affilitiateFrame.setVisibility(View.VISIBLE);

            binding.mheader.affiliateActive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(activity, AffiliateActivity.class));

                }
            });
        }

        binding.mheader.amEdt.addTextChangedListener(new TextWatcher() {
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
        binding.mheader.remarknew.addTextChangedListener(new TextWatcher() {
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




        binding.mheader.amEdt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (v.getId() ==  binding.mheader.amEdt.getId()) {
                     binding.mheader.amEdt.setCursorVisible(true);
                     binding.mheader.amEdt.setCursorVisible(false);
                 }
             }
         });

        binding.mheader.remarknew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == binding.mheader.remarknew.getId()) {
                    binding.mheader.remarknew.setCursorVisible(true);
                    binding.mheader.remarknew.setCursorVisible(false);
                }
            }
        });


        binding.mheader.serachNewEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.mheader.serachNewEdt.setText("");
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
                                            adapter = new MCashWalletFragment.UserAdapter(activity, response.body().getData(), binding.mheader.serachNewEdt);
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
        binding.mheader.sendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.e("ampunt", "" + amount);
                startActivity(new Intent(MCashWalletFragment.this, TransferWalletMCashActivity.class));
               /* if ( binding.mheader.serachNewEdt.getText().toString().isEmpty()) {
                    Toast.makeText(activity, "Please Select User", Toast.LENGTH_SHORT).show();
                } else if (amount==null) {
                    Toast.makeText(activity, "Please Select Amount", Toast.LENGTH_SHORT).show();
                }
                else if (remarks==null) {
                    Toast.makeText(activity, "Please Enter Remarks", Toast.LENGTH_SHORT).show();
                }

                else {
                    transferMoneyByServer();
                }*/
            }
        });
        binding.mheader.filterLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        getMCashTransaction();
     //   binding.transactionsListView.addHeaderView(header);

       /* if (binding.transactionsListView.getHeaderViewsCount() > 1) {
            binding.transactionsListView.removeHeaderView(header);
        }*/


    }


    private void transferMoneyByServer() {
        startActivityForResult(new Intent(activity, PinActivity.class)
                .putExtra("type", "Wallet")
                .putExtra("accessToken", Prefes.getAccessToken(activity)), rcWallet);
    }


    private class UserAdapter extends RecyclerView.Adapter<MCashWalletFragment.UserAdapter.MyViewHolder> {
        Context context;
        List<GetUserResponse.Datum> mList;
        List<GetUserResponse.Datum> searchedItemModelArrayList;
        NormalEditText searc;


        public UserAdapter(Context applicationContext, List<GetUserResponse.Datum> data, NormalEditText searchET) {
            this.context = applicationContext;
            this.mList = data;
            this.searchedItemModelArrayList = new ArrayList<>();
            this.searchedItemModelArrayList.addAll(mList);
            this.searc = searchET;

        }

        @Override
        public MCashWalletFragment.UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_list, parent, false);
            return new MCashWalletFragment.UserAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MCashWalletFragment.UserAdapter.MyViewHolder holder, int position) {
            GetUserResponse.Datum data = mList.get(position);
            holder.countryText.setText(data.getText());
            holder.countryText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    searc.setText(data.getText());
                    userId = data.getId();
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
                        serverDialog.show();
                        MainApplication.getApiService().transfermMoney(Prefes.getAccessToken(activity), value, userId, amount, remarks).enqueue(new Callback<TransferMoney>() {
                            @Override
                            public void onResponse(Call<TransferMoney> call, Response<TransferMoney> response) {
                                if (response.isSuccessful()) {
                                    if(response.body().isStatus()){
                                        startActivity(new Intent(activity, RequestSuccess.class).putExtra("msg",response.body().getMessage()).putExtra("typ","MCash"));

                                    }

                                    else {
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
    private void getMCashTransaction(){
        serverDialog.show();
        ApiCalls.getMcashTransactions(activity, Prefes.getAccessToken(activity), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                //  Log.d("tmcashtrrr", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        serverDialog.dismiss();
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                        binding.mheader.availableBalanceTV.setText(Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));

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



                            binding.transactionsListView.setAdapter(new WalletTransactionAdapter(activity, walletTransactionsModelArrayList, false));

                            if (walletTransactionsModelArrayList.size() > 5) {
                                binding.mheader.viewAll.setVisibility(View.VISIBLE);
                            } else {
                                binding.mheader.viewAll.setVisibility(View.GONE);
                            }

                            if (walletTransactionsModelArrayList.size() > 0) {
                                binding.noLayout.setVisibility(View.GONE);
                            } else {
                                binding.noLayout.setVisibility(View.VISIBLE);
                            }

                            binding.mheader.viewAll.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AllTransactionsActivity.walletTransactionsModelArrayList = walletTransactionsModelArrayList;
                                    startActivity(new Intent(activity, AllTransactionsActivity.class).putExtra("tabType", "mCash"));
                                }
                            });

                        }

                        Collections.reverse(MCashWalletFragment.walletTransactionsModelArrayList);
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

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    @Override
    protected void onResume() {
        if(serverDialog!=null){
            serverDialog.dismiss();
        }
        binding.mheader.serachNewEdt.setText("");
        binding.mheader.amEdt.setText("");
        binding.mheader.remarknew.setText("");

        dev();
        super.onResume();
    }
}
