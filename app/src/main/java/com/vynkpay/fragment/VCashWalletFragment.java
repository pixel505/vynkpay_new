package com.vynkpay.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.AllTransactionsActivity;
import com.vynkpay.adapter.WalletTransactionAdapter;
import com.vynkpay.databinding.FragmentVcashWalletBinding;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.utils.M;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VCashWalletFragment extends AppCompatActivity {
    FragmentVcashWalletBinding binding;
    public static ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList = new ArrayList<>();
    public static String availableBalance;
    VCashWalletFragment activity;
    Dialog serverDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_vcash_wallet);
        activity=VCashWalletFragment.this;
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
        binding.toolbarLayout.toolbarTitlenew.setText("VCash Wallet");
        dev();
    }

    private void dev() {
        if(getIntent()!=null){
            binding.bonusHeader.availableBalanceTV.setText(Functions.CURRENCY_SYMBOL+getIntent().getStringExtra("balancWalletV"));
        }
       binding.bonusHeader.filterLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });



        binding.transactionsListView.setAdapter(new WalletTransactionAdapter(activity, walletTransactionsModelArrayList, false));

        if (walletTransactionsModelArrayList.size()>5){
            binding.bonusHeader.viewAll.setVisibility(View.VISIBLE);
        }else {
            binding.bonusHeader.viewAll.setVisibility(View.GONE);
        }

        if (walletTransactionsModelArrayList.size()>0){
            binding.noLayout.setVisibility(View.GONE);
        }else {
            binding.noLayout.setVisibility(View.VISIBLE);
        }

        binding.bonusHeader.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllTransactionsActivity.walletTransactionsModelArrayList = walletTransactionsModelArrayList;
                startActivity(new Intent(activity, AllTransactionsActivity.class).putExtra("tabType", "vCash"));
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
}
