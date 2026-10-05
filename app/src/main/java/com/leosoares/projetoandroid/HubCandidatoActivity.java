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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HubCandidatoActivity extends AppCompatActivity {

    private enum ModoTema {
        CLARO,
        ESCURO,
        ALTO_CONTRASTE,
        DALTONICO
    }

    private ModoTema temaAtual = ModoTema.CLARO;

    private ImageButton btEntrevista, btDin, btThemeToggle2, ibAcessibilidade;
    private TextView tvProcessoEn, tvProcessoDi, tvNomeCandidato, tvSubtitulo, tvExibido4;
    private CardView cvDuvida3;
    private FloatingActionButton fbInterrogacao3;
    private Button btEntendi4;
    private View headerBackground, main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hub_candidato);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Mapeamento dos Componentes
        main = findViewById(R.id.main);
        headerBackground = findViewById(R.id.headerBackground);
        btThemeToggle2 = findViewById(R.id.btThemeToggle2);
        ibAcessibilidade = findViewById(R.id.ibAcessibilidade);
        tvNomeCandidato = findViewById(R.id.tvNomeCandidato);
        tvSubtitulo = findViewById(R.id.tvSubtitulo);
        btEntrevista = findViewById(R.id.btEntrevista);
        tvProcessoEn = findViewById(R.id.tvProcessoEn);
        btDin = findViewById(R.id.btDin);
        tvProcessoDi = findViewById(R.id.tvProcessoDi);
        fbInterrogacao3 = findViewById(R.id.fbInterrogacao3);
        cvDuvida3 = findViewById(R.id.cvDuvida3);
        tvExibido4 = findViewById(R.id.tvExibido4);
        btEntendi4 = findViewById(R.id.btEntendi4);

        // Recebe dados da Intent
        Intent intentRecebida = getIntent();
        if (intentRecebida != null) {
            if (intentRecebida.hasExtra("NOME_CANDIDATO")) {
                String nome = intentRecebida.getStringExtra("NOME_CANDIDATO");
                if (nome != null && !nome.isEmpty()) {
                    tvNomeCandidato.setText(nome);
                }
            }

            boolean veioModoEscuro = intentRecebida.getBooleanExtra("MODO_ESCURO", false);
            if (veioModoEscuro) {
                aplicarTema(ModoTema.ESCURO);
            }
        }

        // Alternar Modo Claro / Escuro
        btThemeToggle2.setOnClickListener(v -> {
            btThemeToggle2.animate().rotationBy(360f).setDuration(400).start();

            if (temaAtual == ModoTema.ESCURO) {
                aplicarTema(ModoTema.CLARO);
            } else {
                aplicarTema(ModoTema.ESCURO);
            }
        });

        // Botão de Acessibilidade
        if (ibAcessibilidade != null) {
            ibAcessibilidade.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }

        // Pop-up de Ajuda
        fbInterrogacao3.setOnClickListener(v -> {
            cvDuvida3.animate().cancel();
            cvDuvida3.setAlpha(0f);
            cvDuvida3.setVisibility(View.VISIBLE);
            cvDuvida3.animate().alpha(1f).setDuration(400).setListener(null);
        });

        btEntendi4.setOnClickListener(v -> cvDuvida3.animate()
                .alpha(0f)
                .setDuration(400)
                .withEndAction(() -> cvDuvida3.setVisibility(View.GONE)));

        // Listeners das Etapas
        btEntrevista.setOnClickListener(v -> {
            Intent intent = new Intent(HubCandidatoActivity.this, InformacaoCandidato.class);
            intent.putExtra("TIPO_PROCESSO", "ENTREVISTA");
            intent.putExtra("NOME_CANDIDATO", tvNomeCandidato.getText().toString());
            intent.putExtra("MODO_ESCURO", temaAtual == ModoTema.ESCURO);
            startActivity(intent);
        });

        btDin.setOnClickListener(v -> {
            Intent intent = new Intent(HubCandidatoActivity.this, InformacaoCandidato.class);
            intent.putExtra("TIPO_PROCESSO", "DINAMICA");
            intent.putExtra("NOME_CANDIDATO", tvNomeCandidato.getText().toString());
            intent.putExtra("MODO_ESCURO", temaAtual == ModoTema.ESCURO);
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
                btThemeToggle2.setImageResource(R.drawable.sol);

                // Fundo da Tela e Header no padrão Alto Contraste
                main.setBackgroundColor(Color.parseColor("#0A0F14"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#121D28"));

                // Títulos e Textos
                if (tvNomeCandidato != null) tvNomeCandidato.setTextColor(Color.parseColor("#FFE600"));
                if (tvSubtitulo != null) tvSubtitulo.setTextColor(Color.parseColor("#94A3B8"));
                if (tvProcessoEn != null) tvProcessoEn.setTextColor(Color.WHITE);
                if (tvProcessoDi != null) tvProcessoDi.setTextColor(Color.WHITE);

                // Card de Ajuda com moldura Verde Neon (#00E6A1)
                GradientDrawable molduraAltoContraste = new GradientDrawable();
                molduraAltoContraste.setShape(GradientDrawable.RECTANGLE);
                molduraAltoContraste.setColor(Color.parseColor("#1B2B3C"));
                molduraAltoContraste.setCornerRadius(32f);
                molduraAltoContraste.setStroke(4, Color.parseColor("#00E6A1"));

                if (cvDuvida3 != null) {
                    cvDuvida3.setBackground(molduraAltoContraste);
                }
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.WHITE);
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#00E6A1"));
                    btEntendi4.setTextColor(Color.parseColor("#0A0F14"));
                }

                if (fbInterrogacao3 != null) {
                    fbInterrogacao3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B2B3C")));
                    fbInterrogacao3.setImageTintList(ColorStateList.valueOf(Color.parseColor("#00E6A1")));
                }

                Toast.makeText(this, "Alto Contraste Ativado", Toast.LENGTH_SHORT).show();
                break;

                // MODO DALTONICO
            case DALTONICO:
                btThemeToggle2.setImageResource(R.drawable.sol);
                break;

            case ESCURO:
                btThemeToggle2.setImageResource(R.drawable.lua);

                animarCorFundo(main, "#E2E2E2", "#12161A");
                if (headerBackground != null) animarCorFundo(headerBackground, "#203652", "#1B2430");

                animarCorTexto(tvNomeCandidato, "#203652", "#E2E8F0");
                animarCorTexto(tvSubtitulo, "#555555", "#94A3B8");
                animarCorTexto(tvProcessoEn, "#203652", "#E2E8F0");
                animarCorTexto(tvProcessoDi, "#203652", "#E2E8F0");

                if (cvDuvida3 != null) {
                    animarCorCard(cvDuvida3, "#FFFFFF", "#243144");
                }
                if (tvExibido4 != null) animarCorTexto(tvExibido4, "#212121", "#E2E8F0");
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }

                animarCorFAB(fbInterrogacao3, "#FFFFFF", "#243144", "#000000", "#E2E8F0");

                Toast.makeText(this, "Modo Escuro Ativado", Toast.LENGTH_SHORT).show();
                break;

            case CLARO:
            default:
                btThemeToggle2.setImageResource(R.drawable.sol);

                // RESTAURAÇÃO EXATA DO MODO CLARO DO XML
                main.setBackgroundColor(Color.parseColor("#E2E2E2"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#203652"));

                if (tvNomeCandidato != null) tvNomeCandidato.setTextColor(Color.parseColor("#203652"));
                if (tvSubtitulo != null) tvSubtitulo.setTextColor(Color.parseColor("#555555"));
                if (tvProcessoEn != null) tvProcessoEn.setTextColor(Color.parseColor("#203652"));
                if (tvProcessoDi != null) tvProcessoDi.setTextColor(Color.parseColor("#203652"));

                if (cvDuvida3 != null) {
                    cvDuvida3.setCardBackgroundColor(Color.WHITE);
                }
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#FFFFFF"));
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }

                if (fbInterrogacao3 != null) {
                    fbInterrogacao3.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    fbInterrogacao3.setImageTintList(ColorStateList.valueOf(Color.BLACK));
                }

                Toast.makeText(this, "Modo Claro Ativado", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // --- MÉTODOS AUXILIARES DE ANIMAÇÃO DE CORES ---

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