package com.leosoares.projetoandroid;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/*
   TelaLogin:
   Gerencia a autenticação de usuários, exibição de senha e alternância
   entre Modo Claro, Modo Escuro, Alto Contraste e Modo Daltônico
   de forma mutuamente exclusiva para evitar conflitos de estilo.
*/

public class TelaLogin extends AppCompatActivity {

    // Enum para controlar os estados de temas da aplicação
    enum ModoTema {
        CLARO,
        ESCURO,
        ALTO_CONTRASTE,
        DALTONICO
    }

    private ModoTema temaAtual = ModoTema.CLARO;
    ImageButton ibOcultar, btThemeToggle, ibAcessibilidade;
    private ImageView ivLogo;
    private Button btEntendi4, btEntrar;
    private View headerBackground3, main;
    private CardView cvDuvida3, cardView;
    private TextView tvExibido4, tvAtencao;
    private FloatingActionButton fbInterrogacao;
    private EditText edUsuario, edSenha;
    private FrameLayout frameLayout;

    private boolean isVisivel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Componentes
        main = findViewById(R.id.main);
        cardView = findViewById(R.id.cardView);
        headerBackground3 = findViewById(R.id.headerBackground3);
        ivLogo = findViewById(R.id.ivLogo);
        ibOcultar = findViewById(R.id.ibOcultar);
        ibAcessibilidade = findViewById(R.id.ibAcessibilidade);
        edSenha = findViewById(R.id.edSenha);
        edUsuario = findViewById(R.id.edUsuario);
        btEntrar = findViewById(R.id.btEntrar);
        fbInterrogacao = findViewById(R.id.fbInterrogacao);
        cvDuvida3 = findViewById(R.id.cvDuvida3);
        tvExibido4 = findViewById(R.id.tvExibido4);
        tvAtencao = findViewById(R.id.tvAtencao);
        btEntendi4 = findViewById(R.id.btEntendi4);
        btThemeToggle = findViewById(R.id.btThemeToggle);

        // Botão modo claro e modo escuro
        btThemeToggle.setOnClickListener(v -> {
            btThemeToggle.animate().rotationBy(360f).setDuration(400).start();

            if (temaAtual == ModoTema.ESCURO) {
                aplicarTema(ModoTema.CLARO);
            } else {
                aplicarTema(ModoTema.ESCURO);
            }
        });

        // Pop up Acessibilidade
        if (ibAcessibilidade != null) {
            ibAcessibilidade.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }

        // Pop up Ajuda
        fbInterrogacao.setOnClickListener(v -> {
            cvDuvida3.animate().cancel();
            cvDuvida3.setAlpha(0f);
            cvDuvida3.setVisibility(View.VISIBLE);
            cvDuvida3.animate().alpha(1f).setDuration(400).setListener(null);
        });

        // Fechar Pop-up de Ajuda
        btEntendi4.setOnClickListener(v -> cvDuvida3.animate()
                .alpha(0f)
                .setDuration(400)
                .withEndAction(() -> cvDuvida3.setVisibility(View.GONE)));

        // Exibir / Ocultar Senha
        ibOcultar.setOnClickListener(v -> {
            if (isVisivel) {
                edSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ibOcultar.setImageResource(R.drawable.exibirsenha);
                isVisivel = false;
            } else {
                edSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ibOcultar.setImageResource(R.drawable.ocultarsenha);
                isVisivel = true;
            }
            edSenha.setSelection(edSenha.getText().length());
        });

        // Botão Entrar
        btEntrar.setOnClickListener(v -> {
            String nomeUsuario = edUsuario.getText().toString().trim();
            Intent intent = new Intent(TelaLogin.this, SplashActivity.class);
            intent.putExtra("NOME_USUARIO", nomeUsuario);
            intent.putExtra("TEMA_ATUAL", temaAtual.name());
            startActivity(intent);
        });
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
                btThemeToggle.setImageResource(R.drawable.sol);
                main.setBackgroundColor(Color.parseColor("#0A0F14"));
                if (ivLogo != null) ivLogo.setImageResource(R.drawable.altocontraste_logo);
                if (headerBackground3 != null) headerBackground3.setBackgroundColor(Color.parseColor("#121D28"));
                if (frameLayout != null) frameLayout.setBackgroundColor(Color.parseColor("#121D28"));
                if (cardView != null) cardView.setCardBackgroundColor(Color.parseColor("#1B2B3C"));
                if (btEntrar != null) {
                    btEntrar.setBackgroundColor(Color.parseColor("#00E6A1"));
                    btEntrar.setTextColor(Color.parseColor("#0A0F14"));
                }
                if (edUsuario != null) {
                    edUsuario.setTextColor(Color.WHITE);
                    edUsuario.setHintTextColor(Color.parseColor("#94A3B8"));
                }
                if (edSenha != null) {
                    edSenha.setTextColor(Color.WHITE);
                    edSenha.setHintTextColor(Color.parseColor("#94A3B8"));
                }
                // Card de Dúvidas com Borda Destacada em Verde Neon
                if (cvDuvida3 != null) {
                    GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.RECTANGLE);
                    shape.setColor(Color.parseColor("#1B2B3C"));
                    shape.setCornerRadius(32f);
                    shape.setStroke(6, Color.parseColor("#00E6A1"));
                    cvDuvida3.setBackground(shape);
                }
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.WHITE);
                if (tvAtencao != null) tvAtencao.setTextColor(Color.parseColor("#FFE600"));
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#00E6A1"));
                    btEntendi4.setTextColor(Color.parseColor("#0A0F14"));
                }

                Toast.makeText(this, "Alto Contraste Ativado", Toast.LENGTH_SHORT).show();
                break;

                // MODO DALTONICO
            case DALTONICO:
                btThemeToggle.setImageResource(R.drawable.sol);
                if (ivLogo != null) ivLogo.setImageResource(R.drawable.daltonico_logo);
                break;

            case ESCURO:
                btThemeToggle.setImageResource(R.drawable.lua);
                animarCorFundo(main, "#E2E2E2", "#12161A");
                if (ivLogo != null) ivLogo.setImageResource(R.drawable.main_rh);
                if (headerBackground3 != null) animarCorFundo(headerBackground3, "#203652", "#1B2430");
                if (frameLayout != null) animarCorFundo(frameLayout, "#203652", "#1B2430");
                if (cardView != null) animarCorCard(cardView, "#FFFFFF", "#1E293B");
                if (btEntrar != null) {
                    btEntrar.setBackgroundColor(Color.parseColor("#2B4C7E"));
                    btEntrar.setTextColor(Color.WHITE);
                }
                animarCorTexto(edUsuario, "#000000", "#E2E8F0");
                animarCorTexto(edSenha, "#000000", "#E2E8F0");
                if (edUsuario != null) edUsuario.setHintTextColor(Color.parseColor("#94A3B8"));
                if (edSenha != null) edSenha.setHintTextColor(Color.parseColor("#94A3B8"));

                if (cvDuvida3 != null) {
                    cvDuvida3.setBackground(null);
                    animarCorCard(cvDuvida3, "#FFFFFF", "#243144");
                }
                if (tvExibido4 != null) animarCorTexto(tvExibido4, "#212121", "#E2E8F0");
                if (tvAtencao != null) tvAtencao.setTextColor(Color.parseColor("#FFB800"));
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }

                animarCorFAB(fbInterrogacao, "#FFFFFF", "#243144", "#000000", "#E2E8F0");

                Toast.makeText(this, "Modo Escuro Ativado", Toast.LENGTH_SHORT).show();
                break;

            case CLARO:
            default:
                btThemeToggle.setImageResource(R.drawable.sol);
                if (ivLogo != null) ivLogo.setImageResource(R.drawable.main_rh);
                animarCorFundo(main, "#12161A", "#E2E2E2");
                if (headerBackground3 != null) animarCorFundo(headerBackground3, "#1B2430", "#203652");
                if (frameLayout != null) animarCorFundo(frameLayout, "#1B2430", "#203652");
                if (cardView != null) animarCorCard(cardView, "#1E293B", "#FFFFFF");
                if (btEntrar != null) {
                    btEntrar.setBackgroundColor(Color.parseColor("#1A304B"));
                    btEntrar.setTextColor(Color.WHITE);
                }

                animarCorTexto(edUsuario, "#E2E8F0", "#000000");
                animarCorTexto(edSenha, "#E2E8F0", "#000000");
                if (edUsuario != null) edUsuario.setHintTextColor(Color.parseColor("#8E8E93"));
                if (edSenha != null) edSenha.setHintTextColor(Color.parseColor("#8E8E93"));

                if (cvDuvida3 != null) {
                    cvDuvida3.setBackground(null);
                    cvDuvida3.setCardBackgroundColor(Color.WHITE);
                }
                if (tvExibido4 != null) animarCorTexto(tvExibido4, "#E2E8F0", "#212121");
                if (tvAtencao != null) tvAtencao.setTextColor(Color.parseColor("#FFB800"));
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }

                animarCorFAB(fbInterrogacao, "#243144", "#FFFFFF", "#E2E8F0", "#000000");

                Toast.makeText(this, "Modo Normal Ativado", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // Métodos Auxiliares e Animação

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