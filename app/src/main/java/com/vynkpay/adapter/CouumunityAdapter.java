package com.vynkpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.R;
import com.vynkpay.databinding.CommunityItemListBinding;
import com.vynkpay.retrofit.model.TeamSummaryResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CouumunityAdapter extends RecyclerView.Adapter<CouumunityAdapter.ViewHolder> {
    Context context;
    List<TeamSummaryResponse.Datum> listdata;

    public CouumunityAdapter(Context ac, List<TeamSummaryResponse.Datum> listdata) {
        this.context = ac;
        this.listdata = listdata;
    }
    public void setFilter(List<TeamSummaryResponse.Datum> FilteredDataList) {
        listdata = FilteredDataList;
        notifyDataSetChanged();
    }
    @Override
    public CouumunityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommunityItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.community_item_list, parent, false);
        CouumunityAdapter.ViewHolder viewHolder = new CouumunityAdapter.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CouumunityAdapter.ViewHolder holder, int position) {
        TeamSummaryResponse.Datum myListData = listdata.get(position);
        holder.binding.levelText.setText("LEVEL"+" "+myListData.getLevel());
        if(myListData.getPaidStatus().equals("0")){
            holder.binding.statusText.setText("Unpaid");
            holder.binding.statusText.setTextColor(ContextCompat.getColor(context, R.color.buttonOffer));
        }

        else if(myListData.getPaidStatus().equals("1")){
            holder.binding.statusText.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            holder.binding.statusText.setText("Paid");

        }
        holder.binding.nameText.setText(myListData.getName());
        holder.binding.nameryttext.setText("("+myListData.getUsername()+")");
        holder.binding.codeText.setText("+"+myListData.getUser_country_code());
        holder.binding.numbertext.setText(myListData.getUserPhone());
        holder.binding.usernameText.setText(myListData.getReferdby());
        holder.binding.refusernameText.setText("("+myListData.getRefusername()+")");

        try {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            Date d = f.parse(myListData.getRegisterDate());
            DateFormat date = new SimpleDateFormat("dd-MM-yyy");
            holder.binding.registeDateText.setText(date.format(d));
            System.out.println("Date: " + date.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            holder.binding.designationtext.setText(myListData.getDesignation());
        }catch (Exception e){
            e.printStackTrace();
        }


        holder.binding.paidDateText.setText(myListData.getPackagePrice());


    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final CommunityItemListBinding binding;

        ViewHolder(final CommunityItemListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
