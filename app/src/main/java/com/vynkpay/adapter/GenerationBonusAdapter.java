package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.GenerationBonusDetailActivity;
import com.vynkpay.databinding.GenerationItemBinding;
import com.vynkpay.retrofit.model.GenerationBonusResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GenerationBonusAdapter extends RecyclerView.Adapter<GenerationBonusAdapter.ViewHolder>  {
    Context context;
    List<GenerationBonusResponse.Data.Listing> listdata;
    String viewType;

    public GenerationBonusAdapter(Context ac, List<GenerationBonusResponse.Data.Listing> data, String viewType) {
        this.context = ac;
        this.listdata = data;
        this.viewType = viewType;
    }

    @Override
    public GenerationBonusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GenerationItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.generation_item, parent, false);
        GenerationBonusAdapter.ViewHolder viewHolder = new GenerationBonusAdapter.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GenerationBonusAdapter.ViewHolder holder, int position) {
        GenerationBonusResponse.Data.Listing  myListData = listdata.get(position);

        if (viewType.equals("AB")){
            holder.binding.nameText.setText(myListData.getRefName());
            holder.binding.nameryttext.setText("("+myListData.getRefUsername()+")");
        }else {
            holder.binding.nameText.setText(myListData.getName());
            holder.binding.nameryttext.setText("("+myListData.getUsername()+")");
        }

        holder.binding.paidDateText.setText(Functions.CURRENCY_SYMBOL+" "+myListData.getPAmount());

        if(myListData.getStatus().equals("0")){
            holder.binding.purchaseamountText.setText("Confirmed");
        } else {
            holder.binding.purchaseamountText.setText("On Hold");
        }

        try {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            Date d = f.parse(myListData.getCreatedDate());
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
            holder.binding.datetext.setText(date.format(d));
            System.out.println("Date: " + date.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.binding.clickLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewType.equals("GB")){
                    try {
                        DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
                        Date d = f.parse(myListData.getCreatedDate());
                        DateFormat date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
                        String dateForFilter = date.format(d);
                        context.startActivity(new Intent(context, GenerationBonusDetailActivity.class)
                                .putExtra("dateForView", dateForFilter)
                                .putExtra("title", "Generation Bonus Detail")
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else if (viewType.equals("AB")){
                    context.startActivity(new Intent(context, GenerationBonusDetailActivity.class)
                            .putExtra("title", "Appraisal Bonus Detail")
                            .putExtra("frontUserID", myListData.getFrontUserId())
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        final GenerationItemBinding binding;
        ViewHolder(final GenerationItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }



}


