package com.vynkpay.activity.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityKycUploadActiviyBinding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class KycUploadActiviy extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    ActivityKycUploadActiviyBinding binding;
    KycUploadActiviy ac;
    Boolean pan;
    String  panImagepath,aadharImagepath;
    Bitmap bitmap;
    Dialog dialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(KycUploadActiviy.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(KycUploadActiviy.this,KycUploadActiviy.this::finishAffinity);
        }
    }
}
