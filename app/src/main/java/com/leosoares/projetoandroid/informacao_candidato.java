package com.leosoares.projetoandroid;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class informacao_candidato extends AppCompatActivity {

    RatingBar rbNotaCandidato;
    TextView tvNome;
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