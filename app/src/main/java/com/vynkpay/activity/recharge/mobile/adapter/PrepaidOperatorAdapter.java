package com.vynkpay.activity.recharge.mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.recharge.mobile.activity.SelectCircleActivity;
import com.vynkpay.activity.recharge.mobile.model.RechargeModel;
import com.vynkpay.custom.NormalLightTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PrepaidOperatorAdapter extends RecyclerView.Adapter<PrepaidOperatorAdapter.MyViewHolder> {
    private ArrayList<RechargeModel> operatorList;
    Context context;
    String type;

    public PrepaidOperatorAdapter(Context context, ArrayList<RechargeModel> data, String type) {
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



        Picasso.get().load(BuildConfig.ImageBaseURl + operatorList.get(listPosition).getOpratorImage()).error(R.drawable.app_icon).into(holder.imageOperator);

        holder.tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SelectCircleActivity.class);
                intent.putExtra("operator", operatorList.get(listPosition).getOperatorName());
                intent.putExtra("type", type);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
