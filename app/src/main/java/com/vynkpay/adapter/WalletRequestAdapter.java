package com.vynkpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.models.WithdrawalRequestModel;
import com.vynkpay.utils.Functions;

import java.util.ArrayList;

public class WalletRequestAdapter extends RecyclerView.Adapter<WalletRequestAdapter.Holder> {
    Context context;
    ArrayList<WithdrawalRequestModel> withdrawalRequestModelArrayList;

    public WalletRequestAdapter(Context context, ArrayList<WithdrawalRequestModel> withdrawalRequestModelArrayList) {
        this.context = context;
        this.withdrawalRequestModelArrayList = withdrawalRequestModelArrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallet_request_item_layout, parent ,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.dateTV.setText(Functions.changeDateFormat(withdrawalRequestModelArrayList.get(position).getCreated_date(),
                "yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy"));

        holder.amountTV.setText(withdrawalRequestModelArrayList.get(position).getAmount());
        holder.idTV.setText("#"+withdrawalRequestModelArrayList.get(position).getInvoice_number());

        if (withdrawalRequestModelArrayList.get(position).getIsactive().equals("1")){
            holder.statusTV.setText(context.getString(R.string.requested));
            holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorYellow));
        }else if (withdrawalRequestModelArrayList.get(position).getIsactive().equals("4")){
            holder.statusTV.setText(context.getString(R.string.paid));
            holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
        }else if (withdrawalRequestModelArrayList.get(position).getIsactive().equals("5")){
            holder.statusTV.setText(context.getString(R.string.canceled));
            holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.buttonOffer));
        }

        if (withdrawalRequestModelArrayList.get(position).getMode().equals("0")){
            holder.modeTV.setText("IMPS");
        }else if (withdrawalRequestModelArrayList.get(position).getMode().equals("1")){
            holder.modeTV.setText("NEFT");
        }else if (withdrawalRequestModelArrayList.get(position).getMode().equals("2")){
            holder.modeTV.setText("BTC");
        }

        holder.bankTV.setText(withdrawalRequestModelArrayList.get(position).getBank_name());
        holder.nameTV.setText(withdrawalRequestModelArrayList.get(position).getName_in_bank());
        holder.ifscTV.setText(withdrawalRequestModelArrayList.get(position).getIfsc_code());

        String mask = withdrawalRequestModelArrayList.get(position).getAccount_number().replaceAll("\\w(?=\\w{4})", "X");
        holder.accountNumberTV.setText(mask);

        holder.serviceFeesTV.setText(withdrawalRequestModelArrayList.get(position).getAdmin_chrg_amount());
        holder.tdsFeesTV.setText(withdrawalRequestModelArrayList.get(position).getTds_amount());
        holder.impsFeesTV.setText(withdrawalRequestModelArrayList.get(position).getOther_fee_amount());


        if (withdrawalRequestModelArrayList.get(position).isClicked()){
            holder.detailLinear.setVisibility(View.VISIBLE);
        }else {
            holder.detailLinear.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (withdrawalRequestModelArrayList.get(position).isClicked()){
                    holder.detailLinear.setVisibility(View.GONE);
                    withdrawalRequestModelArrayList.get(position).setClicked(false);
                }else {
                    holder.detailLinear.setVisibility(View.VISIBLE);
                    withdrawalRequestModelArrayList.get(position).setClicked(true);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return withdrawalRequestModelArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView dateTV ;
        TextView amountTV;
        TextView idTV;
        TextView statusTV;
        TextView modeTV;
        TextView bankTV;
        TextView nameTV;
        TextView accountNumberTV;
        TextView ifscTV;
        TextView serviceFeesTV;
        TextView tdsFeesTV;
        TextView impsFeesTV;
        LinearLayout detailLinear;

        public Holder(@NonNull View itemView) {
            super(itemView);
             dateTV = itemView.findViewById(R.id.dateTV);
             amountTV = itemView.findViewById(R.id.amountTV);
             idTV = itemView.findViewById(R.id.idTV);
             statusTV = itemView.findViewById(R.id.statusTV);

             modeTV = itemView.findViewById(R.id.modeTV);
             bankTV = itemView.findViewById(R.id.bankNameTV);
             nameTV = itemView.findViewById(R.id.accountHolderNameTV);
             accountNumberTV = itemView.findViewById(R.id.accountNumberTV);
             ifscTV = itemView.findViewById(R.id.ifscTV);

             serviceFeesTV = itemView.findViewById(R.id.serviceFeesTV);
             tdsFeesTV = itemView.findViewById(R.id.tdsFeesTV);
             impsFeesTV = itemView.findViewById(R.id.impsFeesTV);

             detailLinear = itemView.findViewById(R.id.detailLinear);
        }
    }
}
