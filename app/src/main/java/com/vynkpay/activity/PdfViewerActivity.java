package com.vynkpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityPdfViewerBinding;
import com.vynkpay.models.PDFFileModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import java.io.File;


public class PdfViewerActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityPdfViewerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pdf_viewer);

        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbar.notifyIcon.setImageResource(R.drawable.ic_share_white);
        binding.toolbar.notifyIcon.setVisibility(View.INVISIBLE);

        binding.toolbar.notifyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //share(getRootDirPath());
            }
        });

        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.toolbar.toolbarTitle.setText("Career Plan");

        PRDownloader.initialize(this);

//        final DownloadTask downloadTask = new DownloadTask(PdfViewerActivity.this);
//        downloadTask.execute(BuildConfig.BASE_URL + "account/"+Prefes.getPDFFile(this));


        if (PDFFileModel.file_recognize.equals(Prefes.getPDFREC(PdfViewerActivity.this))){
            //old file
            Log.d("file_recognize", PDFFileModel.file_recognize+"//old");
            File downloadedFile = new File(getRootDirPath(), "careerPlan.pdf");
            if (downloadedFile.exists()){
                binding.progressLinear.setVisibility(View.GONE);
                // binding.toolbar.notifyIcon.setVisibility(View.VISIBLE);
                showPdfFromFile(downloadedFile);
            }else {
                binding.progressLinear.setVisibility(View.VISIBLE);
                downloadingFile();
            }
        }else {
            //new file to download
            Log.d("file_recognize", PDFFileModel.file_recognize+"//new");
            Prefes.savePDFREC(PDFFileModel.file_recognize,  PdfViewerActivity.this);
            binding.progressLinear.setVisibility(View.VISIBLE);
            downloadingFile();
        }


    }

    public void downloadingFile(){
        Log.d("uerre",BuildConfig.BASE_URL + "account/"+Prefes.getPDFFile(this)+"==");
        PRDownloader.download(BuildConfig.BASE_URL + "account/"+Prefes.getPDFFile(this), getRootDirPath(), "careerPlan.pdf")
                .build().setOnStartOrResumeListener(new OnStartOrResumeListener() {
            @Override
            public void onStartOrResume() {

            }

        }).setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(Progress progress) {
                String val= String.valueOf( progress.currentBytes * 100 / progress.totalBytes );
                Log.d("sddDownloadPRogress", val+"//");
                binding.percetageTV.setText(val + "%");
                binding.progressBar.setProgress(Integer.parseInt(val));
            }
        }).start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
               // binding.toolbar.notifyIcon.setVisibility(View.VISIBLE);
                binding.progressLinear.setVisibility(View.GONE);
                File downloadedFile = new File(getRootDirPath(), "careerPlan.pdf");
                showPdfFromFile(downloadedFile);

                Log.d("sdasdPathhts", getRootDirPath()+"//");
            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    public String getRootDirPath(){
        String path = this.getApplicationContext().getFilesDir() + "/careerPlanDir/";
        File file = new File(path);
        file.mkdirs();
        path += "careerPlan.pdf";
        return path;
    }

    private void showPdfFromFile(File file){
        binding.pdfView.fromFile(file)
                .password(null)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true).onPageError(new OnPageErrorListener() {
            @Override
            public void onPageError(int page, Throwable t) {

            }
        }).load();
    }


    public void share(String myFilePath){
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(myFilePath);

        if(fileWithinMyDir.exists()) {
            intentShareFile.setType("application/pdf");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+myFilePath));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(PdfViewerActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(PdfViewerActivity.this,PdfViewerActivity.this::finishAffinity);
        }
    }
}
