package com.vynkpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.viewpager.widget.PagerAdapter;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.models.BannerModel;
import com.vynkpay.utils.RoundishImageView;
import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    Context context;
    LayoutInflater mLayoutInflater;
    ArrayList<BannerModel> sliderItemModelArrayList;

    public ImageAdapter(Context context, ArrayList<BannerModel> sliderItemModelArrayList){
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
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_item_layout, container, false);

        RoundishImageView imageView=itemView.findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Functions.loadImageCall(context, BuildConfig.BASE_URL +"account/"+ sliderItemModelArrayList.get(position).getFull_banner_image(), imageView);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}