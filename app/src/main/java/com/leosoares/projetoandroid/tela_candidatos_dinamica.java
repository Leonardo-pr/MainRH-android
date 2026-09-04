package com.leosoares.projetoandroid;

import android.os.Bundle;
import android.view.View;
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

public class tela_candidatos_dinamica extends AppCompatActivity {

    RecyclerView rvListaCandidatos;
    CandidatoAdapter adapter;
    List<Candidato> listaCandidatos;
    FloatingActionButton fbInterrogacao4;
    CardView cvDuvida4;
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
        rvListaCandidatos = findViewById(R.id.rvListaResposta);
        tvCandi = findViewById(R.id.tvCandi);
        rvListaCandidatos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CandidatoAdapter(listaCandidatos);
        rvListaCandidatos.setAdapter(adapter);
        cvDuvida4 = (CardView) findViewById(R.id.cvDuvida4);
        fbInterrogacao4 = (FloatingActionButton) findViewById(R.id.fbInterrogacao4);

        fbInterrogacao4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancela qualquer temporizador ativo para evitar bugs se clicar várias vezes
                v.animate().cancel();
                cvDuvida4.animate().cancel();

                // Efeito Surgimento (Fade-in)
                cvDuvida4.setAlpha(0f); // Começa totalmente invisível
                cvDuvida4.setVisibility(View.VISIBLE);
                cvDuvida4.animate()
                        .alpha(1f) // 100% visível
                        .setDuration(400) // Duração do efeito (400 milissegundos)
                        .setListener(null);

                // Temporizador de 4 segundos
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Efeito Sumir (Fade-out)
                        cvDuvida4.animate()
                                .alpha(0f) // Ativar transparencia
                                .setDuration(400)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        cvDuvida4.setVisibility(View.GONE);
                                    }
                                });
                    }
                }, 4000); // 4000 milissegundos = 4 segundos
            }
        });

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