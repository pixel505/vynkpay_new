package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.vynkpay.R;
import com.vynkpay.activity.ClubBonusActivity;
import com.vynkpay.activity.ClubBonusListActivity;
import com.vynkpay.activity.ReferalBonusActivity;
import com.vynkpay.databinding.BonusItemListBinding;
import com.vynkpay.models.MyAccount;
import org.jetbrains.annotations.NotNull;

public class BonusAdapter extends RecyclerView.Adapter<BonusAdapter.ViewHolder> {
    Context context;
    MyAccount[]  listdata;

    public BonusAdapter(Context ac, MyAccount[] listdata) {
        this.context = ac;
        this.listdata = listdata;
    }

    @Override
    public BonusAdapter.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        BonusItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.bonus_item_list, parent, false);
        BonusAdapter.ViewHolder viewHolder = new ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BonusAdapter.ViewHolder holder, int position) {
        MyAccount myListData = listdata[position];
        holder.binding.practiceTitleText.setText(myListData.getDescription());
        Glide.with(context).load(myListData.getImgId()).into(holder.binding.practiceImage);

        /* MyAccount[] myAccount = new MyAccount[] {
                new MyAccount("First Level Bonus", R.drawable.referralbonus),
                new MyAccount("Generation Bonus", R.drawable.generationbonus),
                new MyAccount("Global Royalty", R.drawable.globalpoolbonus),
                new MyAccount("Leadership Bonus", R.drawable.leadershipbonu),
                new MyAccount("Ambassador Bonus", R.drawable.ambassador),
                new MyAccount("Chairman Bonus", R.drawable.chairman),
                new MyAccount("Appraisal Bonus", R.drawable.appraisal),
                new MyAccount("Club Bonus", R.drawable.club)

        };*/
        holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listdata[position].getDescription().equalsIgnoreCase("First Level Bonus")){
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","1"));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Generation Bonus")){
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","2"));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Global Royalty")){
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","3"));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Leadership Bonus")){
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","4"));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Ambassador Bonus")){
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","5"));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Chairman Bonus")){
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","6"));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Appraisal Bonus")){
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","7"));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Club Bonus")){
                    context.startActivity(new Intent(context, ClubBonusListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","8"));
                    //context.startActivity(new Intent(context, ClubBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","8"));
                    //Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
                } else if (listdata[position].getDescription().equalsIgnoreCase("Volume Bonus")){
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","9"));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Performance Bonus")){
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","10"));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Shopping Bonus")){
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","11"));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Club Bonus1")){
                    context.startActivity(new Intent(context, ClubBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","c1"));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Club Bonus2")){
                    context.startActivity(new Intent(context, ClubBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","c2"));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Club Bonus3")){
                    context.startActivity(new Intent(context, ClubBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","c3"));
                }

            }
        });


       /* if(position==0){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","1"));

                }
            });
        }*/


      /* if(position==1){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","2"));
                }
            });
        }*/

        /*if(position==2){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","3"));
                }
            });
        }*/

       /* if(position==3){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","4"));
                }
            });
        }*/

      /*  if(position==4){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","5"));
                }
            });
        }*/


       /* if(position==5){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","6"));
                }
            });
        }*/

       /* if(position==6){
            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ReferalBonusActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("type","7"));
                }
            });
        }*/

       /* if (position == 7){

            holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
                }
            });

        }*/
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final BonusItemListBinding binding;

        ViewHolder(final BonusItemListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }

    }

}


