package com.vynkpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.models.GenerationBonusDetailModel;
import com.vynkpay.utils.Functions;

import java.util.ArrayList;

public class GenerationBonusDetailAdapter extends RecyclerView.Adapter<GenerationBonusDetailAdapter.Holder>{

    Context context;
    ArrayList<GenerationBonusDetailModel> generationBonusDetailModelArrayList;

    public GenerationBonusDetailAdapter(Context context, ArrayList<GenerationBonusDetailModel> generationBonusDetailModelArrayList) {
        this.context = context;
        this.generationBonusDetailModelArrayList = generationBonusDetailModelArrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.bonus_detail_item_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.nameUserName.setText(generationBonusDetailModelArrayList.get(position).getName()+"("+generationBonusDetailModelArrayList.get(position).getUsername()+")");
        holder.fromNameUserName.setText(generationBonusDetailModelArrayList.get(position).getRef_name()+"("+generationBonusDetailModelArrayList.get(position).getRef_username()+")");
        holder.levelTV.setText("Level "+generationBonusDetailModelArrayList.get(position).getFrom_level());

        if (generationBonusDetailModelArrayList.get(position).getStatus().equals("0")){
            holder.statusTV.setText("Confirmed");
        }else {
            holder.statusTV.setText("On Hold");
        }

        holder.amountTV.setText(generationBonusDetailModelArrayList.get(position).getP_amount());

        holder.dateTV.setText(Functions.changeDateFormat(generationBonusDetailModelArrayList.get(position).getCreated_date(), "yyyy-MM-dd hh:mm:ss", "dd-MMM-yyyy hh:mm aa"));
    }

    @Override
    public int getItemCount() {
        return generationBonusDetailModelArrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView nameUserName, fromNameUserName, levelTV, statusTV, dateTV, amountTV;
        public Holder(@NonNull View itemView) {
            super(itemView);
            nameUserName = itemView.findViewById(R.id.nameUserNameTV);
            fromNameUserName = itemView.findViewById(R.id.fromNameUserNameTV);
            levelTV = itemView.findViewById(R.id.levelTV);
            statusTV = itemView.findViewById(R.id.statusTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            amountTV = itemView.findViewById(R.id.amountTV);
        }
    }
}
