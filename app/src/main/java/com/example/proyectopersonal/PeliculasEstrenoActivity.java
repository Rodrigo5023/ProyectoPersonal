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
import com.example.proyectopersonal.Adapters.MovieAdapter;
import com.example.proyectopersonal.Entidades.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class PeliculasEstrenoActivity extends AppCompatActivity {

    MovieDB movieDB = new MovieDB();
    Movie[] listaMovies;
    int x; int CONDICION = 1;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
    String nombreUsuario = usuario.getDisplayName();
    String correoUsuario = usuario.getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peliculas_estreno);

        // PELICULAS QUE SE VAN A ESTRENAR
        if (isInternetAvailable()) {
            //https://api.themoviedb.org/3/movie/upcoming?api_key=06a1953c26075c04668b820d78955ec7&language=en-US&page=1
            String urlPelicula = movieDB.getUrlMovieDB() + "movie/upcoming?api_key=" + movieDB.getApiKey() + "&language=en-US&page=1";
            final RequestQueue queueMoviesEstreno = Volley.newRequestQueue(PeliculasEstrenoActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPelicula,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            try { JSONObject jsonObject = new JSONObject(response);
                                JSONArray results = (JSONArray) jsonObject.get("results");
                                int tamañoLista = results.length();
                                listaMovies = new Movie[tamañoLista];

                                for ( int x=0; x<tamañoLista; x++){
                                    Movie movie = new Movie();
                                    JSONObject pelicula = (JSONObject) results.get(x);
                                    String idMovie = pelicula.getString("id"); movie.setId(Integer.valueOf(idMovie));
                                    String titleMovie = pelicula.getString("title"); movie.setTitle(titleMovie);
                                    String tituloMovie = pelicula.getString("original_title"); movie.setOriginal_title(tituloMovie);
                                    String descripcionMovie = pelicula.getString("overview");movie.setOverview(descripcionMovie);
                                    String posterMovie = pelicula.getString("poster_path"); movie.setPoster_path(posterMovie);
                                    String lenguajeMovie = pelicula.getString("original_language"); movie.setOriginal_language(lenguajeMovie);
                                    String estrenoMovie = pelicula.getString("release_date"); movie.setRelease_date(estrenoMovie);
                                    String puntuacionMovie = pelicula.getString("vote_average"); movie.setVote_average(puntuacionMovie);
                                    String votosMovie = pelicula.getString("vote_count"); movie.setVote_count(votosMovie);
                                    listaMovies[x] = movie;
                                }
                                final MovieAdapter movieAdapter = new MovieAdapter(listaMovies,PeliculasEstrenoActivity.this,CONDICION,databaseReference,
                                        correoUsuario);
                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewEstreno);
                                recyclerView.setAdapter(movieAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(PeliculasEstrenoActivity.this));
                            }
                            catch (JSONException e) { e.printStackTrace(); }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(PeliculasEstrenoActivity.this, "Error: Rateadas", Toast.LENGTH_SHORT).show();
                        }
                    });
            queueMoviesEstreno.add(stringRequest);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantallaprincipal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.PeliculasPopulares:
                startActivity(new Intent(PeliculasEstrenoActivity.this, PeliculasPopularesActivity.class));
                return true;
            case R.id.PeliculasRateadas:
                startActivity(new Intent(PeliculasEstrenoActivity.this, PeliculasTopActivity.class));
                return true;
            case R.id.PeliculasEstreno:
                startActivity(new Intent(PeliculasEstrenoActivity.this, PeliculasEstrenoActivity.class));
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