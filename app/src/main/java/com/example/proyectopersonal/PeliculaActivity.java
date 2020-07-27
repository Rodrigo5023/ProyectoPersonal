package com.example.proyectopersonal;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.proyectopersonal.Entidades.Cast;
import com.example.proyectopersonal.Entidades.Crew;
import com.example.proyectopersonal.Entidades.Genero;
import com.example.proyectopersonal.Entidades.Movie;
import com.example.proyectopersonal.Entidades.Review;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class PeliculaActivity extends AppCompatActivity {

    MovieDB movieDB = (MovieDB)getApplicationContext();
    Movie movie = new Movie(); Genero genero = new Genero();
    Cast cast = new Cast(); Crew crew = new Crew();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        final String idPelicula = getIntent().getStringExtra("idPelicula");

        // METODO DETALLES DE LA PELICULA
        if(isInternetAvailable()) {
            String urlPelicula = movieDB.getUrlMovieDB() + "movie/" + idPelicula + "?api_key=" + movieDB.getApiKey() + "&language=es-ES";
            final RequestQueue queueMovies = Volley.newRequestQueue(PeliculaActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPelicula,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                  Gson gson = new Gson();
                  Movie[] arrayMovies = gson.fromJson(response,Movie[].class);
                  List<Movie> listaMovies = Arrays.asList(arrayMovies);
                }
            },
                    new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(movieDB, "Error/Detalles Película", Toast.LENGTH_SHORT).show();
                }
            }); queueMovies.add(stringRequest); }

        // METODO ACTORES DE LA PELICULA
        if(isInternetAvailable()) {
            // https://api.themoviedb.org/3/movie/{movie_id}/credits?api_key=06a1953c26075c04668b820d78955ec7
            String urlActores = movieDB.getUrlMovieDB() + "movie/" + idPelicula + "?/credits?api_key=" + movieDB.getApiKey();
            final RequestQueue queueCastAndCrew = Volley.newRequestQueue(PeliculaActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlActores,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           Gson gson = new Gson();
                           Cast[] arrayActores = gson.fromJson(response,Cast[].class);
                           List<Cast> listaActores  = Arrays.asList(arrayActores);
                           Crew[] arrayEquipo = gson.fromJson(response, Crew[].class);
                           List<Crew> listaEquipo = Arrays.asList(arrayEquipo);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(movieDB, "Error Actores/ Equipo de Trabajo", Toast.LENGTH_SHORT).show();
                        }
                    }); queueCastAndCrew.add(stringRequest);
        }

        // REVIEWS DE LA PELICULA
        if(isInternetAvailable()) {
            // https://api.themoviedb.org/3/movie/475557/reviews?api_key=06a1953c26075c04668b820d78955ec7&language=en-US
            String urlReviews = movieDB.getUrlMovieDB() + "movie/" + idPelicula + "?/reviews?api_key=" + movieDB.getApiKey() + "&language=es-ES";
            final RequestQueue queueReviews = Volley.newRequestQueue(PeliculaActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlReviews,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            Review[] arrayReviews = gson.fromJson(response,Review[].class);
                            List<Review> listaReviews  = Arrays.asList(arrayReviews);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(movieDB, "Error Reviews", Toast.LENGTH_SHORT).show();
                        }
                    }); queueReviews.add(stringRequest);
        }





        


    }

    // Crear metodo para recycler views


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

