package com.vynkpay.activity.shops;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityAddBankDetailBinding;
import com.vynkpay.models.GetKycStatusResponse;
import com.vynkpay.models.UpdatebankResponse;
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

public class AddBankDetailActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    AddBankDetailActivity ac;
    ActivityAddBankDetailBinding binding;
    String[] country = {"Select Account Type", "Saving", "Current"};
    String bankType="", cheqeImagepath="";
    Dialog dialog1;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_bank_detail);
        ac = AddBankDetailActivity.this;
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
        dialog1 = M.showDialog(AddBankDetailActivity.this, "", false, false);
        init();
        getKyc();

    }

    void init(){
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(v -> finish());
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText("Add Bank Detail");

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
                        .start(AddBankDetailActivity.this);
            }
        });


        binding.approveLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (bankType == null) {
                    Toast.makeText(ac, "Please Select Bank Type", Toast.LENGTH_SHORT).show();
                } else if (binding.benifiName.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Enter Beneficiary Name", Toast.LENGTH_SHORT).show();
                } else if (binding.accNo.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Enter Account Number", Toast.LENGTH_SHORT).show();
                } else if (binding.ifscName.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Enter IFSC Code", Toast.LENGTH_SHORT).show();
                } else if (binding.bankName.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Enter Bank Name", Toast.LENGTH_SHORT).show();
                } else if (binding.bnkAdName.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Enter Bank address", Toast.LENGTH_SHORT).show();
                } /*else if (cheqeImagepath == null) {
                    Toast.makeText(ac, "Please Upload Cheque", Toast.LENGTH_SHORT).show();
                }*/ else {


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
                        MainApplication.getApiService().uploadImage(Prefes.getAccessToken(AddBankDetailActivity.this), fileToUpload, fileToUpload1)
                                .enqueue(new Callback<UpdateImageResponse>() {
                                    @Override
                                    public void onResponse(Call<UpdateImageResponse> call, Response<UpdateImageResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            dialog1.dismiss();

                                            if (response.body().getStatus().equals("true")) {
                                                Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                dialog1.show();
                                                File file = new File(cheqeImagepath);
                                                compress(cheqeImagepath);
                                                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("bnk_cheque", file.getName(), imageBody);
                                                RequestBody bank = RequestBody.create(MediaType.parse("multipart/form-data"), bankType);
                                                RequestBody benifiName = RequestBody.create(MediaType.parse("multipart/form-data"), binding.benifiName.getText().toString());
                                                RequestBody accNo = RequestBody.create(MediaType.parse("multipart/form-data"), binding.accNo.getText().toString());
                                                RequestBody ifscName = RequestBody.create(MediaType.parse("multipart/form-data"), binding.ifscName.getText().toString());
                                                RequestBody bankName = RequestBody.create(MediaType.parse("multipart/form-data"), binding.bankName.getText().toString());
                                                RequestBody bnkAdName = RequestBody.create(MediaType.parse("multipart/form-data"), binding.bankName.getText().toString());
                                                RequestBody branchCode = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                                                RequestBody pan = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                                                MainApplication.getApiService().uploadBank(Prefes.getAccessToken(AddBankDetailActivity.this),
                                                        fileToUpload, benifiName, bankName, bnkAdName, accNo, ifscName, branchCode, pan, bank)
                                                        .enqueue(new Callback<UpdatebankResponse>() {
                                                            @Override
                                                            public void onResponse(Call<UpdatebankResponse> call, Response<UpdatebankResponse> response) {
                                                                if (response.isSuccessful() && response.body() != null) {
                                                                    dialog1.dismiss();
                                                                    if (response.body().getStatus().equals("true")) {
                                                                        Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                                        //startActivity(new Intent(AddBankDetailActivity.this, KycActivity.class).putExtra("statusKyc", "1"));
                                                                        //finish();
                                                                    } else if (response.body().getStatus().equals("false")) {
                                                                        Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                                        //startActivity(new Intent(AddBankDetailActivity.this, KycActivity.class).putExtra("statusKyc", "1"));
                                                                    }

                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<UpdatebankResponse> call, Throwable t) {
                                                                dialog1.dismiss();
                                                                Toast.makeText(ac, t.getMessage()!=null ? t.getMessage() : "Error" , Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(ac, t.getMessage()!=null?t.getMessage():"Error", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    } else {
                        dialog1.show();
                        MultipartBody.Part fileToUpload;
                        if (cheqeImagepath==null || TextUtils.isEmpty(cheqeImagepath)){
                            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");

                            fileToUpload = MultipartBody.Part.createFormData("bnk_cheque", "", attachmentEmpty);
                        }else {
                            File file = new File(cheqeImagepath);
                            compress(cheqeImagepath);
                            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            fileToUpload = MultipartBody.Part.createFormData("bnk_cheque", file.getName(), imageBody);
                        }
                        RequestBody bank = RequestBody.create(MediaType.parse("multipart/form-data"), bankType);
                        RequestBody benifiName = RequestBody.create(MediaType.parse("multipart/form-data"), binding.benifiName.getText().toString());
                        RequestBody accNo = RequestBody.create(MediaType.parse("multipart/form-data"), binding.accNo.getText().toString());
                        RequestBody ifscName = RequestBody.create(MediaType.parse("multipart/form-data"), binding.ifscName.getText().toString());
                        RequestBody bankName = RequestBody.create(MediaType.parse("multipart/form-data"), binding.bankName.getText().toString());
                        RequestBody bnkAdName = RequestBody.create(MediaType.parse("multipart/form-data"), binding.bankName.getText().toString());
                        RequestBody branchCode = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                        RequestBody pan = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                        MainApplication.getApiService().uploadBank(Prefes.getAccessToken(AddBankDetailActivity.this),
                                fileToUpload, benifiName, bankName, bnkAdName, accNo, ifscName, branchCode, pan, bank)
                                .enqueue(new Callback<UpdatebankResponse>() {
                                    @Override
                                    public void onResponse(Call<UpdatebankResponse> call, Response<UpdatebankResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            dialog1.dismiss();
                                            if (response.body().getStatus().equals("true")) {
                                                Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                               /* if(sp.getString("value","").equals("Global") && Prefes.getisIndian(ac).equals("NO")){
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
                                                }*/

                                            } else if (response.body().getStatus().equals("false")) {
                                                Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();



                                               /* if(sp.getString("value","").equals("Global") && Prefes.getisIndian(ac).equals("NO")){
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
                                                }*/
                                            }

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UpdatebankResponse> call, Throwable t) {
                                        dialog1.dismiss();
                                        Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                    /* */
                }
            }
        });
        binding.spinnerBank.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinnerBank.setAdapter(aa);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position > 0) {
            bankType = country[position];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getKyc() {
        dialog1.show();
        MainApplication.getApiService().kycStatusMethod(Prefes.getAccessToken(AddBankDetailActivity.this)).enqueue(new Callback<GetKycStatusResponse>() {
            @Override
            public void onResponse(Call<GetKycStatusResponse> call, Response<GetKycStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dialog1.dismiss();
                    try {
                        if (response.body().getStatus().equalsIgnoreCase("true")) {
                            Log.d("kycc",new Gson().toJson(response.body().getData().getUserdata()));
                            if (response.body().getData().getUserdata().getCountryCode().equals("91")) {

                                GetKycStatusResponse.Data.KycStatus getKycStatus = response.body().getData().getKycStatus();
                                GetKycStatusResponse.Data.BankDetails getBankDetails = response.body().getData().getBankDetails();

                                String AccountType = response.body().getData().getBankDetails().getAccountType();
                                String NameInBank = response.body().getData().getBankDetails().getNameInBank();
                                String AccountNumber = response.body().getData().getBankDetails().getAccountNumber();
                                String IfscCode = response.body().getData().getBankDetails().getIfscCode();
                                String BankName = response.body().getData().getBankDetails().getBankName();
                                String BranchAddress = response.body().getData().getBankDetails().getBranchAddress();
                                String ChequeReceipt = response.body().getData().getBankDetails().getChequeReceipt();
                                binding.bankName.setText(BankName);
                                binding.benifiName.setText(NameInBank);
                                binding.accNo.setText(AccountNumber);
                                binding.ifscName.setText(IfscCode);
                                binding.bnkAdName.setText(BranchAddress);
                                try {
                                    searchAccountType(AccountType);
                                    //Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + ChequeReceipt).into(binding.cheqImage);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }

                                }   // Rejected
                            else {
                                Log.e("countryCode", "onResponse: " + response.body().getData().getUserdata().getCountryCode());

                                GetKycStatusResponse.Data.KycStatus getKycStatus = response.body().getData().getKycStatus();
                                GetKycStatusResponse.Data.BankDetails getBankDetails = response.body().getData().getBankDetails();
                                if (getKycStatus.getStaus().equals("0")) {   // have to do kyc

                                }  // new
                            }   // not indian
                        } else {
                            Toast.makeText(AddBankDetailActivity.this,response.body().getMessage()!=null?response.body().getMessage():"Error",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetKycStatusResponse> call, Throwable t) {
                dialog1.dismiss();
            }
        });
    }

    public void searchAccountType(String bankType){
        try {
            int count = 1;
            for (int i =0;i<country.length;i++){
                if (bankType.equalsIgnoreCase(country[i])){
                    count = i;
                }
            }
            binding.spinnerBank.setSelection(count);
        }catch (Exception e){
            e.printStackTrace();
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
                        Uri tempUri = getImageUri(AddBankDetailActivity.this, bitmap);
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