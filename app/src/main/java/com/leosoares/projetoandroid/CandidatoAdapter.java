package com.leosoares.projetoandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CandidatoAdapter extends RecyclerView.Adapter<CandidatoAdapter.ViewHolder>{

    private List<Candidato> listaCandidato;

    public CandidatoAdapter(List<Candidato> listaCandidato){
        this.listaCandidato = listaCandidato;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_candidato, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Candidato candidato = listaCandidato.get(position);
        holder.NomeCandidato.setText(candidato.getTvNomeCandidato());
    }

    @Override
    public int getItemCount() {
        return listaCandidato != null ? listaCandidato.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView NomeCandidato;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            NomeCandidato = itemView.findViewById(R.id.tvNomeCandidato);
        }
    }
}
