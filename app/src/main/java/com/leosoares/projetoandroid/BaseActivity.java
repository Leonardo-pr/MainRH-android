package com.leosoares.projetoandroid;

import android.os.Bundle;
import android.view.View;
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
        // Configura os botões globais de tema e acessibilidade sempre que a tela carregar
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
        // Botão de alternancia modo noturno e claro simbolos
        ImageButton btThemeToggle = findViewById(R.id.btThemeToggle);
        if (btThemeToggle != null) {
            btThemeToggle.setOnClickListener(v -> {
                btThemeToggle.animate().rotationBy(360f).setDuration(400).start();
                int temaAtual = ThemeManager.getSavedTheme(this);
                if (temaAtual == ThemeManager.THEME_DARK) {
                    ThemeManager.saveTheme(this, ThemeManager.THEME_LIGHT);
                } else {
                    ThemeManager.saveTheme(this, ThemeManager.THEME_DARK);
                }
                recreate();
            });
        }

        // Botão de Acessibilidade presente nas telas
        ImageButton ibAcessibilidade = findViewById(R.id.ibAcessibilidade);
        if (ibAcessibilidade != null) {
            ibAcessibilidade.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }
    }

    private void abrirBottomSheetAcessibilidade() {
        AcessibilidadeBottomSheet dialog = new AcessibilidadeBottomSheet();

        dialog.setListener(new AcessibilidadeBottomSheet.OnAcessibilidadeListener() {
            @Override
            public void onToggleAltoContraste() {
                ThemeManager.saveTheme(BaseActivity.this, ThemeManager.THEME_HIGH_CONTRAST);
                recreate();
            }

            @Override
            public void onToggleDaltonico() {
                ThemeManager.saveTheme(BaseActivity.this, ThemeManager.THEME_COLORBLIND);
                recreate();
            }

            @Override
            public void onTemaSelecionado(int tipoTema) {
                ThemeManager.saveTheme(BaseActivity.this, tipoTema);
                recreate();
            }
        });

        dialog.show(getSupportFragmentManager(), "AcessibilidadeBottomSheet");
    }
}