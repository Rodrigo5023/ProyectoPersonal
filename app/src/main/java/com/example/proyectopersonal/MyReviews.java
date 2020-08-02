package com.example.proyectopersonal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;

import com.example.proyectopersonal.Adapters.MovieAdapter;
import com.example.proyectopersonal.Adapters.ReviewAdapter;
import com.example.proyectopersonal.Entidades.Movie;
import com.example.proyectopersonal.Entidades.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyReviews extends AppCompatActivity {

    Review[] misReviews;
    int x = 0; int CONDICION = 2;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String nombreFiltro = user.getEmail();
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reviews);


        databaseReference.child("Reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Long longitud = dataSnapshot.getChildrenCount();
                    int longitudRW = longitud.intValue();
                    ArrayList listaReview = new ArrayList<Review>();

                    for(DataSnapshot children: dataSnapshot.getChildren()){
                        if (dataSnapshot.exists()){
                            final Review review = children.getValue(Review.class);
                            String nombreRaro = children.getKey(); review.setNombreRaro(nombreRaro);
                            if (review.getAuthor().equals(nombreFiltro) ) { listaReview.add(review);}
                        }
                    }
                    misReviews = new Review[longitudRW];

                    for (int x=0; x<longitudRW; x++){
                        misReviews[x] = (Review) listaReview.get(x); }

                    ReviewAdapter reviewAdapter = new ReviewAdapter(misReviews, MyReviews.this,CONDICION,databaseReference);
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewMyReviews);
                    recyclerView.setAdapter(reviewAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MyReviews.this));
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
                startActivity(new Intent(MyReviews.this, PeliculasPopularesActivity.class));
                return true;
            case R.id.PeliculasRateadas:
                startActivity(new Intent(MyReviews.this, PeliculasTopActivity.class));
                return true;
            case R.id.PeliculasEstreno:
                startActivity(new Intent(MyReviews.this, PeliculasEstrenoActivity.class));
                return true;
        }
        return onOptionsItemSelected(item);}

}