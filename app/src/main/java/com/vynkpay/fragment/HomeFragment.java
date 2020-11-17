package com.vynkpay.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.activity.activities.MoneyTransferActivity;
import com.vynkpay.activity.activities.PayActivity;
import com.vynkpay.activity.activities.WalletActivity;
import com.vynkpay.activity.activities.history.AllRechargeHistoryActivity;
import com.vynkpay.adapter.BookServiceAdapter;
import com.vynkpay.adapter.CustomPagerAdapter;
import com.vynkpay.adapter.RechargeServiceAdapter;
import com.vynkpay.models.OffersModel;
import com.vynkpay.models.ServiceModel;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;
import com.rd.PageIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class HomeFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.bookServiceRecyclerView)
    RecyclerView bookServiceRecyclerView;
    @BindView(R.id.rechargeServiceRecyclerView)
    RecyclerView rechargeServiceRecyclerView;
    @BindView(R.id.walletLayout)
    LinearLayout walletLayout;
    @BindView(R.id.moneyTransferLayout)
    LinearLayout moneyTransferLayout;
    @BindView(R.id.payLayout)
    LinearLayout payLayout;
    @BindView(R.id.historyLayout)
    LinearLayout historyLayout;
    @BindView(R.id.viewpagerTop)
    ViewPager viewpagerTop;
    @BindView(R.id.pageIndicatorView)
    PageIndicatorView pageIndicatorView;
    @BindView(R.id.comingSoon)
    ImageView comingSoon;
    CustomPagerAdapter customPagerAdapter;
    static int SPAN_COUNT;
    DisposableSubscriber<JSONObject> d1;
    private JSONObject jsonObject;
    View view;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (d1 != null) {
            d1.dispose();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_rcg, container, false);

        ButterKnife.bind(HomeFragment.this, view);
        serviceModelArrayList.clear();

        setListeners();
        new FetchServiceTask(getActivity()).execute();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (getActivity()!=null){
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        if (width <= 650 && width > 500) {
            SPAN_COUNT = 3;
        } else if (width <= 480 && width > 350) {
            SPAN_COUNT = 3;
        } else {
            SPAN_COUNT = 4;
        }

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

        return view;
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
                offersModel.setOffer_url(offer_url);
                offersModel.setCreated_at(created_at);
                offerList.add(offersModel);
            }

            customPagerAdapter = new CustomPagerAdapter(getActivity(), offerList);
            viewpagerTop.setAdapter(customPagerAdapter);
            //pageIndicatorView.setViewPager(viewpagerTop);
//            pageIndicatorView.setAnimationType(AnimationType.THIN_WORM);
//            pageIndicatorView.setOrientation(Orientation.HORIZONTAL);
//            pageIndicatorView.setRtlMode(RtlMode.Off);
//            pageIndicatorView.setInteractiveAnimation(true);
//            pageIndicatorView.setAutoVisibility(false);

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
        MySingleton.getInstance(getActivity()).addToRequestQueue(req);
        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return future.get();
    }

    private void setListeners() {
        walletLayout.setOnClickListener(this);
        historyLayout.setOnClickListener(this);
        moneyTransferLayout.setOnClickListener(this);
        payLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.walletLayout:
                if (!Prefes.getUserData(getActivity()).equalsIgnoreCase("")) {
                    M.launchActivity(getActivity(), WalletActivity.class);
                } else {
                    M.makeChecks(getActivity(), LoginActivity.class);
                }
                break;

            case R.id.moneyTransferLayout:
                if (!Prefes.getUserData(getActivity()).equalsIgnoreCase("")) {
                    M.launchActivity(getActivity(), MoneyTransferActivity.class);
                } else {
                    M.makeChecks(getActivity(), LoginActivity.class);
                }
                break;

            case R.id.payLayout:
                if (!Prefes.getUserData(getActivity()).equalsIgnoreCase("")) {
                    M.launchActivity(getActivity(), PayActivity.class);
                } else {
                    M.makeChecks(getActivity(), LoginActivity.class);
                }
                break;

            case R.id.historyLayout:
                if (!Prefes.getUserData(getActivity()).equalsIgnoreCase("")) {
                    M.launchActivity(getActivity(), AllRechargeHistoryActivity.class);
                } else {
                    M.makeChecks(getActivity(), LoginActivity.class);
                }
                break;
        }
    }

    ArrayList<ServiceModel> serviceModelArrayList = new ArrayList<>();

    private static class FetchServiceTask extends AsyncTask<Void, Void, HashMap<Integer, ArrayList<ServiceModel>>> {
        private final Context weakActivity;

        private FetchServiceTask(Context myActivity) {
            this.weakActivity = myActivity;
        }

        RechargeServiceAdapter rechargeServiceAdapter;
        BookServiceAdapter bookServiceAdapter;
        ArrayList<ServiceModel> tempServiceModelArrayList = new ArrayList<ServiceModel>();
        ArrayList<ServiceModel> tempBookServiceModelArrayList = new ArrayList<ServiceModel>();
        HashMap<Integer, ArrayList<ServiceModel>> listing = new HashMap<Integer, ArrayList<ServiceModel>>();

        @Override
        protected HashMap<Integer, ArrayList<ServiceModel>> doInBackground(Void... voids) {
            tempServiceModelArrayList.clear();
            listing.clear();
            tempBookServiceModelArrayList.clear();
            for (int k = 0; k < ApiParams.AssetsUsed.rechargeServiceUsed.length; k++) {
                tempServiceModelArrayList.add(new ServiceModel(ApiParams.AssetsUsed.rechargeServiceUsed[k], ApiParams.AssetsUsed.recharge_service_array[k]));
            }
            for (int u = 0; u < ApiParams.AssetsUsed.bookServiceUsed.length; u++) {
                tempBookServiceModelArrayList.add(new ServiceModel(ApiParams.AssetsUsed.bookServiceUsed[u], ApiParams.AssetsUsed.book_service_array[u]));
            }
            listing.put(0, tempServiceModelArrayList);
            listing.put(1, tempBookServiceModelArrayList);
            return listing;
        }

        @Override
        protected void onPostExecute(HashMap<Integer, ArrayList<ServiceModel>> aVoid) {
            RecyclerView rechargeServiceRecyclerView = (RecyclerView) ((Activity) weakActivity).findViewById(R.id.rechargeServiceRecyclerView);
            RecyclerView bookServiceRecyclerView = (RecyclerView) ((Activity) weakActivity).findViewById(R.id.bookServiceRecyclerView);
            rechargeServiceAdapter = new RechargeServiceAdapter(weakActivity, aVoid.get(0));
            rechargeServiceRecyclerView.setNestedScrollingEnabled(false);
            rechargeServiceRecyclerView.setLayoutManager(new GridLayoutManager(weakActivity, SPAN_COUNT));
            rechargeServiceRecyclerView.setAdapter(rechargeServiceAdapter);
            rechargeServiceAdapter.notifyDataSetChanged();
            bookServiceAdapter = new BookServiceAdapter(weakActivity, aVoid.get(1));
            bookServiceRecyclerView.setNestedScrollingEnabled(false);
            bookServiceRecyclerView.setLayoutManager(new GridLayoutManager(weakActivity, SPAN_COUNT));
            bookServiceRecyclerView.setAdapter(bookServiceAdapter);
            bookServiceAdapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
