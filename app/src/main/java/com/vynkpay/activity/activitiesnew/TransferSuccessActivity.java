package com.vynkpay.activity.activitiesnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityTransferSuccessBinding;

public class TransferSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityTransferSuccessBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    NormalButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_transfer_success);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        submitButton = findViewById(R.id.submitButton);
        toolbarTitle.setText(getString(R.string.transferWallet));
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        if(getIntent()!=null){
            if(getIntent().getStringExtra("typ").equals("Bonus")){
                binding.messageText.setText("Bonus Transferred Successfully");
                //binding.kycImage.setImageResource(R.drawable.bonuswall);
            }

            if(getIntent().getStringExtra("typ").equals("MCash")){
                binding.messageText.setText("MCash Transferred Successfully");
                //binding.kycImage.setImageResource(R.drawable.mcashwall);
            }

            if(getIntent().getStringExtra("typ").equals("eCash")){
                binding.messageText.setText("ECash Request Submitted Successfully");
                //binding.kycImage.setImageResource(R.drawable.requestcash);
            }

            if(getIntent().getStringExtra("typ").equals("vCash")){
                binding.messageText.setText("VCash Request Submitted Successfully");
                //binding.kycImage.setImageResource(R.drawable.requestcash);
            }


        }
        else {

        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == submitButton){
            TransferSuccessActivity.this.finish();
        }
    }
}