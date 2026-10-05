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

public class TelaMenu extends AppCompatActivity {

    private FloatingActionButton fbInterrogacao2;
    private Button btEntendi;
    private ImageButton btThemeToggle, ibAcessibilidade3;
    private SearchView svBusca;
    private View main, headerBackground;
    private boolean modoEscuro = false;
    private TextView tvUser, tvSaudacao, tvOpcoes, tvExibido4;
    private CardView cvDuvida3, cvContainerList;
    private RecyclerView rvListaResposta;
    private EntrevistaAdapter adapter;
    private List<Entrevista> listaEntrevistas;
    private boolean modoDaltonico = false;
    private boolean modoAltoContraste = false;

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

        // Mapeamento
        main = findViewById(R.id.main);
        svBusca = findViewById(R.id.svBusca);
        headerBackground = findViewById(R.id.headerBackground);
        ibAcessibilidade3 = findViewById(R.id.ibAcessibilidade3);
        btThemeToggle = findViewById(R.id.btThemeToggle);
        fbInterrogacao2 = findViewById(R.id.fbInterrogacao2);
        btEntendi = findViewById(R.id.btEntendi4);
        cvDuvida3 = findViewById(R.id.cvDuvida3);
        cvContainerList = findViewById(R.id.cvContainerList);
        tvUser = findViewById(R.id.tvUser);
        tvSaudacao = findViewById(R.id.tvSaudacao);
        tvOpcoes = findViewById(R.id.tvOpcoes);
        tvExibido4 = findViewById(R.id.tvExibido4);
        rvListaResposta = findViewById(R.id.rvListaResposta);


        // Modo Noturno
        btThemeToggle.setOnClickListener(v -> {
            modoEscuro = !modoEscuro;

            // Animação de Giro
            btThemeToggle.animate().rotationBy(360f).setDuration(400).start();

            if (modoEscuro) {
                btThemeToggle.setImageResource(R.drawable.lua);

                // Transição de Fundos
                animarCorFundo(main, "#E2E2E2", "#12161A");
                animarCorFundo(headerBackground, "#203652", "#1B2430");
                animarCorCard(cvContainerList, "#FFFFFF", "#1E293B");
                animarCorCard(cvDuvida3, "#FFFFFF", "#243144");

                // Transição de Textos
                animarCorTexto(tvSaudacao, "#1F4273", "#E2E8F0");
                animarCorTexto(tvUser, "#47B192", "#56C8A8");
                animarCorTexto(tvOpcoes, "#5A6E85", "#94A3B8");
                animarCorTexto(tvExibido4, "#212121", "#E2E8F0");
;
                // Transição Botão de Dúvida
                animarCorFAB(fbInterrogacao2, "#FFFFFF", "#243144", "#000000", "#E2E8F0");

            } else {
                btThemeToggle.setImageResource(R.drawable.sol);

                // Volta para as cores do Modo Claro
                animarCorFundo(main, "#12161A", "#E2E2E2");
                animarCorFundo(headerBackground, "#1B2430", "#203652");
                animarCorCard(cvContainerList, "#1E293B", "#FFFFFF");
                animarCorCard(cvDuvida3, "#243144", "#FFFFFF");

                animarCorTexto(tvSaudacao, "#E2E8F0", "#1F4273");
                animarCorTexto(tvUser, "#56C8A8", "#47B192");
                animarCorTexto(tvOpcoes, "#94A3B8", "#5A6E85");
                animarCorTexto(tvExibido4, "#E2E8F0", "#212121");

                animarCorFAB(fbInterrogacao2, "#243144", "#FFFFFF", "#E2E8F0", "#000000");
            }
        });

        if (ibAcessibilidade3 != null) {
            ibAcessibilidade3.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }

        // Nome do Usuário Logado
        Intent intent = getIntent();
        if (intent.hasExtra("NOME_USUARIO")) {
            String nome = intent.getStringExtra("NOME_USUARIO");
            if(nome != null && !nome.isEmpty()) {
                tvUser.setText(nome);
            }
        }
        modoEscuro = intent.getBooleanExtra("MODO_ESCURO", false);

        if(modoEscuro) {
            aplicarTemaEscuroEstatico();
        }

        // Popup de Dúvida
        fbInterrogacao2.setOnClickListener(v -> {
            cvDuvida3.animate().cancel();
            cvDuvida3.setAlpha(0f);
            cvDuvida3.setVisibility(View.VISIBLE);
            cvDuvida3.animate().alpha(1f).setDuration(400).setListener(null);
        });

        btEntendi.setOnClickListener(v -> cvDuvida3.animate()
                .alpha(0f)
                .setDuration(400)
                .withEndAction(() -> cvDuvida3.setVisibility(View.GONE)));

        // RecyclerView
        rvListaResposta.setLayoutManager(new LinearLayoutManager(this));
        listaEntrevistas = carregarEntrevistasEmProcesso();
        adapter = new EntrevistaAdapter(listaEntrevistas);
        rvListaResposta.setAdapter(adapter);
    }

    //  Métodos Auxiliares de transição suave (VALUE ANIMATOR)

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

            Toast.makeText(this, "Modo Daltônico Ativado", Toast.LENGTH_SHORT).show();
        } else {
            // Restaura cores originais
            if (modoEscuro) {
                main.setBackgroundColor(Color.parseColor("#12161A"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#1B2430"));
            } else {
                main.setBackgroundColor(Color.parseColor("#E2E2E2"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#203652"));
            }
            Toast.makeText(this, "Modo Daltônico Desativado", Toast.LENGTH_SHORT).show();
        }
    }

    private void aplicarAltoContraste(boolean ativar) {
        if (ativar) {
            main.setBackgroundColor(Color.BLACK);
            if (headerBackground != null) headerBackground.setBackgroundColor(Color.BLACK);

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

    private void aplicarTemaEscuroEstatico() {
        // Ícone da barra superior
        if (btThemeToggle != null) {
            btThemeToggle.setImageResource(R.drawable.lua);
        }

        // Fundo Principal
        if (main != null) {
            main.setBackgroundColor(Color.parseColor("#12161A"));
        }

        // Barra superior (Header)
        if (headerBackground != null) {
            headerBackground.setBackgroundColor(Color.parseColor("#1B2430"));
        }

        // Cards / Containers
        if (cvContainerList != null) {
            cvContainerList.setCardBackgroundColor(Color.parseColor("#1E293B"));
        }

        // Textos da Tela
        if (tvSaudacao != null) tvSaudacao.setTextColor(Color.parseColor("#E2E8F0"));
        if (tvUser != null) tvUser.setTextColor(Color.parseColor("#56C8A8"));

        // Botão de Ajuda (FAB)
        if (fbInterrogacao2 != null) {
            fbInterrogacao2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#243144")));
            fbInterrogacao2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#E2E8F0")));
        }
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