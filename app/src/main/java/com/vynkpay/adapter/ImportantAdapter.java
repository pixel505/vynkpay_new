package com.vynkpay.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.activities.ChainTransactionActivity;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.StatiaticsResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.utils.Functions;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImportantAdapter extends RecyclerView.Adapter<ImportantAdapter.Holder>{

    Context context;
    StatiaticsResponse statiaticsResponse;

    public ImportantAdapter(Context context,StatiaticsResponse statiaticsResponse) {
        this.context = context;
        this.statiaticsResponse = statiaticsResponse;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_importantlinks,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        try {

            MainApplication.getApiService().getTransferSettings(Prefes.getAccessToken(context)).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d("settingsresponse",response.body());
                    //{"status":true,"data":{"m_wallet_transfer_enable":true,"v_wallet_transfer_enable":true,"earning_wallet_transfer_enable":true},"message":"success"}
                    try {
                        JSONObject respData = new JSONObject(response.body());
                        if (respData.getString("status").equalsIgnoreCase("true")){
                            JSONObject data = respData.getJSONObject("data");
                            String m_wallet_transfer_enable = data.getString("m_wallet_transfer_enable");
                            String v_wallet_transfer_enable = data.getString("v_wallet_transfer_enable");
                            String earning_wallet_transfer_enable = data.getString("earning_wallet_transfer_enable");
                            String opt_vcash_enable = data.getString("opt_vcash_enable");

                            if (opt_vcash_enable.equalsIgnoreCase("true")){
                                holder.linVcash.setVisibility(View.VISIBLE);
                                holder.vyncTXN2.setVisibility(View.GONE);
                                holder.vyncTXN.setVisibility(View.VISIBLE);
                            } else {
                                holder.linVcash.setVisibility(View.GONE);
                                holder.vyncTXN2.setVisibility(View.VISIBLE);
                                holder.vyncTXN.setVisibility(View.GONE);
                            }
                            if (respData.has("message")){
                                Log.d("settingsresponse",respData.getString("message"));
                            }
                        }else {
                            if (respData.has("message")){
                                Log.d("settingsresponse",respData.getString("message"));
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("settingsresponse",t.getMessage()!=null?t.getMessage():"Error");
                }
            });

            holder.tvOptVcash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context,R.style.myDialog);
                    dialog.setContentView(R.layout.custom_vcashopt);
                    LinearLayout btnNo = dialog.findViewById(R.id.btnNo);
                    LinearLayout btnYes = dialog.findViewById(R.id.btnYes);

                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            callVcashOptOut(holder.linVcash, holder.vyncTXN2, holder.vyncTXN);
                        }
                    });
                    dialog.show();
                }
            });

            Log.d("dfdfdf",Prefes.getisIndian(context)+"//");
            if (Prefes.getisIndian(context).equalsIgnoreCase("YES")) {
                holder.linNext.setVisibility(View.GONE);
                holder.linPresent.setVisibility(View.GONE);
            } else {
                holder.linNext.setVisibility(View.GONE);
                holder.linPresent.setVisibility(View.GONE);
            }


            Functions.loadImageCall(context, statiaticsResponse.getDesImg(), holder.ivPresentD);
            Functions.loadImageCall(context, statiaticsResponse.getNextDesImg(), holder.ivPresentN);

            Functions.loadImageCall(context, BuildConfig.BASE_URL +"account/"+ statiaticsResponse.getDesImg(), holder.ivVynccPresentD);
            Functions.loadImageCall(context, BuildConfig.BASE_URL +"account/"+ statiaticsResponse.getNextDesImg(), holder.ivVynccPresentN);
            holder.ivTotalEarning.setImageResource(R.drawable.totalearning3x);
            holder.ivPurchaseA.setImageResource(R.drawable.purchaseamount3x);
            holder.ivPurchaseD.setImageResource(R.drawable.purchase3x);

            holder.tvPresentD.setText(statiaticsResponse.getDesTitle());
            holder.tvNextD.setText(statiaticsResponse.getNextDesTitle());

            holder.tvVynccPresentD.setText(statiaticsResponse.getDesTitle());
            holder.tvVynccNextD.setText(statiaticsResponse.getNextDesTitle());

            holder.tvPurchaseAmount.setText(Functions.CURRENCY_SYMBOL+""+statiaticsResponse.getPurchaseAmount());
            holder.tvPurchaseDate.setText(statiaticsResponse.getPurchaseDate());
            holder.tvTotalEarning.setText(Functions.CURRENCY_SYMBOL+""+statiaticsResponse.getTotalEarning());

            holder.vyncTokenBlnc.setText(statiaticsResponse.getTokenBalance());
            holder.vyncTokenName.setText(statiaticsResponse.getTokenName());
            Functions.loadImageCall(context, statiaticsResponse.getTokenIcon(), holder.vyncTokenIcon);


            holder.vyncTokenBlnc2.setText(statiaticsResponse.getTokenBalance());
            holder.vyncTokenName2.setText(statiaticsResponse.getTokenName());
            Functions.loadImageCall(context, statiaticsResponse.getTokenIcon(), holder.vyncTokenIcon2);


            holder.vyncTXN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ChainTransactionActivity.class));
                }
            });

            holder.vyncTXN2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ChainTransactionActivity.class));
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }

    }


    public void callVcashOptOut(LinearLayout linVcash, LinearLayout vyncTXN2, CardView vyncTXN){
        MainApplication.getApiService().optVCashAdd(Prefes.getAccessToken(context)).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.isSuccessful() && response.body()!=null){
                        Log.d("vcashresponse",response.body());
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getString("status").equalsIgnoreCase("true")){
                            String message = jsonObject.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            MainApplication.getApiService().getTransferSettings(Prefes.getAccessToken(context)).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Log.d("settingsresponse",response.body());
                                    //{"status":true,"data":{"m_wallet_transfer_enable":true,"v_wallet_transfer_enable":true,"earning_wallet_transfer_enable":true},"message":"success"}
                                    try {
                                        JSONObject respData = new JSONObject(response.body());
                                        if (respData.getString("status").equalsIgnoreCase("true")){
                                            JSONObject data = respData.getJSONObject("data");
                                            String m_wallet_transfer_enable = data.getString("m_wallet_transfer_enable");
                                            String v_wallet_transfer_enable = data.getString("v_wallet_transfer_enable");
                                            String earning_wallet_transfer_enable = data.getString("earning_wallet_transfer_enable");
                                            String opt_vcash_enable = data.getString("opt_vcash_enable");
                                            if (opt_vcash_enable.equalsIgnoreCase("true")){
                                                linVcash.setVisibility(View.VISIBLE);
                                                vyncTXN2.setVisibility(View.GONE);
                                                vyncTXN.setVisibility(View.VISIBLE);
                                            } else {
                                                linVcash.setVisibility(View.GONE);
                                                vyncTXN2.setVisibility(View.VISIBLE);
                                                vyncTXN.setVisibility(View.GONE);
                                            }
                                            if (respData.has("message")){
                                                Log.d("settingsresponse",respData.getString("message"));
                                            }
                                        }else {
                                            if (respData.has("message")){
                                                Log.d("settingsresponse",respData.getString("message"));
                                            }
                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("settingsresponse",t.getMessage()!=null?t.getMessage():"Error");
                                }
                            });
                        }else {
                            String message = jsonObject.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("vcashresponse",t.getMessage() != null ? t.getMessage() : "Error");
            }

        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class Holder extends RecyclerView.ViewHolder{

        NormalTextView tvOptVcash;
        NormalTextView tvPresentD;
        NormalTextView tvNextD;
        NormalTextView tvPurchaseDate;
        NormalTextView tvPurchaseAmount;
        NormalTextView tvTotalEarning;

        NormalTextView tvVynccPresentD;
        NormalTextView tvVynccNextD;
        NormalTextView vyncTokenName;
        NormalTextView vyncTokenName2;
        NormalTextView vyncTokenBlnc;
        NormalTextView vyncTokenBlnc2;

        ImageView ivPresentD;
        ImageView ivPresentN;
        ImageView ivPurchaseD;
        ImageView ivPurchaseA;
        ImageView ivTotalEarning;

        ImageView ivVynccPresentD;
        ImageView ivVynccPresentN;
        ImageView vyncTokenIcon;
        ImageView vyncTokenIcon2;

        LinearLayout linPresent;
        LinearLayout linNext;
        LinearLayout linVcash;

        LinearLayout vyncTXN2;
        CardView vyncTXN;



        public Holder(@NonNull View itemView) {
            super(itemView);
            vyncTXN = itemView.findViewById(R.id.vyncTXN);
            tvOptVcash = itemView.findViewById(R.id.tvOptVcash);
            tvPresentD = itemView.findViewById(R.id.tvPresentD);
            tvNextD = itemView.findViewById(R.id.tvNextD);
            tvPurchaseDate = itemView.findViewById(R.id.tvPurchaseDate);
            tvPurchaseAmount = itemView.findViewById(R.id.tvPurchaseAmount);
            tvTotalEarning = itemView.findViewById(R.id.tvTotalEarning);

            tvVynccPresentD = itemView.findViewById(R.id.tvVynccPresentD);
            tvVynccNextD = itemView.findViewById(R.id.tvVynccNextD);

            ivPresentD = itemView.findViewById(R.id.ivPresentD);
            ivPresentN = itemView.findViewById(R.id.ivPresentN);
            ivPurchaseD = itemView.findViewById(R.id.ivPurchaseD);
            ivPurchaseA = itemView.findViewById(R.id.ivPurchaseA);
            ivTotalEarning = itemView.findViewById(R.id.ivTotalEarning);

            ivVynccPresentD = itemView.findViewById(R.id.ivVynccPresentD);
            ivVynccPresentN = itemView.findViewById(R.id.ivVynccPresentN);

            linPresent = itemView.findViewById(R.id.linPresent);
            linNext = itemView.findViewById(R.id.linNext);
            linVcash = itemView.findViewById(R.id.linVcash);

            vyncTokenBlnc = itemView.findViewById(R.id.vyncTokenBlnc);
            vyncTokenBlnc2 = itemView.findViewById(R.id.vyncTokenBlnc2);
            vyncTokenIcon = itemView.findViewById(R.id.vyncTokenIcon);
            vyncTokenIcon2 = itemView.findViewById(R.id.vyncTokenIcon2);
            vyncTokenName = itemView.findViewById(R.id.vyncTokenName);

            vyncTokenName2 = itemView.findViewById(R.id.vyncTokenName2);
            vyncTXN2 = itemView.findViewById(R.id.vyncTXN2);

        }
    }

}
