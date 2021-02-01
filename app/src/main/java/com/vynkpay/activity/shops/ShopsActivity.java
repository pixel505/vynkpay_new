package com.vynkpay.activity.shops;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import com.vynkpay.R;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.databinding.ActivityShopsBinding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import java.util.ArrayList;
import java.util.List;

public class ShopsActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityShopsBinding binding;
    public static ImageView ivFilter;
    public static NormalEditText searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_shops);
        init();
    }

    void init(){
        ivFilter = binding.ivFilter;
        searchView = binding.searchView;
        binding.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopsActivity.this.finish();
            }
        });
        binding.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);

       /* binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.filter(newText);
                return false;
            }
        });

        binding.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (!binding.searchView.isFocused()) {
                    //dialog.dismiss();
                }
                return false;
            }
        });*/
       // binding.tabLayout.setupWithViewPager(binding.viewPager);
        //setupViewPager(binding.viewPager);
        try {
            //binding.tabLayout.getTabAt(0).setIcon(R.drawable.shop2x);
            //binding.tabLayout.getTabAt(1).setIcon(R.drawable.spp2x);
            //binding.tabLayout.getTabAt(2).setIcon(R.drawable.offers2x);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,new ShopFragment()).commitAllowingStateLoss();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new ShopFragment(), "SHOP");
        adapter.addFragment(new SPFragment(), "SP");
        adapter.addFragment(new OffersFragment(), "OFFER");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(ShopsActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(ShopsActivity.this,ShopsActivity.this::finishAffinity);
        }
    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



}