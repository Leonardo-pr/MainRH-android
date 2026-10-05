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
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/*
   HubCandidatoActivity:
   Gerencia a seleção de etapas do candidato com suporte ao Modo Escuro animado.
*/

public class HubCandidatoActivity extends AppCompatActivity {

    private ImageButton btEntrevista, btDin, btThemeToggle2;
    private TextView tvProcessoEn, tvProcessoDi, tvNomeCandidato, tvSubtitulo, tvExibido4;
    private CardView cvDuvida3;
    private FloatingActionButton fbInterrogacao3;
    private Button btEntendi4;
    private View headerBackground, main;

    private boolean modoEscuro = false;

    private boolean passouEntrevista = true;
    private boolean realizouEntrevista = true;

    private boolean passouDinamica = false;
    private boolean realizouDinamica = false;

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

        // Mapeamento dos Componentes (baseado na Component Tree)
        main = findViewById(R.id.main);
        headerBackground = findViewById(R.id.headerBackground);
        btThemeToggle2 = findViewById(R.id.btThemeToggle2);
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

        // Recebe o nome do candidato enviado pela tela anterior
        Intent intentRecebida = getIntent();
        if (intentRecebida != null && intentRecebida.hasExtra("NOME_CANDIDATO")) {
            String nome = intentRecebida.getStringExtra("NOME_CANDIDATO");
            if (nome != null && !nome.isEmpty()) {
                tvNomeCandidato.setText(nome);
            }
        }

        // --- MODO NOTURNO / CLARO ---
        btThemeToggle2.setOnClickListener(v -> {
            modoEscuro = !modoEscuro;

            // Giro do botão Sol/Lua
            btThemeToggle2.animate().rotationBy(360f).setDuration(400).start();

            if (modoEscuro) {
                btThemeToggle2.setImageResource(R.drawable.lua);

                // Animações de Transição de Cores
                animarCorFundo(main, "#E2E2E2", "#12161A");
                if (headerBackground != null) animarCorFundo(headerBackground, "#203652", "#1B2430");
                animarCorCard(cvDuvida3, "#FFFFFF", "#243144");

                // Textos
                animarCorTexto(tvNomeCandidato, "#203652", "#E2E8F0");
                animarCorTexto(tvSubtitulo, "#555555", "#94A3B8");
                animarCorTexto(tvProcessoEn, "#203652", "#E2E8F0");
                animarCorTexto(tvProcessoDi, "#203652", "#E2E8F0");
                if (tvExibido4 != null) animarCorTexto(tvExibido4, "#212121", "#E2E8F0");

                // Botão de Ajuda (FAB)
                animarCorFAB(fbInterrogacao3, "#FFFFFF", "#243144", "#000000", "#E2E8F0");

            } else {
                btThemeToggle2.setImageResource(R.drawable.sol);

                // Volta para o Modo Claro
                animarCorFundo(main, "#12161A", "#E2E2E2");
                if (headerBackground != null) animarCorFundo(headerBackground, "#1B2430", "#203652");
                animarCorCard(cvDuvida3, "#243144", "#FFFFFF");

                // Textos
                animarCorTexto(tvNomeCandidato, "#E2E8F0", "#203652");
                animarCorTexto(tvSubtitulo, "#94A3B8", "#555555");
                animarCorTexto(tvProcessoEn, "#E2E8F0", "#203652");
                animarCorTexto(tvProcessoDi, "#E2E8F0", "#203652");
                if (tvExibido4 != null) animarCorTexto(tvExibido4, "#E2E8F0", "#212121");

                // Botão de Ajuda (FAB)
                animarCorFAB(fbInterrogacao3, "#243144", "#FFFFFF", "#E2E8F0", "#000000");
            }
        });

        // Abrir Pop-up de Ajuda
        fbInterrogacao3.setOnClickListener(v -> {
            cvDuvida3.animate().cancel();
            cvDuvida3.setAlpha(0f);
            cvDuvida3.setVisibility(View.VISIBLE);
            cvDuvida3.animate().alpha(1f).setDuration(400).setListener(null);
        });

        // Fechar Pop-up
        btEntendi4.setOnClickListener(v -> cvDuvida3.animate()
                .alpha(0f)
                .setDuration(400)
                .withEndAction(() -> cvDuvida3.setVisibility(View.GONE)));

        // Listeners dos botões de etapas
        btEntrevista.setOnClickListener(v -> {
            Intent intent = new Intent(HubCandidatoActivity.this, InformacaoCandidato.class);
            intent.putExtra("TIPO_PROCESSO", "ENTREVISTA");
            intent.putExtra("NOME_CANDIDATO", tvNomeCandidato.getText().toString());
            startActivity(intent);
        });

        btDin.setOnClickListener(v -> {
            Intent intent = new Intent(HubCandidatoActivity.this, InformacaoCandidato.class);
            intent.putExtra("TIPO_PROCESSO", "DINAMICA");
            intent.putExtra("NOME_CANDIDATO", tvNomeCandidato.getText().toString());
            startActivity(intent);
        });
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
}