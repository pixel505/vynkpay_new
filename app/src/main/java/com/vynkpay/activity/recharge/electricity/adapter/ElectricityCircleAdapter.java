package com.vynkpay.activity.recharge.electricity.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.recharge.electricity.activity.ElectricityActivity;
import com.vynkpay.activity.recharge.electricity.model.ElectricityModel;
import com.vynkpay.activity.recharge.electricity.model.EventBusElectric;
import com.vynkpay.custom.NormalLightTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ElectricityCircleAdapter extends RecyclerView.Adapter<ElectricityCircleAdapter.MyViewHolder> {
    private ArrayList<ElectricityModel> cicleArrayList;
    Context context;
    String operator;
    String type;
    String operator_id;


    public ElectricityCircleAdapter(Context context, ArrayList<ElectricityModel> circleArrayList, String operator, String type, String operator_id) {
        this.cicleArrayList = circleArrayList;
        this.context = context;
        this.operator = operator;
        this.type = type;
        this.operator_id = operator_id;
        Log.i(">>id", "ElectricityCircleAdapter: " + operator + "-");
    }

    @Override
    public ElectricityCircleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_circle_name_rcg, parent, false);
        return new ElectricityCircleAdapter.MyViewHolder(view);
    }

    Intent resultIntent;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        if (operator_id.equalsIgnoreCase("99")) {
            holder.circleText.setText(cicleArrayList.get(listPosition).getId() + " " + cicleArrayList.get(listPosition).getDivision());
        } else {
            holder.circleText.setText(cicleArrayList.get(listPosition).getDivision());
        }
        holder.tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultIntent = new Intent(context, ElectricityActivity.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                EventBus.getDefault().postSticky(new EventBusElectric(cicleArrayList.get(listPosition).getId(),
                        cicleArrayList.get(listPosition).getOperator_id(),
                        cicleArrayList.get(listPosition).getState(), cicleArrayList.get(listPosition).getDivision(),
                        cicleArrayList.get(listPosition).getCode(), cicleArrayList.get(listPosition).getStatus()));
                ((Activity) context).startActivity(resultIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cicleArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        NormalLightTextView circleText;
        LinearLayout tap;


        public MyViewHolder(View itemView) {
            super(itemView);
            circleText = itemView.findViewById(R.id.circleText);
            tap = itemView.findViewById(R.id.tap);

        }
    }
}

