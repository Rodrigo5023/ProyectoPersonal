package com.example.proyectopersonal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectopersonal.Detalles.ActorDetallesActivity;
import com.example.proyectopersonal.Detalles.EquipoDetallesActivity;
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
        public Button botonVerMas;

        public CrewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nombreCrew =itemView.findViewById(R.id.textViewNombreEquipo);
            this.departamento = itemView.findViewById(R.id.textViewDepartamento);
            this.trabajo = itemView.findViewById(R.id.textViewTrabajo);
            this.botonVerMas = itemView.findViewById(R.id.buttonDirector);
        }
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
        final String nombreCrew  = crew.getName(); holder.nombreCrew.setText(nombreCrew);
        final String departamento = crew.getDepartment(); holder.departamento.setText(departamento);
        final String trabajo = crew.getJob(); holder.trabajo.setText(trabajo);

        holder.botonVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contexto, EquipoDetallesActivity.class);
                int idDirector = crew.getId();
                intent.putExtra("idDirector", idDirector);
                contexto.startActivity(intent);}
        });



        }


    @Override
    public int getItemCount() {
        return listaCrew.length; }
}
