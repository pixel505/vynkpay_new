package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunitySummaryActivity extends AppCompatActivity {
    ActivityCommunitySummaryBinding binding;
    CommunitySummaryActivity ac;
    Dialog dialog1;

    CouumunityAdapter adapter;
    List<TeamSummaryResponse.Datum> dataList, filteredDataList;

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
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.communitynew);

        getTeam();


    }

    private void getTeam() {
        dialog1.show();
        MainApplication.getApiService().getTeam(Prefes.getAccessToken(ac)).enqueue(new Callback<TeamSummaryResponse>() {
            @Override
            public void onResponse(Call<TeamSummaryResponse> call, Response<TeamSummaryResponse> response) {

                dialog1.dismiss();
                if (response.isSuccessful()) {
                    if (response.body()!=null){
                        if (response.body().getStatus().equals("true")){
                            Log.d("commmmynn", new Gson().toJson(response.body().getData()));
                            binding.noLayout.setVisibility(View.GONE);
                            GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                            CouumunityAdapter adapter = new CouumunityAdapter(getApplicationContext(), response.body().getData());
                            binding.commurecycler.setLayoutManager(manager);
                            binding.commurecycler.setAdapter(adapter);

                            dataList = response.body().getData();
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
                            binding.noLayout.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<TeamSummaryResponse> call, Throwable t) {
                dialog1.dismiss();
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
}
