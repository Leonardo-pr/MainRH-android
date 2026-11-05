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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TelaMenu extends BaseActivity {

    private enum ModoTema {
        CLARO,
        ESCURO,
        ALTO_CONTRASTE,
        DALTONICO
    }

    private ModoTema temaAtual = ModoTema.CLARO;
    private FloatingActionButton fbInterrogacao2;
    private Button btEntendi;
    ImageButton btThemeToggle;
    private ImageButton ibAcessibilidade3;
    private SearchView svBusca;
    private View main, headerBackground;
    private TextView tvUser, tvSaudacao, tvOpcoes, tvExibido4, tvTituloDuvida;
    private CardView cvDuvida3, cvContainerList;
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

        // Mapeamento dos Componentes
        main = findViewById(R.id.main);
        svBusca = findViewById(R.id.svBusca);
        headerBackground = findViewById(R.id.headerBackground);
        ibAcessibilidade3 = findViewById(R.id.ibAcessibilidade);
        fbInterrogacao2 = findViewById(R.id.fbInterrogacao2);
        btEntendi = findViewById(R.id.btEntendi4);
        cvDuvida3 = findViewById(R.id.cvDuvida3);
        cvContainerList = findViewById(R.id.cvContainerList);
        tvUser = findViewById(R.id.tvUser);
        tvSaudacao = findViewById(R.id.tvSaudacao);
        tvOpcoes = findViewById(R.id.tvOpcoes);
        tvExibido4 = findViewById(R.id.tvExibido4);
        tvTituloDuvida = findViewById(R.id.tvTituloDuvida);
        rvListaResposta = findViewById(R.id.rvListaResposta);

        // Receber dados da Intent (Nome do Usuário)
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("NOME_USUARIO")) {
            String nome = intent.getStringExtra("NOME_USUARIO");
            if (nome != null && !nome.isEmpty()) {
                tvUser.setText(nome);
            }
        }

        // Configuração do RecyclerView
        if (rvListaResposta != null) {
            rvListaResposta.setLayoutManager(new LinearLayoutManager(this));
            listaEntrevistas = carregarEntrevistasEmProcesso();
            adapter = new EntrevistaAdapter(listaEntrevistas);
            rvListaResposta.setAdapter(adapter);
        }

        // Sincroniza o tema salvo ao iniciar
        sincronizarTemaSalvo();

        // Botão Sol / Lua
        if (btThemeToggle != null) {
            btThemeToggle.setOnClickListener(v -> {
                btThemeToggle.animate().rotationBy(360f).setDuration(400).start();
                if (temaAtual == ModoTema.ESCURO) {
                    salvarEAplicarTema(ModoTema.CLARO);
                } else {
                    salvarEAplicarTema(ModoTema.ESCURO);
                }
            });
        }

        if (svBusca != null) {
            svBusca.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filtrar(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filtrar(newText);
                    return true;
                }
            });
        }

        // Botão de Acessibilidade
        if (ibAcessibilidade3 != null) {
            ibAcessibilidade3.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }

        // Pop-up de Dúvida
        if (fbInterrogacao2 != null && cvDuvida3 != null) {
            fbInterrogacao2.setOnClickListener(v -> {
                cvDuvida3.animate().cancel();
                cvDuvida3.setAlpha(0f);
                cvDuvida3.setVisibility(View.VISIBLE);
                cvDuvida3.animate().alpha(1f).setDuration(400).setListener(null);
            });
        }

        if (btEntendi != null && cvDuvida3 != null) {
            btEntendi.setOnClickListener(v -> cvDuvida3.animate()
                    .alpha(0f)
                    .setDuration(400)
                    .withEndAction(() -> cvDuvida3.setVisibility(View.GONE)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Garante a sincronização automática caso o usuário volte de outra tela
        sincronizarTemaSalvo();
    }

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
            recreate(); // Recarrega a tela instantaneamente com o novo tema aplicado
        });

        dialog.show(getSupportFragmentManager(), "AcessibilidadeBottomSheet");
    }

    private void aplicarTema(ModoTema novoTema) {
        this.temaAtual = novoTema;

        switch (novoTema) {
            case ALTO_CONTRASTE:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.sol);
                if (main != null) main.setBackgroundColor(Color.parseColor("#0A0F14"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#121D28"));
                if (tvSaudacao != null) tvSaudacao.setTextColor(Color.parseColor("#FFE600"));
                if (tvUser != null) tvUser.setTextColor(Color.parseColor("#00E6A1"));
                if (tvOpcoes != null) tvOpcoes.setTextColor(Color.parseColor("#94A3B8"));
                if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.parseColor("#1B2B3C"));
                if (cvDuvida3 != null) {
                    GradientDrawable bg = new GradientDrawable();
                    bg.setShape(GradientDrawable.RECTANGLE);
                    bg.setColor(Color.parseColor("#1B2B3C"));
                    bg.setCornerRadius(32f);
                    bg.setStroke(4, Color.parseColor("#00E6A1"));
                    cvDuvida3.setBackground(bg);
                }
                if (tvTituloDuvida != null) tvTituloDuvida.setTextColor(Color.parseColor("#FFE600"));
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.WHITE);
                if (btEntendi != null) {
                    btEntendi.setBackgroundColor(Color.parseColor("#00E6A1"));
                    btEntendi.setTextColor(Color.parseColor("#0A0F14"));
                }
                if (fbInterrogacao2 != null) {
                    fbInterrogacao2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B2B3C")));
                    fbInterrogacao2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#00E6A1")));
                }
                break;

            case DALTONICO:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.sol);
                if (main != null) main.setBackgroundColor(Color.parseColor("#FFFFFF"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#0057B8"));

                if (tvSaudacao != null) tvSaudacao.setTextColor(Color.parseColor("#000000"));
                if (tvUser != null) tvUser.setTextColor(Color.parseColor("#0057B8"));
                if (tvOpcoes != null) tvOpcoes.setTextColor(Color.parseColor("#333333"));
                if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
                if (cvDuvida3 != null) {
                    GradientDrawable bg = new GradientDrawable();
                    bg.setShape(GradientDrawable.RECTANGLE);
                    bg.setColor(Color.WHITE);
                    bg.setCornerRadius(32f);
                    bg.setStroke(4, Color.parseColor("#0057B8"));
                    cvDuvida3.setBackground(bg);
                }
                if (tvTituloDuvida != null) tvTituloDuvida.setTextColor(Color.parseColor("#0057B8"));
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#333333"));
                if (btEntendi != null) {
                    btEntendi.setBackgroundColor(Color.parseColor("#0057B8"));
                    btEntendi.setTextColor(Color.WHITE);
                }
                if (fbInterrogacao2 != null) {
                    fbInterrogacao2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0057B8")));
                    fbInterrogacao2.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                }
                break;

            case ESCURO:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.lua);
                if (main != null) main.setBackgroundColor(Color.parseColor("#12161A"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#1B2430"));

                if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.parseColor("#1E293B"));
                if (cvDuvida3 != null) {
                    GradientDrawable bg = new GradientDrawable();
                    bg.setShape(GradientDrawable.RECTANGLE);
                    bg.setColor(Color.parseColor("#243144"));
                    bg.setCornerRadius(32f);
                    cvDuvida3.setBackground(bg);
                }
                if (tvTituloDuvida != null) tvTituloDuvida.setTextColor(Color.parseColor("#FEBE11"));
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#E2E8F0"));
                if (tvSaudacao != null) tvSaudacao.setTextColor(Color.parseColor("#E2E8F0"));
                if (tvUser != null) tvUser.setTextColor(Color.parseColor("#56C8A8"));
                if (tvOpcoes != null) tvOpcoes.setTextColor(Color.parseColor("#94A3B8"));
                if (btEntendi != null) {
                    btEntendi.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi.setTextColor(Color.WHITE);
                }
                if (fbInterrogacao2 != null) {
                    fbInterrogacao2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#243144")));
                    fbInterrogacao2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#E2E8F0")));
                }
                break;

            case CLARO:
            default:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.sol);
                if (main != null) main.setBackgroundColor(Color.parseColor("#E2E2E2"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#203652"));
                if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.WHITE);
                if (cvDuvida3 != null) {
                    GradientDrawable bg = new GradientDrawable();
                    bg.setShape(GradientDrawable.RECTANGLE);
                    bg.setColor(Color.WHITE);
                    bg.setCornerRadius(32f);
                    cvDuvida3.setBackground(bg);
                }
                if (tvTituloDuvida != null) tvTituloDuvida.setTextColor(Color.parseColor("#1F4273"));
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#212121"));
                if (tvSaudacao != null) tvSaudacao.setTextColor(Color.parseColor("#1F4273"));
                if (tvUser != null) tvUser.setTextColor(Color.parseColor("#47B192"));
                if (tvOpcoes != null) tvOpcoes.setTextColor(Color.parseColor("#5A6E85"));
                if (btEntendi != null) {
                    btEntendi.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi.setTextColor(Color.WHITE);
                }
                if (fbInterrogacao2 != null) {
                    fbInterrogacao2.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    fbInterrogacao2.setImageTintList(ColorStateList.valueOf(Color.BLACK));
                }
                break;
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void filtrar(String texto) {
        List<Entrevista> listaFiltrada = new ArrayList<>();
        List<Entrevista> original = carregarEntrevistasEmProcesso();

        for (Entrevista item : original) {
            if (item.getNomeVaga().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(item);
            }
        }

        adapter = new EntrevistaAdapter(listaFiltrada);
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