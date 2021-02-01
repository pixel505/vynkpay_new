package com.vynkpay.activity.bustickets.activity;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketSearchActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    @BindView(R.id.search_recyclerview)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_ticket_search_rcg);

        ButterKnife.bind(TicketSearchActivity.this);

        layoutManager = new LinearLayoutManager(TicketSearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(TicketSearchActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(TicketSearchActivity.this,TicketSearchActivity.this::finishAffinity);
        }
    }
}
