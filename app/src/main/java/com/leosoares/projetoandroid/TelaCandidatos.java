package com.leosoares.projetoandroid;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

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

public class TelaCandidatos extends BaseActivity {

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
    private ImageButton ibAcessibilidade2;
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
        tvCandi = findViewById(R.id.tvCandi);
        cvContainerList = findViewById(R.id.cvContainerList);
        svBusca = findViewById(R.id.svBusca);
        rvListaResposta = findViewById(R.id.rvListaResposta);
        fbInterrogacao4 = findViewById(R.id.fbInterrogacao4);
        cvDuvida4 = findViewById(R.id.cvDuvida4);
        tvExibido4 = findViewById(R.id.tvExibido4);
        btEntendi4 = findViewById(R.id.btEntendi4);
        ibAcessibilidade2 = findViewById(R.id.ibAcessibilidade);

        // Sincroniza o tema salvo ao abrir a tela
        sincronizarTemaSalvo();

        listaCandidatos = carregarCandidatosEmProcesso();
        rvListaResposta.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CandidatoAdapter(listaCandidatos);
        rvListaResposta.setAdapter(adapter);

        // Perfil do Usuário
        CardView cvFotoPerfil = findViewById(R.id.cvFotoPerfil);
        if (cvFotoPerfil != null) {
            cvFotoPerfil.setOnClickListener(v -> {
                android.content.SharedPreferences prefs = getSharedPreferences("MainRH_Prefs", MODE_PRIVATE);
                String nomeLogado = prefs.getString("NOME_USUARIO_LOGADO", "Recrutador");

                PerfilBottomSheet perfilSheet = new PerfilBottomSheet(nomeLogado);
                perfilSheet.show(getSupportFragmentManager(), "PerfilBottomSheet");
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


        // Botão de Acessibilidade (BottomSheet)
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

    @Override
    protected void onResume() {
        super.onResume();
        sincronizarTemaSalvo();
    }

    // Lê o que está salvo no ThemeManager e aplica
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

    // Salva globalmente no ThemeManager e aplica o tema na tela
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

    // Aplicação visual de cores para cada Modo de Tema
    private void aplicarTema(ModoTema novoTema) {
        this.temaAtual = novoTema;

        switch (novoTema) {
            case ALTO_CONTRASTE:
                if (main != null) main.setBackgroundColor(Color.parseColor("#0A0F14"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#121D28"));
                if (tvCandi != null) tvCandi.setTextColor(Color.parseColor("#FFE600"));
                if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.parseColor("#1B2B3C"));

                if (cvDuvida4 != null) {
                    GradientDrawable molduraAltoContraste = new GradientDrawable();
                    molduraAltoContraste.setShape(GradientDrawable.RECTANGLE);
                    molduraAltoContraste.setColor(Color.parseColor("#1B2B3C"));
                    molduraAltoContraste.setCornerRadius(32f);
                    molduraAltoContraste.setStroke(4, Color.parseColor("#00E6A1"));
                    cvDuvida4.setBackground(molduraAltoContraste);
                }
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.WHITE);
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#00E6A1"));
                    btEntendi4.setTextColor(Color.parseColor("#0A0F14"));
                }
                if (fbInterrogacao4 != null) {
                    fbInterrogacao4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B2B3C")));
                    fbInterrogacao4.setImageTintList(ColorStateList.valueOf(Color.parseColor("#00E6A1")));
                }
                break;

            case DALTONICO:
                if (main != null) main.setBackgroundColor(Color.parseColor("#FFFFFF"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#0057B8"));
                if (tvCandi != null) tvCandi.setTextColor(Color.parseColor("#0057B8"));
                if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.parseColor("#F5F5F5"));

                if (cvDuvida4 != null) {
                    GradientDrawable bgDaltonico = new GradientDrawable();
                    bgDaltonico.setShape(GradientDrawable.RECTANGLE);
                    bgDaltonico.setColor(Color.WHITE);
                    bgDaltonico.setCornerRadius(32f);
                    bgDaltonico.setStroke(4, Color.parseColor("#0057B8"));
                    cvDuvida4.setBackground(bgDaltonico);
                }
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#333333"));
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#0057B8"));
                    btEntendi4.setTextColor(Color.WHITE);
                }
                if (fbInterrogacao4 != null) {
                    fbInterrogacao4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0057B8")));
                    fbInterrogacao4.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                }
                break;

            case ESCURO:
                if (main != null) main.setBackgroundColor(Color.parseColor("#12161A"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#1B2430"));
                if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.parseColor("#1E293B"));

                if (cvDuvida4 != null) {
                    GradientDrawable bgEscuro = new GradientDrawable();
                    bgEscuro.setShape(GradientDrawable.RECTANGLE);
                    bgEscuro.setColor(Color.parseColor("#243144"));
                    bgEscuro.setCornerRadius(32f);
                    cvDuvida4.setBackground(bgEscuro);
                }
                if (tvCandi != null) tvCandi.setTextColor(Color.parseColor("#E2E8F0"));
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#E2E8F0"));
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }
                if (fbInterrogacao4 != null) {
                    fbInterrogacao4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#243144")));
                    fbInterrogacao4.setImageTintList(ColorStateList.valueOf(Color.parseColor("#E2E8F0")));
                }
                break;

            case CLARO:
            default:
                if (main != null) main.setBackgroundColor(Color.parseColor("#E2E2E2"));
                if (headerBackground != null) headerBackground.setBackgroundColor(Color.parseColor("#203652"));
                if (cvContainerList != null) cvContainerList.setCardBackgroundColor(Color.WHITE);

                if (cvDuvida4 != null) {
                    GradientDrawable bgClaro = new GradientDrawable();
                    bgClaro.setShape(GradientDrawable.RECTANGLE);
                    bgClaro.setColor(Color.WHITE);
                    bgClaro.setCornerRadius(32f);
                    cvDuvida4.setBackground(bgClaro);
                }
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
                break;
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}