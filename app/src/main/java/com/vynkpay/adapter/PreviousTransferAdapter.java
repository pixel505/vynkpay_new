package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.AddMoneyActivity;
import com.vynkpay.custom.NormalLightTextView;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.events.UpdateWalletTransferFields;
import com.vynkpay.models.TransferMoneyModel;
import com.vynkpay.utils.ApiParams;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class PreviousTransferAdapter extends RecyclerView.Adapter<PreviousTransferAdapter.MyViewHolder> {
    ArrayList<?> serviceModelArrayList;
    Context context;
    int countReturn;
    String what;

    public PreviousTransferAdapter(Context context, ArrayList<?> serviceModelArrayList, int countReturn, String what) {
        this.context = context;
        this.serviceModelArrayList = serviceModelArrayList;
        this.countReturn = countReturn;
        this.what = what;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_previous_recharge_rcg, parent, false);
        return new MyViewHolder(v);
    }

    Object f;

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if (serviceModelArrayList.size() > 0){

            if (serviceModelArrayList.get(position) instanceof TransferMoneyModel) {
                holder.tvAmount.setText(Functions.CURRENCY_SYMBOL + Double.parseDouble(((TransferMoneyModel) serviceModelArrayList.get(position)).getAmount()));
                holder.tvDateTime.setText(((TransferMoneyModel) serviceModelArrayList.get(position)).getDate());

                holder.txnID.setText("TXN ID : " + ((TransferMoneyModel) serviceModelArrayList.get(position)).getTxn());
                switch (((TransferMoneyModel) serviceModelArrayList.get(position)).getProvider()) {

                    // type for wallet related transactions
                    case "4":
                        holder.tvAmount.setVisibility(View.GONE);
                        holder.tvStatus.setVisibility(View.GONE);
                        holder.tvMobileNumber.setText("VynkPay wallet received payment of " + Functions.CURRENCY_SYMBOL + " " + Double.parseDouble(((TransferMoneyModel) serviceModelArrayList.get(position)).getAmount()));
                        break;

                    // type for user to user  related transactions
                    case "2":
                        holder.tvAmount.setVisibility(View.VISIBLE);
                        holder.tvStatus.setVisibility(View.VISIBLE);
                        holder.tvStatus.setText(((TransferMoneyModel) serviceModelArrayList.get(position)).getSentOrRecieve());
                        holder.tvMobileNumber.setText("Mobile No. : " + ((TransferMoneyModel) serviceModelArrayList.get(position)).getMobile());
                        break;

                    // type for theme park
                    case "7":
                        holder.tvAmount.setVisibility(View.VISIBLE);
                        holder.tvStatus.setVisibility(View.VISIBLE);
                        holder.tvMobileNumber.setVisibility(View.VISIBLE);
                        holder.tvMobileNumber.setText("Payment of theme park");
                        if (((TransferMoneyModel) serviceModelArrayList.get(position)).getStatus().equalsIgnoreCase("1")) {
                            holder.tvStatus.setText("Successful");
                            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green_status));
                        } else if (((TransferMoneyModel) serviceModelArrayList.get(position)).getStatus().equalsIgnoreCase("2")) {
                            holder.tvStatus.setText("Failed");
                            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_red));
                        } else if (((TransferMoneyModel) serviceModelArrayList.get(position)).getStatus().equalsIgnoreCase("3")) {
                            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.buttonText));
                            holder.tvStatus.setText("Pending");
                        }
                        break;

                    //type for gift card
                    case "6":
                        holder.tvAmount.setVisibility(View.VISIBLE);
                        holder.tvStatus.setVisibility(View.VISIBLE);
                        holder.tvMobileNumber.setVisibility(View.VISIBLE);
                        holder.tvMobileNumber.setText("Payment of gift card");
                        if (((TransferMoneyModel) serviceModelArrayList.get(position)).getStatus().equalsIgnoreCase("1")) {
                            holder.tvStatus.setText("Successful");
                            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green_status));
                        } else if (((TransferMoneyModel) serviceModelArrayList.get(position)).getStatus().equalsIgnoreCase("2")) {
                            holder.tvStatus.setText("Failed");
                            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_red));
                        } else if (((TransferMoneyModel) serviceModelArrayList.get(position)).getStatus().equalsIgnoreCase("3")) {
                            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.buttonText));
                            holder.tvStatus.setText("Pending");
                        }
                        break;
                }
                holder.tap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (what.equals("WalletActivity")) {
                            Intent intent = new Intent(context, AddMoneyActivity.class);
                            intent.putExtra(ApiParams.amount, ((TransferMoneyModel) serviceModelArrayList.get(position)).getAmount());
                            context.startActivity(intent);
                        } else if (what.equals("WalletTransfer")) {
                            EventBus.getDefault().postSticky(new UpdateWalletTransferFields(((TransferMoneyModel) serviceModelArrayList.get(position)).getAmount(), ((TransferMoneyModel) serviceModelArrayList.get(position)).getMobile()));
                        }
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return countReturn == 0 ? 1 : serviceModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        NormalTextView tvAmount, tvStatus, tvMobileNumber, date;
        NormalLightTextView tvDateTime, txnID;
        CardView tap;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            txnID = itemView.findViewById(R.id.txnID);
            tvMobileNumber = itemView.findViewById(R.id.tvMobileNumber);
            date = itemView.findViewById(R.id.created_at);
            tap = itemView.findViewById(R.id.tap);
        }
    }
}
