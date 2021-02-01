package com.vynkpay.activity.bustickets.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.bustickets.adapter.DateAdapter;
import com.vynkpay.activity.bustickets.adapter.RecentTicketsAdapter;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.bus_btn_search)
    Button btn_search;
    @BindView(R.id.bus_edtxt_from)
    EditText edtxt_from;
    @BindView(R.id.bus_edtxt_to)
    NormalEditText edtxt_to;
    @BindView(R.id.bus_date_recyclerview)
    RecyclerView date_recyclerview;
    @BindView(R.id.bus_recent_recyclerview)
    RecyclerView recent_recyclerview;

    DateAdapter adapter;
    RecentTicketsAdapter search_adapter;
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> recents = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager, search_layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_bus_rcg);

        ButterKnife.bind(BusActivity.this);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Bus Tickets");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        dates.add("19 Jan");
        dates.add("20 Jan");
        dates.add("21 Jan");
        dates.add("Pick A");

        recents.add("a");
        recents.add("b");
        recents.add("a");
        recents.add("b");

        layoutManager = new GridLayoutManager(BusActivity.this, 4);
        date_recyclerview.setLayoutManager(layoutManager);
        adapter = new DateAdapter(BusActivity.this, dates);
        date_recyclerview.setAdapter(adapter);

        search_layoutManager = new LinearLayoutManager(BusActivity.this);
        recent_recyclerview.setLayoutManager(search_layoutManager);
        search_adapter = new RecentTicketsAdapter(BusActivity.this, recents);
        recent_recyclerview.setAdapter(search_adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(BusActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(BusActivity.this,BusActivity.this::finishAffinity);
        }
    }
}
