package com.leosoares.projetoandroid;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/*
   InformacaoCandidato:
   Gerencia a avaliação das questões do candidato com suporte ao Modo Escuro animado.
*/

public class InformacaoCandidato extends AppCompatActivity {

    private RatingBar rbNotaCandidato;
    private FloatingActionButton fbInterrogacao5;
    private CardView cvDuvida4, cvContainerList;
    private TextView tvNomeCandidatoDetalhe;
    private TextView tvEnunciado, tvResposta, tvContadorQuestao, tvNotaNumerica, tvExibido4;
    private ImageButton btnAnterior, btnProximo, btThemeToggle3, ibAcessibilidade5;
    private Button btEnviar, btEntendi4;
    private View headerBackground2, main;

    private List<Questao> listaQuestoes = new ArrayList<>();
    private int indiceAtual = 0;
    private boolean modoEscuro = false;
    private boolean modoDaltonico = false;
    private boolean modoAltoContraste = false;


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

        // Mapeamento dos Componentes
        main = findViewById(R.id.main);
        headerBackground2 = findViewById(R.id.headerBackground2);
        ibAcessibilidade5 = findViewById(R.id.ibAcessibilidade5);
        btThemeToggle3 = findViewById(R.id.btThemeToggle3);
        tvNomeCandidatoDetalhe = findViewById(R.id.tvNomeCandidatoDetalhe);
        cvContainerList = findViewById(R.id.cvContainerList);
        tvContadorQuestao = findViewById(R.id.tvContadorQuestao);
        tvEnunciado = findViewById(R.id.tvEnunciado);
        tvResposta = findViewById(R.id.tvResposta);
        btnAnterior = findViewById(R.id.btnAnterior);
        btnProximo = findViewById(R.id.btnProximo);
        tvNotaNumerica = findViewById(R.id.tvNotaNumerica);
        rbNotaCandidato = findViewById(R.id.rbNotaCandidato);
        btEnviar = findViewById(R.id.btEnviar);
        fbInterrogacao5 = findViewById(R.id.fbInterrogacao5);
        cvDuvida4 = findViewById(R.id.cvDuvida4);
        tvExibido4 = findViewById(R.id.tvExibido4);
        btEntendi4 = findViewById(R.id.btEntendi4);

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

        // --- MODO NOTURNO / CLARO ---
        btThemeToggle3.setOnClickListener(v -> {
            modoEscuro = !modoEscuro;

            // Giro do botão Sol/Lua
            btThemeToggle3.animate().rotationBy(360f).setDuration(400).start();

            if (modoEscuro) {
                btThemeToggle3.setImageResource(R.drawable.lua);

                // Transições de Fundo e Cards
                animarCorFundo(main, "#E2E2E2", "#12161A");
                if (headerBackground2 != null) animarCorFundo(headerBackground2, "#203652", "#1B2430");
                if (cvContainerList != null) animarCorCard(cvContainerList, "#FFFFFF", "#1E293B");
                animarCorCard(cvDuvida4, "#FFFFFF", "#243144");

                // Textos
                animarCorTexto(tvNomeCandidatoDetalhe, "#203652", "#E2E8F0");
                animarCorTexto(tvContadorQuestao, "#666666", "#94A3B8");
                animarCorTexto(tvEnunciado, "#203652", "#E2E8F0");
                animarCorTexto(tvResposta, "#333333", "#CBD5E1");
                if (tvExibido4 != null) animarCorTexto(tvExibido4, "#212121", "#E2E8F0");

                // Botão de Ajuda (FAB)
                animarCorFAB(fbInterrogacao5, "#FFFFFF", "#243144", "#000000", "#E2E8F0");

            } else {
                btThemeToggle3.setImageResource(R.drawable.sol);

                // Volta para o Modo Claro
                animarCorFundo(main, "#12161A", "#E2E2E2");
                if (headerBackground2 != null) animarCorFundo(headerBackground2, "#1B2430", "#203652");
                if (cvContainerList != null) animarCorCard(cvContainerList, "#1E293B", "#FFFFFF");
                animarCorCard(cvDuvida4, "#243144", "#FFFFFF");

                // Textos
                animarCorTexto(tvNomeCandidatoDetalhe, "#E2E8F0", "#203652");
                animarCorTexto(tvContadorQuestao, "#94A3B8", "#666666");
                animarCorTexto(tvEnunciado, "#E2E8F0", "#203652");
                animarCorTexto(tvResposta, "#CBD5E1", "#333333");
                if (tvExibido4 != null) animarCorTexto(tvExibido4, "#E2E8F0", "#212121");

                // Botão de Ajuda (FAB)
                animarCorFAB(fbInterrogacao5, "#243144", "#FFFFFF", "#E2E8F0", "#000000");
            }
        });

        if (ibAcessibilidade5 != null) {
            ibAcessibilidade5.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }

        rbNotaCandidato.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
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
        btEntendi4.setOnClickListener(v -> cvDuvida4.animate()
                .alpha(0f)
                .setDuration(400)
                .withEndAction(() -> cvDuvida4.setVisibility(View.GONE)));
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

    // Método auxiliar para definir a cor conforme a nota
    private void atualizarCorNota(float nota) {
        int cor;
        if (nota <= 1.5f) {
            cor = Color.parseColor("#E53935"); // Vermelho (Insuficiente)
        } else if (nota <= 3.0f) {
            cor = Color.parseColor("#FB8C00"); // Laranja / Amarelo (Mediano)
        } else if (nota <= 4.0f) {
            cor = Color.parseColor("#43A047"); // Verde Claro (Bom)
        } else {
            cor = Color.parseColor("#2E7D32"); // Verde Escuro (Excelente)
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

    // --- Métodos Auxiliares de Transição Suave (VALUE ANIMATOR) ---

    private void abrirBottomSheetAcessibilidade() {
        AcessibilidadeBottomSheet dialog = new AcessibilidadeBottomSheet();

        dialog.setListener(new AcessibilidadeBottomSheet.OnAcessibilidadeListener() {
            @Override
            public void onToggleAltoContraste() {
                modoAltoContraste = !modoAltoContraste;
                aplicarAltoContraste(modoAltoContraste);
            }

            @Override
            public void onToggleDaltonico() {
                modoDaltonico = !modoDaltonico;
                aplicarModoDaltonico(modoDaltonico);
            }
        });

        dialog.show(getSupportFragmentManager(), "AcessibilidadeBottomSheet");
    }

    private void aplicarModoDaltonico(boolean ativar) {
        if (ativar) {
            main.setBackgroundColor(Color.parseColor("#FFFFFF"));
            if (headerBackground2 != null) headerBackground2.setBackgroundColor(Color.parseColor("#0057B8"));

            Toast.makeText(this, "Modo Daltônico Ativado", Toast.LENGTH_SHORT).show();
        } else {
            // Restaura cores originais
            if (modoEscuro) {
                main.setBackgroundColor(Color.parseColor("#12161A"));
                if (headerBackground2 != null) headerBackground2.setBackgroundColor(Color.parseColor("#1B2430"));
            } else {
                main.setBackgroundColor(Color.parseColor("#E2E2E2"));
                if (headerBackground2 != null) headerBackground2.setBackgroundColor(Color.parseColor("#203652"));
            }
            Toast.makeText(this, "Modo Daltônico Desativado", Toast.LENGTH_SHORT).show();
        }
    }

    private void aplicarAltoContraste(boolean ativar) {
        if (ativar) {
            main.setBackgroundColor(Color.BLACK);
            if (headerBackground2 != null) headerBackground2.setBackgroundColor(Color.BLACK);

            Toast.makeText(this, "Alto Contraste Ativado", Toast.LENGTH_SHORT).show();
        } else {
            aplicarModoDaltonico(false);
        }
    }

    private void animarCorFundo(View view, String hexInicio, String hexFim) {
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor(hexInicio), Color.parseColor(hexFim));
        anim.setDuration(400);
        anim.addUpdateListener(animation -> view.setBackgroundColor((int) animation.getAnimatedValue()));
        anim.start();
    }

    private void animarCorCard(CardView card, String hexInicio, String hexFim) {
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor(hexInicio), Color.parseColor(hexFim));
        anim.setDuration(400);
        anim.addUpdateListener(animation -> card.setCardBackgroundColor((int) animation.getAnimatedValue()));
        anim.start();
    }

    private void animarCorTexto(TextView tv, String hexInicio, String hexFim) {
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor(hexInicio), Color.parseColor(hexFim));
        anim.setDuration(400);
        anim.addUpdateListener(animation -> tv.setTextColor((int) animation.getAnimatedValue()));
        anim.start();
    }

    private void animarCorFAB(FloatingActionButton fab, String fundoInicio, String fundoFim, String iconeInicio, String iconeFim) {
        ValueAnimator animFundo = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor(fundoInicio), Color.parseColor(fundoFim));
        animFundo.setDuration(400);
        animFundo.addUpdateListener(animation -> fab.setBackgroundTintList(ColorStateList.valueOf((int) animation.getAnimatedValue())));
        animFundo.start();

        ValueAnimator animIcone = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor(iconeInicio), Color.parseColor(iconeFim));
        animIcone.setDuration(400);
        animIcone.addUpdateListener(animation -> fab.setImageTintList(ColorStateList.valueOf((int) animation.getAnimatedValue())));
        animIcone.start();
    }
}