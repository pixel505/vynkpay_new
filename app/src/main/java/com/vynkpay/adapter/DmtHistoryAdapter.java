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
import com.vynkpay.models.DmtHistoryModel;
import com.vynkpay.utils.M;

import java.util.ArrayList;

public class DmtHistoryAdapter extends RecyclerView.Adapter<DmtHistoryAdapter.Holder>{

    Context context;
    ArrayList<DmtHistoryModel> dmtHistoryModelArrayList;

    public DmtHistoryAdapter(Context context, ArrayList<DmtHistoryModel> dmtHistoryModelArrayList) {
        this.context = context;
        this.dmtHistoryModelArrayList = dmtHistoryModelArrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cashback_reward_item_layout_rcg, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.titleTV.setText("Bank amount transfer");
        Double amount=Double.parseDouble(dmtHistoryModelArrayList.get(position).getAmount().replace(",", ""));
        holder.amountTV.setText(Functions.CURRENCY_SYMBOL+String.format("%.2f", amount));
        if (dmtHistoryModelArrayList.get(position).getStatus().equals("0")){
            holder.statusTV.setText("Pending");
            holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.color_text));
        }else {
            holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.green_status));
            holder.statusTV.setText("Confirmed");
        }

        //2020-06-08 13:16:19
        holder.dateTV.setText(M.changeDateFormat(dmtHistoryModelArrayList.get(position).getCreated_at(), "yyyy-MM-dd hh:mm:ss", "dd-MMM-yyyy hh:mm aa"));
    }

    @Override
    public int getItemCount() {
        return dmtHistoryModelArrayList.size();
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
