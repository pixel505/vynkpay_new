package com.vynkpay.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.viewpager.widget.PagerAdapter;
import com.vynkpay.R;
import com.vynkpay.activity.activities.ChoosePaymentActivity;
import com.vynkpay.custom.NormalBoldTextView;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.retrofit.model.GetPackageResponse;
import com.vynkpay.utils.Functions;

import java.util.List;

public class PackageAdapter extends PagerAdapter {
    Context context;
    LayoutInflater mLayoutInflater;
    List<GetPackageResponse.DataBean.PackagesBean> sliderItemModelArrayList;

    public PackageAdapter(Context context, List<GetPackageResponse.DataBean.PackagesBean> sliderItemModelArrayList){
        this.context=context;
        this.sliderItemModelArrayList=sliderItemModelArrayList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sliderItemModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_package_item_layout, container, false);
        NormalTextView tvTitleDisp = itemView.findViewById(R.id.tvTitleDisp);
        NormalTextView tvWeekly = itemView.findViewById(R.id.tvWeekly);
        NormalTextView tvText = itemView.findViewById(R.id.tvText);
        NormalBoldTextView packageprice=itemView.findViewById(R.id.packageprice);
        NormalBoldTextView cashback=itemView.findViewById(R.id.cashback);
        NormalBoldTextView points=itemView.findViewById(R.id.points);
        NormalButton btn=itemView.findViewById(R.id.bynow);
        LinearLayout ln=itemView.findViewById(R.id.lnBg);
        ImageView uperImage=itemView.findViewById(R.id.uperIcon);
        uperImage.setVisibility(View.GONE);
        GetPackageResponse.DataBean.PackagesBean data=sliderItemModelArrayList.get(position);
        if (data.getType().equalsIgnoreCase("1")){
            tvWeekly.setVisibility(View.GONE);
            tvText.setText("Cashback");
        }else {
            tvText.setText("");
            tvWeekly.setVisibility(View.VISIBLE);
            tvWeekly.setText("Weekly "+Functions.CURRENCY_SYMBOL+data.getWeekly_amount());
        }

        if(Functions.isIndian){
            packageprice.setText(Functions.CURRENCY_SYMBOL +data.getTotal_amount());
        }
        else {
            packageprice.setText(Functions.CURRENCY_SYMBOL +data.getPrice());
        }
        cashback.setText(Functions.CURRENCY_SYMBOL+data.getPrice());
        points.setText(data.getPoints());
        if(data.getCurser().equals("allowed")) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* int totalAmount= Integer.parseInt(data.getTotal_amount());
                    int cash= Integer.parseInt(data.getCashback());*/
                    context.startActivity(new Intent(context, ChoosePaymentActivity.class).putExtra("purchaseprice",data.getPrice())
                            .putExtra("cashback",data.getCashback())
                            .putExtra("ids",data.getId())

                    );
                }
            });
        }
        else {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "You can not buy this package", Toast.LENGTH_SHORT).show();
                }
            });
        }


        if(position%8==0){
            ln.setBackgroundResource(R.drawable.onebg);
            uperImage.setImageResource(R.drawable.packageneww);
        }

        if(position%8==1){
            ln.setBackgroundResource(R.drawable.twobg);
            uperImage.setImageResource(R.drawable.twoicon);

        }
        if(position%8==2){
            ln.setBackgroundResource(R.drawable.threebg);
            uperImage.setImageResource(R.drawable.threeicon);

        }
        if(position%8==3){
            ln.setBackgroundResource(R.drawable.fourbg);
            uperImage.setImageResource(R.drawable.fouricon);

        }
        if(position%8==4){
            ln.setBackgroundResource(R.drawable.fifthbg);
            uperImage.setImageResource(R.drawable.fifthicon);

        }
        if(position%8==5){
            ln.setBackgroundResource(R.drawable.sixthbg);
            uperImage.setImageResource(R.drawable.sixthicon);
        }

        if(position%8==6){
            ln.setBackgroundResource(R.drawable.seventhbg);
            uperImage.setImageResource(R.drawable.seventhicon);
        }

        if(position%8==7){
            ln.setBackgroundResource(R.drawable.eightbg);
            uperImage.setImageResource(R.drawable.eighticon);

        }

        /*try {
            Glide.with(context).load(data.getImg()).fitCenter().into(uperImage);
            tvTitleDisp.setText(data.getTitle());
            tvTitleDisp.setSelected(true);
        }catch (Exception e){
            e.printStackTrace();
        }*/


        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }

}