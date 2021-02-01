package com.vynkpay.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Picasso;
import com.vynkpay.BuildConfig;
import com.vynkpay.activity.activities.StatementActivity;
import com.vynkpay.activity.activities.WalletNewActivity;
import com.vynkpay.activity.activitiesnew.CustomerWalletActivity;
import com.vynkpay.activity.activitiesnew.TransactionInternationalActivity;
import com.vynkpay.activity.shops.AddBankDetailActivity;
import com.vynkpay.activity.shops.ShopsActivity;
import com.vynkpay.databinding.ActivityHomeBindingImpl;
import com.vynkpay.fragment.FragmentHomeGlobal;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.retrofit.model.NotificationResponse;
import com.vynkpay.retrofit.model.ReferLinkResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.AboutUsActivity;
import com.vynkpay.activity.activities.AccountAccessActivity;
import com.vynkpay.activity.activities.AccountActivity;
import com.vynkpay.activity.activities.KycActivity;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.activity.activities.NotificationActivity;
import com.vynkpay.activity.activities.SupportTicketActivity;
import com.vynkpay.activity.activities.history.AllRechargeHistoryActivity;
import com.vynkpay.fragment.FragmentHome;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.LogoutResponse;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import org.json.JSONException;
import org.json.JSONObject;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, PlugInControlReceiver.ConnectivityReceiverListener {

    static HomeActivity mInstance;
    CircleImageView profileImage;
    TextView textCartItemCount;
    int mCartItemCount = 0;
    Fragment fragment;
    private boolean reloadNeed = true;
    DrawerLayout drawer;
    ActivityHomeBindingImpl binding;
    String referralLink="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mInstance = HomeActivity.this;
        ButterKnife.bind(this);
        if (getIntent().hasExtra("Country")) {
            Log.d("homeactvi", getIntent().getStringExtra("Country") + "///////");
        }

        if (Prefes.getisIndian(HomeActivity.this).equals("")) {

            if (getIntent().getStringExtra("Country").equals("Global")) {
                fragment = new FragmentHomeGlobal();
                switchFragment(fragment, "home");
            } else if (getIntent().getStringExtra("Country").equals("India")) {
                fragment = new FragmentHome();
                switchFragment(fragment, "home");
            }
            Log.d("homeAct",getIntent().getStringExtra("Country")+"//(IF)");
        } else {
            try {
                Log.d("homeAct",getIntent().getStringExtra("Country")+"//(ELSE)");
                if (getIntent().getStringExtra("Country").equals("Global") && Prefes.getisIndian(getApplicationContext()).equalsIgnoreCase("NO")) {
                    fragment = new FragmentHomeGlobal();
                    switchFragment(fragment, "home");
                } else if (getIntent().getStringExtra("Country").equals("Global") && Prefes.getisIndian(getApplicationContext()).equalsIgnoreCase("YES")) {
                    fragment = new FragmentHome();
                    switchFragment(fragment, "home");

                } else if (getIntent().getStringExtra("Country").equals("India") && Prefes.getisIndian(getApplicationContext()).equalsIgnoreCase("YES")) {
                    fragment = new FragmentHome();
                    switchFragment(fragment, "home");

                } else if (getIntent().getStringExtra("Country").equals("India") && Prefes.getisIndian(getApplicationContext()).equalsIgnoreCase("NO")) {
                    fragment = new FragmentHomeGlobal();
                    switchFragment(fragment, "home");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        reloadData();

        binding.sideLayout.loginLinear.setVisibility(View.VISIBLE);
        binding.sideLayout.logoutFrame.setVisibility(View.GONE);
        binding.sideLayout.headerLayout.emailText.setText("Vynkpay Email");
        binding.sideLayout.headerLayout.userNameText.setText("Vynkpay");
        binding.sideLayout.headerLayout.imageView.setImageResource(R.drawable.dummy);

        setNavigationClicks();

        Log.d("getUserType",Prefes.getUserType(HomeActivity.this)+"//");
        /*if (Prefes.getUserType(HomeActivity.this).equalsIgnoreCase("2")){
            Log.d("loginas","c");
        }else {
            Log.d("loginas","a");
        }*/

    }

    public static synchronized HomeActivity getInstance(){
        return mInstance;
    }

    BottomNavigationView bottomNavigationView;

    private void reloadData() {
        View view=findViewById(R.id.appBar);
        View contentview=view.findViewById(R.id.contentMain);

             LinearLayout homeLinear=contentview.findViewById(R.id.hln);
             LinearLayout shopLinear=contentview.findViewById(R.id.shopLinear);
             LinearLayout walletLinear=contentview.findViewById(R.id.walletLinear);
             LinearLayout transactionLinear=contentview.findViewById(R.id.transactionLinear);
             LinearLayout storeLinear=contentview.findViewById(R.id.storeLinear);

             homeLinear.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     if (getIntent().getStringExtra("Country") != null) {

                         if (getIntent().getStringExtra("Country").equals("Global") && Prefes.getisIndian(getApplicationContext()).equalsIgnoreCase("NO")) {
                             fragment = new FragmentHomeGlobal();
                             replaceFragment(fragment, "home");
                         } else if (getIntent().getStringExtra("Country").equals("Global") && Prefes.getisIndian(getApplicationContext()).equalsIgnoreCase("YES")) {
                             fragment = new FragmentHome();
                             replaceFragment(fragment, "home");

                         } else if (getIntent().getStringExtra("Country").equals("India") && Prefes.getisIndian(getApplicationContext()).equalsIgnoreCase("YES")) {
                             fragment = new FragmentHome();
                             replaceFragment(fragment, "home");

                         } else if (getIntent().getStringExtra("Country").equals("India") && Prefes.getisIndian(getApplicationContext()).equalsIgnoreCase("NO")) {
                             fragment = new FragmentHomeGlobal();
                             replaceFragment(fragment, "home");
                         }

                     }


                     if (getIntent().getStringExtra("Country") != null && Prefes.getisIndian(getApplicationContext()).equals("")) {
                         if (getIntent().getStringExtra("Country").equals("Global")) {
                             fragment = new FragmentHomeGlobal();
                             replaceFragment(fragment, "home");
                         } else if (getIntent().getStringExtra("Country").equals("India")) {
                             fragment = new FragmentHome();
                             replaceFragment(fragment, "home");

                         }
                     }
                 }
             });

             shopLinear.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     //RKChanges
                     if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                         startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                     } else {
                         startActivity(new Intent(HomeActivity.this, ShopsActivity.class));
                     }
                     //Toast.makeText(HomeActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
                 }
             });

        walletLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(HomeActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
                if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                } else {
                    if (Prefes.getUserType(HomeActivity.this).equalsIgnoreCase("2")){
                        startActivity(new Intent(HomeActivity.this, CustomerWalletActivity.class));
                    }else {
                        startActivity(new Intent(HomeActivity.this, WalletNewActivity.class));
                    }
                }
            }
        });

        transactionLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                } else {

                    if(Functions.isIndian) {
                        startActivity(new Intent(HomeActivity.this, AllRechargeHistoryActivity.class));
                    } else {
                        startActivity(new Intent(HomeActivity.this, TransactionInternationalActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }

                }
            }
        });

        storeLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainApplication.getApiService().getLinks(Prefes.getAccessToken(HomeActivity.this)).enqueue(new Callback<ReferLinkResponse>() {
                    @Override
                    public void onResponse(Call<ReferLinkResponse> call, Response<ReferLinkResponse> response) {
                        if(response.isSuccessful()){
                            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                    //.setLink(Uri.parse("https://www.pixelsoftwares.com/?referralCode="+referralCode))
                                    .setLink(Uri.parse(response.body().getData().getReferUrl()))
                                    //.setDomainUriPrefix("https://pixelsoftwares.page.link")
                                    .setDomainUriPrefix(response.body().getData().getReferPage())
                                    .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder()
                                            .setTitle("Vynkpay - Apps on Google Play")
                                            .setDescription("Developing an iconic digital-infrastructural entity that creates an altogether new definition of e-commerce industry")
                                            .build())
                                    // Open links with this app on Android
                                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                                    .buildDynamicLink();

                            Uri dynamicLinkUri = dynamicLink.getUri();

                            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                    .setLongLink(dynamicLinkUri)
                                    .buildShortDynamicLink()
                                    .addOnCompleteListener(HomeActivity.this, new OnCompleteListener<ShortDynamicLink>() {
                                        @Override
                                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                            if (task.isSuccessful()) {
                                                // Short link created
                                                if (task.getResult()!=null){
                                                    Uri shortLink = task.getResult().getShortLink();
                                                    referralLink = shortLink.toString();
                                                    Log.e("neww",""+referralLink);
                                                    Intent sendIntent = new Intent();
                                                    sendIntent.setAction(Intent.ACTION_SEND);
                                                    sendIntent.putExtra(Intent.EXTRA_TEXT, referralLink);
                                                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "VYNKPAY Refer To Friend");
                                                    sendIntent.setType("text/plain");
                                                    startActivity(Intent.createChooser(sendIntent, "VYNKPAY Refer To Friend"));
                                                }

                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(Call<ReferLinkResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage()!=null ? t.getMessage() : "Error");
                    }
                });
            }
        });



    }

    public void sideHeaderCall(String imageURL) {
        if (Prefes.getUserID(HomeActivity.this).equals("")) {

            binding.sideLayout.loginLinear.setVisibility(View.VISIBLE);
            binding.sideLayout.logoutFrame.setVisibility(View.GONE);
            binding.sideLayout.headerLayout.emailText.setText("Vynkpay Email");
            binding.sideLayout.headerLayout.userNameText.setText("Vynkpay");
            binding.sideLayout.headerLayout.imageView.setImageResource(R.drawable.dummy);

        } else {
            binding.sideLayout.loginLinear.setVisibility(View.GONE);
            binding.sideLayout.logoutFrame.setVisibility(View.VISIBLE);

            if (imageURL == null) {
                binding.sideLayout.headerLayout.imageView.setImageResource(R.drawable.dummy);
            } else if (imageURL.isEmpty()) {
                binding.sideLayout.headerLayout.imageView.setImageResource(R.drawable.dummy);
            } else {
                Picasso.get().load(BuildConfig.BASE_URL + imageURL).placeholder(R.drawable.dummy).into(binding.sideLayout.headerLayout.imageView);
            }

            binding.sideLayout.headerLayout.userNameText.setText(Prefes.getName(HomeActivity.this));
            binding.sideLayout.headerLayout.emailText.setText(Prefes.getEmail(HomeActivity.this));
        }
    }

    public void setNavigationClicks() {
        binding.sideLayout.legalitiesLinear.setOnClickListener(this);
        binding.sideLayout.shareLinear.setOnClickListener(this);
        binding.sideLayout.shareLinear.setVisibility(View.GONE);
        binding.sideLayout.careerPlanLinear.setOnClickListener(this);
        binding.sideLayout.homeLinear.setOnClickListener(this);
        binding.sideLayout.loginLinear.setOnClickListener(this);
        binding.sideLayout.shopLinear.setOnClickListener(this);
        binding.sideLayout.notificationLinear.setOnClickListener(this);
        binding.sideLayout.notificationLinear.setVisibility(View.GONE);
        binding.sideLayout.transactionLinear.setOnClickListener(this);
        if (Functions.isIndian) {
            binding.sideLayout.transactionLinear.setVisibility(View.GONE);
        }
        binding.sideLayout.accessLinear.setOnClickListener(this);
        binding.sideLayout.addBankDetailLinear.setOnClickListener(this);
        binding.sideLayout.profileLinear.setOnClickListener(this);
        binding.sideLayout.kycLinear.setOnClickListener(this);
        binding.sideLayout.supportLinear.setOnClickListener(this);
        binding.sideLayout.aboutLinear.setOnClickListener(this);
        binding.sideLayout.logoutFrame.setOnClickListener(this);

        if (Prefes.getUserType(HomeActivity.this).equalsIgnoreCase("2")){
            binding.sideLayout.legalitiesLinear.setVisibility(View.GONE);
            binding.sideLayout.careerPlanLinear.setVisibility(View.GONE);
            binding.sideLayout.kycLinear.setVisibility(View.GONE);
            binding.sideLayout.accessLinear.setVisibility(View.GONE);
            binding.sideLayout.addBankDetailLinear.setVisibility(View.VISIBLE);

        } else {

            binding.sideLayout.legalitiesLinear.setVisibility(View.VISIBLE);
            binding.sideLayout.careerPlanLinear.setVisibility(View.VISIBLE);
            binding.sideLayout.kycLinear.setVisibility(View.VISIBLE);
            binding.sideLayout.accessLinear.setVisibility(View.VISIBLE);
            binding.sideLayout.addBankDetailLinear.setVisibility(View.GONE);

        }


        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                sideHeaderCall(imageURL);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }

        });

    }

    String fragmentTag = "home";

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof FragmentHome || fragment instanceof FragmentHomeGlobal) {
            if (bottomNavigationView != null) {
                fragmentTag = fragment.getTag();
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
            }
        }
    }

    private void switchFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_container, fragment, tag);
        transaction.commit();
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, tag);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.badge_notification, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = menuItem.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);



       // setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart: {
                // Do something

                if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    String imageURL = "";

    @Override
    protected void onResume() {
        super.onResume();
//        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        MySingleton.getInstance(HomeActivity.this).setConnectivityListener(this);
        if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
            Log.d("calledd","calledw");
            binding.sideLayout.loginLinear.setVisibility(View.VISIBLE);
            binding.sideLayout.logoutFrame.setVisibility(View.GONE);
            binding.sideLayout.headerLayout.emailText.setText("Vynkpay Email");
            binding.sideLayout.headerLayout.userNameText.setText("Vynkpay");
            binding.sideLayout.headerLayout.imageView.setImageResource(R.drawable.dummy);

        } else {
            Log.d("calledd","calledlogin");

            ApiCalls.getUserDetails(HomeActivity.this, Prefes.getAccessToken(HomeActivity.this), new VolleyResponse() {
                @Override
                public void onResult(String result, String status, String message) {
                    Log.d("userDwetailssssa", result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString("success").equals("true")) {
                            JSONObject data = jsonObject.getJSONObject("data");

                            Functions.CURRENCY_SYMBOL = data.getString("currency_symbol");
                            Log.e("curr",""+Functions.CURRENCY_SYMBOL);
                            Log.e("curr",""+data.getString("is_indian"));

                            if (data.has("username")){
                                String username = data.getString("username");
                                //Prefes.saveUserName(username, HomeActivity.this);
                            }

                            if (data.getString("is_indian").equalsIgnoreCase("YES")) {
                                //indian
                                Functions.isIndian = true;
                                binding.sideLayout.transactionLinear.setVisibility(View.GONE);

                            } else {
                                //foreigner
                                Functions.isIndian = false;
                                binding.sideLayout.transactionLinear.setVisibility(View.GONE);

                            }
                            imageURL = data.getString("image");
                            sideHeaderCall(imageURL);
                        } else {
                            Prefes.clear(HomeActivity.this);
                            binding.sideLayout.loginLinear.setVisibility(View.VISIBLE);
                            binding.sideLayout.logoutFrame.setVisibility(View.GONE);
                            binding.sideLayout.headerLayout.emailText.setText("Vynkpay Email");
                            binding.sideLayout.headerLayout.userNameText.setText("Vynkpay");
                            binding.sideLayout.headerLayout.imageView.setImageResource(R.drawable.dummy);
                        }

                    } catch ( Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String error) {
                    Log.d("Error",error+"");
                }
            });

            MainApplication.getApiService().getNotifications(Prefes.getAccessToken(HomeActivity.this)).enqueue(new Callback<NotificationResponse>() {
                @Override
                public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isSuccess()) {
                            if (response.body().getData().getUnread_count().equals("0")) {
                                if (textCartItemCount!=null) {
                                    textCartItemCount.setVisibility(View.GONE);
                                }
                            }

                            else {
                                if (textCartItemCount!=null) {
                                    textCartItemCount.setVisibility(View.VISIBLE);
                                    textCartItemCount.setText(response.body().getData().getUnread_count());
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<NotificationResponse> call, Throwable t) {
                    Log.d("Errorrr",t.getMessage() !=null ? t.getMessage() : "Error");
                }

            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == binding.sideLayout.homeLinear) {
            drawer.closeDrawer(GravityCompat.START);
            if (getIntent().getStringExtra("Country") != null) {
                if (getIntent().getStringExtra("Country").equals("Global") && Prefes.getisIndian(getApplicationContext()).equals("NO")) {
                    fragment = new FragmentHomeGlobal();
                    replaceFragment(fragment, "home");
                } else if (getIntent().getStringExtra("Country").equals("Global") && Prefes.getisIndian(getApplicationContext()).equals("YES")) {
                    fragment = new FragmentHome();
                    replaceFragment(fragment, "home");

                } else if (getIntent().getStringExtra("Country").equals("India") && Prefes.getisIndian(getApplicationContext()).equals("YES")) {
                    fragment = new FragmentHome();
                    replaceFragment(fragment, "home");

                } else if (getIntent().getStringExtra("Country").equals("India") && Prefes.getisIndian(getApplicationContext()).equals("NO")) {
                    fragment = new FragmentHomeGlobal();
                    replaceFragment(fragment, "home");
                }
            }

            if (getIntent().getStringExtra("Country") != null && Prefes.getisIndian(getApplicationContext()).equals("")) {
                if (getIntent().getStringExtra("Country").equals("Global")) {
                    fragment = new FragmentHomeGlobal();
                    replaceFragment(fragment, "home");
                } else if (getIntent().getStringExtra("Country").equals("India")) {
                    fragment = new FragmentHome();
                    replaceFragment(fragment, "home");

                }
            }


        } else if (v == binding.sideLayout.legalitiesLinear) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(HomeActivity.this, AboutUsActivity.class)
                    .putExtra("title", "Legality")
                    .putExtra("page", "legalities"));

        } else if (v == binding.sideLayout.notificationLinear) {
            drawer.closeDrawer(GravityCompat.START);
            if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
            }

        } else if (v == binding.sideLayout.transactionLinear) {
            drawer.closeDrawer(GravityCompat.START);
            if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(HomeActivity.this, AllRechargeHistoryActivity.class));
            }
        } else if (v == binding.sideLayout.kycLinear) {
            drawer.closeDrawer(GravityCompat.START);
            if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(HomeActivity.this, KycActivity.class));
            }

        } else if (v == binding.sideLayout.shareLinear) {
            drawer.closeDrawer(GravityCompat.START);
            if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                //startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            } else {
                MainApplication.getApiService().getLinks(Prefes.getAccessToken(HomeActivity.this)).enqueue(new Callback<ReferLinkResponse>() {
                    @Override
                    public void onResponse(Call<ReferLinkResponse> call, Response<ReferLinkResponse> response) {
                        if(response.isSuccessful()){
                            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                    //.setLink(Uri.parse("https://www.pixelsoftwares.com/?referralCode="+referralCode))
                                    .setLink(Uri.parse(response.body().getData().getReferUrl()))
                                    //.setDomainUriPrefix("https://pixelsoftwares.page.link")
                                    .setDomainUriPrefix(response.body().getData().getReferPage())
                                    .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder()
                                            .setTitle("Vynkpay - Apps on Google Play")
                                            .setDescription("Developing an iconic digital-infrastructural entity that creates an altogether new definition of e-commerce industry")
                                            .build())
                                    // Open links with this app on Android
                                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                                    .buildDynamicLink();

                            Uri dynamicLinkUri = dynamicLink.getUri();

                            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                    .setLongLink(dynamicLinkUri)
                                    .buildShortDynamicLink()
                                    .addOnCompleteListener(HomeActivity.this, new OnCompleteListener<ShortDynamicLink>() {
                                        @Override
                                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                            if (task.isSuccessful()) {
                                                // Short link created
                                                if (task.getResult()!=null){
                                                    Uri shortLink = task.getResult().getShortLink();
                                                    referralLink = shortLink.toString();
                                                     Log.e("neww",""+referralLink);
                                                    Intent sendIntent = new Intent();
                                                    sendIntent.setAction(Intent.ACTION_SEND);
                                                    sendIntent.putExtra(Intent.EXTRA_TEXT, referralLink);
                                                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "VYNKPAY Refer To Friend");
                                                    sendIntent.setType("text/plain");
                                                    startActivity(Intent.createChooser(sendIntent, "VYNKPAY Refer To Friend"));
                                                }

                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(Call<ReferLinkResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage()!=null ? t.getMessage() : "Error");
                    }

                });
            }

           /* try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Vynkpay");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                e.printStackTrace();
            }*/

        } else if (v == binding.sideLayout.careerPlanLinear) {
            drawer.closeDrawer(GravityCompat.START);

            if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            } else {
                if (!Prefes.getPDFFile(HomeActivity.this).equals("")) {
                    startActivity(new Intent(HomeActivity.this, PdfViewerActivity.class));
                } else {
                    Toast.makeText(this, "Career plans are not available", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (v == binding.sideLayout.profileLinear) {
            drawer.closeDrawer(GravityCompat.START);
            if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(HomeActivity.this, AccountActivity.class));
            }

        } else if (v == binding.sideLayout.accessLinear) {
            drawer.closeDrawer(GravityCompat.START);
            if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(HomeActivity.this, AccountAccessActivity.class));
            }
        } else if (v == binding.sideLayout.addBankDetailLinear) {
            drawer.closeDrawer(GravityCompat.START);
            if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(HomeActivity.this, AddBankDetailActivity.class));
            }
        } else if (v == binding.sideLayout.supportLinear) {
            drawer.closeDrawer(GravityCompat.START);
            if (Prefes.getAccessToken(HomeActivity.this).equals("")) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(HomeActivity.this, SupportTicketActivity.class));
            }

        } else if (v == binding.sideLayout.logoutFrame) {

            logoutDialog();

        } else if (v == binding.sideLayout.loginLinear) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        } else if (v == binding.sideLayout.aboutLinear) {
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(HomeActivity.this, AboutUsActivity.class)
                    .putExtra("title", "About us")
                    .putExtra("page", "about"));
        } else {
            drawer.closeDrawer(GravityCompat.START);
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setCancelable(false);
        View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.app_update_layout_rcg, null);
        builder.setView(view);
        TextView titleTV = view.findViewById(R.id.titleTV);
        TextView messageTV = view.findViewById(R.id.messageTV);
        TextView noThanksButtonTV = view.findViewById(R.id.noThanksButtonTV);
        TextView updateButtonTV = view.findViewById(R.id.updateButtonTV);
        titleTV.setText("Logout");
        messageTV.setText("Are you sure you want to logout?");
        noThanksButtonTV.setText("No");
        noThanksButtonTV.setVisibility(View.VISIBLE);
        updateButtonTV.setText("Yes");

        AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();

        noThanksButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        updateButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                drawer.closeDrawer(GravityCompat.START);
                MainApplication.getApiService().logoutMethod(Prefes.getAccessToken(HomeActivity.this)).enqueue(new Callback<LogoutResponse>() {

                    @Override
                    public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            Prefes.clear(HomeActivity.this);
                            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                            finishAffinity();
                            //Old
                        }
                    }

                    @Override
                    public void onFailure(Call<LogoutResponse> call, Throwable t) {
                        Toast.makeText(HomeActivity.this, t.getMessage()!=null ? t.getMessage() : "Error", Toast.LENGTH_SHORT).show();
                    }

                });

            }

        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(HomeActivity.this,HomeActivity.this::finishAffinity);
        }
    }
}