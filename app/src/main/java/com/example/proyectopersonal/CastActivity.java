package com.example.proyectopersonal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectopersonal.Adapters.CastAdapter;
import com.example.proyectopersonal.Adapters.MovieAdapter;
import com.example.proyectopersonal.Detalles.PeliculaActivity;
import com.example.proyectopersonal.Entidades.Cast;
import com.example.proyectopersonal.Entidades.Crew;
import com.example.proyectopersonal.Entidades.Movie;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.List;

public class CastActivity extends AppCompatActivity {

    MovieDB movieDB = new MovieDB();
    Cast[] listaActores;
    int x;
    String idPelicula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);

       idPelicula = getIntent().getStringExtra("idMovie");

        // METODO ACTORES DE LA PELICULA
        if (isInternetAvailable()) {
            // https://api.themoviedb.org/3/movie/{movie_id}/credits?api_key=06a1953c26075c04668b820d78955ec7
            String urlActores = movieDB.getUrlMovieDB() + "movie/" + idPelicula + "/credits?api_key=" + movieDB.getApiKey();
            final RequestQueue queueCastAndCrew = Volley.newRequestQueue(CastActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlActores,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            try { JSONObject jsonObject = new JSONObject(response);
                                JSONArray casting = (JSONArray) jsonObject.get("cast");
                                int tamañoLista = casting.length();
                                listaActores = new Cast[tamañoLista];

                                for ( int x=0; x<tamañoLista; x++){
                                    Cast actor = new Cast();
                                    JSONObject cast = (JSONObject) casting.get(x);
                                    String idCast = cast.getString("cast_id"); actor.setCast_id(Integer.valueOf(idCast));
                                    String character = cast.getString("character"); actor.setCharacter(character);
                                    String credit_id = cast.getString("credit_id"); actor.setCredit_id(credit_id);
                                    String id = cast.getString("id"); actor.setId(Integer.valueOf(id));
                                    String gender = cast.getString("gender"); actor.setGender(Integer.valueOf(gender));
                                    String name = cast.getString("name"); actor.setName(name);
                                    String profile_path = cast.getString("profile_path"); actor.setProfile_path(profile_path);
                                    listaActores[x] = actor;
                                }

                                final CastAdapter castAdapter = new CastAdapter(listaActores,CastActivity.this);
                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCast);
                                recyclerView.setAdapter(castAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(CastActivity.this));


                            }

                            catch (JSONException e) { e.printStackTrace(); }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CastActivity.this, "Error:Cast", Toast.LENGTH_SHORT).show();
                        }
                    });
            queueCastAndCrew.add(stringRequest);
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantallamovie, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.PeliculaActores:
                Intent intent1 = new Intent(CastActivity.this, CastActivity.class);
                intent1.putExtra("idMovie", idPelicula); startActivity(intent1);
                return true;
            case R.id.PeliculaDirector:
                Intent intent2 = new Intent(CastActivity.this, CrewActivity.class);
                intent2.putExtra("idMovie", idPelicula); startActivity(intent2);
                return true;
            case R.id.PeliculaRecomendaciones:
                Intent intent3 = new Intent(CastActivity.this, RecomendacionesActivity.class);
                intent3.putExtra("idMovie", idPelicula); startActivity(intent3);
                return true;
            case R.id.PeliculaReviews:
                Intent intent4 = new Intent(CastActivity.this, ReviewActivity.class);
                intent4.putExtra("idMovie", idPelicula); startActivity(intent4);
                return true;
        }
        return onOptionsItemSelected(item);}

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network networks = connectivityManager.getActiveNetwork();
            if (networks == null) return false;

            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(networks); if (networkCapabilities == null) return false;
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) return true;
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) return true;
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) return true;
            return false;
        } else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) return false;
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) return true;
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) return true;
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) return true;
            return false; }
    }

}