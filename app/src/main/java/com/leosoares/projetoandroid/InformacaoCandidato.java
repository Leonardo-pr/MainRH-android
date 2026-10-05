package com.leosoares.projetoandroid;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
   Gerencia a avaliação das questões do candidato com suporte ao Modo Escuro animado
   e aos modos de Acessibilidade (Daltônico e Alto Contraste).
*/

public class InformacaoCandidato extends AppCompatActivity {

    private enum ModoTema {
        CLARO,
        ESCURO,
        ALTO_CONTRASTE,
        DALTONICO
    }

    private ModoTema temaAtual = ModoTema.CLARO;

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
        if (btThemeToggle3 != null) {
            btThemeToggle3.setOnClickListener(v -> {
                btThemeToggle3.animate().rotationBy(360f).setDuration(400).start();
                if (temaAtual == ModoTema.ESCURO) {
                    aplicarTema(ModoTema.CLARO);
                } else {
                    aplicarTema(ModoTema.ESCURO);
                }
            });
        }

        // Pop-Up de Acessibilidade
        if (ibAcessibilidade5 != null) {
            ibAcessibilidade5.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }

        // Listener da RatingBar
        rbNotaCandidato.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                listaQuestoes.get(indiceAtual).setNota(rating);
                tvNotaNumerica.setText(String.format(java.util.Locale.US, "%.1f", rating));
                atualizarCorNota(rating);
                verificarEstatusBotaoEnviar();
            }
        });

        // Navegação das Questões
        btnProximo.setOnClickListener(v -> {
            if (indiceAtual < listaQuestoes.size() - 1) {
                indiceAtual++;
                atualizarExibicaoQuestao();
            }
        });

        btnAnterior.setOnClickListener(v -> {
            if (indiceAtual > 0) {
                indiceAtual--;
                atualizarExibicaoQuestao();
            }
        });

        // Enviar Avaliação
        btEnviar.setOnClickListener(v -> {
            Toast.makeText(this, "Avaliação finalizada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Fechar o pop-up de dúvida
        if (btEntendi4 != null && cvDuvida4 != null) {
            btEntendi4.setOnClickListener(v -> cvDuvida4.animate()
                    .alpha(0f)
                    .setDuration(400)
                    .withEndAction(() -> cvDuvida4.setVisibility(View.GONE)));
        }
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

        rbNotaCandidato.setRating(qAtual.getNota());
        tvNotaNumerica.setText(String.format(java.util.Locale.US, "%.1f", qAtual.getNota()));
        atualizarCorNota(qAtual.getNota());

        tvContadorQuestao.setText(String.format(java.util.Locale.US, "Questão %d de %d", (indiceAtual + 1), listaQuestoes.size()));

        btnAnterior.setEnabled(indiceAtual > 0);
        btnAnterior.setAlpha(indiceAtual > 0 ? 1.0f : 0.3f);

        btnProximo.setEnabled(indiceAtual < listaQuestoes.size() - 1);
        btnProximo.setAlpha(indiceAtual < listaQuestoes.size() - 1 ? 1.0f : 0.3f);
    }

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
        btEnviar.setAlpha(todasAvaliadas ? 1.0f : 0.5f);
    }

    private void configurarDuvida() {
        if (fbInterrogacao5 != null && cvDuvida4 != null) {
            fbInterrogacao5.setOnClickListener(v -> {
                v.animate().cancel();
                cvDuvida4.animate().cancel();
                cvDuvida4.setAlpha(0f);
                cvDuvida4.setVisibility(View.VISIBLE);
                cvDuvida4.animate().alpha(1f).setDuration(400).setListener(null);

                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    if (cvDuvida4 != null) {
                        cvDuvida4.animate().alpha(0f).setDuration(400).withEndAction(() -> cvDuvida4.setVisibility(View.GONE));
                    }
                }, 4000);
            });
        }
    }

    private void abrirBottomSheetAcessibilidade() {
        AcessibilidadeBottomSheet dialog = new AcessibilidadeBottomSheet();

        dialog.setListener(new AcessibilidadeBottomSheet.OnAcessibilidadeListener() {
            @Override
            public void onToggleAltoContraste() {
                if (temaAtual == ModoTema.ALTO_CONTRASTE) {
                    aplicarTema(ModoTema.CLARO);
                } else {
                    aplicarTema(ModoTema.ALTO_CONTRASTE);
                }
            }

            @Override
            public void onToggleDaltonico() {
                if (temaAtual == ModoTema.DALTONICO) {
                    aplicarTema(ModoTema.CLARO);
                } else {
                    aplicarTema(ModoTema.DALTONICO);
                }
            }
        });

        dialog.show(getSupportFragmentManager(), "AcessibilidadeBottomSheet");
    }

    // Gerenciador de TEMAS
    private void aplicarTema(ModoTema novoTema) {
        this.temaAtual = novoTema;

        switch (novoTema) {
            case ALTO_CONTRASTE:
                if (btThemeToggle3 != null) btThemeToggle3.setImageResource(R.drawable.sol);

                main.setBackgroundColor(Color.parseColor("#0A0F14"));
                if (headerBackground2 != null) headerBackground2.setBackgroundColor(Color.parseColor("#121D28"));

                if (tvNomeCandidatoDetalhe != null) tvNomeCandidatoDetalhe.setTextColor(Color.parseColor("#FFE600"));
                if (tvContadorQuestao != null) tvContadorQuestao.setTextColor(Color.parseColor("#00E6A1"));
                if (tvEnunciado != null) tvEnunciado.setTextColor(Color.WHITE);
                if (tvResposta != null) tvResposta.setTextColor(Color.parseColor("#CBD5E1"));

                if (cvContainerList != null) {
                    cvContainerList.setCardBackgroundColor(Color.parseColor("#1B2B3C"));
                }

                // Card de Dúvida com borda verde neon
                GradientDrawable molduraAltoContraste = new GradientDrawable();
                molduraAltoContraste.setShape(GradientDrawable.RECTANGLE);
                molduraAltoContraste.setColor(Color.parseColor("#1B2B3C"));
                molduraAltoContraste.setCornerRadius(32f);
                molduraAltoContraste.setStroke(4, Color.parseColor("#00E6A1"));

                if (cvDuvida4 != null) cvDuvida4.setBackground(molduraAltoContraste);
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.WHITE);
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#00E6A1"));
                    btEntendi4.setTextColor(Color.parseColor("#0A0F14"));
                }

                if (fbInterrogacao5 != null) {
                    fbInterrogacao5.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B2B3C")));
                    fbInterrogacao5.setImageTintList(ColorStateList.valueOf(Color.parseColor("#00E6A1")));
                }

                Toast.makeText(this, "Alto Contraste Ativado", Toast.LENGTH_SHORT).show();
                break;

                // MODO DALTONICO
            case DALTONICO:
                if (btThemeToggle3 != null) btThemeToggle3.setImageResource(R.drawable.sol);
                break;

            case ESCURO:
                if (btThemeToggle3 != null) btThemeToggle3.setImageResource(R.drawable.lua);

                animarCorFundo(main, "#E2E2E2", "#12161A");
                if (headerBackground2 != null) animarCorFundo(headerBackground2, "#203652", "#1B2430");

                if (cvContainerList != null) animarCorCard(cvContainerList, "#FFFFFF", "#1E293B");
                if (cvDuvida4 != null) animarCorCard(cvDuvida4, "#FFFFFF", "#243144");

                animarCorTexto(tvNomeCandidatoDetalhe, "#203652", "#E2E8F0");
                animarCorTexto(tvContadorQuestao, "#666666", "#94A3B8");
                animarCorTexto(tvEnunciado, "#203652", "#E2E8F0");
                animarCorTexto(tvResposta, "#333333", "#CBD5E1");
                if (tvExibido4 != null) animarCorTexto(tvExibido4, "#212121", "#E2E8F0");

                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }

                if (fbInterrogacao5 != null) {
                    animarCorFAB(fbInterrogacao5, "#FFFFFF", "#243144", "#000000", "#E2E8F0");
                }

                Toast.makeText(this, "Modo Escuro Ativado", Toast.LENGTH_SHORT).show();
                break;

            case CLARO:
            default:
                if (btThemeToggle3 != null) btThemeToggle3.setImageResource(R.drawable.sol);

                main.setBackgroundColor(Color.parseColor("#E2E2E2"));
                if (headerBackground2 != null) headerBackground2.setBackgroundColor(Color.parseColor("#203652"));

                if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.WHITE);
                if (cvDuvida4 != null) cvDuvida4.setCardBackgroundColor(Color.WHITE);

                if (tvNomeCandidatoDetalhe != null) tvNomeCandidatoDetalhe.setTextColor(Color.parseColor("#203652"));
                if (tvContadorQuestao != null) tvContadorQuestao.setTextColor(Color.parseColor("#666666"));
                if (tvEnunciado != null) tvEnunciado.setTextColor(Color.parseColor("#203652"));
                if (tvResposta != null) tvResposta.setTextColor(Color.parseColor("#333333"));
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#212121"));

                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }

                if (fbInterrogacao5 != null) {
                    fbInterrogacao5.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    fbInterrogacao5.setImageTintList(ColorStateList.valueOf(Color.BLACK));
                }

                Toast.makeText(this, "Modo Claro Ativado", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // --- Métodos Auxiliares de Transição Suave (VALUE ANIMATOR) ---

    private void animarCorFundo(View view, String hexInicio, String hexFim) {
        if (view == null) return;
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor(hexInicio), Color.parseColor(hexFim));
        anim.setDuration(400);
        anim.addUpdateListener(animation -> view.setBackgroundColor((int) animation.getAnimatedValue()));
        anim.start();
    }

    private void animarCorCard(CardView card, String hexInicio, String hexFim) {
        if (card == null) return;
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor(hexInicio), Color.parseColor(hexFim));
        anim.setDuration(400);
        anim.addUpdateListener(animation -> card.setCardBackgroundColor((int) animation.getAnimatedValue()));
        anim.start();
    }

    private void animarCorTexto(TextView tv, String hexInicio, String hexFim) {
        if (tv == null) return;
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor(hexInicio), Color.parseColor(hexFim));
        anim.setDuration(400);
        anim.addUpdateListener(animation -> tv.setTextColor((int) animation.getAnimatedValue()));
        anim.start();
    }

    private void animarCorFAB(FloatingActionButton fab, String fundoInicio, String fundoFim, String iconeInicio, String iconeFim) {
        if (fab == null) return;
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