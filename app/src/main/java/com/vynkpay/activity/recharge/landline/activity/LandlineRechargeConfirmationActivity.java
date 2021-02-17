package com.vynkpay.activity.recharge.landline.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.M;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LandlineRechargeConfirmationActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layProceed)
    LinearLayout layProceed;
    @BindView(R.id.tvLandlineNumber)
    NormalTextView tvLandlineNumber;
    @BindView(R.id.tvOperatorName)
    NormalTextView txtOperatorName;
    @BindView(R.id.tvAccountNumber)
    NormalTextView tvAccountNumber;
    @BindView(R.id.tvAmount)
    NormalTextView tvAmount;
    @BindView(R.id.tvDueDate)
    NormalTextView tvDueDate;
    @BindView(R.id.txtSummaryAmount)
    NormalTextView txtSummaryAmount;
    @BindView(R.id.txtPromoCode)
    NormalTextView txtPromoCode;
    @BindView(R.id.txtTotalConfirmAmount)
    NormalTextView txtTotalConfirmAmount;
    @BindView(R.id.toolbar_title)
    NormalTextView toolbar_title;

    @BindView(R.id.llAccountLayout)
    LinearLayout llAccountLayout;
    @BindView(R.id.llDueDateLayout)
    LinearLayout llDueDateLayout;
    @BindView(R.id.labelLandline)
    NormalTextView labelLandline;

    @BindView(R.id.detailHeading)
    NormalTextView detailHeading;
    @BindView(R.id.summaryHeading)
    NormalTextView summaryHeading;

    @BindView(R.id.llDobLayout)
    LinearLayout llDobLayout;
    @BindView(R.id.tvDOB)
    NormalTextView tvDOb;
    @BindView(R.id.laySeePlain)
            LinearLayout laySeePlain;

    String _AMOUNT = "", _LABEL = "", _BILL_DATE = "", _SPECIAL_OR_TOP_UP = "",
            _DUE_DATE = "", _LANDLINE_NUMBER = "", _ACCOUNT_NUMBER = "",
            _OPERATOR_NAME = "", _OPERATOR_IMAGE = "", _TYPE = "", _DOB = "",operatorDetailId="";
    private String opUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_landline_recharge_rcg);
        ButterKnife.bind(LandlineRechargeConfirmationActivity.this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setListeners();
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

        _AMOUNT = getIntent().getStringExtra("amount");
        _BILL_DATE = getIntent().getStringExtra("billDate");
        _DUE_DATE = getIntent().getStringExtra("dueDate");
        _TYPE = getIntent().getStringExtra("type");
        _LABEL = getIntent().getStringExtra("label");
        _DOB = getIntent().getStringExtra("DOB");
        _LANDLINE_NUMBER = getIntent().getStringExtra("landLineNumber");
        _ACCOUNT_NUMBER = getIntent().getStringExtra("accountNumber");
        _OPERATOR_NAME = getIntent().getStringExtra("operatorName");
        _OPERATOR_IMAGE = getIntent().getStringExtra("operatorImage");


        _SPECIAL_OR_TOP_UP = getIntent().getStringExtra("operatorId");
        operatorDetailId = getIntent().getStringExtra("operatorDetail");

        Log.e("op",""+_SPECIAL_OR_TOP_UP);
        Log.e("op",""+operatorDetailId);

        Log.i(">>type", "onCreate: " + _TYPE);
        if (_ACCOUNT_NUMBER.equalsIgnoreCase("") || _ACCOUNT_NUMBER.equalsIgnoreCase("10")) {
            llAccountLayout.setVisibility(View.GONE);
        } else {
            llAccountLayout.setVisibility(View.VISIBLE);
        }
        if (_TYPE.equalsIgnoreCase("4")) {
            llAccountLayout.setVisibility(View.GONE);
            labelLandline.setText(_LABEL);
            toolbar_title.setText("Confirm Broadband Bill");
            summaryHeading.setText("Broadband Summary");
            detailHeading.setText("Broadband Details");
        } else if (_TYPE.equalsIgnoreCase("10")) {
            labelLandline.setText(_LABEL);
            toolbar_title.setText("Confirm Water Bill");
            summaryHeading.setText("Water Bill Summary");
            detailHeading.setText("Water Bill Details");
        } else if (_TYPE.equalsIgnoreCase("3")) {
            labelLandline.setText(_LABEL);
            toolbar_title.setText("Confirm Electricity Bill");
            summaryHeading.setText("Electricity Bill Summary");
            detailHeading.setText("Electricity Bill Details");
        } else if (_TYPE.equalsIgnoreCase("11")) {
            labelLandline.setText(_LABEL);
            llDobLayout.setVisibility(View.VISIBLE);
            String[] v = _DOB.split("@");
            Log.i(">>dob", "onCreate: " + _DOB);
            tvDOb.setText(M.changeDateFormat(v[1] + " " + "00:12:45"));
            llAccountLayout.setVisibility(View.GONE);
            toolbar_title.setText("Confirm Insurance Premium");
            summaryHeading.setText("Insurance Summary");
            detailHeading.setText("Insurance Details");
        } else if (_TYPE.equalsIgnoreCase("9")) {
            if (_SPECIAL_OR_TOP_UP.equalsIgnoreCase("61")) {
                labelLandline.setText("Customer Account Number");
            } else {
                labelLandline.setText(_LABEL);
            }
            toolbar_title.setText("Confirm Gas Bill");
            summaryHeading.setText("Gas Bill Summary");
            detailHeading.setText("Gas Bill Details");
        } else {
            labelLandline.setText("Landline Number");
            toolbar_title.setText("Confirm Landline Bill");
            summaryHeading.setText("Landline Summary");
            detailHeading.setText("Landline Details");
        }

        if (_DUE_DATE.equalsIgnoreCase("")) {
            llDueDateLayout.setVisibility(View.GONE);
        } else {
            llDueDateLayout.setVisibility(View.VISIBLE);
        }

        String mPromoValue = "0";
        double promoAmount = Double.parseDouble("0");
        double planAmount = Double.parseDouble(_AMOUNT);
        txtPromoCode.setText(Functions.CURRENCY_SYMBOL_USER + promoAmount);
        txtSummaryAmount.setText(Functions.CURRENCY_SYMBOL_USER + planAmount);
        txtTotalConfirmAmount.setText(Functions.CURRENCY_SYMBOL_USER + planAmount + "");
        txtOperatorName.setText(_OPERATOR_NAME);
        tvLandlineNumber.setText(_LANDLINE_NUMBER);
        tvAccountNumber.setText(_ACCOUNT_NUMBER);
        tvAmount.setText(Functions.CURRENCY_SYMBOL_USER + planAmount);
        tvDueDate.setText(_DUE_DATE);
    }

    public void setListeners() {

        laySeePlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LandlineRechargeConfirmationActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        layProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandlineRechargeConfirmationActivity.this, LandlinePaymentMethodActivity.class);
                intent.putExtra("amount", _AMOUNT);
                intent.putExtra("type", _TYPE);
                intent.putExtra("label", _LABEL);
                intent.putExtra("operator_id", _SPECIAL_OR_TOP_UP);
                intent.putExtra("landlineNumber", _LANDLINE_NUMBER);
                intent.putExtra("accountNumber", _ACCOUNT_NUMBER);
                intent.putExtra("DOB", _DOB);
                intent.putExtra("operatorDetailid", operatorDetailId);


                Prefes.saveType(_TYPE, LandlineRechargeConfirmationActivity.this);
                Prefes.saveOperatorUrl(opUrl,LandlineRechargeConfirmationActivity.this);
                Prefes.savePhoneNumber(_ACCOUNT_NUMBER,LandlineRechargeConfirmationActivity.this);
                Prefes.saveOperatorID(_SPECIAL_OR_TOP_UP,LandlineRechargeConfirmationActivity.this);
                Prefes.saveCircle(_OPERATOR_NAME,LandlineRechargeConfirmationActivity.this);
                Prefes.saveOperatorDID(operatorDetailId,LandlineRechargeConfirmationActivity.this);



                Log.e("operatorid2",""+_SPECIAL_OR_TOP_UP);
                Log.e("operatorDetailid2",""+operatorDetailId);


                startActivity(intent);
            }
        });
    }
}
