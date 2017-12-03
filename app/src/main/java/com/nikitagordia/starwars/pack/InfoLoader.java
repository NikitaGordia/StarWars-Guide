package com.nikitagordia.starwars.pack;

import android.content.Context;
import android.icu.text.IDNA;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 02.12.17.
 */

public abstract class InfoLoader {


    protected Context context;
    protected RequestQueue requestQueue;
    protected TextView info, title;

    public InfoLoader(Context context, TextView title, TextView info) {
        this.context = context;
        this.title = title;
        this.info = info;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void requireField(String ref, final String field, final String reqStr) {
        requestQueue.add(new StringRequest(Request.Method.GET, ref, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    onRequest(reqStr, obj.getString(field));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void loadInfo(JSONObject obj) {
        try {
            parse(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateInfo();
    }

    protected String addToArr(String arr, String item) {
        if (arr.equals("-")) arr = item; else arr += ", \n\t\t\t" + item;
        return arr;
    }

    protected abstract void updateInfo();

    public abstract void parse(JSONObject obj) throws JSONException;

    public abstract void onRequest(String reqStr, String res);
}
