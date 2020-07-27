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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectopersonal.Adapters.MovieAdapter;
import com.example.proyectopersonal.Adapters.ReviewAdapter;
import com.example.proyectopersonal.Entidades.Review;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    MovieDB movieDB = new MovieDB();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        final String idPelicula = getIntent().getStringExtra("idMovie");

        // REVIEWS DE LA PELICULA
        if(isInternetAvailable()) {
            // https://api.themoviedb.org/3/movie/475557/reviews?api_key=06a1953c26075c04668b820d78955ec7&language=en-US
            String urlReviews = movieDB.getUrlMovieDB() + "movie/" + idPelicula + "?/reviews?api_key=" + movieDB.getApiKey() + "&language=es-ES";
            final RequestQueue queueReviews = Volley.newRequestQueue(ReviewActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlReviews,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            Review[] arrayReviews = gson.fromJson(response,Review[].class);
                            List<Review> listaReviews  = Arrays.asList(arrayReviews);

                            final ReviewAdapter reviewAdapter = new ReviewAdapter(arrayReviews,ReviewActivity.this);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewReview);
                            recyclerView.setAdapter(reviewAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));

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