package com.vynkpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.adapter.GenerationBonusDetailAdapter;
import com.vynkpay.databinding.ActivityGenerationBonusDetailBinding;
import com.vynkpay.models.GenerationBonusDetailModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GenerationBonusDetailActivity extends AppCompatActivity {

    ActivityGenerationBonusDetailBinding binding;
    String dateForView, title, frontUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_generation_bonus_detail);
        dev();
    }

    private void dev() {
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbar.notifyIcon.setVisibility(View.INVISIBLE);
        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.recyclerView.setLayoutManager(Functions.layoutManager(GenerationBonusDetailActivity.this, Functions.VERTICAL, 0));
        Intent intent=getIntent();
        if (intent!=null){
            if (intent.hasExtra("dateForView"));{
                dateForView = intent.getStringExtra("dateForView");
            }

            if (intent.hasExtra("title")){
                title = intent.getStringExtra("title");
            }

            if (intent.hasExtra("frontUserID")){
                frontUserID = intent.getStringExtra("frontUserID");
            }

            if (title.equals("Generation Bonus Detail")){
                getBonusDetails();
            }else {
                getTeamProfit();
            }

            binding.toolbar.toolbarTitle.setText(title);
        }

    }

    ArrayList<GenerationBonusDetailModel> generationBonusDetailModelArrayList = new ArrayList<>();
    public void getBonusDetails(){
        generationBonusDetailModelArrayList.clear();
        binding.progress.setVisibility(View.VISIBLE);
        ApiCalls.getBonusDetail(GenerationBonusDetailActivity.this, dateForView,
                Prefes.getAccessToken(GenerationBonusDetailActivity.this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("sdasdsdsfsdLOGGG", result);
                binding.progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONArray listing = dataObject.getJSONArray("listing");
                    for (int i=0; i<listing.length(); i++){
                        JSONObject object = listing.getJSONObject(i);
                        String id = object.getString("id");
                        String user_id = object.getString("user_id");
                        String profit_type = object.getString("profit_type");
                        String name = object.getString("name");
                        String username = object.getString("username");
                        String p_amount = object.getString("p_amount");
                        String statusString = object.getString("status");
                        String created_date = object.getString("created_date");
                        String ref_name = object.getString("ref_name");
                        String ref_username = object.getString("ref_username");
                        String ref_email = object.getString("ref_email");
                        String ref_phone = object.getString("ref_phone");
                        String from_level = object.getString("from_level");

                        generationBonusDetailModelArrayList.add(new GenerationBonusDetailModel( id,  user_id,
                                 profit_type,  name,  username,  p_amount,  statusString, created_date,  ref_name,
                                ref_username,  ref_email,  ref_phone, from_level));
                    }

                    binding.recyclerView.setAdapter(new GenerationBonusDetailAdapter(GenerationBonusDetailActivity.this, generationBonusDetailModelArrayList));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                binding.progress.setVisibility(View.GONE);
            }
        });
    }

    public void getTeamProfit(){
        generationBonusDetailModelArrayList.clear();
        binding.progress.setVisibility(View.VISIBLE);
        ApiCalls.getTeamProfitDetail(GenerationBonusDetailActivity.this, frontUserID, Prefes.getAccessToken(GenerationBonusDetailActivity.this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("teamProfitDetail", result);
                binding.progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    JSONArray listing = dataObject.getJSONArray("listing");
                    for (int i=0; i<listing.length(); i++){
                        JSONObject object = listing.getJSONObject(i);
                        String id = object.getString("id");
                        String user_id = object.getString("user_id");
                        String profit_type = object.getString("profit_type");
                        String name = object.getString("name");
                        String username = object.getString("username");
                        String p_amount = object.getString("p_amount");
                        String statusString = object.getString("status");
                        String created_date = object.getString("created_date");
                        String ref_name = object.getString("ref_name");
                        String ref_username = object.getString("ref_username");
                        String ref_email = object.getString("ref_email");
                        String ref_level = object.getString("ref_level");


                        generationBonusDetailModelArrayList.add(new GenerationBonusDetailModel( id,  user_id,
                                profit_type,  name,  username,  p_amount,  statusString, created_date,  ref_name,
                                ref_username,  ref_email,  "", ref_level));
                    }

                    binding.recyclerView.setAdapter(new GenerationBonusDetailAdapter(GenerationBonusDetailActivity.this, generationBonusDetailModelArrayList));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                binding.progress.setVisibility(View.GONE);
            }
        });
    }
}
