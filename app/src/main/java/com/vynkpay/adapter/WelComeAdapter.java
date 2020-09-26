package com.vynkpay.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.vynkpay.R;
import com.vynkpay.models.Slider;

import java.util.ArrayList;

/**
 * Created by designer on 14/12/17.
 */

public class WelComeAdapter extends PagerAdapter {

    private ArrayList<Slider> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public WelComeAdapter(Context context,ArrayList<Slider> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.custom_welcomelayout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.imageAd);
        TextView tvTitleText = (TextView)imageLayout.findViewById(R.id.tvTitleText);
        TextView tvText = (TextView)imageLayout.findViewById(R.id.tvText);
        LinearLayout linMainTuto = (LinearLayout)imageLayout.findViewById(R.id.linMainTuto);
        /*if (position == 0) {
            linMainTuto.setBackgroundColor(ContextCompat.getColor(context,R.color.searchfreelancer));
        }else if (position == 1){
            linMainTuto.setBackgroundColor(ContextCompat.getColor(context,R.color.hirefreelancer));
        }else if (position == 2){
            linMainTuto.setBackgroundColor(ContextCompat.getColor(context,R.color.worklaunchproject));
        }else {
            linMainTuto.setBackgroundColor(ContextCompat.getColor(context,R.color.earnmoney));
        }*/


        imageView.setImageResource(IMAGES.get(position).getImage());
        tvTitleText.setText(IMAGES.get(position).getText());
        tvText.setText(IMAGES.get(position).getDesc());


        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
