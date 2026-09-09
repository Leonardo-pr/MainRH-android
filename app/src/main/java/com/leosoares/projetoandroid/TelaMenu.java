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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TelaMenu extends AppCompatActivity {

    private FloatingActionButton fbInterrogacao2;
    private Button btEntendi;
    private TextView tvUser;
    private CardView cvDuvida;
    private RecyclerView rvListaResposta;
    private EntrevistaAdapter adapter;
    private List<Entrevista> listaEntrevistas;

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

        // Mapeamento dos componentes
        fbInterrogacao2 = findViewById(R.id.fbInterrogacao2);
        btEntendi = findViewById(R.id.btEntendi4);
        cvDuvida = findViewById(R.id.cvDuvida3);
        tvUser = findViewById(R.id.tvUser);
        rvListaResposta = findViewById(R.id.rvListaResposta);

        // Receber o nome do usuário logado
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("NOME_USUARIO")) {
            String nome = intent.getStringExtra("NOME_USUARIO");
            if (nome != null && !nome.isEmpty()) {
                tvUser.setText(nome);
            }
        }

        // Configuração do Card de Ajuda Pop-up
        fbInterrogacao2.setOnClickListener(v -> {
            cvDuvida.animate().cancel();
            cvDuvida.setAlpha(0f);
            cvDuvida.setVisibility(View.VISIBLE);
            cvDuvida.animate().alpha(1f).setDuration(400).setListener(null);
        });

        btEntendi.setOnClickListener(v -> cvDuvida.animate()
                .alpha(0f)
                .setDuration(400)
                .withEndAction(() -> cvDuvida.setVisibility(View.GONE)));

        // Configuração da RecyclerView com a lista que ficava na tela_dinamica
        rvListaResposta.setLayoutManager(new LinearLayoutManager(this));
        listaEntrevistas = carregarEntrevistasEmProcesso();
        adapter = new EntrevistaAdapter(listaEntrevistas);
        rvListaResposta.setAdapter(adapter);
    }

    private List<Entrevista> carregarEntrevistasEmProcesso() {
        List<Entrevista> lista = new ArrayList<>();
        lista.add(new Entrevista("Analista de Sistemas", "Em processo", 12));
        lista.add(new Entrevista("Desenvolvedor Java", "Em processo", 8));
        lista.add(new Entrevista("Engenheiro de Software", "Em processo", 15));
        lista.add(new Entrevista("Designer UX/UI", "Em processo", 5));
        lista.add(new Entrevista("Suporte Técnico", "Em processo", 20));
        return lista;
    }
}