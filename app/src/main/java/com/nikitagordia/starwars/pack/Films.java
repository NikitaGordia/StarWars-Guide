package com.nikitagordia.starwars.pack;

import android.content.Context;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 01.12.17.
 */

public class Films extends InfoLoader {

    public static final String REF = "https://swapi.co/api/films";
    public static final int ID = 2;

    public String titleStr, director, producer, releaseDate, created, edited, url, episodeId, openingCrawl,
                    charactersArr = "-", planetsArr = "-", vehiclesArr = "-", starShipsArr = "-", speciesArr = "-";

    public Films(Context context, TextView title, TextView info) {
        super(context, title, info);
    }


    public void parse(JSONObject obj) throws JSONException {
        titleStr = obj.getString("title");
        openingCrawl = obj.getString("opening_crawl");
        director = obj.getString("director");
        producer = obj.getString("producer");
        releaseDate = obj.getString("release_date");
        created = obj.getString("created");
        edited = obj.getString("edited");
        url = obj.getString("url");
        episodeId = obj.getString("episode_id");

        JSONArray array = obj.getJSONArray("characters");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "name", "CharName");

        array = obj.getJSONArray("planets");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "name", "PlanName");

        array = obj.getJSONArray("starships");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "name", "ShipName");

        array = obj.getJSONArray("species");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "name", "SpeciName");

        array = obj.getJSONArray("vehicles");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "name", "VehiName");

        updateInfo();
    }

    @Override
    protected void updateInfo() {
        title.setText(titleStr);
        info.setText("Episode : " + episodeId + "\n" +
                    "Opening crawl : " + addTab(openingCrawl) + "\n" +
                    "Director : " + director + "\n" +
                    "Producer : " + producer + "\n" +
                    "Release date : " + releaseDate + "\n" +
                    "Characters : " + charactersArr + "\n" +
                    "Planets : "+ planetsArr + "\n" +
                    "Species : " + speciesArr + "\n" +
                    "Vehicles : " + vehiclesArr + "\n" +
                    "Starships : " + starShipsArr + "\n\n\n" +
                    "URL : " + url + "\n" +
                    "Created : " + created + "\n" +
                    "Edited : " + edited + "\n");
    }

    @Override
    public void onRequest(String reqStr, String res) {
        if (reqStr.equals("CharName")) charactersArr = addToArr(charactersArr, res); else
            if (reqStr.equals("PlanName")) planetsArr = addToArr(planetsArr, res); else
                if (reqStr.equals("ShipName")) starShipsArr = addToArr(starShipsArr, res); else
                    if (reqStr.equals("SpeciName")) speciesArr = addToArr(speciesArr, res); else
                        if (reqStr.equals("VehiName")) vehiclesArr = addToArr(vehiclesArr, res);
        updateInfo();
    }

    public String addTab(String s) {
        String res = "";
        for (int i = 0; i < s.length(); i++) {
            res += s.charAt(i);
            if (s.charAt(i) == '\n')
                res += "\t\t\t";
        }
        return res;
    }
}
