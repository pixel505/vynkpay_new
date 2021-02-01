package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.fragment.AddNewBeneficiaryFragment;
import com.vynkpay.fragment.SeeAllBeneficiaryFragment;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddBeneficiaryActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    @BindView(R.id.llAddNewLayout)
    LinearLayout llAddNewLayout;
    @BindView(R.id.llSeeAllBeneficiaryLayout)
    LinearLayout llSeeAllBeneficiaryLayout;
    String accessToken;
    JSONObject userPreference;
    Dialog dialog;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String TYPE_OF_BENEFICIARY;
    @BindView(R.id.seeAllTV)
    NormalTextView seeAllTV;
    @BindView(R.id.addNewTV)
    NormalTextView addNewTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_add_beneficiary_rcg);
        ButterKnife.bind(AddBeneficiaryActivity.this);
        setListeners();
        dialog = M.showDialog(AddBeneficiaryActivity.this, "", false, false);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle.setText("Select Beneficiary");


        TYPE_OF_BENEFICIARY = getIntent().getStringExtra(ApiParams.typeOfSelection);
        Log.i(">>value", "onCreate: " + TYPE_OF_BENEFICIARY + "");

        try {
            userPreference = new JSONObject(Prefes.getUserData(AddBeneficiaryActivity.this));
            accessToken = Prefes.getAccessToken(AddBeneficiaryActivity.this);
            Log.i(">>access_token", "onCreate: " + accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (TYPE_OF_BENEFICIARY.equals("0")) {
            findAdd();
        } else {
            findAll();
        }
    }


    private void setListeners() {
        llAddNewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAdd();
            }
        });
        llSeeAllBeneficiaryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findAll();
            }
        });
    }

    private void findAdd() {
        llAddNewLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_yellow));
        llSeeAllBeneficiaryLayout.setBackgroundDrawable(null);
        inflateFragment(new AddNewBeneficiaryFragment());
        addNewTV.setTextColor(ContextCompat.getColor(AddBeneficiaryActivity.this, R.color.colorPrimary));
        seeAllTV.setTextColor(ContextCompat.getColor(AddBeneficiaryActivity.this, R.color.white));
    }

    private void findAll() {
        llSeeAllBeneficiaryLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_yellow));
        llAddNewLayout.setBackgroundDrawable(null);
        inflateFragment(new SeeAllBeneficiaryFragment());
        addNewTV.setTextColor(ContextCompat.getColor(AddBeneficiaryActivity.this, R.color.white));
        seeAllTV.setTextColor(ContextCompat.getColor(AddBeneficiaryActivity.this, R.color.colorPrimary));
    }

    private void inflateFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SeeAllBeneficiaryFragment){
            llSeeAllBeneficiaryLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_corner_yellow));
            llAddNewLayout.setBackgroundDrawable(null);
            addNewTV.setTextColor(ContextCompat.getColor(AddBeneficiaryActivity.this, R.color.white));
            seeAllTV.setTextColor(ContextCompat.getColor(AddBeneficiaryActivity.this, R.color.colorPrimary));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(AddBeneficiaryActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(AddBeneficiaryActivity.this,AddBeneficiaryActivity.this::finishAffinity);
        }
    }
}
