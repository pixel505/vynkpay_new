package com.vynkpay.activity.recharge.dth.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class DthPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    ArrayList<Fragment> fragmentArrayList;

    public DthPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<Fragment> fragmentArrayList) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.fragmentArrayList = fragmentArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        fragment = fragmentArrayList.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}