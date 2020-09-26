package com.vynkpay.activity.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.databinding.ActivityKycForeignBankBinding;
import com.vynkpay.models.UpdatebankResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetBitAddressResponse;
import com.vynkpay.retrofit.model.UpdateImageResponse;
import com.vynkpay.utils.M;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KycForeignBankActivity extends AppCompatActivity {
    ActivityKycForeignBankBinding binding;
    KycForeignBankActivity ac;
    Dialog dialog1;
 SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_kyc_foreign_bank);
        ac = KycForeignBankActivity.this;
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);

        dialog1 = M.showDialog(KycForeignBankActivity.this, "", false, false);

        clicks();
    }

    private void clicks() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText("KYC");


        binding.approveLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (binding.benifiName.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Enter BTC Address", Toast.LENGTH_SHORT).show();
                } else {


                    if (getIntent().getStringExtra("panImagepath") != null && getIntent().getStringExtra("aadharImagepath") != null) {
                        String panImagepath = getIntent().getStringExtra("panImagepath");
                        String aadharImagepath = getIntent().getStringExtra("aadharImagepath");


                        dialog1.show();
                        File file = new File(panImagepath);
                        File file1 = new File(aadharImagepath);
                        compress(panImagepath);
                        compress(aadharImagepath);
                        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        RequestBody imageBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("national_id", file.getName(), imageBody);
                        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("address_proof", file1.getName(), imageBody1);
                        MainApplication.getApiService().uploadImage(Prefes.getAccessToken(KycForeignBankActivity.this), fileToUpload, fileToUpload1)
                                .enqueue(new Callback<UpdateImageResponse>() {
                                    @Override
                                    public void onResponse(Call<UpdateImageResponse> call, Response<UpdateImageResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            dialog1.dismiss();

                                            if (response.body().getStatus().equals("true")) {
                                                Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                MainApplication.getApiService().bitAddress(Prefes.getAccessToken(KycForeignBankActivity.this), binding.benifiName.getText().toString())
                                                        .enqueue(new Callback<GetBitAddressResponse>() {
                                                            @Override
                                                            public void onResponse(Call<GetBitAddressResponse> call, Response<GetBitAddressResponse> response) {
                                                                if (response.isSuccessful() && response.body() != null) {
                                                                    if (response.body().getStatus().equals("true")) {
                                                                        Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                                        if(sp.getString("value","").equals("Global") && Prefes.getisIndian(ac).equals("NO")){
                                                                            startActivity(new Intent(ac, HomeActivity.class).putExtra("Country", "Global"));
                                                                        }

                                                                        else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(ac).equals("YES")){
                                                                            startActivity(new Intent(ac, HomeActivity.class).putExtra("Country", "India"));
                                                                        }


                                                                        else if(sp.getString("value","").equals("India") && Prefes.getisIndian(ac).equals("YES")){
                                                                            startActivity(new Intent(ac, HomeActivity.class).putExtra("Country", "India"));
                                                                        }

                                                                        else if(sp.getString("value","").equals("India") && Prefes.getisIndian(ac).equals("NO")){
                                                                            startActivity(new Intent(ac, HomeActivity.class).putExtra("Country", "Global"));
                                                                        }
                                                                    }}
                                                            }

                                                            @Override
                                                            public void onFailure(Call<GetBitAddressResponse> call, Throwable t) {

                                                            }
                                                        });


                                            } else if (response.body().getStatus().equals("false")) {
                                                Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UpdateImageResponse> call, Throwable t) {
                                        dialog1.dismiss();
                                        Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                    } else {

                    }







                    /* */
                }

            }
        });

    }


    private File compress(String mProfileImage) {
        File compressedFile = null;
        File file = new File(mProfileImage);

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(mProfileImage);

            compressedFile = new File(Environment.getExternalStorageDirectory().toString() + "/health4cash" + file.getName());
            if (!compressedFile.exists())
                compressedFile.mkdirs();

            FileOutputStream out = new FileOutputStream(compressedFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressedFile;
    }
}
