package com.vynkpay.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.vynkpay.retrofit.model.NotificationResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    List<NotificationResponse.DataBean.NotificationBean> notificationListData;
    Context context;
    public NotificationAdapter(Context context, List<NotificationResponse.DataBean.NotificationBean> notificationListData) {
        this.context = context;
        this.notificationListData = notificationListData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_notification_rcg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        NotificationResponse.DataBean.NotificationBean data=notificationListData.get(position);
        holder.description.setText(data.getBody());
        holder.title.setText(data.getSubject());
        holder.time.setText(Functions.changeDateFormat(data.getCreate_date(),"yyyy-MM-dd hh:mm:ss", "dd-MMM-yyyy hh:mm aa"));
        if(data.getImage()!=null) {
            Picasso.get().load(data.getImage()).error(R.drawable.app_icon).into(holder.notificationImage);
        }
        if(data.getRead_status().equals("0")){
            holder.cardClick.setBackgroundColor(Color.parseColor("#E39F9F"));
            holder.cardClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
        else{
            holder.cardClick.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }
    @Override
    public int getItemCount() {
        return notificationListData.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView notificationImage;
        NormalTextView title, description, time,delete;
        LinearLayout cardClick;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notificationTitle);
            description = itemView.findViewById(R.id.notificationDescription);
            time = itemView.findViewById(R.id.notificationTime);
            notificationImage = itemView.findViewById(R.id.notificationImage);
            cardClick = itemView.findViewById(R.id.cardClick);
        }
    }
}
