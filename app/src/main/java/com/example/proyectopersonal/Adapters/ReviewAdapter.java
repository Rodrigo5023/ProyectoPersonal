package com.example.proyectopersonal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectopersonal.Entidades.Cast;
import com.example.proyectopersonal.Entidades.Review;
import com.example.proyectopersonal.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    Review[] listaReview;
    Context contexto;

    public ReviewAdapter (Review[] listaReview, Context contexto) {
        this.listaReview = listaReview;
        this.contexto = contexto; }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView autorReview;
        public TextView cuerpoReview;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.autorReview =itemView.findViewById(R.id.textViewAutor);
            this.cuerpoReview = itemView.findViewById(R.id.textViewCuerpoComentario);}
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
        String autorReview = review.getAuthor(); holder.autorReview.setText(autorReview);
        String cuerpoReview = review.getContent(); holder.cuerpoReview.setText(cuerpoReview);
    }

    @Override
    public int getItemCount() {
        return listaReview.length;
    }
}
