package com.vynkpay.activity.bustickets.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketSearchActivity extends AppCompatActivity {
    @BindView(R.id.search_recyclerview)
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_search_rcg);

        ButterKnife.bind(TicketSearchActivity.this);

        layoutManager = new LinearLayoutManager(TicketSearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        
    }
}
