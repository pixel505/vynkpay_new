package com.vynkpay.activity.activities.history.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.vynkpay.activity.activities.history.fragment.AllHistoryFragment;
import com.vynkpay.activity.activities.history.fragment.BankTransferHistoryFragment;
import com.vynkpay.activity.activities.history.fragment.RechargeHistoryFragment;
import com.vynkpay.activity.activities.history.fragment.WalletHistoryFragment;

public class HistoryPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public HistoryPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new RechargeHistoryFragment();
            /*case 1:
                return new TravelHistoryFragment();*/
           /* case 1:
                return new RechargeHistoryFragment();
            case 2:
                return new WalletHistoryFragment();*/
           /* case 3:
                return new BankTransferHistoryFragment();*/
            /*case 4:
                return new ThemeParkHistoryFragment();
            case 5:
                return new GiftCardHistory();*/
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}