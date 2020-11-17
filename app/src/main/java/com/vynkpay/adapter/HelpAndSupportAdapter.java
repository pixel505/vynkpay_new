package com.vynkpay.adapter;

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
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.activities.HelpAndSupportQuestions;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.HelpSupportModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class HelpAndSupportAdapter extends RecyclerView.Adapter<HelpAndSupportAdapter.MyViewHolder> {
    ArrayList<?> notificationListData;
    Context context;

    public HelpAndSupportAdapter(Context context, ArrayList<?> notificationListData) {
        this.context = context;
        this.notificationListData = notificationListData;
    }


    private MyViewHolder m;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_help_support_rcg, parent, false);
        m = new MyViewHolder(v);
        return m;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (notificationListData.get(position) instanceof HelpSupportModel) {
            holder.catTitle.setText(((HelpSupportModel) notificationListData.get(position)).getCategoryName());
            holder.desc.setText(((HelpSupportModel) notificationListData.get(position)).getDescription());
            Picasso.get().load(BuildConfig.ImageBaseURl + ((HelpSupportModel) notificationListData.get(position)).getImage()).error(R.drawable.app_icon).into(holder.catImage);
            holder.tap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HelpAndSupportQuestions.class);
                    intent.putExtra("id", ((HelpSupportModel) notificationListData.get(position)).getCategoryId());
                    intent.putExtra("name", ((HelpSupportModel) notificationListData.get(position)).getCategoryName());
                    intent.putExtra("description", ((HelpSupportModel) notificationListData.get(position)).getDescription());
                    ((Activity) context).startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return notificationListData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        NormalTextView catTitle, desc;
        ImageView catImage;
        LinearLayout tap;

        public MyViewHolder(View itemView) {
            super(itemView);
            catImage = itemView.findViewById(R.id.catImage);
            catTitle = itemView.findViewById(R.id.catTitle);
            tap = itemView.findViewById(R.id.tap);
            desc = itemView.findViewById(R.id.desc);
        }
    }
}
