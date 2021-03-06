package com.vynkpay.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.R;
import com.vynkpay.databinding.DirectRefItemListBinding;
import com.vynkpay.retrofit.model.ReferalsResponse;
import com.vynkpay.utils.Functions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DirectRefAdapter extends RecyclerView.Adapter<DirectRefAdapter.ViewHolder>  {
    Context context;
    List<ReferalsResponse.Datum>  listdata,arrayListFiltered;

    public DirectRefAdapter(Context ac, List<ReferalsResponse.Datum> listdata) {
        this.context = ac;
        this.listdata = listdata;
        this.arrayListFiltered=listdata;
    }
    public void setFilter(List<ReferalsResponse.Datum> FilteredDataList) {
        listdata = FilteredDataList;
        notifyDataSetChanged();
    }

    @Override
    public DirectRefAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DirectRefItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.direct_ref_item_list, parent, false);
        DirectRefAdapter.ViewHolder viewHolder = new DirectRefAdapter.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DirectRefAdapter.ViewHolder holder, int position) {
        ReferalsResponse.Datum  myListData = listdata.get(position);

        holder.binding.nameText.setText(myListData.getName());

        holder.binding.codeText.setText("+"+myListData.getMobileCode());
        holder.binding.numbertext.setText(myListData.getPhone());

        try {
            holder.binding.nameryttext.setText("("+myListData.getUsername()+")");
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            Date d = f.parse(myListData.getCreatedDate());
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
            holder.binding.registeDateText.setText(date.format(d));
            System.out.println("Date: " + date.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }



        if(myListData.getPaidStatus().equals("0")){
            holder.binding.statusText.setText("Unpaid");
            holder.binding.statusText.setTextColor(ContextCompat.getColor(context, R.color.buttonOffer));
            holder.binding.paidDateLinear.setVisibility(View.INVISIBLE);
        }else if(myListData.getPaidStatus().equals("1")){
            holder.binding.statusText.setText("Paid");
            holder.binding.statusText.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            holder.binding.paidDateLinear.setVisibility(View.VISIBLE);
        }

        holder.binding.purchaseamountText.setText(Functions.CURRENCY_SYMBOL+myListData.getPackagePrice());


        if(myListData.getPurchaseCreateDate()!=null) {
            try {
                DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date date = f.parse(myListData.getPurchaseCreateDate());
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                holder.binding.paidDateText.setText(dateFormat.format(date));
                holder.binding.paidDateLinear.setVisibility(View.VISIBLE);
                holder.binding.paidDateText.setTextColor(ContextCompat.getColor(context, R.color.black));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }else {
            holder.binding.paidDateText.setText("Unpaid");
            holder.binding.paidDateText.setTextColor(ContextCompat.getColor(context, R.color.buttonOffer));
            holder.binding.statusText.setText("Unpaid");
            holder.binding.statusText.setTextColor(ContextCompat.getColor(context, R.color.buttonOffer));
        }

        try{
            holder.binding.designationtext.setText(myListData.getDesignation());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final DirectRefItemListBinding binding;

        ViewHolder(final DirectRefItemListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
