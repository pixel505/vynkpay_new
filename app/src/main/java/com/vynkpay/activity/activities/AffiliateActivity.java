package com.vynkpay.activity.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vynkpay.R;
import com.vynkpay.adapter.PackageAdapter;
import com.vynkpay.custom.NormalBoldTextView;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityAffiliateBinding;
import com.vynkpay.fragment.MCashWalletFragment;
import com.vynkpay.models.PlanList;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetPackageResponse;
import com.vynkpay.retrofit.model.GetUserPackageResponse;
import com.vynkpay.retrofit.model.GetUserResponse;
import com.vynkpay.retrofit.model.PaidItemResponse;
import com.vynkpay.retrofit.model.SendWaletOtp;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AffiliateActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    ActivityAffiliateBinding binding;
    AffiliateActivity ac;
    Dialog dialog,dialog1;
    RecyclerView countryRecycler;
    UsrAdapter adapter;
    String userId="",orgId="";
    List<GetUserPackageResponse.Datum> packageList = new ArrayList<>();
    List<GetUserPackageResponse.Datum> affilateList = new ArrayList<>();
    List<GetUserPackageResponse.Datum> vyncchainList = new ArrayList<>();
    List<PlanList> planList = new ArrayList<>();
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_affiliate);
        ac = AffiliateActivity.this;
        dialog1 = M.showDialog(ac, "", false, false);
        binding.packageList.setLayoutManager(M.horizontalRecyclerView(AffiliateActivity.this));
        getMCashTransaction();

      /*binding.radioGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=binding.radioGrp.getCheckedRadioButtonId();
                radioButton=(RadioButton)findViewById(selectedId);
                Toast.makeText(AffiliateActivity.this,radioButton.getText(),Toast.LENGTH_SHORT).show();
            }
        });*/

        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //startActivity(new Intent(ac,WalletNewActivity.class));
               finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText("Affiliate Activation");

        clicks();
        getSettings();
    }

    public void getSettings(){
        planList.clear();
        MainApplication.getApiService().getTransferSettings(Prefes.getAccessToken(AffiliateActivity.this)).enqueue(new Callback<String>() {
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
                        if (affiliate_activation.equalsIgnoreCase("true")){

                        } else {

                        }
                        if (respData.has("message")){
                            Log.d("settingsresponse",respData.getString("message"));
                        }
                        JSONArray plan_list = data.getJSONArray("plan_list");
                        for (int i=0;i<plan_list.length();i++){
                            JSONObject jData = plan_list.getJSONObject(i);
                            String id = jData.getString("id");
                            String title = jData.getString("title");
                            String package_type = jData.getString("package_type");
                            String display = jData.getString("display");
                            String defaultStr = jData.getString("default");
                            planList.add(new PlanList(id,title,package_type,display,defaultStr));
                        }

                        if ((planList != null ? planList.size() : 0) > 0) {
                            //binding.linPackageOption.setVisibility(View.VISIBLE);
                            for (int i = 0; i < planList.size(); i++) {
                                if (planList.get(i).getTitle().equalsIgnoreCase("Affiliate")) {
                                    if (planList.get(i).getDisplay().equalsIgnoreCase("1")) {
                                        //binding.rAffiliate.setVisibility(View.VISIBLE);
                                    } else {
                                        //binding.rAffiliate.setVisibility(View.GONE);
                                    }
                                    if (planList.get(i).getDefault().equalsIgnoreCase("1")) {
                                        //binding.rAffiliate.setChecked(true);
                                    } else {
                                       // binding.rAffiliate.setChecked(false);
                                    }
                                }
                                if (planList.get(i).getTitle().equalsIgnoreCase("VYNC Chain")) {
                                    if (planList.get(i).getDisplay().equalsIgnoreCase("1")) {
                                        //binding.rVyncChain.setVisibility(View.VISIBLE);
                                    } else {
                                        //binding.rVyncChain.setVisibility(View.GONE);
                                    }
                                    if (planList.get(i).getDefault().equalsIgnoreCase("1")) {
                                        //binding.rVyncChain.setChecked(true);
                                    } else {
                                       // binding.rVyncChain.setChecked(false);
                                    }
                                }
                            }
                        } else {
                            //binding.linPackageOption.setVisibility(View.GONE);
                        }

                        /*if (Prefes.getisIndian(AffiliateActivity.this).equalsIgnoreCase("YES")){
                            binding.linPackageOption.setVisibility(View.GONE);
                        } else {
                            if ((planList != null ? planList.size() : 0) > 0) {
                                binding.linPackageOption.setVisibility(View.VISIBLE);
                                for (int i = 0; i < planList.size(); i++) {
                                    if (planList.get(i).getTitle().equalsIgnoreCase("Affiliate")) {
                                        if (planList.get(i).getDisplay().equalsIgnoreCase("1")) {
                                            binding.rAffiliate.setVisibility(View.VISIBLE);
                                        } else {
                                            binding.rAffiliate.setVisibility(View.GONE);
                                        }
                                        if (planList.get(i).getDefault().equalsIgnoreCase("1")) {
                                            binding.rAffiliate.setChecked(true);
                                        } else {
                                            binding.rAffiliate.setChecked(false);
                                        }
                                    }
                                    if (planList.get(i).getTitle().equalsIgnoreCase("VYNC Chain")) {
                                        if (planList.get(i).getDisplay().equalsIgnoreCase("1")) {
                                            binding.rVyncChain.setVisibility(View.VISIBLE);
                                        } else {
                                            binding.rVyncChain.setVisibility(View.GONE);
                                        }
                                        if (planList.get(i).getDefault().equalsIgnoreCase("1")) {
                                            binding.rVyncChain.setChecked(true);
                                        } else {
                                            binding.rVyncChain.setChecked(false);
                                        }
                                    }
                                }
                            } else {
                                binding.linPackageOption.setVisibility(View.GONE);
                            }
                        }*/

                    }else {
                        //binding.linPackageOption.setVisibility(View.GONE);
                        if (respData.has("message")){
                            Log.d("settingsresponse",respData.getString("message"));
                        }
                    }
                }catch (Exception e){
                    //binding.linPackageOption.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("settingsresponseM",t.getMessage()!=null?t.getMessage():"Error");
            }
        });
    }


    private void getMCashTransaction(){

        dialog1.show();
            MCashWalletFragment.walletTransactionsModelArrayList.clear();
            ApiCalls.getMcashTransactions(ac, Prefes.getAccessToken(ac), new VolleyResponse() {
                @Override
                public void onResult(String result, String status, String message) {
                    //  Log.d("tmcashtrrr", result+"//");
                    dialog1.dismiss();
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
                    dialog1.dismiss();
                }
            });
        }


        private void clicks() {

           /* binding.radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int selectedId=group.getCheckedRadioButtonId();
                    radioButton=(RadioButton)findViewById(selectedId);
                    if (radioButton.getText().toString().equalsIgnoreCase("Affiliate")){
                        affilateList();
                    }else  if (radioButton.getText().toString().equalsIgnoreCase("VYNC Chain")){
                        vyncChainList();
                    }
                }
            });*/

        binding.searchUserEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.searchUserEdt.setText("");
                dialog = new Dialog(ac);
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
                orgId = packageList.get(binding.viewPackageList.getCurrentItem()).getId();
                Log.d("packageIIDDDLOGGG", packageList.get(binding.viewPackageList.getCurrentItem()).getId()+"//");

                if (binding.searchUserEdt.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Select User", Toast.LENGTH_SHORT).show();
                }

               /* else if(binding.packageText.getText().toString().isEmpty()){
                    Toast.makeText(ac, "No Package Found Select another User", Toast.LENGTH_SHORT).show();

                }*/
                else if(TextUtils.isEmpty(orgId)){
                    Toast.makeText(ac, "No Package Found Select another User", Toast.LENGTH_SHORT).show();
                } else{
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


    public void affilateList(){
        affilateList.clear();
        if(packageList.size()>0){
            for (int i=0;i<packageList.size();i++){
                if (packageList.get(i).getType().equalsIgnoreCase("1")){
                    affilateList.add(packageList.get(i));
                    Log.d("afflilateLOGGGDD", new Gson().toJson(affilateList));
                }
            }
            if (affilateList.size()>0){
                binding.packageList.setAdapter(new PackageAdapter(affilateList));
                binding.viewPackageList.setAdapter(new PackageViewAdapter(AffiliateActivity.this,affilateList));
                binding.tabLayout.setViewPager(binding.viewPackageList);
            } else {

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
                orgId=vyncchainList.get(0).getId();
                binding.packageList.setAdapter(new PackageAdapter(vyncchainList));
                binding.viewPackageList.setAdapter(new PackageViewAdapter(AffiliateActivity.this,vyncchainList));
                binding.tabLayout.setViewPager(binding.viewPackageList);
            }else {

            }
        }
    }


    private void getPackage() {
        packageList.clear();
        MainApplication.getApiService().getUserPackage(Prefes.getAccessToken(ac), userId).enqueue(new Callback<GetUserPackageResponse>() {
            @Override
            public void onResponse(Call<GetUserPackageResponse> call, Response<GetUserPackageResponse> response) {
                if (response.isSuccessful() && response != null) {
                    Log.d("packageresp",new Gson().toJson(response.body()));
                    if (response.body().getStatus()) {
                        binding.packageTextt.setVisibility(View.VISIBLE);
                        binding.packageCard.setVisibility(View.GONE);
                        binding.linPackage.setVisibility(View.VISIBLE);
                        if ((response.body().getData() !=null ? response.body().getData().size():0)>0){
                            packageList.addAll(response.body().getData());

                            vyncChainList();

                            //MyChanges
                            affilateList();

                            binding.packageText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().get(0).getTotalAmount());
                            binding.amountText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().get(0).getPrice());
                            binding.pointsText.setText("("+response.body().getData().get(0).getPoints()+" "+"points"+")");

                        }else {
                            //binding.linPackageOption.setVisibility(View.GONE);
                        }

                        // binding.packageList.setAdapter(new PackageAdapter(packageList));

                         //orgId=response.body().getData().get(0).getId();
                    } else {
                         binding.packageCard.setVisibility(View.GONE);
                        binding.packageTextt.setVisibility(View.GONE);
                        binding.linPackage.setVisibility(View.GONE);

                    }
                } else {
                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetUserPackageResponse> call, Throwable t) {
                Toast.makeText(ac, t.getMessage()!=null?t.getMessage():"Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(AffiliateActivity.this,AffiliateActivity.this::finishAffinity);
        }
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

    public class PackageViewAdapter extends PagerAdapter {
        //boolean[] clicked = new boolean[0];
        Context context;
        LayoutInflater mLayoutInflater;
        List<GetUserPackageResponse.Datum> packageList;

        public PackageViewAdapter(Context context, List<GetUserPackageResponse.Datum> packageList){
            this.context=context;
            this.packageList=packageList;
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //clicked = new boolean[packageList.size()];
        }

        @Override
        public int getCount() {
            return packageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.custom_packagelist, container, false);
            NormalTextView packageText = itemView.findViewById(R.id.packageText);
            NormalTextView amountText = itemView.findViewById(R.id.amountText);
            NormalTextView pointsText = itemView.findViewById(R.id.pointsText);
            ImageView ivCheck = itemView.findViewById(R.id.ivCheck);
            try {

                packageText.setText(Functions.CURRENCY_SYMBOL+packageList.get(position).getTotalAmount());
                amountText.setText(Functions.CURRENCY_SYMBOL+packageList.get(position).getPrice());
                pointsText.setText("("+packageList.get(position).getPoints()+" "+"points"+")");
                if (packageList.get(position).getType().equalsIgnoreCase("1")){
                    pointsText.setVisibility(View.VISIBLE);
                    pointsText.setText("Vcash 200% and Global Royality 100%");
                } else {
                    pointsText.setVisibility(View.VISIBLE);
                    pointsText.setText("Weekly "+Functions.CURRENCY_SYMBOL+packageList.get(position).getWeekly_amount());
                }

                itemView.setOnClickListener(view -> {
                    orgId = packageList.get(position).getId();
                    Log.d("selecrdslkjdljld", packageList.get(position).getId());
                });

            }catch (Exception e){
                e.printStackTrace();
            }
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout)object);
        }

    }

    class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.PHolder>{

        List<GetUserPackageResponse.Datum> packageList;

        public PackageAdapter(List<GetUserPackageResponse.Datum> packageList) {
            this.packageList = packageList;
        }

        @NonNull
        @Override
        public PHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(AffiliateActivity.this).inflate(R.layout.custom_packagelist,parent,false);
            return new PHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PHolder holder, int position) {
            try {
                holder.packageText.setText(Functions.CURRENCY_SYMBOL+packageList.get(position).getTotalAmount());
                holder.amountText.setText(Functions.CURRENCY_SYMBOL+packageList.get(position).getPrice());
                holder.pointsText.setText("("+packageList.get(position).getPoints()+" "+"points"+")");
                if (packageList.get(position).getType().equalsIgnoreCase("1")){
                    holder.pointsText.setVisibility(View.VISIBLE);
                    holder.pointsText.setText("Cashback");
                }else {
                    holder.pointsText.setVisibility(View.VISIBLE);
                    holder.pointsText.setText("Weekly "+Functions.CURRENCY_SYMBOL+packageList.get(position).getWeekly_amount());
                }
                holder.itemView.setOnClickListener(view -> {
                    orgId = packageList.get(position).getId();
                    Log.d("selecrdslkjdljld", packageList.get(position).getId());
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return packageList!=null?packageList.size():0;
        }

        class PHolder extends RecyclerView.ViewHolder{

            NormalTextView packageText;
            NormalTextView amountText;
            NormalTextView pointsText;
            public PHolder(@NonNull View itemView) {
                super(itemView);
                packageText = itemView.findViewById(R.id.packageText);
                amountText = itemView.findViewById(R.id.amountText);
                pointsText = itemView.findViewById(R.id.pointsText);
            }
        }

    }


    @Override
    protected void onResume() {
        if(dialog1!=null){
            dialog1.dismiss();
        }
        super.onResume();
        MySingleton.getInstance(AffiliateActivity.this).setConnectivityListener(this);
    }

}