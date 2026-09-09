package com.leosoares.projetoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HubCandidatoActivity extends AppCompatActivity {

    private ImageButton btEntrevista, btDin;
    private TextView tvProcessoEn, tvProcessoDi, tvNomeCandidato;
    CardView cvDuvida3;
    FloatingActionButton fbInterrogacao3;
    Button btEntendi4;
    private boolean passouEntrevista = true;
    private boolean realizouEntrevista = true;

    private boolean passouDinamica = false;
    private boolean realizouDinamica = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_candidato);

        btEntendi4 = findViewById(R.id.btEntendi4);
        fbInterrogacao3 = findViewById(R.id.fbInterrogacao3);
        cvDuvida3 = findViewById(R.id.cvDuvida3);
        btEntrevista = findViewById(R.id.btEntrevista);
        btDin = findViewById(R.id.btDin);
        tvProcessoEn = findViewById(R.id.tvProcessoEn);
        tvProcessoDi = findViewById(R.id.tvProcessoDi);
        tvNomeCandidato = findViewById(R.id.tvNomeCandidato);

        // Recebe o nome do candidato enviado pela tela anterior
        Intent intentRecebida = getIntent();
        if (intentRecebida != null && intentRecebida.hasExtra("NOME_CANDIDATO")) {
            String nome = intentRecebida.getStringExtra("NOME_CANDIDATO");
            if (nome != null && !nome.isEmpty()) {
                tvNomeCandidato.setText(nome);
            }
        }

        fbInterrogacao3.setOnClickListener(v -> {
            cvDuvida3.animate().cancel();
            cvDuvida3.setAlpha(0f);
            cvDuvida3.setVisibility(View.VISIBLE);
            cvDuvida3.animate().alpha(1f).setDuration(400).setListener(null);
        });

        btEntendi4.setOnClickListener(v -> cvDuvida3.animate()
                .alpha(0f)
                .setDuration(400)
                .withEndAction(() -> cvDuvida3.setVisibility(View.GONE)));

        // 3. Listeners dos botões
        btEntrevista.setOnClickListener(v -> {
            Intent intent = new Intent(HubCandidatoActivity.this, InformacaoCandidato.class);
            intent.putExtra("TIPO_PROCESSO", "ENTREVISTA");
            intent.putExtra("NOME_CANDIDATO", tvNomeCandidato.getText().toString());
            startActivity(intent);
        });

        btDin.setOnClickListener(v -> {
            Intent intent = new Intent(HubCandidatoActivity.this, InformacaoCandidato.class);
            intent.putExtra("TIPO_PROCESSO", "DINAMICA");
            intent.putExtra("NOME_CANDIDATO", tvNomeCandidato.getText().toString());
            startActivity(intent);
        });
    }

}