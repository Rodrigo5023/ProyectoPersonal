package com.example.proyectopersonal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectopersonal.Entidades.Movie;
import com.example.proyectopersonal.Detalles.PeliculaActivity;
import com.example.proyectopersonal.R;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String urlPhoto = " https://image.tmdb.org/t/p/w600_and_h900_bestv2";

    Movie[] listaMovie;
    Context contexto;

    public MovieAdapter (Movie[] listaMovie, Context contexto) {
        this.listaMovie = listaMovie;
        this.contexto = contexto; }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreMovie;
        public TextView overviewMovie;
        public TextView fechaEstrenoMovie;
        public TextView rateMovie;
        public ImageView posterMovie;
        public Button botonDetalles;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nombreMovie =itemView.findViewById(R.id.textViewNombreActor);
            this.overviewMovie = itemView.findViewById(R.id.textViewOverview);
            this.fechaEstrenoMovie = itemView.findViewById(R.id.textViewEstreno);
            this.rateMovie = itemView.findViewById(R.id.textViewRate);
            this.posterMovie = itemView.findViewById(R.id.imageViewPoster);
            this.botonDetalles = itemView.findViewById(R.id.buttonDetalles);}
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
        String nombreMovie = movie.getOriginal_title(); holder.nombreMovie.setText(nombreMovie);
        String overviewMovie = movie.getOverview(); holder.overviewMovie.setText(overviewMovie);
        String estrenoMovie = movie.getRelease_date(); holder.fechaEstrenoMovie.setText(estrenoMovie);
        String rateMovie = movie.getVote_average(); holder.rateMovie.setText(rateMovie);
        String urlPoster = urlPhoto + movie.getPoster_path();
        publicarImagen(urlPoster, holder);

            holder.botonDetalles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contexto, PeliculaActivity.class);
                    int idMovie = movie.getId();
                    intent.putExtra("idMovie", idMovie);
                    contexto.startActivity(intent);}
            });


    }

    @Override
    public int getItemCount() {
        return listaMovie.length;
    }

    public void publicarImagen (String url, MovieViewHolder holder){
        Glide.with(contexto).load(url).into(holder.posterMovie);
    }



}
