package com.vynkpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityVyncBonusessBinding;
import com.vynkpay.databinding.BonusItemListBinding;
import com.vynkpay.models.MyAccount;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import org.jetbrains.annotations.NotNull;

public class VyncBonusessActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityVyncBonusessBinding binding;
    VyncBonusessActivity ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_vync_bonusess);
        ac = VyncBonusessActivity.this;
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
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.vyncbonuses);

        MyAccount[] myAccount = new MyAccount[]{
                new MyAccount("Weekly Bonus", R.drawable.weeklybonus3x),
                new MyAccount("Referral Bonus", R.drawable.referralbonus),
                new MyAccount("Performance Bonus", R.drawable.performancebonus3x),
                new MyAccount("Volume Bonus", R.drawable.volumebonus3x)
        };
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        BonusAdapter adapter = new BonusAdapter(getApplicationContext(), myAccount);
        binding.bonusRecycler.setLayoutManager(manager);
        binding.bonusRecycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(VyncBonusessActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(VyncBonusessActivity.this,VyncBonusessActivity.this::finishAffinity);
        }
    }

    public class BonusAdapter extends RecyclerView.Adapter<BonusAdapter.ViewHolder> {
        Context context;
        MyAccount[]  listdata;

        public BonusAdapter(Context ac, MyAccount[] listdata) {
            this.context = ac;
            this.listdata = listdata;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
            BonusItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.bonus_item_list, parent, false);
           ViewHolder viewHolder = new ViewHolder(binding);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MyAccount myListData = listdata[position];
            holder.binding.practiceTitleText.setText(myListData.getDescription());
            Glide.with(context).load(myListData.getImgId()).into(holder.binding.practiceImage);

            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (listdata[position].getDescription().equalsIgnoreCase("Weekly Bonus")){
                        context.startActivity(new Intent(context, WeeklyActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","v4"));
                    } else if (listdata[position].getDescription().equalsIgnoreCase("Referral Bonus")){
                        context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","v14"));
                    }  else if (listdata[position].getDescription().equalsIgnoreCase("Volume Bonus")){
                        context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","v15"));
                    } else if (listdata[position].getDescription().equalsIgnoreCase("Performance Bonus")){
                        context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","v16"));
                    }

                }
            });
        }


        @Override
        public int getItemCount() {
            return listdata.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final BonusItemListBinding binding;

            ViewHolder(final BonusItemListBinding itemBinding) {
                super(itemBinding.getRoot());
                this.binding = itemBinding;
            }

        }

    }

}