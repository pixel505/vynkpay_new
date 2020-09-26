package com.vynkpay.activity.recharge.mobile.activity;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class RechargeConfirmationActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.layProceed)
    LinearLayout layProceed;
    @BindView(R.id.txtMobileNumber)
    NormalTextView txtMobileNumber;
    @BindView(R.id.txtOperatorName)
    NormalTextView txtOperatorName;
    @BindView(R.id.txtPlan)
    NormalTextView txtPlan;
    @BindView(R.id.txtValidity)
    NormalTextView txtValidity;
    @BindView(R.id.txtAmount)
    NormalTextView txtAmount;
    @BindView(R.id.txtSummaryAmount)
    NormalTextView txtSummaryAmount;
    @BindView(R.id.txtPromoCode)
    NormalTextView txtPromoCode;
    @BindView(R.id.txtTotalConfirmAmount)
    NormalTextView txtTotalConfirmAmount;
    @BindView(R.id.layPlan)
    LinearLayout layPlan;
    @BindView(R.id.layValidilty)
    LinearLayout layValidilty;
    @BindView(R.id.layTalkTime)
    LinearLayout layTalkTime;
    @BindView(R.id.toolbar_title)
    NormalTextView toolbar_title;
    @BindView(R.id.laySeePlain)
            LinearLayout laySeePlain;
    String _PLAN, _TALKTIME, _VALIDITY, _OPERATOR, _MOBILE_NUMBER, _AMOUNT, _TYPE, _OPERATOR_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_recharge_rcg);
        ButterKnife.bind(RechargeConfirmationActivity.this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setListeners();
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar_title.setText("Confirm Recharge");
        _PLAN = getIntent().getStringExtra("plan");
        _TALKTIME = getIntent().getStringExtra("talkTime");
        _VALIDITY = getIntent().getStringExtra("validity");
        _OPERATOR = getIntent().getStringExtra("operator");
        _MOBILE_NUMBER = getIntent().getStringExtra("mobile");
        _AMOUNT = getIntent().getStringExtra("amount");
        _TYPE = getIntent().getStringExtra("type");
        _OPERATOR_ID = getIntent().getStringExtra("operator_id");
        double planAmount = Double.parseDouble(_AMOUNT);
        Log.i(">>operatorId", "confirm: " + _OPERATOR_ID);
        txtMobileNumber.setText(_MOBILE_NUMBER);
        txtOperatorName.setText(_OPERATOR);
        if (_PLAN.equals("")) {
            layPlan.setVisibility(View.GONE);
            txtPlan.setText("N.A");
            layPlan.setVisibility(View.GONE);
        } else {
            txtPlan.setText(_PLAN);
            layPlan.setVisibility(View.VISIBLE);
        }

        if (_TALKTIME.equals("")) {
            txtAmount.setText(Functions.CURRENCY_SYMBOL + _TALKTIME);
            layTalkTime.setVisibility(View.GONE);
        } else {
            txtAmount.setText(Functions.CURRENCY_SYMBOL + _TALKTIME);
            layTalkTime.setVisibility(View.VISIBLE);
        }

        if (_VALIDITY.equals("")) {
            txtValidity.setText("N.A");
            layValidilty.setVisibility(View.GONE);
        } else {
            layValidilty.setVisibility(View.VISIBLE);
            txtValidity.setText(_VALIDITY);
        }

        String mPromoValue = "0";
        double promoAmount = Double.parseDouble("0");


        txtPromoCode.setText(Functions.CURRENCY_SYMBOL + promoAmount);
        txtSummaryAmount.setText(Functions.CURRENCY_SYMBOL + planAmount);

        txtTotalConfirmAmount.setText(Functions.CURRENCY_SYMBOL + planAmount + "");

    }

    public void setListeners() {
        laySeePlain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RechargeConfirmationActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        layProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RechargeConfirmationActivity.this, PaymentMethodActivity.class);
                intent.putExtra("amount", _AMOUNT);
                intent.putExtra("type", _TYPE);
                intent.putExtra("operator_id", _OPERATOR_ID);
                intent.putExtra("mobile", _MOBILE_NUMBER);
                startActivity(intent);
            }
        });
    }
}
