package com.vynkpay.activity.activities.history.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vynkpay.activity.activities.history.HistoryDetailActivity;
import com.vynkpay.custom.NormalLightTextView;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.TransferMoneyModel;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;

import java.util.ArrayList;

public class HistoryRechargesAdapter extends RecyclerView.Adapter<HistoryRechargesAdapter.MyViewHolder> {
    ArrayList<?> serviceModelArrayList;
    Context context;
    int countReturn;
    String type;

    public HistoryRechargesAdapter(Context context, ArrayList<?> serviceModelArrayList, int countReturn, String type) {
        this.context = context;
        this.serviceModelArrayList = serviceModelArrayList;
        this.countReturn = countReturn;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_previous_recharge_rcg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if (serviceModelArrayList.get(position) instanceof TransferMoneyModel) {
            holder.tvAmount.setText(Functions.CURRENCY_SYMBOL + Double.parseDouble(((TransferMoneyModel) serviceModelArrayList.get(position)).getAmount()));
            if (((TransferMoneyModel) serviceModelArrayList.get(position)).getConn_type().equalsIgnoreCase(ApiParams.type_for_prepaid)
                    || ((TransferMoneyModel) serviceModelArrayList.get(position)).getConn_type().equalsIgnoreCase(ApiParams.type_for_prepaid)
                    || ((TransferMoneyModel) serviceModelArrayList.get(position)).getConn_type().equalsIgnoreCase(ApiParams.type_for_datacard_prepaid)
                    || ((TransferMoneyModel) serviceModelArrayList.get(position)).getConn_type().equalsIgnoreCase(ApiParams.type_for_datacard_postpaid)) {
                holder.tvMobileNumber.setText("Recharge of " + ((TransferMoneyModel) serviceModelArrayList.get(position)).getOperator());
            } else {
                holder.tvMobileNumber.setText("Bill payment of " + ((TransferMoneyModel) serviceModelArrayList.get(position)).getOperator());
            }

            holder.tvDateTime.setText(((TransferMoneyModel) serviceModelArrayList.get(position)).getDate());
            holder.txnID.setText("TXN ID - " + ((TransferMoneyModel) serviceModelArrayList.get(position)).getTcnId());
            if (((TransferMoneyModel) serviceModelArrayList.get(position)).getStatus().equalsIgnoreCase("1")) {
                holder.tvStatus.setText("Successful");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green_status));
            } else if (((TransferMoneyModel) serviceModelArrayList.get(position)).getStatus().equalsIgnoreCase("2")) {
                holder.tvStatus.setText("Failed");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_red));
            } else if (((TransferMoneyModel) serviceModelArrayList.get(position)).getStatus().equalsIgnoreCase("3")) {
                holder.tvStatus.setText("Pending");
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_red));
            }

            holder.tap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HistoryDetailActivity.class);
                    intent.putExtra("id", ((TransferMoneyModel) serviceModelArrayList.get(position)).getId());
                    intent.putExtra("txn", ((TransferMoneyModel) serviceModelArrayList.get(position)).getTcnId());
                    intent.putExtra("title", holder.tvMobileNumber.getText().toString());
                    context.startActivity(intent);
                }
            });
        }
        if (type.equals("recharge")) {
            holder.icon.setVisibility(View.GONE);
        } else {
            holder.icon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return serviceModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        NormalTextView tvAmount, tvStatus, tvMobileNumber;
        NormalLightTextView tvDateTime, txnID;
        CardView tap;
        LinearLayout icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvMobileNumber = itemView.findViewById(R.id.tvMobileNumber);
            tap = itemView.findViewById(R.id.tap);
            icon = itemView.findViewById(R.id.icon);
            txnID = itemView.findViewById(R.id.txnID);
        }
    }
}
