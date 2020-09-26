package com.vynkpay.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.models.EcashModelClass;
import com.vynkpay.utils.Functions;

import java.util.ArrayList;
import java.util.List;

public class ECashAdapter extends BaseAdapter{
    Context context;
    List<EcashModelClass.Data.Ecash> ecashModelClassArrayList;

    public ECashAdapter(Context context, List<EcashModelClass.Data.Ecash> ecashModelClassArrayList) {
        this.context = context;
        this.ecashModelClassArrayList = ecashModelClassArrayList;
    }

    @Override
    public int getCount() {
        return ecashModelClassArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView dateTV, transactionTV, remarkTV, amountTV;
        convertView = LayoutInflater.from(context).inflate(R.layout.ecash_item_layout, parent, false);
        dateTV=convertView.findViewById(R.id.dateTV);
        transactionTV=convertView.findViewById(R.id.transactionTV);
        remarkTV=convertView.findViewById(R.id.remarkTV);
        amountTV=convertView.findViewById(R.id.amountTV);
        Log.e("dates",""+ecashModelClassArrayList.get(position).getModifiedDate());
        dateTV.setText(Functions.changeDateFormat(ecashModelClassArrayList.get(position).getModifiedDate(), "yyyy-MM-dd hh:mm:ss", "dd-MMM-yyyy hh:mm aa"));
        transactionTV.setText("#"+ecashModelClassArrayList.get(position).getTxnNo());
        remarkTV.setText(ecashModelClassArrayList.get(position).getRemarks());
        amountTV.setText(ecashModelClassArrayList.get(position).getAmount());
        return convertView;
    }
}
