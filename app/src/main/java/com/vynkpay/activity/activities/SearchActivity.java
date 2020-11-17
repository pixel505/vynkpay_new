package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.recharge.broadband.activity.BroadbandActivity;
import com.vynkpay.activity.recharge.datacard.activity.DataCardActivity;
import com.vynkpay.activity.recharge.dth.activity.DthActivity;
import com.vynkpay.activity.recharge.electricity.activity.ElectricityActivity;
import com.vynkpay.activity.recharge.gas.GasActivity;
import com.vynkpay.activity.recharge.insurance.InsuranceActivity;
import com.vynkpay.activity.recharge.landline.activity.LandLineActivity;
import com.vynkpay.activity.recharge.mobile.activity.PostPaidActivity;
import com.vynkpay.activity.recharge.mobile.activity.PrepaidActivity;
import com.vynkpay.activity.recharge.water.WaterActivity;
import com.vynkpay.adapter.SearchItemAdapter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.SearchItemModel;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.noDataIV)
    ImageView noDataIV;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;

    ArrayList<SearchItemModel> searchItemModelArrayList=new ArrayList<>();
    SearchItemAdapter searchItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_rcg);
        ButterKnife.bind(SearchActivity.this);
        dev();
    }

    private void dev() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbarTitle.setText(getString(R.string.app_name));
        toolbarTitle.setTextColor(ContextCompat.getColor(SearchActivity.this, R.color.white));

        searchView.setIconified(false);

        searchItemModelArrayList.clear();
        searchItemModelArrayList.add(new SearchItemModel("Mobile Prepaid", R.drawable.mobile_icon, PrepaidActivity.class));
        searchItemModelArrayList.add(new SearchItemModel("Mobile Postpaid", R.drawable.mobile_icon, PostPaidActivity.class));
        searchItemModelArrayList.add(new SearchItemModel("Data Card", R.drawable.data_card_icon, DataCardActivity.class));
        searchItemModelArrayList.add(new SearchItemModel("DTH", R.drawable.dth_icon, DthActivity.class));
        searchItemModelArrayList.add(new SearchItemModel("LandLine", R.drawable.landline_icon, LandLineActivity.class));
        searchItemModelArrayList.add(new SearchItemModel("Broadband", R.drawable.braodband_icon, BroadbandActivity.class));
        searchItemModelArrayList.add(new SearchItemModel("Electricity", R.drawable.electricity_icon, ElectricityActivity.class));
        searchItemModelArrayList.add(new SearchItemModel("Gas", R.drawable.gas_icon, GasActivity.class));
        searchItemModelArrayList.add(new SearchItemModel("Water", R.drawable.water_icon, WaterActivity.class));
        searchItemModelArrayList.add(new SearchItemModel("Insurance", R.drawable.insurance_icon, InsuranceActivity.class));
        searchItemModelArrayList.add(new SearchItemModel("Train Booking", R.drawable.train_booking_icon, WebViewActivity.class));
        searchItemModelArrayList.add(new SearchItemModel("Domestic Money Transfer", R.drawable.domestic_money_icon, MoneyTransferActivity.class));

        recyclerView.setLayoutManager(Functions.layoutManager(SearchActivity.this, Functions.VERTICAL,0));
        searchItemAdapter=new SearchItemAdapter(SearchActivity.this, searchItemModelArrayList);
        recyclerView.setAdapter(searchItemAdapter);

        recyclerView.setVisibility(View.GONE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchItemAdapter.filter(newText);
                recyclerView.setVisibility(View.VISIBLE);
                noDataIV.setVisibility(View.GONE);


                if (newText.isEmpty()){
                    recyclerView.setVisibility(View.GONE);
                    noDataIV.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (!searchView.isFocused()){
                 finish();
                }
                return false;
            }
        });
    }
}
