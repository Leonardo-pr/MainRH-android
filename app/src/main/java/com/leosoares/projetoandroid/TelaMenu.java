package com.leosoares.projetoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class TelaMenu extends AppCompatActivity {

    FloatingActionButton fbDin;
    FloatingActionButton fbEntrevista;
    FloatingActionButton fbInterrogacao2;
    TextView tvProcessoEn;
    TextView tvSaudacao;
    TextView tvOpcoes;
    TextView tvUser;
    TextView tvProcessoDi;
    TextView tvExibido2;
    CardView cvDuvida2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
            });

        fbDin = (FloatingActionButton) findViewById(R.id.fbDin);
        fbEntrevista = (FloatingActionButton) findViewById(R.id.fbEntrevista);
        fbInterrogacao2 = (FloatingActionButton) findViewById(R.id.fbInterrogacao2);
        cvDuvida2 = (CardView) findViewById(R.id.cvDuvida2);
        tvSaudacao = (TextView) findViewById(R.id.tvSaudacao);
        tvUser = (TextView) findViewById(R.id.tvUser);
        tvOpcoes = (TextView) findViewById(R.id.tvOpcoes);
        tvProcessoDi = (TextView) findViewById(R.id.tvProcessoDi);
        tvProcessoEn = (TextView) findViewById(R.id.tvProcessoEn);

        fbInterrogacao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancela qualquer temporizador ativo para evitar bugs se clicar várias vezes
                v.animate().cancel();
                cvDuvida2.animate().cancel();

                // Efeito Surgimento (Fade-in)
                cvDuvida2.setAlpha(0f); // Começa totalmente invisível
                cvDuvida2.setVisibility(View.VISIBLE);
                cvDuvida2.animate()
                        .alpha(1f) // 100% visível
                        .setDuration(400) // Duração do efeito (400 milissegundos)
                        .setListener(null);

                // Temporizador de 4 segundos
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Efeito Sumir (Fade-out)
                        cvDuvida2.animate()
                                .alpha(0f) // Ativar transparencia
                                .setDuration(400)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        cvDuvida2.setVisibility(View.GONE);
                                    }
                                });
                    }
                }, 4000); // 4000 milissegundos = 4 segundos
            }
        });


        fbDin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaMenu.this, tela_dinamica.class);
                startActivity(intent);
            }

        });

        fbEntrevista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaMenu.this, tela_candidatos_dinamica.class);
                startActivity(intent);
            }
        });
    }
}