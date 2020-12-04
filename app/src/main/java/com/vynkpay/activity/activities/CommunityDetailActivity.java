package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.vynkpay.R;
import com.vynkpay.databinding.ActivityCommunityDetailBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.CommunityDetailResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityDetailActivity extends AppCompatActivity {
   ActivityCommunityDetailBinding binding;
    CommunityDetailActivity ac;
    Dialog serverDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_community_detail);
        ac = CommunityDetailActivity.this;
        serverDialog = M.showDialog(CommunityDetailActivity.this, "", false, false);
        clicks();
    }

    private void clicks() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.communitydetail);
        getCommunityDetail();
    }

    private void getCommunityDetail() {
        serverDialog.show();;
        MainApplication.getApiService().getCommunityDetail(Prefes.getAccessToken(ac)).enqueue(new Callback<CommunityDetailResponse>() {
            @Override
            public void onResponse(Call<CommunityDetailResponse> call, Response<CommunityDetailResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    serverDialog.dismiss();
                   if(response.body().getStatus().equals("true")){
                       binding.sizeText.setText(response.body().getData().getTeamSize());
                       binding.activeText.setText(response.body().getData().getTeamSizeActive());
                       binding.inactiveText.setText(response.body().getData().getTeamSizeInactive());
                       binding.incomeText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getTeamTotalIncome());
                       binding.earningText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getMyEarning());
                       binding.activeDirecttext.setText(response.body().getData().getDirectActive());
                       if(response.body().getData().getBonusAchivement().equals("")){
                            binding.bonusMainText.setVisibility(View.GONE);
                       }
                       else {
                           binding.bonusText.setText(response.body().getData().getBonusAchivement());
                       }
                       binding.clubText.setText(response.body().getData().getClubLevel());
                       binding.globalText.setText(response.body().getData().getGlobalPoolStatus());
                       binding.ambassadarText.setText(response.body().getData().getAmbassadorStatus());
                       binding.appraisalText.setText(response.body().getData().getAppraisalStatus());
                       binding.chairmanText.setText(response.body().getData().getChairmanStatus());
                       binding.leaderText.setText(response.body().getData().getLeadershipStatus());
                       if(response.body().getData().getClubLevel().equals("")){
                           binding.clubmaintext.setVisibility(View.GONE);
                       }
                       else {
                           binding.clubText.setText(response.body().getData().getClubLevel());
                       }
                   }
                }
            }

            @Override
            public void onFailure(Call<CommunityDetailResponse> call, Throwable t) {
                serverDialog.dismiss();
            }
        });
    }

}