package com.vynkpay.newregistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityRegister2Binding;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetCountryResponse;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register2Activity extends AppCompatActivity implements View.OnClickListener, PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityRegister2Binding binding;
    Dialog dialog, serverDialog;
    String countryCode = "",which="",forType="",referalCode="";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register2);
        serverDialog = M.showDialog(Register2Activity.this, "", false, false);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
        binding.otpButton.setOnClickListener(this);
        binding.linCode.setOnClickListener(this);
        binding.tvCCode.setOnClickListener(this);
        getDynamicLink();
        if (getIntent().hasExtra("which")){
            which = getIntent().getStringExtra("which");
        }
        if (getIntent().hasExtra("forType")){
            forType = getIntent().getStringExtra("forType");
        }
        if (forType.equalsIgnoreCase("login")){

        } else {

        }

        if(sp.getString("value", "").equalsIgnoreCase("Global")){
            binding.linEmail.setVisibility(View.VISIBLE);
            binding.linPhone.setVisibility(View.GONE);
            binding.tvText.setText(getString(R.string.email));
            binding.tvTextDetail.setText("Enter your email to verify");
        }else {
            binding.linEmail.setVisibility(View.GONE);
            binding.linPhone.setVisibility(View.VISIBLE);
            binding.tvText.setText(getString(R.string.mobile_number));
            binding.tvTextDetail.setText("Enter your mobile number to verify");
        }

      /*  if (which.equalsIgnoreCase("customer")){
            binding.linEmail.setVisibility(View.GONE);
            binding.linPhone.setVisibility(View.VISIBLE);
            binding.tvText.setText(getString(R.string.mobile_number));
        }else {
            binding.linEmail.setVisibility(View.VISIBLE);
            binding.linPhone.setVisibility(View.GONE);
            binding.tvText.setText(getString(R.string.email));
        }*/

       /* if(sp.getString("value", "").equalsIgnoreCase("Global")){
            binding.linEmail.setVisibility(View.VISIBLE);
            binding.linPhone.setVisibility(View.GONE);
        }else {
            binding.linEmail.setVisibility(View.GONE);
            binding.linPhone.setVisibility(View.VISIBLE);
        }*/
    }

    public void getDynamicLink() {

        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                // Get deep link from result (may be null if no link is found)
                Uri deepLink = null;
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.getLink();
                    String referLink = deepLink.toString();
                    referLink = referLink.substring(referLink.lastIndexOf("=") + 1);
                    referalCode = referLink;
                    sp.edit().putString("referalCodeC",referalCode).apply();
                    //referIdEdt.setText(referalCode);

                    Log.e("linkkk", "" + referalCode);
                    Log.e("linkkk", "" + referLink);
                    Log.e("linkkk", "" + pendingDynamicLinkData);
                    Log.e("linkkk", "" + deepLink);
                } else {
                    if (!sp.getString("referalCodeC","").equalsIgnoreCase("")){
                        referalCode = sp.getString("referalCodeC","");
                        //referIdEdt.setText(referalCode);
                    }
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register2Activity.this, e.toString(), Toast.LENGTH_SHORT).show();
                if (!sp.getString("referalCodeC","").equalsIgnoreCase("")){
                    referalCode = sp.getString("referalCodeC","");
                    //referIdEdt.setText(referalCode);
                }
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(Register2Activity.this).setConnectivityListener(this);
    }

    //for international phone will replace with email

    void onRegister(String countryCode,String uMobile){
        serverDialog.show();
        MainApplication.getApiService().registerCustomer(countryCode,uMobile,sp.getString("referalCodeC","")).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                serverDialog.dismiss();
                Log.d("registerresponse",response.body());
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            if (jsonObject.getString("status").equalsIgnoreCase("true")){
                                Toast.makeText(Register2Activity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                String temp_id = jsonObject.getString("temp_id");
                                String url = jsonObject.getString("url");
                                String type = jsonObject.getString("type");
                                startActivity(new Intent(Register2Activity.this,Register3Activity.class).putExtra("which",which).putExtra("temp_id",temp_id).putExtra("type",type));
                            } else {
                                Toast.makeText(Register2Activity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                serverDialog.dismiss();
                Toast.makeText(Register2Activity.this, t.getMessage()!=null?t.getMessage():"Error", Toast.LENGTH_SHORT).show();
                Log.d("registerresponse",t.getMessage()!=null?t.getMessage():"Error");
            }

        });
    }

    @Override
    public void onClick(View view) {
        if (view == binding.otpButton){
            if (sp.getString("value", "").equalsIgnoreCase("Global")){
                if (TextUtils.isEmpty(binding.etEmailText.getText().toString().trim())){
                    Toast.makeText(Register2Activity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (!M.validateEmail(binding.etEmailText.getText().toString().trim())){
                    Toast.makeText(Register2Activity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                }else {
                    onRegisterWithEmail(binding.etEmailText.getText().toString().trim());
                }
            } else {
                if (TextUtils.isEmpty(binding.etMobileText.getText().toString().trim())){
                    Toast.makeText(Register2Activity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                }  else if (binding.etMobileText.getText().toString().length()<9){
                    Toast.makeText(Register2Activity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                }else {
                    onRegister(binding.tvCCode.getText().toString().replace("+",""),binding.etMobileText.getText().toString());
                    //startActivity(new Intent(Register2Activity.this,Register3Activity.class).putExtra("which",which).putExtra("mobile_number",binding.etMobileText.getText().toString()));
                }
            }

        }

        if (view == binding.linCode){
            serverDialog.show();
            MainApplication.getApiService().getCountry().enqueue(new Callback<GetCountryResponse>() {
                @Override
                public void onResponse(Call<GetCountryResponse> call, Response<GetCountryResponse> response) {
                    if (response.isSuccessful()) {
                        serverDialog.dismiss();
                        dialog = new Dialog(Register2Activity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        if (dialog.getWindow()!=null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setContentView(R.layout.country_dialog_new);
                        SearchView searchView = dialog.findViewById(R.id.searchView);
                        searchView.setIconified(false);
                        RecyclerView countryRecycler = dialog.findViewById(R.id.countryRecycler);
                        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                        CountryAdapter adapter = new CountryAdapter(getApplicationContext(), response.body().getData());
                        countryRecycler.setLayoutManager(manager);
                        countryRecycler.setAdapter(adapter);


                        //searchView functionality
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                adapter.filter(newText);
                                return false;
                            }
                        });

                        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                            @Override
                            public boolean onClose() {
                                if (!searchView.isFocused()) {
                                    dialog.dismiss();
                                }
                                return false;
                            }
                        });

                        dialog.show();

                    }
                }

                @Override
                public void onFailure(Call<GetCountryResponse> call, Throwable t) {
                    serverDialog.dismiss();
                    Toast.makeText(Register2Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (view == binding.tvCCode){
            serverDialog.show();
            MainApplication.getApiService().getCountry().enqueue(new Callback<GetCountryResponse>() {

                @Override
                public void onResponse(@NotNull Call<GetCountryResponse> call, @NotNull Response<GetCountryResponse> response) {

                    if (response.isSuccessful()) {
                        Log.d("cResponsee",new Gson().toJson(response.body()));
                        serverDialog.dismiss();
                        dialog = new Dialog(Register2Activity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        if (dialog.getWindow()!=null) {
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        }
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setContentView(R.layout.country_dialog_new);
                        SearchView searchView = dialog.findViewById(R.id.searchView);
                        searchView.setIconified(false);
                        RecyclerView countryRecycler = dialog.findViewById(R.id.countryRecycler);
                        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                        CountryAdapter adapter = new CountryAdapter(getApplicationContext(), response.body().getData());
                        countryRecycler.setLayoutManager(manager);
                        countryRecycler.setAdapter(adapter);


                        //searchView functionality
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                adapter.filter(newText);
                                return false;
                            }
                        });

                        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                            @Override
                            public boolean onClose() {
                                if (!searchView.isFocused()) {
                                    dialog.dismiss();
                                }
                                return false;
                            }
                        });

                        dialog.show();

                    }
                }

                @Override
                public void onFailure(Call<GetCountryResponse> call, Throwable t) {
                    serverDialog.dismiss();
                    Toast.makeText(Register2Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(Register2Activity.this,Register2Activity.this::finishAffinity);
        }
    }

    private class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {
        Context context;
        List<GetCountryResponse.Datum> mList;
        List<GetCountryResponse.Datum> searchedItemModelArrayList;

        public CountryAdapter(Context applicationContext, List<GetCountryResponse.Datum> data) {
            this.context = applicationContext;
            this.mList = data;
            this.searchedItemModelArrayList = new ArrayList<>();
            this.searchedItemModelArrayList.addAll(mList);
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_list, parent, false);

            return new CountryAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            GetCountryResponse.Datum data = mList.get(position);
            holder.countryText.setText(data.getText());
            holder.countryText.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    binding.tvCCode.setText("+"+data.getStdCode());
                    countryCode = data.getStdCode();
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
                for (GetCountryResponse.Datum wp : searchedItemModelArrayList) {
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

    void onRegisterWithEmail(String email){
        serverDialog.show();
        MainApplication.getApiService().registerByEmail(email,sp.getString("referalCodeC","")).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //{"status":true,"success":true,
                // "url":"https:\/\/www.mlm.pixelsoftwares.com\/vynkpay\/account\/verification","type":"register",
                // "temp_id":"fe24bb7cf8642a9c86bb04c1da46389337b3f979e0a80e838e78067a22354f738dccbd7f7f71e35cbeb199c5edd1a70abb20bdaef5f17310dc51e363bce0de5221e4+Ebyc7Ze7bYJvD7S2bC67+ENFRb1xvrn0Z0z5lQ=",
                // "message":"OTP send Successfully. Please Check your Email for OTP."}
                serverDialog.dismiss();
                if (response.isSuccessful() && response.body()!=null){
                    try {
                        Log.d("emailregister",new Gson().toJson(response.body()));
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getString("status").equalsIgnoreCase("true")){
                            Toast.makeText(Register2Activity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            String temp_id = jsonObject.getString("temp_id");
                            String url = jsonObject.getString("url");
                            String type = jsonObject.getString("type");
                            startActivity(new Intent(Register2Activity.this,Register3Activity.class).putExtra("which",which).putExtra("temp_id",temp_id).putExtra("type",type));
                        } else {
                            Toast.makeText(Register2Activity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                serverDialog.dismiss();
                Log.d("signuprespo",t.getMessage()!=null?t.getMessage():"Error");
            }
        });
    }

}