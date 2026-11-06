package com.leosoares.projetoandroid;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends BaseActivity {

    private enum ModoTema {
        CLARO,
        ESCURO,
        ALTO_CONTRASTE,
        DALTONICO
    }

    private static final int SPLASH_TIME_OUT = 2500;
    private ModoTema temaAtual = ModoTema.CLARO;
    private RelativeLayout relativeLayout;
    private ImageView imgLogo;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        relativeLayout = findViewById(R.id.RelativeLayout);
        imgLogo = findViewById(R.id.imgLogo);
        progressBar = findViewById(R.id.progressBar);

        if (relativeLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(relativeLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Receber Nome do Usuario
        String nomeUsuario = "";
        Intent intentAnterior = getIntent();
        if (intentAnterior != null && intentAnterior.hasExtra("NOME_USUARIO")) {
            nomeUsuario = intentAnterior.getStringExtra("NOME_USUARIO");
        }

        // Ler diretamente o ThemeManager para o tema salvo
        int temaSalvo = ThemeManager.getSavedTheme(this);
        switch (temaSalvo) {
            case ThemeManager.THEME_DARK:
                temaAtual = ModoTema.ESCURO;
                break;
            case ThemeManager.THEME_HIGH_CONTRAST:
                temaAtual = ModoTema.ALTO_CONTRASTE;
                break;
            case ThemeManager.THEME_COLORBLIND:
                temaAtual = ModoTema.DALTONICO;
                break;
            case ThemeManager.THEME_LIGHT:
            default:
                temaAtual = ModoTema.CLARO;
                break;
        }

        // Aplica o tema visual no Splash Screen
        aplicarTemaSplash(temaAtual);

        final String nomeParaTelaMenu = nomeUsuario;

        // Transição repassando as informações para TelaMenu
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, TelaMenu.class);
            intent.putExtra("NOME_USUARIO", nomeParaTelaMenu);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_TIME_OUT);
    }

    private void aplicarTemaSplash(ModoTema tema) {
        switch (tema) {
            case ALTO_CONTRASTE:
                if (relativeLayout != null) {
                    relativeLayout.setBackgroundColor(Color.parseColor("#0A0F14"));
                }
                if (imgLogo != null) {
                    imgLogo.setImageResource(R.drawable.icone_altocontraste);
                }
                if (progressBar != null) {
                    progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.parseColor("#00E6A1")));
                }
                break;

            case DALTONICO:
                if (relativeLayout != null) {
                    relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                if (imgLogo != null) {
                    imgLogo.setImageResource(R.drawable.icone_daltonismo);
                }
                if (progressBar != null) {
                    progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.parseColor("#0057B8")));
                }
                break;

            case ESCURO:
                if (relativeLayout != null) {
                    relativeLayout.setBackgroundColor(Color.parseColor("#12161A"));
                }
                if (imgLogo != null) {
                    imgLogo.setImageResource(R.drawable.icone_modoescuro);
                }
                if (progressBar != null) {
                    progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.parseColor("#56C8A8")));
                }
                break;

            case CLARO:
            default:
                if (relativeLayout != null) {
                    relativeLayout.setBackgroundColor(Color.parseColor("#E2E2E2"));
                }
                if (imgLogo != null) {
                    imgLogo.setImageResource(R.drawable.iconemain_rh);
                }
                if (progressBar != null) {
                    progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.parseColor("#1A304B")));
                }
                break;
        }
    }
}