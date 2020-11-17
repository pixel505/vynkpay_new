package com.vynkpay.adapter;

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
import com.vynkpay.R;
import com.vynkpay.activity.activities.SupportChatActivity;
import com.vynkpay.databinding.OpenTicketItemListBinding;
import com.vynkpay.retrofit.model.GetOpenCloseTicket;

import java.util.List;

public class OpenTicketAdapter extends RecyclerView.Adapter<OpenTicketAdapter.ViewHolder> {
    Context context;
    List<GetOpenCloseTicket.Data.CloseTicket>  listdata;
    String isActive;

    public OpenTicketAdapter(Context applicationContext, List<GetOpenCloseTicket.Data.CloseTicket> myList, String s) {
        this.context=applicationContext;
        this.listdata=myList;
        this.isActive=s;
    }


    @Override
    public OpenTicketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OpenTicketItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.open_ticket_item_list, parent, false);
        OpenTicketAdapter.ViewHolder viewHolder = new OpenTicketAdapter.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OpenTicketAdapter.ViewHolder holder, int position) {
        GetOpenCloseTicket.Data.CloseTicket  myListData = listdata.get(position);
        holder.binding.ticketidtext.setText(myListData.getTicketNo());
        holder.binding.subjectText.setText(myListData.getSubject());
     //   holder.binding.descText.setText(""+": "+myListData.getDescription());
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
                holder.binding.openCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, SupportChatActivity.class).setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("ticketId",myListData.getId()).putExtra("mark","cannot"));
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
        final OpenTicketItemListBinding binding;

        ViewHolder(final OpenTicketItemListBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
