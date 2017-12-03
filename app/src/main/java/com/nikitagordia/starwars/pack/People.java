package com.nikitagordia.starwars.pack;

import android.content.Context;
import android.icu.text.IDNA;
import android.util.Log;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 30.11.17.
 */

public class People extends InfoLoader {

    public static final String REF = "https://swapi.co/api/people";
    public static final int ID = 10;

    public String name, hairColor, skinColor, eyeColor, birthYear, gender, created, edited, url, height, mass,
                    filmsArr = "-", homeworld, speciesArr = "-", vehiclesArr = "-", starShipsArr = "-";

    public People(Context context, TextView title, TextView info) {
        super(context, title, info);
    }


    @Override
    protected void updateInfo() {
        title.setText(name);
        info.setText("Hair color : " + hairColor + "\n" +
                "Skin color : " + skinColor + "\n" +
                "Eye color : " + eyeColor + "\n" +
                "Birth year : " + birthYear + "\n" +
                "Gender : " + gender + "\n" +
                "Mass : " + mass + "\n" +
                "Height : " + height + "\n" +
                "Films : " + filmsArr + "\n" +
                "Species : " + speciesArr + "\n" +
                "Vehicles : " + vehiclesArr + "\n" +
                "Starships : " + starShipsArr + "\n\n\n" +
                "URL : " + url + "\n" +
                "Created : " + created + "\n" +
                "Edited : " + edited + "\n");
    }

    @Override
    public void parse(JSONObject obj) throws JSONException{
        name = obj.getString("name");
        hairColor = obj.getString("hair_color");
        skinColor = obj.getString("skin_color");
        eyeColor = obj.getString("eye_color");
        birthYear = obj.getString("birth_year");
        gender = obj.getString("gender");
        created = obj.getString("created");
        edited = obj.getString("edited");
        url = obj.getString("url");
        height = obj.getString("height");
        mass = obj.getString("mass");
        homeworld = obj.getString("homeworld");

        requireField(homeworld, "name", "HomeWorld");

        JSONArray array = obj.getJSONArray("films");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "title", "FilmName");

        array = obj.getJSONArray("species");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "name", "SpecieName");

        array = obj.getJSONArray("vehicles");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "name", "VehicleName");

        array = obj.getJSONArray("starships");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "name", "StarshipName");

        updateInfo();
    }

    @Override
    public void onRequest(String reqStr, String res) {
        if (reqStr.equals("FilmName")) filmsArr = addToArr(filmsArr, res); else
            if (reqStr.equals("SpecieName")) speciesArr = addToArr(speciesArr, res); else
                if (reqStr.equals("VehicleName")) vehiclesArr = addToArr(vehiclesArr, res); else
                    if (reqStr.equals("StarshipName")) starShipsArr = addToArr(starShipsArr, res);

        updateInfo();
    }
}
