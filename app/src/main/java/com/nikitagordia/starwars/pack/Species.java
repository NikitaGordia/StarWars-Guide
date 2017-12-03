package com.nikitagordia.starwars.pack;

import android.content.Context;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 02.12.17.
 */

public class Species extends InfoLoader{

    public static final String REF = "https://swapi.co/api/species";
    public static final int ID = 3;

    public String name, classification, designation, averageHeight, skinColor, hairColor, eyeColor,
                        averageLifespan, homeworld, language, peopleArr = "-", filmsArr = "-", create, edited, url;

    public Species(Context context, TextView title, TextView info) {
        super(context, title, info);
    }

    @Override
    public void parse(JSONObject obj) throws JSONException {
        name = obj.getString("name");
        classification = obj.getString("classification");
        designation = obj.getString("designation");
        averageHeight = obj.getString("average_height");
        skinColor = obj.getString("skin_colors");
        hairColor = obj.getString("hair_colors");
        eyeColor = obj.getString("eye_colors");
        averageLifespan = obj.getString("average_lifespan");
        homeworld = obj.getString("homeworld");
        language = obj.getString("language");
        create = obj.getString("created");
        edited = obj.getString("edited");
        url = obj.getString("url");

        requireField(homeworld, "name", "home");

        JSONArray array = obj.getJSONArray("people");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "name", "CharName");

        array = obj.getJSONArray("films");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "title", "FilmName");

        updateInfo();
    }

    @Override
    protected void updateInfo() {
        title.setText(name);
        info.setText("Classification : " + classification + "\n" +
                    "Designation : " + designation + "\n" +
                    "Average heigth : " + averageHeight + "\n" +
                    "Skin colors : " + skinColor + "\n" +
                    "Hair colors : " + hairColor + "\n" +
                    "Eye colors : " + eyeColor + "\n" +
                    "Average lifespan : " + averageLifespan + "\n" +
                    "Homeworld : " + homeworld + "\n" +
                    "Language : " + language + "\n" +
                    "Characters : " + peopleArr + "\n" +
                    "Films : " + filmsArr + "\n\n\n" +
                    "Create : " + create + "\n" +
                    "Edited : " + edited + "\n" +
                    "URL : " + url + "\n");
    }

    @Override
    public void onRequest(String reqStr, String res) {
        if (reqStr.equals("home")) homeworld = res; else
            if (reqStr.equals("CharName")) peopleArr = addToArr(peopleArr, res); else
                if (reqStr.equals("FilmName")) filmsArr = addToArr(filmsArr, res);
        updateInfo();
    }
}











