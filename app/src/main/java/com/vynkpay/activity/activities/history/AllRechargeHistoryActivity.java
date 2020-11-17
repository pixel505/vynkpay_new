package com.vynkpay.activity.activities.history;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import com.vynkpay.R;
import com.vynkpay.activity.activities.history.adapter.HistoryPagerAdapter;
import com.vynkpay.custom.NormalTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AllRechargeHistoryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    NormalTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tab;
    @BindView(R.id.frameLayout)
    ViewPager viewPager;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_rcg);

        ButterKnife.bind(AllRechargeHistoryActivity.this);
        setListeners();
        position = getIntent().getIntExtra("pos", 0);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar_title.setText("Recharge/Bill History");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

      //  tab.addTab(tab.newTab().setText("ALL"));
        tab.addTab(tab.newTab().setText("RECHARGE/BILL"));
      //  tab.addTab(tab.newTab().setText("WALLET"));


        HistoryPagerAdapter adapter = new HistoryPagerAdapter(getSupportFragmentManager(), tab.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.setCurrentItem(0);
        //TabLayout.Tab t = tab.getTabAt(position);
       // t.select();
    }

    private void setListeners() {

    }

}
