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
import com.vynkpay.models.WalletTransactionsModel;
import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.Holder>{

    Context context;
    ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList;

    public TransactionsAdapter(Context context, ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList) {
        this.context = context;
        this.walletTransactionsModelArrayList = walletTransactionsModelArrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.transaction_item_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.dateTV.setText(Functions.changeDateFormat(walletTransactionsModelArrayList.get(position).getCreated_date(),
                "yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy"));
        holder.amountTV.setText(walletTransactionsModelArrayList.get(position).getP_amount());
        holder.balanceTV.setText(walletTransactionsModelArrayList.get(position).getBalance());
        holder.descriptionTV.setText(walletTransactionsModelArrayList.get(position).getName());
        if (walletTransactionsModelArrayList.get(position).getPaid_status().equals("1")){
            holder.statusTV.setText(context.getString(R.string.confirmed));
            holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }else {
            holder.statusTV.setText(context.getString(R.string.pending));
            holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorYellow));
        }
    }

    @Override
    public int getItemCount() {
        if (walletTransactionsModelArrayList.size()>0){
            return walletTransactionsModelArrayList.size();
        }else {
            return 10;
        }

    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView dateTV, amountTV, balanceTV, statusTV, descriptionTV;
        public Holder(@NonNull View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.dateTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            balanceTV = itemView.findViewById(R.id.balanceTV);
            statusTV = itemView.findViewById(R.id.statusTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
        }
    }
}
