package com.vynkpay.activity.giftcards.activity;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.giftcards.adapter.SingleGiftCardAdapter;
import com.vynkpay.activity.giftcards.model.GiftListModel;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;

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

public class SingleGiftCardActivity extends AppCompatActivity {
    Dialog dialog;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.giftRecyclerView)
    RecyclerView giftRecyclerView;
    String access_token;
    DisposableSubscriber<JSONObject> d1;


    String category_id;
    String category_name;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (d1 != null) {
            d1.dispose();
        }
    }

    private JSONObject temp_JSON;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_giftcard_rcg);
        ButterKnife.bind(SingleGiftCardActivity.this);
        access_token = M.fetchUserTrivialInfo(SingleGiftCardActivity.this, access_token);
        dialog = M.showDialog(SingleGiftCardActivity.this, "", true, true);

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        category_id = getIntent().getStringExtra("category_id");
        category_name = getIntent().getStringExtra("category_name");
        toolbar_title.setText(category_name);

        dialog.show();
        d1 = new DisposableSubscriber<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
                temp_JSON = jsonObject;
            }

            @Override
            public void onError(Throwable t) {
                dialog.dismiss();
            }

            @Override
            public void onComplete() {
                dialog.dismiss();
                handleOperatorResponse(temp_JSON);
            }
        };

        newGetOperatorData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d1);

    }

    private JSONObject jsonObject;


    private void handleOperatorResponse(JSONObject jsonObject) {
        ArrayList<GiftListModel> giftCardListModelArrayList = new ArrayList<>();
        this.jsonObject = jsonObject;
        Log.i(">>help", "handleOperatorResponse: " + this.jsonObject.toString());
        try {
            JSONArray jsonArray = this.jsonObject.getJSONArray(ApiParams.data);
            for (int k = 0; k < jsonArray.length(); k++) {
                ArrayList<String> images = new ArrayList<>();
                GiftListModel giftCardListModel = new GiftListModel();
                JSONObject jsonObject1 = jsonArray.getJSONObject(k);
                String name = jsonObject1.getString("name");
                String id = jsonObject1.getString("id");
                String price_type = jsonObject1.getString("price_type");
                String min_custom_price = jsonObject1.getString("min_custom_price");
                String max_custom_price = jsonObject1.getString("max_custom_price");
                String image = jsonObject1.getString("image");
                String custom_denominations = jsonObject1.getString("custom_denominations");
                giftCardListModel.setCategoryId(id);
                giftCardListModel.setCategoryName(name);
                giftCardListModel.setCategoryType(price_type);
                giftCardListModel.setCategoryMin(min_custom_price);
                giftCardListModel.setCategoryMax(max_custom_price);
                giftCardListModel.setCategoryCustomDenominations(custom_denominations);
                giftCardListModel.setImage(BuildConfig.ImageBaseURl+image.replaceAll("\\\\",""));
                giftCardListModelArrayList.add(giftCardListModel);
            }

            SingleGiftCardAdapter giftCardCategoryListingAdapter = new SingleGiftCardAdapter(SingleGiftCardActivity.this, giftCardListModelArrayList);
            giftRecyclerView.setLayoutManager(M.verticalRecyclerView(SingleGiftCardActivity.this));
            giftRecyclerView.setAdapter(giftCardCategoryListingAdapter);
            giftCardCategoryListingAdapter.setHasStableIds(true);
            giftCardCategoryListingAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.i(">>log", "handleOperatorResponse: ");
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
        String Url = BuildConfig.APP_BASE_URL + URLS.giftCardProducts + "?category_id=" + category_id;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Url, null, future, null);
        MySingleton.getInstance(SingleGiftCardActivity.this).addToRequestQueue(req);
        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return future.get();
    }
}
