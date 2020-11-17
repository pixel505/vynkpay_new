package com.vynkpay.activity.activitiesnew.conversion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityConvertSuccessBinding;

public class ConvertSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityConvertSuccessBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle,tvMessage;
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_convert_success);
        tvMessage.setText("");
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getString(R.string.convertmcashtext));
        try {
            if (getIntent().hasExtra("message")){
                message = getIntent().getStringExtra("message");
                tvMessage.setText(message);
            }else {
                tvMessage.setText("");
            }
        }catch (Exception e){
            tvMessage.setText("");
            e.printStackTrace();
        }
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.submitButton){
            ConvertSuccessActivity.this.finish();
        }
    }
}