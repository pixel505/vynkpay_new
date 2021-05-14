package com.vynkpay.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityWeeklyBinding;
import com.vynkpay.databinding.RoiDetailLayoutBinding;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.ReferalBonusResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeeklyDetailActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityWeeklyBinding binding;
    WeeklyDetailActivity ac;
    String dateString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_weekly);
        ac = WeeklyDetailActivity.this;


        clicks();
    }

    void clicks(){
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.weeklybonusDetail);

        Intent intent = getIntent();
        if (intent!=null){
            dateString=intent.getStringExtra("dateString");
            Log.d("checkdateLOG",dateString+"//");
            getWeeklyBonusDetail(dateString);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(WeeklyDetailActivity.this).setConnectivityListener(this);
    }

    void getWeeklyBonusDetail(String date){
        binding.progressFrame.setVisibility(View.VISIBLE);
       /* ApiCalls.getROIDetails(WeeklyDetailActivity.this, Prefes.getAccessToken(WeeklyDetailActivity.this), date, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.optBoolean("status")){
                        Log.d("sdjhjdassad", jsonObject.toString()+"//");
                    }else {
                        binding.noLayout.setVisibility(View.VISIBLE);
                        binding.noMessageTV.setText(message+"");
                        Toast.makeText(ac, message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });*/

        MainApplication.getApiService().getWeeklyBonusDetail(Prefes.getAccessToken(ac), date).enqueue(new Callback<ReferalBonusResponse>() {
            @Override
            public void onResponse(Call<ReferalBonusResponse> call, Response<ReferalBonusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("bonusLOGG", new Gson().toJson(response.body().getData()));

                    binding.progressFrame.setVisibility(View.GONE);
                    if (response.body().getStatus().equals("true")) {
                        if (response.body().getData().size()>0){
                            binding.noLayout.setVisibility(View.GONE);
                            Log.d("bonusLOGG", new Gson().toJson(response.body().getData()));

                            GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                            WeeklyBonusAdapter adapter = new WeeklyBonusAdapter(getApplicationContext(), response.body().getData());
                            binding.referalbonusRecycler.setLayoutManager(manager);
                            binding.referalbonusRecycler.setAdapter(adapter);
                        }else {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.noMessageTV.setText(response.body().getMessage()+"");
                            Toast.makeText(ac, "No Data Found", Toast.LENGTH_LONG).show();
                        }

                    } else if (response.body().getMessage().equals("false")) {
                        binding.noLayout.setVisibility(View.VISIBLE);
                        binding.noMessageTV.setText(response.body().getMessage()+"");
                        Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else {
                    binding.progressFrame.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ReferalBonusResponse> call, Throwable t) {
                Toast.makeText(ac, t.getMessage(), Toast.LENGTH_LONG).show();
                binding.progressFrame.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(WeeklyDetailActivity.this, WeeklyDetailActivity.this::finishAffinity);
        }
    }


    public static class WeeklyBonusAdapter extends RecyclerView.Adapter<WeeklyBonusAdapter.ViewHolder>  {
        Context context;
        List<ReferalBonusResponse.Datum> listdata;

        public WeeklyBonusAdapter(Context ac, List<ReferalBonusResponse.Datum> data) {
            this.context = ac;
            this.listdata = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RoiDetailLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.roi_detail_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(binding);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ReferalBonusResponse.Datum  myListData = listdata.get(position);
            holder.binding.nameText.setText(myListData.getName());
            holder.binding.nameryttext.setText("("+myListData.getUsername()+")");
            holder.binding.paidDateText.setText(Functions.CURRENCY_SYMBOL+" "+myListData.getAmountOf());
            holder.binding.bonusAmountTV.setText(Functions.CURRENCY_SYMBOL+" "+myListData.getPAmount());
            if(myListData.getStatus().equals("0")){
                holder.binding.purchaseamountText.setText("Confirmed");
            }

            else {
                holder.binding.purchaseamountText.setText("On Hold");

            }

            try {
                DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                Date d = f.parse(myListData.getCreatedDate());
                DateFormat date = new SimpleDateFormat("dd-MM-yyy",Locale.getDefault());
                holder.binding.datetext.setText(date.format(d));
                System.out.println("Date: " + date.format(d));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        @Override
        public int getItemCount() {
            return listdata.size();
        }
        static class ViewHolder extends RecyclerView.ViewHolder {
            final RoiDetailLayoutBinding binding;
            ViewHolder(final RoiDetailLayoutBinding itemBinding) {
                super(itemBinding.getRoot());
                this.binding = itemBinding;
            }
        }
    }



}