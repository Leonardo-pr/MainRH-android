package com.leosoares.projetoandroid;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/*
  TelaCandidatos Proposta:

  Os Cadidatos


 */

public class TelaCandidatos extends AppCompatActivity {

    RecyclerView rvListaCandidatos;
    CandidatoAdapter adapter;
    List<Candidato> listaCandidatos;
    FloatingActionButton fbInterrogacao4;
    Button btEntendi4;
    TextView tvExibido4;
    CardView cvDuvida4;
    TextView tvCandi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_candidatos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listaCandidatos = carregarCadidatosEmProcesso();
        rvListaCandidatos = (RecyclerView) findViewById(R.id.rvListaResposta);
        tvCandi = (TextView) findViewById(R.id.tvCandi);
        rvListaCandidatos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CandidatoAdapter(listaCandidatos);
        rvListaCandidatos.setAdapter(adapter);
        tvExibido4 = (TextView) findViewById(R.id.tvExibido4);
        btEntendi4 = (Button) findViewById(R.id.btEntendi4);
         cvDuvida4 = (CardView) findViewById(R.id.cvDuvida4);
        fbInterrogacao4 = (FloatingActionButton) findViewById(R.id.fbInterrogacao4);

        fbInterrogacao4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvDuvida4.animate().cancel();

                // Exibe o card com efeito Fade-in
                cvDuvida4.setAlpha(0f);
                cvDuvida4.setVisibility(View.VISIBLE);
                cvDuvida4.animate()
                        .alpha(1f)
                        .setDuration(400)
                        .setListener(null);
            }
        });

        // Fechar o pop-up ao clicar no botão "Entendi"
        btEntendi4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esconde o card com efeito Fade-out
                cvDuvida4.animate()
                        .alpha(0f)
                        .setDuration(400)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                cvDuvida4.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }


    private List<Candidato> carregarCadidatosEmProcesso() {
        List<Candidato> lista = new ArrayList<>();
        // Exemplo de dados
        // Apenas vagas em "Processo" devem ser adicionadas a essa lista
        lista.add(new Candidato("Lucas Souza"));
        lista.add(new Candidato("Roberto Carvalho"));
        lista.add(new Candidato("Lucas Wanderley"));
        lista.add(new Candidato("João Silva Santos"));
        lista.add(new Candidato("Aleixo Martins"));

        return lista;
    }
}