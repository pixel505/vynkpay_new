package com.vynkpay.activity.shops;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.shops.models.Categorys;
import com.vynkpay.activity.shops.models.CountryList;
import com.vynkpay.activity.shops.models.Deal;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.FragmentShopBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.utils.M;
import com.vynkpay.utils.URLS;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {

    FragmentShopBinding binding;
    Dialog dialog;
    ArrayList<Categorys> categorysList = new ArrayList<>();
    ArrayList<CountryList> countryList = new ArrayList<>();
    ArrayList<Deal> dealsList = new ArrayList<>();
    String selectedId = "";

    public ShopFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shop, container, false);
        View view = binding.getRoot();
        dialog = M.showDialog(getActivity(),"",false,false);
        binding.rvList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        getShops();
        ShopsActivity.ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterShowDialog();
            }
        });
        //getShopList();
        onSearch("");
        init();
        return view;
    }

    void init(){
        ShopsActivity.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length()>0){
                    onSearch(charSequence.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void showFilterShowDialog(){
        Dialog mdialog = new Dialog(getActivity());
        if (mdialog.getWindow() != null){
            mdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        mdialog.setContentView(R.layout.custom_filterdialog);
        NormalTextView tvClear = mdialog.findViewById(R.id.tvClear);
        RecyclerView crvList = mdialog.findViewById(R.id.crvList);
        crvList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        if (categorysList.size()>0){
            CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(),categorysList);
            crvList.setAdapter(categoryAdapter);
            categoryAdapter.setOnItemClickListener((id, title, total) -> {
                mdialog.dismiss();
                //showSubCategory(title,total);
                selectedId = id;
                onFilter(selectedId);
                //getShopList();
            });
        }

        tvClear.setOnClickListener(view -> {
             mdialog.dismiss();
             onSearch("");
        });

        mdialog.show();

    }

    void showSubCategory(String title,String total){
        Dialog mdialog = new Dialog(getActivity());
        if (mdialog.getWindow() != null){
            mdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        mdialog.setContentView(R.layout.custom_filtersubcatedialog);
        NormalTextView tvClear = mdialog.findViewById(R.id.tvClear);
        NormalTextView tvCategoryName = mdialog.findViewById(R.id.tvCategoryName);
        LinearLayout linBack = mdialog.findViewById(R.id.linBack);
        RecyclerView subrvList = mdialog.findViewById(R.id.subrvList);
        subrvList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        tvCategoryName.setText(title+"("+total+")");
        /*if (categorysList.size()>0){
            binding.rvList.setAdapter(new CategoryAdapter(getActivity(),categorysList));
        }*/

        linBack.setOnClickListener(view -> mdialog.dismiss());
        tvClear.setOnClickListener(view -> {
            mdialog.dismiss();

        });

        mdialog.show();
    }

    public void onFilter(String searchText){
        dealsList.clear();
        dialog.show();
        binding.noLayout.setVisibility(View.GONE);
        MainApplication.getApiService().shopSearch(Prefes.getAccessToken(getActivity()),"",searchText).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                binding.noLayout.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body()!=null){
                    Log.d("searchresp",response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getString("status").equalsIgnoreCase("true")){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray dealsArr = data.getJSONArray("deals");
                            for (int i=0; i<dealsArr.length(); i++){
                                JSONObject jData = dealsArr.getJSONObject(i);
                                String id = jData.optString("id");
                                String title = jData.optString("title");
                                String brand = jData.optString("brand");
                                String image = jData.optString("image");
                                String path = jData.optString("path");
                                String cashbackPercentage = jData.optString("cashbackPercentage");
                                String shoopingPoints = jData.optString("shoopingPoints");
                                String description = jData.optString("description");
                                String terms = jData.optString("terms");
                                String durl = jData.optString("url");
                                String country_id = jData.optString("country_id");
                                String category_id = jData.optString("category_id");
                                String created_at = jData.optString("created_at");
                                String isActive = jData.optString("isActive");
                                String updated_at = jData.optString("updated_at");
                                String category_title = jData.optString("category_title");
                                String country_name = jData.optString("country_name");
                                dealsList.add(new Deal( id,  title,  brand,  image,  path,  cashbackPercentage,  shoopingPoints,  description,  terms,  durl,  country_id,  category_id,  created_at,  updated_at,  isActive,  category_title, country_name));
                            }
                            if (dealsList.size()>0){
                                binding.rvList.setAdapter(new ListAdapter(dealsList));
                            }else {
                                binding.noLayout.setVisibility(View.VISIBLE);
                            }

                        }else {
                            if (jsonObject.has("message")){
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(jsonObject.getString("message")+"");
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }catch (Exception e){
                        binding.noLayout.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                } else {
                    binding.noLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                Log.d("searchresp",t.getMessage()!=null ? t.getMessage() :"");
            }

        });
    }
    //shopSearch
    public void onSearch(String searchText){
        dealsList.clear();
        dialog.show();
        binding.noLayout.setVisibility(View.GONE);
        MainApplication.getApiService().shopSearch(Prefes.getAccessToken(getActivity()),searchText,"").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                binding.noLayout.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body()!=null){
                    Log.d("searchresp",response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getString("status").equalsIgnoreCase("true")){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray dealsArr = data.getJSONArray("deals");
                            for (int i=0; i<dealsArr.length(); i++){
                                JSONObject jData = dealsArr.getJSONObject(i);
                                String id = jData.optString("id");
                                String title = jData.optString("title");
                                String brand = jData.optString("brand");
                                String image = jData.optString("image");
                                String path = jData.optString("path");
                                String cashbackPercentage = jData.optString("cashbackPercentage");
                                String shoopingPoints = jData.optString("shoopingPoints");
                                String description = jData.optString("description");
                                String terms = jData.optString("terms");
                                String durl = jData.optString("url");
                                String country_id = jData.optString("country_id");
                                String category_id = jData.optString("category_id");
                                String created_at = jData.optString("created_at");
                                String isActive = jData.optString("isActive");
                                String updated_at = jData.optString("updated_at");
                                String category_title = jData.optString("category_title");
                                String country_name = jData.optString("country_name");
                                dealsList.add(new Deal( id,  title,  brand,  image,  path,  cashbackPercentage,  shoopingPoints,  description,  terms,  durl,  country_id,  category_id,  created_at,  updated_at,  isActive,  category_title, country_name));
                            }
                            if (dealsList.size()>0){
                                binding.rvList.setAdapter(new ListAdapter(dealsList));
                            }else {
                                binding.noLayout.setVisibility(View.VISIBLE);
                            }

                        }else {
                            if (jsonObject.has("message")){
                                binding.noLayout.setVisibility(View.VISIBLE);
                                binding.noMessageTV.setText(jsonObject.getString("message")+"");
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }catch (Exception e){
                        binding.noLayout.setVisibility(View.VISIBLE);
                        e.printStackTrace();
                    }
                } else {
                    binding.noLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                Log.d("searchresp",t.getMessage()!=null ? t.getMessage() :"");
            }

        });
    }

    //account/api_accounts/shops
    public void getShops(){
        categorysList.clear();
        countryList.clear();
        dialog.show();
        Log.d("printaccesstoken",Prefes.getAccessToken(getActivity())+"///");
        MainApplication.getApiService().getShops(Prefes.getAccessToken(getActivity())).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                Log.d("shopresponse",response.body());
                if (response.isSuccessful() && response.body() != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getString("status").equalsIgnoreCase("true")){
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray categoryArr = data.getJSONArray("category");
                            JSONArray countryArr = data.getJSONArray("country");

                            for (int i=0; i<categoryArr.length(); i++){
                                JSONObject jData = categoryArr.getJSONObject(i);
                                String id = jData.getString("id");
                                String title = jData.getString("title");
                                String total = jData.getString("total");
                                categorysList.add(new Categorys(id,title,total));
                            }


                            for (int i=0; i<countryArr.length(); i++){
                                JSONObject jData = countryArr.getJSONObject(i);
                                String id = jData.getString("id");
                                String country_name = jData.getString("country_name");
                                countryList.add(new CountryList(id,country_name));
                            }

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                Log.d("shopresponse",t.getMessage()!=null?t.getMessage():"Error");
            }
        });
    }

    public void getShopList(){
        dealsList.clear();
        binding.noLayout.setVisibility(View.GONE);
        String url = BuildConfig.BASE_URL+ URLS.SHOP_URL+selectedId;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
                Log.d("crespone",response);
                binding.noLayout.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray dealsArr = data.getJSONArray("deals");
                        for (int i=0; i<dealsArr.length(); i++){
                            JSONObject jData = dealsArr.getJSONObject(i);
                            String id = jData.optString("id");
                            String title = jData.optString("title");
                            String brand = jData.optString("brand");
                            String image = jData.optString("image");
                            String path = jData.optString("path");
                            String cashbackPercentage = jData.optString("cashbackPercentage");
                            String shoopingPoints = jData.optString("shoopingPoints");
                            String description = jData.optString("description");
                            String terms = jData.optString("terms");
                            String durl = jData.optString("url");
                            String country_id = jData.optString("country_id");
                            String category_id = jData.optString("category_id");
                            String created_at = jData.optString("created_at");
                            String isActive = jData.optString("isActive");
                            String updated_at = jData.optString("updated_at");
                            String category_title = jData.optString("category_title");
                            String country_name = jData.optString("country_name");
                            dealsList.add(new Deal( id,  title,  brand,  image,  path,  cashbackPercentage,  shoopingPoints,  description,  terms,  durl,  country_id,  category_id,  created_at,  updated_at,  isActive,  category_title, country_name));
                        }
                        if (dealsList.size()>0){
                            binding.rvList.setAdapter(new ListAdapter(dealsList));
                        }else {
                            binding.noLayout.setVisibility(View.VISIBLE);
                        }

                    }else {
                        if (jsonObject.has("message")){
                            binding.noLayout.setVisibility(View.VISIBLE);
                            binding.noMessageTV.setText(jsonObject.getString("message")+"");
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    binding.noLayout.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }

            }, error -> {
                binding.noLayout.setVisibility(View.VISIBLE);
                String errorString = error.getMessage()!=null?error.getMessage():"Error";
                Log.d("crespone",errorString);
                Toast.makeText(getActivity(),errorString , Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(),getString(R.string.timeOut) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getActivity(),getString(R.string.requestUnAuthorized) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getActivity(),getString(R.string.serverError) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getActivity(),getString(R.string.networkIssue) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getActivity(),getString(R.string.parserError) , Toast.LENGTH_SHORT).show();
                }
            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("access_token", Prefes.getAccessToken(getActivity()));
                    return params;
                }

            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Volley.newRequestQueue(getActivity()).add(stringRequest);

        }


    class ListAdapter extends RecyclerView.Adapter<ListAdapter.Holder>{

        ArrayList<Deal> dealsList;

        public ListAdapter( ArrayList<Deal> dealsList) {
            this.dealsList = dealsList;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_shoplist,parent,false);
            return new Holder(view);
        }



        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            try {
                holder.tvProductName.setText(dealsList.get(position).getTitle());
                holder.tvOffers.setText("upto "+dealsList.get(position).getCashbackPercentage()+" % Cashback");
                if (getActivity()!=null) {
                    Glide.with(getActivity())
                            .load(dealsList.get(position).getPath()+dealsList.get(position).getImage())
                            .into(holder.ivBrandImage);
                }
                holder.tvShopOnline.setOnClickListener(view -> {
                    //Toast.makeText(getActivity(), dealsList.get(position).getUrl(), Toast.LENGTH_SHORT).show();
                    try {
                        startActivity(new Intent(getActivity(),ProductsDetailActivity.class).putExtra("allData",new Gson().toJson(dealsList.get(position))));
                        ///Intent intent = new Intent(Intent.ACTION_VIEW);
                        //intent.setData(Uri.parse(dealsList.get(position).getUrl()));
                        //startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //startActivity(new Intent(getActivity(),ProductsDetailActivity.class).putExtra("allData",new Gson().toJson(dealsList.get(position))));
                });

                holder.itemView.setOnClickListener(view -> startActivity(new Intent(getActivity(),ProductsDetailActivity.class).putExtra("allData",new Gson().toJson(dealsList.get(position)))));
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return dealsList!=null ? dealsList.size() : 0;
        }

        class Holder extends RecyclerView.ViewHolder{

            NormalTextView tvProductName;
            NormalTextView tvOffers;
            NormalTextView tvShopOnline;
            ImageView ivBrandImage;
            public Holder(@NonNull View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvOffers = itemView.findViewById(R.id.tvOffers);
                tvShopOnline = itemView.findViewById(R.id.tvShopOnline);
                ivBrandImage = itemView.findViewById(R.id.ivBrandImage);
            }

        }
    }

}