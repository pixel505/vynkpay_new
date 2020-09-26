package com.vynkpay.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by ayushsingla on 19/12/16.
 */

public class NormalButton extends Button {
    public NormalButton(Context context) {
        super(context);
        init(context);
    }

    public NormalButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NormalButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/ITC_MEDIUM.ttf");
        setTypeface(font);
    }

}
