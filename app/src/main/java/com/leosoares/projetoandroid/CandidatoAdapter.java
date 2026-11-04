package com.leosoares.projetoandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CandidatoAdapter extends RecyclerView.Adapter<CandidatoAdapter.ViewHolder> {

    private List<Candidato> listaCandidatos;

    public CandidatoAdapter(List<Candidato> listaCandidatos) {
        this.listaCandidatos = listaCandidatos;
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
        Context context = holder.itemView.getContext();
        Candidato candidato = listaCandidatos.get(position);

        holder.tvNomeCandidato.setText(candidato.getTvNomeCandidato());

        // Mudança na cor do Card Baseado no Tema
        int temaSalvo = ThemeManager.getSavedTheme(context);

        switch (temaSalvo) {
            case ThemeManager.THEME_HIGH_CONTRAST:
                if (holder.cardContainer != null) {
                    GradientDrawable border = new GradientDrawable();
                    border.setShape(GradientDrawable.RECTANGLE);
                    border.setColor(Color.parseColor("#1B2B3C"));
                    border.setCornerRadius(24f);
                    border.setStroke(3, Color.parseColor("#00E6A1"));
                    holder.cardContainer.setBackground(border);
                }
                holder.tvNomeCandidato.setTextColor(Color.WHITE);
                break;

            case ThemeManager.THEME_DARK:
                if (holder.cardContainer != null) {
                    GradientDrawable bgEscuro = new GradientDrawable();
                    bgEscuro.setShape(GradientDrawable.RECTANGLE);
                    bgEscuro.setColor(Color.parseColor("#1E293B"));
                    bgEscuro.setCornerRadius(24f);
                    holder.cardContainer.setBackground(bgEscuro);
                }
                holder.tvNomeCandidato.setTextColor(Color.parseColor("#E2E8F0"));
                break;

            case ThemeManager.THEME_COLORBLIND:
            case ThemeManager.THEME_LIGHT:
            default:
                if (holder.cardContainer != null) {
                    GradientDrawable bgClaro = new GradientDrawable();
                    bgClaro.setShape(GradientDrawable.RECTANGLE);
                    bgClaro.setColor(Color.WHITE);
                    bgClaro.setCornerRadius(24f);
                    holder.cardContainer.setBackground(bgClaro);
                }
                holder.tvNomeCandidato.setTextColor(Color.parseColor("#203652"));
                break;
        }

        // Clique para abrir o Hub do Candidato
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HubCandidatoActivity.class);
            intent.putExtra("NOME_CANDIDATO", candidato.getTvNomeCandidato());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaCandidatos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeCandidato;
        View cardContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeCandidato = itemView.findViewById(R.id.tvNomeCandidato);
            cardContainer = itemView.findViewById(R.id.llCardCandidato);
        }
    }
}