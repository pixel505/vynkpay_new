package com.vynkpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.vynkpay.R;
import com.vynkpay.adapter.BonusAdapter;
import com.vynkpay.databinding.ActivityClubBonusListBinding;
import com.vynkpay.models.MyAccount;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class ClubBonusListActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityClubBonusListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_club_bonus_list);
        clicks();

    }

    private void clicks() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.clubbonus);

            MyAccount[] myAccount = new MyAccount[] {
                    new MyAccount("Club Bonus1", R.drawable.club),
                    new MyAccount("Club Bonus2", R.drawable.club),
                    new MyAccount("Club Bonus3", R.drawable.club)
            };

        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        BonusAdapter adapter = new BonusAdapter(getApplicationContext(), myAccount);
        binding.clubRecycler.setLayoutManager(manager);
        binding.clubRecycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(ClubBonusListActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(ClubBonusListActivity.this,ClubBonusListActivity.this::finishAffinity);
        }
    }
}