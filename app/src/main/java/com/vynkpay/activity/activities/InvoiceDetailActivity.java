package com.vynkpay.activity.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityInvoiceDetailBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetInvoiceDetailResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceDetailActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    ActivityInvoiceDetailBinding binding;
    InvoiceDetailActivity ac;
    File imagePath;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invoice_detail);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        ac = InvoiceDetailActivity.this;
        dialog = M.showDialog(ac, "", false, false);
        click();
    }
    private void click() {
        if(getIntent()!=null){
            String id=getIntent().getStringExtra("invoiceId");
            getInvoiceDetail(id);
        }
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.invoice);
    }
    public Bitmap takeScreenshot() {
        binding.invoiceLn.setDrawingCacheEnabled(true);
        return binding.invoiceLn.getDrawingCache();
    }
    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/Pictures/"+String.format("%1$"+5+ "s", "picture")+".png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(ac, "Pdf Downloaded !!", Toast.LENGTH_SHORT).show();
            Document document = new Document();
            try {
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = "Invoice-"+ n +".pdf";
                File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"vynkpay");
                directory.mkdirs();
                PdfWriter.getInstance(document, new FileOutputStream(directory+"/" +fname)); //  Change pdf's name.
                document.open();
                Image image = Image.getInstance(Environment.getExternalStorageDirectory() + "/Pictures/"+String.format("%1$"+5+ "s", "picture")+".png");  // Change image's name and extension.
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                        - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                image.scalePercent(scaler);
                image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
                document.add(image);
                document.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
        dialog.dismiss();
        finish();
    }
    private void getInvoiceDetail(String id) {
        MainApplication.getApiService().getInvoiceDetail(Prefes.getAccessToken(ac),id).enqueue(new Callback<GetInvoiceDetailResponse>() {
            @Override
            public void onResponse(Call<GetInvoiceDetailResponse> call, Response<GetInvoiceDetailResponse> response) {

                if(response.isSuccessful() && response.body()!=null){
                    Log.d("invoiceDetailloOFF", new Gson().toJson(response.body().getData())+"//");
                    if(response.body().getStatus().equals("true")){
                        binding.invoice.setText(response.body().getData().getInvoiceNumber());
                        try {
                            DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                            Date d = f.parse(response.body().getData().getCreatedDate());
                            DateFormat date = new SimpleDateFormat("dd-MM-yyy",Locale.getDefault());
                            binding.date.setText(date.format(d));
                            System.out.println("Date: " + date.format(d));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        binding.name.setText(response.body().getData().getName());
                        binding.address.setText(response.body().getData().getAddress());
                        binding.adress1.setText(response.body().getData().getAddressLine2());
                        binding.mobile.setText("+"+response.body().getData().getMobileCode()+" "+response.body().getData().getMobile());
                        binding.email.setText(response.body().getData().getEmail());
                        binding.desc.setText(response.body().getData().getPTitle());
                        binding.qty.setText(response.body().getData().getMode());
                        binding.price.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getPpPrice());
                        binding.total.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getAmount());
                        binding.subTotal.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getPpPrice());
                        binding.tax.setText(response.body().getData().getPpGst());
                        binding.taxtotal.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getPpTotalPrice());
                        binding.packageTypeTV.setText(response.body().getData().getPp_type());
                        if(response.body().getData().getPaymentVia().equals("0")){
                            binding.paymetmethod.setText("Wallet");
                        }
                        else {
                            binding.paymetmethod.setText("Bit");
                        }
                        binding.download.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.show();
                                Bitmap bitmap = takeScreenshot();
                                saveBitmap(bitmap);
                            }
                        });
                    } else {
                        Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<GetInvoiceDetailResponse> call, Throwable t) {
                Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(InvoiceDetailActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(InvoiceDetailActivity.this,InvoiceDetailActivity.this::finishAffinity);
        }
    }
}