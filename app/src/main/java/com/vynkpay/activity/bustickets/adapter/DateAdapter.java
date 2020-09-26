package com.vynkpay.activity.bustickets.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;

import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    Context context;
    ArrayList<String> dates;
    ClickListnerForRecyclerView clickListnerForRecyclerView;

    public DateAdapter(Context context, ArrayList<String> dates) {
        this.dates = dates;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflator_for_date_rcg, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.txt_date.setText(dates.get(position).toString());

        if (position == 3) {
            holder.txt_date1.setTextColor(context.getResources().getColor(R.color.color_two));
            holder.txt_date.setTextColor(context.getResources().getColor(R.color.color_two));
            holder.lay_date.setBackgroundColor(context.getResources().getColor(R.color.buttonText));
        }

        holder.lay_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListnerForRecyclerView != null) {
                    clickListnerForRecyclerView.layoutClicked(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lay_date;
        TextView txt_date, txt_date1;

        public ViewHolder(View itemView) {
            super(itemView);

            lay_date = (LinearLayout) itemView.findViewById(R.id.lay_date);

            txt_date = (TextView) itemView.findViewById(R.id.txtview_date);
            txt_date1 = (TextView) itemView.findViewById(R.id.txtview_date1);
        }
    }

    private interface ClickListnerForRecyclerView {
        void layoutClicked(View view, int position);
    }

}
