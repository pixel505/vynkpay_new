package com.vynkpay.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.R;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.utils.Functions;
import java.util.ArrayList;

public class AllTransactionAdapter extends RecyclerView.Adapter<AllTransactionAdapter.Holder>{

    Context context;
    ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList;

    public AllTransactionAdapter(Context context, ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList) {
        this.context = context;
        this.walletTransactionsModelArrayList = walletTransactionsModelArrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_item_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.dateTV.setText(Functions.changeDateFormat(walletTransactionsModelArrayList.get(position).getCreated_date(),
                "yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy"));
        holder.amountTV.setText(Functions.CURRENCY_SYMBOL+walletTransactionsModelArrayList.get(position).getP_amount());
        holder.balanceTV.setText(Functions.CURRENCY_SYMBOL+walletTransactionsModelArrayList.get(position).getBalance());


        Log.d("tmcashtrrr", walletTransactionsModelArrayList.get(position).getStatus()+"//");

        switch (walletTransactionsModelArrayList.get(position).getStatus()){
            case "0":
                holder.statusTV.setText(context.getString(R.string.confirmed));
                holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                break;
            case "2":
                holder.statusTV.setText(context.getString(R.string.withdrawRequest));
                holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorYellow));
                break;
            case "3":
                holder.statusTV.setText(context.getString(R.string.withdrawPaid));
                holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                break;
            case "4":
                holder.statusTV.setText(context.getString(R.string.withdrawCancel));
                holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.buttonOffer));
                break;
            case "5":
                holder.statusTV.setText(context.getString(R.string.paidUser));
                holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                break;
            default:
                holder.statusTV.setText(context.getString(R.string.confirmed));
                holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }

        holder.itemLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.descriptionTV.setText(walletTransactionsModelArrayList.get(position).getProfit_type());
    }

    @Override
    public int getItemCount() {
        return walletTransactionsModelArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView dateTV;
        TextView amountTV;
        TextView balanceTV;
        TextView statusTV;
        TextView descriptionTV ;
        LinearLayout itemLinear;
        public Holder(@NonNull View convertView) {
            super(convertView);
            dateTV = convertView.findViewById(R.id.dateTV);
            amountTV = convertView.findViewById(R.id.amountTV);
            balanceTV = convertView.findViewById(R.id.balanceTV);
            statusTV = convertView.findViewById(R.id.statusTV);
            descriptionTV = convertView.findViewById(R.id.descriptionTV);
            itemLinear= convertView.findViewById(R.id.itemLinear);
        }
    }
}
