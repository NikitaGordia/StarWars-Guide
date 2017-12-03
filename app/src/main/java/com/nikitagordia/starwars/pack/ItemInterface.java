package com.nikitagordia.starwars.pack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by root on 30.11.17.
 */

public class ItemInterface {

    public String first, second;
    private JSONObject object;
    private String ref;

    public ItemInterface(JSONObject obj, String first, String second) throws JSONException {
        object = obj;
        ref = obj.getString("url");
        this.first = obj.getString(first);
        this.second = obj.getString(second);
    }

    public JSONObject getObject() {
        return object;
    }

    public String getRef() {
        return ref;
    }
}
