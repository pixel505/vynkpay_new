package com.vynkpay.activity.recharge.datacard.fragment;

import android.app.Activity;
import android.app.Dialog;
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
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.recharge.datacard.adapter.DataCardPlanAdapter;
import com.vynkpay.activity.recharge.mobile.model.OnBottomReachedListener;
import com.vynkpay.activity.recharge.mobile.model.PlanModel;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.sorting.SortAscending;
import com.vynkpay.sorting.SortDescending;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.URLS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataCardThreeGFragment extends Fragment {
    @BindView(R.id.planRecyclerView)
    RecyclerView planRecyclerView;
    private View view;
    Dialog dialog;
    private ArrayList<PlanModel> planModelArrayList = new ArrayList<>();
    String operatorName, circleName;
    private int count = 1;
    @BindView(R.id.noLayoutData)
    LinearLayout noLayoutData;
    String type = "";
    @BindView(R.id.sortBy)
    NormalTextView sortBy;
    DataCardPlanAdapter fullTalkTimeAdapter;
    private boolean state = true;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plans_rcg, container, false);
        ButterKnife.bind(DataCardThreeGFragment.this, view);
        dialog = M.showDialog(getActivity(), "", false, false);
        planModelArrayList.clear();
        type = Prefes.getType(getActivity());
        makePlanListingCall();
        fullTalkTimeAdapter = new DataCardPlanAdapter(getActivity(), planModelArrayList);
        planRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        planRecyclerView.setNestedScrollingEnabled(false);
        planRecyclerView.setAdapter(fullTalkTimeAdapter);

        sortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state) {
                    state = false;
                    Collections.sort(planModelArrayList, new SortAscending());
                    handlePagination();
                } else {
                    state = true;
                    Collections.sort(planModelArrayList, new SortDescending());
                    handlePagination();
                }
            }

        });
        return view;
    }

    private void handlePagination() {
        fullTalkTimeAdapter.notifyDataSetChanged();
        fullTalkTimeAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached(int position) {
                makePlanListingCall();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=getActivity();
        count=1;
    }

    @Override
    public void onResume() {
        super.onResume();
        count=1;
    }

    String mSuccess, mMessage;
    private void makePlanListingCall() {
        if ((Activity) getActivity() != null) {
            dialog.show();
        }

        Log.d("urllDataCard", BuildConfig.APP_BASE_URL +
                URLS.datacard_plans + "?operator=" + Prefes.getOperator(activity).replaceAll(" ", "%20") +
                "&circle=" + Prefes.getCircle(activity).replaceAll(" ", "%20") +
                "&recharge_type=" + "3g" +
                "&page=" + count);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL +
                URLS.datacard_plans + "?operator=" + Prefes.getOperator(activity).replaceAll(" ", "%20") +
                "&circle=" + Prefes.getCircle(activity).replaceAll(" ", "%20") +
                "&recharge_type=" + "3g" +
                "&page=" + count,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if ((Activity) getActivity() != null) {
                                dialog.dismiss();
                            }
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
                                        String operator_id = jsonObject1.getString("operatorid");
                                        String circle_id = jsonObject1.getString("circleid");
                                        String recharge_amount = jsonObject1.getString("recharge_amount");
                                        String recharge_talktime = jsonObject1.has("recharge_talktime") ? jsonObject1.getString("recharge_talktime") : "";
                                        String recharge_validity = jsonObject1.getString("recharge_validity");
                                        String recharge_long_desc = jsonObject1.getString("recharge_longdesc");
                                        String recharge_type = jsonObject1.getString("recharge_type");
                                        PlanModel planModel = new PlanModel();
                                        planModel.setId(id);
                                        planModel.setOperator_id(operator_id);
                                        planModel.setCircle_id(circle_id);
                                        planModel.setRecharge_amount(recharge_amount);
                                        planModel.setRecharge_talktime(recharge_talktime);
                                        planModel.setRecharge_validity(recharge_validity);
                                        planModel.setRecharge_long_desc(recharge_long_desc);
                                        planModel.setRecharge_type(recharge_type);
                                        planModelArrayList.add(planModel);
                                    }
                                    count++;
                                    handlePagination();
                                }
                            } else {
                                if ((Activity) getActivity() != null) {
                                    dialog.dismiss();
                                }
                                M.dialogOk(getActivity(), mMessage, "Error!");
                            }
                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                            if ((Activity) getActivity() != null) {
                                dialog.dismiss();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // dialog.dismiss();
                        if ((Activity) getActivity() != null) {
                            dialog.dismiss();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}