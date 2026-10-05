package com.leosoares.projetoandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AcessibilidadeBottomSheet extends BottomSheetDialogFragment {

    public interface OnAcessibilidadeListener {
        void onToggleAltoContraste();
        void onToggleDaltonico();
    }

    private OnAcessibilidadeListener listener;

    public void setListener(OnAcessibilidadeListener listener) {
        this.listener = listener;
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