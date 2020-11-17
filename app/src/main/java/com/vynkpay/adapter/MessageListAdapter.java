package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.activities.WatchFileActivity;
import com.vynkpay.activity.activities.WatchPdfActivity;
import com.vynkpay.databinding.MessageItemBinding;
import com.vynkpay.retrofit.model.GetChatResponse;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {
    Context context;
    List<GetChatResponse.Data.Conversation> listdata;

    public MessageListAdapter(Context applicationContext, List<GetChatResponse.Data.Conversation> myList) {
        this.context=applicationContext;
        this.listdata=myList;
    }


    @Override
    public MessageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MessageItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.message_item, parent, false);
        MessageListAdapter.ViewHolder viewHolder = new MessageListAdapter.ViewHolder(binding);
        return viewHolder;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(MessageListAdapter.ViewHolder holder, int position) {


        GetChatResponse.Data.Conversation  myListData = listdata.get(position);

        if(myListData.getUsertype().equals("0")){
                holder.binding.leftLn.setVisibility(View.VISIBLE);
                holder.binding.leftmsg.setText(myListData.getBody());
                holder.binding.leftTime.setText(myListData.getCreatedDate());

            if(myListData.getAttachment()!=null){
                holder.binding.leftAttch.setVisibility(View.VISIBLE);
                holder.binding.rightText.setText(String.valueOf(myListData.getImage()));
                holder.binding.rightText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String split_string_array[]=String.valueOf(myListData.getImage()).split("\\.");

                        String part1 = split_string_array[0]; // 004
                        String part2 = split_string_array[1]; // 034556
                        Log.e("part2",""+part2);
                        Log.e("part1",""+part1);


                        if(part2.equals("pdf")){
                            Intent intent=new Intent(context, WatchPdfActivity.class);
                            intent.putExtra("pdfLink",BuildConfig.BASE_URL+"account/"+myListData.getAttachment());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }

                        else {
                            Intent intent=new Intent(context, WatchFileActivity.class);
                            intent.putExtra("imageLink",BuildConfig.BASE_URL+"account/"+myListData.getAttachment());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }

                    }
                });

            }
        }

        else {


            Log.e("data",""+myListData.getAttachment());
            Log.e("data",""+myListData.getImage());
            Log.e("data",""+myListData.getPath());


            holder.binding.rightLn.setVisibility(View.VISIBLE);
            holder.binding.rightTime.setText(myListData.getCreatedDate());
            holder.binding.rightmsg.setText(myListData.getBody());
            if(myListData.getAttachment()!=null ){
                holder.binding.rightAttch.setVisibility(View.VISIBLE);
                holder.binding.pathText.setText(String.valueOf(myListData.getImage()));
                holder.binding.pathText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       /* context.startActivity(new Intent(context, WatchFileActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra("link",BuildConfig.BASE_URL+"account/"+myListData.getAttachment())
                                .putExtra("linkType",String.valueOf(myListData.getImage()))*/




                        String split_string_array[]=String.valueOf(myListData.getImage()).split("\\.");

                        String part1 = split_string_array[0]; // 004
                        String part2 = split_string_array[1]; // 034556
                        Log.e("part2",""+part2);
                        Log.e("part1",""+part1);


                        if(part2.equals("pdf")){
                            Intent intent=new Intent(context, WatchPdfActivity.class);
                            intent.putExtra("pdfLink",BuildConfig.BASE_URL+"account/"+myListData.getAttachment());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }

                        else {
                            Intent intent=new Intent(context, WatchFileActivity.class);
                            intent.putExtra("imageLink",BuildConfig.BASE_URL+"account/"+myListData.getAttachment());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }



                    }
                });
             }

            else {
                holder.binding.rightAttch.setVisibility(View.GONE);
            }

        }




    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final MessageItemBinding binding;

        ViewHolder(final MessageItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
