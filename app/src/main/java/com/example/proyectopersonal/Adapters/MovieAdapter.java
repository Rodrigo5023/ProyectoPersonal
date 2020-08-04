package com.example.proyectopersonal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyectopersonal.Entidades.Movie;
import com.example.proyectopersonal.Detalles.PeliculaActivity;
import com.example.proyectopersonal.PeliculasPopularesActivity;
import com.example.proyectopersonal.R;
import com.example.proyectopersonal.WatchListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    ArrayList<Movie> listaPelisMiradas;

    private static final String urlPhoto = "https://image.tmdb.org/t/p/w600_and_h900_bestv2";

    int condition;

    DatabaseReference databaseReference;
    int condicion;
    Movie[] listaMovie;
    Context contexto;
    String correoUser;

    public MovieAdapter (Movie[] listaMovie, Context contexto,int condicion, DatabaseReference databaseReference
    , String correoUser) {
        this.listaMovie = listaMovie;
        this.contexto = contexto;
        this.condicion = condicion;
        this.databaseReference = databaseReference;
        this.correoUser = correoUser;}

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreMovie;
        public TextView overviewMovie;
        public TextView fechaEstrenoMovie;
        public TextView rateMovie;
        public ImageView posterMovie;
        public Button botonDetalles;
        public Button botonBorrar;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nombreMovie =itemView.findViewById(R.id.textViewAutor);
            this.overviewMovie = itemView.findViewById(R.id.textViewOverview);
            this.fechaEstrenoMovie = itemView.findViewById(R.id.textViewEstreno);
            this.rateMovie = itemView.findViewById(R.id.textViewRate);
            this.posterMovie = itemView.findViewById(R.id.imageViewMovieUnica);
            this.botonDetalles = itemView.findViewById(R.id.buttonDetalles);
            this.botonBorrar = itemView.findViewById(R.id.buttonEliminarMovie);}
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexto).inflate(R.layout.adapter_movie_rv,parent,false);
        MovieAdapter.MovieViewHolder movieViewHolder = new MovieAdapter.MovieViewHolder(itemView);
        return movieViewHolder; }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = listaMovie[position];
        String nombreMovie = movie.getTitle(); holder.nombreMovie.setText(nombreMovie);
        String overviewMovie = movie.getOverview(); holder.overviewMovie.setText(overviewMovie);
        String estrenoMovie = movie.getRelease_date(); holder.fechaEstrenoMovie.setText(estrenoMovie);
        String rateMovie = movie.getVote_average(); holder.rateMovie.setText(rateMovie);
        String urlPoster = urlPhoto + movie.getPoster_path();
        publicarImagen(urlPoster, holder);


            holder.botonDetalles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contexto, PeliculaActivity.class);
                    int idMovieX = movie.getId();
                    String idMovie = String.valueOf(idMovieX);
                    intent.putExtra("idMovie", idMovie);
                    intent.putExtra("condition", String.valueOf(condition));
                    contexto.startActivity(intent);}
            });

            if (condicion == 1) {holder.botonBorrar.setVisibility(View.INVISIBLE);}
            if (condicion == 2) {
                holder.botonBorrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child("WatchList").child(movie.getNombreRaro()).removeValue();
                    }
                });

            }





    }

    @Override
    public int getItemCount() {
        return listaMovie.length;
    }

    public void publicarImagen (String url, MovieAdapter.MovieViewHolder holder){
        Glide.with(contexto).load(url).into(holder.posterMovie);


    }



}
