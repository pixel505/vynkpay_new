package com.vynkpay.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.helper.CustomProgress;
import com.vynkpay.models.ErrorDialogModel;
import com.vynkpay.models.SuccessDialogModel;
import com.vynkpay.prefes.Prefes;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class M {
    public static String TOPIC = "9";
    private static ProgressDialog dialog;
    private static Dialog dialog1;

    public static boolean isScreenshotDisable = true;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void makeChecks(Context context, Class<?> launching) {
        Intent intent = new Intent(context, launching);
        intent.putExtra(ApiParams.finishLogin, "1");
        context.startActivity(intent);
    }

    private static JSONObject userPreference;

    public static ErrorDialogModel makeErrorDialog(final Context context) {
        Dialog errorDialog = M.inflateDialog(context, R.layout.dialog_error_message_rcg);
        NormalTextView errorMessage = errorDialog.findViewById(R.id.errorMessage);
        NormalButton okButton = errorDialog.findViewById(R.id.okButton);
        LinearLayout closeButton = errorDialog.findViewById(R.id.closeButton);
        return new ErrorDialogModel(errorDialog, errorMessage, okButton, closeButton);
    }

    public static SuccessDialogModel makeSuccessDialog(final Context context) {
        Dialog successDialog = M.inflateDialog(context, R.layout.dialog_payment_result_rcg);
        NormalTextView tvMobileNumber = successDialog.findViewById(R.id.tvMobileNumber);
        NormalTextView tvAmount = successDialog.findViewById(R.id.tvAmount);
        NormalTextView tvTransactionId = successDialog.findViewById(R.id.tvTransactionId);
        NormalTextView tvDateTime = successDialog.findViewById(R.id.tvDateTime);
        NormalButton okButton = successDialog.findViewById(R.id.okButton);
        LinearLayout closeButton = successDialog.findViewById(R.id.closeButton);
        ImageView imageResult = successDialog.findViewById(R.id.imageResult);
        NormalTextView forWhatPaid = successDialog.findViewById(R.id.forWhatPaid);
        NormalTextView textResult = successDialog.findViewById(R.id.textResult);
        return new SuccessDialogModel(successDialog, tvMobileNumber, tvAmount, tvTransactionId, tvDateTime, okButton, closeButton, imageResult, forWhatPaid, textResult);
    }

    public static int dpInPx(float myPixels) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, myPixels, displaymetrics);
    }

    public static void makeNavigationToHome(Dialog dialog, Context context) {
        if (dialog != null) {
            dialog.dismiss();
        }
      SharedPreferences  sp = context.getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);

        if(sp.getString("value","").equals("Global") && Prefes.getisIndian(context).equals("NO")){
            context.startActivity(new Intent(context, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            ((Activity) context).finish();
        }

        else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(context).equals("YES")){
            context.startActivity(new Intent(context, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            ((Activity) context).finish();
        }


        else if(sp.getString("value","").equals("India") && Prefes.getisIndian(context).equals("YES")){
            context.startActivity(new Intent(context, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            ((Activity) context).finish();
        }

        else if(sp.getString("value","").equals("India") && Prefes.getisIndian(context).equals("NO")){
           context. startActivity(new Intent(context, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            ((Activity) context).finish();
        }


    }

    // get User Data
    public static String fetchUserTrivialInfo(Context context, String receivedVar) {
        try {
            userPreference = new JSONObject(Prefes.getUserData(context));
            return userPreference.getString(receivedVar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String upDateUserTrivialInfo(Context context, String key, String newVal) {
        try {
            userPreference = new JSONObject(Prefes.getUserData(context));
            userPreference.put(key, newVal);
            Prefes.saveUserData(userPreference.toString(), context);
            Log.i(">>val", "upDateUserTrivialInfo: " + userPreference.toString());
            return userPreference.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void removeZero(Editable s) {
        if (s.toString().length() == 1 && s.toString().startsWith("0")) {
            s.clear();
        } else if (s.toString().length() == 1 && s.toString().startsWith(".")) {
            s.clear();
        }
    }

    public static void removeDot(Editable s) {
        if (s.toString().length() == 1 && s.toString().startsWith(".")) {
            s.clear();
        }
    }

    public static void reset(Context context) {
        Prefes.saveDescription("", context);
        Prefes.saveValidity("", context);
        Prefes.savePlan("", context);
        Prefes.saveTalktime("", context);
    }

    public static ArrayAdapter<String> makeSpinnerAdapterWhite(Context context, String[] spinnerArray) {
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, R.layout.text_view_white, spinnerArray);
        aa.setDropDownViewResource(R.layout.drop_down_list_item_white);
        return aa;
    }

    public static void dialogOk(Context context, String message, String title) {
        final Dialog errorDialog = M.inflateDialog(context, R.layout.dialog_error_message_rcg);
        NormalTextView errorMessage = errorDialog.findViewById(R.id.errorMessage);
        NormalButton okButton = errorDialog.findViewById(R.id.okButton);
        okButton.setText(title.equalsIgnoreCase("Success") ? "ok" : "Try Again");
        LinearLayout closeButton = errorDialog.findViewById(R.id.closeButton);
        errorMessage.setText(message);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorDialog.dismiss();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
            }
        });
        errorDialog.show();
    }

    public static void dialogYesOrNo(Context context, String message, String title) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void printShortMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void printLongMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void longLog(String logName, String response){
        int maxLogSize = 1000;
        for(int i = 0; i <= response.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > response.length() ? response.length() : end;
            Log.v(logName, response.substring(start, end));
        }
    }

    public static void showProgressDialog(Context context, boolean WHAT, String title, String loadingMessage) {
        if (WHAT) {
            dialog = ProgressDialog.show(context, title, loadingMessage);
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }

    public static Dialog showDialog(Context context, String message, boolean isCancel, boolean isDismiss) {
        return CustomProgress.show(context, message, isCancel);
    }


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final String removeMultipleRegex = "";

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    public static LinearLayoutManager verticalRecyclerView(Context context) {
        return new LinearLayoutManager(context);
    }

    public static LinearLayoutManager horizontalRecyclerView(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    @NonNull
    public static LinearLayoutManager gridLayoutRecyclerView(Context context, int gridColumn) {
        return new GridLayoutManager(context, gridColumn);
    }

    public static void launchActivity(Context context, Class<?> launchingActivity) {
        (context).startActivity(new Intent(context, launchingActivity));
    }

    public static void launch(Context context, Intent intent) {
        (context).startActivity(intent);
    }

    public static void launchActivityWithFinish(Context context, Class<?> launchingActivity) {
        (context).startActivity(new Intent(context, launchingActivity));
        ((Activity) context).finish();
    }

//    public static ArrayAdapter<String> makeSpinnerAdapter(Context context, String[] spinnerArray) {
//        ArrayAdapter<String> aa = new ArrayAdapter<>(context, R.layout.text_view, spinnerArray);
//        aa.setDropDownViewResource(R.layout.drop_down_list_item);
//        return aa;
//    }
//
//    public static ArrayAdapter<String> makeSpinnerAdapterWhite(Context context, String[] spinnerArray) {
//        ArrayAdapter<String> aa = new ArrayAdapter<>(context, R.layout.text_view_white, spinnerArray);
//        aa.setDropDownViewResource(R.layout.drop_down_list_item_white);
//        return aa;
//    }

//    public static void makeErrorTips(View view, String message, int backgroundColorId, int textColorId) {
//        Tooltip.Builder builder = new Tooltip.Builder(view)
//                .setCancelable(true)
//                .setDismissOnClick(false)
//                .setTextColor(textColorId)
//                .setBackgroundColor(backgroundColorId)
//                .setTextSize(R.dimen.textSize)
//                .setGravity(Gravity.END)
//                .setCornerRadius(10f)
//                .setText(message);
//
//        builder.show();
//    }

    @NonNull
    public static Dialog inflateDialog(Context context, int layoutId) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(layoutId);

        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT; // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lWindowParams);

//        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
//
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int keyCode,
//                                 KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    dialog.dismiss();
//                }
//                return true;
//            }
//        });
        return dialog;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

//    public static Dialog showDialog(Context context, String message, boolean isCancel, boolean isDismiss) {
//        return CustomProgress.show(context, message, isCancel);
//    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getRealPathFromFile(Uri fileUri) {
        File file = new File(fileUri.getPath());
        return file.getPath();
    }

    public static File getRealFileFromUri(Uri fileUri) {
        return new File(fileUri.getPath());
    }

    public static String changeTimeFormat(String inputString) {
        Log.i(">>inputDate", "changeDateFormat: " + inputString);
        String[] date1 = new String[1];
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.getDefault());

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(inputString);
            str = outputFormat.format(date);
            date1 = str.split(" ");
            Log.i(">>method", "changeTimeFormat: " + str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String mSuccess, mMessage;

    public static void makeLogoutRequest(final Context context, final String accessToken, final Dialog dialog) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, BuildConfig.APP_BASE_URL + URLS.logout,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>forgotPassword", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                Prefes.saveAccessToken("", context);
                                Prefes.savePhoneNumber("", context);
                                Prefes.saveEmail("", context);
                                Prefes.saveUserData("", context);
                                SharedPreferences  sp = context.getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
                                if(sp.getString("value","").equals("Global") && Prefes.getisIndian(context).equals("NO")){
                                    context.startActivity(new Intent(context, HomeActivity.class).putExtra("Country", "Global").putExtra(ApiParams.finishLogin, "0000")
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    ((Activity) context).finish();
                                }

                                else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(context).equals("YES")){
                                    context.startActivity(new Intent(context, HomeActivity.class).putExtra("Country", "India").putExtra(ApiParams.finishLogin, "0000").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    ((Activity) context).finish();
                                }

                                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(context).equals("YES")){
                                    context.startActivity(new Intent(context, HomeActivity.class).putExtra("Country", "India").putExtra(ApiParams.finishLogin, "0000").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    ((Activity) context).finish();
                                }

                                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(context).equals("NO")){
                                    context. startActivity(new Intent(context, HomeActivity.class).putExtra("Country", "Global").putExtra(ApiParams.finishLogin, "0000").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    ((Activity) context).finish();
                                }

                            } else {
                                M.dialogOk(context, mMessage, "Error");
                            }

                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            M.dialogOk(context, "Please check internet connection", "Error");
                        } else if (error instanceof AuthFailureError) {
                            M.dialogOk(context, "You are not authorized", "Error");
                        } else if (error instanceof ServerError) {
                            M.dialogOk(context, "Server issue", "Error");
                        } else if (error instanceof NetworkError) {
                            M.dialogOk(context, "Please check internet connection", "Error");
                        } else if (error instanceof ParseError) {
                            M.dialogOk(context, "Internal error", "Error");
                        }
                        dialog.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, accessToken);
                params.put("Accept", "application/json");
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    /**
     * @@To show and hide the password from inputEdit text
     */
    public static void showHidePassword(Context context, boolean receivePasswordShownVariable, NormalEditText etPassword, ImageView showHideImage) {
        if (!receivePasswordShownVariable) {
            showHideImage.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_view));
            etPassword.setTransformationMethod(new PasswordTransformationMethod());
        } else {
            showHideImage.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_hide));
            etPassword.setTransformationMethod(null);
        }
        etPassword.setText(etPassword.getText().toString());
        etPassword.setSelection(etPassword.length());
    }

    /**
     * @YYYY-MM-DD When you have both date and time in received
     * string and you want only date to be returned by this function
     **/

    public static String changeDateFormat(String inputString) {
        Log.i(">>inputDate", "changeDateFormat: " + inputString);
        String[] date1 = new String[1];
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.getDefault());

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(inputString);
            str = outputFormat.format(date);
            date1 = str.split(" ");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1[0];
    }

    public static String changeDateFormat(String date, String foundFormat, String requiredFormat){
        //yyyy-MM-dd hh:mm:ss
        SimpleDateFormat spf=new SimpleDateFormat(foundFormat,Locale.getDefault());
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat(requiredFormat);
        return spf.format(newDate);
    }


    /**
     * @DD-MM-YYYY When you have both date and time in received
     * string and you want only date to be returned by this function
     **/

    public static String changeDateFormatDD(String inputString) {
        Log.i(">>inputDate", "changeDateFormat: " + inputString);
        String[] date1 = new String[1];
        String inputPattern = "dd-MM-yyyy HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.getDefault());

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(inputString);
            str = outputFormat.format(date);
            date1 = str.split(" ");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1[0];

    }


    /**
     * When you have both date and time in received
     * /string and you want both to be returned by this function
     **/

    public static String changeDateTimeFormat(String inputString) {
        Log.i(">>inputDate", "changeDateFormat: " + inputString);
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy h:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.getDefault());

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(inputString);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }

    private static boolean isDecision = false;

    public interface OnPopClickListener{
        void onClick();
    }

    public static void showUSBPopUp(Context context,OnPopClickListener listener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage("Please disconnect your device from USB to continue using the application");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener !=null){
                    listener.onClick();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        try {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }catch (Exception e){
            e.printStackTrace();
        }

       /* AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setCancelable(false);
        dialog.setMessage("Please disconnect your device from USB to contine using the application");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener !=null){
                    listener.onClick();
                }
            }
        }).show();
        dialog.get.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(neededColor);*/
    }

}
