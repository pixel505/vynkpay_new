package com.vynkpay.network_classes;

import android.content.Context;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VolleyNetworkClass {

    private VolleyResponse volleyResponse;
    Context context;
    private HashMap<String, String> stringHashMap;

    public VolleyNetworkClass(Context context, VolleyResponse volleyResponse) {
        this.volleyResponse = volleyResponse;
        this.context = context;
    }

    public void makeRequest(String url, String token,  HashMap<String, String> hashMap, final Map<String, VolleyMultipartRequest.DataPart> params){

        stringHashMap=hashMap;
        String URL= BuildConfig.BASE_URL+url;

        VolleyMultipartRequest volleyMultipartRequest=new VolleyMultipartRequest(Request.Method.POST, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String responseString=new String(response.data);
                try {
                    JSONObject jsonObject=new JSONObject(responseString);
                    StatusModel statusModel=new StatusModel(jsonObject);

                    if (volleyResponse!=null){
                        String status="";
                        String message="";
                        if (jsonObject.has("status")){
                            status=jsonObject.getString("status");
                        }

                        if (jsonObject.has("message")){
                            message=jsonObject.getString("message");
                        }

                        volleyResponse.onResult(responseString, status, message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorString=error.getMessage();
                if (volleyResponse!=null){
                    volleyResponse.onError(errorString);
                }

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context,context.getString(R.string.timeOut) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context,context.getString(R.string.requestUnAuthorized) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context,context.getString(R.string.serverError) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context,context.getString(R.string.networkIssue) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context,context.getString(R.string.parserError) , Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return checkParams(stringHashMap);
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("access_token", token);
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(volleyMultipartRequest);
    }

    public void makeRequest(String url, String token, HashMap<String, String> hashMap){

        stringHashMap=hashMap;
        String URL= BuildConfig.BASE_URL +url;
        

        VolleyMultipartRequest volleyMultipartRequest=new VolleyMultipartRequest(Request.Method.POST, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                
                String responseString=new String(response.data);

                //check user already login on another device
                try {
                    JSONObject jsonObject=new JSONObject(responseString);
                    StatusModel statusModel=new StatusModel(jsonObject);
                    if (volleyResponse!=null){
                        String status="";
                        String message="";
                        if (jsonObject.has("status")){
                            status=jsonObject.getString("status");
                        }

                        if (jsonObject.has("message")){
                            message=jsonObject.getString("message");
                        }

                        volleyResponse.onResult(responseString, status, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              
                String errorString=error.getMessage();
                if (volleyResponse!=null){
                    volleyResponse.onError(errorString);
                }

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context,context.getString(R.string.timeOut) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context,context.getString(R.string.requestUnAuthorized) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context,context.getString(R.string.serverError) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context,context.getString(R.string.networkIssue) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context,context.getString(R.string.parserError) , Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return checkParams(stringHashMap);
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                return null;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("access_token", token);
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(volleyMultipartRequest);
    }

    public void makeRequest(String url, String token){

        String URL= BuildConfig.BASE_URL+url;
        VolleyMultipartRequest volleyMultipartRequest=new VolleyMultipartRequest(Request.Method.POST, URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                String responseString=new String(response.data);

                //check user already login on another device
                try {
                    JSONObject jsonObject=new JSONObject(responseString);
                    StatusModel statusModel=new StatusModel(jsonObject);
                    if (volleyResponse!=null){
                        String status="";
                        String message="";
                        if (jsonObject.has("status")){
                            status=jsonObject.getString("status");
                        }

                        if (jsonObject.has("message")){
                            message=jsonObject.getString("message");
                        }

                        volleyResponse.onResult(responseString, status, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorString=error.getMessage();
                if (volleyResponse!=null){
                    volleyResponse.onError(errorString);
                }

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context,context.getString(R.string.timeOut) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context,context.getString(R.string.requestUnAuthorized) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context,context.getString(R.string.serverError) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context,context.getString(R.string.networkIssue) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context,context.getString(R.string.parserError) , Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return null;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                return null;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("access_token", token);
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(context).add(volleyMultipartRequest);
    }

    private Map<String, String> checkParams(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
            if (pairs.getValue() == null) {
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }

    public void makeGetRequest(String url, String param, String token){

        String URL= BuildConfig.BASE_URL+url+"?"+param;
        String SURL=URL.replace("%20","");

        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, SURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //check user already login on another device
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    StatusModel statusModel=new StatusModel(jsonObject);
                    if (volleyResponse!=null){
                        String status="";
                        String message="";
                        if (jsonObject.has("status")){
                            status=jsonObject.getString("status");
                        }

                        if (jsonObject.has("message")){
                            message=jsonObject.getString("message");
                        }

                        volleyResponse.onResult(response, status, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String errorString=error.getMessage();
                if (volleyResponse!=null){
                    volleyResponse.onError(errorString);
                }

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(context,context.getString(R.string.timeOut) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context,context.getString(R.string.requestUnAuthorized) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(context,context.getString(R.string.serverError) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(context,context.getString(R.string.networkIssue) , Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(context,context.getString(R.string.parserError) , Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headerMap=new HashMap<>();
                headerMap.put("access_token", token);
                return headerMap;
            }
        };

        requestQueue.add(stringRequest);
    }
}