package com.leosoares.projetoandroid;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class tela_dinamica extends AppCompatActivity {

    RecyclerView rvListaDinamicas;
    EntrevistaAdapter adapter;
    List<Entrevista> listaEntrevistas;
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