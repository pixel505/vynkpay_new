package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.activity.activities.MoneyTransferActivity;
import com.vynkpay.activity.activities.WebViewActivity;
import com.vynkpay.activity.recharge.broadband.activity.BroadbandActivity;
import com.vynkpay.activity.recharge.datacard.activity.DataCardActivity;
import com.vynkpay.activity.recharge.dth.activity.DthActivity;
import com.vynkpay.activity.recharge.electricity.activity.ElectricityActivity;
import com.vynkpay.activity.recharge.gas.GasActivity;
import com.vynkpay.activity.recharge.insurance.InsuranceActivity;
import com.vynkpay.activity.recharge.landline.activity.LandLineActivity;
import com.vynkpay.activity.recharge.mobile.activity.PostPaidActivity;
import com.vynkpay.activity.recharge.mobile.activity.PrepaidActivity;
import com.vynkpay.activity.recharge.water.WaterActivity;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.ServiceModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;

import java.util.ArrayList;

public class AllServicesAdapter extends RecyclerView.Adapter<AllServicesAdapter.Holder>{

    Context context;
    ArrayList<ServiceModel> serviceModelArrayList;
    Intent intent;

    public AllServicesAdapter(Context context, ArrayList<ServiceModel> serviceModelArrayList) {
        this.context = context;
        this.serviceModelArrayList = serviceModelArrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.service_item_layout_rcg, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.nameTV.setText(serviceModelArrayList.get(position).getServiceName());
        holder.iconIV.setImageResource(serviceModelArrayList.get(position).getServiceIconInt());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (position) {
                    case 0:
                        M.reset(context);
                        intent = new Intent(context, PrepaidActivity.class);
                        intent.putExtra("type", ApiParams.type_for_prepaid);
                        context.startActivity(intent);
                        break;
                    case 1:
                        M.reset(context);
                        intent = new Intent(context, PostPaidActivity.class);
                        intent.putExtra("type", ApiParams.type_for_postpaid);
                        context.startActivity(intent);
                        break;
                    case 2:
                        M.reset(context);
                        intent = new Intent(context, DataCardActivity.class);
                        context.startActivity(intent);
                        break;
                    case 3:
                        M.reset(context);
                        intent = new Intent(context, DthActivity.class);
                        context.startActivity(intent);
                        break;
                    case 4:
                        M.reset(context);
                        intent = new Intent(context, LandLineActivity.class);
                        context.startActivity(intent);
                        break;
                    case 5:
                        M.reset(context);
                        intent = new Intent(context, BroadbandActivity.class);
                        context.startActivity(intent);
                        break;
                    case 6:
                        M.reset(context);
                        intent = new Intent(context, ElectricityActivity.class);
                        context.startActivity(intent);
                        break;
                    case 7:
                        M.reset(context);
                        intent = new Intent(context, GasActivity.class);
                        context.startActivity(intent);
                        break;
                    case 8:
                        intent = new Intent(context, WaterActivity.class);
                        context.startActivity(intent);
                        break;
                    case 9:
                        M.reset(context);
                        intent = new Intent(context, InsuranceActivity.class);
                        context.startActivity(intent);
                        break;
                    case 10:
                        M.reset(context);
                        intent=new Intent(context, WebViewActivity.class);
                        intent.putExtra("title","Train Booking");
                        intent.putExtra("url","");
                        context.startActivity(intent);
                        break;
                    case 11:
                        M.reset(context);
                        if (!Prefes.getUserData(context).equalsIgnoreCase("")) {
                            M.launchActivity(context, MoneyTransferActivity.class);
                        } else {
                            M.makeChecks(context, LoginActivity.class);
                        }
                }
            }
        });

        if (serviceModelArrayList.get(position).getServiceName().equalsIgnoreCase("more")){
            holder.iconIV.setVisibility(View.GONE);
            holder.moreFrame.setVisibility(View.VISIBLE);
        }else {
            holder.iconIV.setVisibility(View.VISIBLE);
            holder.moreFrame.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return serviceModelArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        NormalTextView nameTV;
        ImageView iconIV;
        FrameLayout moreFrame;
        public Holder(@NonNull View itemView) {
            super(itemView);
            nameTV=itemView.findViewById(R.id.nameTV);
            iconIV=itemView.findViewById(R.id.iconIV);
            moreFrame=itemView.findViewById(R.id.moreFrame);
        }
    }
}
