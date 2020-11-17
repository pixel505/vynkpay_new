package com.vynkpay.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.OffersModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OfferAdapter extends PagerAdapter {
    Context context;
    ArrayList<?> offerDataList;
    LayoutInflater layoutInflater;

    public OfferAdapter(Context context, ArrayList<?> offerDataList) {
        this.context = context;
        this.offerDataList = offerDataList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return offerDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((CardView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.row_item_offer_rcg, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.offerImage);
        NormalTextView offerTitle = (NormalTextView) itemView.findViewById(R.id.offerTitle);
        NormalTextView offerDescription = (NormalTextView) itemView.findViewById(R.id.offerDescription);
        NormalTextView offerCode = (NormalTextView) itemView.findViewById(R.id.offerCode);
        NormalTextView offerCodeText = (NormalTextView) itemView.findViewById(R.id.offerCodeText);

        if (offerDataList.get(position) instanceof OffersModel) {
            Picasso.get().load(BuildConfig.ImageBaseURl+((OffersModel) offerDataList.get(position)).getImage()).into(imageView);
            offerTitle.setText(((OffersModel) offerDataList.get(position)).getTitle());
            offerDescription.setText(((OffersModel) offerDataList.get(position)).getDescription());
            offerCode.setText(((OffersModel) offerDataList.get(position)).getOffer_url());
            offerCodeText.setText(((OffersModel) offerDataList.get(position)).getCreated_at());
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
    }
}
