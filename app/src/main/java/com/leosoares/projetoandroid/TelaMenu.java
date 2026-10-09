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
    private TextView tvUser, tvSaudacao, tvOpcoes, tvExibido4;
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

        // Receber dados da Intent
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("NOME_USUARIO")) {
                String nome = intent.getStringExtra("NOME_USUARIO");
                if (nome != null && !nome.isEmpty()) {
                    tvUser.setText(nome);
                }
            }
            if (intent.hasExtra("TEMA_ATUAL")){
                String nome = intent.getStringExtra("TEMA_ATUAL");
                if (nome != null && !nome.isEmpty()){
                    temaAtual.name();
                }
            }

        }

        // Alternar Modo Noturno / Claro
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

        // Botão de Acessibilidade
        if (ibAcessibilidade3 != null) {
            ibAcessibilidade3.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }

        // Popup de Dúvida
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

        // Configuração do RecyclerView
        if (rvListaResposta != null) {
            rvListaResposta.setLayoutManager(new LinearLayoutManager(this));
            listaEntrevistas = carregarEntrevistasEmProcesso();
            adapter = new EntrevistaAdapter(listaEntrevistas);
            rvListaResposta.setAdapter(adapter);
        }
    }

    private void filtrar(String texto) {
        List<Entrevista> listaFiltrada = new ArrayList<>();

        // Pega a lista original completa
        List<Entrevista> original = carregarEntrevistasEmProcesso();

        for (Entrevista List : original) {
            if (List.getNomeVaga().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(List);
            }
        }

        // Cria um novo adapter temporário apenas com os itens filtrados e joga no RecyclerView
        adapter = new EntrevistaAdapter(listaFiltrada);
        rvListaResposta.setAdapter(adapter);
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
    void aplicarTema(ModoTema novoTema) {
        this.temaAtual = novoTema;

        switch (novoTema) {
            case ALTO_CONTRASTE:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.sol);

                main.setBackgroundColor(Color.parseColor("#0A0F14"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#121D28"));

                if (tvSaudacao != null) tvSaudacao.setTextColor(Color.parseColor("#FFE600"));
                if (tvUser != null) tvUser.setTextColor(Color.parseColor("#00E6A1"));
                if (tvOpcoes != null) tvOpcoes.setTextColor(Color.parseColor("#94A3B8"));

                if (cvContainerList != null) {
                    cvContainerList.setCardBackgroundColor(Color.parseColor("#1B2B3C"));
                }

                // Card de Ajuda Pop-up com moldura verde neon
                GradientDrawable molduraAltoContraste = new GradientDrawable();
                molduraAltoContraste.setShape(GradientDrawable.RECTANGLE);
                molduraAltoContraste.setColor(Color.parseColor("#1B2B3C"));
                molduraAltoContraste.setCornerRadius(32f);
                molduraAltoContraste.setStroke(4, Color.parseColor("#00E6A1"));

                if (cvDuvida3 != null) {
                    cvDuvida3.setBackground(molduraAltoContraste);
                }
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.WHITE);
                if (btEntendi != null) {
                    btEntendi.setBackgroundColor(Color.parseColor("#00E6A1"));
                    btEntendi.setTextColor(Color.parseColor("#0A0F14"));
                }

                if (fbInterrogacao2 != null) {
                    fbInterrogacao2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B2B3C")));
                    fbInterrogacao2.setImageTintList(ColorStateList.valueOf(Color.parseColor("#00E6A1")));
                }

                Toast.makeText(this, "Alto Contraste Ativado", Toast.LENGTH_SHORT).show();
                break;

                // MODO DALTONICO
            case DALTONICO:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.sol);
                if (tvUser != null) tvUser.setTextColor(Color.parseColor("#6d04ff"));


                break;

            case ESCURO:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.lua);

                animarCorFundo(main, "#E2E2E2", "#12161A");
                if (headerBackground != null) animarCorFundo(headerBackground, "#203652", "#1B2430");

                if (cvContainerList != null) animarCorCard(cvContainerList, "#FFFFFF", "#1E293B");
                if (cvDuvida3 != null) animarCorCard(cvDuvida3, "#FFFFFF", "#243144");

                animarCorTexto(tvSaudacao, "#1F4273", "#E2E8F0");
                animarCorTexto(tvUser, "#47B192", "#56C8A8");
                animarCorTexto(tvOpcoes, "#5A6E85", "#94A3B8");
                animarCorTexto(tvExibido4, "#212121", "#E2E8F0");

                if (btEntendi != null) {
                    btEntendi.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi.setTextColor(Color.WHITE);
                }

                animarCorFAB(fbInterrogacao2, "#FFFFFF", "#243144", "#000000", "#E2E8F0");

                Toast.makeText(this, "Modo Escuro Ativado", Toast.LENGTH_SHORT).show();
                break;

            case CLARO:
            default:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.sol);
                main.setBackgroundColor(Color.parseColor("#E2E2E2"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#203652"));

                if (cvContainerList != null) {
                    cvContainerList.setCardBackgroundColor(Color.WHITE);
                }
                if (cvDuvida3 != null) {
                    cvDuvida3.setCardBackgroundColor(Color.WHITE);
                }
                if (tvSaudacao != null) tvSaudacao.setTextColor(Color.parseColor("#1F4273"));
                if (tvUser != null) tvUser.setTextColor(Color.parseColor("#47B192"));
                if (tvOpcoes != null) tvOpcoes.setTextColor(Color.parseColor("#5A6E85"));
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#FFFFFF"));
                if (btEntendi != null) {
                    btEntendi.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi.setTextColor(Color.WHITE);
                }
                if (fbInterrogacao2 != null) {
                    fbInterrogacao2.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    fbInterrogacao2.setImageTintList(ColorStateList.valueOf(Color.BLACK));
                }

                Toast.makeText(this, "Modo Claro Ativado", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //  Métodos Auxiliares de transição suave (VALUE ANIMATOR)

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