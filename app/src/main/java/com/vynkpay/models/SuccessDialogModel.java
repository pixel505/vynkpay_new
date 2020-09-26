package com.vynkpay.models;

import android.app.Dialog;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;

public class SuccessDialogModel {
    Dialog successDialog;
    NormalTextView tvMobileNumber;
    NormalTextView tvAmount;
    NormalTextView tvTransactionId;
    NormalTextView tvDateTime;
    NormalButton okButton;
    LinearLayout closeButton;
    ImageView imageResult;
    NormalTextView forWhatPaid;
    NormalTextView textResult;

    public SuccessDialogModel(Dialog successDialog, NormalTextView tvMobileNumber, NormalTextView tvAmount, NormalTextView tvTransactionId, NormalTextView tvDateTime, NormalButton okButton, LinearLayout closeButton, ImageView imageResult, NormalTextView forWhatPaid, NormalTextView textResult) {
        this.successDialog = successDialog;
        this.tvMobileNumber = tvMobileNumber;
        this.tvAmount = tvAmount;
        this.tvTransactionId = tvTransactionId;
        this.tvDateTime = tvDateTime;
        this.okButton = okButton;
        this.closeButton = closeButton;
        this.imageResult = imageResult;
        this.forWhatPaid = forWhatPaid;
        this.textResult = textResult;
    }

    public Dialog getSuccessDialog() {
        return successDialog;
    }

    public NormalTextView getTvMobileNumber() {
        return tvMobileNumber;
    }

    public NormalTextView getTvAmount() {
        return tvAmount;
    }

    public NormalTextView getTvTransactionId() {
        return tvTransactionId;
    }

    public NormalTextView getTvDateTime() {
        return tvDateTime;
    }

    public NormalButton getOkButton() {
        return okButton;
    }

    public LinearLayout getCloseButton() {
        return closeButton;
    }

    public ImageView getImageResult() {
        return imageResult;
    }

    public NormalTextView getForWhatPaid() {
        return forWhatPaid;
    }

    public NormalTextView getTextResult() {
        return textResult;
    }
}
