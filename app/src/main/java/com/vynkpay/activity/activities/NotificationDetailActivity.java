package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.NotificationReadResponse;
import com.vynkpay.retrofit.model.NotificationResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.squareup.picasso.Picasso;
import com.vynkpay.utils.M;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationDetailActivity extends AppCompatActivity {
    @BindView(R.id.bigImage)
    ImageView bigImage;
    @BindView(R.id.title)
    NormalTextView title;
    @BindView(R.id.desc)
    NormalTextView desc;
    @BindView(R.id.toolbarnew)
    Toolbar toolbar;
    @BindView(R.id.toolbarTitlenew)
    NormalTextView toolbarTitle;
    Dialog serverDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail_rcg);
        ButterKnife.bind(NotificationDetailActivity.this);
        serverDialog = M.showDialog(NotificationDetailActivity.this, "", false, false);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbarTitle.setText("Notification Detail");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(getIntent()!=null) {
            Log.e("idnew",""+getIntent().getStringExtra("notificationid"));
            getNotificationDetail();
        }
    }
    private void getNotificationDetail() {
        serverDialog.show();
        MainApplication.getApiService().readNotification(Prefes.getAccessToken(NotificationDetailActivity.this), getIntent().getStringExtra("notificationid")).enqueue(new Callback<NotificationReadResponse>() {
            @Override
            public void onResponse(Call<NotificationReadResponse> call, Response<NotificationReadResponse> response) {
                if (response.isSuccessful() && response.body()!=null) {
                    serverDialog.dismiss();
                    if(response.body().getSuccess()){
                        List<NotificationReadResponse.Data> studentData = new ArrayList<>();
                        studentData.add(response.body().getData());
                        for (int i = 0; i < studentData.size(); i++) {
                            if (studentData.get(i).getNotification().size() > 0) {
                                if(studentData.get(i).getNotification().get(i).getImage()!=null) {
                                    Glide.with(NotificationDetailActivity.this).load(studentData.get(i).getNotification().get(i).getImage()).into(bigImage);
                                }
                                else {
                                    bigImage.setVisibility(View.GONE);
                                }
                                title.setText(studentData.get(i).getNotification().get(i).getSubject());
                                desc.setText(studentData.get(i).getNotification().get(i).getBody());
                            } } }
                    else {
                        serverDialog.dismiss();
                        Toast.makeText(NotificationDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } } }
            @Override
            public void onFailure(Call<NotificationReadResponse> call, Throwable throwable) {
                serverDialog.dismiss();
            }
        });
    }

}
