package com.vynkpay.activity.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityChoosePaymentCBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.RequestCashResponse;
import com.vynkpay.utils.M;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosePaymentActivityC extends AppCompatActivity {
    ActivityChoosePaymentCBinding binding;
    ChoosePaymentActivityC ac;
    String cheqeImagepath;
    Dialog dialog1;
    DatePickerDialog picker;

    Calendar dateSelected = Calendar.getInstance();
    private DatePickerDialog datePickerDialog;
    private String dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment_c);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_payment_c);
        ac = ChoosePaymentActivityC.this;
        dialog1 = M.showDialog(ac, "", false, false);


        binding.idno2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(ac,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.idno2.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                picker.show();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.enterpdetail);
        binding.idno1.setText(Prefes.getUserName(ac));
        binding.amount1.setText(getIntent().getStringExtra("am1"));

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
            }
        });


        binding.uploadCheckLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity(null)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setFixAspectRatio(true)
                        .setAspectRatio(2, 1)
                        .setAllowRotation(false)
                        .setFlipVertically(false)
                        .setFlipHorizontally(false)
                        .setAllowFlipping(false)
                        .setMinCropWindowSize(1100, 700)
                        .start(ac);


            }
        });
    }

    private void validations() {
        if(binding.amount1.getText().toString().isEmpty()){
            Toast.makeText(ac, "Please Enter Amount", Toast.LENGTH_SHORT).show();
        }

        else if(binding.deposit.getText().toString().isEmpty()){
            Toast.makeText(ac, "Please Enter Value", Toast.LENGTH_SHORT).show();
        }

        else if(binding.idno2.getText().toString().isEmpty()){
            Toast.makeText(ac, "Please Enter ", Toast.LENGTH_SHORT).show();
        }

        else if(binding.amount2.getText().toString().isEmpty()){
            Toast.makeText(ac, "Please Enter Id", Toast.LENGTH_SHORT).show();
        }

        else if(binding.remark.getText().toString().isEmpty()){
            Toast.makeText(ac, "Please Enter Remarks", Toast.LENGTH_SHORT).show();
        }

        else if(cheqeImagepath==null){
            Toast.makeText(ac, "Please Upload Receipt", Toast.LENGTH_SHORT).show();
        }

        else {
            dialog1.show();
            File file = new File(cheqeImagepath);
            compress(cheqeImagepath);
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("upload_ch", file.getName(), imageBody);
            RequestBody bank = RequestBody.create(MediaType.parse("multipart/form-data"), binding.amount1.getText().toString());
            RequestBody benifiName = RequestBody.create(MediaType.parse("multipart/form-data"), binding.deposit.getText().toString());
            RequestBody accNo = RequestBody.create(MediaType.parse("multipart/form-data"), binding.idno2.getText().toString());
            RequestBody ifscName = RequestBody.create(MediaType.parse("multipart/form-data"), binding.amount2.getText().toString());
            RequestBody bankName = RequestBody.create(MediaType.parse("multipart/form-data"), binding.remark.getText().toString());
            MainApplication.getApiService().reqstCash(Prefes.getAccessToken(ac),bank,benifiName,accNo,ifscName,bankName,fileToUpload)
                    .enqueue(new Callback<RequestCashResponse>() {
                        @Override
                        public void onResponse(Call<RequestCashResponse> call, Response<RequestCashResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                dialog1.dismiss();
                                if (response.body().getStatus().equals("true")) {
                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ac, RequestSuccess.class).putExtra("msg",response.body().getMessage()).putExtra("typ","eCash"));
                                     finish();

                                } else if (response.body().getStatus().equals("false")) {
                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<RequestCashResponse> call, Throwable t) {
                            dialog1.dismiss();
                            Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (data != null) {
                    try {
                        CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        Uri resultUri = result.getUri();
                        cheqeImagepath = resultUri.getPath();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        Uri tempUri = getImageUri(ac, bitmap);
                        binding.cheqImage.setVisibility(View.VISIBLE);

                        //  binding.panImage.setImageURI(tempUri);


                        Bitmap bmImg = BitmapFactory.decodeFile(cheqeImagepath);
                        BitmapDrawable background = new BitmapDrawable(bmImg);
                        binding.cheqImage.setBackgroundDrawable(background);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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