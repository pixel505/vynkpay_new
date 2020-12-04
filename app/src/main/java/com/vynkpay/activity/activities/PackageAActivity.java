package com.vynkpay.activity.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.google.gson.Gson;
import com.vynkpay.R;
import com.vynkpay.adapter.PackageAdapter;
import com.vynkpay.databinding.ActivityPackageABinding;
import com.vynkpay.models.PlanList;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetPackageResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageAActivity extends AppCompatActivity {

    ActivityPackageABinding binding;
    PackageAActivity ac;
    List<PlanList> planList = new ArrayList<>();
    List<GetPackageResponse.DataBean.PackagesBean> packageList = new ArrayList<>();

    List<GetPackageResponse.DataBean.PackagesBean> affilateList = new ArrayList<>();
    List<GetPackageResponse.DataBean.PackagesBean> vyncchainList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_package_a);
        ac = PackageAActivity.this;
        //getPackageByServer();
        clicks();


        binding.linPAffiliate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clickkk","yes");
                binding.ivAffiliate.setImageResource(R.drawable.checkwalleticon);
                binding.ivVynkChain.setImageBitmap(null);
                affilateList();
            }
        });

        binding.linPVynkChain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ivVynkChain.setImageResource(R.drawable.checkwalleticon);
                binding.ivAffiliate.setImageBitmap(null);
                vyncChainList();
            }
        });
        getSettings();


    }


    public void getSettings(){
        planList.clear();
        MainApplication.getApiService().getTransferSettings(Prefes.getAccessToken(PackageAActivity.this)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("settingsresponseM",response.body());
                //{"status":true,"data":{"m_wallet_transfer_enable":true,"v_wallet_transfer_enable":true,"earning_wallet_transfer_enable":true},"message":"success"}
                try {
                    JSONObject respData = new JSONObject(response.body());
                    if (respData.getString("status").equalsIgnoreCase("true")){
                        JSONObject data = respData.getJSONObject("data");
                        String m_wallet_transfer_enable = data.getString("m_wallet_transfer_enable");
                        String v_wallet_transfer_enable = data.getString("v_wallet_transfer_enable");
                        String earning_wallet_transfer_enable = data.getString("earning_wallet_transfer_enable");
                        String affiliate_activation = data.getString("affiliate_activation");
                        String opt_vcash_enable = data.getString("opt_vcash_enable");
                        if (respData.has("message")){
                            Log.d("settingsresponse",respData.getString("message"));
                        }
                            JSONArray plan_list = data.getJSONArray("plan_list");
                            for (int i = 0; i < plan_list.length(); i++) {
                                JSONObject jData = plan_list.getJSONObject(i);
                                String id = jData.getString("id");
                                String title = jData.getString("title");
                                String package_type = jData.getString("package_type");
                                String display = jData.getString("display");
                                String defaultStr = jData.getString("default");
                                planList.add(new PlanList(id, title, package_type, display, defaultStr));
                            }

                        if (Prefes.getisIndian(PackageAActivity.this).equalsIgnoreCase("YES")){
                            binding.linPackageOption.setVisibility(View.GONE);
                        }else {
                            if ((planList != null ? planList.size() : 0) > 0) {
                               /* if (planList.size()>1){
                                    if (planList.get(0).getDisplay().equalsIgnoreCase("1")){
                                        binding.linPAffiliate.setVisibility();
                                    }
                                }*/
                                Log.d("calledd", "001");
                                binding.linPackageOption.setVisibility(View.VISIBLE);
                                for (int i = 0; i < planList.size(); i++) {
                                    if (planList.get(i).getTitle().equalsIgnoreCase("Affiliate")) {
                                        if (planList.get(i).getDisplay().equalsIgnoreCase("1")) {
                                            binding.linPAffiliate.setVisibility(View.VISIBLE);
                                        } else {
                                            binding.linPAffiliate.setVisibility(View.GONE);
                                        }
                                        if (planList.get(i).getDefault().equalsIgnoreCase("1")) {
                                            binding.ivAffiliate.setImageResource(R.drawable.checkwalleticon);
                                        } else {
                                            binding.ivAffiliate.setImageBitmap(null);
                                        }
                                    }
                                    if (planList.get(i).getTitle().equalsIgnoreCase("VYNC Chain")) {
                                        if (planList.get(i).getDisplay().equalsIgnoreCase("1")) {
                                            binding.linPVynkChain.setVisibility(View.VISIBLE);
                                        } else {
                                            binding.linPVynkChain.setVisibility(View.GONE);
                                        }
                                        if (planList.get(i).getDefault().equalsIgnoreCase("1")) {
                                            binding.ivVynkChain.setImageResource(R.drawable.checkwalleticon);
                                        } else {
                                            binding.ivVynkChain.setImageBitmap(null);
                                        }
                                    }
                                }
                            } else {
                                binding.linPackageOption.setVisibility(View.GONE);
                            }
                        }

                    }else {
                        binding.linPackageOption.setVisibility(View.GONE);
                        if (respData.has("message")){
                            Log.d("settingsresponse",respData.getString("message"));
                        }
                    }
                }catch (Exception e){
                    binding.linPackageOption.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("settingsresponseM",t.getMessage()!=null?t.getMessage():"Error");
            }
        });
        getPackageByServer();
    }

    private void clicks() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        //binding.toolbarLayout.toolbarTitlenew.setText(R.string.packagetext);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.purchase);
    }

    public void affilateList(){
        affilateList.clear();
        if(packageList.size()>0){
            for (int i=0;i<packageList.size();i++){
                if (packageList.get(i).getType().equalsIgnoreCase("1")){
                    affilateList.add(packageList.get(i));
                }
            }
            if (affilateList.size()>0){
                binding.viewPagerFrame.setVisibility(View.VISIBLE);
                binding.viewPager.setAdapter(new PackageAdapter(ac, affilateList));
                //Log.e("data", "" + response.body().getData().getPackages());
                binding.tabLayout.setViewPager(binding.viewPager);
                binding.viewPager.startAutoScroll();
                binding.viewPager.setInterval(5000);
                binding.viewPager.setStopScrollWhenTouch(true);
                binding.viewPager.setBorderAnimation(true);
                binding.viewPager.setScrollDurationFactor(8);
            }else {
                binding.viewPagerFrame.setVisibility(View.GONE);
            }
        }
    }


    public void vyncChainList(){
        vyncchainList.clear();
        if(packageList.size()>0){
            for (int i=0;i<packageList.size();i++){
                if (packageList.get(i).getType().equalsIgnoreCase("0")){
                    vyncchainList.add(packageList.get(i));
                }
            }
            if (vyncchainList.size()>0){
                binding.viewPagerFrame.setVisibility(View.VISIBLE);
                binding.viewPager.setAdapter(new PackageAdapter(ac, vyncchainList));
                //Log.e("data", "" + response.body().getData().getPackages());
                binding.tabLayout.setViewPager(binding.viewPager);
                binding.viewPager.startAutoScroll();
                binding.viewPager.setInterval(5000);
                binding.viewPager.setStopScrollWhenTouch(true);
                binding.viewPager.setBorderAnimation(true);
                binding.viewPager.setScrollDurationFactor(8);
            }else {
                binding.viewPagerFrame.setVisibility(View.GONE);
            }
        }
    }


    public void getPackageByServer() {
        packageList.clear();
        Log.d("investmentData","call");
        MainApplication.getApiService().getPackage(Prefes.getAccessToken(ac)).enqueue(new Callback<GetPackageResponse>() {
            @Override
            public void onResponse(Call<GetPackageResponse> call, Response<GetPackageResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        Log.d("investmentDataF",new Gson().toJson(response.body()));
                        Log.d("investmentData",new Gson().toJson(response.body().getData().getPackages()));
                        if ((response.body().getData().getPackages() !=null ? response.body().getData().getPackages().size():0)>0){
                            Log.d("investmentData","ifcalled");
                            binding.tvMessage.setVisibility(View.GONE);
                            packageList.addAll(response.body().getData().getPackages());
                            //MyComment
                            affilateList();
                           /* binding.viewPager.setAdapter(new PackageAdapter(ac, response.body().getData().getPackages()));
                            //Log.e("data", "" + response.body().getData().getPackages());
                            binding.tabLayout.setViewPager(binding.viewPager);
                            binding.viewPager.startAutoScroll();
                            binding.viewPager.setInterval(5000);
                            binding.viewPager.setStopScrollWhenTouch(true);
                            binding.viewPager.setBorderAnimation(true);
                            binding.viewPager.setScrollDurationFactor(8);*/
                        }else {
                            Log.d("investmentData","elsecalled");
                            binding.linPackageOption.setVisibility(View.GONE);
                            binding.tvMessage.setVisibility(View.VISIBLE);
                            binding.tvMessage.setText(response.body().getMessage()!=null?response.body().getMessage():"");
                        }
                    } else {
                        Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPackageResponse> call, Throwable t) {
                Log.d("errorrr",t.getMessage() != null ? t.getMessage() : "Error");
            }

        });

    }

}
