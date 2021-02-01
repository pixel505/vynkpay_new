package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.history.AllRechargeHistoryActivity;
import com.vynkpay.adapter.CustomPagerAdapter;
import com.vynkpay.adapter.ServicesAdapter;
import com.vynkpay.adapter.WhatsNewAdapter;
import com.vynkpay.custom.NormalBoldTextView;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.OffersModel;
import com.vynkpay.models.ServiceModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;
import com.vynkpay.utils.URLS;
import com.google.android.material.appbar.AppBarLayout;
import com.rd.PageIndicatorView;
import com.vynkpay.BuildConfig;
import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Orientation;
import com.rd.draw.data.RtlMode;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, PlugInControlReceiver.ConnectivityReceiverListener {
    @BindView(R.id.ivUserImage)
    CircleImageView ivUserImage;
    @BindView(R.id.etUserName)
    NormalTextView etUserName;
    @BindView(R.id.etPhoneNumber)
    NormalTextView etPhoneNumber;
    @BindView(R.id.myAccountLayout)
    NormalTextView myAccountLayout;
    @BindView(R.id.myWalletLayout)
    NormalTextView myWalletLayout;
    @BindView(R.id.myShareLayout)
    NormalTextView myShareLayout;
    @BindView(R.id.myAddMoney)
    NormalTextView myAddMoney;
    @BindView(R.id.myRedeemVoucher)
    NormalTextView myRedeemVoucher;
    @BindView(R.id.myNotification)
    NormalTextView myNotification;
    @BindView(R.id.mySupport)
    NormalTextView mySupport;
    @BindView(R.id.myTerms)
    LinearLayout myTerms;
    @BindView(R.id.headerLinear)
    LinearLayout headerLinear;
    @BindView(R.id.myHelp)
    LinearLayout myHelp;
    @BindView(R.id.myAbout)
    LinearLayout myAbout;
    @BindView(R.id.layoutLogOut)
    NormalTextView layoutLogOut;
    @BindView(R.id.onLoginLayout)
    LinearLayout onLoginLayout;
    @BindView(R.id.beforeLoginLayout)
    LinearLayout beforeLoginLayout;
    @BindView(R.id.logoutLayout)
    LinearLayout logoutLayout;
    @BindView(R.id.signUpButton)
    NormalButton signUpButton;
    @BindView(R.id.loginButton)
    NormalButton loginButton;
    @BindView(R.id.myRefundPolicy)
    LinearLayout myRefundPolicy;
    @BindView(R.id.myPrivacyPolicy)
    LinearLayout myPrivacyPolicy;
    @BindView(R.id.myCashBackPolicy)
    LinearLayout myCashBackPolicy;
    @BindView(R.id.hamburgerFrame)
    FrameLayout hamburgerFrame;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.searchFrame)
    FrameLayout searchFrame;
    @BindView(R.id.notificationFrame)
    FrameLayout notificationFrame;
    @BindView(R.id.addMoneyLinear)
    LinearLayout addMoneyLinear;
    @BindView(R.id.accountLinear)
    LinearLayout accountLinear;
    @BindView(R.id.inboxLinear)
    LinearLayout inboxLinear;
    @BindView(R.id.servicesLinear)
    LinearLayout servicesLinear;
    @BindView(R.id.servicesRecyclerView)
    RecyclerView servicesRecyclerView;
    @BindView(R.id.walletLinear)
    LinearLayout walletLinear;
    @BindView(R.id.cashbackLinear)
    LinearLayout cashbackLinear;
    @BindView(R.id.viewpagerTop)
    ViewPager viewpagerTop;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;
    @BindView(R.id.walletBalanceTV)
    NormalBoldTextView walletBalanceTV;
    @BindView(R.id.cashBackRewardTV)
    NormalBoldTextView cashBackRewardTV;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.whatsNewRecyclerView)
    RecyclerView whatsNewRecyclerView;
    @BindView(R.id.homeLinear)
    LinearLayout homeLinear;
    @BindView(R.id.myHistoryLayout)
    LinearLayout myHistoryLayout;
    @BindView(R.id.offerFrame)
    FrameLayout offerFrame;

    ServicesAdapter servicesAdapter;
    WhatsNewAdapter whatsNewAdapter;
    ArrayList<ServiceModel> serviceModelArrayList=new ArrayList<>();
    ArrayList<ServiceModel> whatNewArrayList=new ArrayList<>();
    Dialog dialog;
    String accessToken;
    JSONObject userPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_dashboard_new_rcg);
        ButterKnife.bind(Dashboard.this);
        dev();
    }

    private void dev() {
        dialog = M.showDialog(Dashboard.this, "", false, false);
        hamburgerFrame.setOnClickListener(this);
        searchFrame.setOnClickListener(this);
        notificationFrame.setOnClickListener(this);

        addMoneyLinear.setOnClickListener(this);
        accountLinear.setOnClickListener(this);
        inboxLinear.setOnClickListener(this);
        servicesLinear.setOnClickListener(this);

        walletLinear.setOnClickListener(this);
        cashbackLinear.setOnClickListener(this);
        homeLinear.setOnClickListener(this);
        myHistoryLayout.setOnClickListener(this);

        headerLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mDrawerLayout.closeDrawer(GravityCompat.START);

        serviceModelArrayList.clear();
        serviceModelArrayList.add(new ServiceModel("Mobile Prepaid", R.drawable.mobile_icon));
        serviceModelArrayList.add(new ServiceModel("Mobile Postpaid", R.drawable.mobile_icon));
        serviceModelArrayList.add(new ServiceModel("Data Card", R.drawable.data_card_icon));
        serviceModelArrayList.add(new ServiceModel("DTH", R.drawable.dth_icon));
        serviceModelArrayList.add(new ServiceModel("LandLine", R.drawable.landline_icon));
        serviceModelArrayList.add(new ServiceModel("Broadband", R.drawable.braodband_icon));
        serviceModelArrayList.add(new ServiceModel("Electricity", R.drawable.electricity_icon));
        serviceModelArrayList.add(new ServiceModel("More", R.drawable.gas_icon));
        servicesRecyclerView.setLayoutManager(Functions.layoutManager(Dashboard.this, Functions.GRID, 4));
        servicesAdapter = new ServicesAdapter(Dashboard.this, serviceModelArrayList);
        servicesRecyclerView.setAdapter(servicesAdapter);

        whatNewArrayList.clear();
        whatsNewRecyclerView.setLayoutManager(Functions.layoutManager(Dashboard.this, Functions.HORIZONTAL, 0));
        whatNewArrayList.add(new ServiceModel("Pay Now", R.drawable.gasbuy));
        whatNewArrayList.add(new ServiceModel("Domestic Pay", R.drawable.dmt_icon));
        whatNewArrayList.add(new ServiceModel("Recharge", R.drawable.rechrg));
        whatNewArrayList.add(new ServiceModel("Book Now", R.drawable.train_booking_icon_new));
        whatsNewAdapter=new WhatsNewAdapter(Dashboard.this, whatNewArrayList);
        whatsNewRecyclerView.setAdapter(whatsNewAdapter);

        try {
            userPreference = new JSONObject(Prefes.getUserData(Dashboard.this));
            accessToken = userPreference.getString(ApiParams.access_token);
            Log.i(">>access_token", "onCreate: " + accessToken);
            Log.i(">>access_token", "onCreate: " + userPreference.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setListeners();
        updateUIAccordingToUserStatus();
        getRecentOffers();
        fetchWalletData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.hamburgerFrame:
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.searchFrame:
                M.launchActivity(Dashboard.this, SearchActivity.class);
                break;
            case R.id.notificationFrame:
                if (!Prefes.getUserData(Dashboard.this).equalsIgnoreCase("")) {
                    M.launchActivity(Dashboard.this, NotificationActivity.class);
                } else {
                    M.makeChecks(Dashboard.this, LoginActivity.class);
                }
                break;
            case R.id.addMoneyLinear:
                if (!Prefes.getUserData(Dashboard.this).equalsIgnoreCase("")) {
                    M.launchActivity(Dashboard.this, AddMoneyActivity.class);
                }else {
                    M.makeChecks(Dashboard.this, LoginActivity.class);
                }

                break;
            case R.id.accountLinear:
                if (!Prefes.getUserData(Dashboard.this).equalsIgnoreCase("")) {
                    M.launchActivity(Dashboard.this, AccountActivity.class);
                } else {
                    M.makeChecks(Dashboard.this, LoginActivity.class);
                }
                break;
            case R.id.inboxLinear:
                if (!Prefes.getUserData(Dashboard.this).equalsIgnoreCase("")) {
                    M.launchActivity(Dashboard.this, NotificationActivity.class);
                } else {
                    M.makeChecks(Dashboard.this, LoginActivity.class);
                }
                break;
            case R.id.servicesLinear:
                M.launchActivity(Dashboard.this, MoreActivity.class);
                break;
            case R.id.cashbackLinear:
                M.launchActivity(Dashboard.this, CashbackRewardActivity.class);
                break;
            case R.id.walletLinear:
                M.launchActivity(Dashboard.this, WalletActivity.class);
                break;
            case R.id.homeLinear:
                updateUIAccordingToUserStatus();
                getRecentOffers();
                fetchWalletData();
                break;
            case R.id.myHistoryLayout:
                if (!Prefes.getUserData(Dashboard.this).equalsIgnoreCase("")) {
                    M.launchActivity(Dashboard.this, AllRechargeHistoryActivity.class);
                }else {
                    M.launchActivity(Dashboard.this, LoginActivity.class);
                }

                break;
        }
    }


    private void updateUIAccordingToUserStatus() {
        String name = Prefes.getName(Dashboard.this);

        if (!Prefes.getUserData(Dashboard.this).equalsIgnoreCase("")) {
            if (name!=null && !name.equals("") && !name.equals("full_name"))
                etUserName.setText(name);
            else {
                if (!M.fetchUserTrivialInfo(Dashboard.this, ApiParams.full_name).equals("null")) {
                    etUserName.setText(M.fetchUserTrivialInfo(Dashboard.this, ApiParams.full_name));
                } else
                    etUserName.setText("Username");

            }

            etPhoneNumber.setText(M.fetchUserTrivialInfo(Dashboard.this, ApiParams.email));
            onLoginLayout.setVisibility(View.VISIBLE);
            logoutLayout.setVisibility(View.VISIBLE);
            beforeLoginLayout.setVisibility(View.GONE);
            appBar.setVisibility(View.VISIBLE);

            makeProfileRequest(accessToken);

        } else {
            appBar.setVisibility(View.GONE);
            etUserName.setText("Pay with VynkPay");
            etPhoneNumber.setText("SignIn and start paying");
            onLoginLayout.setVisibility(View.GONE);
            logoutLayout.setVisibility(View.GONE);
            beforeLoginLayout.setVisibility(View.VISIBLE);
        }
    }


    private void makeProfileRequest(final String accessToken) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("userDetails", response+"//");
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data);

                                String fullNameString = jsonObject1.getString(ApiParams.full_name);
                                etUserName.setText(fullNameString);
                                String image = jsonObject1.getString("image");

                                if (!jsonObject1.isNull("image")){
                                    if (!image.isEmpty()){
                                        Picasso.get().load(image).into(ivUserImage);
                                    }
                                }

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
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, accessToken);
                return params;
            }
        };
        MySingleton.getInstance(Dashboard.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setListeners() {

        layoutLogOut.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Prefes.saveName("", Dashboard.this);
                M.makeLogoutRequest(Dashboard.this, accessToken, dialog);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);

                Intent intent = new Intent(Dashboard.this, WebViewActivity.class);
                intent.putExtra("url", "register");
                intent.putExtra("title", "");
                startActivity(intent);

                //M.makeChecks(Dashboard.this, SignUpActivity.class);

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                M.makeChecks(Dashboard.this, LoginActivity.class);
            }
        });
        myAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (!Prefes.getUserData(Dashboard.this).equalsIgnoreCase("")) {
                    M.launchActivity(Dashboard.this, AccountActivity.class);
                } else {
                    M.makeChecks(Dashboard.this, LoginActivity.class);
                }
            }
        });

        myShareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (!Prefes.getUserData(Dashboard.this).equalsIgnoreCase("")) {
                    M.launchActivity(Dashboard.this, ReferAndEarnActivity.class);
                } else {
                    M.makeChecks(Dashboard.this, LoginActivity.class);
                }
            }
        });

        myWalletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (!Prefes.getUserData(Dashboard.this).equalsIgnoreCase("")) {
                    M.launchActivity(Dashboard.this, WalletActivity.class);
                } else {
                    M.makeChecks(Dashboard.this, LoginActivity.class);
                }
            }
        });

        myAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (!Prefes.getUserData(Dashboard.this).equalsIgnoreCase("")) {
                    M.launchActivity(Dashboard.this, AddMoneyActivity.class);
                } else {
                    M.makeChecks(Dashboard.this, LoginActivity.class);
                }
            }
        });

        myRedeemVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Toast.makeText(Dashboard.this, "Coming soon.", Toast.LENGTH_SHORT).show();
            }
        });

        myNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (!Prefes.getUserData(Dashboard.this).equalsIgnoreCase("")) {
                    M.launchActivity(Dashboard.this, NotificationActivity.class);
                } else {
                    M.makeChecks(Dashboard.this, LoginActivity.class);
                }
            }
        });

        myHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
               // M.launchActivity(Dashboard.this, HelpAndSupportActivity.class);
                Intent intent = new Intent(Dashboard.this, WebViewActivity.class);
                intent.putExtra("url", "contact");
                intent.putExtra("title", "");
                startActivity(intent);
            }
        });

        mySupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(Dashboard.this, WebViewActivity.class);
                intent.putExtra("url", "contact");
                intent.putExtra("title", "");
                startActivity(intent);
            }
        });

        myAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(Dashboard.this, WebViewActivity.class);
                intent.putExtra("url", "about");
                intent.putExtra("title", "");
                startActivity(intent);
            }
        });

        myTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 mDrawerLayout.closeDrawer(GravityCompat.START);
                //Toast.makeText(Dashboard.this, "Coming soon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, WebViewActivity.class);
                intent.putExtra("url", "terms");
                intent.putExtra("title", "");
                startActivity(intent);
            }
        });

        myRefundPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 mDrawerLayout.closeDrawer(GravityCompat.START);
                //Toast.makeText(Dashboard.this, "Coming soon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, WebViewActivity.class);
                intent.putExtra("url", "refund");
                intent.putExtra("title", "");
                startActivity(intent);
            }
        });


        myPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                //Toast.makeText(Dashboard.this, "Coming soon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, WebViewActivity.class);
                intent.putExtra("url", "privacy");
                intent.putExtra("title", "");
                startActivity(intent);
            }
        });

        myCashBackPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                //Toast.makeText(Dashboard.this, "Coming soon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, WebViewActivity.class);
                intent.putExtra("url", "cashback");
                intent.putExtra("title", "");
                startActivity(intent);
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUIAccordingToUserStatus();
        fetchWalletData();
        MySingleton.getInstance(Dashboard.this).setConnectivityListener(this);
    }

    RequestQueue requestQueue;
    public void getRecentOffers(){
        String Url = BuildConfig.APP_BASE_URL + URLS.recentOffers.replace(" ","%20");
            if (requestQueue==null) {
                requestQueue = Volley.newRequestQueue(Dashboard.this);
            }

            StringRequest stringRequest=new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("recentOfferResponse", response+"//");
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.getBoolean("success")){
                            offerFrame.setVisibility(View.VISIBLE);
                            handleOperatorResponse(jsonObject);
                        }else {
                            offerFrame.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        offerFrame.setVisibility(View.GONE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    offerFrame.setVisibility(View.GONE);
                }
            });
            requestQueue.add(stringRequest);
    }

    ArrayList<OffersModel> offerList = new ArrayList<>();
    private void handleOperatorResponse(JSONObject receivedJSON) {
        offerList.clear();
        try {
            JSONArray dataArray = receivedJSON.getJSONArray(ApiParams.data);
            for (int l = 0; l < dataArray.length(); l++) {
                JSONObject jsonObject1 = dataArray.getJSONObject(l);
                String id = jsonObject1.getString("id");
                String title = jsonObject1.getString("title");
                String description = jsonObject1.getString("description");
                String image = jsonObject1.getString("image");
                String status = jsonObject1.getString("status");
                String offer_url = jsonObject1.getString("offer_url");
                String created_at = jsonObject1.getString("created_at");
                OffersModel offersModel = new OffersModel();
                offersModel.setId(id);
                offersModel.setTitle(title);
                offersModel.setDescription(description);
                offersModel.setImage(image.replaceAll("////", ""));
                offersModel.setStatus(status);
                offersModel.setOffer_url(offer_url);
                offersModel.setCreated_at(created_at);
                offerList.add(offersModel);
            }

            CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(Dashboard.this, offerList);
            viewpagerTop.setAdapter(customPagerAdapter);
            pageIndicatorView.setViewPager(viewpagerTop);
            pageIndicatorView.setAnimationType(AnimationType.THIN_WORM);
            pageIndicatorView.setOrientation(Orientation.HORIZONTAL);
            pageIndicatorView.setRtlMode(RtlMode.Off);
            pageIndicatorView.setInteractiveAnimation(true);
            pageIndicatorView.setAutoVisibility(false);


        } catch (Exception e) {
            Log.i(">>appDetail", "handleOperatorResponse: " + e.getMessage());
        }

    }

    private void fetchWalletData() {
        try {
            userPreference = new JSONObject(Prefes.getUserData(Dashboard.this));
            accessToken = userPreference.getString(ApiParams.access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dialog.show();
        if (requestQueue==null) {
            requestQueue = Volley.newRequestQueue(Dashboard.this);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.wallet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>walletData", "onResponse: " + jsonObject.toString());
                            JSONObject dataObject=jsonObject.getJSONObject(ApiParams.data);
                            String balance=dataObject.getString("balance");
                            String reward=dataObject.getString("cashback_balance");
                            walletBalanceTV.setText(Functions.CURRENCY_SYMBOL+String.format("%.2f", Double.parseDouble(balance.replace(",",""))));
                            cashBackRewardTV.setText(Functions.CURRENCY_SYMBOL+String.format("%.2f", Double.parseDouble(reward.replace(",",""))));
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
                            M.dialogOk(Dashboard.this, "Please check internet connection", "Error");
                        } else if (error instanceof AuthFailureError) {
                            M.dialogOk(Dashboard.this, "You are not authorized", "Error");
                        } else if (error instanceof ServerError) {
                            M.dialogOk(Dashboard.this, "Server issue", "Error");
                        } else if (error instanceof NetworkError) {
                            M.dialogOk(Dashboard.this, "Please check internet connection", "Error");
                        } else if (error instanceof ParseError) {
                            M.dialogOk(Dashboard.this, "Internal error", "Error");
                        }

                        dialog.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, accessToken);
                return params;
            }
        };
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(Dashboard.this,Dashboard.this::finishAffinity);
        }
    }
}
