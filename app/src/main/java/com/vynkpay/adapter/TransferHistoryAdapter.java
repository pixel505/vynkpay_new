package com.vynkpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.R;
import com.vynkpay.activity.activities.TokenHistoryModel;
import com.vynkpay.utils.Functions;

import java.util.ArrayList;

public class TransferHistoryAdapter extends RecyclerView.Adapter<TransferHistoryAdapter.Holder>{

    Context context;
    ArrayList<TokenHistoryModel> tokenHistoryModelArrayList;

    public TransferHistoryAdapter(Context context, ArrayList<TokenHistoryModel> tokenHistoryModelArrayList) {
        this.context = context;
        this.tokenHistoryModelArrayList = tokenHistoryModelArrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.token_transactions_history_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.txnTV.setText(tokenHistoryModelArrayList.get(position).getTrx_address());
        holder.dateTV.setText(Functions.changeDateFormat(tokenHistoryModelArrayList.get(position).getCreated_at(), "yyyy-MM-dd hh:mm:ss", "dd MMM, hh:mm:ss"));
        holder.tokenNameTV.setText(tokenHistoryModelArrayList.get(position).getTokenSymbol());
        holder.tokenTV.setText("+"+tokenHistoryModelArrayList.get(position).getToken_amount());
        holder.statusTV.setText(tokenHistoryModelArrayList.get(position).getStatus_txt());

        if (tokenHistoryModelArrayList.get(position).getStatus().equalsIgnoreCase("3")){
            holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            holder.imageIV.setImageResource(R.drawable.verified);
        }else {
            holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.color_red));
            holder.imageIV.setImageResource(R.drawable.notverified);
        }
    }

    @Override
    public int getItemCount() {
        return tokenHistoryModelArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txnTV, statusTV, tokenTV, tokenNameTV, dateTV;
        ImageView imageIV;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imageIV = itemView.findViewById(R.id.imageIV);
            txnTV = itemView.findViewById(R.id.txnTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            tokenTV = itemView.findViewById(R.id.tokenTV);
            tokenNameTV = itemView.findViewById(R.id.tokenNameTV);
            statusTV = itemView.findViewById(R.id.statusTV);
        }
    }
}
