package com.vynkpay.activity.themepark.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.themepark.event.IsAgeLimitSelectedEvent;
import com.vynkpay.activity.themepark.event.ThemeParkCategoryTicketEvent;
import com.vynkpay.activity.themepark.event.ThemeParkQuantityEvent;
import com.vynkpay.activity.themepark.model.ThemeParkCategoryTicketModel;
import com.vynkpay.custom.NormalTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ThemeParkTicketSelectionAdapter extends RecyclerView.Adapter<ThemeParkTicketSelectionAdapter.MyViewHolder> {
    private Context context;
    String quantity = "1";
    private ArrayList<ThemeParkCategoryTicketModel> giftCardModelArrayList;
    ThemeParkTicketAdapter themeParkTicketAdapter;

    public ThemeParkTicketSelectionAdapter(Context context, ArrayList<ThemeParkCategoryTicketModel> giftCardModelArrayList) {
        this.context = context;
        this.giftCardModelArrayList = giftCardModelArrayList;
        themeParkTicketAdapter = new ThemeParkTicketAdapter();
    }


    public ThemeParkTicketSelectionAdapter() {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_single_theme_park_category_rcg, parent, false);
        MyViewHolder m = new MyViewHolder(v);
        return m;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        onViewAttachedToWindow(holder);

        holder.perPerson.setText("Age Limit : " + giftCardModelArrayList.get(position).getTiketCategoryMinAge() + "yrs-" + giftCardModelArrayList.get(position).getTiketCategoryMaxAge() + "yrs");
        holder.ticketPrice.setText(Functions.CURRENCY_SYMBOL + "" + giftCardModelArrayList.get(position).getTiketCategoryPrice());
        holder.ageLimit.setText(giftCardModelArrayList.get(position).getTicketCategoryName());
        holder.ticketDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Integer.valueOf(holder.ticketQuantity.getText().toString()) == 1)) {
                    int tempVar = Integer.valueOf(holder.ticketQuantity.getText().toString());
                    tempVar--;
                    holder.ticketQuantity.setText(tempVar + "");
                    quantity = holder.ticketQuantity.getText().toString();
                    calculatedPrice = (Integer.parseInt(quantity) * Float.parseFloat(giftCardModelArrayList.get(position).getTiketCategoryPrice()));
                    EventBus.getDefault().post(new ThemeParkQuantityEvent(quantity
                            , calculatedPrice, Float.parseFloat(giftCardModelArrayList.get(position).getTiketCategoryPrice())));
                }
            }
        });
        holder.ticketIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Integer.valueOf(holder.ticketQuantity.getText().toString()) >= Integer.valueOf(giftCardModelArrayList.get(position).getTiketCategoryMax()))) {
                    int tempVar = Integer.valueOf(holder.ticketQuantity.getText().toString());
                    tempVar++;
                    holder.ticketQuantity.setText(tempVar + "");
                    quantity = holder.ticketQuantity.getText().toString();
                    calculatedPrice = (Integer.parseInt(quantity) * Float.parseFloat(giftCardModelArrayList.get(position).getTiketCategoryPrice()));
                    EventBus.getDefault().post(new ThemeParkQuantityEvent(quantity
                            , calculatedPrice, Float.parseFloat(giftCardModelArrayList.get(position).getTiketCategoryPrice())));
                } else {
                    Toast.makeText(context, "You cannot book more than " + giftCardModelArrayList.get(position).getTiketCategoryMax() + " tickets", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.ageLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPositionClicked = position;
                isAgeLimitSelected = true;
                notifyDataSetChanged();
            }
        });

        if (lastPositionClicked == position) {
            holder.ageLimit.setChecked(true);
            isAgeLimitSelected = true;
            calculatedPrice = (Integer.parseInt(quantity) * Float.parseFloat(giftCardModelArrayList.get(position).getTiketCategoryPrice()));
            EventBus.getDefault().post(new ThemeParkCategoryTicketEvent(giftCardModelArrayList.get(position)));
            EventBus.getDefault().post(new IsAgeLimitSelectedEvent(isAgeLimitSelected));
            EventBus.getDefault().post(new ThemeParkQuantityEvent(quantity
                    , calculatedPrice, Float.parseFloat(giftCardModelArrayList.get(position).getTiketCategoryPrice())));
        } else {
            holder.ageLimit.setChecked(false);
            isAgeLimitSelected = false;
        }
    }

    float calculatedPrice;
    public boolean isAgeLimitSelected = false;
    private int lastPositionClicked = -1;

    public void reset() {
        lastPositionClicked = -1;
        EventBus.getDefault().post(new IsAgeLimitSelectedEvent(false));
        ThemeParkTicketSelectionAdapter.this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return giftCardModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        NormalTextView ticketPrice, perPerson, ticketQuantity;
        RadioButton ageLimit;
        RecyclerView subCategoryRecyclerView;
        LinearLayout ticketIncrement, ticketDecrement;

        public MyViewHolder(View itemView) {
            super(itemView);
            ageLimit = itemView.findViewById(R.id.ageLimit);
            ticketPrice = itemView.findViewById(R.id.ticketPrice);
            perPerson = itemView.findViewById(R.id.perPerson);
            ticketIncrement = itemView.findViewById(R.id.ticketIncrement);
            ticketDecrement = itemView.findViewById(R.id.ticketDecrement);
            ticketQuantity = itemView.findViewById(R.id.ticketQuantity);
            subCategoryRecyclerView = itemView.findViewById(R.id.subCategoryRecyclerView);

        }
    }
}
