package com.example.proyectopersonal.Detalles;

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
import android.view.View;
import android.widget.Button;
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
import com.example.proyectopersonal.Adapters.MovieAdapter;
import com.example.proyectopersonal.Entidades.Cast;
import com.example.proyectopersonal.Entidades.Crew;
import com.example.proyectopersonal.Entidades.Genero;
import com.example.proyectopersonal.Entidades.Movie;
import com.example.proyectopersonal.GenreActivity;
import com.example.proyectopersonal.MovieDB;
import com.example.proyectopersonal.R;
import com.example.proyectopersonal.RecomendacionesActivity;
import com.google.gson.Gson;

public class PeliculaActivity extends AppCompatActivity {

    MovieDB movieDB = new MovieDB();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

        final String idPelicula = getIntent().getStringExtra("idMovie");

        // METODO DETALLES DE LA PELICULA
        if(isInternetAvailable()) {
            String urlPelicula = movieDB.getUrlMovieDB() + "movie/" + idPelicula + "?api_key=" + movieDB.getApiKey() + "&language=es-ES";
            final RequestQueue queueMovies = Volley.newRequestQueue(PeliculaActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPelicula,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                  Gson gson = new Gson();
                  Movie movie = gson.fromJson(response,Movie.class);

                    TextView movieTitulo = (TextView) findViewById(R.id.textViewTitulo); movieTitulo.setText(movie.getOriginal_title());
                    TextView movieEstreno = (TextView) findViewById(R.id.textViewEstreno); movieEstreno.setText(movie.getRelease_date());
                    TextView movieTime = (TextView) findViewById(R.id.textViewDuracion); movieTime.setText(movie.getRuntime());
                    TextView movieRate = (TextView) findViewById(R.id.textViewRate); movieRate.setText(movie.getVote_average());
                    TextView movieFrase = (TextView) findViewById(R.id.textViewFrase); movieFrase.setText(movie.getTagline());
                    TextView movieOverview = (TextView) findViewById(R.id.textViewOverview); movieOverview.setText(movie.getOverview());

                    ImageView moviePoster = (ImageView) findViewById(R.id.imageViewPoster); String poster = movie.getPoster_path();
                    String urlPoster = movieDB.getUrlPhoto() + poster;
                    Glide.with(PeliculaActivity.this).load(urlPoster).into(moviePoster);

                    Genero[] listaGeneros = movie.getListaGeneros();
                    final GeneroAdapter movieAdapter = new GeneroAdapter(listaGeneros, PeliculaActivity.this);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGeneros);
                    recyclerView.setAdapter(movieAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PeliculaActivity.this));

                    Button botonRecomendaciones = (Button) findViewById(R.id.Recomendaciones);
                    botonRecomendaciones.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PeliculaActivity.this, RecomendacionesActivity.class);
                            intent.putExtra("idMovie", idPelicula);
                            startActivity(intent);
                        }
                    });
                }
            },
                    new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PeliculaActivity.this, "Error:Movie", Toast.LENGTH_SHORT).show();
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

