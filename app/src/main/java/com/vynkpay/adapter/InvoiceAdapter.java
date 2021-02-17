package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.R;
import com.vynkpay.activity.activities.InvoiceDetailActivity;
import com.vynkpay.databinding.InvoiceItemBinding;
import com.vynkpay.retrofit.model.GetInvoiceResponse;
import com.vynkpay.utils.Functions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder>  {
    Context context;
    List<GetInvoiceResponse.Data.Listing> listdata;

    public InvoiceAdapter(Context ac, List<GetInvoiceResponse.Data.Listing> data) {
        this.context = ac;
        this.listdata = data;
    }


    public void setFilter(List<GetInvoiceResponse.Data.Listing> FilteredDataList) {
        listdata = FilteredDataList;
        notifyDataSetChanged();
    }

    @Override
    public InvoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InvoiceItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.invoice_item, parent, false);
        InvoiceAdapter.ViewHolder viewHolder = new InvoiceAdapter.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InvoiceAdapter.ViewHolder holder, int position) {
        GetInvoiceResponse.Data.Listing  myListData = listdata.get(position);
        holder.binding.nameText.setText(myListData.getName());
        holder.binding.nameryttext.setText(myListData.getUsername());
        holder.binding.payment.setText(Functions.CURRENCY_SYMBOL+myListData.getAmount());
        holder.binding.invoiceNumber.setText(myListData.getInvoiceNumber());

        try {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            Date d = f.parse(myListData.getCreatedDate());
            DateFormat date = new SimpleDateFormat("dd-MM-yyy",Locale.getDefault());
            holder.binding.purchaseDate.setText(date.format(d));
            System.out.println("Date: " + date.format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }





        holder.binding.viewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, InvoiceDetailActivity.class).putExtra("invoiceId",myListData.getId()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });


    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        final InvoiceItemBinding binding;
        ViewHolder(final InvoiceItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}

