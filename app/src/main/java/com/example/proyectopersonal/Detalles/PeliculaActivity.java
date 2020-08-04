package com.example.proyectopersonal.Detalles;

import androidx.annotation.NonNull;
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
import com.example.proyectopersonal.AddReviewActivity;
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
import com.example.proyectopersonal.WatchListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PeliculaActivity extends AppCompatActivity {

    MovieDB movieDB = new MovieDB();
    ArrayList<Movie> listaPelisMiradas;
    Genero[] listaGeneros;
    String idPelicula;
    Movie movie;
    int condicion;
    String conditionX;
    String nombrePelicula;




    //ImageView moviePoster = findViewById(R.id.imageViewPoster);

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String correoUser = user.getEmail();
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula);

       idPelicula = getIntent().getStringExtra("idMovie");
       conditionX = getIntent().getStringExtra("condition");



        // METODO DETALLES DE LA PELICULA
        if(isInternetAvailable()) {
            String urlPelicula = movieDB.getUrlMovieDB() + "movie/" + idPelicula + "?api_key=" + movieDB.getApiKey() + "&language=es-ES";
            final RequestQueue queueMovies = Volley.newRequestQueue(PeliculaActivity.this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPelicula,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                  Gson gson = new Gson();
                  movie = gson.fromJson(response,Movie.class);



                    // Revisar los nombres de los TextViews
                    TextView movieTitulo = (TextView) findViewById(R.id.textViewTitulo); movieTitulo.setText(movie.getTitle());
                    TextView movieEstreno = (TextView) findViewById(R.id.textView5); movieEstreno.setText(movie.getRelease_date());
                    TextView movieTime = (TextView) findViewById(R.id.textViewDuracion); movieTime.setText(movie.getRuntime());
                    TextView movieRate = (TextView) findViewById(R.id.textViewPopulaaridad); movieRate.setText(movie.getVote_average());
                    TextView movieFrase = (TextView) findViewById(R.id.textViewFrase); movieFrase.setText(movie.getTagline());
                    TextView movieOverview = (TextView) findViewById(R.id.textViewDescripcion); movieOverview.setText(movie.getOverview());

                    String poster = movie.getPoster_path();
                    String urlPoster = movieDB.getUrlPhoto() + poster;
                    publicarImagen(urlPoster);

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
                        nombrePelicula = movie.getTitle();

                        final GeneroAdapter generoAdapter = new GeneroAdapter(listaGeneros, PeliculaActivity.this);
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGeneros);
                        recyclerView.setAdapter(generoAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(PeliculaActivity.this));

                    } catch (JSONException e) { e.printStackTrace(); }


                }
            },
                    new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }); queueMovies.add(stringRequest);
        }

        // Agregar validacion de película
        databaseReference.child("WatchList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listaPelisMiradas = new ArrayList<Movie>();
                    for(DataSnapshot children: dataSnapshot.getChildren()){
                        if (dataSnapshot.exists()){
                            Movie movie1 = children.getValue(Movie.class);
                            boolean cond1 = movie1.getVysor().equals(correoUser);
                            boolean cond2 = movie1.getId() == Integer.valueOf(idPelicula);
                            boolean condX = cond1 & cond2;
                            if (condX) {
                                listaPelisMiradas.add(movie1); }
                        }
                    }
                }  condicion = listaPelisMiradas.size();
                Log.d("condicion",  String.valueOf(condicion));

                final Button botonWatchList = (Button) findViewById(R.id.buttonWatchList);
                if (condicion == 0)
                {
                    TextView textViewWL = (TextView) findViewById(R.id.textViewWL);
                    textViewWL.setText("");

                    botonWatchList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            movie.setVysor(correoUser);
                            databaseReference.child("WatchList").push().setValue(movie);
                            Toast.makeText(PeliculaActivity.this, "Se agregó esta película a su WatchList", Toast.LENGTH_SHORT).show();
                            botonWatchList.setVisibility(View.INVISIBLE);
                        }
                    }); }

                if(condicion > 0) {
                    botonWatchList.setVisibility(View.INVISIBLE);
                    TextView textViewWL = (TextView) findViewById(R.id.textViewWL);
                    textViewWL.setText("Esta película ya se encuentra en su WatchList");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });







        Button botonRecomendaciones = (Button) findViewById(R.id.Recomendaciones);
        botonRecomendaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeliculaActivity.this, RecomendacionesActivity.class);
                int idMovieX = movie.getId();
                String idMovie = String.valueOf(idMovieX);
                intent.putExtra("idMovie", idMovie);
                startActivity(intent);
            }
        });

        Button botonNuevaReview = (Button) findViewById(R.id.buttonNuevoReview);
        botonNuevaReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeliculaActivity.this, AddReviewActivity.class);
                intent.putExtra("idMovie", idPelicula);
                intent.putExtra("nombrePelicula",movie.getTitle());
                startActivity(intent);
            }
        });


    }

    public void publicarImagen (String url){
    Picasso.with(getApplicationContext()).load(url).into((ImageView) findViewById(R.id.imageViewPosterMovie));
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantallamovie, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.PeliculaActores:
                Intent intent1 = new Intent(PeliculaActivity.this, CastActivity.class);
                String idMovie1 = String.valueOf(idPelicula);
                intent1.putExtra("idMovie", idMovie1);
                intent1.putExtra("nombrePelicula", nombrePelicula);startActivity(intent1);

                return true;
            case R.id.PeliculaDirector:
                Intent intent2 = new Intent(PeliculaActivity.this, CrewActivity.class);
                String idMovie2 = String.valueOf(idPelicula);
                intent2.putExtra("idMovie", idMovie2);
                intent2.putExtra("nombrePelicula", nombrePelicula);startActivity(intent2);
                return true;
            case R.id.PeliculaRecomendaciones:
                Intent intent3 = new Intent(PeliculaActivity.this, RecomendacionesActivity.class);
                String idMovie = String.valueOf(idPelicula);
                intent3.putExtra("idMovie", idMovie);
                intent3.putExtra("nombrePelicula", nombrePelicula);startActivity(intent3);
                return true;
            case R.id.PeliculaReviews:
                Intent intent4 = new Intent(PeliculaActivity.this, ReviewActivity.class);
                String idMovie4 = String.valueOf(idPelicula);
                intent4.putExtra("idMovie", idMovie4);
                intent4.putExtra("nombrePelicula", nombrePelicula);
                startActivity(intent4);
                return true;
            case R.id.CerrarSesion:
                FirebaseAuth.getInstance().signOut(); finish();
                startActivity(new Intent(PeliculaActivity.this, MainActivity.class));
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

