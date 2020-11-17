package com.vynkpay.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.HelpSupportModel;
import java.util.ArrayList;

public class HelpDetailsAdapter extends RecyclerView.Adapter<HelpDetailsAdapter.MyViewHolder> {
    ArrayList<HelpSupportModel> notificationListData;
    Context context;

    public HelpDetailsAdapter(Context context, ArrayList<HelpSupportModel> notificationListData) {
        this.context = context;
        this.notificationListData = notificationListData;
    }


    private MyViewHolder m;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_help_detials_rcg, parent, false);
        m = new MyViewHolder(v);
        return m;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.question.setText((notificationListData.get(position)).getQuestion());
        holder.answer.setText("Answer : " + (notificationListData.get(position)).getAnswer());
        if (((notificationListData.get(position)).isClicked)) {
            holder.answerLayout.setVisibility(View.VISIBLE);
        } else {
            holder.answerLayout.setVisibility(View.GONE);
        }
        holder.makeVisibleHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpSupportModel helpSupportModel = ((notificationListData.get(position)));
                if (helpSupportModel.isClicked) {
                    helpSupportModel.isClicked = false;
                    notificationListData.set(position, helpSupportModel);
                    HelpDetailsAdapter.this.notifyDataSetChanged();
                } else {
                    helpSupportModel.isClicked = true;
                    notificationListData.set(position, helpSupportModel);
                    HelpDetailsAdapter.this.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationListData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        NormalTextView answer, question;
        LinearLayout makeVisibleHide;
        LinearLayout answerLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            makeVisibleHide = itemView.findViewById(R.id.makeVisibleHide);
            question = itemView.findViewById(R.id.question);
            answerLayout = itemView.findViewById(R.id.answerLayout);
            answer = itemView.findViewById(R.id.answer);
        }
    }
}
