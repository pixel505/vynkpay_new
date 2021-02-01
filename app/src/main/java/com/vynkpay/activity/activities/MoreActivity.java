package com.vynkpay.activity.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.adapter.AllServicesAdapter;
import com.vynkpay.adapter.CustomPagerAdapter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.ServiceModel;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rechargeServiceRecyclerView)
    RecyclerView rechargeServiceRecyclerView;
    CustomPagerAdapter customPagerAdapter;
    ArrayList<String> dummy = new ArrayList<>();
    static int SPAN_COUNT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_rcg);

        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        ButterKnife.bind(MoreActivity.this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        setListeners();
        toolbarTitle.setText("More on VynkPay");

       // new FetchServiceTask(MoreActivity.this).execute();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        if (width <= 650 && width > 500) {
            SPAN_COUNT = 3;
        } else if (width <= 480 && width > 350) {
            SPAN_COUNT = 3;
        } else {
            SPAN_COUNT = 4;
        }

        setServicesItems();
    }

    private void setListeners() {
    }

    ArrayList<ServiceModel> serviceModelArrayList=new ArrayList<>();
    public void setServicesItems(){
        serviceModelArrayList.clear();
        serviceModelArrayList.add(new ServiceModel("Mobile Prepaid", R.drawable.mobile_icon));
        serviceModelArrayList.add(new ServiceModel("Mobile Postpaid", R.drawable.mobile_icon));
        serviceModelArrayList.add(new ServiceModel("Data Card", R.drawable.data_card_icon));
        serviceModelArrayList.add(new ServiceModel("DTH", R.drawable.dth_icon));
        serviceModelArrayList.add(new ServiceModel("LandLine", R.drawable.landline_icon));
        serviceModelArrayList.add(new ServiceModel("Broadband", R.drawable.braodband_icon));
        serviceModelArrayList.add(new ServiceModel("Electricity", R.drawable.electricity_icon));
        serviceModelArrayList.add(new ServiceModel("Gas", R.drawable.gas_icon));
        serviceModelArrayList.add(new ServiceModel("Water", R.drawable.water_icon));
        serviceModelArrayList.add(new ServiceModel("Insurance", R.drawable.insurance_icon));
        serviceModelArrayList.add(new ServiceModel("Train Booking", R.drawable.train_booking_icon));
        serviceModelArrayList.add(new ServiceModel("Domestic Money Transfer", R.drawable.domestic_money_icon));
        rechargeServiceRecyclerView.setLayoutManager(Functions.layoutManager(MoreActivity.this, Functions.GRID, 4));
        AllServicesAdapter servicesAdapter=new AllServicesAdapter(MoreActivity.this, serviceModelArrayList);
        rechargeServiceRecyclerView.setAdapter(servicesAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(MoreActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(MoreActivity.this,MoreActivity.this::finishAffinity);
        }
    }
}
