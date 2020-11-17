package com.vynkpay.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class WalletTabAdapter extends FragmentPagerAdapter {

    public WalletTabAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        /*if (position==0){
            fragment=new BonusWalletFragment();
        }else if (position==1){
            fragment=new MCashWalletFragment();
        }else if (position==2){
            fragment=new ECashWalletFragment();
        }else if (position==3){
            fragment=new VCashWalletFragment();
        }*/
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
