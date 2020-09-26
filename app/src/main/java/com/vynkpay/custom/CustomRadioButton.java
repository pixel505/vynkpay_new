package com.vynkpay.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class CustomRadioButton extends RadioButton {
    public CustomRadioButton(Context context) {
        super(context);
        init(context);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    void init(Context context) {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/ITC_MEDIUM.ttf");
        setTypeface(font);
    }
}
