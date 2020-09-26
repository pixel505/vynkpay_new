package com.vynkpay.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by ayushsingla on 19/12/16.
 */

public class NormalEditText extends AppCompatEditText {
    public NormalEditText(Context context) {
        super(context);
        init(context);
    }

    public NormalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NormalEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/ITC_MEDIUM.ttf");
        setTypeface(font);
    }

}
