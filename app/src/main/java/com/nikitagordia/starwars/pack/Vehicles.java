package com.nikitagordia.starwars.pack;

import android.content.Context;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 02.12.17.
 */

public class Vehicles extends InfoLoader {

    public static final String REF = "https://swapi.co/api/vehicles";
    public static final int ID = 4;

    public String name, model, manufacturer, costInCredits, length, maxAtmospheringSeed,
                crew, passengers, cargoCapacity, consumables, vehicleClass, pilotsArr = "-",
                    filmsArr = "-", created, edited, url;

    public Vehicles(Context context, TextView title, TextView info) {
        super(context, title, info);
    }

    @Override
    public void parse(JSONObject obj) throws JSONException {
        name = obj.getString("name");
        model = obj.getString("model");
        manufacturer = obj.getString("manufacturer");
        costInCredits = obj.getString("cost_in_credits");
        length = obj.getString("length");
        maxAtmospheringSeed = obj.getString("max_atmosphering_speed");
        crew = obj.getString("crew");
        passengers = obj.getString("passengers");
        cargoCapacity = obj.getString("cargo_capacity");
        consumables = obj.getString("consumables");
        vehicleClass = obj.getString("vehicle_class");
        created = obj.getString("created");
        edited = obj.getString("edited");
        url = obj.getString("url");

        JSONArray array = obj.getJSONArray("pilots");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "name", "PilotName");

        array = obj.getJSONArray("films");
        for (int i = 0; i < array.length(); i++)
            requireField(array.getString(i), "title", "FilmTitle");

        updateInfo();
    }

    @Override
    protected void updateInfo() {
        title.setText(name);
        info.setText("Model : " + model + "\n" +
                    "Manufacturer : " + manufacturer + "\n" +
                    "Cost in credits : " + costInCredits + "\n" +
                    "Length : " + length + "\n" +
                    "Max atmosphering speed : " + maxAtmospheringSeed + "\n" +
                    "Crew : " + crew + "\n" +
                    "Passengers : " + passengers + "\n" +
                    "Cargo capacity : " + cargoCapacity + "\n" +
                    "Consumables : " + consumables + "\n" +
                    "Vehicle class : " + vehicleClass + "\n" +
                    "Pilots : " + pilotsArr + "\n" +
                    "Films : " + filmsArr + "\n\n\n" +
                    "URL : " + url + "\n" +
                    "Created : " + created + "\n" +
                    "Edited : " + edited + "\n");
    }

    @Override
    public void onRequest(String reqStr, String res) {
        if (reqStr.equals("PilotName")) pilotsArr = addToArr(pilotsArr, res); else
            if (reqStr.equals("FilmTitle")) filmsArr = addToArr(filmsArr, res);
        updateInfo();
    }
}













