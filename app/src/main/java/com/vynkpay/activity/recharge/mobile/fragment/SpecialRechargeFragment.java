package com.vynkpay.activity.recharge.mobile.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.recharge.mobile.adapter.FullTalkTimeAdapter;
import com.vynkpay.activity.recharge.mobile.model.OnBottomReachedListener;
import com.vynkpay.activity.recharge.mobile.model.PlanModel;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.URLS;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SpecialRechargeFragment extends Fragment {
    @BindView(R.id.planRecyclerView)
    RecyclerView planRecyclerView;
    private View view;
    Activity activity;
    //Dialog dialog;
    private ArrayList<PlanModel> planModelArrayList = new ArrayList<>();
    String operatorName, circleName;
    private int count = 1;
    @BindView(R.id.noLayoutData)
    LinearLayout noLayoutData;

    @BindView(R.id.sortBy)
    NormalTextView sortBy;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plans_rcg, container, false);
        ButterKnife.bind(SpecialRechargeFragment.this, view);
        planModelArrayList.clear();
        sortBy.setVisibility(View.GONE);
        makePlanListingCall();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        count = 1;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=getActivity();
        count = 1;
    }

    String mSuccess, mMessage;
    private void makePlanListingCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.rechargePlans + "?operator=" + Prefes.getOperator(activity).replaceAll(" ", "%20") +
                "&circle=" + Prefes.getCircle(activity).replaceAll(" ", "%20") +
                "&recharge_type=" + "Other" +
                "&page=" + count,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>threeGdata", "five: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject j = jsonObject.getJSONObject(ApiParams.data);
                                if (!(j.has("data") && j.get("data") instanceof JSONArray)) {
                                    if (!(planModelArrayList.size() > 0)) {
                                        noLayoutData.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    JSONArray jsonArray = j.getJSONArray(ApiParams.data);
                                    for (int k = 0; k < jsonArray.length(); k++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(k);
                                        String id = jsonObject1.getString("id");
                                        String operator_id = jsonObject1.getString("operator_id");
                                        String circle_id = jsonObject1.getString("circle_id");
                                        String recharge_amount = jsonObject1.getString("recharge_amount");
                                        String recharge_talktime = jsonObject1.has("recharge_talktime") ? jsonObject1.getString("recharge_talktime") : "";

                                        String recharge_validity = jsonObject1.getString("recharge_validity");
                                        String recharge_long_desc = jsonObject1.getString("recharge_long_desc");
                                        String recharge_type = jsonObject1.getString("recharge_type");
                                        String business_rule = jsonObject1.optString("business_rule");
                                        PlanModel planModel = new PlanModel();
                                        planModel.setId(id);
                                        planModel.setOperator_id(operator_id);
                                        planModel.setCircle_id(circle_id);
                                        planModel.setRecharge_amount(recharge_amount);
                                        planModel.setRecharge_talktime(recharge_talktime);
                                        planModel.setRecharge_validity(recharge_validity);
                                        planModel.setRecharge_long_desc(recharge_long_desc);
                                        planModel.setRecharge_type(recharge_type);
                                        planModel.setBusiness_rule(business_rule);
                                        planModelArrayList.add(planModel);
                                    }
                                    count++;
                                    FullTalkTimeAdapter fullTalkTimeAdapter = new FullTalkTimeAdapter(activity, planModelArrayList);
                                    planRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    planRecyclerView.setNestedScrollingEnabled(false);
                                    planRecyclerView.setAdapter(fullTalkTimeAdapter);
                                    fullTalkTimeAdapter.notifyDataSetChanged();
                                    fullTalkTimeAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
                                        @Override
                                        public void onBottomReached(int position) {
                                            makePlanListingCall();
                                        }
                                    });
                                }
                            } else {
                                noLayoutData.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}