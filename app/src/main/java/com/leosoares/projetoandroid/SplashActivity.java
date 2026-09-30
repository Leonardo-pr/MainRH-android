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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2500;
    private boolean modoEscuro = false;

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

        // Recebe os dados enviados pela TelaLogin e Splash
        String nomeUsuario = "";
        Intent intentAnterior = getIntent();
        if (intentAnterior != null) {
            if (intentAnterior.hasExtra("NOME_USUARIO")) {
                nomeUsuario = intentAnterior.getStringExtra("NOME_USUARIO");
            }
            modoEscuro = intentAnterior.getBooleanExtra("MODO_ESCURO", false);
        }

        final String nomeParaTelaMenu = nomeUsuario;

        // Ajusta as cores se estiver no modo escuro
        if (modoEscuro) {
            if (relativeLayout != null) {
                relativeLayout.setBackgroundColor(Color.parseColor("#12161A"));
            }
            if (progressBar != null) {
                progressBar.setIndeterminateTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            }
        }

        // Transição para a TelaMenu
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, TelaMenu.class);
            intent.putExtra("NOME_USUARIO", nomeParaTelaMenu);
            intent.putExtra("MODO_ESCURO", modoEscuro);

            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_TIME_OUT);
    }
}