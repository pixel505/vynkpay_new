package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vynkpay.R;
import com.vynkpay.adapter.DirectRefAdapter;
import com.vynkpay.databinding.ActivityDirectRefralsBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.ReferalsResponse;
import com.vynkpay.utils.M;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectRefralsActivity extends AppCompatActivity  {
    ActivityDirectRefralsBinding binding;
    DirectRefralsActivity ac;
    Dialog dialog1;
    DirectRefAdapter adapter;
    List<ReferalsResponse.Datum> dataList, filteredDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_direct_refrals);
        ac = DirectRefralsActivity.this;
        dialog1 = M.showDialog(DirectRefralsActivity.this, "", false, false);
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
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.directre);
        getReferals();


    }

    private void getReferals() {
        dialog1.show();
        MainApplication.getApiService().getReferal(Prefes.getAccessToken(ac)).enqueue(new Callback<ReferalsResponse>() {
            @Override
            public void onResponse(Call<ReferalsResponse> call, Response<ReferalsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dialog1.dismiss();

                    if (response.body().getStatus().equals("true")){
                        binding.noLayout.setVisibility(View.GONE);
                        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                        adapter = new DirectRefAdapter(getApplicationContext(), response.body().getData());
                        binding.directrecycler.setLayoutManager(manager);
                        binding.directrecycler.setAdapter(adapter);

                        dataList=response.body().getData();
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

            @Override
            public void onFailure(Call<ReferalsResponse> call, Throwable t) {
                dialog1.dismiss();
                Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private List<ReferalsResponse.Datum> filter(List<ReferalsResponse.Datum> data, String newText) {
        newText = newText.toLowerCase();
        String text;
        filteredDataList = new ArrayList<>();
        for(ReferalsResponse.Datum dataFromDataList:data){
            text=dataFromDataList.getName().toLowerCase();

            if(text.contains(newText)){
                filteredDataList.add(dataFromDataList);
            }
        }

        return filteredDataList;

    }

}
