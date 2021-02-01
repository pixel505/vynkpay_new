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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityGenerateTicketBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GenerateticketResponse;
import com.vynkpay.retrofit.model.GetTicketDepartment;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateTicketActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PlugInControlReceiver.ConnectivityReceiverListener {
    ActivityGenerateTicketBinding binding;
    GenerateTicketActivity ac;
    Dialog dialog1;
    String[] categories = {"Choose Category", "General", "Technical", "Sales", "Admin", "Security", "Management"};
    String categoryString, cheqeImagepath;
    ArrayAdapter aa;
    ArrayList<String> mList = new ArrayList<String>();
    ArrayList<String> mListId = new ArrayList<String>();
    String orgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_generate_ticket);
        ac = GenerateTicketActivity.this;
        dialog1 = M.showDialog(GenerateTicketActivity.this, "", false, false);
        init();
    }

    private void init() {
        getDepartment();

        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.support);

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
                        .start(GenerateTicketActivity.this);


            }
        });
        binding.approveLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orgId == null) {
                    Toast.makeText(ac, "Please Choose Category", Toast.LENGTH_SHORT).show();
                } else if (binding.bankName.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Enter Subject", Toast.LENGTH_SHORT).show();

                } else if (binding.bnkAdName.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Enter Message", Toast.LENGTH_SHORT).show();

                } else {

                    dialog1.show();
                    List<MultipartBody.Part> parts = new ArrayList<>();
                    if(cheqeImagepath==null){
                        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                        parts.add(MultipartBody.Part.createFormData("image_id[]", "", imageBody));
                    }

                    else {
                        File file = new File(cheqeImagepath);
                        compress(cheqeImagepath);
                        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        parts.add(MultipartBody.Part.createFormData("image_id[]", file.getName(), imageBody));
                    }
                    RequestBody department = RequestBody.create(MediaType.parse("multipart/form-data"), orgId);
                    RequestBody subject = RequestBody.create(MediaType.parse("multipart/form-data"), binding.bankName.getText().toString());
                    RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), binding.bnkAdName.getText().toString());

                    MainApplication.getApiService().generateTicket(Prefes.getAccessToken(ac), parts, department, subject, body)
                            .enqueue(new Callback<GenerateticketResponse>() {
                                @Override
                                public void onResponse(Call<GenerateticketResponse> call, Response<GenerateticketResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        dialog1.dismiss();
                                        if (response.body().getStatus().equals("true")) {
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(GenerateTicketActivity.this, SupportTicketActivity.class));
                                            finish();
                                        } else if (response.body().getStatus().equals("false")) {
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(GenerateTicketActivity.this, SupportTicketActivity.class));
                                            finish();
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<GenerateticketResponse> call, Throwable t) {
                                    dialog1.dismiss();
                                    Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                }
            }
        });

        binding.spinnerBank.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list


    }

    private void getDepartment() {
        MainApplication.getApiService().getTicketDepartment(Prefes.getAccessToken(ac)).enqueue(new Callback<GetTicketDepartment>() {
            @Override
            public void onResponse(Call<GetTicketDepartment> call, Response<GetTicketDepartment> response) {
                if (response.isSuccessful() && response != null) {
                    if (response.body().getStatus().equals("true")) {
                        List<GetTicketDepartment.Datum> studentData = response.body().getData();
                        for (int i = 0; i < studentData.size(); i++) {
                            if (studentData.get(i).getDepartmentName() != null) {

                                mList.add(studentData.get(i).getTitle());
                                mListId.add(studentData.get(i).getId());

                                Log.e("list", "" + mList);
                                Log.e("listId", "" + mListId);


                            }

                        }

                        addData(mList);
                    }


                }
            }

            @Override
            public void onFailure(Call<GetTicketDepartment> call, Throwable t) {

            }
        });


    }

    private void addData(ArrayList<String> mList) {
        ArrayAdapter aa = new ArrayAdapter<>(this, R.layout.simple_spinner_item_new, mList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinnerBank.setAdapter(aa);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        categoryString = mList.get(position);
        orgId = mListId.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                        Uri tempUri = getImageUri(GenerateTicketActivity.this, bitmap);
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

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(GenerateTicketActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(GenerateTicketActivity.this,GenerateTicketActivity.this::finishAffinity);
        }
    }
}
