package com.vynkpay.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.models.WalletTransactionsModel;
import java.util.ArrayList;

public class WalletTransactionAdapter extends BaseAdapter {
    Context context;
    ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList;
    boolean showAll;

    public WalletTransactionAdapter(Context context, ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList, boolean showAll) {
        this.context = context;
        this.walletTransactionsModelArrayList = walletTransactionsModelArrayList;
        this.showAll = showAll;
    }

    @Override
    public int getCount() {
        if (showAll){
            return walletTransactionsModelArrayList.size();
        }else {
            if (walletTransactionsModelArrayList.size()>5){
                return 5;
            }else {
                return walletTransactionsModelArrayList.size();
            }
        }

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.transaction_item_layout, parent , false);
        TextView dateTV = convertView.findViewById(R.id.dateTV);
        TextView amountTV = convertView.findViewById(R.id.amountTV);
        TextView balanceTV = convertView.findViewById(R.id.balanceTV);
        TextView statusTV = convertView.findViewById(R.id.statusTV);
        TextView descriptionTV = convertView.findViewById(R.id.descriptionTV);
        LinearLayout itemLinear= convertView.findViewById(R.id.itemLinear);

        dateTV.setText(Functions.changeDateFormat(walletTransactionsModelArrayList.get(position).getCreated_date(),
                "yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy"));
        amountTV.setText(walletTransactionsModelArrayList.get(position).getP_amount());
        balanceTV.setText(walletTransactionsModelArrayList.get(position).getBalance());


        Log.d("tmcashtrrr", walletTransactionsModelArrayList.get(position).getStatus()+"//");

        switch (walletTransactionsModelArrayList.get(position).getStatus()){
            case "0":
                statusTV.setText(context.getString(R.string.confirmed));
                statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                break;
            case "2":
                statusTV.setText(context.getString(R.string.withdrawRequest));
                statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorYellow));
                break;
            case "3":
                statusTV.setText(context.getString(R.string.withdrawPaid));
                statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                break;
            case "4":
                statusTV.setText(context.getString(R.string.withdrawCancel));
                statusTV.setTextColor(ContextCompat.getColor(context, R.color.buttonOffer));
                break;
            case "5":
                statusTV.setText(context.getString(R.string.paidUser));
                statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                break;
            default:
                statusTV.setText(context.getString(R.string.confirmed));
                statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }

        itemLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        descriptionTV.setText(walletTransactionsModelArrayList.get(position).getProfit_type());

        return convertView;
    }

}
