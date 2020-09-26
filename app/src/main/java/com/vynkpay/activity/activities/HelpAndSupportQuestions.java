package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.adapter.HelpDetailsAdapter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.HelpSupportModel;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelpAndSupportQuestions extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.recyclerViewHelpAndSupport)
    RecyclerView recyclerViewHelpAndSupport;
    Dialog dialog1;
    String id, name;
    ArrayList<HelpSupportModel> helpSupportModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_details_rcg);
        ButterKnife.bind(HelpAndSupportQuestions.this);
        dialog1 = M.showDialog(HelpAndSupportQuestions.this, "", false, false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        setListeners();
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        toolbarTitle.setText(name);
        helpSupportModelArrayList.clear();
        fetchSupportList();
        bindAdapter();
    }

    private void setListeners() {

    }

    HelpDetailsAdapter helpAndSupportAdapter;

    private void bindAdapter() {
        helpAndSupportAdapter = new HelpDetailsAdapter(HelpAndSupportQuestions.this, helpSupportModelArrayList);
        recyclerViewHelpAndSupport.setLayoutManager(new LinearLayoutManager(HelpAndSupportQuestions.this));
        recyclerViewHelpAndSupport.setAdapter(helpAndSupportAdapter);
    }

    String mSuccess, mMessage;

    private void fetchSupportList() {
        dialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.faqDetails + "/" + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog1.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>numberFetch", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONArray dataArray = jsonObject.getJSONArray(ApiParams.data);
                                for (int k = 0; k < dataArray.length(); k++) {
                                    JSONObject jsonObject1 = dataArray.getJSONObject(k);
                                    HelpSupportModel helpSupportModel = new HelpSupportModel();
                                    helpSupportModel.setQuestion(jsonObject1.getString("question"));
                                    helpSupportModel.setAnswer(jsonObject1.getString("answer"));
                                    helpSupportModelArrayList.add(helpSupportModel);
                                }
                                helpAndSupportAdapter.notifyDataSetChanged();
                            } else {
                                M.dialogOk(HelpAndSupportQuestions.this, mMessage, "Error");
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
                        dialog1.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.accept, ApiParams.application_json);
                return params;
            }

        };
        MySingleton.getInstance(HelpAndSupportQuestions.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
