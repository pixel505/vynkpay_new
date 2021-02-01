package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.vynkpay.R;
import com.vynkpay.adapter.CloseTicketAdapter;
import com.vynkpay.adapter.OpenTicketAdapter;
import com.vynkpay.databinding.ActivitySupportTicketBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetOpenCloseTicket;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportTicketActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    ActivitySupportTicketBinding binding;
    SupportTicketActivity ac;
    Dialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_support_ticket);
        ac = SupportTicketActivity.this;
        dialog1 = M.showDialog(SupportTicketActivity.this, "", false, false);
        init();
    }


    private void init() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.support);


        getTickets();


    }

    private void getTickets() {
        dialog1.show();
        MainApplication.getApiService().getOpenCloseTicket(Prefes.getAccessToken(ac)).enqueue(new Callback<GetOpenCloseTicket>() {
            @Override
            public void onResponse(Call<GetOpenCloseTicket> call, Response<GetOpenCloseTicket> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus().equals("true")) {
                        dialog1.dismiss();
                        binding.closeText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Log.e("closeticket", "" + response.body().getData().getCloseTickets());
                                binding.closeLn.setVisibility(View.VISIBLE);
                                binding.openLn.setVisibility(View.GONE);
                                binding.openText.setText("OPEN");
                                binding.openText.setTextColor(Color.parseColor("#000000"));
                                binding.openText.setBackgroundColor(Color.parseColor("#FFFFFF"));


                                binding.closeText.setText("CLOSED");
                                binding.closeText.setTextColor(Color.parseColor("#FFFFFF"));
                                binding.closeText.setBackgroundColor(Color.parseColor("#000000"));


                                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                OpenTicketAdapter adapter = new OpenTicketAdapter(getApplicationContext(), response.body().getData().getCloseTickets(), "1");
                                binding.closeRecycler.setLayoutManager(manager);
                                binding.closeRecycler.setAdapter(adapter);


                            }
                        });
                        binding.openText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                if (response.body().getData().getActiveTickets() == 1) {

                                    binding.openLn.setVisibility(View.VISIBLE);
                                    binding.closeLn.setVisibility(View.GONE);

                                    binding.openText.setText("OPEN");
                                    binding.openText.setTextColor(Color.parseColor("#FFFFFF"));
                                    binding.openText.setBackgroundColor(Color.parseColor("#000000"));


                                    binding.closeText.setText("CLOSED");
                                    binding.closeText.setTextColor(Color.parseColor("#000000"));
                                    binding.closeText.setBackgroundColor(Color.parseColor("#FFFFFF"));


                                    GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                    CloseTicketAdapter adapter = new CloseTicketAdapter(getApplicationContext(), response.body().getData().getActvTickets(), "1");
                                    binding.openRecycler.setLayoutManager(manager);
                                    binding.openRecycler.setAdapter(adapter);

                                } else if (response.body().getData().getActiveTickets() == 0) {
                                    binding.ticketLn.setVisibility(View.VISIBLE);

                                    binding.requestTicket.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(SupportTicketActivity.this, GenerateTicketActivity.class));
                                        }
                                    });
                                }
                            }


                        });
                        if (response.body().getData().getActiveTickets() == 1) {

                            binding.openLn.setVisibility(View.VISIBLE);


                            GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                            CloseTicketAdapter adapter = new CloseTicketAdapter(getApplicationContext(), response.body().getData().getActvTickets(),"1");
                            binding.openRecycler.setLayoutManager(manager);
                            binding.openRecycler.setAdapter(adapter);


                            binding.openText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    if (response.body().getData().getActiveTickets() == 1) {

                                        binding.openLn.setVisibility(View.VISIBLE);
                                        binding.closeLn.setVisibility(View.GONE);

                                        binding.openText.setText("OPEN");
                                        binding.openText.setTextColor(Color.parseColor("#FFFFFF"));
                                        binding.openText.setBackgroundColor(Color.parseColor("#000000"));


                                        binding.closeText.setText("CLOSED");
                                        binding.closeText.setTextColor(Color.parseColor("#000000"));
                                        binding.closeText.setBackgroundColor(Color.parseColor("#FFFFFF"));


                                        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                        CloseTicketAdapter adapter = new CloseTicketAdapter(getApplicationContext(), response.body().getData().getActvTickets(), "1");
                                        binding.openRecycler.setLayoutManager(manager);
                                        binding.openRecycler.setAdapter(adapter);

                                    } else if (response.body().getData().getActiveTickets() == 0) {
                                        binding.ticketLn.setVisibility(View.VISIBLE);

                                        binding.requestTicket.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                startActivity(new Intent(SupportTicketActivity.this, GenerateTicketActivity.class));
                                            }
                                        });
                                    }
                                }


                            });


                        }
                        else if (response.body().getData().getActiveTickets() == 0) {

                            binding.ticketLn.setVisibility(View.VISIBLE);

                            binding.requestTicket.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(SupportTicketActivity.this, GenerateTicketActivity.class));
                                }
                            });


                            binding.openText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    binding.closeLn.setVisibility(View.GONE);

                                    binding.openText.setText("OPEN");
                                    binding.openText.setTextColor(Color.parseColor("#FFFFFF"));
                                    binding.openText.setBackgroundColor(Color.parseColor("#000000"));


                                    binding.closeText.setText("CLOSED");
                                    binding.closeText.setTextColor(Color.parseColor("#000000"));
                                    binding.closeText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                                    binding.ticketLn.setVisibility(View.VISIBLE);
                                    binding.requestTicket.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(SupportTicketActivity.this, GenerateTicketActivity.class));
                                        }
                                    });


                                }
                            });

                            binding.closeText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Log.e("closeticket", "" + response.body().getData().getCloseTickets());
                                    binding.closeLn.setVisibility(View.VISIBLE);
                                    binding.openLn.setVisibility(View.GONE);
                                    binding.openText.setText("OPEN");
                                    binding.openText.setTextColor(Color.parseColor("#000000"));
                                    binding.openText.setBackgroundColor(Color.parseColor("#FFFFFF"));


                                    binding.closeText.setText("CLOSED");
                                    binding.closeText.setTextColor(Color.parseColor("#FFFFFF"));
                                    binding.closeText.setBackgroundColor(Color.parseColor("#000000"));


                                    GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                    OpenTicketAdapter adapter = new OpenTicketAdapter(getApplicationContext(), response.body().getData().getCloseTickets(),"1");
                                    binding.closeRecycler.setLayoutManager(manager);
                                    binding.closeRecycler.setAdapter(adapter);


                                }
                            });

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetOpenCloseTicket> call, Throwable t) {
                dialog1.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(SupportTicketActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(SupportTicketActivity.this,SupportTicketActivity.this::finishAffinity);
        }
    }
}
