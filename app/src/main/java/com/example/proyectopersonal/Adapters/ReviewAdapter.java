package com.example.proyectopersonal.Adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectopersonal.Entidades.Cast;
import com.example.proyectopersonal.Entidades.Review;
import com.example.proyectopersonal.R;
import com.google.firebase.database.DatabaseReference;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    Review[] listaReview;
    Context contexto;
    int condicion;
    DatabaseReference databaseReference;

    public ReviewAdapter (Review[] listaReview, Context contexto, int condicion, DatabaseReference databaseReference) {
        this.listaReview = listaReview;
        this.contexto = contexto;
        this.condicion = condicion;
        this.databaseReference = databaseReference;
        }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView autorReview;
        public TextView cuerpoReview;
        public Button botonBorrar;
        public TextView nombrePelicula;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.autorReview =itemView.findViewById(R.id.textViewAutorComentario);
            this.cuerpoReview = itemView.findViewById(R.id.textViewCuerpoComentario);
            this.botonBorrar = itemView.findViewById(R.id.buttonEliminarReview);
            this.nombrePelicula = itemView.findViewById(R.id.textViewPeliMovie);
            }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexto).inflate(R.layout.adapter_review_rv,parent,false);
        ReviewAdapter.ReviewViewHolder reviewViewHolder = new ReviewAdapter.ReviewViewHolder(itemView);
        return reviewViewHolder; }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        final Review review = listaReview[position];
        String movieReview = review.getIdPelicula(); holder.nombrePelicula.setText(movieReview);
        String autorReview = review.getAuthor(); holder.autorReview.setText(autorReview);
        String cuerpoReview = review.getContent(); holder.cuerpoReview.setText(cuerpoReview);

        if (condicion == 1) { holder.botonBorrar.setVisibility(View.INVISIBLE);}
        if (condicion == 2) {
            holder.botonBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child("Reviews").child(review.getNombreRaro()).removeValue();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return listaReview.length;
    }
}
