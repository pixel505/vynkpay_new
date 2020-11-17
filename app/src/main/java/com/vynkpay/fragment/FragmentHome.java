package com.vynkpay.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.vynkpay.activity.activities.AboutUsActivity;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.activity.recharge.broadband.activity.BroadbandActivity;
import com.vynkpay.activity.recharge.datacard.activity.DataCardActivity;
import com.vynkpay.activity.recharge.dth.activity.DthActivity;
import com.vynkpay.activity.recharge.electricity.activity.ElectricityActivity;
import com.vynkpay.activity.recharge.gas.GasActivity;
import com.vynkpay.activity.recharge.insurance.InsuranceActivity;
import com.vynkpay.activity.recharge.landline.activity.LandLineActivity;
import com.vynkpay.activity.recharge.mobile.activity.PostPaidActivity;
import com.vynkpay.activity.recharge.mobile.activity.PrepaidActivity;
import com.vynkpay.activity.recharge.water.WaterActivity;
import com.vynkpay.adapter.ImageAdapter;
import com.vynkpay.models.BannerModel;
import com.vynkpay.models.PDFFileModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetWalletResponse;
import com.vynkpay.utils.M;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;


public class FragmentHome extends Fragment {
    @BindView(R.id.dthLinear)
    LinearLayout dthLinear;
    @BindView(R.id.electricityLn)
    LinearLayout electricityLn;
    @BindView(R.id.dataCardLn)
    LinearLayout dataCardLn;
    @BindView(R.id.landLineLn)
    LinearLayout landLineLn;
    @BindView(R.id.waterLn)
    LinearLayout waterLn;
    @BindView(R.id.gasLn)
    LinearLayout gasLn;
    @BindView(R.id.insuranceLn)
    LinearLayout insuranceLn;
    @BindView(R.id.moreLn)
    LinearLayout moreLn;
    @BindView(R.id.rechargeLn)
    LinearLayout rechargeLn;
    @BindView(R.id.broadbandLn)
    LinearLayout broadbandLn;
    @BindView(R.id.userIdText)
    TextView userIdText;

    @BindView(R.id.bonusWalletText)
    TextView bonusWalletText;

    @BindView(R.id.vCashWalletText)
    TextView vCashWalletText;

    @BindView(R.id.tvBonus)
    TextView tvBonus;



    @BindView(R.id.mCashWalletText)
    TextView mCashWalletText;

    @BindView(R.id.userLayout)
    LinearLayout userLayout;

    @BindView(R.id.bonusLinear)
    LinearLayout bonusLinear;

    @BindView(R.id.mCashLinear)
    LinearLayout mCashLinear;

    @BindView(R.id.vCashLinear)
    LinearLayout vCashLinear;

    @BindView(R.id.viewPager)
    AutoScrollViewPager viewPager;

    @BindView(R.id.tabLayout)
    DotsIndicator tabLayout;

    @BindView(R.id.crdBouns)
    CardView crdBouns;

    String bonusBalance,mCashBalance,vCashBalance;

    SharedPreferences popupSP;
    SharedPreferences.Editor editor;
    Activity activity;
    String imageURL="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        popupSP = activity.getSharedPreferences("popupSP", Context.MODE_PRIVATE);
        editor = popupSP.edit();
        editor.putBoolean("showPopup", true).apply();

        getBonusTransaction();
        getVCashTransaction();
        getMCashTransaction();

        fetchWalletData();

        if (Prefes.getAccessToken(activity).equals("")) {
            userLayout.setVisibility(View.GONE);
        } else {
            userLayout.setVisibility(View.VISIBLE);
            if (Prefes.getUserType(activity).equalsIgnoreCase("2")){
                userIdText.setText("");
            }else {
                userIdText.setText(getString(R.string.userName) + " " + Prefes.getUserName(activity));
            }
        }


        if (Prefes.getUserType(activity).equalsIgnoreCase("2")){
            //crdBouns.setVisibility(View.VISIBLE);
            tvBonus.setText("Cashback");
        }else {
            //crdBouns.setVisibility(View.VISIBLE);
            tvBonus.setText("Bonus Wallet");
        }

        bonusLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefes.getAccessToken(activity).equals("")) {
                    startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    startActivity(new Intent(activity, BonusWalletFragment.class).putExtra("balancWallet",bonusBalance));
                }
            }
        });

        mCashLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefes.getAccessToken(activity).equals("")) {
                    startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    startActivity(new Intent(activity,MCashWalletFragment.class).putExtra("balancWalletM",mCashBalance));
                }
            }
        });

        vCashLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefes.getAccessToken(activity).equals("")) {
                    startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    startActivity(new Intent(activity,VCashWalletFragment.class).putExtra("balancWalletV",vCashBalance));
                }
            }
        });

        rechargeLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupSP.getBoolean("popupSP", true)){
                    preLoadDialog();
                }else {
                    M.reset(activity);
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    ViewGroup viewGroup = v.findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(activity).inflate(R.layout.custom_rcg_dialog, viewGroup, false);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();
                    LinearLayout postPaid = dialogView.findViewById(R.id.btn_postpaid);
                    LinearLayout prePaid = dialogView.findViewById(R.id.btn_prepaid);
                    postPaid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            startActivity(new Intent(activity, PostPaidActivity.class).putExtra("type", "2"));
                        }
                    });
                    prePaid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            startActivity(new Intent(activity, PrepaidActivity.class).putExtra("type", "1"));
                        }
                    });
                    alertDialog.show();

                    if (alertDialog.getWindow() != null) {
                        alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                    }
                }
            }
        });

        dthLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupSP.getBoolean("popupSP", true)){
                    preLoadDialog();
                }else {
                    M.reset(activity);
                    startActivity(new Intent(activity, DthActivity.class));
                }
            }
        });

        broadbandLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupSP.getBoolean("popupSP", true)){
                    preLoadDialog();
                }else {
                    M.reset(activity);
                    startActivity(new Intent(activity, BroadbandActivity.class));
                }
            }
        });

        electricityLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupSP.getBoolean("popupSP", true)){
                    preLoadDialog();
                }else {
                    M.reset(activity);
                    startActivity(new Intent(activity, ElectricityActivity.class));
                }
            }
        });

        dataCardLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupSP.getBoolean("popupSP", true)){
                    preLoadDialog();
                }else {
                    M.reset(activity);
                    startActivity(new Intent(activity, DataCardActivity.class));
                }
            }
        });

        landLineLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupSP.getBoolean("popupSP", true)){
                    preLoadDialog();
                }else {
                    M.reset(activity);
                    startActivity(new Intent(activity, LandLineActivity.class));
                }
            }
        });

        waterLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupSP.getBoolean("popupSP", true)){
                    preLoadDialog();
                }else {
                    M.reset(activity);
                    startActivity(new Intent(activity, WaterActivity.class));
                }
            }
        });

        gasLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupSP.getBoolean("popupSP", true)){
                    preLoadDialog();
                }else {
                    M.reset(activity);
                    startActivity(new Intent(activity, GasActivity.class));
                }
            }
        });

        insuranceLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupSP.getBoolean("popupSP", true)){
                    preLoadDialog();
                }else {
                    M.reset(activity);
                    startActivity(new Intent(activity, InsuranceActivity.class));
                }
            }
        });


        moreLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        getBanners();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    private void fetchWalletData() {
        MainApplication.getApiService().walletAmountMethod(Prefes.getAccessToken(activity)).enqueue(new Callback<GetWalletResponse>() {
            @Override
            public void onResponse(Call<GetWalletResponse> call, retrofit2.Response<GetWalletResponse> response) {
                if (response.body().getSuccess()) {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {

                            bonusWalletText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getEarningBalance());
                            vCashWalletText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getBalance());

                            Prefes.saveCash(response.body().getData().getBalance(), activity);
                            mCashWalletText.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getWalletRedeem());

                        } else {

                        }
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<GetWalletResponse> call, Throwable t) {
                Log.d("Error",t.getMessage()!=null?t.getMessage():"Error");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Prefes.getAccessToken(activity).equals("")) {
            userLayout.setVisibility(View.GONE);
        } else {
            userLayout.setVisibility(View.VISIBLE);
            if (Prefes.getUserType(activity).equalsIgnoreCase("2")){
                userIdText.setText("");
            }else {
                userIdText.setText(getString(R.string.userName) + " " + Prefes.getUserName(activity));
            }
        }
    }

    ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();

    private void getBanners() {
        bannerModelArrayList.clear();
        ApiCalls.getBanners(activity,Prefes.getAccessToken(activity), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("sdasdasddsdasdas", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String banner_image = object.getString("banner_image");
                            String path = object.getString("path");
                            String banner_type = object.getString("banner_type");
                            String created_date = object.getString("created_date");
                            String full_banner_image = object.getString("full_banner_image");

                            bannerModelArrayList.add(new BannerModel(id, banner_image, path, banner_type, created_date, full_banner_image));
                        }

                        viewPager.setAdapter(new ImageAdapter(activity, bannerModelArrayList));

                        tabLayout.setViewPager(viewPager);

                        viewPager.startAutoScroll();
                        viewPager.setInterval(5000);
                        viewPager.setStopScrollWhenTouch(true);
                        viewPager.setBorderAnimation(true);
                        viewPager.setScrollDurationFactor(8);

                        JSONArray pdfJsonArray = jsonObject.getJSONArray("pdf");
                        for (int i = 0; i < pdfJsonArray.length(); i++) {
                            JSONObject object = pdfJsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String banner_image = object.getString("banner_image");
                            String path = object.getString("path");
                            String banner_type = object.getString("banner_type");
                            String file_recognize = object.getString("file_recognize");
                            String created_date = object.getString("created_date");
                            String full_banner_image = object.getString("full_banner_image");

                            String compPathOfPDF = path + banner_image;

                            Prefes.savePDFPath(compPathOfPDF, activity);

                            new PDFFileModel(id, banner_image, path, banner_type, file_recognize, created_date, full_banner_image);

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void getBonusTransaction(){
        ApiCalls.getBonusTransactions(activity, Prefes.getAccessToken(activity), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("transactionZLog", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                        bonusBalance=dataObject.getString("walletBalance");
                        JSONArray listingArray = dataObject.getJSONArray("listing");
                        for (int i=0; i<listingArray.length(); i++){
                            JSONObject object=listingArray.getJSONObject(i);
                            String id = object.getString("id");
                            String front_user_id = object.getString("front_user_id");
                            String user_id  = object.getString("user_id");
                            String type  = object.getString("type");
                            String payment_via = object.getString("payment_via");
                            String p_amount  = object.getString("p_amount");
                            String profit_type = object.getString("profit_type");
                            String mode  = object.getString("mode");
                            String transactionStatus  = object.getString("status");
                            String created_date  = object.getString("created_date");
                            String username = object.getString("username");
                            String email  = object.getString("email");
                            String phone  = object.getString("phone");
                            String name  = object.getString("name");
                            String paid_status  = object.getString("paid_status");
                            String balance  = object.getString("balance");
                            String frontusername = object.getString("frontusername");

                            BonusWalletFragment.walletTransactionsModelArrayList.add(new WalletTransactionsModel( id,  front_user_id,  user_id,  type,
                                    payment_via, p_amount,  profit_type,  mode,  transactionStatus,  created_date,  username,
                                    email,  phone,  name,  paid_status,  balance, frontusername));
                        }

                        Collections.reverse(BonusWalletFragment.walletTransactionsModelArrayList);

                    }else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String error) {
                Log.d("Error",error+"");
            }
        });
    }

    private void getVCashTransaction(){
        ApiCalls.getVcashTransactions(activity, Prefes.getAccessToken(activity), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("transactionZLog", result + "//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        vCashBalance=dataObject.getString("walletBalance");

                        JSONArray listingArray = dataObject.getJSONArray("listing");
                        for (int i = 0; i < listingArray.length(); i++) {
                            JSONObject object = listingArray.getJSONObject(i);
                            String id = object.getString("id");
                            String front_user_id = object.getString("front_user_id");
                            String user_id = object.getString("user_id");
                            String type = object.getString("type");
                            String payment_via = object.getString("payment_via");
                            String p_amount = object.getString("p_amount");
                            String profit_type = object.getString("profit_type");
                            String mode = object.getString("mode");
                            String transactionStatus = object.getString("status");
                            String created_date = object.getString("created_date");
                            String username = object.getString("username");
                            String email = object.getString("email");
                            String phone = object.getString("phone");
                            String name = object.getString("name");
                            String paid_status = object.getString("paid_status");
                            String balance = object.getString("balance");
                            String frontusername = object.getString("frontusername");

                            VCashWalletFragment.walletTransactionsModelArrayList.add(new WalletTransactionsModel(id, front_user_id, user_id, type,
                                    payment_via, p_amount, profit_type, mode, transactionStatus, created_date, username,
                                    email, phone, name, paid_status, balance, frontusername));
                        }

                        Collections.reverse(VCashWalletFragment.walletTransactionsModelArrayList);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String error) {
                Log.d("Error",error+"");
            }

        });
    }

    private void getMCashTransaction(){
        ApiCalls.getMcashTransactions(activity, Prefes.getAccessToken(activity), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                //  Log.d("tmcashtrrr", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                        mCashBalance=dataObject.getString("walletBalance");

                        JSONArray listingArray = dataObject.getJSONArray("listing");
                        for (int i=0; i<listingArray.length(); i++){
                            JSONObject object=listingArray.getJSONObject(i);
                            String id = object.getString("id");
                            String front_user_id = object.getString("front_user_id");
                            String user_id  = object.getString("user_id");
                            String type  = object.getString("type");
                            String payment_via = object.getString("payment_via");
                            String p_amount  = object.getString("p_amount");
                            String profit_type = object.getString("profit_type");
                            String mode  = object.getString("mode");
                            String transactionStatus  = object.getString("status");
                            String created_date  = object.getString("created_date");
                            String username = object.getString("username");
                            String email  = object.getString("email");
                            String phone  = object.getString("phone");
                            String name  = object.getString("name");
                            String paid_status  = object.getString("paid_status");
                            String balance  = object.getString("balance");
                            String frontusername = object.getString("frontusername");

                            MCashWalletFragment.walletTransactionsModelArrayList.add(new WalletTransactionsModel( id,  front_user_id,  user_id,  type,
                                    payment_via, p_amount,  profit_type,  mode,  transactionStatus,  created_date,  username,
                                    email,  phone,  name,  paid_status,  balance, frontusername));
                        }

                        Collections.reverse(MCashWalletFragment.walletTransactionsModelArrayList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                Log.d("Error",error+"");
            }

        });
    }


    public void preLoadDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setCancelable(false);

        View view= LayoutInflater.from(activity).inflate(R.layout.privacy_setting_popup_layout, null);
        builder.setView(view);

        TextView disagreeTV=view.findViewById(R.id.noThanksButtonTV);
        TextView okButtonTV=view.findViewById(R.id.okButtonTV);
        TextView privacyPolicyTV=view.findViewById(R.id.privacyPolicyTV);

        AlertDialog alertDialog=builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();

        editor.putBoolean("popupSP", false).apply();

        privacyPolicyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, AboutUsActivity.class)
                        .putExtra("title", "Privacy Policy")
                        .putExtra("page", "privacy_policy"));
                alertDialog.dismiss();
            }
        });


        okButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow()!=null){
            alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }

    }

}
