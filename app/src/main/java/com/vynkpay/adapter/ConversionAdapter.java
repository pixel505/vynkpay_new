package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vynkpay.R;
import com.vynkpay.activity.BonusActivity;
import com.vynkpay.activity.activities.Community;
import com.vynkpay.activity.activities.CommunityDetailActivity;
import com.vynkpay.activity.activities.ConversionActivity;
import com.vynkpay.activity.activities.InvoiceActivity;
import com.vynkpay.activity.activities.PackageAActivity;
import com.vynkpay.activity.activities.StatementActivity;
import com.vynkpay.activity.activities.WalletNewActivity;
import com.vynkpay.activity.activities.WithdrawRequestListActivity;
import com.vynkpay.databinding.AccountItemListBinding;
import com.vynkpay.databinding.ConversionItemListBinding;
import com.vynkpay.models.MyAccount;
import com.vynkpay.retrofit.model.ConversionResponse;
import com.vynkpay.utils.CallActivity;
import com.vynkpay.utils.Functions;

import java.util.List;

public class ConversionAdapter extends RecyclerView.Adapter<ConversionAdapter.ViewHolder> {
    Context context;
    List<ConversionResponse.Data.Listing>  listdata;


    public ConversionAdapter(Context ac, List<ConversionResponse.Data.Listing> listing) {
        this.context = ac;
        this.listdata = listing;
    }

    @Override
    public ConversionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConversionItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.conversion_item_list, parent, false);
        ConversionAdapter.ViewHolder viewHolder = new ConversionAdapter.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ConversionAdapter.ViewHolder holder, int position) {
        ConversionResponse.Data.Listing data=listdata.get(position);
        holder.binding.dateTV.setText(Functions.changeDateFormat(data.getCreatedDate(), "yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy"));
        holder.binding.amountTV.setText(Functions.CURRENCY_SYMBOL+data.getPAmount());
        holder.binding.serviceTV.setText(Functions.CURRENCY_SYMBOL+data.getTax());
        holder.binding.balanceTV.setText(Functions.CURRENCY_SYMBOL+data.getAmountOf());
        holder.binding.descriptionTV.setText(data.getProfitType());
        switch (data.getStatus()){
            case "0":
                holder.binding.statusTV.setText(context.getString(R.string.confirmed));
                holder.binding.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                break;
            case "2":
                holder.binding.statusTV.setText(context.getString(R.string.withdrawRequest));
                holder.binding.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorYellow));
                break;
            case "3":
                holder.binding.statusTV.setText(context.getString(R.string.withdrawPaid));
                holder.binding.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                break;
            case "4":
                holder.binding.statusTV.setText(context.getString(R.string.withdrawCancel));
                holder.binding.statusTV.setTextColor(ContextCompat.getColor(context, R.color.buttonOffer));
                break;
            case "5":
                holder.binding.statusTV.setText(context.getString(R.string.paidUser));
                holder.binding.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                break;
            default:
                holder.binding.statusTV.setText(context.getString(R.string.confirmed));
                holder.binding.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }


    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ConversionItemListBinding binding;

        ViewHolder(final ConversionItemListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}




