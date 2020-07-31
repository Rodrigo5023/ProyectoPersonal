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
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.proyectopersonal.CastActivity;
import com.example.proyectopersonal.CrewActivity;
import com.example.proyectopersonal.Entidades.Cast;
import com.example.proyectopersonal.Entidades.Crew;
import com.example.proyectopersonal.Entidades.Genero;
import com.example.proyectopersonal.Entidades.Movie;
import com.example.proyectopersonal.GenreActivity;
import com.example.proyectopersonal.MainActivity;
import com.example.proyectopersonal.MovieDB;
import com.example.proyectopersonal.PeliculasEstrenoActivity;
import com.example.proyectopersonal.PeliculasTopActivity;
import com.example.proyectopersonal.R;
import com.example.proyectopersonal.RecomendacionesActivity;
import com.example.proyectopersonal.ReviewActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PeliculaActivity extends AppCompatActivity {

    MovieDB movieDB = new MovieDB();
    Genero[] listaGeneros;
    String idPelicula;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

       idPelicula = getIntent().getStringExtra("idMovie");

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

                    // Revisar los nombres de los TextViews
                    TextView movieTitulo = (TextView) findViewById(R.id.textViewTitulo); movieTitulo.setText(movie.getOriginal_title());
                    TextView movieEstreno = (TextView) findViewById(R.id.textView5); movieEstreno.setText(movie.getRelease_date());
                    TextView movieTime = (TextView) findViewById(R.id.textViewDuracion); movieTime.setText(movie.getRuntime());
                    TextView movieRate = (TextView) findViewById(R.id.textViewPopulaaridad); movieRate.setText(movie.getVote_average());
                    TextView movieFrase = (TextView) findViewById(R.id.textViewFrase); movieFrase.setText(movie.getTagline());
                    TextView movieOverview = (TextView) findViewById(R.id.textViewDescripcion); movieOverview.setText(movie.getOverview());

                    ImageView moviePoster = (ImageView) findViewById(R.id.imageViewPoster); String poster = movie.getPoster_path();
                    String urlPoster = movieDB.getUrlPhoto() + poster;
                    Glide.with(PeliculaActivity.this).load(urlPoster).into(moviePoster);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray generos = (JSONArray) jsonObject.get("genres");
                        int tamañoLista = generos.length();
                        listaGeneros = new Genero[tamañoLista];
                        for ( int x=0; x<tamañoLista; x++){
                            Genero genero = new Genero();
                            JSONObject genres = (JSONObject) generos.get(x);
                            String idMovie = genres.getString("id"); genero.setId(Integer.valueOf(idMovie));
                            String nombre = genres.getString("name"); genero.setName(nombre);
                            listaGeneros[x] = genero; }
                        final GeneroAdapter generoAdapter = new GeneroAdapter(listaGeneros, PeliculaActivity.this);
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGeneros);
                        recyclerView.setAdapter(generoAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(PeliculaActivity.this));

                    } catch (JSONException e) { e.printStackTrace(); }

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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantallamovie, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.PeliculaActores:
                Intent intent1 = new Intent(PeliculaActivity.this, CastActivity.class);
                intent1.putExtra("idMovie", idPelicula); startActivity(intent1);
                return true;
            case R.id.PeliculaDirector:
                Intent intent2 = new Intent(PeliculaActivity.this, CrewActivity.class);
                intent2.putExtra("idMovie", idPelicula); startActivity(intent2);
                return true;
            case R.id.PeliculaRecomendaciones:
                Intent intent3 = new Intent(PeliculaActivity.this, RecomendacionesActivity.class);
                intent3.putExtra("idMovie", idPelicula); startActivity(intent3);
                return true;
            case R.id.PeliculaReviews:
                Intent intent4 = new Intent(PeliculaActivity.this, ReviewActivity.class);
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

