package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.vynkpay.activity.activities.InvoiceActivity;
import com.vynkpay.activity.activities.PackageAActivity;
import com.vynkpay.activity.activities.StatementActivity;
import com.vynkpay.activity.activities.WalletNewActivity;
import com.vynkpay.R;
import com.vynkpay.activity.BonusActivity;
import com.vynkpay.activity.activities.Community;
import com.vynkpay.activity.activities.WithdrawRequestListActivity;
import com.vynkpay.databinding.AccountItemListBinding;
import com.vynkpay.models.MyAccount;
import com.vynkpay.utils.CallActivity;

public class AccountAdapter  extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    Context context;
    MyAccount[]  listdata;
    CallActivity callActivity;

    public AccountAdapter(Context ac, MyAccount[] listdata, CallActivity callActivity) {
        this.context = ac;
        this.listdata = listdata;
        this.callActivity = callActivity;
    }

    @Override
    public AccountAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AccountItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.account_item_list, parent, false);
        AccountAdapter.ViewHolder viewHolder = new AccountAdapter.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AccountAdapter.ViewHolder holder, int position) {
        MyAccount  myListData = listdata[position];
        holder.binding.practiceTitleText.setText(myListData.getDescription());
        Glide.with(context).load(myListData.getImgId()).into(holder.binding.practiceImage);
        holder.binding.practiceMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  if (position == 0){
                    context.startActivity(new Intent(context, PackageAActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if (position == 1){
                    context.startActivity(new Intent(context, InvoiceActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else if (position ==2){
                    context.startActivity(new Intent(context, Community.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else if (position ==3){
                    context.startActivity(new Intent(context, BonusActivity.class).putExtra("from","Bonuses").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else if (position ==4){
                    context.startActivity(new Intent(context, BonusActivity.class).putExtra("from","OldBonuses").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else if (position == 5){
                    context.startActivity(new Intent(context, WalletNewActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else if (position == 6){
                    context.startActivity(new Intent(context, WithdrawRequestListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } *//*else if (position == 6){
                    context.startActivity(new Intent(context, CommunityDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }*//* else if (position == 7){
                    context.startActivity(new Intent(context, StatementActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }*/

                if (listdata[position].getDescription().equalsIgnoreCase("Purchase")){
                    context.startActivity(new Intent(context, PackageAActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Invoice")){
                    context.startActivity(new Intent(context, InvoiceActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Community")){
                    context.startActivity(new Intent(context, Community.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Bonuses")){
                    context.startActivity(new Intent(context, BonusActivity.class).putExtra("from","Bonuses").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Old Bonuses")){
                    context.startActivity(new Intent(context, BonusActivity.class).putExtra("from","OldBonuses").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Wallets")){
                    context.startActivity(new Intent(context, WalletNewActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Withdrawal History")){
                    context.startActivity(new Intent(context, WithdrawRequestListActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if (listdata[position].getDescription().equalsIgnoreCase("Statement")){
                    context.startActivity(new Intent(context, StatementActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final AccountItemListBinding binding;

        ViewHolder(final AccountItemListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }

}



