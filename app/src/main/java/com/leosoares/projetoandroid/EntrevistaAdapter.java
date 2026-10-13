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

public class EntrevistaAdapter extends RecyclerView.Adapter<EntrevistaAdapter.ViewHolder> {

    private List<Entrevista> listaEntrevistas;

    public EntrevistaAdapter(List<Entrevista> listaEntrevistas) {
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
        Context context = holder.itemView.getContext();
        Entrevista entrevista = listaEntrevistas.get(position);

        holder.tvNomeVaga.setText(entrevista.getNomeVaga());
        holder.tvStatus.setText("Status: " + entrevista.getStatus());
        holder.tvCandidatos.setText(entrevista.getTotalCandidatos() + " candidatos");

        // Mudança da cor do Card Baseado no Tema
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
                holder.tvNomeVaga.setTextColor(Color.WHITE);
                holder.tvStatus.setTextColor(Color.parseColor("#00E6A1"));
                holder.tvCandidatos.setTextColor(Color.parseColor("#FFE600"));
                break;

            case ThemeManager.THEME_COLORBLIND:
                if (holder.cardContainer != null) {
                    GradientDrawable bgDaltonico = new GradientDrawable();
                    bgDaltonico.setShape(GradientDrawable.RECTANGLE);
                    bgDaltonico.setColor(Color.parseColor("#F5F5F5")); // Fundo claro daltônico
                    bgDaltonico.setCornerRadius(24f);
                    bgDaltonico.setStroke(2, Color.parseColor("#0057B8")); // Borda azul acessível
                    holder.cardContainer.setBackground(bgDaltonico);
                }
                holder.tvNomeVaga.setTextColor(Color.parseColor("#0057B8"));
                holder.tvStatus.setTextColor(Color.parseColor("#000000"));
                holder.tvCandidatos.setTextColor(Color.parseColor("#333333"));
                break;

            case ThemeManager.THEME_DARK:
                if (holder.cardContainer != null) {
                    GradientDrawable bgEscuro = new GradientDrawable();
                    bgEscuro.setShape(GradientDrawable.RECTANGLE);
                    bgEscuro.setColor(Color.parseColor("#1E293B"));
                    bgEscuro.setCornerRadius(24f);
                    holder.cardContainer.setBackground(bgEscuro);
                }
                holder.tvNomeVaga.setTextColor(Color.parseColor("#E2E8F0"));
                holder.tvStatus.setTextColor(Color.parseColor("#56C8A8"));
                holder.tvCandidatos.setTextColor(Color.parseColor("#94A3B8"));
                break;

            case ThemeManager.THEME_LIGHT:
            default:
                if (holder.cardContainer != null) {
                    GradientDrawable bgClaro = new GradientDrawable();
                    bgClaro.setShape(GradientDrawable.RECTANGLE);
                    bgClaro.setColor(Color.WHITE);
                    bgClaro.setCornerRadius(24f);
                    holder.cardContainer.setBackground(bgClaro);
                }
                holder.tvNomeVaga.setTextColor(Color.parseColor("#203652"));
                holder.tvStatus.setTextColor(Color.parseColor("#47B192"));
                holder.tvCandidatos.setTextColor(Color.parseColor("#666666"));
                break;
        }

        // Clique para ir até a tela de Candidatos
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TelaCandidatos.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaEntrevistas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomeVaga, tvStatus, tvCandidatos;
        View cardContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomeVaga = itemView.findViewById(R.id.tvNomeVaga);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCandidatos = itemView.findViewById(R.id.tvCandidatos);
            cardContainer = itemView.findViewById(R.id.llCardEntrevista);
        }
    }
}