package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vynkpay.R;
import com.vynkpay.activity.activitiesnew.RequestWithdrawnActivity;
import com.vynkpay.activity.activitiesnew.WithdrawTypeActivity;
import com.vynkpay.adapter.AllTransactionAdapter;
import com.vynkpay.adapter.WalletTransactionAdapter;
import com.vynkpay.databinding.ActivityAllTransactionsBinding;
import com.vynkpay.fragment.BonusWalletFragment;
import com.vynkpay.fragment.VCashWalletFragment;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class AllTransactionsActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityAllTransactionsBinding binding;
    String tabType;
    public static ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList = new ArrayList<>();
    //WalletTransactionAdapter walletTransactionAdapter;
    //private int preLast;
    int pageNumber = 1;

    AllTransactionAdapter allTransactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding= DataBindingUtil.setContentView(this, R.layout.activity_all_transactions);
        dev();
    }

    private void dev() {
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.toolbar.notifyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllTransactionsActivity.this, NotificationActivity.class));
            }
        });

        binding.recyclerView.setLayoutManager(Functions.layoutManager(AllTransactionsActivity.this, Functions.VERTICAL, 0));
        allTransactionAdapter = new AllTransactionAdapter(AllTransactionsActivity.this, walletTransactionsModelArrayList);
        binding.recyclerView.setAdapter(allTransactionAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            tabType = intent.getStringExtra("tabType");

            if (tabType.equals("bonus")) {
                binding.toolbar.toolbarTitle.setText("Earning transactions");
                getBonusTransaction("1", true);

            } else if (tabType.equals("vCash")) {
                binding.toolbar.toolbarTitle.setText("Vcash transactions");
                getVCashTransaction("1", true);

            } else if (tabType.equals("mCash")) {
                binding.toolbar.toolbarTitle.setText("Mcash transactions");
                getTransactionsHistory("1", true);

            } else if (tabType.equals("eCash")) {
                binding.toolbar.toolbarTitle.setText("Mcash Requests");

            }
        }

        /*View view = LayoutInflater.from(AllTransactionsActivity.this).inflate(R.layout.empty_space_layout, null);
        binding.transactionsListView.addHeaderView(view);
        walletTransactionAdapter = new WalletTransactionAdapter(AllTransactionsActivity.this, walletTransactionsModelArrayList, true);
        binding.transactionsListView.setAdapter(walletTransactionAdapter);
        binding.transactionsListView.setOnScrollListener(this);*/


        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    pageNumber++;
                    if (tabType.equals("bonus")) {
                        getBonusTransaction(String.valueOf(pageNumber), false);
                    } else if (tabType.equals("vCash")) {
                        getVCashTransaction(String.valueOf(pageNumber), false);
                    } else if (tabType.equals("mCash")) {
                        getTransactionsHistory(String.valueOf(pageNumber), false);
                    }
                }
            }
        });


        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNumber = 1;
                if (tabType.equals("bonus")) {
                    getBonusTransaction(String.valueOf(pageNumber), true);
                } else if (tabType.equals("vCash")) {
                    getVCashTransaction(String.valueOf(pageNumber), true);
                } else if (tabType.equals("mCash")) {
                    getTransactionsHistory(String.valueOf(pageNumber), true);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(AllTransactionsActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(AllTransactionsActivity.this,AllTransactionsActivity.this::finishAffinity);
        }
    }


    private void getVCashTransaction(String pageNumber, boolean isRefresh){
        ApiCalls.getVcashTransactions(AllTransactionsActivity.this, Prefes.getAccessToken(AllTransactionsActivity.this), pageNumber, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {

                if (binding.swipeLayout.isRefreshing()){
                    binding.swipeLayout.setRefreshing(false);
                }

                if (isRefresh){
                    walletTransactionsModelArrayList.clear();
                }

                Log.d("transactionZLog", result + "//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")) {
                        //serverDialog.dismiss();
                        JSONObject dataObject = jsonObject.getJSONObject("data");

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

                            walletTransactionsModelArrayList.add(new WalletTransactionsModel(id, front_user_id, user_id, type,
                                    payment_via, p_amount, profit_type, mode, transactionStatus, created_date, username,
                                    email, phone, name, paid_status, balance, frontusername));

                        }
                        //Collections.reverse(walletTransactionsModelArrayList);

                        allTransactionAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                if (binding.swipeLayout.isRefreshing()){
                    binding.swipeLayout.setRefreshing(false);
                }
            }

        });
    }

    public void getTransactionsHistory(String pageNumber, boolean isRefresh){

        ApiCalls.getMcashTransactions(AllTransactionsActivity.this, Prefes.getAccessToken(AllTransactionsActivity.this), pageNumber, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                if (isRefresh){
                    walletTransactionsModelArrayList.clear();
                }
                if (binding.swipeLayout.isRefreshing()){
                    binding.swipeLayout.setRefreshing(false);
                }
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        JSONObject dataObject=jsonObject.getJSONObject("data");

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

                        //Collections.reverse(walletTransactionsModelArrayList);

                        allTransactionAdapter.notifyDataSetChanged();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                if (binding.swipeLayout.isRefreshing()){
                    binding.swipeLayout.setRefreshing(false);
                }
            }
        });
    }

    public void getBonusTransaction(String pageNumber, boolean isRefresh){
        ApiCalls.getBonusTransactions(AllTransactionsActivity.this, Prefes.getAccessToken(AllTransactionsActivity.this), pageNumber, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {

                if (isRefresh){
                    walletTransactionsModelArrayList.clear();
                }

                if (binding.swipeLayout.isRefreshing()){
                    binding.swipeLayout.setRefreshing(false);
                }

                Log.d("transactionZLog", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        JSONObject dataObject=jsonObject.getJSONObject("data");
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

                        //Collections.reverse(walletTransactionsModelArrayList);

                        allTransactionAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String error) {
                if (binding.swipeLayout.isRefreshing()){
                    binding.swipeLayout.setRefreshing(false);
                }
            }
        });
    }

}
