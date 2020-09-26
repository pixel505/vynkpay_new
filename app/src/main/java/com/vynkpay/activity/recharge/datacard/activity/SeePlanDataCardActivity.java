package com.vynkpay.activity.recharge.datacard.activity;

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
import com.vynkpay.activity.recharge.datacard.fragment.DataCardThreeGFragment;
import com.vynkpay.activity.recharge.datacard.fragment.DataCardTwoGFragment;
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
import com.vynkpay.activity.recharge.datacard.adapter.DataCardPagerAdapter;
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

public class SeePlanDataCardActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    NormalTextView toolbar_title;
    @BindView(R.id.tabs)
    TabLayout tab;
    @BindView(R.id.frameLayout)
    ViewPager viewPager;
    String operatorName, circleName;
    PlansPagerAdapter adapter;
    String type;
    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_plan_rcg);
        ButterKnife.bind(SeePlanDataCardActivity.this);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        operatorName = getIntent().getStringExtra(ApiParams.operator);
        circleName = getIntent().getStringExtra(ApiParams.circle);
        Log.i(">>data", "onCreate: " + operatorName + "-" + circleName);
        type = getIntent().getStringExtra("type");
        toolbar_title.setText(operatorName + "-" + circleName);
        Prefes.saveOperator(operatorName, SeePlanDataCardActivity.this);
        Prefes.saveCircle(circleName, SeePlanDataCardActivity.this);
        Prefes.saveType(type, SeePlanDataCardActivity.this);

        Prefes.saveDescription("", SeePlanDataCardActivity.this);

        twoGCall();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Prefes.savePlan(tab.getTabAt(position).getText().toString(), SeePlanDataCardActivity.this);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Prefes.savePlan(tab.getText().toString(), SeePlanDataCardActivity.this);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        if (tab.getTabCount() == 2) {
            tab.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }


    ArrayList<Fragment> fragmentArrayList=new ArrayList<>();
    private void twoGCall() {
        mainLinear.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        fragmentArrayList.clear();
        tab.removeAllTabs();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.datacard_plans + "?operator=" + Prefes.getOperator(SeePlanDataCardActivity.this).replaceAll(" ", "%20") +
                "&circle=" + Prefes.getCircle(SeePlanDataCardActivity.this).replaceAll(" ", "%20") +
                "&recharge_type=" + "2g" +
                "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dataCardDataa", response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String mSuccess = jsonObject.optString(ApiParams.success);
                            String mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);
                                if (!(j.has("data") && j.get("data") instanceof JSONArray)) {

                                } else {
                                    JSONArray jsonArray = j.getJSONArray(ApiParams.data);
                                    if (jsonArray.length()>0){
                                        tab.addTab(tab.newTab().setText("2g"));
                                        fragmentArrayList.add(new DataCardTwoGFragment());
                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        threeGCall();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        threeGCall();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SeePlanDataCardActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void threeGCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.datacard_plans + "?operator=" + Prefes.getOperator(SeePlanDataCardActivity.this).replaceAll(" ", "%20") +
                "&circle=" + Prefes.getCircle(SeePlanDataCardActivity.this).replaceAll(" ", "%20") +
                "&recharge_type=" + "3g" +
                "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("dataCardDataa", response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String mSuccess = jsonObject.optString(ApiParams.success);
                            String mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);
                                if (!(j.has("data") && j.get("data") instanceof JSONArray)) {

                                } else {
                                    JSONArray jsonArray = j.getJSONArray(ApiParams.data);
                                    if (jsonArray.length()>0){
                                        tab.addTab(tab.newTab().setText("3g"));
                                        fragmentArrayList.add(new DataCardThreeGFragment());
                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }


                        if (fragmentArrayList.size()>0){
                            mainLinear.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            DataCardPagerAdapter adapter = new DataCardPagerAdapter(getSupportFragmentManager(), tab.getTabCount(), fragmentArrayList);
                            viewPager.setAdapter(adapter);
                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
                        }else {
                            Toast.makeText(SeePlanDataCardActivity.this, "Plans are not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (fragmentArrayList.size()>0){
                            mainLinear.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            DataCardPagerAdapter adapter = new DataCardPagerAdapter(getSupportFragmentManager(), tab.getTabCount(), fragmentArrayList);
                            viewPager.setAdapter(adapter);
                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
                        }else {
                            Toast.makeText(SeePlanDataCardActivity.this, "Plans are not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SeePlanDataCardActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
