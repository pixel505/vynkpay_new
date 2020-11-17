package com.vynkpay.activity.recharge.dth.adapter;

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
import com.vynkpay.R;
import com.vynkpay.activity.recharge.dth.activity.DthActivity;
import com.vynkpay.activity.recharge.dth.events.EventBusDth;
import com.vynkpay.activity.recharge.mobile.model.RechargeModel;
import com.vynkpay.custom.NormalLightTextView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class DthOperatorAdapter extends RecyclerView.Adapter<DthOperatorAdapter.MyViewHolder> {
    private ArrayList<RechargeModel> operatorList;
    Context context;
    String type;

    public DthOperatorAdapter(Context context, ArrayList<RechargeModel> data, String type) {
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

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.operatorName.setText(operatorList.get(listPosition).getOperatorName());
        Log.i(">>iage", "onBindViewHolder: " + BuildConfig.ImageBaseURl + operatorList.get(listPosition).getOpratorImage());
        Picasso.get().load(BuildConfig.ImageBaseURl + operatorList.get(listPosition).getOpratorImage()).error(R.drawable.app_icon).into(holder.imageOperator);
        //Picasso.with(context).load(R.drawable.app_icon).into(holder.imageOperator);
        holder.tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DthActivity.class);
                intent.putExtra("operator", operatorList.get(listPosition).getOperatorName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                EventBus.getDefault().postSticky(new EventBusDth(operatorList.get(listPosition).getOperatorName()
                        , operatorList.get(listPosition).getMaxLimit(),
                        operatorList.get(listPosition).getMinLimit(),
                        operatorList.get(listPosition).getLabel()));
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
