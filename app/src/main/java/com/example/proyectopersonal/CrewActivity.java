package com.example.proyectopersonal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectopersonal.Adapters.CastAdapter;
import com.example.proyectopersonal.Adapters.CrewAdapter;
import com.example.proyectopersonal.Entidades.Cast;
import com.example.proyectopersonal.Entidades.Crew;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class CrewActivity extends AppCompatActivity {

    MovieDB movieDB = new MovieDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew);

        final String idPelicula = getIntent().getStringExtra("idMovie");

        // METODO EQUIPO DE LA PELICULA
        if (isInternetAvailable()) {
            // https://api.themoviedb.org/3/movie/{movie_id}/credits?api_key=06a1953c26075c04668b820d78955ec7
            String urlActores = movieDB.getUrlMovieDB() + "movie/" + idPelicula + "?/credits?api_key=" + movieDB.getApiKey();
            final RequestQueue queueCastAndCrew = Volley.newRequestQueue(CrewActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlActores,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            Crew[] arrayEquipo = gson.fromJson(response, Crew[].class);
                            List<Crew> listaEquipo = Arrays.asList(arrayEquipo);

                            final CrewAdapter crewAdapter = new CrewAdapter(arrayEquipo, CrewActivity.this);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCrew);
                            recyclerView.setAdapter(crewAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(CrewActivity.this));

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CrewActivity.this, "Error:Cast", Toast.LENGTH_SHORT).show();
                        }
                    });
            queueCastAndCrew.add(stringRequest);
        }



    }




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