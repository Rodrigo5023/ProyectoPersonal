package com.example.proyectopersonal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectopersonal.Entidades.Cast;
import com.example.proyectopersonal.R;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    Cast[] listaActores;
    Context contexto;

    public CastAdapter (Cast[] listaActores, Context contexto) {
        this.listaActores = listaActores;
        this.contexto = contexto; }

    public static class CastViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreActor;
        public TextView nombrePersonaje;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nombreActor =itemView.findViewById(R.id.textViewNombre);
            this.nombrePersonaje = itemView.findViewById(R.id.textViewPersonaje);}
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexto).inflate(R.layout.adapter_cast_rv,parent,false);
        CastAdapter.CastViewHolder castViewHolder = new CastViewHolder(itemView);
        return castViewHolder; }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {
        final Cast actor = listaActores[position];
        final String nombreActor = actor.getName(); holder.nombreActor.setText(nombreActor);
        final String nombrePersonaje = actor.getCharacter(); holder.nombrePersonaje.setText(nombrePersonaje);
        // Agregar Boton -> RECOMENDACIONES POR ACTOR
    }

    @Override
    public int getItemCount() {
        return listaActores.length; }
}
