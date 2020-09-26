package com.vynkpay.activity.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityKycUploadActiviyBinding;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.UpdateImageResponse;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KycUploadActiviy extends AppCompatActivity {
    ActivityKycUploadActiviyBinding binding;
    KycUploadActiviy ac;
    Boolean pan;
    String  panImagepath,aadharImagepath;
    Bitmap bitmap;
    Dialog dialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kyc_upload_activiy);
        ac = KycUploadActiviy.this;
        dialog1 = M.showDialog(KycUploadActiviy.this, "", false, false);
        clicks();
    }

    private void clicks() {
        // toolbar
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText("KYC");

        binding.nextLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (panImagepath == null) {
                    Toast.makeText(ac, "Upload Pan Document", Toast.LENGTH_SHORT).show();

                } else if (aadharImagepath == null) {
                    Toast.makeText(ac, "Upload Aadhar Document", Toast.LENGTH_SHORT).show();

                } else {

                    startActivity(new Intent(ac,KycBankActivity.class).putExtra("panImagepath",panImagepath)
                            .putExtra("aadharImagepath",aadharImagepath));




                }
            }

        });

        binding.uploadPanLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pan = true;
            /*    CropImage.activity(null)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                        .setInitialCropWindowPaddingRatio(0)
                        .setAspectRatio(1,1)
               // mCropImageView.setCropRect(new Rect(100, 300, 500, 1200)
                        .start(KycUploadActiviy.this);*/


                CropImage.activity(null)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setFixAspectRatio(true)
                        .setAspectRatio(2, 1)
                        .setAllowRotation(false)
                        .setFlipVertically(false)
                        .setFlipHorizontally(false)
                        .setAllowFlipping(false)
                        .setMinCropWindowSize(1100, 700)
                        .start(KycUploadActiviy.this);


            }
        });


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
                        .start(KycUploadActiviy.this);


            }
        });

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
                            panImagepath=resultUri.getPath();
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                            Uri tempUri = getImageUri(KycUploadActiviy.this, bitmap);
                            binding.panImage.setVisibility(View.VISIBLE);

                          //  binding.panImage.setImageURI(tempUri);


                            Bitmap bmImg = BitmapFactory.decodeFile(panImagepath);
                            BitmapDrawable background = new BitmapDrawable(bmImg);
                            binding.panImage.setBackgroundDrawable(background);




                        } else {
                            CropImage.ActivityResult result = CropImage.getActivityResult(data);
                            Uri resultUri = result.getUri();
                            aadharImagepath=resultUri.getPath();
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                            Uri tempUri = getImageUri(KycUploadActiviy.this, bitmap);
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

}
