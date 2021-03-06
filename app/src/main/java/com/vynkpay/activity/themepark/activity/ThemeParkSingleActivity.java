package com.vynkpay.activity.themepark.activity;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.giftcards.model.GiftListModel;
import com.vynkpay.activity.themepark.adapter.SingleThemeParkAdapter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;
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

public class ThemeParkSingleActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
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
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_single_giftcard_rcg);
        ButterKnife.bind(ThemeParkSingleActivity.this);
        access_token = M.fetchUserTrivialInfo(ThemeParkSingleActivity.this, access_token);
        dialog = M.showDialog(ThemeParkSingleActivity.this, "", true, true);

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
                String id = jsonObject1.getString("product_id");
                String country = jsonObject1.getString("country");
                String city = jsonObject1.getString("city");
                String image = jsonObject1.getString("image");
                String price = jsonObject1.getString("price");

                giftCardListModel.setCategoryId(id);
                giftCardListModel.setCategoryName(name);
                giftCardListModel.setPrice(price);
                giftCardListModel.setCountry(country);
                giftCardListModel.setCity(city);
                giftCardListModel.setImage(BuildConfig.ImageBaseURl + image.replaceAll("\\\\", ""));
                giftCardListModelArrayList.add(giftCardListModel);
            }
            SingleThemeParkAdapter giftCardCategoryListingAdapter = new SingleThemeParkAdapter(ThemeParkSingleActivity.this, giftCardListModelArrayList);
            giftRecyclerView.setLayoutManager(M.verticalRecyclerView(ThemeParkSingleActivity.this));
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
        String Url = BuildConfig.APP_BASE_URL + URLS.themeParkProducts + "?category_id=" + category_id;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Url, null, future, null);
        MySingleton.getInstance(ThemeParkSingleActivity.this).addToRequestQueue(req);
        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return future.get();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(ThemeParkSingleActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(ThemeParkSingleActivity.this,ThemeParkSingleActivity.this::finishAffinity);
        }
    }
}
