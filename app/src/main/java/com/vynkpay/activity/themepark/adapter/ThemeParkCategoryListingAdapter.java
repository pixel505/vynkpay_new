package com.vynkpay.activity.themepark.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.vynkpay.R;
import com.vynkpay.activity.themepark.activity.ThemeParkDetailActivity;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.GiftCardListModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ThemeParkCategoryListingAdapter extends RecyclerView.Adapter<ThemeParkCategoryListingAdapter.MyViewHolder> {
    Context context;
    ArrayList<GiftCardListModel> giftCardModelArrayList;

    public ThemeParkCategoryListingAdapter(Context context, ArrayList<GiftCardListModel> giftCardModelArrayList) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_gift_card_single_view_rcg, parent, false);
        MyViewHolder m = new MyViewHolder(v);
        return m;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        onViewAttachedToWindow(holder);
        if (!giftCardModelArrayList.get(position).getImage().isEmpty()) {
            Picasso.get().load(giftCardModelArrayList.get(position).getImage()).error(R.drawable.logo).into(holder.giftImage);
        } else {
            Picasso.get().load(R.drawable.logo).into(holder.giftImage);
        }
        holder.giftTitle.setText(giftCardModelArrayList.get(position).getCategoryName());
        holder.giftTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ThemeParkDetailActivity.class);
                intent.putExtra("id", giftCardModelArrayList.get(position).getCategoryId());
                intent.putExtra("name", giftCardModelArrayList.get(position).getCategoryName());
                intent.putExtra("image", giftCardModelArrayList.get(position).getImage().isEmpty() ? "" : giftCardModelArrayList.get(position).getImage());
                ((Activity) context).startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return giftCardModelArrayList.size() < 4 ? giftCardModelArrayList.size() : 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        NormalTextView giftTitle, giftDescription;
        ImageView giftImage;
        CardView giftTap;

        public MyViewHolder(View itemView) {
            super(itemView);
            giftTitle = itemView.findViewById(R.id.giftTitle);
            giftDescription = itemView.findViewById(R.id.giftDescription);
            giftTap = itemView.findViewById(R.id.giftTap);
            giftImage = itemView.findViewById(R.id.giftImage);

        }
    }
}
