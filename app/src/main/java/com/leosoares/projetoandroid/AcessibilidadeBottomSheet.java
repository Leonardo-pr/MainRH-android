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
        void onTemaSelecionado(int tipoTema);
    }

    private OnAcessibilidadeListener listener;

    public void setListener(OnAcessibilidadeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
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
        CardView cvClaro = view.findViewById(R.id.cvClaro);
        CardView cvEscuro = view.findViewById(R.id.cvEscuro);
        View btnFechar = view.findViewById(R.id.btnFechar);

        if (cvAltoContraste != null) {
            cvAltoContraste.setOnClickListener(v -> {
                if (listener != null) listener.onTemaSelecionado(ThemeManager.THEME_HIGH_CONTRAST);
                dismiss();
            });
        }

        if (cvDaltonico != null) {
            cvDaltonico.setOnClickListener(v -> {
                if (listener != null) listener.onTemaSelecionado(ThemeManager.THEME_COLORBLIND);
                dismiss();
            });
        }

        if (cvClaro != null) {
            cvClaro.setOnClickListener(v -> {
                if (listener != null) listener.onTemaSelecionado(ThemeManager.THEME_LIGHT);
                dismiss();
            });
        }

        if (cvEscuro != null) {
            cvEscuro.setOnClickListener(v -> {
                if (listener != null) listener.onTemaSelecionado(ThemeManager.THEME_DARK);
                dismiss();
            });
        }

        if (btnFechar != null) {
            btnFechar.setOnClickListener(v -> dismiss());
        }

        return view;
    }
}