package com.vynkpay.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
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

public class AddNewBeneficiaryFragment extends Fragment implements View.OnClickListener {
    View view;
    @BindView(R.id.etFirstName)
    NormalEditText etFirstName;
    @BindView(R.id.etLastName)
    NormalEditText etLastName;
    @BindView(R.id.etMobileNumber)
    NormalEditText etMobileNumber;
    @BindView(R.id.etAccountNumber)
    NormalEditText etAccountNumber;
    @BindView(R.id.etReAccountNumber)
    NormalEditText etReAccountNumber;
    @BindView(R.id.etIFSC)
    NormalEditText etIFSC;
    @BindView(R.id.addBeneficiaryButton)
    NormalButton addBeneficiaryButton;
    @BindView(R.id.addWithVerify)
    NormalButton addWithVerify;
    Dialog dialog;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_beneficiary_rcg, container, false);
        ButterKnife.bind(AddNewBeneficiaryFragment.this, view);
        dialog = M.showDialog(getActivity(), "", false, false);

        Log.i(">>axxe", "onCreateView: ");
        setListeners();

        fetchWalletData();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=getActivity();
    }

    private void setListeners() {
        addBeneficiaryButton.setOnClickListener(this);
        addWithVerify.setOnClickListener(this);
    }

    String mMessage, mSuccess;

    private void addBeneficiaryOnDmt() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.addBeneficiary,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(">>addingBen", "onResponse: " + response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject data = jsonObject.getJSONObject(ApiParams.data);
                                //JSONObject addInfo = jsonObject.getJSONObject(ApiParams.add_info).getJSONObject(ApiParams.data).getJSONObject(ApiParams.beneficiary);
                                String beneficiary_id = data.optString("beneficiary_id");
                                String verification_status = data.optString("verification_status");

                                if (verification_status.equals("VERIFIED")){
                                    dialog.dismiss();
                                    Toast.makeText(activity, "Beneficiary already existed", Toast.LENGTH_SHORT).show();
                                }else {
                                    callUpdate(Prefes.getUserID(activity), etAccountNumber.getText().toString(), etIFSC.getText().toString(), beneficiary_id);
                                }

                            } else {
                                dialog.dismiss();
                                M.dialogOk(getActivity(), mMessage, "Error");
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
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(ApiParams.user_id, Prefes.getUserID(activity));
                map.put(ApiParams.fname, etFirstName.getText().toString());
                map.put(ApiParams.lname, etLastName.getText().toString());
                map.put(ApiParams.ben_account, etAccountNumber.getText().toString());
                map.put(ApiParams.ben_ifsc, etIFSC.getText().toString());
                map.put(ApiParams.mobile_number, etMobileNumber.getText().toString());
                //map.put(ApiParams.verify, isVerificationNeeded + "");

                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(getActivity().getApplicationContext()));
                return params;
            }

        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void fetchCustomerDetails() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.customerDetail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>customerDetail", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {

                            } else {
                                M.dialogOk(getActivity(), mMessage, "Error");
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
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(getActivity().getApplicationContext()));
                return params;
            }

        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //1 for normal Addition with verification
            case R.id.addWithVerify:
                if (etFirstName.length() == 0 || etFirstName.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_first_name), Toast.LENGTH_SHORT).show();
                } else if (etLastName.length() == 0 || etLastName.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_last_name), Toast.LENGTH_SHORT).show();
                } else if (etMobileNumber.length() == 0 || etMobileNumber.getText().toString().trim().length() == 0
                        || etMobileNumber.length() < 10) {
                    Toast.makeText(getActivity(), getString(R.string.valid_phone), Toast.LENGTH_SHORT).show();
                } else if (etAccountNumber.length() == 0 || etAccountNumber.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_account), Toast.LENGTH_SHORT).show();
                } else if (etIFSC.length() == 0 || etIFSC.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_ifsc), Toast.LENGTH_SHORT).show();
                } else if ((etAccountNumber.length() != etReAccountNumber.length()) ||
                        !(etAccountNumber.getText().toString().equals(etReAccountNumber.getText().toString()))) {
                    Toast.makeText(getActivity(), getString(R.string.not_matched_account), Toast.LENGTH_SHORT).show();
                } else {
                    if (walletAmount>0){
                        dialog();
                    }else {
                        Toast.makeText(activity, "Insufficient amount in wallet", Toast.LENGTH_SHORT).show();
                    }
                }
                //0 for normal Addition with verification
                break;
            case R.id.addBeneficiaryButton:
                if (etFirstName.length() == 0 || etFirstName.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_first_name), Toast.LENGTH_SHORT).show();
                } else if (etLastName.length() == 0 || etLastName.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_last_name), Toast.LENGTH_SHORT).show();
                } else if (etMobileNumber.length() == 0 || etMobileNumber.getText().toString().trim().length() == 0
                        || etMobileNumber.length() < 10) {
                    Toast.makeText(getActivity(), getString(R.string.valid_phone), Toast.LENGTH_SHORT).show();
                } else if (etAccountNumber.length() == 0 || etAccountNumber.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_account), Toast.LENGTH_SHORT).show();
                } else if (etIFSC.length() == 0 || etIFSC.getText().toString().trim().length() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.valid_ifsc), Toast.LENGTH_SHORT).show();
                } else if ((etAccountNumber.length() != etReAccountNumber.length()) ||
                        !(etAccountNumber.getText().toString().equals(etReAccountNumber.getText().toString()))) {
                    Toast.makeText(getActivity(), getString(R.string.not_matched_account), Toast.LENGTH_SHORT).show();
                } else {
                    if (walletAmount>0){
                        dialog();
                    }else {
                        Toast.makeText(activity, "Insufficient amount in wallet", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    public void dialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setCancelable(false);

        View view= LayoutInflater.from(activity).inflate(R.layout.app_update_layout_rcg, null);
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
                addBeneficiaryOnDmt();
            }
        });

        if (alertDialog.getWindow()!=null){
            alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
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
                            M.dialogOk(activity, "Please check internet connection", "Error");
                        } else if (error instanceof AuthFailureError) {
                            M.dialogOk(activity, "You are not authorized", "Error");
                        } else if (error instanceof ServerError) {
                            M.dialogOk(activity, "Server issue", "Error");
                        } else if (error instanceof NetworkError) {
                            M.dialogOk(activity, "Please check internet connection", "Error");
                        } else if (error instanceof ParseError) {
                            M.dialogOk(activity, "Internal error", "Error");
                        }
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(getActivity().getApplicationContext()));
                return params;
            }
        };
        MySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void callUpdate(String user_id, String ben_account, String ben_ifsc, String beneficiary_id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.beginValidationByOtp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d("sdsvrifBenn", response+"//");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);

                            if (mSuccess.equalsIgnoreCase("true")){
                                FragmentManager manager = getFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.frame_container, new SeeAllBeneficiaryFragment());
                                transaction.commit();
                            }else {
                                JSONObject pay_response=jsonObject.getJSONObject("pay_response");
                                String OPERATOR_ERROR_MESSAGE=pay_response.optString("OPERATOR_ERROR_MESSAGE");
                                Toast.makeText(activity, OPERATOR_ERROR_MESSAGE+"", Toast.LENGTH_SHORT).show();
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
                params.put(ApiParams.access_token, Prefes.getAccessToken(getActivity().getApplicationContext()));
                return params;
            }

        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(activity).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
