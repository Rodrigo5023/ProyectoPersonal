package com.example.proyectopersonal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.proyectopersonal.Detalles.PeliculaActivity;
import com.example.proyectopersonal.Entidades.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                intent.putExtra("idPelicula",idPelicula);
                startActivity(intent);

            }
        });






    }
}