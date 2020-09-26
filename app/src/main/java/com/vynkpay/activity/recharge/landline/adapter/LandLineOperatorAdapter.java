package com.vynkpay.activity.recharge.landline.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.recharge.broadband.activity.BroadbandActivity;
import com.vynkpay.activity.recharge.electricity.activity.ElectricityActivity;
import com.vynkpay.activity.recharge.gas.GasActivity;
import com.vynkpay.activity.recharge.insurance.InsuranceActivity;
import com.vynkpay.activity.recharge.landline.activity.LandLineActivity;
import com.vynkpay.activity.recharge.landline.events.EventBusLandline;
import com.vynkpay.activity.recharge.mobile.model.RechargeModel;
import com.vynkpay.activity.recharge.water.WaterActivity;
import com.vynkpay.custom.NormalLightTextView;
import com.vynkpay.utils.ApiParams;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class LandLineOperatorAdapter extends RecyclerView.Adapter<LandLineOperatorAdapter.MyViewHolder> {
    private ArrayList<RechargeModel> operatorList;
    Context context;
    String type;

    public LandLineOperatorAdapter(Context context, ArrayList<RechargeModel> data, String type) {
        this.operatorList = data;
        this.context = context;
        this.type = type;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_operator_rcg, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    Intent intent;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.operatorName.setText(operatorList.get(listPosition).getOperatorName());
        Log.i(">>iage", "onBindViewHolder: " + BuildConfig.ImageBaseURl + operatorList.get(listPosition).getOpratorImage());
        Picasso.get().load(BuildConfig.ImageBaseURl + operatorList.get(listPosition).getOpratorImage()).error(R.drawable.app_icon).into(holder.imageOperator);
        holder.tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("4")) {
                    intent = new Intent(context, BroadbandActivity.class);
                } else if (type.equalsIgnoreCase("10")) {
                    intent = new Intent(context, WaterActivity.class);
                } else if (type.equalsIgnoreCase("11")) {
                    intent = new Intent(context, InsuranceActivity.class);
                } else if (type.equalsIgnoreCase("9")) {
                    intent = new Intent(context, GasActivity.class);
                } else if (type.equalsIgnoreCase(ApiParams.type_electricty)) {
                    intent = new Intent(context, ElectricityActivity.class);
                } else {
                    intent = new Intent(context, LandLineActivity.class);
                }

                intent.putExtra("operator", operatorList.get(listPosition).getOperatorName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                EventBus.getDefault().postSticky(new EventBusLandline(
                        operatorList.get(listPosition).getOperatorId(),operatorList.get(listPosition).getOperatorName()
                        , operatorList.get(listPosition).getMaxLimit(),
                        operatorList.get(listPosition).getMinLimit(),
                        operatorList.get(listPosition).getLabel(),
                        operatorList.get(listPosition).getOperatorInnerModelArrayList().get(0).getId()));
                ((Activity) context).startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return operatorList.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView operatorCircleImageView;
        NormalLightTextView operatorName;
        ImageView imageOperator;
        LinearLayout tap;


        public MyViewHolder(View itemView) {
            super(itemView);
            imageOperator = itemView.findViewById(R.id.imageOperator);
            operatorName = itemView.findViewById(R.id.operatorName);
            tap = itemView.findViewById(R.id.tap);

        }
    }

}
