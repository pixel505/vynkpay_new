package com.vynkpay.activity.activities.history.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TravelHistoryFragment extends Fragment {
    View view;
    @BindView(R.id.sortBy)
    NormalTextView sortBy;
    @BindView(R.id.noLayoutData)
    LinearLayout noLayoutData;
    @BindView(R.id.noDataText)
    NormalTextView noDataText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plans_rcg, container, false);
        ButterKnife.bind(TravelHistoryFragment.this, view);
        sortBy.setVisibility(View.GONE);
        noLayoutData.setVisibility(View.VISIBLE);
        noDataText.setText("No Travel history found");
        return view;
    }
}
