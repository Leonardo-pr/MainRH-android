package com.leosoares.projetoandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class informacao_candidato extends AppCompatActivity {

    RatingBar rbNotaCandidato;
    FloatingActionButton fbInterrogacao5;
    CardView cvDuvida4;
    TextView tvNomeCandidatoDetalhe;

    TextView tvEnunciado, tvResposta, tvContadorQuestao, tvNotaNumerica;
    ImageButton btnAnterior, btnProximo;
    Button btEnviar, btEntendi4;

    List<Questao> listaQuestoes = new ArrayList<>();
    int indiceAtual = 0;

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

        fbInterrogacao5 = findViewById(R.id.fbInterrogacao5);
        cvDuvida4 = findViewById(R.id.cvDuvida4);
        tvNomeCandidatoDetalhe = findViewById(R.id.tvNomeCandidatoDetalhe);
        tvNotaNumerica = findViewById(R.id.tvNotaNumerica);
        rbNotaCandidato = findViewById(R.id.rbNotaCandidato);
        tvEnunciado = findViewById(R.id.tvEnunciado);
        tvResposta = findViewById(R.id.tvResposta);
        tvContadorQuestao = findViewById(R.id.tvContadorQuestao);
        btnAnterior = findViewById(R.id.btnAnterior);
        btnProximo = findViewById(R.id.btnProximo);
        btEntendi4 = findViewById(R.id.btEntendi4);
        btEnviar = findViewById(R.id.btEnviar);

        // Lógica do Botão Dúvida (FAB)
        configurarDuvida();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("NOME_CANDIDATO")) {
            String nome = intent.getStringExtra("NOME_CANDIDATO");
            if (nome != null && !nome.isEmpty()) {
                tvNomeCandidatoDetalhe.setText(nome);
            }
        }

        // Carrega Dados MOCK
        carregarQuestoesMock();

        // Exibe Primeira Questão e Desabilita Enviar
        atualizarExibicaoQuestao();
        verificarEstatusBotaoEnviar();

        rbNotaCandidato.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    // Salva a nota no objeto da questão atual
                    listaQuestoes.get(indiceAtual).setNota(rating);

                    // Atualiza o texto da nota na hora (ex: 3.5)
                    tvNotaNumerica.setText(String.format(java.util.Locale.US, "%.1f", rating));

                    // Atualiza a cor (vermelho/amarelo/verde) na hora
                    atualizarCorNota(rating);

                    // Revalida se o botão 'Enviar' pode ser liberado
                    verificarEstatusBotaoEnviar();
                }
            }
        });

        // Clique no Botão Próximo
        btnProximo.setOnClickListener(v -> {
            if (indiceAtual < listaQuestoes.size() - 1) {
                indiceAtual++;
                atualizarExibicaoQuestao();
            }
        });

        // Clique no Botão Anterior
        btnAnterior.setOnClickListener(v -> {
            if (indiceAtual > 0) {
                indiceAtual--;
                atualizarExibicaoQuestao();
            }
        });

        // Clique no Botão Enviar
        btEnviar.setOnClickListener(v -> {
            Toast.makeText(this, "Avaliação finalizada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
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



    private void carregarQuestoesMock() {
        listaQuestoes.add(new Questao("Qual a diferença entre Abstract Class e Interface em Java?",
                "Respondeu que interfaces suportam herança múltipla de tipo e classes abstratas mantêm estado."));
        listaQuestoes.add(new Questao("Como você trata vazamento de memória (Memory Leak) no Android?",
                "Explicou o uso do LeakCanary e evitar referências estáticas de Context."));
        listaQuestoes.add(new Questao("Descreva uma experiência com trabalho em equipe sob pressão.",
                "Citou o projeto de conclusão do curso na faculdade mantendo boa comunicação."));
    }

    private void atualizarExibicaoQuestao() {
        Questao qAtual = listaQuestoes.get(indiceAtual);

        tvEnunciado.setText(qAtual.getEnunciado());
        tvResposta.setText(qAtual.getResposta());

        // Define a nota visual da RatingBar
        rbNotaCandidato.setRating(qAtual.getNota());

        // Força o TextView a mostrar a nota salva da questão (mesmo que seja 0.0)
        tvNotaNumerica.setText(String.format(java.util.Locale.US, "%.1f", qAtual.getNota()));

        // Atualiza a cor correspondente
        atualizarCorNota(qAtual.getNota());

        // Contador Ex: Questão 1 de 3
        tvContadorQuestao.setText(String.format(java.util.Locale.US, "Questão %d de %d", (indiceAtual + 1), listaQuestoes.size()));

        // Habilita/Desabilita as setas laterais
        btnAnterior.setEnabled(indiceAtual > 0);
        btnAnterior.setAlpha(indiceAtual > 0 ? 1.0f : 0.3f);

        btnProximo.setEnabled(indiceAtual < listaQuestoes.size() - 1);
        btnProximo.setAlpha(indiceAtual < listaQuestoes.size() - 1 ? 1.0f : 0.3f);
    }

    // Metodo auxiliar para definir a cor conforme a nota
    private void atualizarCorNota(float nota) {
        int cor;
        if (nota <= 1.5f) {
            cor = android.graphics.Color.parseColor("#E53935"); // Vermelho (Insuficiente)
        } else if (nota <= 3.0f) {
            cor = android.graphics.Color.parseColor("#FB8C00"); // Laranja / Amarelo (Mediano)
        } else if (nota <= 4.0f) {
            cor = android.graphics.Color.parseColor("#43A047"); // Verde Claro (Bom)
        } else {
            cor = android.graphics.Color.parseColor("#2E7D32"); // Verde Escuro (Excelente)
        }

        tvNotaNumerica.setTextColor(cor);
    }

    private void verificarEstatusBotaoEnviar() {
        boolean todasAvaliadas = true;
        for (Questao q : listaQuestoes) {
            if (q.getNota() == 0) {
                todasAvaliadas = false;
                break;
            }
        }
        btEnviar.setEnabled(todasAvaliadas);
        btEnviar.setAlpha(todasAvaliadas ? 1.0f : 0.5f); // Efeito visual de desabilitado
    }

    private void configurarDuvida() {
        fbInterrogacao5.setOnClickListener(v -> {
            v.animate().cancel();
            cvDuvida4.animate().cancel();
            cvDuvida4.setAlpha(0f);
            cvDuvida4.setVisibility(View.VISIBLE);
            cvDuvida4.animate().alpha(1f).setDuration(400).setListener(null);

            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                cvDuvida4.animate().alpha(0f).setDuration(400).withEndAction(() -> cvDuvida4.setVisibility(View.GONE));
            }, 4000);
        });
    }
}