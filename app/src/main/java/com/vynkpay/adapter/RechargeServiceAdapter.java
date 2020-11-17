package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.vynkpay.R;
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
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import java.util.ArrayList;

public class RechargeServiceAdapter extends RecyclerView.Adapter<RechargeServiceAdapter.MyViewHolder> {
    ArrayList<?> serviceModelArrayList;
    Context context;

    public RechargeServiceAdapter(Context context, ArrayList<?> serviceModelArrayList) {
        this.context = context;
        this.serviceModelArrayList = serviceModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_recharge_service_rcg, parent, false);
        return new MyViewHolder(v);
    }

    Intent intent;

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (serviceModelArrayList.get(position) instanceof ServiceModel) {
            holder.rechargeText.setText(((ServiceModel) serviceModelArrayList.get(position)).getServiceName());
            holder.rechargeIcon.setBackgroundResource(((ServiceModel) serviceModelArrayList.get(position)).getServiceIconInt());
            holder.tap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return serviceModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView rechargeIcon;
        NormalTextView rechargeText;
        LinearLayout tap;

        public MyViewHolder(View itemView) {
            super(itemView);
            rechargeIcon = itemView.findViewById(R.id.rechargeIcon);
            rechargeText = itemView.findViewById(R.id.rechargeText);
            tap = itemView.findViewById(R.id.tap);
        }
    }
}
