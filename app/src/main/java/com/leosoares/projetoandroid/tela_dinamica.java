package com.leosoares.projetoandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class tela_dinamica extends AppCompatActivity {

    RecyclerView rvListaDinamicas;
    EntrevistaAdapter adapter;
    List<Entrevista> listaEntrevistas;
    FloatingActionButton fbInterrogacao3;
    Button btEntendi3;
    CardView cvDuvida3;
    TextView tvExibido3;
    ImageView ivFotoPerfil2;
    CardView cvFotoPerfil;
    LinearLayout llCardEntrevista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_dinamica);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvListaDinamicas = (RecyclerView) findViewById(R.id.rvListaResposta);
        rvListaDinamicas.setLayoutManager(new LinearLayoutManager(this));
        listaEntrevistas = carregarEntrevistasEmProcesso();
        llCardEntrevista = (LinearLayout) findViewById(R.id.llCardEntrevista);
        ivFotoPerfil2 = (ImageView) findViewById(R.id.ivFotoPerfil2);
        cvFotoPerfil = (CardView) findViewById(R.id.cvFotoPerfil);
        adapter = new EntrevistaAdapter(listaEntrevistas);
        rvListaDinamicas.setAdapter(adapter);
        cvDuvida3 = (CardView) findViewById(R.id.cvDuvida3);
        tvExibido3 = (TextView) findViewById(R.id.tvExibido3);
        btEntendi3 = (Button) findViewById(R.id.btEntendi3);
        fbInterrogacao3 = (FloatingActionButton) findViewById(R.id.fbInterrogacao3);


        // 1. Abrir o pop-up ao clicar no botão de interrogação
        fbInterrogacao3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvDuvida3.animate().cancel();

                // Exibe o card com efeito Fade-in
                cvDuvida3.setAlpha(0f);
                cvDuvida3.setVisibility(View.VISIBLE);
                cvDuvida3.animate()
                        .alpha(1f)
                        .setDuration(400)
                        .setListener(null);
            }
        });

// 2. Fechar o pop-up ao clicar no botão "Entendi"
        btEntendi3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esconde o card com efeito Fade-out
                cvDuvida3.animate()
                        .alpha(0f)
                        .setDuration(400)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                cvDuvida3.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    private List<Entrevista> carregarEntrevistasEmProcesso() {
        List<Entrevista> lista = new ArrayList<>();
        // Exemplo de dados
        // Apenas vagas em "Processo" devem ser adicionadas a essa lista
        lista.add(new Entrevista("Analista de Sistemas", "Em processo", 12));
        lista.add(new Entrevista("Desenvolvedor Java", "Em processo", 8));
        lista.add(new Entrevista("Engenheiro de Software", "Em processo", 15));
        lista.add(new Entrevista("Designer UX/UI", "Em processo", 5));
        lista.add(new Entrevista("Suporte Técnico", "Em processo", 20));

        return lista;
    }
}