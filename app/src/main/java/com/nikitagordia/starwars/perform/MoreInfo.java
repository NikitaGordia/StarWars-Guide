package com.nikitagordia.starwars.perform;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.nikitagordia.starwars.MainActivity;
import com.nikitagordia.starwars.R;
import com.nikitagordia.starwars.pack.Films;
import com.nikitagordia.starwars.pack.InfoLoader;
import com.nikitagordia.starwars.pack.People;
import com.nikitagordia.starwars.pack.Planets;
import com.nikitagordia.starwars.pack.Species;
import com.nikitagordia.starwars.pack.Starships;
import com.nikitagordia.starwars.pack.Vehicles;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 02.12.17.
 */

public class MoreInfo extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info_activity);

        TextView title, info;
        title = (TextView) findViewById(R.id.title);
        info = (TextView) findViewById(R.id.info);

        try {
            JSONObject obj = new JSONObject(getIntent().getStringExtra(MainActivity.EXTRA_JSON_STR));
            String url = obj.getString("url");
            InfoLoader loader = null;
            if (url.startsWith(People.REF + "/")) loader = new People(getApplicationContext(), title, info); else
                if (url.startsWith(Planets.REF + "/")) loader = new Planets(getApplicationContext(), title, info); else
                    if (url.startsWith(Films.REF + "/")) loader = new Films(getApplicationContext(), title, info); else
                        if (url.startsWith(Species.REF + "/")) loader = new Species(getApplicationContext(), title, info); else
                            if (url.startsWith(Vehicles.REF + "/")) loader = new Vehicles(getApplicationContext(), title, info); else
                                if (url.startsWith(Starships.REF + "/")) loader = new Starships(getApplicationContext(), title, info);

            if (loader == null) return;
            loader.loadInfo(obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
