package com.vynkpay.newregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vynkpay.R;
import com.vynkpay.activity.activities.Signupnew;
import com.vynkpay.databinding.ActivityRegister2Binding;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetCountryResponse;
import com.vynkpay.utils.M;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register2Activity extends AppCompatActivity implements View.OnClickListener {

    ActivityRegister2Binding binding;
    Dialog dialog, serverDialog;
    String countryCode = "",which="";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register2);
        serverDialog = M.showDialog(Register2Activity.this, "", false, false);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
        binding.otpButton.setOnClickListener(this);
        binding.linCode.setOnClickListener(this);
        binding.tvCCode.setOnClickListener(this);
        if (getIntent().hasExtra("which")){
            which = getIntent().getStringExtra("which");
        }
        assert which != null;
        if (which.equalsIgnoreCase("customer")){
            binding.linEmail.setVisibility(View.GONE);
            binding.linPhone.setVisibility(View.VISIBLE);
            binding.tvText.setText(getString(R.string.mobile_number));
        }else {
            binding.linEmail.setVisibility(View.VISIBLE);
            binding.linPhone.setVisibility(View.GONE);
            binding.tvText.setText(getString(R.string.email));
        }

       /* if(sp.getString("value", "").equalsIgnoreCase("Global")){
            binding.linEmail.setVisibility(View.VISIBLE);
            binding.linPhone.setVisibility(View.GONE);
        }else {
            binding.linEmail.setVisibility(View.GONE);
            binding.linPhone.setVisibility(View.VISIBLE);
        }*/
    }

    //for international phone will replace with email


    @Override
    public void onClick(View view) {
        if (view == binding.otpButton){
            startActivity(new Intent(Register2Activity.this,Register3Activity.class).putExtra("which",which));
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
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                    countryCode = "+"+data.getStdCode();
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

}