package com.vynkpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.NotificationActivity;
import com.vynkpay.adapter.TravelAdapter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.ServiceModel;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreActivityNew extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_new);
        ButterKnife.bind(this);


    }
}
