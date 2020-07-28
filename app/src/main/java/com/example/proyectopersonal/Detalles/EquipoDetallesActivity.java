package com.example.proyectopersonal.Detalles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.proyectopersonal.Adapters.MovieAdapter;
import com.example.proyectopersonal.Entidades.Cast;
import com.example.proyectopersonal.Entidades.Crew;
import com.example.proyectopersonal.Entidades.Movie;
import com.example.proyectopersonal.MovieDB;
import com.example.proyectopersonal.R;
import com.google.gson.Gson;

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

import java.util.Arrays;
import java.util.List;


public class EquipoDetallesActivity extends AppCompatActivity {

    MovieDB movieDB = new MovieDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipo_detalles);

        final String idDirector = getIntent().getStringExtra("idDirector");


        // OBTENER EQUIPO
        if(isInternetAvailable()) {
            //https://api.themoviedb.org/3/person/179?api_key=06a1953c26075c04668b820d78955ec7&language=en-US
            String urlPelicula = movieDB.getUrlMovieDB() + "person/" + idDirector + "?api_key=" + movieDB.getApiKey() + "&language=en-US";
            final RequestQueue queueMovies = Volley.newRequestQueue(EquipoDetallesActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPelicula,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            Crew crew = gson.fromJson(response,Crew.class);

                            TextView nombreEquipo = (TextView) findViewById(R.id.textViewNombreEquipo); nombreEquipo.setText(crew.getName());
                            TextView biografiaEquipo = (TextView) findViewById(R.id.textViewbBiography); biografiaEquipo.setText(crew.getBiography());
                            ImageView fotoEquipo = (ImageView) findViewById(R.id.imageViewFotoActor); String poster = crew.getProfile_path();
                            String urlPoster = movieDB.getUrlPhoto() + poster;
                            String urlNull = "https://pbs.twimg.com/profile_images/640707118610448384/HMiCeu81.jpg";
                            if (poster != null){ Glide.with(EquipoDetallesActivity.this).load(urlPoster).into(fotoEquipo);}
                            else { Glide.with(EquipoDetallesActivity.this).load(urlNull).into(fotoEquipo);}
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(EquipoDetallesActivity.this, "Error:Movie", Toast.LENGTH_SHORT).show();
                        }
                    }); queueMovies.add(stringRequest);
        }




        // OBTENER PELICULAS POR EQUIPO
        if(isInternetAvailable()) {
            //https://api.themoviedb.org/3/person/190/movie_credits?api_key=06a1953c26075c04668b820d78955ec7&language=en-US
            String urlPelicula = movieDB.getUrlMovieDB() + "person/" + idDirector + "?api_key=" + movieDB.getApiKey() + "&language=en-US";
            final RequestQueue queueMovies = Volley.newRequestQueue(EquipoDetallesActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPelicula,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            Movie[] arrayMovies = gson.fromJson(response,Movie[].class);
                            List<Movie> listaMovies = Arrays.asList(arrayMovies);

                            final MovieAdapter movieAdapter = new MovieAdapter(arrayMovies, EquipoDetallesActivity.this);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewActorMovies);
                            recyclerView.setAdapter(movieAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(EquipoDetallesActivity.this));

                            TextView cantidadMovies = (TextView) findViewById(R.id.textViewCantidadEquipo);
                            int tamañoLista = arrayMovies.length; cantidadMovies.setText(tamañoLista);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(EquipoDetallesActivity.this, "Error:Movie", Toast.LENGTH_SHORT).show();
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