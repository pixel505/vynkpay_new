package com.vynkpay.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.R;
import com.vynkpay.activity.activities.CancelButtonIntereface;
import com.vynkpay.activity.activities.TokenHistoryModel;
import com.vynkpay.custom.BoldButton;
import com.vynkpay.custom.CustomRadioButton;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;

import java.util.ArrayList;

public class TransferHistoryAdapter extends RecyclerView.Adapter<TransferHistoryAdapter.Holder>{

    Context context;
    ArrayList<TokenHistoryModel> tokenHistoryModelArrayList;
    CancelButtonIntereface cancelButtonIntereface;

    public TransferHistoryAdapter(Context context, ArrayList<TokenHistoryModel> tokenHistoryModelArrayList, CancelButtonIntereface cancelButtonIntereface) {
        this.context = context;
        this.tokenHistoryModelArrayList = tokenHistoryModelArrayList;
        this.cancelButtonIntereface = cancelButtonIntereface;
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
        }else if (tokenHistoryModelArrayList.get(position).getStatus().equalsIgnoreCase("0")){
            holder.imageIV.setImageResource(R.drawable.ic_warning);
        }else {
            holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.color_red));
            holder.imageIV.setImageResource(R.drawable.notverified);
        }

        if (tokenHistoryModelArrayList.get(position).getStatus().equalsIgnoreCase("0")){
            holder.cancelButton.setVisibility(View.VISIBLE);
            holder.statusTV.setVisibility(View.GONE);
        }else {
            holder.cancelButton.setVisibility(View.GONE);
            holder.statusTV.setVisibility(View.VISIBLE);
        }

        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(context, new M.OnPopClickListener() {
                    @Override
                    public void onClick() {
                        if (cancelButtonIntereface!=null){
                            cancelButtonIntereface.onClickCancel(tokenHistoryModelArrayList.get(position).getId());
                        }
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return tokenHistoryModelArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txnTV, statusTV, tokenTV, tokenNameTV, dateTV;
        ImageView imageIV;
        BoldButton cancelButton;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imageIV = itemView.findViewById(R.id.imageIV);
            txnTV = itemView.findViewById(R.id.txnTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            tokenTV = itemView.findViewById(R.id.tokenTV);
            tokenNameTV = itemView.findViewById(R.id.tokenNameTV);
            statusTV = itemView.findViewById(R.id.statusTV);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }


    public static void showPopUp(Context context, M.OnPopClickListener listener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to cancel request?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener !=null){
                    listener.onClick();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        try {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
