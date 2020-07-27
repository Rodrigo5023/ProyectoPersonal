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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectopersonal.Adapters.GeneroAdapter;
import com.example.proyectopersonal.Adapters.ReviewAdapter;
import com.example.proyectopersonal.Entidades.Genero;
import com.example.proyectopersonal.Entidades.Movie;
import com.google.gson.Gson;

public class GenreActivity extends AppCompatActivity {

    MovieDB movieDB = new MovieDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        final String idPelicula = getIntent().getStringExtra("idMovie");

        // METODO DETALLES DE LA PELICULA
        if(isInternetAvailable()) {
            String urlPelicula = movieDB.getUrlMovieDB() + "movie/" + idPelicula + "?api_key=" + movieDB.getApiKey() + "&language=es-ES";
            final RequestQueue queueMovies = Volley.newRequestQueue(GenreActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPelicula,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            Movie movie = gson.fromJson(response,Movie.class);
                            Genero[] arrayGenero= movie.getListaGeneros();

                            final GeneroAdapter generoAdapter = new GeneroAdapter(arrayGenero,GenreActivity.this);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGenero);
                            recyclerView.setAdapter(generoAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(GenreActivity.this));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(GenreActivity.this, "Error:Movie", Toast.LENGTH_SHORT).show();
                        }
                    }); queueMovies.add(stringRequest);

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