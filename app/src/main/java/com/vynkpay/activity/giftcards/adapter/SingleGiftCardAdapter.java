package com.vynkpay.activity.giftcards.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.giftcards.activity.GiftCardDetailActivity;
import com.vynkpay.activity.giftcards.model.GiftListModel;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SingleGiftCardAdapter extends RecyclerView.Adapter<SingleGiftCardAdapter.MyViewHolder> {
    Context context;
    ArrayList<GiftListModel> giftCardModelArrayList;

    public SingleGiftCardAdapter(Context context, ArrayList<GiftListModel> giftCardModelArrayList) {
        this.context = context;
        this.giftCardModelArrayList = giftCardModelArrayList;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_single_gift_rcg, parent, false);
        MyViewHolder m = new MyViewHolder(v);
        return m;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        onViewAttachedToWindow(holder);
        if (!giftCardModelArrayList.get(position).getImage().isEmpty()) {
            Picasso.get().load(giftCardModelArrayList.get(position).getImage()).into(holder.giftImage);
        } else {
            Picasso.get().load(R.drawable.app_icon).into(holder.giftImage);
        }
        holder.giftTitle.setText(giftCardModelArrayList.get(position).getCategoryName());
        holder.giftDescription.setText(giftCardModelArrayList.get(position).getCategoryName());
        //holder.giftButton.setText(Functions.CURRENCY_SYMBOL + giftCardModelArrayList.get(position).getCategoryMin() + "-" + Functions.CURRENCY_SYMBOL + giftCardModelArrayList.get(position).getCategoryMax());
        holder.giftButton.setText("Starting from " + Functions.CURRENCY_SYMBOL + giftCardModelArrayList.get(position).getCategoryMin());
        holder.gitTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GiftCardDetailActivity.class);
                intent.putExtra("id", giftCardModelArrayList.get(position).getCategoryId());
                intent.putExtra("name", giftCardModelArrayList.get(position).getCategoryName());
                intent.putExtra("image", giftCardModelArrayList.get(position).getImage().isEmpty() ? "" : giftCardModelArrayList.get(position).getImage());
                ((Activity) context).startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return giftCardModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        NormalTextView giftTitle, giftDescription;
        ImageView giftImage;
        LinearLayout gitTap;
        NormalButton giftButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            giftTitle = itemView.findViewById(R.id.giftTitle);
            giftDescription = itemView.findViewById(R.id.giftDescription);
            giftImage = itemView.findViewById(R.id.giftImage);
            gitTap = itemView.findViewById(R.id.gitTap);
            giftButton = itemView.findViewById(R.id.giftButton);

        }
    }
}
