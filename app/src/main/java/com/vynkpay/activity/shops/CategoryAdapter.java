package com.vynkpay.activity.shops;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.R;
import com.vynkpay.activity.shops.models.Categorys;
import com.vynkpay.custom.NormalTextView;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder>{

    Context context;
    ArrayList<Categorys> categorysList;

    interface OnItemClickListener{
        void onClick(String id,String title,String total);
    }

    OnItemClickListener onItemClickListener;

    public CategoryAdapter(Context context,ArrayList<Categorys> categorysList) {
        this.context = context;
        this.categorysList = categorysList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_categorylist,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        try {
            holder.tvCategory.setText(categorysList.get(position).getTitle()+"("+categorysList.get(position).getTotal()+")");
            holder.itemView.setOnClickListener(view -> {
                if (onItemClickListener!=null){
                    onItemClickListener.onClick(categorysList.get(position).getId(),categorysList.get(position).getTitle(),categorysList.get(position).getTotal());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return categorysList!=null ? categorysList.size() : 0;
    }

    static class Holder extends RecyclerView.ViewHolder{

        NormalTextView tvCategory;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);

        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

}
