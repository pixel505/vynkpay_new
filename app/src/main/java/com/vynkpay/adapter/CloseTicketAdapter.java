package com.vynkpay.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.SupportChatActivity;
import com.vynkpay.databinding.CloseTicketItemListBinding;
import com.vynkpay.retrofit.model.GetOpenCloseTicket;

import java.util.List;

public class CloseTicketAdapter extends RecyclerView.Adapter<CloseTicketAdapter.ViewHolder> {
    Context context;
    List<GetOpenCloseTicket.Data.ActvTicket>  listdata;
    String isActive;

    public CloseTicketAdapter(Context applicationContext, List<GetOpenCloseTicket.Data.ActvTicket> myList, String s) {
        this.context=applicationContext;
        this.listdata=myList;
        this.isActive=s;
    }


    @Override
    public CloseTicketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CloseTicketItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.close_ticket_item_list, parent, false);
        CloseTicketAdapter.ViewHolder viewHolder = new CloseTicketAdapter.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CloseTicketAdapter.ViewHolder holder, int position) {
        GetOpenCloseTicket.Data.ActvTicket  myListData = listdata.get(position);
        holder.binding.ticketidtext.setText(myListData.getTicketNo());
        holder.binding.subjectText.setText(myListData.getSubject());
      //  holder.binding.descText.setText(myListData.getDescription());
        holder.binding.departText.setText(myListData.getDept());
        holder.binding.dateText.setText(myListData.getCreatedDate());
        if(myListData.getStatus().equals("0")){
            holder.binding.statusText.setText("Pending");

        }


        else if(myListData.getStatus().equals("1")){
            holder.binding.statusText.setText("Replied");

        }

        else if(myListData.getStatus().equals("2")){
            holder.binding.statusText.setText("Closed");

        }




        String text = "Description"+" :"+myListData.getMsg();
        SpannableString ss = new SpannableString(text);
        SpannableStringBuilder ssb = new SpannableStringBuilder(ss);
        ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.parseColor("#8E8F8F"));
        ssb.setSpan(fcsRed, 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.binding.descText.setText(ssb);

        if(isActive!=null) {
            if (isActive.equals("1")) {
                holder.binding.closeCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, SupportChatActivity.class).setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("ticketId",myListData.getId()).putExtra("mark","can"));


                    }
                });
            }
        }



    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final CloseTicketItemListBinding binding;

        ViewHolder(final CloseTicketItemListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
