package com.vynkpay.activity.bustickets.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;

import java.util.ArrayList;

public class RecentTicketsAdapter extends RecyclerView.Adapter<RecentTicketsAdapter.ViewHolder> {
    Context context;
    ArrayList<String> dates;

    public RecentTicketsAdapter(Context context, ArrayList<String> dates) {
        this.dates = dates;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflator_for_recent_searches_rcg, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);


        }
    }
}
