package com.vynkpay.activity.themepark.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import com.vynkpay.R;
import com.vynkpay.activity.themepark.event.IsTicketTypeSelectedEvent;
import com.vynkpay.activity.themepark.event.ThemeParkTicketEvent;
import com.vynkpay.activity.themepark.model.ThemeParkTicketModel;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;

public class ThemeParkTicketAdapter extends RecyclerView.Adapter<ThemeParkTicketAdapter.MyViewHolder> {
    Context context;
    ArrayList<ThemeParkTicketModel> giftCardModelArrayList;

    public boolean isTicketTypeSelected = false;

    ThemeParkTicketSelectionAdapter themeParkTicketSelectionAdapter;

    public ThemeParkTicketAdapter() {
    }

    public ThemeParkTicketAdapter(Context context, ArrayList<ThemeParkTicketModel> giftCardModelArrayList) {
        this.context = context;
        this.giftCardModelArrayList = giftCardModelArrayList;
        this.themeParkTicketSelectionAdapter = new ThemeParkTicketSelectionAdapter();
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_single_theme_park_ticket_rcg, parent, false);
        MyViewHolder m = new MyViewHolder(v);
        return m;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        onViewAttachedToWindow(holder);
        holder.ticketTitleCheck.setText(giftCardModelArrayList.get(position).getTitle());

        holder.ticketTitleCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPositionClicked = position;
                isTicketTypeSelected = true;
                themeParkTicketSelectionAdapter.reset();
                EventBus.getDefault().post(new ThemeParkTicketEvent(giftCardModelArrayList.get(position)));
                EventBus.getDefault().post(new IsTicketTypeSelectedEvent(isTicketTypeSelected));
                notifyDataSetChanged();

            }
        });

        if (lastPositionClicked == position) {
            holder.ticketTitleCheck.setChecked(true);

        } else {
            holder.ticketTitleCheck.setChecked(false);
            isTicketTypeSelected = false;
        }
        bindDataToNextAdapter(position, holder);
    }


    private void bindDataToNextAdapter(int position, MyViewHolder holder) {
        ThemeParkTicketSelectionAdapter themeParkTicketSelectionAdapter = new ThemeParkTicketSelectionAdapter(context, giftCardModelArrayList.get(position).getSubCategoryData());
        holder.subCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.subCategoryRecyclerView.setAdapter(themeParkTicketSelectionAdapter);
        themeParkTicketSelectionAdapter.notifyDataSetChanged();
    }

    private static int lastPositionClicked = -1;

    @Override
    public int getItemCount() {
        return giftCardModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton ticketTitleCheck;
        RecyclerView subCategoryRecyclerView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ticketTitleCheck = itemView.findViewById(R.id.ticketTitleCheck);
            subCategoryRecyclerView = itemView.findViewById(R.id.subCategoryRecyclerView);

        }
    }

    public void reset() {
        lastPositionClicked = -1;
        ThemeParkTicketAdapter.this.notifyDataSetChanged();
    }
}
