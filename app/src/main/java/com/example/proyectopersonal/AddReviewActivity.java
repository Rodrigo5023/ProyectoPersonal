package com.example.proyectopersonal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.proyectopersonal.Detalles.PeliculaActivity;
import com.example.proyectopersonal.Entidades.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class AddReviewActivity extends AppCompatActivity {

    Review review = new Review();
    String idPelicula;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        idPelicula = getIntent().getStringExtra("idMovie");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String nombreFiltro = user.getEmail();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        review.setAuthor(nombreFiltro);
        review.setIdPelicula(idPelicula);

        Button botonAgregar = (Button) findViewById(R.id.buttonAdd);
        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText cuerpoComentario =  (EditText) findViewById(R.id.editTextAdd);
                String cuerpoReview = cuerpoComentario.getText().toString();
                review.setContent(cuerpoReview);
                databaseReference.child("Reviews").push().setValue(review);
                Intent intent = new Intent(AddReviewActivity.this, PeliculaActivity.class);
                intent.putExtra("idMovie",idPelicula);
                startActivity(intent);

            }
        });






    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pantallaprincipal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.PeliculasPopulares:
                startActivity(new Intent(AddReviewActivity.this, PeliculasPopularesActivity.class));
                return true;
            case R.id.PeliculasRateadas:
                startActivity(new Intent(AddReviewActivity.this, PeliculasTopActivity.class));
                return true;
            case R.id.PeliculasEstreno:
                startActivity(new Intent(AddReviewActivity.this, PeliculasEstrenoActivity.class));
                return true;
            case R.id.WatchList:
                startActivity(new Intent(AddReviewActivity.this, WatchListActivity.class));
                return true;
            case R.id.Reviews:
                startActivity(new Intent(AddReviewActivity.this, MyReviews.class));
                return true;
            case R.id.CerrarSesion:
                FirebaseAuth.getInstance().signOut(); finish();
                startActivity(new Intent(AddReviewActivity.this, MainActivity.class));
                return true;

        }
        return onOptionsItemSelected(item);}

}