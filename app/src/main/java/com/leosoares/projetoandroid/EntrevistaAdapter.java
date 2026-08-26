package com.leosoares.projetoandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EntrevistaAdapter extends RecyclerView.Adapter<EntrevistaAdapter.ViewHolder>{

    private List<Entrevista> listaEntrevistas;

    public EntrevistaAdapter(List<Entrevista> listaEntrevistas){
        this.listaEntrevistas = listaEntrevistas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_entrevista, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entrevista entrevista = listaEntrevistas.get(position);
        holder.tvNomeVaga.setText(entrevista.getNomeVaga());
        holder.tvStatus.setText("Status: " + entrevista.getStatus());
        holder.tvCandidatos.setText(entrevista.getTotalCandidatos() + " candidatos");
    }

    @Override
    public int getItemCount() {
        return listaEntrevistas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNomeVaga, tvStatus, tvCandidatos;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvNomeVaga = itemView.findViewById(R.id.tvNomeVaga);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCandidatos = itemView.findViewById(R.id.tvCandidatos);
        }
    }
}
