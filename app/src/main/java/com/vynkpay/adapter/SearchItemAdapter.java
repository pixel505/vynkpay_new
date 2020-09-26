package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.activity.activities.WebViewActivity;
import com.vynkpay.activity.recharge.mobile.activity.PostPaidActivity;
import com.vynkpay.activity.recharge.mobile.activity.PrepaidActivity;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.SearchItemModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;

import java.util.ArrayList;
import java.util.Locale;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.Holder>{

    Context context;
    ArrayList<SearchItemModel> allItemModelArrayList;
    ArrayList<SearchItemModel> searchedItemModelArrayList;
    Intent intent;

    public SearchItemAdapter(Context context, ArrayList<SearchItemModel> allItemModelArrayList) {
        this.context = context;
        this.allItemModelArrayList = allItemModelArrayList;
        this.searchedItemModelArrayList=new ArrayList<>();
        this.searchedItemModelArrayList.addAll(allItemModelArrayList);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.search_item_layout_rcg, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.itemNameTV.setText(allItemModelArrayList.get(position).getName());
        holder.iconIV.setImageResource(allItemModelArrayList.get(position).getIcon());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Prefes.getUserData(context).equalsIgnoreCase("")) {
                    if (allItemModelArrayList.get(position).getName().equalsIgnoreCase("Mobile Prepaid")){
                        M.reset(context);
                        intent = new Intent(context, PrepaidActivity.class);
                        intent.putExtra("type", ApiParams.type_for_prepaid);
                        context.startActivity(intent);
                    }else if (allItemModelArrayList.get(position).getName().equalsIgnoreCase("Mobile Postpaid")){
                        M.reset(context);
                        intent = new Intent(context, PostPaidActivity.class);
                        intent.putExtra("type", ApiParams.type_for_postpaid);
                        context.startActivity(intent);
                    }else if (allItemModelArrayList.get(position).getName().equalsIgnoreCase("Train Booking")){
                        M.reset(context);
                        intent=new Intent(context, WebViewActivity.class);
                        intent.putExtra("title","Train Booking");
                        intent.putExtra("url","");
                        context.startActivity(intent);
                    }else {
                        M.reset(context);
                        intent = new Intent(context, allItemModelArrayList.get(position).getLaunchingActivity());
                        context.startActivity(intent);
                    }
                } else {
                    M.makeChecks(context, LoginActivity.class);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allItemModelArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        NormalTextView itemNameTV;
        ImageView iconIV;
        public Holder(@NonNull View itemView) {
            super(itemView);
            itemNameTV=itemView.findViewById(R.id.itemNameTV);
            iconIV=itemView.findViewById(R.id.iconIV);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        allItemModelArrayList.clear();
        if (charText.length() == 0) {
            allItemModelArrayList.addAll(searchedItemModelArrayList);
        } else {
            for (SearchItemModel wp : searchedItemModelArrayList) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    allItemModelArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
