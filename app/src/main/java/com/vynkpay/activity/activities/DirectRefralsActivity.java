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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vynkpay.R;
import com.vynkpay.adapter.DirectRefAdapter;
import com.vynkpay.databinding.ActivityDirectRefralsBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.ReferalsResponse;
import com.vynkpay.utils.EndlessOnScrollListener;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectRefralsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityDirectRefralsBinding binding;
    DirectRefralsActivity ac;
    Dialog dialog1;
    DirectRefAdapter adapter;
    List<ReferalsResponse.Datum> filteredDataList;
    String type = "",paidType="";
    String parmType ="";
    Calendar calendar = Calendar.getInstance();
    int dd=0,mm=0,yyyy=0;
    String[] country = {"All", "Paid"};
    String startDate = "",endDate="";
    int perPage =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_direct_refrals);
        ac = DirectRefralsActivity.this;
        dialog1 = M.showDialog(DirectRefralsActivity.this, "", false, false);
        clicks();
    }

    ArrayAdapter aa;
    private void clicks() {
        // toolbar
        yyyy = calendar.get(Calendar.YEAR);
        mm = calendar.get(Calendar.MONTH);
        dd = calendar.get(Calendar.DAY_OF_MONTH);
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDateFilter){
                    binding.tvStartDate.setText("");
                    binding.tvEndDate.setText("");
                    parmType= "";
                    isDateFilter = false;
                    binding.linFilterView.setVisibility(View.GONE);
                    getReferals();
                }else {
                    finish();
                }
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.directre);
        getReferals();
        aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, country);
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


        binding.directrecycler.addOnScrollListener(new EndlessOnScrollListener() {
            @Override
            public void onScrolledToEnd() {
                perPage = perPage+1;
                if (perPage>1){
                    getReferralsFilter(String.valueOf(perPage), false);
                }else {
                    getReferralsFilter(String.valueOf(perPage), true);
                }

            }
        });

    }

    boolean isDateFilter = false;
    void showDateDialog(){
        DatePickerDialog dialog = new DatePickerDialog(DirectRefralsActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    isDateFilter = true;
                    datumList.clear();
                    getReferralsFilter("1", true);
                }
            }
        },yyyy,mm,dd);
        dialog.show();
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
                        datumList.addAll(response.body().getData());
                        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                        adapter = new DirectRefAdapter(getApplicationContext(), datumList);
                        binding.directrecycler.setLayoutManager(manager);
                        binding.directrecycler.setAdapter(adapter);

                       // dataList=response.body().getData();
                        binding.searchView.setQueryHint("Search Here");
                        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }
                            @Override
                            public boolean onQueryTextChange(String newText) {
                                filteredDataList = filter(datumList, newText);

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

    List<ReferalsResponse.Datum> datumList = new ArrayList<>();
    private void getReferralsFilter(String pageNumber, boolean showLoader) {
        if (showLoader){
            dialog1.show();
        }

        MainApplication.getApiService().getReferralsFilter(Prefes.getAccessToken(ac),binding.tvStartDate.getText().toString(),binding.tvEndDate.getText().toString(),parmType, pageNumber).enqueue(new Callback<ReferalsResponse>() {
            @Override
            public void onResponse(Call<ReferalsResponse> call, Response<ReferalsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (showLoader){
                        dialog1.dismiss();
                    }


                    Log.d("getLOGSDSDSDS", "//parmType="+parmType);

                    if (response.body().getStatus().equals("true")){
                        binding.noLayout.setVisibility(View.GONE);

                        if (response.body().getData().size()>0){
                            datumList.addAll(response.body().getData());
                            GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                            adapter = new DirectRefAdapter(getApplicationContext(), datumList);
                            binding.directrecycler.setLayoutManager(manager);
                            binding.directrecycler.setAdapter(adapter);

                            binding.searchView.setQueryHint("Search Here");
                            binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }
                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    filteredDataList = filter(datumList, newText);

                                    adapter.setFilter(filteredDataList);
                                    return false;
                                }
                            });
                        }else {
                            if (Integer.parseInt(pageNumber)==1){
                                binding.noLayout.setVisibility(View.VISIBLE);
                            }
                        }

                    }else {
                        if (Integer.parseInt(pageNumber)==1){
                            binding.noLayout.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ReferalsResponse> call, Throwable t) {
                if (showLoader){
                    dialog1.dismiss();
                }
                Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
       /* if (position > 0) {*/
            paidType = country[position];
            if (paidType.equalsIgnoreCase("Paid")){
                parmType="1";
                binding.tvEndDate.setText("");
                binding.tvStartDate.setText("");
                datumList.clear();
                getReferralsFilter("1", true);
            }else if (paidType.equalsIgnoreCase("UnPaid")){
                binding.tvEndDate.setText("");
                binding.tvStartDate.setText("");
                parmType="0";
                datumList.clear();
                getReferralsFilter("1", true);
            } else {
                getReferals();
            }

      /*  }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(DirectRefralsActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(DirectRefralsActivity.this,DirectRefralsActivity.this::finishAffinity);
        }
    }
}
