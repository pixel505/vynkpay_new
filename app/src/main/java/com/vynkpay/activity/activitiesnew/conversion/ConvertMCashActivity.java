package com.vynkpay.activity.activitiesnew.conversion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Toast;
import com.vynkpay.R;
import com.vynkpay.activity.activitiesnew.DecimalDigitsInputFilter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityConvertMCashBinding;
import com.vynkpay.utils.M;

public class ConvertMCashActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityConvertMCashBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    Dialog serverDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_convert_m_cash);
        serverDialog = M.showDialog(ConvertMCashActivity.this, "", false, false);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getString(R.string.convertmcashtext));
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.submitButton.setOnClickListener(this);
        try {
            binding.amountET.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(12,2)});
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == binding.submitButton){
            Toast.makeText(ConvertMCashActivity.this, "coming soon!1", Toast.LENGTH_SHORT).show();
        }
    }
}