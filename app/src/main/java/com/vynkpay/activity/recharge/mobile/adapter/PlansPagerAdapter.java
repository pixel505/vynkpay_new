package com.vynkpay.activity.recharge.mobile.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import java.util.ArrayList;

public class PlansPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    ArrayList<Fragment> tabArrayList;

    public PlansPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<Fragment> tabArrayList) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.tabArrayList = tabArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        fragment = tabArrayList.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}