package com.vynkpay.activity.recharge.mobile.activity;

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
import com.vynkpay.activity.recharge.mobile.fragment.FourGFragment;
import com.vynkpay.activity.recharge.mobile.fragment.FullTalkTimeFragment;
import com.vynkpay.activity.recharge.mobile.fragment.RoamingFragment;
import com.vynkpay.activity.recharge.mobile.fragment.SpecialRechargeFragment;
import com.vynkpay.activity.recharge.mobile.fragment.ThreeGFragment;
import com.vynkpay.activity.recharge.mobile.fragment.TopUpFragment;
import com.vynkpay.activity.recharge.mobile.fragment.TwoGFragment;
import com.vynkpay.utils.M;
import com.vynkpay.utils.URLS;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
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

public class SeePlanActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    NormalTextView toolbar_title;
    @BindView(R.id.tabs)
    TabLayout tab;
    @BindView(R.id.frameLayout)
    ViewPager viewPager;

    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.mainLinear)
    LinearLayout mainLinear;

    String operatorName, circleName;
    String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_plan_rcg);
        ButterKnife.bind(SeePlanActivity.this);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        operatorName = getIntent().getStringExtra(ApiParams.operator);
        circleName = getIntent().getStringExtra(ApiParams.circle);
        type = getIntent().getStringExtra("type");
        toolbar_title.setText(operatorName + "-" + circleName);
        Prefes.saveOperator(operatorName, SeePlanActivity.this);
        Prefes.saveCircle(circleName, SeePlanActivity.this);
        Prefes.saveType(type, SeePlanActivity.this);



        Prefes.savePlan("Full Talk Time", SeePlanActivity.this); Prefes.saveDescription("", SeePlanActivity.this);

        fullTalkTimeCall();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Prefes.savePlan(tab.getTabAt(position).getText().toString(), SeePlanActivity.this);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Prefes.savePlan(tab.getText().toString(), SeePlanActivity.this);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }



    ArrayList<Fragment> tabArrayList=new ArrayList<>();
    private void fullTalkTimeCall() {
        mainLinear.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        tabArrayList.clear();
        tab.removeAllTabs();

        if (tab.getTabCount() == 2) {
            tab.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.rechargePlans + "?operator=" + Prefes.getOperator(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&circle=" + Prefes.getCircle(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&recharge_type=" + "full" +
                "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        M.longLog("fourGData", response);
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
                                        tabArrayList.add(new FullTalkTimeFragment());

                                        tab.addTab(tab.newTab().setText("Full Talk Time"));

                                    }
                                }
                            }
                        } catch (Exception e) {
                        }

                        fourGCall();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        fourGCall();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SeePlanActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void fourGCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.rechargePlans + "?operator=" + Prefes.getOperator(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&circle=" + Prefes.getCircle(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&recharge_type=" + "4g" +
                "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        M.longLog("fourGData", response);
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
                                        tabArrayList.add(new FourGFragment());
                                        tab.addTab(tab.newTab().setText("4g"));

                                    }
                                }
                            }
                        } catch (Exception e) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(SeePlanActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void threeGCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.rechargePlans + "?operator=" + Prefes.getOperator(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&circle=" + Prefes.getCircle(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&recharge_type=" + "3g" +
                "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        M.longLog("fourGData", response);
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
                                        tabArrayList.add(new ThreeGFragment());
                                        tab.addTab(tab.newTab().setText("3g"));

                                    }
                                }
                            }
                        } catch (Exception e) {
                        }

                        twoGCall();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        twoGCall();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SeePlanActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void twoGCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.rechargePlans + "?operator=" + Prefes.getOperator(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&circle=" + Prefes.getCircle(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&recharge_type=" + "2g" +
                "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        M.longLog("fourGData", response);
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
                                        tabArrayList.add(new TwoGFragment());
                                        tab.addTab(tab.newTab().setText("2g"));

                                    }
                                }
                            }
                        } catch (Exception e) {
                        }

                        topupCall();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        topupCall();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SeePlanActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void topupCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.rechargePlans + "?operator=" + Prefes.getOperator(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&circle=" + Prefes.getCircle(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&recharge_type=" + "top" +
                "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        M.longLog("fourGData", response);
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
                                        tabArrayList.add(new TopUpFragment());
                                        tab.addTab(tab.newTab().setText("Top up"));

                                    }
                                }
                            }
                        } catch (Exception e) {
                        }

                        specialRchCall();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        specialRchCall();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SeePlanActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void specialRchCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.rechargePlans + "?operator=" + Prefes.getOperator(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&circle=" + Prefes.getCircle(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&recharge_type=" + "Other" +
                "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        M.longLog("fourGData", response);
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
                                        tabArrayList.add(new SpecialRechargeFragment());
                                        tab.addTab(tab.newTab().setText("Special Recharge"));

                                    }
                                }
                            }
                        } catch (Exception e) {
                        }
                        roamingCall();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        roamingCall();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(SeePlanActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void roamingCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.rechargePlans + "?operator=" + Prefes.getOperator(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&circle=" + Prefes.getCircle(SeePlanActivity.this).replaceAll(" ", "%20") +
                "&recharge_type=" + "Roaming" +
                "&page=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        M.longLog("fourGData", response);
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
                                        tabArrayList.add(new RoamingFragment());
                                        tab.addTab(tab.newTab().setText("Roaming"));
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }

                        if (tabArrayList.size()>0){
                            mainLinear.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                            PlansPagerAdapter adapter = new PlansPagerAdapter(getSupportFragmentManager(), tab.getTabCount(), tabArrayList);
                            viewPager.setAdapter(adapter);
                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
                        }else {
                            Toast.makeText(SeePlanActivity.this, "Plans are not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (tabArrayList.size()>0){
                            mainLinear.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                            PlansPagerAdapter adapter = new PlansPagerAdapter(getSupportFragmentManager(), tab.getTabCount(), tabArrayList);
                            viewPager.setAdapter(adapter);
                            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
                        }else {
                            Toast.makeText(SeePlanActivity.this, "Plans are not found", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SeePlanActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}
