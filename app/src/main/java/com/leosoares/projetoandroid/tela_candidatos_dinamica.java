package com.leosoares.projetoandroid;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class tela_candidatos_dinamica extends AppCompatActivity {

    RecyclerView rvListaCandidatos;
    CandidatoAdapter adapter;
    List<Candidato> listaCandidatos;
    TextView tvCandi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_candidatos_dinamica);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listaCandidatos = carregarCadidatosEmProcesso();
        rvListaCandidatos = findViewById(R.id.rvListaCandidatos);
        tvCandi = findViewById(R.id.tvCandi);
        rvListaCandidatos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CandidatoAdapter(listaCandidatos);
        rvListaCandidatos.setAdapter(adapter);

    }

    private List<Candidato> carregarCadidatosEmProcesso() {
        List<Candidato> lista = new ArrayList<>();
        // Exemplo de dados
        // Apenas vagas em "Processo" devem ser adicionadas a essa lista
        lista.add(new Candidato("Lucas Luvas Pretas"));
        lista.add(new Candidato("Roberto Robertinho"));
        lista.add(new Candidato("Abner, Abner! ABNEEEEEEEEEEEEER"));
        lista.add(new Candidato("Albert Aeds tems"));
        lista.add(new Candidato("Fruta Frutifera da fruta fruta"));

        return lista;
    }
}