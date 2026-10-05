package com.leosoares.projetoandroid;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
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

/*
  TelaCandidatos:
  Possui busca de candidatos, transição para Modo Escuro animado e suporte aos
  modos de Acessibilidade (Daltônico e Alto Contraste).
*/

public class TelaCandidatos extends AppCompatActivity {

    private enum ModoTema {
        CLARO,
        ESCURO,
        ALTO_CONTRASTE,
        DALTONICO
    }

    private ModoTema temaAtual = ModoTema.CLARO;

    private SearchView svBusca;
    private RecyclerView rvListaResposta;
    private CandidatoAdapter adapter;
    private List<Candidato> listaCandidatos;
    private FloatingActionButton fbInterrogacao4;
    private View headerBackground, main;
    private ImageButton btThemeToggle, ibAcessibilidade2;
    private Button btEntendi4;
    private TextView tvExibido4, tvCandi;
    private CardView cvDuvida4, cvContainerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_candidatos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Mapeamento dos Componentes
        main = findViewById(R.id.main);
        headerBackground = findViewById(R.id.headerBackground);
        btThemeToggle = findViewById(R.id.btThemeToggle);
        tvCandi = findViewById(R.id.tvCandi);
        cvContainerList = findViewById(R.id.cvContainerList);
        svBusca = findViewById(R.id.svBusca);
        rvListaResposta = findViewById(R.id.rvListaResposta);
        fbInterrogacao4 = findViewById(R.id.fbInterrogacao4);
        cvDuvida4 = findViewById(R.id.cvDuvida4);
        tvExibido4 = findViewById(R.id.tvExibido4);
        btEntendi4 = findViewById(R.id.btEntendi4);
        ibAcessibilidade2 = findViewById(R.id.ibAcessibilidade2);

        listaCandidatos = carregarCandidatosEmProcesso();
        rvListaResposta.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CandidatoAdapter(listaCandidatos);
        rvListaResposta.setAdapter(adapter);

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

        // --- MODO NOTURNO / CLARO ---
        if (btThemeToggle != null) {
            btThemeToggle.setOnClickListener(v -> {
                btThemeToggle.animate().rotationBy(360f).setDuration(400).start();
                if (temaAtual == ModoTema.ESCURO) {
                    aplicarTema(ModoTema.CLARO);
                } else {
                    aplicarTema(ModoTema.ESCURO);
                }
            });
        }

        // Pop-Up de Acessibilidade
        if (ibAcessibilidade2 != null) {
            ibAcessibilidade2.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }

        // Pop-up de Ajuda
        if (fbInterrogacao4 != null) {
            fbInterrogacao4.setOnClickListener(v -> {
                if (cvDuvida4 != null) {
                    cvDuvida4.animate().cancel();
                    cvDuvida4.setAlpha(0f);
                    cvDuvida4.setVisibility(View.VISIBLE);
                    cvDuvida4.animate().alpha(1f).setDuration(400).setListener(null);
                }
            });
        }

        if (btEntendi4 != null && cvDuvida4 != null) {
            btEntendi4.setOnClickListener(v -> cvDuvida4.animate()
                    .alpha(0f)
                    .setDuration(400)
                    .withEndAction(() -> cvDuvida4.setVisibility(View.GONE)));
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

    private void filtrar(String texto) {
        List<Candidato> listaFiltrada = new ArrayList<>();
        List<Candidato> original = carregarCandidatosEmProcesso();

        for (Candidato c : original) {
            if (c.getTvNomeCandidato().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(c);
            }
        }
        adapter = new CandidatoAdapter(listaFiltrada);
        rvListaResposta.setAdapter(adapter);
    }

    private List<Candidato> carregarCandidatosEmProcesso() {
        List<Candidato> lista = new ArrayList<>();
        lista.add(new Candidato("Lucas Souza"));
        lista.add(new Candidato("Roberto Carvalho"));
        lista.add(new Candidato("Lucas Wanderley"));
        lista.add(new Candidato("João Silva Santos"));
        lista.add(new Candidato("Aleixo Martins"));
        return lista;
    }

    //  Gerenciador de TEMAS
    private void aplicarTema(ModoTema novoTema) {
        this.temaAtual = novoTema;

        switch (novoTema) {
            case ALTO_CONTRASTE:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.sol);

                main.setBackgroundColor(Color.parseColor("#0A0F14"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#121D28"));

                if (tvCandi != null) tvCandi.setTextColor(Color.parseColor("#FFE600"));

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

                if (fbInterrogacao4 != null) {
                    fbInterrogacao4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B2B3C")));
                    fbInterrogacao4.setImageTintList(ColorStateList.valueOf(Color.parseColor("#00E6A1")));
                }

                Toast.makeText(this, "Alto Contraste Ativado", Toast.LENGTH_SHORT).show();
                break;

                // MODO DALTONICO
            case DALTONICO:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.sol);
                break;

            case ESCURO:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.lua);

                animarCorFundo(main, "#E2E2E2", "#12161A");
                if (headerBackground != null) animarCorFundo(headerBackground, "#203652", "#1B2430");

                if (cvContainerList != null) animarCorCard(cvContainerList, "#FFFFFF", "#1E293B");
                if (cvDuvida4 != null) animarCorCard(cvDuvida4, "#FFFFFF", "#243144");

                animarCorTexto(tvCandi, "#203652", "#E2E8F0");
                if (tvExibido4 != null) animarCorTexto(tvExibido4, "#212121", "#E2E8F0");

                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }

                if (fbInterrogacao4 != null) {
                    animarCorFAB(fbInterrogacao4, "#FFFFFF", "#243144", "#000000", "#E2E8F0");
                }

                Toast.makeText(this, "Modo Escuro Ativado", Toast.LENGTH_SHORT).show();
                break;

            case CLARO:
            default:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.sol);

                main.setBackgroundColor(Color.parseColor("#E2E2E2"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#203652"));

                if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.WHITE);
                if (cvDuvida4 != null) cvDuvida4.setCardBackgroundColor(Color.WHITE);

                if (tvCandi != null) tvCandi.setTextColor(Color.parseColor("#203652"));
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#212121"));

                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }

                if (fbInterrogacao4 != null) {
                    fbInterrogacao4.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    fbInterrogacao4.setImageTintList(ColorStateList.valueOf(Color.BLACK));
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