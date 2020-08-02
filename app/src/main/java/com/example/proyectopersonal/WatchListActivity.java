package com.example.proyectopersonal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.proyectopersonal.Adapters.MovieAdapter;
import com.example.proyectopersonal.Entidades.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class WatchListActivity extends AppCompatActivity {

    Movie[] listaPelisMiradas;
    int x = 0; int CONDICION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String correoUsuario = user.getEmail();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("WatchList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Long longitud = dataSnapshot.getChildrenCount();
                    int longitudWL = longitud.intValue();
                    ArrayList listaWatchList = new ArrayList<Movie>();

                    for(DataSnapshot children: dataSnapshot.getChildren()){
                        if (dataSnapshot.exists()){
                            final Movie movie = children.getValue(Movie.class);
                            String nombreRaro = children.getKey(); movie.setNombreRaro(nombreRaro);
                            if (movie.getVysor().equals(correoUsuario) ) { listaWatchList.add(movie);}
                        }
                    }
                    listaPelisMiradas = new Movie[longitudWL];

                    for (int x=0; x<longitudWL; x++){
                        listaPelisMiradas[x] = (Movie) listaWatchList.get(x); }

                    MovieAdapter movieAdapter = new MovieAdapter(listaPelisMiradas, WatchListActivity.this,CONDICION,databaseReference,
                            correoUsuario);
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewWatchList);
                    recyclerView.setAdapter(movieAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(WatchListActivity.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantallaprincipal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.PeliculasPopulares:
                startActivity(new Intent(WatchListActivity.this, PeliculasPopularesActivity.class));
                return true;
            case R.id.PeliculasRateadas:
                startActivity(new Intent(WatchListActivity.this, PeliculasTopActivity.class));
                return true;
            case R.id.PeliculasEstreno:
                startActivity(new Intent(WatchListActivity.this, PeliculasEstrenoActivity.class));
                return true;
        }
        return onOptionsItemSelected(item);}

}