package com.vynkpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.R;
import com.vynkpay.custom.NormalEditText;
import java.util.List;

public class WithdrawalTypeAdapter extends RecyclerView.Adapter<WithdrawalTypeAdapter.MyViewHolder> {
    Context context;
    List<String> mList;
    NormalEditText sear;

    public interface OnItemClickListener{
        void onClick(String wType);
    }

    OnItemClickListener mlistener;


    public WithdrawalTypeAdapter(Context activity, List<String> withdrawalType) {
        this.context = activity;
        this.mList = withdrawalType;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mlistener = listener;
    }

    @Override
    public WithdrawalTypeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_list, parent, false);

        return new WithdrawalTypeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WithdrawalTypeAdapter.MyViewHolder holder, int position) {
       holder.countryText.setText(mList.get(position));
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mlistener.onClick(mList.get(position));
           }
       });

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView countryText;

        public MyViewHolder(View view) {
            super(view);
            countryText = view.findViewById(R.id.countryText);
        }
    }
}
