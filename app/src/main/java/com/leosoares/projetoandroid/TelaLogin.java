package com.leosoares.projetoandroid;

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

import androidx.activity.EdgeToEdge;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TelaLogin extends BaseActivity {

    // Enum para controlar os estados de temas
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
        frameLayout = findViewById(R.id.frameLayout2);

        // Carrega o tema salvo ao abrir o aplicativo
        sincronizarTemaSalvo();

        // Pop up Acessibilidade
        if (ibAcessibilidade != null) {
            ibAcessibilidade.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }

        // Pop up Ajuda
        if (fbInterrogacao != null) {
            fbInterrogacao.setOnClickListener(v -> {
                cvDuvida3.animate().cancel();
                cvDuvida3.setAlpha(0f);
                cvDuvida3.setVisibility(View.VISIBLE);
                cvDuvida3.animate().alpha(1f).setDuration(400).setListener(null);
            });
        }

        // Fechar Pop-up de Ajuda
        if (btEntendi4 != null) {
            btEntendi4.setOnClickListener(v -> cvDuvida3.animate()
                    .alpha(0f)
                    .setDuration(400)
                    .withEndAction(() -> cvDuvida3.setVisibility(View.GONE)));
        }

        // Exibir / Ocultar Senha
        if (ibOcultar != null) {
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
        }

        // Botão Entrar
        if (btEntrar != null) {
                btEntrar.setOnClickListener(v -> {
                    String nomeUsuario = edUsuario.getText().toString().trim();
                    // Salva o nome na memória global do app
                    android.content.SharedPreferences prefs = getSharedPreferences("MainRH_Prefs", MODE_PRIVATE);
                    prefs.edit().putString("NOME_USUARIO_LOGADO", nomeUsuario).apply();
                    // Continua com a Intent normal
                    Intent intent = new Intent(TelaLogin.this, SplashActivity.class);
                    intent.putExtra("NOME_USUARIO", nomeUsuario);
                    startActivity(intent);
                });
        }
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

    // Salva no gerenciador e aplica a cor
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

    // Cores Temas
    private void aplicarTema(ModoTema novoTema) {
        this.temaAtual = novoTema;

        switch (novoTema) {
            case ALTO_CONTRASTE:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.sol);
                if (main != null) main.setBackgroundColor(Color.parseColor("#0A0F14"));
                if (ivLogo != null) ivLogo.setImageResource(R.drawable.altocontraste_logo);
                if (headerBackground3 != null) headerBackground3.setBackgroundColor(Color.parseColor("#121D28"));
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
                if (cvDuvida3 != null) {
                    GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.RECTANGLE);
                    shape.setColor(Color.parseColor("#1B2B3C"));
                    shape.setCornerRadius(32f);
                    shape.setStroke(4, Color.parseColor("#00E6A1"));
                    cvDuvida3.setBackground(shape);
                }
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.WHITE);
                if (tvAtencao != null) tvAtencao.setTextColor(Color.parseColor("#FFE600"));
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#00E6A1"));
                    btEntendi4.setTextColor(Color.parseColor("#0A0F14"));
                }
                if (fbInterrogacao != null) {
                    fbInterrogacao.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1B2B3C")));
                    fbInterrogacao.setImageTintList(ColorStateList.valueOf(Color.parseColor("#00E6A1")));
                }
                break;

            case DALTONICO:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.sol);
                if (main != null) main.setBackgroundColor(Color.WHITE);
                if (headerBackground3 != null) headerBackground3.setBackgroundColor(Color.parseColor("#0057B8"));
                if (cardView != null) cardView.setCardBackgroundColor(Color.parseColor("#F5F5F5"));

                if (cvDuvida3 != null) {
                    GradientDrawable shapeDaltonico = new GradientDrawable();
                    shapeDaltonico.setShape(GradientDrawable.RECTANGLE);
                    shapeDaltonico.setColor(Color.WHITE);
                    shapeDaltonico.setCornerRadius(32f);
                    shapeDaltonico.setStroke(4, Color.parseColor("#0057B8"));
                    cvDuvida3.setBackground(shapeDaltonico);
                }
                if (btEntrar != null) {
                    btEntrar.setBackgroundColor(Color.parseColor("#0057B8"));
                    btEntrar.setTextColor(Color.WHITE);
                }
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#0057B8"));
                    btEntendi4.setTextColor(Color.WHITE);
                }
                if (tvAtencao != null) tvAtencao.setTextColor(Color.parseColor("#0057B8"));
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#333333"));
                if (edUsuario != null) {
                    edUsuario.setTextColor(Color.BLACK);
                    edUsuario.setHintTextColor(Color.parseColor("#666666"));
                }
                if (edSenha != null) {
                    edSenha.setTextColor(Color.BLACK);
                    edSenha.setHintTextColor(Color.parseColor("#666666"));
                }
                if (ivLogo != null) ivLogo.setImageResource(R.drawable.daltonico_logo);
                if (fbInterrogacao != null) {
                    fbInterrogacao.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0057B8")));
                    fbInterrogacao.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                }
                break;

            case ESCURO:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.lua);
                if (main != null) main.setBackgroundColor(Color.parseColor("#12161A"));
                if (ivLogo != null) ivLogo.setImageResource(R.drawable.main_rh);
                if (headerBackground3 != null) headerBackground3.setBackgroundColor(Color.parseColor("#1B2430"));
                if (cardView != null) cardView.setCardBackgroundColor(Color.parseColor("#1E293B"));

                if (btEntrar != null) {
                    btEntrar.setBackgroundColor(Color.parseColor("#2B4C7E"));
                    btEntrar.setTextColor(Color.WHITE);
                }
                if (edUsuario != null) {
                    edUsuario.setTextColor(Color.parseColor("#E2E8F0"));
                    edUsuario.setHintTextColor(Color.parseColor("#94A3B8"));
                }
                if (edSenha != null) {
                    edSenha.setTextColor(Color.parseColor("#E2E8F0"));
                    edSenha.setHintTextColor(Color.parseColor("#94A3B8"));
                }
                if (cvDuvida3 != null) {
                    GradientDrawable shapeEscuro = new GradientDrawable();
                    shapeEscuro.setShape(GradientDrawable.RECTANGLE);
                    shapeEscuro.setColor(Color.parseColor("#243144"));
                    shapeEscuro.setCornerRadius(32f);
                    cvDuvida3.setBackground(shapeEscuro);
                }
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#E2E8F0"));
                if (tvAtencao != null) tvAtencao.setTextColor(Color.parseColor("#FFB800"));
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }
                if (fbInterrogacao != null) {
                    fbInterrogacao.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#243144")));
                    fbInterrogacao.setImageTintList(ColorStateList.valueOf(Color.parseColor("#E2E8F0")));
                }
                if (ivLogo != null) ivLogo.setImageResource(R.drawable.modoescuro_logo);
                if (fbInterrogacao != null) {
                    fbInterrogacao.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0057B8")));
                    fbInterrogacao.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                }
                break;

            case CLARO:
            default:
                if (btThemeToggle != null) btThemeToggle.setImageResource(R.drawable.sol);
                if (ivLogo != null) ivLogo.setImageResource(R.drawable.main_rh);
                if (main != null) main.setBackgroundColor(Color.parseColor("#FCF5FD"));
                if (headerBackground3 != null) headerBackground3.setBackgroundColor(Color.parseColor("#203652"));
                if (cardView != null) cardView.setCardBackgroundColor(Color.WHITE);

                if (btEntrar != null) {
                    btEntrar.setBackgroundColor(Color.parseColor("#1A304B"));
                    btEntrar.setTextColor(Color.WHITE);
                }
                if (edUsuario != null) {
                    edUsuario.setTextColor(Color.BLACK);
                    edUsuario.setHintTextColor(Color.parseColor("#8E8E93"));
                }
                if (edSenha != null) {
                    edSenha.setTextColor(Color.BLACK);
                    edSenha.setHintTextColor(Color.parseColor("#8E8E93"));
                }
                if (cvDuvida3 != null) {
                    GradientDrawable shapeClaro = new GradientDrawable();
                    shapeClaro.setShape(GradientDrawable.RECTANGLE);
                    shapeClaro.setColor(Color.WHITE);
                    shapeClaro.setCornerRadius(32f);
                    cvDuvida3.setBackground(shapeClaro);
                }
                if (tvExibido4 != null) tvExibido4.setTextColor(Color.parseColor("#212121"));
                if (tvAtencao != null) tvAtencao.setTextColor(Color.parseColor("#FEBE11"));
                if (btEntendi4 != null) {
                    btEntendi4.setBackgroundColor(Color.parseColor("#47B192"));
                    btEntendi4.setTextColor(Color.WHITE);
                }
                if (fbInterrogacao != null) {
                    fbInterrogacao.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    fbInterrogacao.setImageTintList(ColorStateList.valueOf(Color.BLACK));
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