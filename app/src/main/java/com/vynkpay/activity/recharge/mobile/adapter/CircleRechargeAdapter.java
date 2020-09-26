package com.vynkpay.activity.recharge.mobile.adapter;

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
import com.vynkpay.activity.recharge.datacard.activity.DataCardActivity;
import com.vynkpay.activity.recharge.mobile.activity.PostPaidActivity;
import com.vynkpay.activity.recharge.mobile.activity.PrepaidActivity;
import com.vynkpay.activity.recharge.mobile.events.RechargeEventBus;
import com.vynkpay.activity.recharge.mobile.model.RechargeModel;
import com.vynkpay.custom.NormalLightTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CircleRechargeAdapter extends RecyclerView.Adapter<CircleRechargeAdapter.MyViewHolder> {
    private ArrayList<RechargeModel> cicleArrayList;
    Context context;
    String operator;
    String type;


    public CircleRechargeAdapter(Context context, ArrayList<RechargeModel> circleArrayList, String operator, String type) {
        this.cicleArrayList = circleArrayList;
        this.context = context;
        this.operator = operator;
        this.type = type;
        Log.i(">>id", "ElectricityCircleAdapter: " + operator + "-");
    }

    @Override
    public CircleRechargeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_circle_name_rcg, parent, false);
        return new CircleRechargeAdapter.MyViewHolder(view);
    }

    Intent resultIntent;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        holder.circleText.setText(cicleArrayList.get(listPosition).getCircleName());
        holder.tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case "1":
                        resultIntent = new Intent(context, PrepaidActivity.class);
                        break;
                    case "2":
                        resultIntent = new Intent(context, PostPaidActivity.class);
                        break;
                    case "6":
                        resultIntent = new Intent(context, DataCardActivity.class);
                        break;
                    case "7":
                        resultIntent = new Intent(context, DataCardActivity.class);
                        break;

                }
                resultIntent.putExtra("operator", operator);
                resultIntent.putExtra("circleName", cicleArrayList.get(listPosition).getCircleName());
                resultIntent.putExtra("circle_id", cicleArrayList.get(listPosition).getCircleId());
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                EventBus.getDefault().postSticky(new RechargeEventBus(operator, cicleArrayList.get(listPosition).getCircleName(), cicleArrayList.get(listPosition).getCircleId()));
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

