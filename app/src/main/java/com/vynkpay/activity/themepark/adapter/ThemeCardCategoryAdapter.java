package com.vynkpay.activity.themepark.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.themepark.activity.ThemeParkSingleActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.GiftCardListModel;
import com.vynkpay.models.GiftCardModel;
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
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ThemeCardCategoryAdapter extends RecyclerView.Adapter<ThemeCardCategoryAdapter.MyViewHolder> {
    Context context;
    private RecyclerView.RecycledViewPool viewPool;
    ArrayList<GiftCardModel> giftCardModelArrayList;

    public ThemeCardCategoryAdapter(Context context, ArrayList<GiftCardModel> giftCardModelArrayList) {
        this.context = context;
        this.giftCardModelArrayList = giftCardModelArrayList;
        viewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_gift_card_rcg, parent, false);
        MyViewHolder m = new MyViewHolder(v);
        m.innerRecyclerView.setRecycledViewPool(viewPool);
        return m;
    }

    DisposableSubscriber<JSONObject> d1;
    private JSONObject temp_JSON;

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.categoryTitle.setText(giftCardModelArrayList.get(position).getCategoryName());
        holder.seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ThemeParkSingleActivity.class);
                intent.putExtra("category_id", giftCardModelArrayList.get(position).getCategoryId());
                intent.putExtra("category_name", giftCardModelArrayList.get(position).getCategoryName());
                ((Activity) context).startActivity(intent);
            }
        });

        d1 = new DisposableSubscriber<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
                temp_JSON = jsonObject;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                handleOperatorResponse(temp_JSON, holder);
            }
        };

        newGetOperatorData(giftCardModelArrayList.get(position).getCategoryId(), holder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d1);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (d1 != null) {
            d1.dispose();
        }
    }

    private JSONObject jsonObject;


    private void handleOperatorResponse(JSONObject jsonObject, final MyViewHolder holder) {
        ArrayList<GiftCardListModel> giftCardListModelArrayList = new ArrayList<>();
        this.jsonObject = jsonObject;
        Log.i(">>help", "handleOperatorResponse: " + this.jsonObject.toString());
        try {
            JSONArray jsonArray = this.jsonObject.getJSONArray(ApiParams.data);
            for (int k = 0; k < jsonArray.length(); k++) {
                GiftCardListModel giftCardListModel = new GiftCardListModel();
                JSONObject jsonObject1 = jsonArray.getJSONObject(k);
                String name = jsonObject1.getString("name");
                String id = jsonObject1.getString("product_id");
                String image = jsonObject1.getString("image");
                giftCardListModel.setCategoryId(id);
                giftCardListModel.setCategoryName(name);
                giftCardListModel.setImage(BuildConfig.ImageBaseURl + image.replaceAll("\\\\", ""));
                giftCardListModelArrayList.add(giftCardListModel);
            }

            ThemeParkCategoryListingAdapter giftCardCategoryListingAdapter = new ThemeParkCategoryListingAdapter(context, giftCardListModelArrayList);
            holder.innerRecyclerView.setLayoutManager(M.horizontalRecyclerView(context));
            holder.innerRecyclerView.setAdapter(giftCardCategoryListingAdapter);
            giftCardCategoryListingAdapter.setHasStableIds(true);
            giftCardCategoryListingAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.i(">>log", "handleOperatorResponse: ");
        }


    }

    public Flowable<JSONObject> newGetOperatorData(final String categoryId, final MyViewHolder holder) {
        return Flowable.defer(new Callable<Publisher<? extends JSONObject>>() {
            @Override
            public Publisher<? extends JSONObject> call() throws Exception {
                return Flowable.just(getOperatorData(categoryId, holder));
            }
        });
    }

    private JSONObject getOperatorData(String categoryId, MyViewHolder holder) throws ExecutionException, InterruptedException, RuntimeException {
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        String Url = BuildConfig.APP_BASE_URL + URLS.themeParkProducts + "?category_id=" + categoryId;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Url, null, future, null);
        MySingleton.getInstance(context).addToRequestQueue(req);
        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return future.get();
    }

    @Override
    public int getItemCount() {
        return giftCardModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView innerRecyclerView;
        NormalTextView categoryTitle;
        NormalButton seeAllButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            innerRecyclerView = itemView.findViewById(R.id.innerRecyclerView);
            seeAllButton = itemView.findViewById(R.id.seeAllButton);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);

        }
    }
}
