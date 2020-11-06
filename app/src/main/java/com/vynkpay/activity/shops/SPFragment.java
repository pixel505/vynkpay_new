package com.vynkpay.activity.shops;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vynkpay.R;
import com.vynkpay.databinding.FragmentSPBinding;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SPFragment extends Fragment {


    FragmentSPBinding binding;

    public SPFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_s_p, container, false);
        View view = binding.getRoot();

        return view;
    }
}