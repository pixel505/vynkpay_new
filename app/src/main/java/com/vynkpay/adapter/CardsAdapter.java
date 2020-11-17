package com.vynkpay.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.BankModel;
import java.util.ArrayList;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {
    ArrayList<?> notificationListData;
    Context context;

    public CardsAdapter(Context context, ArrayList<?> notificationListData) {
        this.context = context;
        this.notificationListData = notificationListData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_card_rcg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (notificationListData.get(position) instanceof BankModel) {
            holder.bankCardNumber.setText(((BankModel) notificationListData.get(position)).getBankAccountNumber());
            holder.bankName.setText(((BankModel) notificationListData.get(position)).getBankName());
        }
    }

    @Override
    public int getItemCount() {
        return notificationListData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        NormalTextView bankName, bankCardNumber;
        LinearLayout bankMoreOption;

        public MyViewHolder(View itemView) {
            super(itemView);
            bankMoreOption = itemView.findViewById(R.id.bankMoreOption);
            bankCardNumber = itemView.findViewById(R.id.bankCardNumber);
            bankName = itemView.findViewById(R.id.bankName);
        }
    }
}
