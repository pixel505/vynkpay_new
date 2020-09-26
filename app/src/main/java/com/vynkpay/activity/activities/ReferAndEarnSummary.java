package com.vynkpay.activity.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.recharge.mobile.model.ReferEarnSummaryModel;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityReferAndEarnSummaryBinding;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReferAndEarnSummary extends AppCompatActivity {

    ActivityReferAndEarnSummaryBinding binding;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_refer_and_earn_summary);
        ButterKnife.bind(ReferAndEarnSummary.this);
        dev();
    }

    private void dev() {

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbarTitle.setText("Summary");

        getSummaryDetails();
    }

    ArrayList<ReferEarnSummaryModel> referEarnSummaryModelArrayList = new ArrayList<>();
    public void getSummaryDetails(){
        //user/referralEarnStatement
        referEarnSummaryModelArrayList.clear();
        String url= BuildConfig.APP_BASE_URL+ URLS.refer_earn_summary_URL;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("dsadashdkhsdas", response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject object=jsonArray.getJSONObject(i);
                        String id = object.optString("id");
                        String front_user_id = object.optString("front_user_id");
                        String user_id = object.optString("user_id");
                        String type = object.optString("type");
                        String payment_via = object.optString("payment_via");
                        String p_amount = object.optString("p_amount");
                        String profit_type = object.optString("profit_type");
                        String mode = object.optString("mode");
                        String status = object.optString("status");
                        String created_date = object.optString("created_date");
                        String username = object.optString("username");
                        String email = object.optString("email");
                        String phone = object.optString("phone");
                        String name = object.optString("name");
                        String paid_status = object.optString("paid_status");
                        String balance = object.optString("balance");
                        String frontusername = object.optString("frontusername");
                        String ref_username = object.optString("ref_username");
                        String ref_name = object.optString("ref_name");
                        String ref_email = object.optString("ref_email");
                        String status_txt = object.optString("status_txt");

                        referEarnSummaryModelArrayList.add(new ReferEarnSummaryModel( id,  front_user_id,  user_id,  type,  payment_via,
                                p_amount,  profit_type,  mode,  status,  created_date,  username,  email,  phone,  name,  paid_status,
                                balance,  frontusername,  ref_username,  ref_name,  ref_email,  status_txt));
                    }

                    if (referEarnSummaryModelArrayList.size()>0){
                        binding.mainFrame.setVisibility(View.VISIBLE);
                        binding.noLayout.setVisibility(View.GONE);
                        binding.recyclerView.setAdapter(new SummaryAdapter(ReferAndEarnSummary.this));
                    }else {
                        binding.mainFrame.setVisibility(View.GONE);
                        binding.noLayout.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.d("jdksjdksjksdsdsd",  M.fetchUserTrivialInfo(ReferAndEarnSummary.this, ApiParams.access_token));
                params.put(ApiParams.access_token, M.fetchUserTrivialInfo(ReferAndEarnSummary.this, ApiParams.access_token));
                return params;
            }
        };


        MySingleton.getInstance(ReferAndEarnSummary.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.Holder>{

        Context context;

        public SummaryAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.refer_summary_layout, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.nameTV.setText(referEarnSummaryModelArrayList.get(position).getRef_name()+"("+referEarnSummaryModelArrayList.get(position).getRef_username()+")");
            holder.pointsTV.setText(referEarnSummaryModelArrayList.get(position).getP_amount());
            holder.statusTV.setText(referEarnSummaryModelArrayList.get(position).getStatus_txt());

            if (referEarnSummaryModelArrayList.get(position).getStatus_txt().equals("PENDING")){
                holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.color_red));
            }else {
                holder.statusTV.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            }

            holder.dateTV.setText(M.changeDateFormat(referEarnSummaryModelArrayList.get(position).getCreated_date(), "yyyy-MM-dd hh:mm:ss", "dd MMM yyyy"));
        }

        @Override
        public int getItemCount() {
            return referEarnSummaryModelArrayList.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView userNameTV, nameTV, dateTV, pointsTV, statusTV;
            public Holder(@NonNull View itemView) {
                super(itemView);
                userNameTV = itemView.findViewById(R.id.userNameTV);
                nameTV = itemView.findViewById(R.id.nameTV);
                dateTV = itemView.findViewById(R.id.dateTV);
                pointsTV = itemView.findViewById(R.id.pointsTV);
                statusTV = itemView.findViewById(R.id.statusTV);
            }
        }
    }
}
