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
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HubCandidatoActivity extends BaseActivity {

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
        if (intentRecebida != null && intentRecebida.hasExtra("NOME_CANDIDATO")) {
            String nome = intentRecebida.getStringExtra("NOME_CANDIDATO");
            if (nome != null && !nome.isEmpty()) {
                tvNomeCandidato.setText(nome);
            }
        }
        sincronizarTemaSalvo();

        // Alternar Modo Claro / Escuro (Sol/Lua)
        if (btThemeToggle2 != null) {
            btThemeToggle2.setOnClickListener(v -> {
                btThemeToggle2.animate().rotationBy(360f).setDuration(400).start();

                if (temaAtual == ModoTema.ESCURO) {
                    salvarEAplicarTema(ModoTema.CLARO);
                } else {
                    salvarEAplicarTema(ModoTema.ESCURO);
                }
            });
        }

        // Botão de Acessibilidade
        if (ibAcessibilidade != null) {
            ibAcessibilidade.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }

        // Pop-up de Ajuda
        if (fbInterrogacao3 != null) {
            fbInterrogacao3.setOnClickListener(v -> {
                if (cvDuvida3 != null) {
                    cvDuvida3.animate().cancel();
                    cvDuvida3.setAlpha(0f);
                    cvDuvida3.setVisibility(View.VISIBLE);
                    cvDuvida3.animate().alpha(1f).setDuration(400).setListener(null);
                }
            });
        }

        if (btEntendi4 != null && cvDuvida3 != null) {
            btEntendi4.setOnClickListener(v -> cvDuvida3.animate()
                    .alpha(0f)
                    .setDuration(400)
                    .withEndAction(() -> cvDuvida3.setVisibility(View.GONE)));
        }

        // Listeners das Etapas
        if (btEntrevista != null) {
            btEntrevista.setOnClickListener(v -> {
                Intent intent = new Intent(HubCandidatoActivity.this, InformacaoCandidato.class);
                intent.putExtra("TIPO_PROCESSO", "ENTREVISTA");
                intent.putExtra("NOME_CANDIDATO", tvNomeCandidato.getText().toString());
                startActivity(intent);
            });
        }

        if (btDin != null) {
            btDin.setOnClickListener(v -> {
                Intent intent = new Intent(HubCandidatoActivity.this, InformacaoCandidato.class);
                intent.putExtra("TIPO_PROCESSO", "DINAMICA");
                intent.putExtra("NOME_CANDIDATO", tvNomeCandidato.getText().toString());
                startActivity(intent);
            });
        }
    }

    // Lê o que está salvo no ThemeManager e aplica na tela
    private void sincronizarTemaSalvo() {
        int salvo = ThemeManager.getSavedTheme(this);
        switch (salvo) {
            case ThemeManager.THEME_DARK:
                aplicarTema(ModoTema.ESCURO);
                break;
            case ThemeManager.THEME_HIGH_CONTRAST:
                aplicarTema(ModoTema.ALTO_CONTRASTE);
                break;
            case ThemeManager.THEME_COLORBLIND:
                aplicarTema(ModoTema.DALTONICO);
                break;
            case ThemeManager.THEME_LIGHT:
            default:
                aplicarTema(ModoTema.CLARO);
                break;
        }
    }

    // Salva no ThemeManager e aplica o tema
    private void salvarEAplicarTema(ModoTema novoTema) {
        this.temaAtual = novoTema;
        int tipoInt = ThemeManager.THEME_LIGHT;

        if (novoTema == ModoTema.ESCURO) tipoInt = ThemeManager.THEME_DARK;
        else if (novoTema == ModoTema.ALTO_CONTRASTE) tipoInt = ThemeManager.THEME_HIGH_CONTRAST;
        else if (novoTema == ModoTema.DALTONICO) tipoInt = ThemeManager.THEME_COLORBLIND;

        ThemeManager.saveTheme(this, tipoInt);
        aplicarTema(novoTema);
    }

    private void abrirBottomSheetAcessibilidade() {
        AcessibilidadeBottomSheet dialog = new AcessibilidadeBottomSheet();

        dialog.setListener(tipoTema -> {
            ThemeManager.saveTheme(this, tipoTema);
            recreate();
        });

        dialog.show(getSupportFragmentManager(), "AcessibilidadeBottomSheet");
    }

    private void aplicarTema(ModoTema novoTema) {
        this.temaAtual = novoTema;

        switch (novoTema) {
            case ALTO_CONTRASTE:
                if (btThemeToggle2 != null) btThemeToggle2.setImageResource(R.drawable.sol);

                if (main != null) main.setBackgroundColor(Color.parseColor("#0A0F14"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#121D28"));

                if (tvNomeCandidato != null) tvNomeCandidato.setTextColor(Color.parseColor("#FFE600"));
                if (tvSubtitulo != null) tvSubtitulo.setTextColor(Color.parseColor("#94A3B8"));
                if (tvProcessoEn != null) tvProcessoEn.setTextColor(Color.WHITE);
                if (tvProcessoDi != null) tvProcessoDi.setTextColor(Color.WHITE);

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
                break;

            case DALTONICO:
                if (btThemeToggle2 != null) btThemeToggle2.setImageResource(R.drawable.sol);
                if (main != null) main.setBackgroundColor(Color.parseColor("#FFFFFF"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#0057B8"));

                if (tvNomeCandidato != null) tvNomeCandidato.setTextColor(Color.parseColor("#0057B8"));
                if (tvSubtitulo != null) tvSubtitulo.setTextColor(Color.parseColor("#333333"));
                if (tvProcessoEn != null) tvProcessoEn.setTextColor(Color.parseColor("#0057B8"));
                if (tvProcessoDi != null) tvProcessoDi.setTextColor(Color.parseColor("#0057B8"));

                GradientDrawable bgDaltonico = new GradientDrawable();
                bgDaltonico.setShape(GradientDrawable.RECTANGLE);
                bgDaltonico.setColor(Color.WHITE);
                bgDaltonico.setCornerRadius(32f);
                bgDaltonico.setStroke(4, Color.parseColor("#0057B8"));

                if (cvDuvida3 != null) cvDuvida3.setBackground(bgDaltonico);
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#333333"));
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#0057B8"));
                    btEntendi4.setTextColor(Color.WHITE);
                }
                if (fbInterrogacao3 != null) {
                    fbInterrogacao3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0057B8")));
                    fbInterrogacao3.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                }
                break;

            case ESCURO:
                if (btThemeToggle2 != null) btThemeToggle2.setImageResource(R.drawable.lua);

                if (main != null) main.setBackgroundColor(Color.parseColor("#12161A"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#1B2430"));

                if (tvNomeCandidato != null) tvNomeCandidato.setTextColor(Color.parseColor("#E2E8F0"));
                if (tvSubtitulo != null) tvSubtitulo.setTextColor(Color.parseColor("#94A3B8"));
                if (tvProcessoEn != null) tvProcessoEn.setTextColor(Color.parseColor("#E2E8F0"));
                if (tvProcessoDi != null) tvProcessoDi.setTextColor(Color.parseColor("#E2E8F0"));

                GradientDrawable bgEscuro = new GradientDrawable();
                bgEscuro.setShape(GradientDrawable.RECTANGLE);
                bgEscuro.setColor(Color.parseColor("#243144"));
                bgEscuro.setCornerRadius(32f);

                if (cvDuvida3 != null) cvDuvida3.setBackground(bgEscuro);
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#E2E8F0"));
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }

                if (fbInterrogacao3 != null) {
                    fbInterrogacao3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#243144")));
                    fbInterrogacao3.setImageTintList(ColorStateList.valueOf(Color.parseColor("#E2E8F0")));
                }
                break;

            case CLARO:
            default:
                if (btThemeToggle2 != null) btThemeToggle2.setImageResource(R.drawable.sol);

                if (main != null) main.setBackgroundColor(Color.parseColor("#E2E2E2"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#203652"));

                if (tvNomeCandidato != null) tvNomeCandidato.setTextColor(Color.parseColor("#203652"));
                if (tvSubtitulo != null) tvSubtitulo.setTextColor(Color.parseColor("#555555"));
                if (tvProcessoEn != null) tvProcessoEn.setTextColor(Color.parseColor("#203652"));
                if (tvProcessoDi != null) tvProcessoDi.setTextColor(Color.parseColor("#203652"));

                GradientDrawable bgClaro = new GradientDrawable();
                bgClaro.setShape(GradientDrawable.RECTANGLE);
                bgClaro.setColor(Color.WHITE);
                bgClaro.setCornerRadius(32f);

                if (cvDuvida3 != null) cvDuvida3.setBackground(bgClaro);
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#212121"));
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }

                if (fbInterrogacao3 != null) {
                    fbInterrogacao3.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    fbInterrogacao3.setImageTintList(ColorStateList.valueOf(Color.BLACK));
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sincronizarTemaSalvo();
    }
}