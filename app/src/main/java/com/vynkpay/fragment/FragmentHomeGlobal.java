package com.vynkpay.fragment;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appersiano.progressimage.ProgressImage;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.highsoft.highcharts.common.HIColor;
import com.highsoft.highcharts.common.hichartsclasses.HIArea;
import com.highsoft.highcharts.common.hichartsclasses.HICSSObject;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HICredits;
import com.highsoft.highcharts.common.hichartsclasses.HIExporting;
import com.highsoft.highcharts.common.hichartsclasses.HILabels;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions;
import com.highsoft.highcharts.common.hichartsclasses.HISeries;
import com.highsoft.highcharts.common.hichartsclasses.HISpline;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.common.hichartsclasses.HITooltip;
import com.highsoft.highcharts.common.hichartsclasses.HIXAxis;
import com.highsoft.highcharts.common.hichartsclasses.HIYAxis;
import com.highsoft.highcharts.core.HIChartView;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.adapter.ImageAdapter;
import com.vynkpay.adapter.ImportantAdapter;
import com.vynkpay.custom.NormalBoldTextView;
import com.vynkpay.custom.NormalLightTextView;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.BannerModel;
import com.vynkpay.models.CartHighResponse;
import com.vynkpay.models.PDFFileModel;
import com.vynkpay.models.StatiaticsResponse;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetNonWalletResponse;
import com.vynkpay.utils.Functions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.pzienowicz.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHomeGlobal extends Fragment {

    @BindView(R.id.bonusWalletText)
    TextView bonusWalletText;
    @BindView(R.id.vCashWalletText)
    TextView vCashWalletText;
    @BindView(R.id.mCashWalletText)
    TextView mCashWalletText;
    @BindView(R.id.bonusLinear)
    LinearLayout bonusLinear;
    @BindView(R.id.mCashLinear)
    LinearLayout mCashLinear;
    @BindView(R.id.vCashLinear)
    LinearLayout vCashLinear;
    @BindView(R.id.viewPager)
    AutoScrollViewPager viewPager;
    @BindView(R.id.tabLayout)
    DotsIndicator tabLayout;
    @BindView(R.id.globalImage)
    CircleImageView globalImage;
    @BindView(R.id.globalUserId)
    NormalTextView globalUserId;
    @BindView(R.id.globalUserName)
    NormalBoldTextView globalUserName;
    @BindView(R.id.globalEmail)
    NormalLightTextView globalEmail;

    @BindView(R.id.tvBCash)
    NormalTextView tvBCash;

    @BindView(R.id.linImportantStats)
    LinearLayout linImportantStats;

    @BindView(R.id.crdBouns)
    CardView crdBouns;

    @BindView(R.id.rvListImportant)
    RecyclerView rvListImportant;

    @BindView(R.id.linChartt)
    RelativeLayout linChartt;

    @BindView(R.id.hiChart)
    HIChartView hiChart;

    @BindView(R.id.tvEarning)
    NormalTextView tvEarning;

    @BindView(R.id.frmFilter)
    FrameLayout frmFilter;

    @BindView(R.id.filterRange)
    NormalTextView filterRange;

    @BindView(R.id.chart1)
    LineChart chart;

    @BindView(R.id.lintv)
    LinearLayout lintv;

    @BindView(R.id.tvProgressTV)
    NormalTextView tvProgressTV;

    @BindView(R.id.progressView)
    ProgressImage progressView;

    @BindView(R.id.tvChartTitle)
    NormalTextView tvChartTitle;

    String bonusBalance,vCashBalance,mCashBalance;
    Activity activity;
    String imageURL="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_home_global, container, false);
        ButterKnife.bind(this, view);

        getBonusTransaction();
        getVCashTransaction();
        getMCashTransaction();

        rvListImportant.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));

        lintv.setVisibility(View.GONE);

        if (Prefes.getAccessToken(activity).equals("")) {
            globalUserId.setText("UserId :");
            globalUserName.setText("Username");
            globalEmail.setText("VynkPay Email");
            globalImage.setImageResource(R.drawable.dummy);

        } else {
                Log.e("name",""+Prefes.getName(activity));
                Log.e("name",""+Prefes.getUserName(activity));


             globalUserId.setText("Username :"+Prefes.getUserName(activity));
             globalEmail.setText(Prefes.getEmail(activity));
             globalUserName.setText(Prefes.getName(activity));
             globalImage.setImageResource(R.drawable.dummy);

            if(Prefes.getImage(activity).isEmpty()){
                globalImage.setImageResource(R.drawable.dummy);
            }

            else{
                //Picasso.get().load(BuildConfig.BASE_URL + Prefes.getImage(activity)).into(globalImage);
                Glide.with(activity).load(BuildConfig.BASE_URL + Prefes.getImage(activity)).into(globalImage);
                String imagepath = BuildConfig.BASE_URL + Prefes.getImage(activity);
                Log.e("imageeeeeeeeeeeeeeee", "" + imagepath);
            }


        }

        if (Prefes.getUserType(activity).equalsIgnoreCase("2")) {
            linChartt.setVisibility(View.GONE);
            linImportantStats.setVisibility(View.GONE);
        } else {
            linChartt.setVisibility(View.GONE);
            linImportantStats.setVisibility(View.VISIBLE);
        }

        getUserDetail();

        if (Prefes.getUserType(activity).equalsIgnoreCase("2")){
            //crdBouns.setVisibility(View.GONE);
            tvBCash.setText("Cashback");
        }else {
            //crdBouns.setVisibility(View.VISIBLE);
            tvBCash.setText("Earning Wallet");
        }

        bonusLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefes.getAccessToken(activity).equals("")) {
                    startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    startActivity(new Intent(activity, BonusWalletFragment.class).putExtra("balancWallet",bonusBalance));
                }
            }
        });
        mCashLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefes.getAccessToken(activity).equals("")) {
                    startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    startActivity(new Intent(activity,MCashWalletFragment.class).putExtra("balancWalletM",vCashBalance));
                }
            }
        });
        vCashLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Prefes.getAccessToken(activity).equals("")) {
                    startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    startActivity(new Intent(activity,VCashWalletFragment.class).putExtra("balancWalletV",mCashBalance));
                }
            }
        });

        //frmFilter.setVisibility(View.GONE);
        frmFilter.setOnClickListener(v -> {
            filterRange.setBackgroundResource(R.drawable.filter_back);
            filterRange.setTextColor(Color.parseColor("#ffffff"));
            PopupMenu popup = new PopupMenu(activity, v);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    //Toast.makeText(activity,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    if (item.getTitle().toString().equalsIgnoreCase("Yearly")){
                        getLineChart("year");
                        tvEarning.setText("Yearly Stats...");
                    } else if (item.getTitle().toString().equalsIgnoreCase("Monthly")){
                        getLineChart("month");
                        tvEarning.setText("Monthly Stats...");
                    } else if (item.getTitle().toString().equalsIgnoreCase("Day")){
                        getLineChart("day");
                        tvEarning.setText("Daily Stats...");
                    }
                    return true;
                }
            });

            popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu menu) {
                    filterRange.setBackgroundResource(R.drawable.filter_stroke);
                    filterRange.setTextColor(Color.parseColor("#000000"));
                }
            });

            popup.show();//showing popup menu
        });

        getBanners();

        setChart();

        return view;
    }

    void setChart(){
        chart.setViewPortOffsets(0, 0, 0, 0);
        //chart.setBackgroundResource(R.drawable.logo);
        //chart.setBackgroundColor(Color.rgb(104, 241, 175));
        chart.setBackgroundColor(Color.parseColor("#30DCDCDC"));
        // no description text
        chart.getDescription().setEnabled(false);
        // enable touch gestures
        chart.setTouchEnabled(false);
        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(200);
        XAxis x = chart.getXAxis();
        x.setEnabled(false);

        YAxis y = chart.getAxisLeft();
        y.setDrawLabels(false);
        y.setTypeface(Typeface.createFromAsset(activity.getAssets(),"fonts/ITC_MEDIUM.ttf"));
        y.setLabelCount(5, false);
        y.setAxisMaximum(11000);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.TRANSPARENT);

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);

        // add data
        //seekBarY.setOnSeekBarChangeListener(this);
        //seekBarX.setOnSeekBarChangeListener(this);

        // lower max, as cubic runs significantly slower than linear
        //seekBarX.setMax(700);

        //seekBarX.setProgress(45);
        //seekBarY.setProgress(100);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        chart.animateXY(2000, 2000);
        //setupGradient(chart);

        // don't forget to refresh the drawing
        chart.invalidate();

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (getView()!=null) {
                getView().post(() -> setupGradient(chart));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void setupGradient(LineChart mChart) {
        Paint paint = mChart.getRenderer().getPaintRender();
        int height = mChart.getHeight();

        LinearGradient linGrad = new LinearGradient(0, 0, 0, height,
                getResources().getColor(R.color.black),
                getResources().getColor(R.color.colorPrimary),
                Shader.TileMode.REPEAT);
        paint.setShader(linGrad);
    }

    private void setData(int count, float range,CartHighResponse dataResp) {

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        for (int i=0;i<dataResp.getData().getJsonval().getSeries().size();i++){

            ArrayList<Entry> series = new ArrayList();
            for (int j=0;j<dataResp.getData().getJsonval().getSeries().get(i).getData().size();j++){
                series.add(new Entry(j,Float.valueOf(dataResp.getData().getJsonval().getSeries().get(i).getData().get(j))));
            }
           /* set1 = new LineDataSet(series, "DataSet "+i);
            if (chart.getData() != null &&
                    chart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) chart.getData().getDataSetByIndex(i);
                set1.setValues(series);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {*/
            // create a dataset and give it a type
            LineDataSet set1 = new LineDataSet(series, dataResp.getData().getJsonval().getSeries().get(i).getName());

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            //set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(5f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.WHITE);
            //set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setGradientColor(Color.BLACK,Color.WHITE);
            set1.setColor(/*Color.parseColor("#B10D25")*/Color.parseColor(dataResp.getData().getJsonval().getSeries().get(i).getColor()));
            set1.setFillColor(Color.WHITE);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);

            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            dataSets.add(set1);


            /* }*/
        }

        // create a data object with the data sets
        LineData data = new LineData(dataSets);
        data.setValueTypeface(Typeface.createFromAsset(activity.getAssets(),"fonts/ITC_MEDIUM.ttf"));
        data.setValueTextSize(5f);
        data.setDrawValues(false);

        // set data
        setupGradient(chart);
        chart.setData(data);
        chart.invalidate();
        /*ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * (range + 1)) + 20;
            values.add(new Entry(i, val));
        }*/
    }





    public void getUserDetail() {
        ApiCalls.getUserDetails(activity,Prefes.getAccessToken(getActivity()),new VolleyResponse() {
            @Override
            public void onResult (String result, String status, String message){
                Log.d("userDwetailssssa", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("success").equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        Functions.CURRENCY_SYMBOL = data.getString("currency_symbol");
                        Log.e("curr", "" + Functions.CURRENCY_SYMBOL);
                        Log.e("curr", "" + data.getString("is_indian"));

                        if (data.has("username")) {
                            String username = data.getString("username");
                            //Prefes.saveUserName(username, HomeActivity.this);
                        }
                        if (data.getString("is_indian").equalsIgnoreCase("YES")) {
                            //indian
                            Functions.isIndian = true;

                        } else {
                            //foreigner
                            Functions.isIndian = false;

                        }
                        imageURL = data.getString("image");
                        fetchWalletData();
                        if (Prefes.getUserType(activity).equalsIgnoreCase("2")) {
                            Log.d("usererre", "type2");
                        } else {
                            getDashboardData();
                            //getLineChart("month");
                        }


                    } else {
                        Log.d("userprofile","Error in getting");
                        fetchWalletData();
                        if (Prefes.getUserType(activity).equalsIgnoreCase("2")) {
                            Log.d("usererre", "type2");
                        } else {
                            getDashboardData();
                            //getLineChart("month");
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError (String error){
                Log.d("Error", error + "");
            }
        });
    }


    public void getLineChart(String chart_type){
        //month | year | day
        MainApplication.getApiService().getChartData(Prefes.getAccessToken(activity),chart_type).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    Log.d("chartDataResp",jsonObject.toString());

                    CartHighResponse data = new Gson().fromJson(jsonObject.toString(),CartHighResponse.class);
                    if (data.getStatus().equalsIgnoreCase("true")){
                        Log.d("chartDataResp1",new Gson().toJson(data));
                        //loadChart(cartHighResponse);
                        provideOptionsForChartType(data);
                        setData(0,0,data);
                        Log.d("setSeries",new Gson().toJson(data));
                        /*activity.runOnUiThread(() -> {*/

                        //HIChartView hiChart = view.findViewById(R.id.hiChart);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("chartDataResp",t.getMessage()!=null?t.getMessage():"Error");
            }
        });

    }


    public void provideOptionsForChartType(CartHighResponse data){
        //HIChartView chartView = (HIChartView) findViewById(R.id.hc);
        Log.d("setSeries",new Gson().toJson(data));
        /*activity.runOnUiThread(() -> {*/
        Double step = 1.0;

        Map<String, String> options = new HashMap<>();
        options.put("chartType", "spline");
        options.put("title", "title");
        options.put("subtitle", "subtitle");
        options.put("export", "false");

        HIOptions hiOptions = new HIOptions();

        HIExporting exporting = new HIExporting();
        exporting.setEnabled(false);
        hiOptions.setExporting(exporting);
        HIChart chart = new HIChart();
        chart.setType("spline");
        hiOptions.setChart(chart);

        HITitle title = new HITitle();
        title.setText("");

        hiOptions.setTitle(title);

        HIArea area = new HIArea();
        area.setFillOpacity(0.7);
        HIPlotOptions plotOptions = new HIPlotOptions();
        plotOptions.setSpline(new HISpline());
        plotOptions.setArea(area);
        plotOptions.getSpline().setColor(HIColor.initWithRGBA(0, 0, 0, 0.6));
        hiOptions.setPlotOptions(plotOptions);

        HICredits credits = new HICredits();
        credits.setEnabled(false);
        credits.setText("www.highcharts.com");
        HICSSObject creditsStyle = new HICSSObject();
        creditsStyle.setColor("rgba(255, 255, 255, 0.6)");
        credits.setStyle(creditsStyle);
        hiOptions.setCredits(credits);

        String[] categories = new String[]{"Jan-2020", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        HITooltip tooltip = new HITooltip();
        tooltip.setHeaderFormat("");
        hiOptions.setTooltip(tooltip);

        final HIXAxis xaxis = new HIXAxis();
        xaxis.setTickColor(HIColor.initWithRGBA(0, 0, 0, 0.0));
        xaxis.setLineColor(HIColor.initWithRGBA(0, 0, 0, 0.3));
        HILabels hiLabels = new HILabels();
        hiLabels.setText("Hello");
        hiLabels.setAllowOverlap(true);
        hiLabels.setStep(1);
        xaxis.setLabels(hiLabels);
        HICSSObject xLabelsStyle = new HICSSObject();
        xLabelsStyle.setColor("rgb(0, 0, 0)");
        xLabelsStyle.setFontFamily("Arial");
        xLabelsStyle.setFontSize("8px");
        xaxis.getLabels().setStyle(xLabelsStyle);
        xaxis.setCategories(new ArrayList<>(data.getData().getJsonval().getXAxis().getCategories()));
        xaxis.setBreaks(new ArrayList<>(Arrays.asList(categories)));
        //xaxis.setBreaks((ArrayList) data.getData().getJsonval().getXAxis().getCategories());
        hiOptions.setXAxis(new ArrayList<HIXAxis>(){{add(xaxis);}});

        //String[] axisy = new String[]{"0x", "5x", "10x", "15x"};

        final HIYAxis yaxis = new HIYAxis();
        yaxis.setLineWidth(1);
        yaxis.setGridLineWidth(0);
        yaxis.setLineColor(HIColor.initWithRGBA(0, 0, 0, 0.3));
        yaxis.setLabels(new HILabels());
        HICSSObject yLabelsStyle = new HICSSObject();
        yLabelsStyle.setColor("rgb(0, 0, 0)");
        yLabelsStyle.setFontFamily("Arial");
        yLabelsStyle.setFontSize("10px");
        yaxis.getLabels().setStyle(yLabelsStyle);
        //yaxis.getLabels().setX(-20);
        yaxis.getLabels().setStep(2);
        yaxis.getLabels().setX(-20);
        yaxis.setTitle(new HITitle());
        yaxis.getTitle().setText("");
        hiOptions.setYAxis(new ArrayList<HIYAxis>(){{add(yaxis);}});

        ArrayList<HISeries> arrayList =  new ArrayList<>();

        Log.d("setSeries",data.getData().getJsonval().getSeries().size()+"///");

        for (int i=0;i<data.getData().getJsonval().getSeries().size();i++){
            ArrayList<Double> series = new ArrayList();
            for (int j=0;j<data.getData().getJsonval().getSeries().get(i).getData().size();j++){
                series.add(Double.valueOf(data.getData().getJsonval().getSeries().get(i).getData().get(j)));
            }
            final HISpline spline = new HISpline();
            spline.setName(data.getData().getJsonval().getSeries().get(i).getName());
            spline.setTooltip(new HITooltip());
            spline.getTooltip().setHeaderFormat("");
            spline.getTooltip().setValueSuffix("");
            spline.setShowInLegend(true);
            spline.setLineWidth(Integer.parseInt(data.getData().getJsonval().getSeries().get(i).getLineWidth()));
            spline.setColor(HIColor.initWithName(data.getData().getJsonval().getSeries().get(i).getColor()));
            spline.setData(series);
            arrayList.add(spline);
        }

        //hiOptions.setSeries(new ArrayList<HISeries>(){{add(spline);}});
        hiOptions.setSeries(arrayList);
        //setData(hiOptions);
        hiChart.setOptions(hiOptions);
        hiChart.invalidate();
        hiChart.reload();
        Log.d("setSeries",arrayList.size()+"//==//");

        //hiChart.addView(hiChartView);
        Log.d("setSeries","calledd");
        /*});*/

        //return hiOptions;

    }





    private void getDashboardData(){
        MainApplication.getApiService().getDashboardData(Prefes.getAccessToken(activity)).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    Log.d("dashboardData",jsonObject.toString());
                    if (jsonObject.getString("status").equalsIgnoreCase("true")){
                        linImportantStats.setVisibility(View.VISIBLE);
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject statistics = data.getJSONObject("statistics");
                        String des_title = statistics.getString("des_title");
                        String des_img = statistics.getString("des_img");
                        String next_des_title = statistics.getString("next_des_title");
                        String next_des_img = statistics.getString("next_des_img");
                        String token_des_title = statistics.getString("token_des_title");
                        String token_des_img = statistics.getString("token_des_img");
                        String next_token_des_title = statistics.getString("next_token_des_title");
                        String next_token_des_img = statistics.getString("next_token_des_img");
                        String purchase_date = statistics.getString("purchase_date");
                        String purchase_amount = statistics.getString("purchase_amount");
                        String total_earning = statistics.getString("total_earning");
                        StatiaticsResponse statiaticsResponse = new StatiaticsResponse(des_title,des_img,next_des_title,next_des_img,token_des_title,token_des_img,next_token_des_title,next_token_des_img,purchase_date,purchase_amount,total_earning);
                        rvListImportant.setAdapter(new ImportantAdapter(activity,statiaticsResponse));

                        JSONObject vyncchartObject = data.getJSONObject("vync_chart");
                        String charttotal_earning = vyncchartObject.getString("total_earning");
                        String price_10x = vyncchartObject.getString("price_10x");
                        String total_percentage = vyncchartObject.getString("total_percentage");
                        String percentage = vyncchartObject.getString("percentage");
                        String chart_show = vyncchartObject.getString("chart_show");
                        String chart_title = vyncchartObject.getString("chart_title");
                        tvProgressTV.setText(percentage+"%");
                        tvChartTitle.setText(chart_title);

                        if (chart_show.equalsIgnoreCase("true")){
                            linChartt.setVisibility(View.VISIBLE);
                            try {
                                float percentagee = Float.parseFloat(percentage);
                                progressView.setProgress((int)percentagee);
                                progressView.invalidate();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            linChartt.setVisibility(View.GONE);

                        }

                    }else {
                        linImportantStats.setVisibility(View.GONE);
                        Log.d("dashboardData",jsonObject.getString("message"));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("dashboardData",t.getMessage()!=null?t.getMessage():"Error");
            }

        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    private void fetchWalletData() {
        MainApplication.getApiService().noindianWallet(Prefes.getAccessToken(activity)).enqueue(new Callback<GetNonWalletResponse>() {
            @Override
            public void onResponse(Call<GetNonWalletResponse> call, retrofit2.Response<GetNonWalletResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals("true")) {
                        bonusWalletText.setText(Functions.CURRENCY_SYMBOL + response.body().getData().getBonusWallet());
                        vCashWalletText.setText(Functions.CURRENCY_SYMBOL + response.body().getData().getMcashWallet());
                        Prefes.saveCash(response.body().getData().getMcashWallet(),activity);
                        mCashWalletText.setText(Functions.CURRENCY_SYMBOL + response.body().getData().getVCashWallet());
                        mCashBalance=response.body().getData().getVCashWallet();
                        vCashBalance=response.body().getData().getMcashWallet();
                        bonusBalance=response.body().getData().getBonusWallet();

                    }
                }else {
                }
            }
            @Override
            public void onFailure(Call<GetNonWalletResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Prefes.getAccessToken(activity).equals("")) {
            globalUserId.setText("UserId :");
            globalUserName.setText("Username");
            globalEmail.setText("VynkPay Email");
            globalImage.setImageResource(R.drawable.dummy);

        } else {
            globalUserId.setText("Username :"+Prefes.getUserName(activity));
            globalEmail.setText(Prefes.getEmail(activity));
            globalUserName.setText(Prefes.getName(activity));
            globalImage.setImageResource(R.drawable.dummy);

            if(Prefes.getImage(activity).isEmpty()){
                globalImage.setImageResource(R.drawable.dummy);
            } else{
                Picasso.get().load(BuildConfig.BASE_URL + Prefes.getImage(activity)).into(globalImage);
                String imagepath = BuildConfig.BASE_URL + Prefes.getImage(activity);
                Log.e("imageeeeeeeeeeeeeeee", "" + imagepath);
            }

        }
    }

    ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();

    private void getBanners() {
        bannerModelArrayList.clear();
        ApiCalls.getBanners(activity, Prefes.getAccessToken(activity),new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("sdasdasddsdasdas", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String banner_image = object.getString("banner_image");
                            String path = object.getString("path");
                            String banner_type = object.getString("banner_type");
                            String created_date = object.getString("created_date");
                            String full_banner_image = object.getString("full_banner_image");

                            bannerModelArrayList.add(new BannerModel(id, banner_image, path, banner_type, created_date, full_banner_image));
                        }

                        viewPager.setAdapter(new ImageAdapter(activity, bannerModelArrayList));

                        tabLayout.setViewPager(viewPager);

                        viewPager.startAutoScroll();
                        viewPager.setInterval(5000);
                        viewPager.setStopScrollWhenTouch(true);
                        viewPager.setBorderAnimation(true);
                        viewPager.setScrollDurationFactor(8);

                        JSONArray pdfJsonArray = jsonObject.getJSONArray("pdf");
                        for (int i = 0; i < pdfJsonArray.length(); i++) {
                            JSONObject object = pdfJsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String banner_image = object.getString("banner_image");
                            String path = object.getString("path");
                            String banner_type = object.getString("banner_type");
                            String file_recognize = object.getString("file_recognize");
                            String created_date = object.getString("created_date");
                            String full_banner_image = object.getString("full_banner_image");
                            String compPathOfPDF = path + banner_image;
                            Prefes.savePDFPath(compPathOfPDF, activity);
                            new PDFFileModel(id, banner_image, path, banner_type, file_recognize, created_date, full_banner_image);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.d("Error",error+"");
            }

        });
    }

    public void getBonusTransaction(){
        ApiCalls.getBonusTransactions(activity, Prefes.getAccessToken(activity), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("transactionZLog", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                        JSONArray listingArray = dataObject.getJSONArray("listing");
                        for (int i=0; i<listingArray.length(); i++){
                            JSONObject object=listingArray.getJSONObject(i);
                            String id = object.getString("id");
                            String front_user_id = object.getString("front_user_id");
                            String user_id  = object.getString("user_id");
                            String type  = object.getString("type");
                            String payment_via = object.getString("payment_via");
                            String p_amount  = object.getString("p_amount");
                            String profit_type = object.getString("profit_type");
                            String mode  = object.getString("mode");
                            String transactionStatus  = object.getString("status");
                            String created_date  = object.getString("created_date");
                            String username = object.getString("username");
                            String email  = object.getString("email");
                            String phone  = object.getString("phone");
                            String name  = object.getString("name");
                            String paid_status  = object.getString("paid_status");
                            String balance  = object.getString("balance");
                            String frontusername = object.getString("frontusername");

                            BonusWalletFragment.walletTransactionsModelArrayList.add(new WalletTransactionsModel( id,  front_user_id,  user_id,  type,
                                    payment_via, p_amount,  profit_type,  mode,  transactionStatus,  created_date,  username,
                                    email,  phone,  name,  paid_status,  balance, frontusername));
                        }

                        Collections.reverse(BonusWalletFragment.walletTransactionsModelArrayList);

                    } else {
                        Log.d("errorresp","error");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String error) {
                Log.d("Erroror",error+"");
            }

        });
    }

    private void getVCashTransaction(){
        ApiCalls.getVcashTransactions(activity, Prefes.getAccessToken(activity), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("transactionZLog", result + "//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        JSONArray listingArray = dataObject.getJSONArray("listing");
                        for (int i = 0; i < listingArray.length(); i++) {
                            JSONObject object = listingArray.getJSONObject(i);
                            String id = object.getString("id");
                            String front_user_id = object.getString("front_user_id");
                            String user_id = object.getString("user_id");
                            String type = object.getString("type");
                            String payment_via = object.getString("payment_via");
                            String p_amount = object.getString("p_amount");
                            String profit_type = object.getString("profit_type");
                            String mode = object.getString("mode");
                            String transactionStatus = object.getString("status");
                            String created_date = object.getString("created_date");
                            String username = object.getString("username");
                            String email = object.getString("email");
                            String phone = object.getString("phone");
                            String name = object.getString("name");
                            String paid_status = object.getString("paid_status");
                            String balance = object.getString("balance");
                            String frontusername = object.getString("frontusername");

                            VCashWalletFragment.walletTransactionsModelArrayList.add(new WalletTransactionsModel(id, front_user_id, user_id, type,
                                    payment_via, p_amount, profit_type, mode, transactionStatus, created_date, username,
                                    email, phone, name, paid_status, balance, frontusername));
                        }

                        Collections.reverse(VCashWalletFragment.walletTransactionsModelArrayList);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.d("Error",error+"");
            }

        });

    }

    private void getMCashTransaction(){
        ApiCalls.getMcashTransactions(activity, Prefes.getAccessToken(activity), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                //  Log.d("tmcashtrrr", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                        JSONArray listingArray = dataObject.getJSONArray("listing");
                        for (int i=0; i<listingArray.length(); i++){
                            JSONObject object=listingArray.getJSONObject(i);
                            String id = object.getString("id");
                            String front_user_id = object.getString("front_user_id");
                            String user_id  = object.getString("user_id");
                            String type  = object.getString("type");
                            String payment_via = object.getString("payment_via");
                            String p_amount  = object.getString("p_amount");
                            String profit_type = object.getString("profit_type");
                            String mode  = object.getString("mode");
                            String transactionStatus  = object.getString("status");
                            String created_date  = object.getString("created_date");
                            String username = object.getString("username");
                            String email  = object.getString("email");
                            String phone  = object.getString("phone");
                            String name  = object.getString("name");
                            String paid_status  = object.getString("paid_status");
                            String balance  = object.getString("balance");
                            String frontusername = object.getString("frontusername");

                            MCashWalletFragment.walletTransactionsModelArrayList.add(new WalletTransactionsModel( id,  front_user_id,  user_id,  type,
                                    payment_via, p_amount,  profit_type,  mode,  transactionStatus,  created_date,  username,
                                    email,  phone,  name,  paid_status,  balance, frontusername));
                        }

                        Collections.reverse(MCashWalletFragment.walletTransactionsModelArrayList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error) {
                Log.d("Error",error+"");
            }

        });

    }


}
