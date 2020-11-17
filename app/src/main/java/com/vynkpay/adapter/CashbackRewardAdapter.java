package com.vynkpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.models.CashbackRewardModel;

import java.util.ArrayList;

public class CashbackRewardAdapter extends RecyclerView.Adapter<CashbackRewardAdapter.Holder>{

    Context context;
    ArrayList<CashbackRewardModel> cashbackRewardModelArrayList;

    public CashbackRewardAdapter(Context context, ArrayList<CashbackRewardModel> cashbackRewardModelArrayList) {
        this.context = context;
        this.cashbackRewardModelArrayList = cashbackRewardModelArrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cashback_reward_item_layout_rcg, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.amountTV.setText(Functions.CURRENCY_SYMBOL+String.format("%.2f", Double.parseDouble(cashbackRewardModelArrayList.get(position).getAmount().replace(",",""))));
       // holder.balanceTV.setText(Functions.CURRENCY_SYMBOL+String.format("%.2f", Double.parseDouble(cashbackRewardModelArrayList.get(position).getBalance().replace(",",""))));
        holder.dateTV.setText(Functions.changeDateFormat(cashbackRewardModelArrayList.get(position).getCreated_date(),"yyyy-MM-dd hh:mm:ss","dd-MMM-yyyy hh:mm aa"));
        holder.titleTV.setText(cashbackRewardModelArrayList.get(position).getDetail());
        if (cashbackRewardModelArrayList.get(position).getStatus().equals("0")){
            holder.statusTV.setText("Confirmed");
            holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.green_status));
        }else {
            holder.statusTV.setText("Pending");
            holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.color_red));
        }
    }

    @Override
    public int getItemCount() {
        return cashbackRewardModelArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView titleTV, dateTV, amountTV, statusTV;
        public Holder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.titleTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            statusTV = itemView.findViewById(R.id.statusTV);
        }
    }
}
