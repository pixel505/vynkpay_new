package com.vynkpay.fragment;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.adapter.ImageAdapter;
import com.vynkpay.custom.NormalBoldTextView;
import com.vynkpay.custom.NormalLightTextView;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.BannerModel;
import com.vynkpay.models.EcashModelClass;
import com.vynkpay.models.PDFFileModel;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetNonWalletResponse;
import com.vynkpay.utils.Functions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;

public class FragmentHomeGlobal extends Fragment {
    @BindView(R.id.bonusWalletText)
    TextView bonusWalletText;
    @BindView(R.id.vCashWalletText)
    TextView vCashWalletText;
    @BindView(R.id.mCashWalletText)
    TextView mCashWalletText;
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
    @BindView(R.id.globalImage)
    CircleImageView globalImage;
    @BindView(R.id.globalUserId)
    NormalTextView globalUserId;
    @BindView(R.id.globalUserName)
    NormalBoldTextView globalUserName;
    @BindView(R.id.globalEmail)
    NormalLightTextView globalEmail;

    @BindView(R.id.tvBCash)
    NormalTextView tvBCash;

    @BindView(R.id.crdBouns)
    CardView crdBouns;

    String bonusBalance,vCashBalance,mCashBalance;
    Activity activity;
    String imageURL="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_home_global, container, false);
        ButterKnife.bind(this, view);

        getBonusTransaction();
        getVCashTransaction();
        getMCashTransaction();
        fetchWalletData();


        if (Prefes.getAccessToken(activity).equals("")) {
            globalUserId.setText("UserId :");
            globalUserName.setText("Username");
            globalEmail.setText("VynkPay Email");
            globalImage.setImageResource(R.drawable.dummy);

        } else {
                Log.e("name",""+Prefes.getName(activity));
                Log.e("name",""+Prefes.getUserName(activity));


             globalUserId.setText("Username :"+Prefes.getUserName(activity));
             globalEmail.setText(Prefes.getEmail(activity));
            globalUserName.setText(Prefes.getName(activity));
             globalImage.setImageResource(R.drawable.dummy);

            if(Prefes.getImage(activity).isEmpty()){
                globalImage.setImageResource(R.drawable.dummy);
            }

            else{
                Picasso.get().load(BuildConfig.BASE_URL + Prefes.getImage(activity)).into(globalImage);
                String imagepath = BuildConfig.BASE_URL + Prefes.getImage(activity);
                Log.e("imageeeeeeeeeeeeeeee", "" + imagepath);
            }

        }

        if (Prefes.getUserType(activity).equalsIgnoreCase("2")){
            //crdBouns.setVisibility(View.GONE);
            tvBCash.setText("Cashback");
        }else {
            //crdBouns.setVisibility(View.VISIBLE);
            tvBCash.setText("Bonus Wallet");
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
                    startActivity(new Intent(activity,MCashWalletFragment.class).putExtra("balancWalletM",vCashBalance));
                }
            }
        });
        vCashLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefes.getAccessToken(activity).equals("")) {
                    startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    startActivity(new Intent(activity,VCashWalletFragment.class).putExtra("balancWalletV",mCashBalance));
                }
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
        MainApplication.getApiService().noindianWallet(Prefes.getAccessToken(activity)).enqueue(new Callback<GetNonWalletResponse>() {
            @Override
            public void onResponse(Call<GetNonWalletResponse> call, retrofit2.Response<GetNonWalletResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        bonusWalletText.setText(Functions.CURRENCY_SYMBOL + response.body().getData().getBonusWallet());
                        vCashWalletText.setText(Functions.CURRENCY_SYMBOL + response.body().getData().getMcashWallet());
                        Prefes.saveCash(response.body().getData().getMcashWallet(),activity);
                        mCashWalletText.setText(Functions.CURRENCY_SYMBOL + response.body().getData().getVCashWallet());
                        mCashBalance=response.body().getData().getVCashWallet();
                        vCashBalance=response.body().getData().getMcashWallet();
                        bonusBalance=response.body().getData().getBonusWallet();

                    }
                }else {
                }
            }
            @Override
            public void onFailure(Call<GetNonWalletResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Prefes.getAccessToken(activity).equals("")) {
            globalUserId.setText("UserId :");
            globalUserName.setText("Username");
            globalEmail.setText("VynkPay Email");
            globalImage.setImageResource(R.drawable.dummy);

        } else {
            globalUserId.setText("Username :"+Prefes.getUserName(activity));
            globalEmail.setText(Prefes.getEmail(activity));
            globalUserName.setText(Prefes.getName(activity));
            globalImage.setImageResource(R.drawable.dummy);

            if(Prefes.getImage(activity).isEmpty()){
                globalImage.setImageResource(R.drawable.dummy);
            }

            else{
                Picasso.get().load(BuildConfig.BASE_URL + Prefes.getImage(activity)).into(globalImage);
                String imagepath = BuildConfig.BASE_URL + Prefes.getImage(activity);
                Log.e("imageeeeeeeeeeeeeeee", "" + imagepath);
            }

        }
    }

    ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();

    private void getBanners() {
        bannerModelArrayList.clear();
        ApiCalls.getBanners(activity, Prefes.getAccessToken(activity),new VolleyResponse() {
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
                Log.d("Error",error+"");
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
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String error) {
                Log.d("Erroror",error+"");
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


}
