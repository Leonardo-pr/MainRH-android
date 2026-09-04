package com.leosoares.projetoandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class informacao_candidato extends AppCompatActivity {

    RatingBar rbNotaCandidato;
    TextView tvNome;
    FloatingActionButton fbInterrogacao5;
    CardView cvDuvida5;
    TextView tvNomeCandidatoDetalhe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacao_candidato);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fbInterrogacao5 = (FloatingActionButton) findViewById(R.id.fbInterrogacao5);
        cvDuvida5 = (CardView) findViewById(R.id.cvDuvida5);

        fbInterrogacao5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancela qualquer temporizador ativo para evitar bugs se clicar várias vezes
                v.animate().cancel();
                cvDuvida5.animate().cancel();

                // Efeito Surgimento (Fade-in)
                cvDuvida5.setAlpha(0f); // Começa totalmente invisível
                cvDuvida5.setVisibility(View.VISIBLE);
                cvDuvida5.animate()
                        .alpha(1f) // 100% visível
                        .setDuration(400) // Duração do efeito (400 milissegundos)
                        .setListener(null);

                // Temporizador de 4 segundos
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Efeito Sumir (Fade-out)
                        cvDuvida5.animate()
                                .alpha(0f) // Ativar transparencia
                                .setDuration(400)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        cvDuvida5.setVisibility(View.GONE);
                                    }
                                });
                    }
                }, 4000); // 4000 milissegundos = 4 segundos
            }
        });



        tvNomeCandidatoDetalhe = (TextView) findViewById(R.id.tvNomeCandidatoDetalhe);
        Intent intent = getIntent();
        // 3. Verifica se veio algum dado com a chave "NOME_CANDIDATO"
        if (intent != null && intent.hasExtra("NOME_CANDIDATO")) {
            String nomeCandidato = intent.getStringExtra("NOME_CANDIDATO");

            // 4. Define o nome resgatado no TextView da tela
            tvNomeCandidatoDetalhe.setText(nomeCandidato);

        }
    }
}