package com.vynkpay.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.BankModel;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    ArrayList<?> historyList;
    Context context;
    String isFull;

    public HistoryAdapter(Context context, ArrayList<?> historyList, String isFull) {
        this.context = context;
        this.historyList = historyList;
        this.isFull = isFull;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_history_rcg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (historyList.get(position) instanceof BankModel) {
            holder.historyClubTime.setText(((BankModel) historyList.get(position)).getHistoryClubTime());
            holder.historyTitle.setText(((BankModel) historyList.get(position)).getHistoryTitle());
            holder.historyTime.setText(((BankModel) historyList.get(position)).getHistoryTime());
            holder.historyAddOrDeleted.setText(((BankModel) historyList.get(position)).getHistoryAddOrDeleted());

        }
    }

    @Override
    public int getItemCount() {
        return isFull.equals("0") ? historyList.size() >= 4  ? 4 : historyList.size() : historyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        NormalTextView historyClubTime, historyTitle, historyTime, historyAddOrDeleted;

        public MyViewHolder(View itemView) {
            super(itemView);
            historyClubTime = itemView.findViewById(R.id.historyClubTime);
            historyTitle = itemView.findViewById(R.id.historyTitle);
            historyTime = itemView.findViewById(R.id.historyTime);
            historyAddOrDeleted = itemView.findViewById(R.id.historyAddOrDeleted);
        }
    }
}
