package com.nikitagordia.starwars;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nikitagordia.starwars.pack.Films;
import com.nikitagordia.starwars.pack.ItemInterface;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nikitagordia.starwars.pack.People;
import com.nikitagordia.starwars.pack.Planets;
import com.nikitagordia.starwars.pack.Species;
import com.nikitagordia.starwars.pack.Starships;
import com.nikitagordia.starwars.pack.Vehicles;
import com.nikitagordia.starwars.perform.MoreInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_JSON_STR = "com.nikitagordia.perform.json_str";

    public RecyclerView rv;
    private SwipeRefreshLayout swc;
    public Adapter mAdapter;
    private boolean isLoading;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkMyPermission();

        rv = (RecyclerView) findViewById(R.id.recycler);
        swc = (SwipeRefreshLayout) findViewById(R.id.swap);

        mAdapter = new Adapter();

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(itemDecoration);
        rv.setAdapter(mAdapter);
        rv.setItemAnimator(new SlideInUpAnimator());
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        swc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLoading) return;
                switch (state) {
                    case People.ID :
                        loadPeople();
                        break;
                    case Planets.ID :
                        loadPlanets();
                        break;
                    case Films.ID :
                        loadFilms();
                        break;
                    case Species.ID :
                        loadSpecies();
                        break;
                    case Vehicles.ID :
                        loadVehicles();
                        break;
                }
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (isLoading) return false;
                switch (item.getItemId()) {
                    case R.id.people_pack :
                        if (!updateState(People.ID)) break;
                        loadPeople();
                        break;
                    case R.id.planets_pack :
                        if (!updateState(Planets.ID)) break;
                        loadPlanets();
                        break;
                    case R.id.films_pack :
                        if (!updateState(Films.ID)) break;
                        loadFilms();
                        break;
                    case R.id.species_pack :
                        if (!updateState(Species.ID)) break;
                        loadSpecies();
                        break;
                    case R.id.vehicles_pack :
                        if (!updateState(Vehicles.ID)) break;
                        loadVehicles();
                        break;
                }
                return true;
            }
        });

        updateState(People.ID);
        loadPeople();
    }

    private void requestAndSetList(String ref, final String first, final String second, boolean refresh) {
        if (refresh) mAdapter.clear();
        isLoading = true;
        swc.setRefreshing(true);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(new StringRequest(Request.Method.GET, ref, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    List<ItemInterface> list = new ArrayList<>();
                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++)
                        list.add(new ItemInterface(array.getJSONObject(i), first, second));
                    mAdapter.add(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isLoading = false;
                swc.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
                @Override
                    public void onErrorResponse(VolleyError error) {
                        isLoading = false;
                        swc.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
                    }
        }));

    }

    private void loadPeople() {
        requestAndSetList(People.REF, "name", "birth_year", true);
    }

    private void loadPlanets() {
        requestAndSetList(Planets.REF, "name", "population", true);
    }

    private void loadFilms() {
        requestAndSetList(Films.REF, "title", "producer", true);
    }

    private void loadSpecies() {
        requestAndSetList(Species.REF, "name", "classification", true);
    }

    private void loadVehicles() {
        requestAndSetList(Vehicles.REF, "name", "model", true);
        requestAndSetList(Starships.REF, "name", "model", false);
    }

    private boolean updateState(int id) {
        if (state == id) return false;
        state = id;
        return true;
    }

    private void checkMyPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 0);
            }
        }
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView first, second;
        private ItemInterface itemI;

        public Holder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));

            first = (TextView) itemView.findViewById(R.id.first_label);
            second = (TextView) itemView.findViewById(R.id.second_label);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            startInfo(itemI);
        }

        public void bind(ItemInterface item) {
            itemI = item;
            first.setText(item.first);
            second.setText(item.second);
        }
    }

    public class Adapter extends RecyclerView.Adapter<Holder> {

        List<ItemInterface> list;

        public Adapter() {
            list = new ArrayList();
        }



        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new Holder(layoutInflater, parent);
        }

        public void clear() {
            notifyItemRangeRemoved(0, list.size());

            list.clear();
        }

        public void add(List<ItemInterface> newList) {
            int old = list.size();
            list.addAll(newList);

            notifyItemRangeInserted(old, list.size());
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public void startInfo(ItemInterface item) {
        Intent intent = new Intent(getApplicationContext(), MoreInfo.class);
        intent.putExtra(EXTRA_JSON_STR, item.getObject().toString());
        startActivity(intent);
    }
}
