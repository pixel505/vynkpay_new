package com.vynkpay.activity.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.OtpActivityNew;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetCountryResponse;
import com.vynkpay.retrofit.model.OtpVerifyResponse;
import com.vynkpay.retrofit.model.RegisterResponse;
import com.vynkpay.utils.M;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signupnew extends AppCompatActivity {
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
    Dialog dialog, serverDialog;
    String countryId, referalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_new);
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
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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


    }

    private void validations() {

        if (referIdEdt.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please Enter Referral Id", Toast.LENGTH_SHORT).show();

        } else if (countryNameEdt.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please Select Country", Toast.LENGTH_SHORT).show();

        } else if (fullNameEdt.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();

        } else if (emailEdt.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdt.getText().toString().trim()).matches()) {
            Toast.makeText(this, "Please Enter Valid  Email", Toast.LENGTH_SHORT).show();

        } else if (userNameEdt.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show();

        } else if (mobileEdt.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please Enter Mobile Number ", Toast.LENGTH_SHORT).show();

        } else if (passwordEdt.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please Enter Password ", Toast.LENGTH_SHORT).show();

        } else if (confirmPassEdt.getText().toString().trim().length() == 0) {
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
                referIdEdt.getText().toString(), fullNameEdt.getText().toString()
                , userNameEdt.getText().toString(),
                countryId,
                emailEdt.getText().toString(),
                countryCodeText.getText().toString(),
                mobileEdt.getText().toString(),
                passwordEdt.getText().toString(),
                confirmPassEdt.getText().toString(),
                agree,
                affiliate,
                panNumberEdt.getText().toString(),
                adharNumberEdt.getText().toString()).enqueue(new Callback<RegisterResponse>() {
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

                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                serverDialog.dismiss();
            }
        });
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
                    } else {
                        panNumberEdt.setVisibility(View.GONE);
                        adharNumberEdt.setVisibility(View.GONE);
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
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                // Get deep link from result (may be null if no link is found)
                Uri deepLink = null;
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.getLink();
                    String referLink = deepLink.toString();
                    referLink = referLink.substring(referLink.lastIndexOf("=") + 1);
                    referalCode = referLink;
                    referIdEdt.setText(referalCode);
                    Log.e("linkkk", "" + referalCode);
                    Log.e("linkkk", "" + referLink);
                    Log.e("linkkk", "" + pendingDynamicLinkData);
                    Log.e("linkkk", "" + deepLink);
                } else {
                }
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Signupnew.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
