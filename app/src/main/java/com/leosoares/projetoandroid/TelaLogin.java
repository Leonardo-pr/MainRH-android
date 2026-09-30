package com.leosoares.projetoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
/*
   TelaLogin, Proposta:
   - O Usuário ao abrir o aplicativo está será a primeira tela
   Ela pedi duas coisas: Usuário e Senha
   Quando ele colocar as informções corretas ele será enviado para a proxima tela que será a TelaMenu

   Nesse arquivo java temos 5 funções

   fbInterrogação animated: O botão flutuante treme por um 4 segundos na tela, para o usuário identifique o botão
   logo quando entrar no aplicativo.

   fbInterrogação clicklistener e btEntendi: Ao clicar no botão de dúvida aparecera um pop up na tela, explicando
   a função da tela pro usuário, tem o botão de btEntendi, que serve para fechar o pop-up presente na tela.

   btEntrar clicklistener: Ao clicar no entrar, irá salvar o nome escrito o textfield e enviará para
   para proxima tela, e também o usuario vai ser enviado para a tela splash.

   ibOcultar clicklistener: A imagem de esconder e mostrar senha (Olho), ao entrar no app ele vai estar
   censurado por padrão, se clicar no botão do olho a senha será mostrada.

   O que falta fazer:

   O nome Usuário e a Senha, são dados que estão salvos no Banco de Dados, ou seja tem que impedir usuários
   com dados não cadastrados de entrar

   Extra: Pode ser de criarmos a lógica de email avisando que o tal usuário esqueceu a senha.

*/

public class TelaLogin extends AppCompatActivity {

    ImageButton ibOcultar;
    Button btEntendi;
    CardView cvDuvida;
    TextView tvExibido;
    FloatingActionButton fbInterrogacao;
    Button btEntrar;
    EditText edUsuario;
    EditText edSenha;
    boolean isVisivel = false;
    private boolean modoDaltonico = false;
    ConstraintLayout main;
    CardView cardView;
    Button btnDaltonico;

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

        ibOcultar = (ImageButton) findViewById(R.id.ibOcultar);
        edSenha = (EditText) findViewById(R.id.edSenha);
        edUsuario = (EditText) findViewById(R.id.edUsuario);
        btEntrar = (Button) findViewById(R.id.btEntrar);
        fbInterrogacao = (FloatingActionButton) findViewById(R.id.fbInterrogacao);
        cvDuvida = (CardView) findViewById(R.id.cvDuvida3);
        tvExibido = (TextView) findViewById(R.id.tvExibido4);
        btEntendi = (Button) findViewById(R.id.btEntendi4);
        Button btnDaltonico = findViewById(R.id.btnDaltonico);
        main = findViewById(R.id.main);
        cardView = findViewById(R.id.cardView);
        btnDaltonico = findViewById(R.id.btnDaltonico);


        // Animação do botão
        fbInterrogacao.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300).withEndAction(()
                -> fbInterrogacao.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300));

        Button finalBtnDaltonico = btnDaltonico;
        btnDaltonico.setOnClickListener(v -> {

            modoDaltonico = !modoDaltonico;

            if (modoDaltonico) {

                // Fundo principal
                main.setBackgroundColor(Color.WHITE);

                // Card Login
                cardView.setCardBackgroundColor(Color.parseColor("#F5F5F5"));

                // Card Ajuda
                cvDuvida.setCardBackgroundColor(Color.WHITE);

                // Botão Entrar
                btEntrar.setBackgroundColor(Color.parseColor("#0057B8"));

                // Botão Entendi
                btEntendi.setBackgroundColor(Color.parseColor("#0057B8"));

                // Texto do popup
                tvExibido.setTextColor(Color.BLACK);

                // Campos de texto
                edUsuario.setTextColor(Color.BLACK);
                edSenha.setTextColor(Color.BLACK);

                // Hint dos campos
                edUsuario.setHintTextColor(Color.DKGRAY);
                edSenha.setHintTextColor(Color.DKGRAY);

                // Botão modo daltônico
                finalBtnDaltonico.setBackgroundColor(Color.parseColor("#0057B8"));
                finalBtnDaltonico.setTextColor(Color.WHITE);
                finalBtnDaltonico.setText("Modo Daltônico: ON");

            } else {

                main.setBackgroundColor(Color.parseColor("#E2E2E2"));

                cardView.setCardBackgroundColor(Color.WHITE);

                cvDuvida.setCardBackgroundColor(Color.WHITE);

                btEntrar.setBackgroundColor(Color.parseColor("#1A304B"));

                btEntendi.setBackgroundColor(Color.parseColor("#4CAF50"));

                tvExibido.setTextColor(Color.parseColor("#212121"));

                edUsuario.setTextColor(Color.BLACK);
                edSenha.setTextColor(Color.BLACK);

                edUsuario.setHintTextColor(Color.parseColor("#8E8E93"));
                edSenha.setHintTextColor(Color.parseColor("#8E8E93"));

                finalBtnDaltonico.setBackgroundColor(Color.LTGRAY);
                finalBtnDaltonico.setTextColor(Color.BLACK);
                finalBtnDaltonico.setText("Modo Daltônico: OFF");
            }
        });

        // Abrir o pop-up ao clicar no botão de interrogação
        fbInterrogacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvDuvida.animate().cancel();

                // Exibe o card
                cvDuvida.setAlpha(0f);
                cvDuvida.setVisibility(View.VISIBLE);
                cvDuvida.animate()
                        .alpha(1f)
                        .setDuration(400)
                        .setListener(null);
            }
        });

       // Fechar o pop-up
        btEntendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvDuvida.animate()
                        .alpha(0f)
                        .setDuration(400)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                cvDuvida.setVisibility(View.GONE);
                            }
                        }); // Esconde o card

            }
        });

        ibOcultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVisivel){
                    edSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ibOcultar.setImageResource(R.drawable.exibirsenha);
                    isVisivel = false;
                }else {
                    edSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ibOcultar.setImageResource(R.drawable.ocultarsenha);
                    isVisivel = true;
                }
                edSenha.setSelection(edSenha.getText().length());
            }
        }); //Ocultar ou exibir senha

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeUsuario = edUsuario.getText().toString().trim();
                Intent intent = new Intent(TelaLogin.this, SplashActivity.class);

                //Anexar o dado com uma chave identificadora
                intent.putExtra("NOME_USUARIO", nomeUsuario);
                startActivity(intent);
            }
        });
    }
}