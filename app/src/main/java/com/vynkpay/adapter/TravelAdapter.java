package com.vynkpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.R;
import com.vynkpay.models.ServiceModel;
import java.util.ArrayList;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.MyViewHolder> {
    Context context;
    ArrayList<ServiceModel> mList;

    public TravelAdapter(Context applicationContext, ArrayList<ServiceModel> serviceModelArrayList) {
        this.context=applicationContext;
        this.mList=serviceModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ServiceModel data=mList.get(position);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView travelImage;
        TextView travelTitlle;

        public MyViewHolder(View view) {
            super(view);
            travelImage=view.findViewById(R.id.travelImage);
            travelTitlle=view.findViewById(R.id.travelTitle);


        }
    }
}
