package com.vynkpay.activity.recharge.landline.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalLightTextView;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.M;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BillFetchActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageOperator)
    ImageView imageOperator;
    @BindView(R.id.tvAmount)
    NormalTextView tvAmount;
    @BindView(R.id.tvName)
    NormalTextView tvName;
    @BindView(R.id.tvDate)
    NormalTextView tvDate;
    @BindView(R.id.tvDueDate)
    NormalTextView tvDueDate;

    @BindView(R.id.tvLandlineNumber)
    NormalLightTextView tvLandlineNumber;
    @BindView(R.id.tvOperatorName)
    NormalLightTextView tvOperatorName;
    @BindView(R.id.tvAccountNumber)
    NormalLightTextView tvAccountNumber;

    @BindView(R.id.etAmount)
    NormalEditText etAmount;
    @BindView(R.id.continueButton)
    NormalButton continueButton;
    @BindView(R.id.toolbar_title)
    NormalTextView toolbar_title;
    @BindView(R.id.llAmountLayout)
    LinearLayout llAmountLayout;
    JSONObject userPreference;
    String full_name = "";

    String _AMOUNT = "", _LABEL = "", _BILL_DATE = "", _CONSUMER_NAME = "",
            _PARTIAL_PAY = "", _SPECIAL_OR_TOP_UP = "", _DUE_DATE = "",
            _LANDLINE_NUMBER = "", _ACCOUNT_NUMBER = "", _OPERATOR_NAME = "",
            _OPERATOR_IMAGE = "", _TYPE = "", _DOB = "",operatorDetailId="";
    private String opUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_fetch_rcg);
        ButterKnife.bind(BillFetchActivity.this);
        setListeners();
        try {
            userPreference = new JSONObject(Prefes.getUserData(BillFetchActivity.this));
            full_name = Prefes.getName(BillFetchActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        _AMOUNT = getIntent().getStringExtra("amount");
        _BILL_DATE = getIntent().getStringExtra("billDate");
        _DUE_DATE = getIntent().getStringExtra("dueDate");
        _LABEL = getIntent().getStringExtra("label");
        _TYPE = getIntent().getStringExtra("type");
        _DOB = getIntent().getStringExtra("DOB");
        _LANDLINE_NUMBER = getIntent().getStringExtra("landLineNumber");
        _ACCOUNT_NUMBER = getIntent().getStringExtra("accountNumber");
        _OPERATOR_NAME = getIntent().getStringExtra("operatorName");
        _OPERATOR_IMAGE = getIntent().getStringExtra("operatorImage");
        _SPECIAL_OR_TOP_UP = getIntent().getStringExtra("operatorId");
        _CONSUMER_NAME = getIntent().getStringExtra("consumer_name");
        _PARTIAL_PAY = getIntent().getStringExtra("partial_pay");
        operatorDetailId = getIntent().getStringExtra("operatorDetail");

        switch (_TYPE) {
            case "4":
                toolbar_title.setText("Broadband Bill");
                break;
            case "10":
                toolbar_title.setText("Water Bill");
                break;
            case "11":
                toolbar_title.setText("Insurance Payment");
                break;
            case "9":
                toolbar_title.setText("Gas Bill");
                break;
            case "3":
                toolbar_title.setText("Electricity Bill");
                break;
        }

        tvAmount.setText(Functions.CURRENCY_SYMBOL_USER + _AMOUNT);
        tvName.setText(_CONSUMER_NAME);
        tvOperatorName.setText(_OPERATOR_NAME);
        tvDate.setText((_BILL_DATE.equalsIgnoreCase("NA") ||
                _BILL_DATE.equalsIgnoreCase("N.A") ||
                _BILL_DATE.equalsIgnoreCase("N/A"))
                ? "NA" : M.changeDateFormatDD(_BILL_DATE + " 00:00:00"));
        tvDueDate.setText((_DUE_DATE.equalsIgnoreCase(
                "NA") ||
                _DUE_DATE.equalsIgnoreCase("N.A") ||
                _DUE_DATE.equalsIgnoreCase("N/A")) ?
                "NA" :
                M.changeDateFormatDD(_DUE_DATE + " 00:00:00"));
        if (_TYPE.equalsIgnoreCase("10")) {
            tvAccountNumber.setText(_ACCOUNT_NUMBER);
            tvAccountNumber.setVisibility(View.GONE);
        } else if (_TYPE.equalsIgnoreCase("9")) {
            tvAccountNumber.setText(_ACCOUNT_NUMBER);
            tvAccountNumber.setVisibility(View.VISIBLE);
        } else if (_TYPE.equalsIgnoreCase("4")) {
            tvAccountNumber.setText(_ACCOUNT_NUMBER);
            tvAccountNumber.setVisibility(View.GONE);
        } else if (_TYPE.equalsIgnoreCase("11")) {
            tvAccountNumber.setText(_ACCOUNT_NUMBER);
            tvAccountNumber.setVisibility(View.GONE);
        } else if (_TYPE.equalsIgnoreCase("3")) {
            tvAccountNumber.setText(_ACCOUNT_NUMBER);
            tvAccountNumber.setVisibility(View.GONE);
        } else {
            tvAccountNumber.setText(_ACCOUNT_NUMBER);
            tvAccountNumber.setVisibility(View.VISIBLE);
        }

        tvLandlineNumber.setText(_LANDLINE_NUMBER);
        if (_PARTIAL_PAY.equalsIgnoreCase("N")) {
            etAmount.setText(_AMOUNT);
            etAmount.setSelection(etAmount.length());
            _AMOUNT = etAmount.getText().toString();
            llAmountLayout.setVisibility(View.GONE);
        } else {
            etAmount.setText(_AMOUNT);
            etAmount.setSelection(etAmount.length());
            _AMOUNT = etAmount.getText().toString();
            llAmountLayout.setVisibility(View.VISIBLE);
        }
        Picasso.get().load(_OPERATOR_IMAGE).error(R.drawable.app_icon).into(imageOperator);
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                M.removeZero(s);
            }
        });
    }

    private void setListeners() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BillFetchActivity.this, LandlineRechargeConfirmationActivity.class);
                intent.putExtra("amount", etAmount.getText().toString());
                intent.putExtra("billDate", tvDate.getText().toString());
                intent.putExtra("dueDate", tvDueDate.getText().toString());
                intent.putExtra("type", _TYPE);
                intent.putExtra("label", _LABEL);
                intent.putExtra("DOB", _DOB);
                intent.putExtra("landLineNumber", _LANDLINE_NUMBER);
                intent.putExtra("accountNumber", _ACCOUNT_NUMBER);
                intent.putExtra("operatorName", _OPERATOR_NAME);
                intent.putExtra("operatorId", _SPECIAL_OR_TOP_UP);
                intent.putExtra("operatorDetail", operatorDetailId);
                intent.putExtra("operatorImage", _OPERATOR_IMAGE);



                startActivity(intent);
            }
        });
    }
}
