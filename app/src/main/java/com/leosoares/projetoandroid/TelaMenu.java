package com.leosoares.projetoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button btEntendi;
    TextView tvProcessoEn;
    TextView tvSaudacao;
    TextView tvOpcoes;
    TextView tvUser;
    TextView tvProcessoDi;
    TextView tvExibido;
    CardView cvDuvida;


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
        btEntendi = (Button) findViewById(R.id.btEntendi3);
        tvExibido = (TextView) findViewById(R.id.tvExibido3);
        cvDuvida = (CardView) findViewById(R.id.cvDuvida3);
        tvSaudacao = (TextView) findViewById(R.id.tvSaudacao);
        tvUser = (TextView) findViewById(R.id.tvUser);
        tvOpcoes = (TextView) findViewById(R.id.tvOpcoes);
        tvProcessoDi = (TextView) findViewById(R.id.tvProcessoDi);
        tvProcessoEn = (TextView) findViewById(R.id.tvProcessoEn);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("NOME_USUARIO")){
            String usuario = intent.getStringExtra("NOME_USUARIO");

            if(!usuario.isEmpty()){
                tvUser.setText(usuario);
            } else {
                tvUser.setText("Usuário");
            }
        }

        // 1. Abrir o pop-up ao clicar no botão de interrogação
        fbInterrogacao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvDuvida.animate().cancel();

                // Exibe o card com efeito Fade-in
                cvDuvida.setAlpha(0f);
                cvDuvida.setVisibility(View.VISIBLE);
                cvDuvida.animate()
                        .alpha(1f)
                        .setDuration(400)
                        .setListener(null);
            }
        });

// 2. Fechar o pop-up ao clicar no botão "Entendi"
        btEntendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esconde o card com efeito Fade-out
                cvDuvida.animate()
                        .alpha(0f)
                        .setDuration(400)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                cvDuvida.setVisibility(View.GONE);
                            }
                        });
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