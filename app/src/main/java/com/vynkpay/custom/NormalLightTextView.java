package com.vynkpay.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ayushsingla on 19/12/16.
 */

public class NormalLightTextView extends TextView {
    public NormalLightTextView(Context context) {
        super(context);
        init(context);
    }

    public NormalLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NormalLightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/ITC_BOOK.ttf");
        setTypeface(font);
    }

}
