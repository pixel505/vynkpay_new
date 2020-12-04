package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.activity.activities.MoneyTransferActivity;
import com.vynkpay.activity.activities.WebViewActivity;
import com.vynkpay.activity.recharge.gas.GasActivity;
import com.vynkpay.activity.recharge.mobile.activity.PrepaidActivity;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.ServiceModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.RoundishImageView;
import java.util.ArrayList;

public class WhatsNewAdapter extends RecyclerView.Adapter<WhatsNewAdapter.Holder>{
    Context context;
    ArrayList<ServiceModel> serviceModelArrayList;
    Intent intent;

    public WhatsNewAdapter(Context context, ArrayList<ServiceModel> serviceModelArrayList) {
        this.context = context;
        this.serviceModelArrayList = serviceModelArrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.whats_new_item_layout_rcg, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.normalTextView.setText(serviceModelArrayList.get(position).getServiceName());
        holder.itemIconIV.setImageResource(serviceModelArrayList.get(position).getServiceIconInt());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        M.reset(context);
                        intent = new Intent(context, GasActivity.class);
                        context.startActivity(intent);
                        break;
                    case 1:
                        M.reset(context);
                        if (!Prefes.getUserData(context).equalsIgnoreCase("")) {
                            M.launchActivity(context, MoneyTransferActivity.class);
                        } else {
                            M.makeChecks(context, LoginActivity.class);
                        }
                        break;
                    case 2:
                        M.reset(context);
                        intent = new Intent(context, PrepaidActivity.class);
                        intent.putExtra("type", ApiParams.type_for_prepaid);
                        context.startActivity(intent);
                        break;
                    case 3:
                        M.reset(context);
                        intent=new Intent(context, WebViewActivity.class);
                        intent.putExtra("title","Train Booking");
                        intent.putExtra("url","");
                        context.startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceModelArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        NormalTextView normalTextView;
        RoundishImageView itemIconIV;
        public Holder(@NonNull View itemView) {
            super(itemView);

            normalTextView=itemView.findViewById(R.id.itemNameTV);
            itemIconIV=itemView.findViewById(R.id.itemIconIV);

        }
    }


}
