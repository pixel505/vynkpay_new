package com.vynkpay.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ayushsingla on 19/12/16.
 */

public class NormalBoldEditText extends EditText {
    public NormalBoldEditText(Context context) {
        super(context);
        init(context);
    }

    public NormalBoldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NormalBoldEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/ITC_BOLD.ttf");
        setTypeface(font);
    }

}
