package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vynkpay.R;
import com.vynkpay.adapter.InvoiceAdapter;
import com.vynkpay.databinding.ActivityInvoiceBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetInvoiceResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceActivity extends AppCompatActivity {
    ActivityInvoiceBinding binding;
    InvoiceActivity ac;
    List<GetInvoiceResponse.Data.Listing> dataList, filteredDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invoice);
        ac = InvoiceActivity.this;
        click();
    }

    private void click() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.invoice);
        getInvoice();

    }

    private void getInvoice() {
        MainApplication.getApiService().getInvoice(Prefes.getAccessToken(ac)).enqueue(new Callback<GetInvoiceResponse>() {
            @Override
            public void onResponse(Call<GetInvoiceResponse> call, Response<GetInvoiceResponse> response) {
                if (response.isSuccessful() && response.body()!= null) {
                    if (response.body().getStatus().equals("true")) {
                        if(!response.body().getData().getListing().isEmpty()){
                            GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                            InvoiceAdapter adapter = new InvoiceAdapter(getApplicationContext(), response.body().getData().getListing());
                            binding.directrecycler.setLayoutManager(manager);
                            binding.directrecycler.setAdapter(adapter);
                            dataList=response.body().getData().getListing();

                            binding.searchView.setQueryHint("Search Invoice");
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

                        }
                        else {
                            Toast.makeText(ac, "No Data Found", Toast.LENGTH_LONG).show();

                        }

                    } else if (response.body().getMessage().equals("false")) {
                        Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetInvoiceResponse> call, Throwable t) {
                Toast.makeText(ac, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private List<GetInvoiceResponse.Data.Listing> filter(List<GetInvoiceResponse.Data.Listing> data, String newText) {
        newText=newText.toLowerCase();
        String text;
        filteredDataList=new ArrayList<>();
        for(GetInvoiceResponse.Data.Listing dataFromDataList:data){
            text=dataFromDataList.getName().toLowerCase();

            if(text.contains(newText)){
                filteredDataList.add(dataFromDataList);
            }
        }

        return filteredDataList;

    }
}