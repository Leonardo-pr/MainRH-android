package com.leosoares.projetoandroid;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

public class AcessibilidadeBottomSheet extends DialogFragment {

    public interface OnAcessibilidadeListener {
        void onToggleAltoContraste();
        void onToggleDaltonico();
    }

    private OnAcessibilidadeListener listener;

    public void setListener(OnAcessibilidadeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Centraliza o Dialog e deixa o fundo externo transparente
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_acessibilidade, container, false);

        CardView cvAltoContraste = view.findViewById(R.id.cvAltoContraste);
        CardView cvDaltonico = view.findViewById(R.id.cvDaltonico);
        View btnFechar = view.findViewById(R.id.btnFechar);

        if (cvAltoContraste != null) {
            cvAltoContraste.setOnClickListener(v -> {
                if (listener != null) listener.onToggleAltoContraste();
                dismiss();
            });
        }

        if (cvDaltonico != null) {
            cvDaltonico.setOnClickListener(v -> {
                if (listener != null) listener.onToggleDaltonico();
                dismiss();
            });
        }

        if (btnFechar != null) {
            btnFechar.setOnClickListener(v -> dismiss());
        }

        return view;
    }
}