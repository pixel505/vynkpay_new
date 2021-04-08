package com.vynkpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.R;
import com.vynkpay.activity.ChainTransactionModel;
import com.vynkpay.utils.Functions;
import java.util.ArrayList;

public class TokenTransAdapter extends RecyclerView.Adapter<TokenTransAdapter.Holder>{

    ArrayList<ChainTransactionModel> chainTransactionModelArrayList;
    Context context;

    public TokenTransAdapter(ArrayList<ChainTransactionModel> chainTransactionModelArrayList, Context context) {
        this.chainTransactionModelArrayList = chainTransactionModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.token_transactions_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.priceTV.setText(Functions.CURRENCY_SYMBOL+chainTransactionModelArrayList.get(position).getCoin_price());
        holder.txnID.setText(chainTransactionModelArrayList.get(position).getInvoice_number());
        holder.dateTV.setText(Functions.changeDateFormat(chainTransactionModelArrayList.get(position).getCreated_date(), "yyyy-MM-dd hh:mm:ss", "dd MMM, hh:mm:ss"));
        holder.tokenAmount.setText("+"+chainTransactionModelArrayList.get(position).getTotal_coin());
        holder.tokenName.setText("VYNC");
        if (chainTransactionModelArrayList.get(position).getStatus().equalsIgnoreCase("1")){
            holder.iconIV.setImageResource(R.drawable.verified);
        }else {
            holder.iconIV.setImageResource(R.drawable.notverified);
        }
    }

    @Override
    public int getItemCount() {
        return chainTransactionModelArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txnID, dateTV, tokenAmount, tokenName, priceTV;
        ImageView iconIV;
        public Holder(@NonNull View itemView) {
            super(itemView);
            iconIV = itemView.findViewById(R.id.imageIV);
            txnID = itemView.findViewById(R.id.txnTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            tokenAmount = itemView.findViewById(R.id.tokenTV);
            tokenName = itemView.findViewById(R.id.tokenNameTV);
            priceTV = itemView.findViewById(R.id.amountTV);
        }
    }
}
