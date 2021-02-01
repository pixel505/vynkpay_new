package com.vynkpay.activity.themepark.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;
import com.vynkpay.utils.URLS;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ThemeParkDetailActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    Dialog dialog;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.giftImage)
    ImageView giftImage;
    @BindView(R.id.privacyPolicy)
    NormalTextView privacyPolicy;
    @BindView(R.id.refundPolicy)
    NormalTextView refundPolicy;
    @BindView(R.id.descriptionText)
    NormalTextView descriptionText;
    @BindView(R.id.checkTicketAvailability)
    Button checkTicketAvailability;
    String access_token;
    DisposableSubscriber<JSONObject> d1;
    String product_id;
    String product_name;
    String product_image;

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
        setContentView(R.layout.activity_detail_theme_park_rcg);
        ButterKnife.bind(ThemeParkDetailActivity.this);
        access_token = M.fetchUserTrivialInfo(ThemeParkDetailActivity.this, ApiParams.access_token);
        dialog = M.showDialog(ThemeParkDetailActivity.this, "", true, true);
        setListeners();
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        product_id = getIntent().getStringExtra("id");
        product_name = getIntent().getStringExtra("name");
        product_image = getIntent().getStringExtra("image");
        toolbar_title.setText(product_name);
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
        Picasso.get().load(product_image).into(giftImage);
    }

    private void setListeners() {
        checkTicketAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
    }

    private void selectDate() {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(ThemeParkDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                /*      Your code   to get date and time    */
                selectedmonth = selectedmonth + 1;
                if ((selectedday < 9 && selectedmonth < 9)) {
                    _AVAILABILITY = (selectedyear + "-0" + selectedmonth + "-0" + selectedday);
                } else if ((selectedday < 9 || selectedmonth < 9)) {
                    if (selectedday < 9) {
                        _AVAILABILITY = (selectedyear + "-" + selectedmonth + "-0" + selectedday);
                    } else {
                        _AVAILABILITY = (selectedyear + "-0" + selectedmonth + selectedday);
                    }
                } else {
                    _AVAILABILITY = ("" + selectedyear + "-" + selectedmonth + "-" + selectedday);
                }
                Intent intent = new Intent(ThemeParkDetailActivity.this, ThemeParkTicketSelection.class);
                intent.putExtra("id", product_id);
                intent.putExtra("date", _AVAILABILITY);
                startActivity(intent);


            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date for availability");
        mDatePicker.show();
    }

    String _AVAILABILITY;
    String price;
    String country;
    String city;
    String name;
    String description;

    private void handleOperatorResponse(JSONObject jsonObject) {
        try {
            Log.i(">>response", "handleOperatorResponse: " + jsonObject.toString());
            JSONObject product_details = jsonObject.getJSONObject("data");
            price = product_details.getString("price");
            name = product_details.getString("name");
            country = product_details.getString("country");
            city = product_details.getString("city");
            description = product_details.getString("description");
            descriptionText.setText(description);
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
        Log.i(">>response", "getOperatorData: " + "\n" + product_id);
        String Url = BuildConfig.APP_BASE_URL + URLS.themeParkProductDetail + "?product_id=" + product_id;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Url, null, future, null);
        MySingleton.getInstance(ThemeParkDetailActivity.this).addToRequestQueue(req);
        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return future.get();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(ThemeParkDetailActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(ThemeParkDetailActivity.this,ThemeParkDetailActivity.this::finishAffinity);
        }
    }
}
