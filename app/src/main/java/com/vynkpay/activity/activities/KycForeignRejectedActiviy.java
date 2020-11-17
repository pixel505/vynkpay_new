package com.vynkpay.activity.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityKycForeignRejectedActiviyBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
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

public class KycForeignRejectedActiviy extends AppCompatActivity {
    ActivityKycForeignRejectedActiviyBinding binding;
    KycForeignRejectedActiviy ac;
    Boolean pan;
    String panImagepath, aadharImagepath;
    Bitmap bitmap;
    Dialog dialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_kyc_foreign_rejected_activiy);
        ac = KycForeignRejectedActiviy.this;
        dialog1 = M.showDialog(KycForeignRejectedActiviy.this, "", false, false);

        click();
    }

    private void click() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText("KYC");



        if (getIntent() != null) {










            String nationIdStatus = getIntent().getStringExtra("nationIdStatus");
            String addressProffStatus = getIntent().getStringExtra("addressProffStatus");
            String addressProffPath = getIntent().getStringExtra("addressProffPath");
            String nationProffPath = getIntent().getStringExtra("nationProffPath");

            if(nationIdStatus.equals("1") && addressProffStatus.equals("2")){


                binding.panImage.setVisibility(View.VISIBLE);
                binding.uploadPanLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + addressProffPath).into(binding.aadharImage);

                binding.aadharImage.setVisibility(View.VISIBLE);
                binding.uploadAdharLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + addressProffPath).into(binding.aadharImage);

                binding.aadharPending.setVisibility(View.VISIBLE);
                binding.panPending.setVisibility(View.VISIBLE);

                binding.aadharPending.setText("Pending");
                binding.aadharPending.setTextColor(Color.parseColor("#E4D760"));


                binding.panPending.setText("Approve");
                binding.panPending.setTextColor(Color.parseColor("#5EB161"));

                binding.bLn.setVisibility(View.GONE);


            }

            if(nationIdStatus.equals("2") && addressProffStatus.equals("1")){

                binding.bLn.setVisibility(View.GONE);


                binding.panImage.setVisibility(View.VISIBLE);
                binding.uploadPanLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + addressProffPath).into(binding.aadharImage);

                binding.aadharImage.setVisibility(View.VISIBLE);
                binding.uploadAdharLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + addressProffPath).into(binding.aadharImage);
                binding.aadharPending.setVisibility(View.VISIBLE);
                binding.panPending.setVisibility(View.VISIBLE);

                binding.panPending.setText("Pending");
                binding.panPending.setTextColor(Color.parseColor("#E4D760"));


                binding.aadharPending.setText("Approve");
                binding.aadharPending.setTextColor(Color.parseColor("#5EB161"));

            }

            if (addressProffStatus.equals("2") ) {
                binding.aadharImage.setVisibility(View.VISIBLE);
                binding.uploadAdharLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + addressProffPath).into(binding.aadharImage);
                binding.aadharPending.setVisibility(View.VISIBLE);
                binding.aadharPending.setText("Approved");
                binding.aadharPending.setTextColor(Color.parseColor("#5EB161"));

            }


            if (addressProffStatus.equals("1") ) {
                binding.aadharImage.setVisibility(View.VISIBLE);
                binding.uploadAdharLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + addressProffPath).into(binding.aadharImage);
                binding.aadharPending.setVisibility(View.VISIBLE);
                binding.aadharPending.setText("Pending");
                binding.aadharPending.setTextColor(Color.parseColor("#E4D760"));



            }

            else if (addressProffStatus.equals("3")) {
                binding.aadharPending.setVisibility(View.VISIBLE);
                binding.aadharPending.setText("Rejected");
                binding.aadharPending.setTextColor(Color.parseColor("#B10D25"));
                binding.uploadAdharLn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pan = false;
                        CropImage.activity(null)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setFixAspectRatio(true)
                                .setAspectRatio(2, 1)
                                .setAllowRotation(false)
                                .setFlipVertically(false)
                                .setFlipHorizontally(false)
                                .setAllowFlipping(false)
                                .setMinCropWindowSize(1100, 700)
                                .start(KycForeignRejectedActiviy.this);

                    }
                });

                binding.nextLn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (aadharImagepath == null) {
                            Toast.makeText(ac, "Upload Aadhar Document", Toast.LENGTH_SHORT).show();

                        } else {
                            dialog1.show();
                            File file = new File(aadharImagepath);
                            compress(aadharImagepath);
                            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("address_proof", file.getName(), imageBody);
                            MainApplication.getApiService().uploadAddressImage(Prefes.getAccessToken(KycForeignRejectedActiviy.this), fileToUpload)
                                    .enqueue(new Callback<UpdateImageResponse>() {
                                        @Override
                                        public void onResponse(Call<UpdateImageResponse> call, Response<UpdateImageResponse> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                dialog1.dismiss();
                                                if (response.body().getStatus().equals("true")) {
                                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    finish();

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
                        }
                    }
                });
            }


            if ( nationIdStatus.equals("2")) {
                binding.panImage.setVisibility(View.VISIBLE);
                binding.uploadPanLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + nationProffPath).into(binding.panImage);
                binding.panPending.setVisibility(View.VISIBLE);
                binding.panPending.setText("Approved");
                binding.panPending.setTextColor(Color.parseColor("#5EB161"));


            }




            if (nationIdStatus.equals("1") ) {
                binding.panImage.setVisibility(View.VISIBLE);
                binding.uploadPanLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + nationProffPath).into(binding.panImage);
                binding.panPending.setText("Pending");
                binding.panPending.setTextColor(Color.parseColor("#E4D760"));

                binding.panPending.setVisibility(View.VISIBLE);



            }


            else if (nationIdStatus.equals("3")) {
                binding.panPending.setVisibility(View.VISIBLE);

                binding.panPending.setText("Rejected");
                binding.panPending.setTextColor(Color.parseColor("#B10D25"));
                binding.uploadPanLn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pan = true;
                        CropImage.activity(null)
                                .setCropShape(CropImageView.CropShape.RECTANGLE)
                                .setFixAspectRatio(true)
                                .setAspectRatio(2, 1)
                                .setAllowRotation(false)
                                .setFlipVertically(false)
                                .setFlipHorizontally(false)
                                .setAllowFlipping(false)
                                .setMinCropWindowSize(1100, 700)
                                .start(KycForeignRejectedActiviy.this);


                    }
                });


                binding.nextLn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (panImagepath == null) {
                            Toast.makeText(ac, "Upload Pan Document", Toast.LENGTH_SHORT).show();

                        } else {
                            dialog1.show();
                            File file = new File(panImagepath);
                            compress(panImagepath);
                            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("national_id", file.getName(), imageBody);
                            MainApplication.getApiService().uploadNationalImage(Prefes.getAccessToken(KycForeignRejectedActiviy.this), fileToUpload)
                                    .enqueue(new Callback<UpdateImageResponse>() {
                                        @Override
                                        public void onResponse(Call<UpdateImageResponse> call, Response<UpdateImageResponse> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                dialog1.dismiss();
                                                if (response.body().getStatus().equals("true")) {
                                                    finish();
                                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                        }
                    }
                });


                if(nationIdStatus.equals("3") && addressProffStatus.equals("3")){


                    binding.aadharPending.setVisibility(View.VISIBLE);
                    binding.panPending.setVisibility(View.VISIBLE);

                    binding.panPending.setText("Rejected");
                    binding.panPending.setTextColor(Color.parseColor("#B10D25"));


                    binding.aadharPending.setText("Rejected");
                    binding.aadharPending.setTextColor(Color.parseColor("#B10D25"));

                    binding.uploadPanLn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pan = true;
                            CropImage.activity(null)
                                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                                    .setFixAspectRatio(true)
                                    .setAspectRatio(2, 1)
                                    .setAllowRotation(false)
                                    .setFlipVertically(false)
                                    .setFlipHorizontally(false)
                                    .setAllowFlipping(false)
                                    .setMinCropWindowSize(1100, 700)
                                    .start(KycForeignRejectedActiviy.this);


                        }
                    });


                    binding.uploadAdharLn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pan = false;
                            CropImage.activity(null)
                                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                                    .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                                    .setInitialCropWindowPaddingRatio(0)                                    .start(KycForeignRejectedActiviy.this);


                        }
                    });



                    binding.nextLn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (panImagepath == null) {
                                Toast.makeText(ac, "Upload Pan Document", Toast.LENGTH_SHORT).show();

                            } else if (aadharImagepath == null) {
                                Toast.makeText(ac, "Upload Aadhar Document", Toast.LENGTH_SHORT).show();

                            } else {

                                dialog1.show();
                                File file = new File(panImagepath);
                                File file1 = new File(aadharImagepath);
                                compress(panImagepath);
                                compress(aadharImagepath);
                                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                RequestBody imageBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("national_id", file.getName(), imageBody);
                                MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("address_proof", file1.getName(), imageBody1);
                                MainApplication.getApiService().uploadImage(Prefes.getAccessToken(KycForeignRejectedActiviy.this), fileToUpload, fileToUpload1)
                                        .enqueue(new Callback<UpdateImageResponse>() {
                                            @Override
                                            public void onResponse(Call<UpdateImageResponse> call, Response<UpdateImageResponse> response) {
                                                if (response.isSuccessful() && response.body() != null) {
                                                    dialog1.dismiss();

                                                    if (response.body().getStatus().equals("true")) {
                                                        finish();
                                                        Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                            }
                        }

                    });

                }





            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (data != null) {


                    try {
                        if (pan == true) {
                            CropImage.ActivityResult result = CropImage.getActivityResult(data);
                            Uri resultUri = result.getUri();
                            panImagepath = resultUri.getPath();
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                            Uri tempUri = getImageUri(KycForeignRejectedActiviy.this, bitmap);
                            binding.panImage.setVisibility(View.VISIBLE);

                            //  binding.panImage.setImageURI(tempUri);


                            Bitmap bmImg = BitmapFactory.decodeFile(panImagepath);
                            BitmapDrawable background = new BitmapDrawable(bmImg);
                            binding.panImage.setBackgroundDrawable(background);


                        } else {
                            CropImage.ActivityResult result = CropImage.getActivityResult(data);
                            Uri resultUri = result.getUri();
                            aadharImagepath = resultUri.getPath();
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                            Uri tempUri = getImageUri(KycForeignRejectedActiviy.this, bitmap);
                            binding.aadharImage.setVisibility(View.VISIBLE);
                            Bitmap bmImg = BitmapFactory.decodeFile(aadharImagepath);
                            BitmapDrawable background = new BitmapDrawable(bmImg);
                            binding.aadharImage.setBackgroundDrawable(background);

                        }
                        /**/

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
