package com.vynkpay.activity.giftcards.activity;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GiftCardDescription extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    Dialog dialog;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvGiftBrandName)
    NormalTextView tvGiftBrandName;
    @BindView(R.id.tvGiftBrandCode)
    NormalTextView tvGiftBrandCode;
    @BindView(R.id.tvGiftBrandDescription)
    NormalTextView tvGiftBrandDescription;
    @BindView(R.id.tvGiftTerms)
    NormalTextView tvGiftTerms;
    @BindView(R.id.howToRedeem)
    LinearLayout howToRedeem;
    @BindView(R.id.returnPolicy)
    NormalTextView returnPolicy;
    JSONObject dataObject;
    String dataJSON;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_gift_description_rcg);
        ButterKnife.bind(GiftCardDescription.this);
        toolbar_title.setText("Gift Card Description");
        dialog = M.showDialog(GiftCardDescription.this, "", true, true);

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dataJSON = getIntent().getStringExtra("data");
        try {
            dataObject = new JSONObject(dataJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        dialog.show();
        fetchDetailOfProduct(dataObject);


    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(GiftCardDescription.this).setConnectivityListener(this);
    }

    private void fetchDetailOfProduct(JSONObject dataObject) {
        try {
            dialog.dismiss();
            JSONObject product_details = dataObject.getJSONObject("data").getJSONObject("product_details");
            String name = product_details.getString("name");
            String description = product_details.getString("description");
            String sku = product_details.getString("sku");
            String tnc_mobile = product_details.getString("tnc_mobile");
            String return_policy = product_details.getString("return_policy");
            JSONArray how_to_redeem = product_details.getJSONArray("how_to_redeem");
            for (int k = 0; k < how_to_redeem.length(); k++) {
                NormalTextView normalTextView = new NormalTextView(GiftCardDescription.this);
                normalTextView.setText((k + 1) + "). " + how_to_redeem.getString(k));
                normalTextView.setTextSize(16);
                normalTextView.setPadding(10, 10, 10, 10);
                howToRedeem.addView(normalTextView);
            }

            returnPolicy.setText(return_policy);
            tvGiftTerms.setText(Html.fromHtml(tnc_mobile));
            tvGiftBrandCode.setText(sku);
            tvGiftBrandDescription.setText(description);
            tvGiftBrandName.setText(name);
        } catch (Exception e) {

        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(GiftCardDescription.this,GiftCardDescription.this::finishAffinity);
        }
    }
}
