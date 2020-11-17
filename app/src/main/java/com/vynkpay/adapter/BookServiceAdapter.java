package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.vynkpay.R;
import com.vynkpay.activity.bustickets.activity.BusActivity;
import com.vynkpay.activity.giftcards.activity.GiftCardActivity;
import com.vynkpay.activity.themepark.activity.ThemeParkCategoryActivity;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.ServiceModel;
import com.vynkpay.utils.M;

import java.util.ArrayList;

public class BookServiceAdapter extends RecyclerView.Adapter<BookServiceAdapter.MyViewHolder> {
    ArrayList<?> serviceModelArrayList;
    Context context;
    Intent intent;

    public BookServiceAdapter(Context context, ArrayList<?> serviceModelArrayList) {
        this.context = context;
        this.serviceModelArrayList = serviceModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_recharge_service_rcg, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (serviceModelArrayList.get(position) instanceof ServiceModel) {
            holder.rechargeText.setText(((ServiceModel) serviceModelArrayList.get(position)).getServiceName());
            holder.rechargeIcon.setBackgroundResource(((ServiceModel) serviceModelArrayList.get(position)).getServiceIconInt());
            holder.tap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            M.reset(context);
                            intent = new Intent(context, BusActivity.class);
                            context.startActivity(intent);
                            break;

                        case 6:
                            M.reset(context);
                            intent = new Intent(context, GiftCardActivity.class);
                            context.startActivity(intent);
                            break;

                        case 4:
                            M.reset(context);
                            intent = new Intent(context, ThemeParkCategoryActivity.class);
                            context.startActivity(intent);
                            break;

                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return serviceModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView rechargeIcon;
        NormalTextView rechargeText;
        LinearLayout tap;

        public MyViewHolder(View itemView) {
            super(itemView);
            rechargeIcon = itemView.findViewById(R.id.rechargeIcon);
            rechargeText = itemView.findViewById(R.id.rechargeText);
            tap = itemView.findViewById(R.id.tap);
        }
    }
}
