package com.vynkpay.onboard;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.vynkpay.R;
import com.vynkpay.activity.activities.Dashboard;
import com.vynkpay.adapter.ViewPagerAdapter;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.M;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Orientation;
import com.rd.draw.data.RtlMode;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;
//    private int[] imagesArray = {R.drawable.a, R.drawable.b, R.drawable.c};
    ViewPagerAdapter viewPagerAdapter;
    int count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_onboard_rcg);
        ButterKnife.bind(WelcomeActivity.this);
        count = Prefes.getAppFirstTime(WelcomeActivity.this);
        count++;
        Prefes.setAppFirstTime(count, WelcomeActivity.this);
        viewPagerAdapter = new ViewPagerAdapter(WelcomeActivity.this, null);
        viewPager.setAdapter(viewPagerAdapter);
        pageIndicatorView.setViewPager(viewPager);
        pageIndicatorView.setAnimationType(AnimationType.THIN_WORM);
        pageIndicatorView.setOrientation(Orientation.HORIZONTAL);
        pageIndicatorView.setRtlMode(RtlMode.Off);
        pageIndicatorView.setInteractiveAnimation(true);
        pageIndicatorView.setAutoVisibility(false);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                if (position == (imagesArray.length - 1)) {
//                    exitSplash();
//                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private Handler handler;
    private Runnable runnable;

    public void exitSplash() {
        M.launchActivityWithFinish(WelcomeActivity.this, Dashboard.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(WelcomeActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(WelcomeActivity.this,WelcomeActivity.this::finishAffinity);
        }
    }

}
