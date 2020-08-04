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
import com.example.proyectopersonal.AddReviewActivity;
import com.example.proyectopersonal.Entidades.Cast;
import com.example.proyectopersonal.Entidades.Crew;
import com.example.proyectopersonal.Entidades.Movie;
import com.example.proyectopersonal.MainActivity;
import com.example.proyectopersonal.MovieDB;
import com.example.proyectopersonal.MyReviews;
import com.example.proyectopersonal.PeliculasEstrenoActivity;
import com.example.proyectopersonal.PeliculasPopularesActivity;
import com.example.proyectopersonal.PeliculasTopActivity;
import com.example.proyectopersonal.R;
import com.example.proyectopersonal.RecomendacionesActivity;
import com.example.proyectopersonal.WatchListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class EquipoDetallesActivity extends AppCompatActivity {

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
                            Crew actor = gson.fromJson(response,Crew.class);

                            TextView nombreActor = (TextView) findViewById(R.id.textViewNombreEquipo); nombreActor.setText(actor.getName());
                            TextView biografiaActor = (TextView) findViewById(R.id.textViewbBiography); biografiaActor.setText(actor.getBiography());
                            ImageView fotoActor = (ImageView) findViewById(R.id.imageViewFotoEquipo); String poster = actor.getProfile_path();
                            String urlPoster = movieDB.getUrlPhoto() + poster;
                            String urlNull = "https://pbs.twimg.com/profile_images/640707118610448384/HMiCeu81.jpg";
                            if (actor.getProfile_path() == null){publicarImagen(urlNull);}
                            else { publicarImagen(urlPoster);}

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
            String urlPelicula = movieDB.getUrlMovieDB() + "person/" + idDirector + "/movie_credits?api_key=" + movieDB.getApiKey() + "&language=en-US";
            final RequestQueue queueMovies = Volley.newRequestQueue(EquipoDetallesActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPelicula,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            try { JSONObject jsonObject = new JSONObject(response);
                                JSONArray cast = (JSONArray) jsonObject.get("cast");
                                int tamañoLista = cast.length();
                                listaMovies = new Movie[tamañoLista];

                                TextView cantidadMovies = (TextView) findViewById(R.id.textViewCantidadEquipo);
                                cantidadMovies.setText(String.valueOf(tamañoLista));

                                for ( int x=0; x<tamañoLista; x++){
                                    Movie movie = new Movie();
                                    JSONObject pelicula = (JSONObject) cast.get(x);
                                    String idMovie = pelicula.getString("id"); movie.setId(Integer.valueOf(idMovie));
                                    Log.d("PeliculaID",  idMovie);
                                    String tituloMovie = pelicula.getString("original_title"); movie.setOriginal_title(tituloMovie);
                                    Log.d("PeliculaTítulo",  tituloMovie);
                                    String descripcionMovie = pelicula.getString("overview");movie.setOverview(descripcionMovie);
                                    String posterMovie = pelicula.getString("poster_path"); movie.setPoster_path(posterMovie);
                                    String lenguajeMovie = pelicula.getString("original_language"); movie.setOriginal_language(lenguajeMovie);
                                    // String duracionMovie = pelicula.getString("runtime"); movie.setRuntime(Integer.valueOf(duracionMovie));
                                    String estrenoMovie = pelicula.getString("release_date"); movie.setRelease_date(estrenoMovie);
                                    String puntuacionMovie = pelicula.getString("vote_average"); movie.setVote_average(puntuacionMovie);
                                    String votosMovie = pelicula.getString("vote_count"); movie.setVote_count(votosMovie);
                                    //String fraseMovie = pelicula.getString("tagline"); movie.setTagline(fraseMovie);
                                    listaMovies[x] = movie;
                                }

                                final MovieAdapter movieAdapter = new MovieAdapter(listaMovies, EquipoDetallesActivity.this, CONDICION,databaseReference,
                                        correoUsuario);
                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewEquipoMovies);
                                recyclerView.setAdapter(movieAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(EquipoDetallesActivity.this));


                            }

                            catch (JSONException e) { e.printStackTrace(); }


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

    public void publicarImagen (String url){
        Glide.with(getApplicationContext()).load(url).into((ImageView) findViewById(R.id.imageViewFotoEquipo));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantallaprincipal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.PeliculasPopulares:
                startActivity(new Intent(EquipoDetallesActivity.this, PeliculasPopularesActivity.class));
                return true;
            case R.id.PeliculasRateadas:
                startActivity(new Intent(EquipoDetallesActivity.this, PeliculasTopActivity.class));
                return true;
            case R.id.PeliculasEstreno:
                startActivity(new Intent(EquipoDetallesActivity.this, PeliculasEstrenoActivity.class));
                return true;
            case R.id.WatchList:
                startActivity(new Intent(EquipoDetallesActivity.this, WatchListActivity.class));
                return true;
            case R.id.Reviews:
                startActivity(new Intent(EquipoDetallesActivity.this, MyReviews.class));
                return true;
            case R.id.CerrarSesion:
                FirebaseAuth.getInstance().signOut(); finish();
                startActivity(new Intent(EquipoDetallesActivity.this, MainActivity.class));
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