package com.vynkpay.activity.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.adapter.OfferAdapter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.OfferModel;
import com.vynkpay.models.OffersModel;
import com.vynkpay.transformer.DepthPageTransformer;
import com.vynkpay.utils.ApiParams;
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

public class OfferActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.rechargeBill)
    ViewPager rechargeBill;
    @BindView(R.id.shoppingBill)
    ViewPager shoppingBill;
    @BindView(R.id.FoodBill)
    ViewPager FoodBill;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;
    OfferAdapter offerAdapter;
    ArrayList<OfferModel> offerDataList = new ArrayList<>();
    DisposableSubscriber<JSONObject> d1;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (d1 != null) {
            d1.dispose();
        }
    }

    private JSONObject jsonObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_rcg);
        ButterKnife.bind(OfferActivity.this);
        //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
        setSupportActionBar(toolbar);
        toolbarTitle.setText("Offers");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fetchOfferData();
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);

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

    public Flowable<JSONObject> newGetOperatorData() {
        return Flowable.defer(new Callable<Publisher<? extends JSONObject>>() {
            @Override
            public Publisher<? extends JSONObject> call() throws Exception {
                return Flowable.just(getOperatorData());
            }
        });
    }

    ArrayList<OffersModel> offerList = new ArrayList<>();

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
                offersModel.setOffer_url("CODE" + l);
                offersModel.setCreated_at("50% OFF");
                offerList.add(offersModel);
            }

            offerAdapter = new OfferAdapter(OfferActivity.this, offerList);
            viewPager.setAdapter(offerAdapter);
            //viewPager.setPageMargin(20);
            viewPager.setPageTransformer(true, new DepthPageTransformer());
            pageIndicatorView.setViewPager(viewPager);
            pageIndicatorView.setAnimationType(AnimationType.SLIDE);
            pageIndicatorView.setOrientation(Orientation.HORIZONTAL);
            pageIndicatorView.setRtlMode(RtlMode.Off);
            pageIndicatorView.setUnselectedColor(getResources().getColor(R.color.mainBackColor));
            pageIndicatorView.setSelectedColor(getResources().getColor(R.color.black));
            pageIndicatorView.setInteractiveAnimation(true);
            pageIndicatorView.setAutoVisibility(false);

            offerAdapter = new OfferAdapter(OfferActivity.this, offerList);
            rechargeBill.setAdapter(offerAdapter);
            rechargeBill.setPageMargin(20);

            offerAdapter = new OfferAdapter(OfferActivity.this, offerList);
            shoppingBill.setAdapter(offerAdapter);
            shoppingBill.setPageMargin(20);
            offerAdapter = new OfferAdapter(OfferActivity.this, offerList);
            FoodBill.setAdapter(offerAdapter);
            FoodBill.setPageMargin(20);


        } catch (Exception e) {
            Log.i(">>appDetail", "handleOperatorResponse: " + e.getMessage());
        }

    }

    private JSONObject getOperatorData() throws ExecutionException, InterruptedException, RuntimeException {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String Url = BuildConfig.APP_BASE_URL + URLS.recentOffers;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Url, null, future, null);
        MySingleton.getInstance(OfferActivity.this).addToRequestQueue(req);
        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return future.get();
    }

    private void fetchOfferData() {
        offerDataList.clear();
        for (int k = 0; k < 5; k++) {
            OfferModel offerModel = new OfferModel();
            offerModel.setOfferCode("CODE" + k);
            offerModel.setOfferCodeText("50% OFF");
            offerModel.setTitle("Test Offer test Notification " + k);
            offerModel.setDescription("Hello hello mike testing, mic testing");
            offerModel.setImage((k % 2 == 0) ? "https://cdn.cnn.com/cnnnext/dam/assets/171027052520-processed-foods-overlay-tease.jpg" :
                    (k % 3 == 0) ? "https://www.theandroidsoul.com/wp-content/uploads/2017/12/google-play-store-discounts.png" :
                            "https://cdn-images-1.medium.com/max/2000/1*N5S-XOXZwTQlVdO1ozeLkw.png");
            offerDataList.add(offerModel);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
