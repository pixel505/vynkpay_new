package com.vynkpay.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.vynkpay.retrofit.model.Club1Response;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.adapter.GenerationBonusAdapter;
import com.vynkpay.adapter.ReferalBonusAdapter;
import com.vynkpay.databinding.ActivityReferalBonusBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GenerationBonusResponse;
import com.vynkpay.retrofit.model.ReferalBonusResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferalBonusActivity extends AppCompatActivity {
    ActivityReferalBonusBinding binding;
    ReferalBonusActivity ac;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_referal_bonus);
        ac = ReferalBonusActivity.this;
        clicks();
    }

    private void clicks() {
        if(getIntent()!=null){
            type=getIntent().getStringExtra("type");
        }
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);

        getRefBonus();

    }

    private void getRefBonus() {
        if(type.equals("1")) {
            binding.toolbarLayout.toolbarTitlenew.setText(R.string.referalbonus);
            binding.progressFrame.setVisibility(View.VISIBLE);
            MainApplication.getApiService().getreferalBonus(Prefes.getAccessToken(ac)).enqueue(new Callback<ReferalBonusResponse>() {
                @Override
                public void onResponse(Call<ReferalBonusResponse> call, Response<ReferalBonusResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        binding.progressFrame.setVisibility(View.GONE);
                        if (response.body().getStatus().equals("true")) {
                            if (response.body().getData().size()>0){
                                binding.noLayout.setVisibility(View.GONE);
                                Log.d("bonusLOGG", new Gson().toJson(response.body().getData()));

                                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                ReferalBonusAdapter adapter = new ReferalBonusAdapter(getApplicationContext(), response.body().getData());
                                binding.referalbonusRecycler.setLayoutManager(manager);
                                binding.referalbonusRecycler.setAdapter(adapter);
                            }else {
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                                Toast.makeText(ac, "No Data Found", Toast.LENGTH_LONG).show();
                        }

                        } else if (response.body().getMessage().equals("false")) {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.noMessageTV.setText(response.body().getMessage()+"");
                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        binding.progressFrame.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ReferalBonusResponse> call, Throwable t) {
                    Toast.makeText(ac, t.getMessage(), Toast.LENGTH_LONG).show();
                    binding.progressFrame.setVisibility(View.GONE);
                }
            });
        }

        else if(type.equals("2")){
            binding.toolbarLayout.toolbarTitlenew.setText(R.string.generationBonus);
            binding.progressFrame.setVisibility(View.VISIBLE);
            MainApplication.getApiService().getGenerationBonus(Prefes.getAccessToken(ac)).enqueue(new Callback<GenerationBonusResponse>() {
                @Override
                public void onResponse(Call<GenerationBonusResponse> call, Response<GenerationBonusResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        Log.d("detailsDataLOG", new Gson().toJson(response.body()));

                        binding.progressFrame.setVisibility(View.GONE);
                        if (response.body().getStatus().equals("true")) {
                            if(!response.body().getData().getListing().isEmpty()){
                                binding.noLayout.setVisibility(View.GONE);

                                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                GenerationBonusAdapter adapter = new GenerationBonusAdapter(getApplicationContext(), response.body().getData().getListing(), "GB");
                                binding.referalbonusRecycler.setLayoutManager(manager);
                                binding.referalbonusRecycler.setAdapter(adapter);
                            }else {
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                                Toast.makeText(ac, "No Data Found", Toast.LENGTH_LONG).show();
                            }

                        } else if (response.body().getMessage().equals("false")) {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.noMessageTV.setText(response.body().getMessage()+"");
                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        binding.progressFrame.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GenerationBonusResponse> call, Throwable t) {
                    Toast.makeText(ac, t.getMessage(), Toast.LENGTH_LONG).show();
                    binding.progressFrame.setVisibility(View.GONE);
                }
            });
        }


        else if(type.equals("3")){
            binding.toolbarLayout.toolbarTitlenew.setText(R.string.globalpool);
            binding.progressFrame.setVisibility(View.VISIBLE);
            MainApplication.getApiService().getPoolBonus(Prefes.getAccessToken(ac)).enqueue(new Callback<GenerationBonusResponse>() {
                @Override
                public void onResponse(Call<GenerationBonusResponse> call, Response<GenerationBonusResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        binding.progressFrame.setVisibility(View.GONE);
                        if (response.body().getStatus().equals("true")) {
                            if(!response.body().getData().getListing().isEmpty()){
                                binding.noLayout.setVisibility(View.GONE);
                                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                GenerationBonusAdapter adapter = new GenerationBonusAdapter(getApplicationContext(), response.body().getData().getListing(), "GPB");
                                binding.referalbonusRecycler.setLayoutManager(manager);
                                binding.referalbonusRecycler.setAdapter(adapter);
                            } else {
                                Toast.makeText(ac, "No Data Found", Toast.LENGTH_LONG).show();
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                            }

                        } else if (response.body().getMessage().equals("false")) {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.noMessageTV.setText(response.body().getMessage()+"");
                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        binding.progressFrame.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GenerationBonusResponse> call, Throwable t) {
                    Toast.makeText(ac, t.getMessage(), Toast.LENGTH_LONG).show();
                    binding.progressFrame.setVisibility(View.GONE);
                }
            });
        }

        else if(type.equals("4")){
            binding.toolbarLayout.toolbarTitlenew.setText(R.string.leaderBonus);
            binding.progressFrame.setVisibility(View.VISIBLE);
            MainApplication.getApiService().getleadBonus(Prefes.getAccessToken(ac)).enqueue(new Callback<GenerationBonusResponse>() {
                @Override
                public void onResponse(Call<GenerationBonusResponse> call, Response<GenerationBonusResponse> response) {
                    if (response.isSuccessful() && response.body()!= null) {
                        binding.progressFrame.setVisibility(View.GONE);
                        if (response.body().getStatus().equals("true")) {
                            if(!response.body().getData().getListing().isEmpty()){
                                binding.noLayout.setVisibility(View.GONE);
                                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                GenerationBonusAdapter adapter = new GenerationBonusAdapter(getApplicationContext(), response.body().getData().getListing(), "LB");
                                binding.referalbonusRecycler.setLayoutManager(manager);
                                binding.referalbonusRecycler.setAdapter(adapter);
                            } else {
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                                Toast.makeText(ac, "No Data Found", Toast.LENGTH_LONG).show();
                            }

                        } else if (response.body().getMessage().equals("false")) {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.noMessageTV.setText(response.body().getMessage()+"");
                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        binding.progressFrame.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GenerationBonusResponse> call, Throwable t) {
                    Toast.makeText(ac, t.getMessage()!=null ? t.getMessage() : "Error", Toast.LENGTH_LONG).show();
                    binding.progressFrame.setVisibility(View.GONE);
                }
            });
        }

        else if(type.equals("5")){
            binding.toolbarLayout.toolbarTitlenew.setText(R.string.ambasBonus);
            binding.progressFrame.setVisibility(View.VISIBLE);
            MainApplication.getApiService().getAmbasadarBonus(Prefes.getAccessToken(ac)).enqueue(new Callback<GenerationBonusResponse>() {
                @Override
                public void onResponse(Call<GenerationBonusResponse> call, Response<GenerationBonusResponse> response) {
                    if (response.isSuccessful() && response.body()!= null) {
                        binding.progressFrame.setVisibility(View.GONE);
                        if (response.body().getStatus().equals("true")) {
                            if(!response.body().getData().getListing().isEmpty()){
                                binding.noLayout.setVisibility(View.GONE);
                                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                GenerationBonusAdapter adapter = new GenerationBonusAdapter(getApplicationContext(), response.body().getData().getListing(), "AmB");
                                binding.referalbonusRecycler.setLayoutManager(manager);
                                binding.referalbonusRecycler.setAdapter(adapter);
                            } else {
                                Toast.makeText(ac, "No Data Found", Toast.LENGTH_LONG).show();
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                            }

                        } else if (response.body().getMessage().equals("false")) {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.noMessageTV.setText(response.body().getMessage()+"");
                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        binding.progressFrame.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GenerationBonusResponse> call, Throwable t) {
                    Toast.makeText(ac, t.getMessage(), Toast.LENGTH_LONG).show();
                    binding.progressFrame.setVisibility(View.GONE);
                }

            });
        }

        else if(type.equals("6")){
            binding.toolbarLayout.toolbarTitlenew.setText(R.string.chairBonus);
            binding.progressFrame.setVisibility(View.VISIBLE);
            MainApplication.getApiService().getChairBonus(Prefes.getAccessToken(ac)).enqueue(new Callback<GenerationBonusResponse>() {
                @Override
                public void onResponse(Call<GenerationBonusResponse> call, Response<GenerationBonusResponse> response) {
                    if (response.isSuccessful() && response.body()!= null) {
                        binding.progressFrame.setVisibility(View.GONE);
                        if (response.body().getStatus().equals("true")) {
                            if(!response.body().getData().getListing().isEmpty()){
                                binding.noLayout.setVisibility(View.GONE);
                                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                GenerationBonusAdapter adapter = new GenerationBonusAdapter(getApplicationContext(), response.body().getData().getListing(), "CB");
                                binding.referalbonusRecycler.setLayoutManager(manager);
                                binding.referalbonusRecycler.setAdapter(adapter);
                            } else {
                                Toast.makeText(ac, "No Data Found", Toast.LENGTH_LONG).show();
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                            }

                        } else if (response.body().getMessage().equals("false")) {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.noMessageTV.setText(response.body().getMessage()+"");
                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        binding.progressFrame.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GenerationBonusResponse> call, Throwable t) {
                    Toast.makeText(ac, t.getMessage(), Toast.LENGTH_LONG).show();
                    binding.progressFrame.setVisibility(View.GONE);
                }
            });
        }


        else if(type.equals("7")){
            binding.toolbarLayout.toolbarTitlenew.setText(R.string.appraisalbonus);
            binding.progressFrame.setVisibility(View.VISIBLE);
            MainApplication.getApiService().getApraisalBonus(Prefes.getAccessToken(ac)).enqueue(new Callback<GenerationBonusResponse>() {
                @Override
                public void onResponse(Call<GenerationBonusResponse> call, Response<GenerationBonusResponse> response) {
                    if (response.isSuccessful() && response.body()!= null) {
                        binding.progressFrame.setVisibility(View.GONE);

                        Log.d("appraisalBonusLOG", new Gson().toJson(response.body().getData()));

                        if (response.body().getStatus().equals("true")) {
                            if(!response.body().getData().getListing().isEmpty()){
                                binding.noLayout.setVisibility(View.GONE);
                                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                GenerationBonusAdapter adapter = new GenerationBonusAdapter(getApplicationContext(), response.body().getData().getListing(), "AB");
                                binding.referalbonusRecycler.setLayoutManager(manager);
                                binding.referalbonusRecycler.setAdapter(adapter);
                            } else {
                                Toast.makeText(ac, "No Data Found", Toast.LENGTH_LONG).show();
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                            }

                        } else if (response.body().getMessage().equals("false")) {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.noMessageTV.setText(response.body().getMessage()+"");
                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        binding.progressFrame.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GenerationBonusResponse> call, Throwable t) {
                    Toast.makeText(ac, t.getMessage()!=null?t.getMessage():"Error", Toast.LENGTH_LONG).show();
                    binding.progressFrame.setVisibility(View.GONE);
                }
            });
        }
        else if (type.equals("8")){
            Log.d("printaccess",Prefes.getAccessToken(ReferalBonusActivity.this));
            //binding.toolbarLayout.toolbarTitlenew.setText(getString(R.string.clubbonus));
            //binding.noLayout.setVisibility(View.VISIBLE);
            //binding.noMessageTV.setText("No Club Data found");
        }
        else if (type.equals("9")){
            binding.toolbarLayout.toolbarTitlenew.setText(getString(R.string.volumebonus));
            binding.progressFrame.setVisibility(View.VISIBLE);
            MainApplication.getApiService().getVolumeBonus(Prefes.getAccessToken(ReferalBonusActivity.this)).enqueue(new Callback<GenerationBonusResponse>() {

                @Override
                public void onResponse(Call<GenerationBonusResponse> call, Response<GenerationBonusResponse> response) {
                    if (response.isSuccessful() && response.body()!= null) {
                        binding.progressFrame.setVisibility(View.GONE);

                        Log.d("volumeBonusLOG", new Gson().toJson(response.body().getData()));

                        if (response.body().getStatus().equals("true")) {
                            if(!response.body().getData().getListing().isEmpty()){
                                binding.noLayout.setVisibility(View.GONE);
                                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                GenerationBonusAdapter adapter = new GenerationBonusAdapter(getApplicationContext(), response.body().getData().getListing(), "AB");
                                binding.referalbonusRecycler.setLayoutManager(manager);
                                binding.referalbonusRecycler.setAdapter(adapter);
                            } else {
                                Toast.makeText(ac, "No Data Found", Toast.LENGTH_LONG).show();
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                            }

                        } else if (response.body().getMessage().equals("false")) {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.noMessageTV.setText(response.body().getMessage()+"");
                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        binding.progressFrame.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GenerationBonusResponse> call, Throwable t) {
                    Log.d("vBonusresponse",t.getMessage()!=null?t.getMessage():"Error");
                    binding.progressFrame.setVisibility(View.GONE);
                }
            });
        }
        else if (type.equals("10")){
            binding.toolbarLayout.toolbarTitlenew.setText(getString(R.string.performancebonus));
            binding.progressFrame.setVisibility(View.VISIBLE);

            MainApplication.getApiService().getPerformanceBonus(Prefes.getAccessToken(ReferalBonusActivity.this)).enqueue(new Callback<GenerationBonusResponse>() {

                @Override
                public void onResponse(Call<GenerationBonusResponse> call, Response<GenerationBonusResponse> response) {
                    if (response.isSuccessful() && response.body()!= null) {
                        binding.progressFrame.setVisibility(View.GONE);

                        Log.d("perfomBonusLOG", new Gson().toJson(response.body().getData()));

                        if (response.body().getStatus().equals("true")) {
                            if(!response.body().getData().getListing().isEmpty()){
                                binding.noLayout.setVisibility(View.GONE);
                                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                GenerationBonusAdapter adapter = new GenerationBonusAdapter(getApplicationContext(), response.body().getData().getListing(), "AB");
                                binding.referalbonusRecycler.setLayoutManager(manager);
                                binding.referalbonusRecycler.setAdapter(adapter);
                            } else {
                                Toast.makeText(ac, "No Data Found", Toast.LENGTH_LONG).show();
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                            }

                        } else if (response.body().getMessage().equals("false")) {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.noMessageTV.setText(response.body().getMessage()+"");
                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        binding.progressFrame.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GenerationBonusResponse> call, Throwable t) {
                    Log.d("perfomBonusLOG",t.getMessage()!=null?t.getMessage():"Error");
                    binding.progressFrame.setVisibility(View.GONE);
                }
            });

        }
        else if (type.equals("11")){
            binding.toolbarLayout.toolbarTitlenew.setText(getString(R.string.shoppingbonus));
            binding.progressFrame.setVisibility(View.VISIBLE);

            MainApplication.getApiService().getShoppingBonus(Prefes.getAccessToken(ReferalBonusActivity.this)).enqueue(new Callback<GenerationBonusResponse>() {

                @Override
                public void onResponse(Call<GenerationBonusResponse> call, Response<GenerationBonusResponse> response) {
                    if (response.isSuccessful() && response.body()!= null) {
                        binding.progressFrame.setVisibility(View.GONE);

                        Log.d("shopingBonusLOG", new Gson().toJson(response.body().getData()));

                        if (response.body().getStatus().equals("true")) {
                            if(!response.body().getData().getListing().isEmpty()){
                                binding.noLayout.setVisibility(View.GONE);
                                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                GenerationBonusAdapter adapter = new GenerationBonusAdapter(getApplicationContext(), response.body().getData().getListing(), "AB");
                                binding.referalbonusRecycler.setLayoutManager(manager);
                                binding.referalbonusRecycler.setAdapter(adapter);
                            } else {
                                Toast.makeText(ac, "No Data Found", Toast.LENGTH_LONG).show();
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                            }

                        } else if (response.body().getMessage().equals("false")) {
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.noMessageTV.setText(response.body().getMessage()+"");
                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }else {
                        binding.progressFrame.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<GenerationBonusResponse> call, Throwable t) {
                    Log.d("shopingBonusLOG",t.getMessage()!=null?t.getMessage():"Error");
                    binding.progressFrame.setVisibility(View.GONE);
                }
            });

        }
    }
}
