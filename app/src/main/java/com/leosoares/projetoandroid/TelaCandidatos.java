package com.leosoares.projetoandroid;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
  Possui busca de candidatos, transição para Modo Escuro e Pop-up de Acessibilidade.
*/

public class TelaCandidatos extends AppCompatActivity {

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

    // Flags de Acessibilidade
    private boolean modoEscuro = false;
    private boolean modoDaltonico = false;
    private boolean modoAltoContraste = false;

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


        // --- CONFIGURAÇÃO DA BARRA DE PESQUISA (FUNCIONANDO SEM ALTERAR OUTRAS CLASSES) ---
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

        // Configuração do RecyclerView
        listaCandidatos = carregarCandidatosEmProcesso();
        rvListaResposta.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CandidatoAdapter(listaCandidatos);
        rvListaResposta.setAdapter(adapter);

        // --- MODO NOTURNO / CLARO ---
        btThemeToggle.setOnClickListener(v -> {
            modoEscuro = !modoEscuro;

            btThemeToggle.animate().rotationBy(360f).setDuration(400).start();

            if (modoEscuro) {
                btThemeToggle.setImageResource(R.drawable.lua);

                animarCorFundo(main, "#E2E2E2", "#12161A");
                if (headerBackground != null) animarCorFundo(headerBackground, "#203652", "#1B2430");
                animarCorCard(cvContainerList, "#FFFFFF", "#1E293B");
                animarCorCard(cvDuvida4, "#FFFFFF", "#243144");

                animarCorTexto(tvCandi, "#203652", "#E2E8F0");
                animarCorTexto(tvExibido4, "#212121", "#E2E8F0");

                if (fbInterrogacao4 != null) {
                    animarCorFAB(fbInterrogacao4, "#FFFFFF", "#243144", "#000000", "#E2E8F0");
                }

            } else {
                btThemeToggle.setImageResource(R.drawable.sol);

                animarCorFundo(main, "#12161A", "#E2E2E2");
                if (headerBackground != null) animarCorFundo(headerBackground, "#1B2430", "#203652");
                animarCorCard(cvContainerList, "#1E293B", "#FFFFFF");
                animarCorCard(cvDuvida4, "#243144", "#FFFFFF");

                animarCorTexto(tvCandi, "#E2E8F0", "#203652");
                animarCorTexto(tvExibido4, "#E2E8F0", "#212121");

                if (fbInterrogacao4 != null) {
                    animarCorFAB(fbInterrogacao4, "#243144", "#FFFFFF", "#E2E8F0", "#000000");
                }
            }
        });

        // Pop-Up Acessibilidade
        if (ibAcessibilidade2 != null) {
            ibAcessibilidade2.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }

        // Pop-up de Ajuda
        if (fbInterrogacao4 != null) {
            fbInterrogacao4.setOnClickListener(v -> {
                cvDuvida4.animate().cancel();
                cvDuvida4.setAlpha(0f);
                cvDuvida4.setVisibility(View.VISIBLE);
                cvDuvida4.animate().alpha(1f).setDuration(400).setListener(null);
            });
        }

        if (btEntendi4 != null) {
            btEntendi4.setOnClickListener(v -> cvDuvida4.animate()
                    .alpha(0f)
                    .setDuration(400)
                    .withEndAction(() -> cvDuvida4.setVisibility(View.GONE)));
        }
    }

    // Métodos do BOTTOMSHEET de acessibilidade
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
            if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#0057B8"));
            if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
            if (tvCandi != null) tvCandi.setTextColor(Color.parseColor("#000000"));

            Toast.makeText(this, "Modo Daltônico Ativado", Toast.LENGTH_SHORT).show();
        } else {
            // Restaura cores originais
            if (modoEscuro) {
                main.setBackgroundColor(Color.parseColor("#12161A"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#1B2430"));
                if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.parseColor("#1E293B"));
                if (tvCandi != null) tvCandi.setTextColor(Color.parseColor("#E2E8F0"));
            } else {
                main.setBackgroundColor(Color.parseColor("#E2E2E2"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#203652"));
                if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                if (tvCandi != null) tvCandi.setTextColor(Color.parseColor("#203652"));
            }
            Toast.makeText(this, "Modo Daltônico Desativado", Toast.LENGTH_SHORT).show();
        }
    }

    private void aplicarAltoContraste(boolean ativar) {
        if (ativar) {
            main.setBackgroundColor(Color.BLACK);
            if (headerBackground != null) headerBackground.setBackgroundColor(Color.BLACK);
            if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.parseColor("#1C1C1E"));
            if (tvCandi != null) tvCandi.setTextColor(Color.YELLOW);

            Toast.makeText(this, "Alto Contraste Ativado", Toast.LENGTH_SHORT).show();
        } else {
            aplicarModoDaltonico(false);
        }
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

    // --- Métodos Auxiliares de Transição Suave (VALUE ANIMATOR) ---

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

    private void animarCorIconeImageButton(ImageButton button, String hexInicio, String hexFim) {
        ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor(hexInicio), Color.parseColor(hexFim));
        anim.setDuration(400);
        anim.addUpdateListener(animation -> button.setImageTintList(ColorStateList.valueOf((int) animation.getAnimatedValue())));
        anim.start();
    }

    private void filtrar(String texto) {
        List<Candidato> listaFiltrada = new ArrayList<>();

        // Pega a lista original completa
        List<Candidato> original = carregarCandidatosEmProcesso();

        for (Candidato c : original) {
            // ATENÇÃO: Substitua getNome() pelo método real que retorna o nome na sua classe Candidato (ex: getNomeCandidato())
            if (c.getTvNomeCandidato().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(c);
            }
        }

        // Cria um novo adapter temporário apenas com os itens filtrados e joga no RecyclerView
        adapter = new CandidatoAdapter(listaFiltrada);
        rvListaResposta.setAdapter(adapter);
    }
}