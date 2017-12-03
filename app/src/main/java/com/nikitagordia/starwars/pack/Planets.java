package com.nikitagordia.starwars.pack;

import android.content.Context;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 01.12.17.
 */

public class Planets extends InfoLoader {

    public static final String REF = "https://swapi.co/api/planets";
    public static final int ID = 1;

    public String name, gravity, climate, terrain, surfaceWater, created, edited, population,
            diameter, rotationPeriod, orbitalPeriod, url, filmsArr = "-", residentsArr = "-";

    public Planets(Context context, TextView title, TextView info) {
        super(context, title, info);
    }

    public void parse(JSONObject obj) throws JSONException {
        url = obj.getString("url");
        name = obj.getString("name");
        gravity = obj.getString("gravity");
        climate = obj.getString("climate");
        terrain = obj.getString("terrain");
        surfaceWater = obj.getString("surface_water");
        created = obj.getString("created");
        edited = obj.getString("edited");
        population = obj.getString("population");
        diameter = obj.getString("diameter");
        rotationPeriod = obj.getString("rotation_period");
        orbitalPeriod = obj.getString("orbital_period");

        JSONArray array = obj.getJSONArray("films");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "title", "films");

        array = obj.getJSONArray("residents");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "name", "ResidName");

        updateInfo();
    }

    @Override
    protected void updateInfo() {
        title.setText(name);
        info.setText("Gravity : " + gravity + "\n" +
                    "Population : " + population + "\n" +
                    "Diameter : " + diameter + "\n" +
                    "Rotation period : " + rotationPeriod + "\n" +
                    "Orbital period : " + orbitalPeriod + "\n" +
                    "Climate : " + climate + "\n" +
                    "Terrain : " + terrain + "\n" +
                    "Surface water : " + surfaceWater + "\n" +
                    "Residents : " + residentsArr + "\n" +
                    "Films : " + filmsArr + "\n\n\n" +
                    "URL : " + url + "\n" +
                    "Created : " + created + "\n" +
                    "Edited : " + edited + "\n");
    }

    @Override
    public void onRequest(String reqStr, String res) {
        if (reqStr.equals("films")) filmsArr = addToArr(filmsArr, res); else
            if (reqStr.equals("ResidName")) residentsArr = addToArr(residentsArr, res);
        updateInfo();
    }
}

