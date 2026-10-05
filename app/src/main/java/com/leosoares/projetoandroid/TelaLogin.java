package com.leosoares.projetoandroid;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/*
   TelaLogin:
   Possui transição suave para Modo Escuro, Modo Daltônico,
   controle de exibição de senha e envio de dados para Splash/Menu.
*/

public class TelaLogin extends AppCompatActivity {

    private ImageButton ibOcultar, btThemeToggle, ibAcessibilidade;
    private Button btEntendi, btEntrar;
    private View headerBackground3;
    private CardView cvDuvida, cardView;
    private TextView tvExibido;
    private FloatingActionButton fbInterrogacao;
    private EditText edUsuario, edSenha;
    private ConstraintLayout main;
    private FrameLayout frameLayout;
    private boolean isVisivel = false;
    private boolean modoEscuro = false;
    private boolean modoDaltonico = false;
    private boolean modoAltoContraste = false;


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

        // Mapeamento dos Componentes
        main = findViewById(R.id.main);

        cardView = findViewById(R.id.cardView);
        headerBackground3 = findViewById(R.id.headerBackground3);
        ibOcultar = findViewById(R.id.ibOcultar);
        ibAcessibilidade = findViewById(R.id.ibAcessibilidade);
        edSenha = findViewById(R.id.edSenha);
        edUsuario = findViewById(R.id.edUsuario);
        btEntrar = findViewById(R.id.btEntrar);
        fbInterrogacao = findViewById(R.id.fbInterrogacao);
        cvDuvida = findViewById(R.id.cvDuvida3);
        tvExibido = findViewById(R.id.tvExibido4);
        btEntendi = findViewById(R.id.btEntendi4);
        btThemeToggle = findViewById(R.id.btThemeToggle);

        // Modo Claro ou Escuro
        btThemeToggle.setOnClickListener(v -> {
            modoEscuro = !modoEscuro;

            // Giro do botão Sol/Lua
            btThemeToggle.animate().rotationBy(360f).setDuration(400).start();

            if (modoEscuro) {
                btThemeToggle.setImageResource(R.drawable.lua);

                // Animações de Transição de Cores
                animarCorFundo(main, "#E2E2E2", "#12161A");
                if (frameLayout != null) animarCorFundo(frameLayout, "#203652", "#1B2430");
                animarCorCard(cardView, "#FFFFFF", "#1E293B");
                animarCorCard(cvDuvida, "#FFFFFF", "#243144");
                animarCorFundo(btEntrar, "#1A304B", "#2B4C7E");

                // Textos e Hints
                animarCorTexto(edUsuario, "#000000", "#E2E8F0");
                animarCorTexto(edSenha, "#000000", "#E2E8F0");
                animarCorTexto(tvExibido, "#212121", "#E2E8F0");
                edUsuario.setHintTextColor(Color.parseColor("#94A3B8"));
                edSenha.setHintTextColor(Color.parseColor("#94A3B8"));

                // Botão de Ajuda (FAB)
                animarCorFAB(fbInterrogacao, "#FFFFFF", "#243144", "#000000", "#E2E8F0");

            } else {
                btThemeToggle.setImageResource(R.drawable.sol);

                // Volta para o Modo Claro
                animarCorFundo(main, "#12161A", "#E2E2E2");
                if (frameLayout != null) animarCorFundo(frameLayout, "#1B2430", "#203652");
                animarCorCard(cardView, "#1E293B", "#FFFFFF");
                animarCorCard(cvDuvida, "#243144", "#FFFFFF");
                animarCorFundo(btEntrar, "#2B4C7E", "#1A304B");

                animarCorTexto(edUsuario, "#E2E8F0", "#000000");
                animarCorTexto(edSenha, "#E2E8F0", "#000000");
                animarCorTexto(tvExibido, "#E2E8F0", "#212121");
                edUsuario.setHintTextColor(Color.parseColor("#8E8E93"));
                edSenha.setHintTextColor(Color.parseColor("#8E8E93"));

                animarCorFAB(fbInterrogacao, "#243144", "#FFFFFF", "#E2E8F0", "#000000");
            }
        });

        // Abrir Pop-up de Ajuda
        fbInterrogacao.setOnClickListener(v -> {
            cvDuvida.animate().cancel();
            cvDuvida.setAlpha(0f);
            cvDuvida.setVisibility(View.VISIBLE);
            cvDuvida.animate().alpha(1f).setDuration(400).setListener(null);
        });

        if (ibAcessibilidade != null) {
            ibAcessibilidade.setOnClickListener(v -> abrirBottomSheetAcessibilidade());
        }

        // Fechar Pop-up
        btEntendi.setOnClickListener(v -> cvDuvida.animate()
                .alpha(0f)
                .setDuration(400)
                .withEndAction(() -> cvDuvida.setVisibility(View.GONE)));

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
            intent.putExtra("MODO_ESCURO", modoEscuro);
            startActivity(intent);
        });
    }

    // Métodos Auxiliares de transição suave (VALUE ANIMATOR)

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
            if (headerBackground3 != null) headerBackground3.setBackgroundColor(Color.parseColor("#0057B8"));

            Toast.makeText(this, "Modo Daltônico Ativado", Toast.LENGTH_SHORT).show();
        } else {
            // Restaura cores originais
            if (modoEscuro) {
                main.setBackgroundColor(Color.parseColor("#12161A"));
                if (headerBackground3 != null) headerBackground3.setBackgroundColor(Color.parseColor("#1B2430"));
            } else {
                main.setBackgroundColor(Color.parseColor("#E2E2E2"));
                if (headerBackground3 != null) headerBackground3.setBackgroundColor(Color.parseColor("#203652"));
            }
            Toast.makeText(this, "Modo Daltônico Desativado", Toast.LENGTH_SHORT).show();
        }
    }

    private void aplicarAltoContraste(boolean ativar) {
        if (ativar) {
            main.setBackgroundColor(Color.BLACK);
            if (headerBackground3 != null) headerBackground3.setBackgroundColor(Color.BLACK);

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
}