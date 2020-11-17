package com.vynkpay.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.vynkpay.activity.activities.ChoosePaymentActivityB;
import com.vynkpay.databinding.FragmentEcashWalletBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.R;
import com.vynkpay.adapter.ECashAdapter;
import com.vynkpay.models.EcashModelClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ECashWalletFragment extends AppCompatActivity {
    FragmentEcashWalletBinding binding;
    ECashWalletFragment activity;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_ecash_wallet);
        activity=ECashWalletFragment.this;
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        hideKeyboard(activity);
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText("Request MCash");
        dev();
    }

    private void dev() {

        MainApplication.getApiService().getECash(Prefes.getAccessToken(activity)).enqueue(new Callback<EcashModelClass>() {
            @Override
            public void onResponse(Call<EcashModelClass> call, Response<EcashModelClass> response) {
                if(response.isSuccessful() && response.body() !=null) {

                    if (response.body().getData() == null) {
                        binding.noLayout.setVisibility(View.VISIBLE);

                    }
                    else {

                        binding.noLayout.setVisibility(View.GONE);
                        ECashAdapter adapter = new ECashAdapter(activity, response.body().getData().getEcash());
                        binding.transactionsListView.setAdapter(adapter);
                        binding.transactionsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(AbsListView view, int scrollState) {
                                Log.e("scroll", String.valueOf(scrollState));
                            }
                            @Override
                            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                                if (firstVisibleItem < 2) {
                                    binding.newMcashReq1.setVisibility(View.VISIBLE);
                                    binding.newMcashReq1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(ECashWalletFragment.this, ChoosePaymentActivityB.class).putExtra("balancWalletM", getIntent().getStringExtra("purchaseprice")));

                                        }
                                    });
                                    binding.newMcashReq.setVisibility(View.GONE);
                                } else {


                                    binding.newMcashReq.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(ECashWalletFragment.this, ChoosePaymentActivityB.class).putExtra("balancWalletM", getIntent().getStringExtra("purchaseprice")));

                                        }
                                    });
                                    binding.newMcashReq1.setVisibility(View.GONE);
                                    binding.newMcashReq.setVisibility(View.VISIBLE);
                                }
                            }

                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<EcashModelClass> call, Throwable t) {

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
        dev();
        super.onResume();
    }
}
