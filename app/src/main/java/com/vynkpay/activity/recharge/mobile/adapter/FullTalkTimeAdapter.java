package com.vynkpay.activity.recharge.mobile.adapter;

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
import com.vynkpay.activity.recharge.datacard.activity.DataCardActivity;
import com.vynkpay.activity.recharge.mobile.activity.PostPaidActivity;
import com.vynkpay.activity.recharge.mobile.activity.PrepaidActivity;
import com.vynkpay.activity.recharge.mobile.events.PlanRechargeBus;
import com.vynkpay.activity.recharge.mobile.model.OnBottomReachedListener;
import com.vynkpay.activity.recharge.mobile.model.PlanModel;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;

public class FullTalkTimeAdapter extends RecyclerView.Adapter<FullTalkTimeAdapter.MyViewHolder> {
    private ArrayList<PlanModel> operatorList;
    Context context;
    OnBottomReachedListener onBottomReachedListener;

    public FullTalkTimeAdapter(Context context, ArrayList<PlanModel> data) {
        this.operatorList = data;
        this.context = context;

    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {

        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_plan_rcg, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    String type = "";

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (position == operatorList.size() - 1) {
            onBottomReachedListener.onBottomReached(position);
        }
        if (operatorList.get(position).getRecharge_talktime().isEmpty()) {
            holder.talkTimeTextView.setText("0");
        } else if (operatorList.get(position).getRecharge_talktime().equals("")) {
            holder.talkTimeTextView.setText("N.A");
        } else {
            holder.talkTimeTextView.setText(Functions.CURRENCY_SYMBOL_USER + operatorList.get(position).getRecharge_talktime());
        }
        holder.validityTextView.setText(operatorList.get(position).getRecharge_validity());
        holder.plainDescTextView.setText(operatorList.get(position).getRecharge_long_desc());
        holder.plainAmount.setText(Functions.CURRENCY_SYMBOL_USER + Double.parseDouble(operatorList.get(position).getRecharge_amount()));

        type = Prefes.getType(context);
        Log.i(">>amount", "onBindViewHolder: " + type);
        holder.tap.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View v) {
                switch (type) {
                    case "1":
                        intent = new Intent(context, PrepaidActivity.class);
                        break;
                    case "2":
                        intent = new Intent(context, PostPaidActivity.class);
                        break;
                    case "6":
                        intent = new Intent(context, DataCardActivity.class);
                        break;
                    case "7":
                        intent = new Intent(context, DataCardActivity.class);
                        break;
                }
                Prefes.saveDescription(operatorList.get(position).getRecharge_long_desc(), context);
                Prefes.saveValidity(operatorList.get(position).getRecharge_validity(), context);
                Prefes.saveTalktime(operatorList.get(position).getRecharge_talktime(), context);
                Log.i(">>amount", "onClick: " + operatorList.get(position).getRecharge_amount());
                EventBus.getDefault().postSticky(
                        new PlanRechargeBus(operatorList.get(position).getRecharge_talktime(),
                        operatorList.get(position).getRecharge_amount(), operatorList.get(position).getRecharge_type(),
                        operatorList.get(position).getRecharge_validity()));
               // intent.putExtra("amount", operatorList.get(position).getRecharge_amount());
              /*  intent.putExtra("amount", operatorList.get(position).getRecharge_amount());
                intent.putExtra("talkTime", operatorList.get(position).getRecharge_talktime());
                intent.putExtra("type", operatorList.get(position).getRecharge_type());*/
                ((Activity) context).finish();
            }
        });

        if (operatorList.get(position).getBusiness_rule().isEmpty()){
            holder.specialInstructionTV.setVisibility(View.GONE);
        }else {
            holder.specialInstructionTV.setVisibility(View.VISIBLE);
            holder.specialInstructionTV.setText(operatorList.get(position).getBusiness_rule()+"");
        }

    }



    @Override
    public int getItemCount() {
        return operatorList.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        NormalTextView talkTimeTextView, validityTextView, plainDescTextView, plainAmount, specialInstructionTV;
        LinearLayout layAmountClick;
        CardView tap;


        public MyViewHolder(View itemView) {
            super(itemView);
            talkTimeTextView = (itemView).findViewById(R.id.txtTalkTime);
            validityTextView = (itemView).findViewById(R.id.txtTalkValidity);
            plainDescTextView = (itemView).findViewById(R.id.txtTalkDesc);
            plainAmount = (itemView).findViewById(R.id.txtPlainAmount);
            specialInstructionTV = (itemView).findViewById(R.id.specialInstructionTV);
            layAmountClick = (itemView).findViewById(R.id.layAmountClick);
            tap = (itemView).findViewById(R.id.tap);

        }
    }

}
