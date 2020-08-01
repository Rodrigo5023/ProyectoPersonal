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
import com.example.proyectopersonal.Adapters.ReviewAdapter;
import com.example.proyectopersonal.Entidades.Movie;
import com.example.proyectopersonal.Entidades.Review;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    MovieDB movieDB = new MovieDB();
    Review[] listaReviews;
    int x;
    String idPelicula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

       idPelicula = getIntent().getStringExtra("idMovie");

        // REVIEWS DE LA PELICULA
        if(isInternetAvailable()) {
            // https://api.themoviedb.org/3/movie/475557/reviews?api_key=06a1953c26075c04668b820d78955ec7&language=en-US
            String urlReviews = movieDB.getUrlMovieDB() + "movie/" + idPelicula + "/reviews?api_key=" + movieDB.getApiKey() + "&language=en-US";
            final RequestQueue queueReviews = Volley.newRequestQueue(ReviewActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlReviews,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            try { JSONObject jsonObject = new JSONObject(response);
                                JSONArray results = (JSONArray) jsonObject.get("results");
                                int tamañoLista = results.length();
                                listaReviews = new Review[tamañoLista];

                                for ( int x=0; x<tamañoLista; x++){
                                    Review review = new Review();
                                    JSONObject revius = (JSONObject) results.get(x);
                                    String idReview = revius.getString("id"); review.setId(idReview);
                                    String autorReview = revius.getString("author"); review.setAuthor(autorReview);
                                    String cuerpoReview = revius.getString("content"); review.setContent(cuerpoReview);
                                    String urlReview = revius.getString("url"); review.setUrl(urlReview);
                                    listaReviews[x] = review;
                                }

                                final ReviewAdapter reviewAdapter = new ReviewAdapter(listaReviews,ReviewActivity.this);
                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewReview);
                                recyclerView.setAdapter(reviewAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));


                            }

                            catch (JSONException e) { e.printStackTrace(); }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ReviewActivity.this, "Error: Review", Toast.LENGTH_SHORT).show();
                        }
                    }); queueReviews.add(stringRequest);
        }


    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantallamovie, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.PeliculaActores:
                Intent intent1 = new Intent(ReviewActivity.this, CastActivity.class);
                intent1.putExtra("idMovie", idPelicula); startActivity(intent1);
                return true;
            case R.id.PeliculaDirector:
                Intent intent2 = new Intent(ReviewActivity.this, CrewActivity.class);
                intent2.putExtra("idMovie", idPelicula); startActivity(intent2);
                return true;
            case R.id.PeliculaRecomendaciones:
                Intent intent3 = new Intent(ReviewActivity.this, RecomendacionesActivity.class);
                intent3.putExtra("idMovie", idPelicula); startActivity(intent3);
                return true;
            case R.id.PeliculaReviews:
                Intent intent4 = new Intent(ReviewActivity.this, ReviewActivity.class);
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