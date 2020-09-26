package com.vynkpay.models;

import android.app.Dialog;
import android.widget.LinearLayout;

import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;

public class ErrorDialogModel {
    private NormalTextView errorMessage;
    private Dialog dialog;
    private NormalButton okButton;
    private LinearLayout closeButton;

    public ErrorDialogModel(Dialog dialog, NormalTextView errorMessage, NormalButton okButton, LinearLayout closeButton) {
        this.dialog = dialog;
        this.errorMessage = errorMessage;
        this.okButton = okButton;
        this.closeButton = closeButton;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public NormalTextView getErrorMessage() {
        return errorMessage;
    }

    public NormalButton getOkButton() {
        return okButton;
    }

    public LinearLayout getCloseButton() {
        return closeButton;
    }
}
