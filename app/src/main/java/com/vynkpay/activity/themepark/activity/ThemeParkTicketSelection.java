package com.vynkpay.activity.themepark.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.themepark.adapter.ThemeParkTicketAdapter;
import com.vynkpay.activity.themepark.event.IsAgeLimitSelectedEvent;
import com.vynkpay.activity.themepark.event.IsTicketTypeSelectedEvent;
import com.vynkpay.activity.themepark.event.ThemeParkCategoryTicketEvent;
import com.vynkpay.activity.themepark.event.ThemeParkQuantityEvent;
import com.vynkpay.activity.themepark.event.ThemeParkTicketEvent;
import com.vynkpay.activity.themepark.model.ThemeParkCategoryTicketModel;
import com.vynkpay.activity.themepark.model.ThemeParkTicketModel;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;
import com.vynkpay.utils.URLS;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemeParkTicketSelection extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    Dialog dialog;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ticketTypeRecyclerView)
    RecyclerView ticketTypeRecyclerView;
    @BindView(R.id.selectedDate)
    NormalTextView tvSelectedDate;
    @BindView(R.id.changeDateLayout)
    FrameLayout changeDateLayout;
    @BindView(R.id.bookButton)
    NormalButton bookButton;
    @BindView(R.id.noTicketLayout)
    LinearLayout noTicketLayout;
    @BindView(R.id.ticketLayout)
    LinearLayout ticketLayout;
    String access_token, productId, selectDate;
    ThemeParkTicketAdapter themeParkTicketAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_select_ticket_rcg);
        ButterKnife.bind(ThemeParkTicketSelection.this);
        EventBus.getDefault().register(this);
        access_token = M.fetchUserTrivialInfo(ThemeParkTicketSelection.this, ApiParams.access_token);
        dialog = M.showDialog(ThemeParkTicketSelection.this, "", true, true);
        setListeners();
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title.setText("Select tickets");
        productId = getIntent().getStringExtra("id");
        selectDate = getIntent().getStringExtra("date");
        tvSelectedDate.setText(M.changeDateFormat(selectDate + " 00:45:51"));

        makeTicketRequest();
    }

    public void setListeners() {
        changeDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTicketTypeSelected) {
                    Toast.makeText(ThemeParkTicketSelection.this, "Please select a ticket first", Toast.LENGTH_SHORT).show();
                } else if (!isAgeLimitSelected) {
                    Toast.makeText(ThemeParkTicketSelection.this, "Please select a valid type from ticket", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ThemeParkTicketSelection.this, ThemeParkFillInfoActivity.class);
                    intent.putExtra("themeParkModel", themeParkTicketModel);
                    intent.putExtra("themeParkCategoryModel", themeParkCategoryTicketModel);
                    intent.putExtra("product_id", productId);
                    intent.putExtra("quantity", quantity);
                    intent.putExtra("calculatedPrice", calculatedPrice);
                    intent.putExtra("originalPrice", originalPrice);
                    startActivity(intent);
                }

            }
        });
    }

    private void selectDate() {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(ThemeParkTicketSelection.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                /*      Your code   to get date and time    */
                selectedmonth = selectedmonth + 1;
                if ((selectedday < 9 && selectedmonth < 9)) {
                    selectDate = (selectedyear + "-0" + selectedmonth + "-0" + selectedday);
                } else if ((selectedday < 9 || selectedmonth < 9)) {
                    if (selectedday < 9) {
                        selectDate = (selectedyear + "-" + selectedmonth + "-0" + selectedday);
                    } else {
                        selectDate = (selectedyear + "-0" + selectedmonth + selectedday);
                    }
                } else {
                    selectDate = ("" + selectedyear + "-" + selectedmonth + "-" + selectedday);
                }
                tvSelectedDate.setText(M.changeDateFormat(selectDate + " 00:45:22"));
                makeTicketRequest();

            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date for availability");
        mDatePicker.show();
        mDatePicker.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    mDatePicker.dismiss();
                }
            }
        });
    }

    ArrayList<ThemeParkTicketModel> themeParkTicketModelArrayList = new ArrayList<>();
    ArrayList<ThemeParkCategoryTicketModel> subCategoryList;

    ThemeParkTicketModel themeParkTicketModel;
    ThemeParkCategoryTicketModel themeParkCategoryTicketModel;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onEvent(ThemeParkTicketEvent themeParkTicketModel) {
        if (themeParkTicketModel != null) {
            this.themeParkTicketModel = themeParkTicketModel.getThemeParkTicketModel();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onEvent(ThemeParkCategoryTicketEvent themeParkCategoryTicketModel) {
        if (themeParkCategoryTicketModel != null) {
            this.themeParkCategoryTicketModel = themeParkCategoryTicketModel.getThemeParkTicketModel();
        }
    }

    boolean isAgeLimitSelected = false, isTicketTypeSelected = false;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onEvent(IsTicketTypeSelectedEvent themeParkCategoryTicketModel) {

        isTicketTypeSelected = themeParkCategoryTicketModel.isTicketTypeSelected();

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onEvent(IsAgeLimitSelectedEvent themeParkCategoryTicketModel) {

        isAgeLimitSelected = themeParkCategoryTicketModel.isAgeLimitSelectedEvent();

    }


    String quantity, calculatedPrice, originalPrice;

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onEvent(ThemeParkQuantityEvent themeParkQuantityEvent) {
        quantity = themeParkQuantityEvent.getQuantity();
        calculatedPrice = themeParkQuantityEvent.getCalculatedPrice() + "";
        originalPrice = themeParkQuantityEvent.getOriginalPrice() + "";
    }

    String mSuccess, mMessage;

    private void makeTicketRequest() {
        themeParkTicketModelArrayList.clear();
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.themeParkTicketAvailability + "?product_id=" + productId + "&date_of_tour=" + selectDate,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>billFetchResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString("success");
                            mMessage = jsonObject.optString("message");
                            if (mSuccess.equalsIgnoreCase("true")) {
                                bookButton.setAlpha(1.0f);
                                bookButton.setEnabled(true);
                                noTicketLayout.setVisibility(View.GONE);
                                ticketLayout.setVisibility(View.VISIBLE);
                                JSONArray tour_options = jsonObject.getJSONObject(ApiParams.data).getJSONObject("product_details").getJSONObject(ApiParams.data).getJSONArray("tour_options");
                                String visitDate = jsonObject.getJSONObject(ApiParams.data).getJSONObject("product_details").getJSONObject(ApiParams.data).getJSONObject("availabilities").getString("visit_date");
                                for (int k = 0; k < tour_options.length(); k++) {
                                    ThemeParkTicketModel themeParkTicketModel = new ThemeParkTicketModel();
                                    subCategoryList = new ArrayList<>();
                                    JSONObject jsonObject1 = tour_options.getJSONObject(k);
                                    String id = jsonObject1.getString("id");
                                    String title = jsonObject1.getString("title");
                                    themeParkTicketModel.setId(id);
                                    themeParkTicketModel.setTitle(title);
                                    themeParkTicketModel.setVisitDate(visitDate);
                                    JSONArray categories = jsonObject1.getJSONObject("pricing").getJSONArray("categories");
                                    if (categories != null && categories.length() > 0) {
                                        for (int cat = 0; cat < categories.length(); cat++) {
                                            ThemeParkCategoryTicketModel themeParkCategoryTicketModel = new ThemeParkCategoryTicketModel();
                                            JSONObject ticketCategoryData = categories.getJSONObject(cat);
                                            String categoryID = ticketCategoryData.getString("id");
                                            String name = ticketCategoryData.getString("name");
                                            String min_age = ticketCategoryData.getString("min_age");
                                            String max_age = ticketCategoryData.getString("max_age");
                                            themeParkCategoryTicketModel.setTiketCategoryId(categoryID);
                                            themeParkCategoryTicketModel.setTiketCategoryMinAge(min_age);
                                            themeParkCategoryTicketModel.setTiketCategoryMaxAge(max_age);
                                            themeParkCategoryTicketModel.setTicketCategoryName(name);
                                            JSONArray scale = ticketCategoryData.getJSONArray("scale");
                                            if (scale != null && scale.length() > 0) {
                                                JSONObject scalingObject = scale.getJSONObject(0);
                                                String scale_min_participant = scalingObject.getString("scale_min_participant");
                                                String scale_max_participant = scalingObject.getString("scale_max_participant");
                                                String price = scalingObject.getString("price");
                                                themeParkCategoryTicketModel.setTiketCategoryMin(scale_min_participant);
                                                themeParkCategoryTicketModel.setTiketCategoryMax(scale_max_participant);
                                                themeParkCategoryTicketModel.setTiketCategoryPrice(price);
                                            }
                                            subCategoryList.add(themeParkCategoryTicketModel);
                                        }
                                    }
                                    themeParkTicketModel.setSubCategoryData(subCategoryList);
                                    themeParkTicketAdapter = new ThemeParkTicketAdapter(ThemeParkTicketSelection.this, themeParkTicketModelArrayList);
                                    themeParkTicketAdapter.reset();
                                    ticketTypeRecyclerView.setLayoutManager(new LinearLayoutManager(ThemeParkTicketSelection.this));
                                    ticketTypeRecyclerView.setAdapter(themeParkTicketAdapter);
                                    themeParkTicketModelArrayList.add(themeParkTicketModel);
                                }
                            } else {
                                noTicketLayout.setVisibility(View.VISIBLE);
                                ticketLayout.setVisibility(View.GONE);
                                bookButton.setAlpha(.5f);
                                bookButton.setEnabled(false);
                            }
                        } catch (Exception e) {
                            noTicketLayout.setVisibility(View.VISIBLE);
                            ticketLayout.setVisibility(View.GONE);
                            bookButton.setAlpha(.5f);
                            bookButton.setEnabled(false);
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        noTicketLayout.setVisibility(View.VISIBLE);
                        ticketLayout.setVisibility(View.GONE);
                        bookButton.setAlpha(.5f);
                        bookButton.setEnabled(false);
                        dialog.dismiss();
                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, access_token);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(stringRequest);
        //MySingleton.getInstance(ThemeParkTicketSelection.this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(ThemeParkTicketSelection.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(ThemeParkTicketSelection.this,ThemeParkTicketSelection.this::finishAffinity);
        }
    }
}

