package com.example.proyectopersonal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityRecord;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectopersonal.Entidades.Cast;
import com.example.proyectopersonal.Entidades.Crew;
import com.example.proyectopersonal.R;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {

    Crew[] listaCrew;
    Context contexto;

    public CrewAdapter (Crew[] listaCrew, Context contexto) {
        this.listaCrew = listaCrew;
        this.contexto = contexto; }

    public static class CrewViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreCrew;
        public TextView departamento;
        public TextView trabajo;

        public CrewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nombreCrew =itemView.findViewById(R.id.textViewNombre);
            this.departamento = itemView.findViewById(R.id.textViewDepartamento);
            this.trabajo = itemView.findViewById(R.id.textViewTrabajo);}
    }

    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(contexto).inflate(R.layout.adapter_crew_rv,parent,false);
        CrewAdapter.CrewViewHolder crewViewHolder = new CrewAdapter.CrewViewHolder(itemView);
        return crewViewHolder; }

    @Override
    public void onBindViewHolder(CrewViewHolder holder, int position) {
        final Crew crew = listaCrew[position];
        final String nombreCrew  = crew.getNombre(); holder.nombreCrew.setText(nombreCrew);
        final String departamento = crew.getDepartamento(); holder.departamento.setText(departamento);
        final String trabajo = crew.getTrabajo(); holder.trabajo.setText(trabajo);
        if (trabajo.equals("director")) { // Agregar RECOMENDACIONES POR DIRECTOR
        }
    }

    @Override
    public int getItemCount() {
        return listaCrew.length; }
}
