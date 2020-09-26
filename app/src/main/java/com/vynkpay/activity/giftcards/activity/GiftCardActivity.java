package com.vynkpay.activity.giftcards.activity;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.giftcards.adapter.GiftCardAdapter;
import com.vynkpay.adapter.CustomPagerAdapter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.GiftCardModel;
import com.vynkpay.models.OffersModel;
import com.vynkpay.transformer.DepthPageTransformer;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Orientation;
import com.rd.draw.data.RtlMode;

import org.json.JSONArray;
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class GiftCardActivity extends AppCompatActivity {
    Dialog dialog;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.giftRecyclerView)
    RecyclerView giftRecyclerView;
    String access_token;
    GiftCardAdapter giftCardAdapter;
    DisposableSubscriber<JSONObject> d1;
    @BindView(R.id.viewpagerTop)
    ViewPager viewpagerTop;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (d1 != null) {
            d1.dispose();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giftcard_rcg);

        ButterKnife.bind(GiftCardActivity.this);
        access_token = M.fetchUserTrivialInfo(GiftCardActivity.this, access_token);
        dialog = M.showDialog(GiftCardActivity.this, "", true, true);
        toolbar_title.setText("Gift Cards");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        giftCardCategoryList();
        giftCardAdapter = new GiftCardAdapter(GiftCardActivity.this, giftCardModelArrayList);
        giftRecyclerView.setLayoutManager(new LinearLayoutManager(GiftCardActivity.this));
        giftRecyclerView.setNestedScrollingEnabled(false);
        giftRecyclerView.setAdapter(giftCardAdapter);

        d1 = new DisposableSubscriber<JSONObject>() {
            @Override
            public void onNext(JSONObject receivedJSON) {
                jsonObject = receivedJSON;
            }

            @Override
            public void onError(Throwable t) {
                // todo
            }

            @Override
            public void onComplete() {
                handleOperatorResponse(jsonObject);
            }
        };

        newGetOperatorData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d1);

    }

    CustomPagerAdapter customPagerAdapter;
    ArrayList<OffersModel> offerList = new ArrayList<>();
    private JSONObject jsonObject;

    private void handleOperatorResponse(JSONObject receivedJSON) {
        Log.i(">>receivedJSON", "handleOperatorResponse: " + receivedJSON.toString());
        try {
            JSONArray dataArray = jsonObject.getJSONArray(ApiParams.data);
            for (int l = 0; l < dataArray.length(); l++) {
                JSONObject jsonObject1 = dataArray.getJSONObject(l);
                String id = jsonObject1.getString("id");
                String title = jsonObject1.getString("title");
                String description = jsonObject1.getString("description");
                String image = jsonObject1.getString("image");
                String status = jsonObject1.getString("status");
                String offer_url = jsonObject1.getString("offer_url");
                String created_at = jsonObject1.getString("created_at");
                OffersModel offersModel = new OffersModel();
                offersModel.setId(id);
                offersModel.setTitle(title);
                offersModel.setDescription(description);
                offersModel.setImage(image.replaceAll("////", ""));
                offersModel.setStatus(status);
                offersModel.setOffer_url(offer_url);
                offersModel.setCreated_at(created_at);
                offerList.add(offersModel);
            }

            customPagerAdapter = new CustomPagerAdapter(GiftCardActivity.this, offerList);
            viewpagerTop.setAdapter(customPagerAdapter);
            viewpagerTop.setPageTransformer(true, new DepthPageTransformer());
            pageIndicatorView.setViewPager(viewpagerTop);
            pageIndicatorView.setAnimationType(AnimationType.THIN_WORM);
            pageIndicatorView.setOrientation(Orientation.HORIZONTAL);
            pageIndicatorView.setRtlMode(RtlMode.Off);
            pageIndicatorView.setInteractiveAnimation(true);
            pageIndicatorView.setAutoVisibility(false);
        } catch (Exception e) {
            Log.i(">>appDetail", "handleOperatorResponse: " + e.getMessage());
        }

    }

    public Flowable<JSONObject> newGetOperatorData() {
        return Flowable.defer(new Callable<Publisher<? extends JSONObject>>() {
            @Override
            public Publisher<? extends JSONObject> call() throws Exception {
                return Flowable.just(getOperatorData());
            }
        });
    }

    private JSONObject getOperatorData() throws ExecutionException, InterruptedException, RuntimeException {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String Url = BuildConfig.APP_BASE_URL + URLS.recentOffers;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Url, null, future, null);
        MySingleton.getInstance(GiftCardActivity.this).addToRequestQueue(req);
        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return future.get();
    }

    String mSuccess, mMessage;

    ArrayList<GiftCardModel> giftCardModelArrayList = new ArrayList<>();

    private void giftCardCategoryList() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.giftCardCategory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>giftCat", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONArray j = jsonObject.getJSONObject(ApiParams.data).getJSONArray(ApiParams.categories);
                                for (int k = 0; k < j.length(); k++) {
                                    JSONObject jsonObject1 = j.getJSONObject(k);
                                    String category_id = jsonObject1.getString("category_id");
                                    String category_name = jsonObject1.getString("category_name");
                                    GiftCardModel giftCardModel = new GiftCardModel();
                                    giftCardModel.setCategoryId(category_id);
                                    giftCardModel.setCategoryName(category_name);
                                    giftCardModelArrayList.add(giftCardModel);
                                }
                                giftCardAdapter.notifyDataSetChanged();
                            } else {
                                M.dialogOk(GiftCardActivity.this, mMessage, "Error!");
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
                        dialog.dismiss();
                    }
                }) {
        };
        MySingleton.getInstance(GiftCardActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}
