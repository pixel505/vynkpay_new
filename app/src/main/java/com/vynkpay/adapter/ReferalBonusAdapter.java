package com.vynkpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.databinding.ReferralBonusItemBinding;
import com.vynkpay.retrofit.model.ReferalBonusResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReferalBonusAdapter extends RecyclerView.Adapter<ReferalBonusAdapter.ViewHolder>  {
    Context context;
    List<ReferalBonusResponse.Datum> listdata;

    public ReferalBonusAdapter(Context ac, List<ReferalBonusResponse.Datum> data) {
        this.context = ac;
        this.listdata = data;
    }

    @Override
    public ReferalBonusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReferralBonusItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.referral_bonus_item, parent, false);
        ReferalBonusAdapter.ViewHolder viewHolder = new ReferalBonusAdapter.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReferalBonusAdapter.ViewHolder holder, int position) {
        ReferalBonusResponse.Datum  myListData = listdata.get(position);
        holder.binding.nameText.setText(myListData.getRefName());
        holder.binding.nameryttext.setText("("+myListData.getRefUsername()+")");
        holder.binding.paidDateText.setText(Functions.CURRENCY_SYMBOL+" "+myListData.getFamount());
        holder.binding.bonusAmountTV.setText(Functions.CURRENCY_SYMBOL+" "+myListData.getPAmount());
        if(myListData.getStatus().equals("0")){
            holder.binding.purchaseamountText.setText("Confirmed");
        }

        else {
            holder.binding.purchaseamountText.setText("On Hold");

        }

        try {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = f.parse(myListData.getCreatedDate());
            DateFormat date = new SimpleDateFormat("dd-MM-yyy");
            holder.binding.datetext.setText(date.format(d));
            System.out.println("Date: " + date.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        final ReferralBonusItemBinding binding;
        ViewHolder(final ReferralBonusItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}

