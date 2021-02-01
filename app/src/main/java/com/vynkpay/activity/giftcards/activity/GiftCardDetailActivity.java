package com.vynkpay.activity.giftcards.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.giftcards.adapter.SlabAdapter;
import com.vynkpay.activity.giftcards.event.SlabPriceEvent;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;
import com.vynkpay.utils.URLS;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.reactivestreams.Publisher;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class GiftCardDetailActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    Dialog dialog;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.giftImage)
    ImageView giftImage;
    @BindView(R.id.slabRecyclerView)
    RecyclerView slabRecyclerView;
    @BindView(R.id.etFirstName)
    NormalEditText etFirstName;
    @BindView(R.id.etLastName)
    NormalEditText etLastName;
    @BindView(R.id.etEmail)
    NormalEditText etEmail;
    @BindView(R.id.etMobileNumber)
    NormalEditText etMobileNumber;
    @BindView(R.id.etMessage)
    NormalEditText etMessage;
    @BindView(R.id.etAmount)
    NormalEditText etAmount;
    @BindView(R.id.continueButton)
    NormalButton continueButton;
    @BindView(R.id.readDescriptionButton)
    NormalButton readDescriptionButton;
    @BindView(R.id.slabLayout)
    LinearLayout slabLayout;
    String access_token;
    DisposableSubscriber<JSONObject> d1;

    String product_id;
    String product_name;
    String product_image;
    private JSONObject jsonObject;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (d1 != null) {
            d1.dispose();
        }

        EventBus.getDefault().unregister(this);
    }

    private JSONObject temp_JSON;
    SlabAdapter slabAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_detail_giftcard_rcg);
        ButterKnife.bind(GiftCardDetailActivity.this);
        EventBus.getDefault().register(this);
        access_token = M.fetchUserTrivialInfo(GiftCardDetailActivity.this, ApiParams.access_token);
        dialog = M.showDialog(GiftCardDetailActivity.this, "", true, true);
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

        readDescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GiftCardDetailActivity.this, GiftCardDescription.class);
                intent.putExtra("data", temp_JSON.toString());
                startActivity(intent);
            }
        });

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                M.removeZero(s);
            }
        });

        etMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                M.removeZero(s);
            }
        });


    }

    Intent intent;

    private void setListeners() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etFirstName.length() == 0 || etFirstName.getText().toString().trim().length() == 0) {
                    Toast.makeText(GiftCardDetailActivity.this, getString(R.string.valid_first_name), Toast.LENGTH_SHORT).show();
                } else if (etLastName.length() == 0 || etLastName.getText().toString().trim().length() == 0) {
                    Toast.makeText(GiftCardDetailActivity.this, getString(R.string.valid_last_name), Toast.LENGTH_SHORT).show();
                } else if (etEmail.length() == 0 || etEmail.getText().toString().trim().length() == 0 || !M.validateEmail(etEmail.getText().toString().trim())) {
                    Toast.makeText(GiftCardDetailActivity.this, getString(R.string.valid_email), Toast.LENGTH_SHORT).show();
                } else if (etMobileNumber.length() == 0 || etMobileNumber.getText().toString().trim().length() == 0) {
                    Toast.makeText(GiftCardDetailActivity.this, getString(R.string.valid_phone), Toast.LENGTH_SHORT).show();
                } else {
                    if (price_type.toLowerCase().equals("slab")) {
                        if (etAmount.length() == 0 || etAmount.getText().toString().trim().length() == 0) {
                            Toast.makeText(GiftCardDetailActivity.this, getString(R.string.select_amount), Toast.LENGTH_SHORT).show();
                        } else {
                            intent = new Intent(GiftCardDetailActivity.this, GiftCardPayConfirmation.class);
                            intent.putExtra("id", product_id);
                            intent.putExtra("name", product_name);
                            intent.putExtra("image", product_image);
                            intent.putExtra("firstName", etFirstName.getText().toString());
                            intent.putExtra("lastNAme", etLastName.getText().toString());
                            intent.putExtra("email", etEmail.getText().toString());
                            intent.putExtra("mobile", etMobileNumber.getText().toString());
                            intent.putExtra("giftName", name);
                            intent.putExtra("giftCode", sku);
                            intent.putExtra("amount", etAmount.getText().toString());
                            intent.putExtra("message", etMessage.getText().toString().length() == 0 || etMessage.getText().toString().trim().length() == 0 ? "" : etMessage.getText().toString());
                            startActivity(intent);
                        }
                    } else if (price_type.toLowerCase().equals("range")) {
                        if (etAmount.length() == 0 || etAmount.getText().toString().trim().length() == 0) {
                            Toast.makeText(GiftCardDetailActivity.this, getString(R.string.valid_amount), Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(etAmount.getText().toString()) > Integer.parseInt(max_custom_price) || Integer.parseInt(etAmount.getText().toString()) < Integer.parseInt(min_custom_price)) {
                            Toast.makeText(GiftCardDetailActivity.this, getString(R.string.range) + " " +
                                    Functions.CURRENCY_SYMBOL + " " + min_custom_price + "-" + Functions.CURRENCY_SYMBOL + " " + max_custom_price, Toast.LENGTH_LONG).show();
                        } else {
                            intent = new Intent(GiftCardDetailActivity.this, GiftCardPayConfirmation.class);
                            intent.putExtra("id", product_id);
                            intent.putExtra("name", product_name);
                            intent.putExtra("image", product_image);
                            intent.putExtra("firstName", etFirstName.getText().toString());
                            intent.putExtra("lastNAme", etLastName.getText().toString());
                            intent.putExtra("email", etEmail.getText().toString());
                            intent.putExtra("mobile", etMobileNumber.getText().toString());
                            intent.putExtra("giftName", name);
                            intent.putExtra("giftCode", sku);
                            intent.putExtra("amount", etAmount.getText().toString());
                            intent.putExtra("message", etMessage.getText().toString().length() == 0 || etMessage.getText().toString().trim().length() == 0 ? "" : etMessage.getText().toString());
                            startActivity(intent);
                        }
                    }

                }
            }
        });
    }

    String price_type;
    String min_custom_price;
    String max_custom_price;
    String name;
    String sku;

    private void handleOperatorResponse(JSONObject jsonObject) {
        try {
            JSONObject product_details = jsonObject.getJSONObject("data").getJSONObject("product_details");
            price_type = product_details.getString("price_type");
            name = product_details.getString("name");
            sku = product_details.getString("sku");
            String custom_denominations = product_details.getString("custom_denominations");
            min_custom_price = product_details.getString("min_custom_price");
            max_custom_price = product_details.getString("max_custom_price");
            if (price_type.toLowerCase().equals("slab")) {
                etAmount.setEnabled(false);
                slabLayout.setVisibility(View.VISIBLE);
            } else if (price_type.toLowerCase().equals("range")) {
                etAmount.setEnabled(true);
                etAmount.setHint(min_custom_price + "-" + max_custom_price);
                slabLayout.setVisibility(View.GONE);
            }
            if (!custom_denominations.equals("null")) {
                List<String> priceList = (Arrays.asList(custom_denominations.split(",")));
                if (priceList.size() == 0) {
                    slabLayout.setVisibility(View.GONE);
                } else {
                    slabLayout.setVisibility(View.VISIBLE);
                }
                slabAdapter = new SlabAdapter(GiftCardDetailActivity.this, priceList);
                slabRecyclerView.setLayoutManager(M.horizontalRecyclerView(GiftCardDetailActivity.this));
                slabRecyclerView.setNestedScrollingEnabled(false);
                slabAdapter.setHasStableIds(true);
                slabRecyclerView.setAdapter(slabAdapter);
                slabAdapter.notifyDataSetChanged();
            }
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
        String Url = BuildConfig.APP_BASE_URL + URLS.giftCardProductDetail + "?product_id=" + product_id;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Url, null, future, null);
        MySingleton.getInstance(GiftCardDetailActivity.this).addToRequestQueue(req);
        req.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return future.get();
    }


    String FETCHED_AMOUNT;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onEvent(SlabPriceEvent event) {
        if (event != null) {
            FETCHED_AMOUNT = event.getPrice();
            etAmount.setText(FETCHED_AMOUNT);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(GiftCardDetailActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(GiftCardDetailActivity.this,GiftCardDetailActivity.this::finishAffinity);
        }
    }
}
