package com.vynkpay.activity.recharge.dth.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vynkpay.BuildConfig;
import com.vynkpay.activity.recharge.dth.fragment.AnnualFragment;
import com.vynkpay.activity.recharge.dth.fragment.MonthlyFragment;
import com.vynkpay.activity.recharge.dth.fragment.SixMonthlyFragment;
import com.vynkpay.activity.recharge.dth.fragment.ThreeMonthlyFragment;
import com.vynkpay.utils.URLS;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.recharge.dth.adapter.DthPagerAdapter;
import com.vynkpay.activity.recharge.mobile.adapter.PlansPagerAdapter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeeDthPlansActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    NormalTextView toolbar_title;
    @BindView(R.id.tabs)
    TabLayout tab;
    @BindView(R.id.frameLayout)
    ViewPager viewPager;
    String operatorName;
    PlansPagerAdapter adapter;
    String type;

    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;
    @BindView(R.id.progress)
    ProgressBar progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dth_plan_rcg);
        ButterKnife.bind(SeeDthPlansActivity.this);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        operatorName = getIntent().getStringExtra(ApiParams.operator);
        Log.i(">>data", "onCreate: " + operatorName);
        type = getIntent().getStringExtra("type");
        toolbar_title.setText(operatorName);
        Prefes.saveOperator(operatorName, SeeDthPlansActivity.this);
        Prefes.saveCircle("", SeeDthPlansActivity.this);
        Prefes.saveType(type, SeeDthPlansActivity.this);


        monthlyPackCall();


        Prefes.savePlan("Monthly", SeeDthPlansActivity.this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        Prefes.savePlan("Monthly", SeeDthPlansActivity.this);
                        break;
                    case 1:
                        Prefes.savePlan("3 Month", SeeDthPlansActivity.this);
                        break;
                    case 2:
                        Prefes.savePlan("6 Month", SeeDthPlansActivity.this);
                        break;
                    case 3:
                        Prefes.savePlan("Annual", SeeDthPlansActivity.this);
                        break;
                }
                Prefes.savePlan(tab.getTabAt(position).getText().toString(), SeeDthPlansActivity.this);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Prefes.savePlan(tab.getText().toString(), SeeDthPlansActivity.this);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
    private void monthlyPackCall() {
        fragmentArrayList.clear();
        tab.removeAllTabs();
        mainLinear.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.dth_plans + "?operator=" + Prefes.getOperator(SeeDthPlansActivity.this).replaceAll(" ", "%20") +
                "&recharge_type=" + "Monthly Pack" +
                "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String mSuccess = jsonObject.optString(ApiParams.success);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);
                                if (!(j.has("data") && j.get("data") instanceof JSONArray)) {

                                } else {
                                    JSONArray jsonArray = j.getJSONArray(ApiParams.data);
                                    if (jsonArray.length()>0){
                                        tab.addTab(tab.newTab().setText("Monthly Pack"));
                                        fragmentArrayList.add(new MonthlyFragment());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        threeMonthPackCall();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        threeMonthPackCall();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SeeDthPlansActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void threeMonthPackCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.dth_plans + "?operator=" + Prefes.getOperator(SeeDthPlansActivity.this)+ "&recharge_type=" + "3 Month Packs" + "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String mSuccess = jsonObject.optString(ApiParams.success);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);
                                if (!(j.has("data") && j.get("data") instanceof JSONArray)) {

                                } else {
                                    JSONArray jsonArray = j.getJSONArray(ApiParams.data);
                                    if (jsonArray.length()>0){
                                        tab.addTab(tab.newTab().setText("3 Month Packs"));
                                        fragmentArrayList.add(new ThreeMonthlyFragment());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        sixMonthPackCall();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sixMonthPackCall();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SeeDthPlansActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void sixMonthPackCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.dth_plans + "?operator=" + Prefes.getOperator(SeeDthPlansActivity.this).replaceAll(" ", "%20") +
                "&recharge_type=" + "6 Month Packs" +
                "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String mSuccess = jsonObject.optString(ApiParams.success);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);
                                if (!(j.has("data") && j.get("data") instanceof JSONArray)) {

                                } else {
                                    JSONArray jsonArray = j.getJSONArray(ApiParams.data);
                                    if (jsonArray.length()>0){
                                        tab.addTab(tab.newTab().setText("6 Month Packs"));
                                        fragmentArrayList.add(new SixMonthlyFragment());

                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        annuallyPackCall();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        annuallyPackCall();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SeeDthPlansActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void annuallyPackCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.dth_plans + "?operator=" + Prefes.getOperator(SeeDthPlansActivity.this).replaceAll(" ", "%20") +
                "&recharge_type=" + "Annual Packs" +
                "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String mSuccess = jsonObject.optString(ApiParams.success);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);
                                if (!(j.has("data") && j.get("data") instanceof JSONArray)) {

                                } else {
                                    JSONArray jsonArray = j.getJSONArray(ApiParams.data);
                                    if (jsonArray.length()>0){
                                        tab.addTab(tab.newTab().setText("Annual Packs"));
                                        fragmentArrayList.add(new AnnualFragment());
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        if (tab.getTabCount() == 2) {
                            tab.setTabMode(TabLayout.MODE_FIXED);
                        } else {
                            tab.setTabMode(TabLayout.MODE_SCROLLABLE);
                        }


                        if (fragmentArrayList.size()>0){
                            mainLinear.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                            Prefes.saveDescription("", SeeDthPlansActivity.this);
                            DthPagerAdapter adapter = new DthPagerAdapter(getSupportFragmentManager(), tab.getTabCount(), fragmentArrayList);
                            viewPager.setAdapter(adapter);
                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
                        }else {
                            Toast.makeText(SeeDthPlansActivity.this, "Plans are not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (tab.getTabCount() == 2) {
                            tab.setTabMode(TabLayout.MODE_FIXED);
                        } else {
                            tab.setTabMode(TabLayout.MODE_SCROLLABLE);
                        }

                        if (fragmentArrayList.size()>0){
                            mainLinear.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                            Prefes.saveDescription("", SeeDthPlansActivity.this);
                            DthPagerAdapter adapter = new DthPagerAdapter(getSupportFragmentManager(), tab.getTabCount(), fragmentArrayList);
                            viewPager.setAdapter(adapter);
                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
                        }else {
                            Toast.makeText(SeeDthPlansActivity.this, "Plans are not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SeeDthPlansActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
