package com.vynkpay.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.models.OffersModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {
    ArrayList<OffersModel> sliderImageList;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public CustomPagerAdapter(Context context, ArrayList<OffersModel> sliderImageList) {
        mContext = context;
        this.sliderImageList = sliderImageList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sliderImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((CardView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.row_item_pager_rcg, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        Picasso.get().load(BuildConfig.ImageBaseURl + sliderImageList.get(position).getImage()).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
    }
}