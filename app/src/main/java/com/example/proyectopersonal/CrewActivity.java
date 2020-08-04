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
import com.example.proyectopersonal.Adapters.CastAdapter;
import com.example.proyectopersonal.Adapters.CrewAdapter;
import com.example.proyectopersonal.Entidades.Cast;
import com.example.proyectopersonal.Entidades.Crew;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class CrewActivity extends AppCompatActivity {

    MovieDB movieDB = new MovieDB();
    Crew[] listaCrew;
    int x;
    String idPelicula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew);

       idPelicula = getIntent().getStringExtra("idMovie");

        // METODO EQUIPO DE LA PELICULA
        if (isInternetAvailable()) {
            // https://api.themoviedb.org/3/movie/{movie_id}/credits?api_key=06a1953c26075c04668b820d78955ec7
            String urlActores = movieDB.getUrlMovieDB() + "movie/" + idPelicula + "/credits?api_key=" + movieDB.getApiKey();
            final RequestQueue queueCastAndCrew = Volley.newRequestQueue(CrewActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlActores,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            try { JSONObject jsonObject = new JSONObject(response);
                                JSONArray casting = (JSONArray) jsonObject.get("crew");
                                int tamañoLista = casting.length();
                                listaCrew = new Crew[tamañoLista];

                                for ( int x=0; x<tamañoLista; x++){
                                    Crew director = new Crew();
                                    JSONObject crew = (JSONObject) casting.get(x);
                                    String credit_id = crew.getString("credit_id"); director.setCredit_id(credit_id);
                                    String department = crew.getString("department"); director.setDepartment(department);
                                    String id = crew.getString("id"); director.setId(Integer.valueOf(id));
                                    String job = crew.getString("job"); director.setJob(job);
                                    String name = crew.getString("name"); director.setName(name);
                                    String profile_path = crew.getString("profile_path"); director.setProfile_path(profile_path);
                                    listaCrew[x] = director;

                                }

                                final CrewAdapter crewAdapter = new CrewAdapter(listaCrew,CrewActivity.this);
                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewCrew);
                                recyclerView.setAdapter(crewAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(CrewActivity.this));


                            }

                            catch (JSONException e) { e.printStackTrace(); }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CrewActivity.this, "Error:Cast", Toast.LENGTH_SHORT).show();
                        }
                    });
            queueCastAndCrew.add(stringRequest);
        }



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantallamovie, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.PeliculaActores:
                Intent intent1 = new Intent(CrewActivity.this, CastActivity.class);
                intent1.putExtra("idMovie", idPelicula); startActivity(intent1);
                return true;
            case R.id.PeliculaDirector:
                Intent intent2 = new Intent(CrewActivity.this, CrewActivity.class);
                intent2.putExtra("idMovie", idPelicula); startActivity(intent2);
                return true;
            case R.id.PeliculaRecomendaciones:
                Intent intent3 = new Intent(CrewActivity.this, RecomendacionesActivity.class);
                intent3.putExtra("idMovie", idPelicula); startActivity(intent3);
                return true;
            case R.id.PeliculaReviews:
                Intent intent4 = new Intent(CrewActivity.this, ReviewActivity.class);
                intent4.putExtra("idMovie", idPelicula); startActivity(intent4);
                return true;
            case R.id.CerrarSesion:
                FirebaseAuth.getInstance().signOut(); finish();
                startActivity(new Intent(CrewActivity.this, MainActivity.class));
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