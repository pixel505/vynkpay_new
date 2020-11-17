package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.events.UpDateUIEvent;
import com.vynkpay.fragment.HomeFragment;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.BottomNavigationViewHelper;
import com.vynkpay.utils.M;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardActivity extends AppCompatActivity {
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
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
    @BindView(R.id.myAddMoney)
    NormalTextView myAddMoney;
    @BindView(R.id.myRedeemVoucher)
    NormalTextView myRedeemVoucher;
    @BindView(R.id.myNotification)
    NormalTextView myNotification;
    @BindView(R.id.mySupport)
    NormalTextView mySupport;
    @BindView(R.id.myTerms)
    NormalTextView myTerms;
    @BindView(R.id.headerLinear)
    LinearLayout headerLinear;
    @BindView(R.id.myHelp)
    NormalTextView myHelp;
    @BindView(R.id.myAbout)
    NormalTextView myAbout;
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
    NormalTextView myRefundPolicy;
    @BindView(R.id.myCashBackPolicy)
    NormalTextView myCashBackPolicy;
    androidx.appcompat.app.ActionBarDrawerToggle mDrawerToggle;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_rcg);

        ButterKnife.bind(DashBoardActivity.this);
        EventBus.getDefault().register(this);
        //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
        dialog = M.showDialog(DashBoardActivity.this, "", false, false);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        setListeners();
        setSupportActionBar(toolbar);
        updateUIAccordingToUserStatus();

        toolbarTitle.setText("VynkPay");
        toolbar.setNavigationIcon(R.drawable.dash_board);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle = new androidx.appcompat.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.app_name, R.string.app_name) {
        };

        headerLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.syncState();
        mDrawerLayout.closeDrawer(GravityCompat.START);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

        inflateFragment(new HomeFragment());
    }


    private void updateUIAccordingToUserStatus() {
        String name = Prefes.getName(DashBoardActivity.this);

        if (!Prefes.getUserData(DashBoardActivity.this).equalsIgnoreCase("")) {
            if (name!=null && !name.equals("") && !name.equals("full_name"))
                etUserName.setText(name);
            else {
                if (!M.fetchUserTrivialInfo(DashBoardActivity.this, ApiParams.full_name).equals("null")) {
                    etUserName.setText(M.fetchUserTrivialInfo(DashBoardActivity.this, ApiParams.full_name));
                } else
                    etUserName.setText("Username");
            }

            etPhoneNumber.setText(M.fetchUserTrivialInfo(DashBoardActivity.this, ApiParams.email));
            onLoginLayout.setVisibility(View.VISIBLE);
            logoutLayout.setVisibility(View.VISIBLE);
            beforeLoginLayout.setVisibility(View.GONE);

        } else {
            etUserName.setText("Pay with VynkPay");
            etPhoneNumber.setText("SignIn and start paying");
            onLoginLayout.setVisibility(View.GONE);
            logoutLayout.setVisibility(View.GONE);
            beforeLoginLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setListeners() {
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_menu_home:
                        toolbarTitle.setText(getString(R.string.app_name));
                        return true;
                    case R.id.ic_menu_offer:
                        M.launchActivity(DashBoardActivity.this, OfferActivity.class);
                        return true;
                    case R.id.ic_menu_account:
                        if (!Prefes.getUserData(DashBoardActivity.this).equalsIgnoreCase("")) {
                            M.launchActivity(DashBoardActivity.this, AccountActivity.class);
                            } else {
                            M.makeChecks(DashBoardActivity.this, LoginActivity.class);
                        }
                        return true;
                    case R.id.ic_menu_more:
                        M.launchActivity(DashBoardActivity.this, MoreActivity.class);
                        return true;
                }
                return false;
            }
        });

        layoutLogOut.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Prefes.saveName("", DashBoardActivity.this);
                M.makeLogoutRequest(DashBoardActivity.this, Prefes.getAccessToken(DashBoardActivity.this), dialog);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.makeChecks(DashBoardActivity.this, SignUpActivity.class);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.makeChecks(DashBoardActivity.this, LoginActivity.class);
            }
        });
        myAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.launchActivity(DashBoardActivity.this, AccountActivity.class);

                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        myWalletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.launchActivity(DashBoardActivity.this, WalletActivity.class);
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        myAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                M.launchActivity(DashBoardActivity.this, AddMoneyActivity.class);
            }
        });

        myRedeemVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        myNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                M.launchActivity(DashBoardActivity.this, NotificationActivity.class);
            }
        });
        myHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                M.launchActivity(DashBoardActivity.this, HelpAndSupportActivity.class);
            }
        });
        mySupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        myAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        myTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(DashBoardActivity.this, WebViewActivity.class);
                intent.putExtra("url", "terms_conditions");
                intent.putExtra("title", "Terms & Conditions");
                startActivity(intent);
            }
        });

        myRefundPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(DashBoardActivity.this, WebViewActivity.class);
                intent.putExtra("url", "refund_policy");
                intent.putExtra("title", "Refund Policy");
                startActivity(intent);
            }
        });

        myCashBackPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(DashBoardActivity.this, WebViewActivity.class);
                intent.putExtra("url", "cashback_policy");
                intent.putExtra("title", "CashBack & Policy");
                startActivity(intent);
            }
        });
    }

    String mSuccess, mMessage;

    private void inflateFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notification_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_menu_notification:
                startActivity(new Intent(DashBoardActivity.this, NotificationActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.ic_menu_home);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(UpDateUIEvent event) {
        if (event != null) {
            if (event.isUpdated()) {
                updateUIAccordingToUserStatus();
            }
        }
    }
}
