package com.vynkpay.activity.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.vynkpay.retrofit.model.GetStateResponse;
import com.vynkpay.R;
import com.vynkpay.activity.OtpActivityNew;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetCountryResponse;
import com.vynkpay.retrofit.model.RegisterResponse;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signupnew extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    @BindView(R.id.linMain)
    LinearLayout linMain;
    @BindView(R.id.signLoginText)
    NormalTextView signLoginText;
    @BindView(R.id.signUpBtn)
    NormalButton signUpBtn;
    @BindView(R.id.countryNameEdt)
    NormalTextView countryNameEdt;
    @BindView(R.id.countryCodeText)
    NormalEditText countryCodeText;
    @BindView(R.id.referIdEdt)
    NormalEditText referIdEdt;
    @BindView(R.id.fullNameEdt)
    NormalEditText fullNameEdt;
    @BindView(R.id.emailEdt)
    NormalEditText emailEdt;
    @BindView(R.id.userNameEdt)
    NormalEditText userNameEdt;
    @BindView(R.id.mobileEdt)
    NormalEditText mobileEdt;
    @BindView(R.id.passwordEdt)
    NormalEditText passwordEdt;
    @BindView(R.id.confirmPassEdt)
    NormalEditText confirmPassEdt;

    @BindView(R.id.panNumberEdt)
    NormalEditText panNumberEdt;

    @BindView(R.id.adharNumberEdt)
    NormalEditText adharNumberEdt;

    @BindView(R.id.agreementCheckbox)
    MaterialCheckBox agreementCheckbox;
    String agree, affiliate;
    @BindView(R.id.affilateCheckbox)
    MaterialCheckBox affilateCheckbox;
    @BindView(R.id.tvAgreement)
    NormalTextView tvAgreement;
    @BindView(R.id.tvAffilate)
    NormalTextView tvAffilate;
    @BindView(R.id.stateNameEdt)
    NormalTextView stateNameEdt;
    ArrayList<GetStateResponse> stateList = new ArrayList<>();
    Dialog dialog, serverDialog,stateDialog;
    String countryId = "", referalCode="",stateId = "";
    SharedPreferences sp,sp1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.signup_new);
        sp = getSharedPreferences("sp",Context.MODE_PRIVATE);
        sp1 = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
        ButterKnife.bind(this);
        getDynamicLink();
        serverDialog = M.showDialog(Signupnew.this, "", false, false);

        countryNameEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverDialog.show();
                MainApplication.getApiService().getCountry().enqueue(new Callback<GetCountryResponse>() {
                    @Override
                    public void onResponse(Call<GetCountryResponse> call, Response<GetCountryResponse> response) {
                        if (response.isSuccessful()) {
                            serverDialog.dismiss();
                            dialog = new Dialog(Signupnew.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            if (dialog.getWindow()!=null) {
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            }
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.setContentView(R.layout.country_dialog_new);
                            SearchView searchView = dialog.findViewById(R.id.searchView);
                            searchView.setIconified(false);
                            RecyclerView countryRecycler = dialog.findViewById(R.id.countryRecycler);
                            GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                            CountryAdapter adapter = new CountryAdapter(getApplicationContext(), response.body().getData());
                            countryRecycler.setLayoutManager(manager);
                            countryRecycler.setAdapter(adapter);


                            //searchView functionality
                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    adapter.filter(newText);
                                    return false;
                                }
                            });

                            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                                @Override
                                public boolean onClose() {
                                    if (!searchView.isFocused()) {
                                        dialog.dismiss();
                                    }
                                    return false;
                                }
                            });

                            dialog.show();

                        }
                    }

                    @Override
                    public void onFailure(Call<GetCountryResponse> call, Throwable t) {
                        Toast.makeText(Signupnew.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });

        if(sp.getString("value", "").equalsIgnoreCase("Global")){
            stateNameEdt.setVisibility(View.GONE);
        } else{
            stateNameEdt.setVisibility(View.VISIBLE);
        }

        stateNameEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Signupnew.this, "state click", Toast.LENGTH_SHORT).show();
                serverDialog.show();
                stateList.clear();
                MainApplication.getApiService().getAllState().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        serverDialog.dismiss();
                        Log.d("stateResponse",response.body());
                        try {
                            JSONObject jData = new JSONObject(response.body());
                            if (jData.getString("status").equalsIgnoreCase("true")){
                                JSONObject data = jData.getJSONObject("data");
                                JSONArray stateArr = data.getJSONArray("state");
                                for (int i=0; i<stateArr.length();i++){
                                    JSONObject sData = stateArr.getJSONObject(i);
                                    String id = sData.getString("id");
                                    String state_name = sData.getString("state_name");
                                    stateList.add(new GetStateResponse(id,state_name));
                                }
                                stateDialog = new Dialog(Signupnew.this);
                                stateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                if (stateDialog.getWindow()!=null){
                                    stateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                }
                                stateDialog.setCancelable(false);
                                stateDialog.setCanceledOnTouchOutside(true);
                                stateDialog.setContentView(R.layout.custom_statedialog);
                                SearchView searchView = stateDialog.findViewById(R.id.searchViewState);
                                searchView.setIconified(false);
                                RecyclerView stateRecycler = stateDialog.findViewById(R.id.stateRecycler);
                                GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false);
                                stateRecycler.setLayoutManager(manager);
                                StateAdapter stateAdapter = new StateAdapter(Signupnew.this,stateList);
                                stateRecycler.setAdapter(stateAdapter);

                                //searchView functionality
                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        stateAdapter.filter(newText);
                                        return false;
                                    }
                                });

                                searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                                    @Override
                                    public boolean onClose() {
                                        if (!searchView.isFocused()) {
                                            stateDialog.dismiss();
                                        }
                                        return false;
                                    }
                                });

                                stateDialog.show();

                            } else {
                                Log.d("stateResponse","message");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        serverDialog.dismiss();
                        Log.d("stateResponse",t.getMessage()!=null?t.getMessage():"Error");
                    }
                });

            }
        });
        signLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signupnew.this, LoginActivity.class));

            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
            }
        });

       /* tvAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (agreementCheckbox.isChecked()){
                    agreementCheckbox.setChecked(false);
                }else {
                    agreementCheckbox.setChecked(true);
                }

            }
        });

        tvAffilate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (affilateCheckbox.isChecked()){
                    affilateCheckbox.setChecked(false);
                }else {
                    affilateCheckbox.setChecked(true);
                }
            }
        });
*/
        SpannableString ss = new SpannableString(getString(R.string.agree));
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // do some thing
                try {
                    startActivity(new Intent(Signupnew.this, AboutUsActivity.class)
                            .putExtra("title", "Terms & Conditions")
                            .putExtra("page", "terms_conditions"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // do another thing
                try {
                    startActivity(new Intent(Signupnew.this, AboutUsActivity.class)
                            .putExtra("title", "Privacy Policy")
                            .putExtra("page", "privacy_policy"));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };

        ss.setSpan(span1, 11, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(span2, 30, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvAgreement.setText(ss);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString ss1 = new SpannableString(getString(R.string.agreeaffiliate));
        ClickableSpan span3 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // do some thing
                try {
                    startActivity(new Intent(Signupnew.this, AboutUsActivity.class)
                            .putExtra("title", "Affiliate Agreement")
                            .putExtra("page", "affiliate"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };



        ss1.setSpan(span3, 8, ss1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvAffilate.setText(ss1);
        tvAffilate.setMovementMethod(LinkMovementMethod.getInstance());
        if (!sp.getString("referalCode","").equalsIgnoreCase("")){
            referalCode = sp.getString("referalCode","");
            referIdEdt.setText(referalCode);
        }

    }

    private void validations() {

        if (TextUtils.isEmpty(referIdEdt.getText().toString().trim())) {
            Toast.makeText(this, "Please Enter Referral Id", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(countryNameEdt.getText().toString().trim())) {
            Toast.makeText(this, "Please Select Country", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(fullNameEdt.getText().toString().trim())) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(emailEdt.getText().toString().trim())) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdt.getText().toString().trim()).matches()) {
            Toast.makeText(this, "Please Enter Valid  Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(userNameEdt.getText().toString().trim())) {
            Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mobileEdt.getText().toString().trim())) {
            Toast.makeText(this, "Please Enter Mobile Number ", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(passwordEdt.getText().toString().trim())) {
            Toast.makeText(this, "Please Enter Password ", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPassEdt.getText().toString().trim())) {
            Toast.makeText(this, "Please Enter Password ", Toast.LENGTH_SHORT).show();
        } else if (!passwordEdt.getText().toString().trim().equals(confirmPassEdt.getText().toString().trim())) {
            Toast.makeText(this, "Password Do not match ", Toast.LENGTH_SHORT).show();
        } else if (panNumberEdt.getVisibility() == View.VISIBLE && panNumberEdt.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Pan Number", Toast.LENGTH_SHORT).show();
        } else if (adharNumberEdt.getVisibility() == View.VISIBLE && adharNumberEdt.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please Enter Aadahar Number", Toast.LENGTH_SHORT).show();
        } else if (!agreementCheckbox.isChecked()) {
            Toast.makeText(this, "Please Accept User Aggrement", Toast.LENGTH_SHORT).show();
        } else if (!affilateCheckbox.isChecked()) {
            Toast.makeText(this, "Please Accept Affiliate Aggrement", Toast.LENGTH_SHORT).show();
        } else {
            signUp();
        }
    }

    private void signUp() {
        serverDialog.show();
        if (agreementCheckbox.isChecked()) {
            agree = "1";
        }

        if (affilateCheckbox.isChecked()) {
            affiliate = "1";
        }


        MainApplication.getApiService().registerMethod(
                referIdEdt.getText().toString(),
                fullNameEdt.getText().toString(),
                userNameEdt.getText().toString(),
                countryId,
                emailEdt.getText().toString(),
                countryCodeText.getText().toString(),
                mobileEdt.getText().toString(),
                passwordEdt.getText().toString(),
                confirmPassEdt.getText().toString(),
                agree,
                affiliate,
                panNumberEdt.getText().toString(),
                adharNumberEdt.getText().toString(),
                stateId).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                //  Log.d("signupKFjdkhdfdf", new Gson().toJson(response.body()));
                if (response.isSuccessful() && response.body() != null) {
                    serverDialog.dismiss();

                    if (response.body().getUrl().equals("")) {
                        Toast.makeText(Signupnew.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Signupnew.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Signupnew.this, OtpActivityNew.class);
                        intent.putExtra("tempid", "" + response.body().getTempId());
                        startActivity(intent);
                        sp.edit().putString("referalCode","").apply();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d("Errorrrrer",t.getMessage()!= null ? t.getMessage() : "Error");
                serverDialog.dismiss();
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(Signupnew.this,Signupnew.this::finishAffinity);
        }
    }

    private class StateAdapter extends RecyclerView.Adapter<StateAdapter.SHolder>{

        Context context;
        List<GetStateResponse> mList;
        List<GetStateResponse> searchedItemModelArrayList;

        public StateAdapter(Context applicationContext, List<GetStateResponse> data) {
            this.context = applicationContext;
            this.mList = data;
            this.searchedItemModelArrayList = new ArrayList<>();
            this.searchedItemModelArrayList.addAll(mList);
        }

        @NonNull
        @Override
        public SHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Signupnew.this).inflate(R.layout.country_list,parent,false);
            return new SHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SHolder holder, int position) {
            try {
                holder.countryText.setText(mList.get(position).getStatename());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stateDialog.dismiss();
                        stateNameEdt.setText(mList.get(position).getStatename());
                        stateId = mList.get(position).getId();
                        Log.d("selectedState",mList.get(position).toString());
                        linMain.clearFocus();
                        hideKeyboard();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mList != null ? mList.size() : 0;
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            mList.clear();
            if (charText.length() == 0) {
                mList.addAll(searchedItemModelArrayList);
            } else {
                for (GetStateResponse wp : searchedItemModelArrayList) {
                    if (wp.getStatename().toLowerCase(Locale.getDefault()).contains(charText)) {
                        mList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }

        class SHolder extends RecyclerView.ViewHolder{

            TextView countryText;
            public SHolder(View view) {
                super(view);
                countryText = view.findViewById(R.id.countryText);
            }
        }
    }

    public void hideKeyboard() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {
        Context context;
        List<GetCountryResponse.Datum> mList;
        List<GetCountryResponse.Datum> searchedItemModelArrayList;

        public CountryAdapter(Context applicationContext, List<GetCountryResponse.Datum> data) {
            this.context = applicationContext;
            this.mList = data;
            this.searchedItemModelArrayList = new ArrayList<>();
            this.searchedItemModelArrayList.addAll(mList);
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.country_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            GetCountryResponse.Datum data = mList.get(position);
            holder.countryText.setText(data.getText());
            holder.countryText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    countryNameEdt.setText(data.getText());
                    fullNameEdt.requestFocus();
                    countryCodeText.setVisibility(View.VISIBLE);
                    countryCodeText.setText("+" + data.getStdCode());

                    countryId = data.getId();

                    if (data.getStdCode().equals("91")) {
                        panNumberEdt.setVisibility(View.VISIBLE);
                        adharNumberEdt.setVisibility(View.VISIBLE);
                        stateNameEdt.setVisibility(View.VISIBLE);
                    } else {
                        panNumberEdt.setVisibility(View.GONE);
                        adharNumberEdt.setVisibility(View.GONE);
                        stateNameEdt.setVisibility(View.GONE);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            mList.clear();
            if (charText.length() == 0) {
                mList.addAll(searchedItemModelArrayList);
            } else {
                for (GetCountryResponse.Datum wp : searchedItemModelArrayList) {
                    if (wp.getText().toLowerCase(Locale.getDefault()).contains(charText)) {
                        mList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView countryText;
            public MyViewHolder(View view) {
                super(view);
                countryText = view.findViewById(R.id.countryText);
            }

        }
    }


    public void getDynamicLink() {
        Log.d("refferla","called");
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                // Get deep link from result (may be null if no link is found)
                Log.d("refferla","called1");
                Uri deepLink = null;
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.getLink();
                    String referLink = deepLink.toString();
                    referLink = referLink.substring(referLink.lastIndexOf("=") + 1);
                    referalCode = referLink;
                    sp.edit().putString("referalCode",referalCode).apply();
                    referIdEdt.setText(referalCode);
                    referIdEdt.setFocusableInTouchMode(false);
                    referIdEdt.setFocusable(false);
                    referIdEdt.setClickable(false);

                    Log.e("linkkk", "" + referalCode);
                    Log.e("linkkk", "" + referLink);
                    Log.e("linkkk", "" + pendingDynamicLinkData);
                    Log.e("linkkk", "" + deepLink);
                } else {
                    if (!sp.getString("referalCode","").equalsIgnoreCase("")){
                        referalCode = sp.getString("referalCode","");
                        referIdEdt.setText(referalCode);
                        referIdEdt.setFocusableInTouchMode(false);
                        referIdEdt.setFocusable(false);
                        referIdEdt.setClickable(false);
                    }else {
                        Log.d("refferla","calledF");
                        referIdEdt.setFocusableInTouchMode(true);
                        referIdEdt.setFocusable(true);
                        referIdEdt.setClickable(true);
                    }
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("refferla","called2");
                Toast.makeText(Signupnew.this, e.toString(), Toast.LENGTH_SHORT).show();
                if (!sp.getString("referalCode","").equalsIgnoreCase("")){
                    referalCode = sp.getString("referalCode","");
                    referIdEdt.setText(referalCode);
                    referIdEdt.setFocusableInTouchMode(false);
                    referIdEdt.setFocusable(false);
                    referIdEdt.setClickable(false);
                }else {
                    referIdEdt.setFocusableInTouchMode(true);
                    referIdEdt.setFocusable(true);
                    referIdEdt.setClickable(true);
                }
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(Signupnew.this).setConnectivityListener(this);
    }
}
