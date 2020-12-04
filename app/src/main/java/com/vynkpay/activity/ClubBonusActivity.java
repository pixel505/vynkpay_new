package com.vynkpay.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.gson.Gson;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityClubBonusBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.Club1Response;
import com.vynkpay.utils.Functions;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubBonusActivity extends AppCompatActivity {

    ActivityClubBonusBinding binding;
    ClubBonusActivity ac;
    String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_club_bonus);
        ac = ClubBonusActivity.this;
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
        binding.toolbarLayout.toolbarTitlenew.setText(getString(R.string.clubbonus));
        binding.referalbonusRecycler.setLayoutManager(new LinearLayoutManager(ClubBonusActivity.this,LinearLayoutManager.VERTICAL,false));
        getClub();
        //getShops();
    }

    void getClub(){
        if (type.equalsIgnoreCase("c1")){
            binding.progressFrame.setVisibility(View.VISIBLE);
            MainApplication.getApiService().getClub1(Prefes.getAccessToken(ClubBonusActivity.this)).enqueue(new Callback<Club1Response>() {
                @Override
                public void onResponse(Call<Club1Response> call, Response<Club1Response> response) {
                    binding.progressFrame.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        if (response.body()!=null) {
                            Log.d("club1response",new Gson().toJson(response.body().getData().getListing()));
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                if ((response.body().getData().getListing()!=null?response.body().getData().getListing().size():0)>0) {
                                    binding.referalbonusRecycler.setAdapter(new ClubBonusAdapter(response.body().getData().getListing()));
                                } else {
                                    binding.noLayout.setVisibility(View.VISIBLE);
                                    binding.noMessageTV.setText(response.body().getMessage()+"");
                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                                Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Club1Response> call, Throwable t) {
                    binding.progressFrame.setVisibility(View.GONE);
                    Log.d("club1response",t.getMessage()!=null?t.getMessage():"Error");
                }
            });
        }else if (type.equalsIgnoreCase("c2")){

            binding.progressFrame.setVisibility(View.VISIBLE);
            MainApplication.getApiService().getClub2(Prefes.getAccessToken(ClubBonusActivity.this)).enqueue(new Callback<Club1Response>() {
                @Override
                public void onResponse(Call<Club1Response> call, Response<Club1Response> response) {
                    binding.progressFrame.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        if (response.body()!=null) {
                            Log.d("club2response",new Gson().toJson(response.body().getData().getListing()));
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                if ((response.body().getData().getListing()!=null?response.body().getData().getListing().size():0)>0) {
                                    binding.referalbonusRecycler.setAdapter(new ClubBonusAdapter(response.body().getData().getListing()));
                                }else {
                                    binding.noLayout.setVisibility(View.VISIBLE);
                                    binding.noMessageTV.setText(response.body().getMessage()+"");
                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }else {
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                                Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Club1Response> call, Throwable t) {
                    binding.progressFrame.setVisibility(View.GONE);
                    Log.d("club2response",t.getMessage()!=null?t.getMessage():"Error");
                }
            });

        }else if (type.equalsIgnoreCase("c3")){

            binding.progressFrame.setVisibility(View.VISIBLE);

            MainApplication.getApiService().getClub3(Prefes.getAccessToken(ClubBonusActivity.this)).enqueue(new Callback<Club1Response>() {
                @Override
                public void onResponse(Call<Club1Response> call, Response<Club1Response> response) {
                    binding.progressFrame.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        if (response.body()!=null) {
                            Log.d("club3response",new Gson().toJson(response.body().getData().getListing()));
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                if ((response.body().getData().getListing()!=null?response.body().getData().getListing().size():0)>0) {
                                    binding.referalbonusRecycler.setAdapter(new ClubBonusAdapter(response.body().getData().getListing()));
                                }else {
                                    binding.noLayout.setVisibility(View.VISIBLE);
                                    binding.noMessageTV.setText(response.body().getMessage()+"");
                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }else {
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(response.body().getMessage()+"");
                                Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Club1Response> call, Throwable t) {
                    binding.progressFrame.setVisibility(View.GONE);
                    Log.d("club3response",t.getMessage()!=null?t.getMessage():"Error");
                }
            });

        }

    }

    class ClubBonusAdapter extends RecyclerView.Adapter<ClubBonusAdapter.Holder>{

        List<Club1Response.Listing> listingList;

        public ClubBonusAdapter(List<Club1Response.Listing> listingList) {
            this.listingList = listingList;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ClubBonusActivity.this).inflate(R.layout.custom_clubbonus,parent,false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            try {
                holder.nameText.setText(listingList.get(position).getFullName());
                holder.nameryttext.setText("("+listingList.get(position).getUsername()+")");
                holder.levelText.setText("Slab "+listingList.get(position).getId());
                holder.statusText.setText("Confirmed");
                holder.paidDateText.setText(Functions.CURRENCY_SYMBOL+" "+listingList.get(position).getFixAmount());

                try {
                    DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = f.parse(listingList.get(position).getCreatedDate());
                    DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
                    holder.datetext.setText(date.format(d));
                    System.out.println("Date: " + date.format(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return listingList != null ? listingList.size() : 0;
        }

        class Holder extends RecyclerView.ViewHolder{
            NormalTextView nameText;
            NormalTextView nameryttext;
            NormalTextView codeText;
            NormalTextView datetext;
            NormalTextView levelText;
            NormalTextView paidDateText;
            NormalTextView statusText;

            public Holder(@NonNull View itemView) {
                super(itemView);
                nameText = itemView.findViewById(R.id.nameText);
                nameryttext = itemView.findViewById(R.id.nameryttext);
                codeText = itemView.findViewById(R.id.codeText);
                datetext = itemView.findViewById(R.id.datetext);
                levelText = itemView.findViewById(R.id.levelText);
                paidDateText = itemView.findViewById(R.id.paidDateText);
                statusText = itemView.findViewById(R.id.statusText);
            }
        }

    }


}