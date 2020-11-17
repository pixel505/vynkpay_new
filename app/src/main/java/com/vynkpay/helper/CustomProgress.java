package com.vynkpay.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ProgressBar;
import com.vynkpay.R;

public class CustomProgress extends Dialog implements DialogInterface.OnDismissListener {
    public CustomProgress(Context context) {
        super(context);
    }

    public CustomProgress(Context context, int theme) {
        super(context, theme);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public static CustomProgress show(Context context, String message
            , boolean cancelable) {
        CustomProgress dialog = new CustomProgress(context);
        dialog.setContentView(R.layout.anim_hud_rcg);
        dialog.setCancelable(cancelable);
        if (dialog.getWindow()!=null){
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
        return dialog;


    }

    @Override
    public void onDismiss(DialogInterface arg0) {
        System.out.println("dismiss is called");
    }
}
