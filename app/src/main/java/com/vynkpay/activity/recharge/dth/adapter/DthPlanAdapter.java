package com.vynkpay.activity.recharge.dth.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.recharge.dth.activity.DthActivity;
import com.vynkpay.activity.recharge.mobile.events.PlanRechargeBus;
import com.vynkpay.activity.recharge.mobile.model.OnBottomReachedListener;
import com.vynkpay.activity.recharge.mobile.model.PlanModel;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class DthPlanAdapter extends RecyclerView.Adapter<DthPlanAdapter.MyViewHolder> {
    private ArrayList<PlanModel> operatorList;
    Context context;
    OnBottomReachedListener onBottomReachedListener;

    public DthPlanAdapter(Context context, ArrayList<PlanModel> data) {
        this.operatorList = data;
        this.context = context;

    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {

        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item__datacard_plan_rcg, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    private String type = "";

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (position == operatorList.size() - 1) {
            onBottomReachedListener.onBottomReached(position);
        }
        holder.validityTextView.setText(operatorList.get(position).getRecharge_validity());
        holder.plainDescTextView.setText(operatorList.get(position).getRecharge_long_desc());
        holder.plainAmount.setText(Functions.CURRENCY_SYMBOL_USER + Double.parseDouble(operatorList.get(position).getRecharge_amount()));
        type = Prefes.getType(context);
        Log.i(">>amount", "onBindViewHolder: " + type);
        holder.tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, DthActivity.class);
                Prefes.saveValidity(operatorList.get(position).getRecharge_validity(), context);
                Prefes.saveDescription(operatorList.get(position).getRecharge_long_desc(), context);
                Prefes.saveTalktime(operatorList.get(position).getRecharge_talktime(), context);
                Log.i(">>amount", "onClick: " + operatorList.get(position).getRecharge_amount());
                EventBus.getDefault().postSticky(new PlanRechargeBus(operatorList.get(position).getRecharge_talktime(),
                        operatorList.get(position).getRecharge_amount(), operatorList.get(position).getRecharge_type(),
                        operatorList.get(position).getRecharge_validity()));
                intent.putExtra("amount", operatorList.get(position).getRecharge_amount());
                intent.putExtra("talkTime", operatorList.get(position).getRecharge_talktime());
                intent.putExtra("type", operatorList.get(position).getRecharge_type());
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ((Activity) context).finish();
            }
        });
    }

    Intent intent;

    @Override
    public int getItemCount() {
        return operatorList.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        NormalTextView validityTextView, plainDescTextView, plainAmount;
        LinearLayout layAmountClick;
        CardView tap;


        public MyViewHolder(View itemView) {
            super(itemView);
            validityTextView = (itemView).findViewById(R.id.txtTalkValidity);
            plainDescTextView = (itemView).findViewById(R.id.txtTalkDesc);
            plainAmount = (itemView).findViewById(R.id.txtPlainAmount);
            layAmountClick = (itemView).findViewById(R.id.layAmountClick);
            tap = (itemView).findViewById(R.id.tap);

        }
    }

}
