package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vynkpay.utils.EndlessOnScrollListener;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.adapter.CouumunityAdapter;
import com.vynkpay.adapter.DirectRefAdapter;
import com.vynkpay.databinding.ActivityCommunitySummaryBinding;
import com.vynkpay.models.DirectRefModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.ReferalsResponse;
import com.vynkpay.retrofit.model.TeamSummaryResponse;
import com.vynkpay.utils.M;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunitySummaryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ActivityCommunitySummaryBinding binding;
    CommunitySummaryActivity ac;
    Dialog dialog1;

    List<TeamSummaryResponse.Datum> dataList = new ArrayList<>(), filteredDataList;
    int perPage = 1;
    CouumunityAdapter adapter;
    boolean isLoading = true;
    String type = "",paidType="";
    Calendar calendar = Calendar.getInstance();
    int dd=0,mm=0,yyyy=0;
    String[] country = {"Select", "Paid", "UnPaid"};
    String startDate = "",endDate="";
    String parmType ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_community_summary);
        ac = CommunitySummaryActivity.this;
        dialog1 = M.showDialog(CommunitySummaryActivity.this, "", false, false);

        clicks();
    }


    private void clicks() {
        // toolbar
        yyyy = calendar.get(Calendar.YEAR);
        mm = calendar.get(Calendar.MONTH);
        dd = calendar.get(Calendar.DAY_OF_MONTH);
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.communitynew);
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
        binding.commurecycler.setLayoutManager(manager);

        adapter = new CouumunityAdapter(getApplicationContext(), dataList);
        binding.commurecycler.setAdapter(adapter);
        getTeamFilter(String.valueOf(perPage));
        binding.commurecycler.addOnScrollListener(new EndlessOnScrollListener() {
            @Override
            public void onScrolledToEnd() {
                perPage = perPage+1;
                if (isLoading) {
                    isLoading = false;
                    getTeamFilter(String.valueOf(perPage));
                    //getTeam(String.valueOf(perPage));
                }
            }
        });


        ArrayAdapter aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinPaidStatus.setAdapter(aa);
        binding.spinPaidStatus.setOnItemSelectedListener(this);

        binding.linFilter.setOnClickListener(view -> {
            if (binding.linFilterView.getVisibility()==View.GONE){
                binding.linFilterView.setVisibility(View.VISIBLE);
            }else {
                binding.linFilterView.setVisibility(View.GONE);
            }
        });

        binding.tvStartDate.setOnClickListener(view -> {
            type = "start";
            showDateDialog();
        });
        binding.tvEndDate.setOnClickListener(view -> {
            type = "end";
            showDateDialog();
        });


    }

    void showDateDialog(){
        DatePickerDialog dialog = new DatePickerDialog(CommunitySummaryActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar cal = Calendar.getInstance();
                cal.set(i,i1,i2);
                String date =  new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
                if (type.equalsIgnoreCase("start")) {
                    binding.tvStartDate.setText(date);
                }else {
                    binding.tvEndDate.setText(date);
                    parmType= "";
                    perPage = 1;
                    binding.commurecycler.setAdapter(null);
                    dataList.clear();
                    adapter = new CouumunityAdapter(CommunitySummaryActivity.this,dataList);
                    binding.commurecycler.setAdapter(adapter);
                    getTeamFilter(String.valueOf(perPage));
                }
            }
        },yyyy,mm,dd);
        dialog.show();
    }

    private void getTeam(String per_page) {
        int previousPage = dataList.size();
        if (previousPage == 0) {
            dialog1.show();
        }
        MainApplication.getApiService().getTeam(Prefes.getAccessToken(ac),per_page).enqueue(new Callback<TeamSummaryResponse>() {
            @Override
            public void onResponse(Call<TeamSummaryResponse> call, Response<TeamSummaryResponse> response) {

                dialog1.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().getStatus().equals("true")){
                            Log.d("commmmynn", new Gson().toJson(response.body()));
                            isLoading = true;
                            Log.d("commmmynn", new Gson().toJson(response.body().getData()));
                            binding.noLayout.setVisibility(View.GONE);
                            dataList.addAll(response.body().getData());
                            Log.d("commmmynnList",dataList.size()+"//p"+per_page);
                            adapter.notifyItemRangeInserted(previousPage,dataList.size());
                            binding.searchView.setQueryHint("Search Here");
                            binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    filteredDataList = filter(dataList, newText);

                                    adapter.setFilter(filteredDataList);
                                    return false;
                                }
                            });
                        }else {
                            isLoading = false;
                            if (dataList.size()>0) {
                                binding.noLayout.setVisibility(View.GONE);
                            }else {
                                binding.noLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<TeamSummaryResponse> call, Throwable t) {
                dialog1.dismiss();
                isLoading = true;
                Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private List<TeamSummaryResponse.Datum> filter(List<TeamSummaryResponse.Datum> data, String newText) {
        newText = newText.toLowerCase();
        String name, username;
        filteredDataList = new ArrayList<>();
        for (TeamSummaryResponse.Datum dataFromDataList : data) {
            name = dataFromDataList.getName().toLowerCase();
            username = dataFromDataList.getUsername().toLowerCase();

            if (name.contains(newText) || username.contains(newText)) {
                filteredDataList.add(dataFromDataList);
            }
        }

        return filteredDataList;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position>0) {
            paidType = country[position];
            if (paidType.equalsIgnoreCase("Paid")) {
                perPage = 1;
                parmType = "1";
                binding.tvEndDate.setText("");
                binding.tvStartDate.setText("");
                binding.commurecycler.setAdapter(null);
                dataList.clear();
                adapter = new CouumunityAdapter(CommunitySummaryActivity.this, dataList);
                binding.commurecycler.setAdapter(adapter);
                getTeamFilter(String.valueOf(perPage));
            } else if (paidType.equalsIgnoreCase("UnPaid")) {
                perPage = 1;
                binding.tvEndDate.setText("");
                binding.tvStartDate.setText("");
                parmType = "0";
                binding.commurecycler.setAdapter(null);
                dataList.clear();
                adapter = new CouumunityAdapter(CommunitySummaryActivity.this, dataList);
                binding.commurecycler.setAdapter(adapter);
                getTeamFilter(String.valueOf(perPage));
            } /*else if (paidType.equalsIgnoreCase("All")) {
                perPage = 1;
                binding.commurecycler.setAdapter(null);
                dataList.clear();
                adapter = new CouumunityAdapter(CommunitySummaryActivity.this, dataList);
                binding.commurecycler.setAdapter(adapter);
                getTeamFilter(String.valueOf(perPage));
            }*/
        }

        /*}*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    private void getTeamFilter(String per_page) {
        int previousPage = dataList.size();
        if (previousPage == 0) {
            dialog1.show();
        }
        MainApplication.getApiService().getTeamFilter(Prefes.getAccessToken(ac),binding.tvStartDate.getText().toString(),binding.tvEndDate.getText().toString(),parmType,per_page).enqueue(new Callback<TeamSummaryResponse>() {
            @Override
            public void onResponse(Call<TeamSummaryResponse> call, Response<TeamSummaryResponse> response) {

                dialog1.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().getStatus().equals("true")){
                            Log.d("commmmynn", new Gson().toJson(response.body()));
                            isLoading = true;
                            Log.d("commmmynn", new Gson().toJson(response.body().getData()));
                            binding.noLayout.setVisibility(View.GONE);
                            dataList.addAll(response.body().getData());
                            Log.d("commmmynnList",dataList.size()+"//p"+per_page);
                            adapter.notifyItemRangeInserted(previousPage,dataList.size());
                            binding.searchView.setQueryHint("Search Here");
                            binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    filteredDataList = filter(dataList, newText);

                                    adapter.setFilter(filteredDataList);
                                    return false;
                                }
                            });
                        }else {
                            isLoading = false;
                            if (dataList.size()>0) {
                                binding.noLayout.setVisibility(View.GONE);
                            }else {
                                binding.noLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<TeamSummaryResponse> call, Throwable t) {
                dialog1.dismiss();
                isLoading = true;
                Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
