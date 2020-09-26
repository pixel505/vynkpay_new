package com.vynkpay.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.DmtBeneficiaryDeleteOtpActivity;
import com.vynkpay.activity.activities.VerifyAccountActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.events.ListingBeneficiaryEvent;
import com.vynkpay.models.BankModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeeAllBeneficiaryAdapter extends RecyclerView.Adapter<SeeAllBeneficiaryAdapter.MyViewHolder> {
    ArrayList<?> historyList;
    Context context;
    Dialog dialog;
    String accessToken;

    public SeeAllBeneficiaryAdapter(Context context, ArrayList<?> historyList, Dialog dialog, String accessToken) {
        this.context = context;
        this.historyList = historyList;
        this.dialog = dialog;
        this.accessToken = accessToken;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_beneficiary_rcg, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (historyList.get(position) instanceof BankModel) {
            holder.benAccountNumber.setText(((BankModel) historyList.get(position)).getAccountNumber());
            if (((BankModel) historyList.get(position)).getBankName().equals("")) {
                holder.benBankName.setText("Not Specified");
            } else {
                holder.benBankName.setText(((BankModel) historyList.get(position)).getBankName());
            }

            holder.benName.setText(((BankModel) historyList.get(position)).getAccountHolderName());
            holder.benIfsc.setText(((BankModel) historyList.get(position)).getIfsc());

            if (!((BankModel) historyList.get(position)).getStatus().equals("")) {
                holder.benTransferButton.setVisibility(View.VISIBLE);
                holder.benVerifyButton.setVisibility(View.GONE);
                holder.benStatus.setText("Verified");
                holder.benStatus.setTextColor(context.getResources().getColor(R.color.green_status));
            } else {
                holder.benTransferButton.setVisibility(View.GONE);
                holder.benVerifyButton.setVisibility(View.VISIBLE);
                holder.benStatus.setText("Not Verified");
                holder.benStatus.setTextColor(context.getResources().getColor(R.color.color_red));
            }


            holder.benVerifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ben_account= ((BankModel) historyList.get(position)).getAccountNumber();
                    String ben_ifsc = ((BankModel) historyList.get(position)).getIfsc();
                    String beneficiary_id = ((BankModel) historyList.get(position)).getBeneficiary_id();
                    String fName = ((BankModel) historyList.get(position)).getAccountHolderName();
                    String phone = ((BankModel) historyList.get(position)).getMobile();

                    context.startActivity(new Intent(context, VerifyAccountActivity.class)
                            .putExtra("ben_account", ben_account)
                            .putExtra("ben_ifsc", ben_ifsc)
                            .putExtra("beneficiary_id", beneficiary_id)
                            .putExtra("name", fName)
                            .putExtra("phone", phone));

                    Log.d("sdataToShow", ben_account+"\n"+ben_ifsc+"\n"+beneficiary_id);
                }
            });

            holder.benDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initiateDelete(((BankModel) historyList.get(position)).getBeneficiary_id(), position);
                }
            });

            holder.benTransferButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(ApiParams.status, ((BankModel) historyList.get(position)).getStatus());
                    intent.putExtra(ApiParams.name, ((BankModel) historyList.get(position)).getAccountHolderName());
                    intent.putExtra(ApiParams.beneficiary_id, ((BankModel) historyList.get(position)).getBeneficiary_id());
                    intent.putExtra(ApiParams.ben_account, ((BankModel) historyList.get(position)).getAccountNumber());
                    intent.putExtra(ApiParams.ben_ifsc, ((BankModel) historyList.get(position)).getIfsc());
                    intent.putExtra(ApiParams.mobile_number, ((BankModel) historyList.get(position)).getMobile());
                    EventBus.getDefault().postSticky(new ListingBeneficiaryEvent(((BankModel) historyList.get(position)).getStatus()
                            ,((BankModel) historyList.get(position)).getBeneficiary_id(),
                            ((BankModel) historyList.get(position)).getAccountNumber(),
                            ((BankModel) historyList.get(position)).getIfsc(),
                            ((BankModel) historyList.get(position)).getMobile(),
                            ((BankModel) historyList.get(position)).getAccountHolderName()));
                            ((Activity) context).finish();
                }
            });
        }
    }

    String mSuccess, mMessage;
    private void initiateDelete(final String beneficiary_id, final int position) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.normalDelete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>registrationResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                String otpCheck = jsonObject.getJSONObject(ApiParams.data).getString(ApiParams.otp);
                                if (otpCheck.equals("0")) {
                                    historyList.remove(historyList.get(position));
                                    SeeAllBeneficiaryAdapter.this.notifyItemRemoved(position);
                                } else {
                                    Intent intent = new Intent(context, DmtBeneficiaryDeleteOtpActivity.class);
                                    intent.putExtra(ApiParams.beneficiary_id, beneficiary_id);
                                    ((Activity) context).startActivity(intent);
                                }
                            } else {
                                M.dialogOk(context, mMessage, "Error");
                            }
                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(ApiParams.beneficiary_id, beneficiary_id);
                map.put(ApiParams.user_id, Prefes.getUserID(context));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, accessToken);
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        NormalTextView benAccountNumber, benName, benBankName, benIfsc, benStatus;
        NormalButton benVerifyButton, benDeleteButton, benTransferButton;
        LinearLayout tap;

        public MyViewHolder(View itemView) {
            super(itemView);
            benAccountNumber = itemView.findViewById(R.id.benAccountNumber);
            benName = itemView.findViewById(R.id.benName);
            benBankName = itemView.findViewById(R.id.benBankName);
            benIfsc = itemView.findViewById(R.id.benIfsc);
            benStatus = itemView.findViewById(R.id.benStatus);
            benVerifyButton = itemView.findViewById(R.id.benVerifyButton);
            benDeleteButton = itemView.findViewById(R.id.benDeleteButton);
            benTransferButton = itemView.findViewById(R.id.benTransferButton);
            tap = itemView.findViewById(R.id.tap);
        }
    }

}
