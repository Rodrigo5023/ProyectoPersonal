package com.example.proyectopersonal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectopersonal.Entidades.Genero;
import com.example.proyectopersonal.R;

public class GeneroAdapter extends RecyclerView.Adapter<GeneroAdapter.GeneroViewHolder> {

    Genero[] listaGenero;
    Context contexto;

    public GeneroAdapter (Genero[] listaGenero, Context contexto) {
        this.listaGenero = listaGenero;
        this.contexto = contexto; }

    public static class GeneroViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreGenero;

        public GeneroViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nombreGenero =itemView.findViewById(R.id.textViewGenero); }
    }

    @NonNull
    @Override
    public GeneroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexto).inflate(R.layout.adapter_genero_rv,parent,false);
        GeneroViewHolder generoViewHolder = new GeneroViewHolder(itemView);
        return generoViewHolder; }


    @Override
    public void onBindViewHolder(GeneroViewHolder holder, int position) {
        final Genero genero = listaGenero[position];
        final String nombreGenero = genero.getName(); holder.nombreGenero.setText(nombreGenero);
        // Agregar Boton -> RECOMENDACIONES GENERO
    }

    @Override
    public int getItemCount() {
        return listaGenero.length; }
}
