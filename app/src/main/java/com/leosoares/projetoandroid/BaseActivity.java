package com.leosoares.projetoandroid;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applySavedTheme();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Configura os botões globais de acessibilidade sempre que a tela carregar
        configurarBotoesGlobais();
    }

    private void applySavedTheme() {
        int currentTheme = ThemeManager.getSavedTheme(this);

        switch (currentTheme) {
            case ThemeManager.THEME_LIGHT:
                setTheme(R.style.Theme_ProjetoAndroid_Claro);
                break;
            case ThemeManager.THEME_DARK:
                setTheme(R.style.Theme_ProjetoAndroid_Escuro);
                break;
            case ThemeManager.THEME_HIGH_CONTRAST:
                setTheme(R.style.Theme_ProjetoAndroid_AltoContraste);
                break;
            case ThemeManager.THEME_COLORBLIND:
                setTheme(R.style.Theme_ProjetoAndroid_Daltonico);
                break;
        }
    }

    private void configurarBotoesGlobais() {
        ImageButton ibAcessibilidade = findViewById(R.id.ibAcessibilidade);
        if (ibAcessibilidade == null) ibAcessibilidade = findViewById(R.id.ibAcessibilidade2);
        if (ibAcessibilidade == null) ibAcessibilidade = findViewById(R.id.ibAcessibilidade3);

        if (ibAcessibilidade != null) {
            ibAcessibilidade.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }
    }

    private void abrirBottomSheetAcessibilidade() {
        AcessibilidadeBottomSheet dialog = new AcessibilidadeBottomSheet();

        dialog.setListener(tipoTema -> {
            ThemeManager.saveTheme(this, tipoTema);
            recreate();
        });

        dialog.show(getSupportFragmentManager(), "AcessibilidadeBottomSheet");
    }
}