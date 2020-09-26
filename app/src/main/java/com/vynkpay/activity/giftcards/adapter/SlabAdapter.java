package com.vynkpay.activity.giftcards.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.giftcards.event.SlabPriceEvent;
import com.vynkpay.custom.NormalTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SlabAdapter extends RecyclerView.Adapter<SlabAdapter.MyViewHolder> {
    Context context;

    private int lastPositionClicked = -1;
    private List<String> giftCardModelArrayList;

    public SlabAdapter(Context context, List<String> giftCardModelArrayList) {
        this.context = context;
        this.giftCardModelArrayList = giftCardModelArrayList;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_slab_rcg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.tvSlabPrice.setText(Functions.CURRENCY_SYMBOL + " " + giftCardModelArrayList.get(position));
        holder.llSlabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPositionClicked = position;
                EventBus.getDefault().postSticky(new SlabPriceEvent(giftCardModelArrayList.get(position)));
                notifyDataSetChanged();
            }
        });
        if (position == lastPositionClicked) {
            holder.tvSlabPrice.setTextColor(context.getResources().getColor(R.color.white));
            holder.llSlabBack.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.round_all));
        } else {
            holder.tvSlabPrice.setTextColor(context.getResources().getColor(R.color.black));
            holder.llSlabBack.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.stroke_all));
        }
    }


    @Override
    public int getItemCount() {
        return giftCardModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llSlabBack;
        NormalTextView tvSlabPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            llSlabBack = itemView.findViewById(R.id.cvSlabBack);
            tvSlabPrice = itemView.findViewById(R.id.tvSlabPrice);
        }
    }
}
