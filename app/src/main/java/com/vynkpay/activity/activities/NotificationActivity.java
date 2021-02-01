package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import com.squareup.picasso.Picasso;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.DeleteNotificationResponse;
import com.vynkpay.retrofit.model.NotificationReadResponse;
import com.vynkpay.retrofit.model.NotificationResponse;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    @BindView(R.id.toolbarnew)
    Toolbar toolbar;
    @BindView(R.id.noLayout)
    LinearLayout noLayout;
    Dialog dialog;
    @BindView(R.id.toolbarTitlenew)
    NormalTextView toolbarTitle;
    @BindView(R.id.notificationRecyclerView)
    RecyclerView notificationRecyclerView;
    @BindView(R.id.mainNotiLn)
    LinearLayout mainNotiLn;
    NotificationAdapter notificationAdapter;
    Handler handler = new Handler();
    int apiDelayed = 5 * 1000; //1 second=1000 milisecond, 5*1000=5seconds
    Runnable runnable;
    Dialog serverDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_rcg);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        ButterKnife.bind(NotificationActivity.this);
        setSupportActionBar(toolbar);
        serverDialog = M.showDialog(NotificationActivity.this, "", false, false);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbarTitle.setText("Notifications");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        serverDialog.show();

    }

    private void getNotifications() {
        MainApplication.getApiService().getNotifications(Prefes.getAccessToken(NotificationActivity.this)).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {

                        List<NotificationResponse.DataBean> studentData = new ArrayList<>();
                        studentData.add(response.body().getData());
                        noLayout.setVisibility(View.GONE);
                        for (int i = 0; i < studentData.size(); i++) {
                            if (studentData.get(i).getNotification().size() > 0) {
                                noLayout.setVisibility(View.GONE);
                                notificationAdapter = new NotificationAdapter(NotificationActivity.this, studentData.get(i).getNotification());
                                notificationRecyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
                                notificationRecyclerView.setAdapter(notificationAdapter);
                                notificationAdapter.notifyDataSetChanged();
                            } else {
                                noLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {

            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                //do your function;
                getNotifications();
                serverDialog.dismiss();
                handler.postDelayed(runnable, apiDelayed);
            }
        }, apiDelayed); // so basically after your getHeroes(), from next time it will be 5 sec repeated
        MySingleton.getInstance(NotificationActivity.this).setConnectivityListener(this);

    }
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(NotificationActivity.this,NotificationActivity.this::finishAffinity);
        }
    }

    class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
        List<NotificationResponse.DataBean.NotificationBean> notificationListData;
        Context context;

        public NotificationAdapter(Context context, List<NotificationResponse.DataBean.NotificationBean> notificationListData) {
            this.context = context;
            this.notificationListData = notificationListData;
        }

        @NonNull
        @Override
        public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_notification_rcg, parent, false);
            return new NotificationAdapter.MyViewHolder(v);
        }


        @Override
        public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, final int position) {
            NotificationResponse.DataBean.NotificationBean data = notificationListData.get(position);
            holder.description.setText(data.getBody());
            holder.title.setText(data.getSubject());
            holder.time.setText(Functions.changeDateFormat(data.getCreate_date(), "yyyy-MM-dd hh:mm:ss", "dd-MMM-yyyy hh:mm aa"));
            if (data.getRead_status().equals("0")) {

            } else {
                holder.cardClick.setAlpha((float) 0.5);
            }
            holder.popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, v);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.pop_menu, popup.getMenu());
                    popup.show();
                    if (data.getRead_status().equals("0")) {
                        popup.getMenu().findItem(R.id.markread).setVisible(true);
                    } else {
                        popup.getMenu().findItem(R.id.markread).setVisible(false);
                    }
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getTitle().equals("Delete")) {
                                MainApplication.getApiService().deleteNotification(Prefes.getAccessToken(context), data.getId()).enqueue(new Callback<DeleteNotificationResponse>() {
                                    @Override
                                    public void onResponse(Call<DeleteNotificationResponse> call, Response<DeleteNotificationResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            if (response.body().isSuccess()) {
                                                getNotifications();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DeleteNotificationResponse> call, Throwable throwable) {

                                    }
                                });

                            } else if (item.getTitle().equals("Mark as Read")) {
                                if (data.getRead_status().equals("0")) {
                                    MainApplication.getApiService().readNotification(Prefes.getAccessToken(context), data.getId()).enqueue(new Callback<NotificationReadResponse>() {
                                        @Override
                                        public void onResponse(Call<NotificationReadResponse> call, Response<NotificationReadResponse> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                if (response.body().getSuccess()) {
                                                    holder.cardClick.setAlpha((float) 0.5);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<NotificationReadResponse> call, Throwable throwable) {

                                        }
                                    });
                                } else {


                                }
                            } else if (item.getTitle().equals("Detail")) {
                                holder.cardClick.setAlpha((float) 0.5);
                                MainApplication.getApiService().readNotification(Prefes.getAccessToken(context), data.getId()).enqueue(new Callback<NotificationReadResponse>() {
                                    @Override
                                    public void onResponse(Call<NotificationReadResponse> call, Response<NotificationReadResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            if (response.body().getSuccess()) {
                                                holder.cardClick.setAlpha((float) 0.5);
                                                startActivity(new Intent(context, NotificationDetailActivity.class).putExtra("notificationid",data.getId()));

                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<NotificationReadResponse> call, Throwable throwable) {

                                    }
                                });

                            }
                            return true;
                        }
                    });
                }
            });
            if (data.getImage() != null) {
                Picasso.get().load(data.getImage()).error(R.drawable.app_icon).into(holder.notificationImage);
            } else {
                Picasso.get().load(R.drawable.app_icon).error(R.drawable.app_icon).into(holder.notificationImage);

            }

            holder.cardClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.cardClick.setAlpha((float) 0.5);
                    MainApplication.getApiService().readNotification(Prefes.getAccessToken(context), data.getId()).enqueue(new Callback<NotificationReadResponse>() {
                        @Override
                        public void onResponse(Call<NotificationReadResponse> call, Response<NotificationReadResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getSuccess()) {
                                    holder.cardClick.setAlpha((float) 0.5);
                                    startActivity(new Intent(context, NotificationDetailActivity.class).putExtra("notificationid",data.getId()));
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<NotificationReadResponse> call, Throwable t) {
                            Log.d("errorStr", t.getMessage()!=null ? t.getMessage() : "Errorr");
                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return notificationListData.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            CircleImageView notificationImage;
            NormalTextView title, description, time;
            LinearLayout cardClick;
            ImageButton popup;

            public MyViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.notificationTitle);
                description = itemView.findViewById(R.id.notificationDescription);
                time = itemView.findViewById(R.id.notificationTime);
                notificationImage = itemView.findViewById(R.id.notificationImage);
                cardClick = itemView.findViewById(R.id.cardClick);
                popup = itemView.findViewById(R.id.popup);
            }
        }
    }
}
