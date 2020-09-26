package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityVerifyAccountBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyAccountActivity extends AppCompatActivity {
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ActivityVerifyAccountBinding binding;

    Dialog dialog;
    String beneficiary_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_verify_account);
        ButterKnife.bind(VerifyAccountActivity.this);
        dialog = M.showDialog(VerifyAccountActivity.this, "", false, false);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle.setText("Verify Account");


        Intent intent=getIntent();
        if (intent!=null){
            String ben_account= intent.getStringExtra("ben_account");
            String ben_ifsc = intent.getStringExtra("ben_ifsc");
            beneficiary_id = intent.getStringExtra("beneficiary_id");
            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");

            binding.etFirstName.setText(name);
            binding.etAccountNumber.setText(ben_account);
            binding.etIFSC.setText(ben_ifsc);
            binding.etMobileNumber.setText(phone);
        }

        binding.verifyBeneficiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (walletAmount>0){
                    if (binding.etFirstName.getText().toString().trim().isEmpty()){
                        Toast.makeText(VerifyAccountActivity.this, "Please enter full name", Toast.LENGTH_SHORT).show();
                    } else if (binding.etAccountNumber.getText().toString().trim().isEmpty()){
                        Toast.makeText(VerifyAccountActivity.this, "Please enter account number", Toast.LENGTH_SHORT).show();
                    }else if (binding.etReAccountNumber.getText().toString().trim().isEmpty()){
                        Toast.makeText(VerifyAccountActivity.this, "Please confirm account number", Toast.LENGTH_SHORT).show();
                    }else if (!binding.etAccountNumber.getText().toString().trim().equals(binding.etReAccountNumber.getText().toString().trim())){
                        Toast.makeText(VerifyAccountActivity.this, "Account number mismatched", Toast.LENGTH_SHORT).show();
                    }else if (binding.etIFSC.getText().toString().trim().isEmpty()){
                        Toast.makeText(VerifyAccountActivity.this, "Please enter IFSC Code", Toast.LENGTH_SHORT).show();
                    }else {
                        dialog();
                    }
                }else {
                    Toast.makeText(VerifyAccountActivity.this, "Insufficient amount in wallet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fetchWalletData();
    }

    public void dialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(VerifyAccountActivity.this);
        builder.setCancelable(false);

        View view= LayoutInflater.from(VerifyAccountActivity.this).inflate(R.layout.app_update_layout_rcg, null);
        builder.setView(view);

        TextView titleTV=view.findViewById(R.id.titleTV);
        TextView messageTV=view.findViewById(R.id.messageTV);
        TextView noThanksButtonTV=view.findViewById(R.id.noThanksButtonTV);
        TextView updateButtonTV=view.findViewById(R.id.updateButtonTV);

        titleTV.setText("Warning!");
        messageTV.setText("Amount of "+Functions.CURRENCY_SYMBOL+" 3.5 will be deducted as service charges to verify beneficiary. Click OK");
        noThanksButtonTV.setText("NO");
        noThanksButtonTV.setVisibility(View.VISIBLE);
        updateButtonTV.setText("OK");

        AlertDialog alertDialog=builder.create();

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);

        alertDialog.show();

        noThanksButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        updateButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                callUpdate(Prefes.getUserID(VerifyAccountActivity.this),
                        binding.etAccountNumber.getText().toString().trim(),
                        binding.etIFSC.getText().toString().trim(),
                        beneficiary_id);
            }
        });

        if (alertDialog.getWindow()!=null){
            alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
    }

    public void callUpdate(String user_id, String ben_account, String ben_ifsc, String beneficiary_id){
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.beginValidationByOtp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("sdsvrifBenn", response+"//");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String mSuccess = jsonObject.optString(ApiParams.success);
                            String mMessage = jsonObject.optString(ApiParams.message);

                            if (mSuccess.equalsIgnoreCase("true")){
                                finish();
                            }else {
                                JSONObject pay_response=jsonObject.getJSONObject("pay_response");
                                String OPERATOR_ERROR_MESSAGE=pay_response.optString("OPERATOR_ERROR_MESSAGE");
                                Toast.makeText(VerifyAccountActivity.this, OPERATOR_ERROR_MESSAGE+"", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("user_id", user_id);
                map.put("ben_account", ben_account);
                map.put("ben_ifsc", ben_ifsc);
                map.put("beneficiary_id", beneficiary_id);

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(VerifyAccountActivity.this));
                return params;
            }

        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(VerifyAccountActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    Double walletAmount=0.0;
    private void fetchWalletData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.wallet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>walletData", "onResponse: " + jsonObject.toString());
                            String mSuccess = jsonObject.optString(ApiParams.success);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data);
                                String balance = jsonObject1.getString(ApiParams.balance);
                                walletAmount = Double.parseDouble(balance);
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
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            M.dialogOk(VerifyAccountActivity.this, "Please check internet connection", "Error");
                        } else if (error instanceof AuthFailureError) {
                            M.dialogOk(VerifyAccountActivity.this, "You are not authorized", "Error");
                        } else if (error instanceof ServerError) {
                            M.dialogOk(VerifyAccountActivity.this, "Server issue", "Error");
                        } else if (error instanceof NetworkError) {
                            M.dialogOk(VerifyAccountActivity.this, "Please check internet connection", "Error");
                        } else if (error instanceof ParseError) {
                            M.dialogOk(VerifyAccountActivity.this, "Internal error", "Error");
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(VerifyAccountActivity.this));
                return params;
            }
        };
        MySingleton.getInstance(VerifyAccountActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
