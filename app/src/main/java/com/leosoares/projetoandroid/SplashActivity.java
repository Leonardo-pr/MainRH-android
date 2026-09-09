package com.leosoares.projetoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String nomeUsuario = "";
        Intent intentAnterior = getIntent();
        if (intentAnterior != null && intentAnterior.hasExtra("NOME_USUARIO")) {
            nomeUsuario = intentAnterior.getStringExtra("NOME_USUARIO");
        }

        final String nomeParaTelaMenu = nomeUsuario;

        // Executa a transição
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, TelaMenu.class);
                intent.putExtra("NOME_USUARIO", nomeParaTelaMenu);

                startActivity(intent);

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}