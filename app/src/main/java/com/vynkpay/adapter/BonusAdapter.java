package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.ReferalBonusActivity;
import com.vynkpay.databinding.BonusItemListBinding;
import com.vynkpay.models.MyAccount;

public class BonusAdapter extends RecyclerView.Adapter<BonusAdapter.ViewHolder> {
    Context context;
    MyAccount[]  listdata;

    public BonusAdapter(Context ac, MyAccount[] listdata) {
        this.context = ac;
        this.listdata = listdata;
    }

    @Override
    public BonusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BonusItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.bonus_item_list, parent, false);
        BonusAdapter.ViewHolder viewHolder = new BonusAdapter.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BonusAdapter.ViewHolder holder, int position) {
        MyAccount myListData = listdata[position];
        holder.binding.practiceTitleText.setText(myListData.getDescription());
        Glide.with(context).load(myListData.getImgId()).into(holder.binding.practiceImage);


        if(position==0){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","1"));

                }
            });
        }


       if(position==1){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","2"));
                }
            });
        }

        if(position==2){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","3"));
                }
            });
        }

        if(position==3){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","4"));
                }
            });
        }

        if(position==4){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","5"));
                }
            });
        }


        if(position==5){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","6"));
                }
            });
        }

        if(position==6){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","7"));
                }
            });
        }

        if (position == 7){

            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
                }
            });

        }
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


