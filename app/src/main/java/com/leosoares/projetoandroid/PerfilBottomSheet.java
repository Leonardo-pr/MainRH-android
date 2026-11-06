package com.leosoares.projetoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PerfilBottomSheet extends BottomSheetDialogFragment {

    private String nomeUsuarioAtual;

    // Construtor que recebe o nome do usuário vindo da Activity
    public PerfilBottomSheet(String nomeUsuario) {
        this.nomeUsuarioAtual = nomeUsuario;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_perfil, container, false);

        ImageButton btnFechar = view.findViewById(R.id.btnFecharPerfil);
        TextView tvNome = view.findViewById(R.id.tvNomePerfilModal);
        TextView optionSair = view.findViewById(R.id.optionSair);

        // Define o nome recebido no TextView do modal; se estiver vazio, usa um padrão
        if (nomeUsuarioAtual != null && !nomeUsuarioAtual.trim().isEmpty()) {
            tvNome.setText(nomeUsuarioAtual);
        } else {
            tvNome.setText("Recrutador");
        }

        btnFechar.setOnClickListener(v -> dismiss());

        optionSair.setOnClickListener(v -> {
            dismiss();
            requireActivity().finishAffinity();
            System.exit(0);
        });

        return view;
    }
}