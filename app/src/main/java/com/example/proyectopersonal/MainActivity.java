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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectopersonal.Adapters.MovieAdapter;
import com.example.proyectopersonal.Entidades.Movie;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MovieDB movieDB = new MovieDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // PELICULAS MAS POPULARES
        if(isInternetAvailable()) {
            //https://api.themoviedb.org/3/movie/popular?api_key=06a1953c26075c04668b820d78955ec7&language=en-US&page=1
            String urlPelicula = movieDB.getUrlMovieDB() + "movie/popular?api_key=" + movieDB.getApiKey() + "&language=en-US&page=1";
            final RequestQueue queueMoviesPopulares = Volley.newRequestQueue(MainActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPelicula,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            Movie[] arrayMovies = gson.fromJson(response,Movie[].class);
                            List<Movie> listMoviesPopulares = Arrays.asList(arrayMovies);

                            final MovieAdapter movieAdapter = new MovieAdapter(arrayMovies,MainActivity.this);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMovies);
                            recyclerView.setAdapter(movieAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Error: Populares", Toast.LENGTH_SHORT).show();
                        }
                    }); queueMoviesPopulares.add(stringRequest); }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantallaprincipal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.PeliculasPopulares:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                return true;
            case R.id.PeliculasRateadas:
                startActivity(new Intent(MainActivity.this, PeliculasTopActivity.class));
                return true;
            case R.id.PeliculasEstreno:
                startActivity(new Intent(MainActivity.this, PeliculasEstrenoActivity.class));
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