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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    ImageButton ibOcultar;
    Button btEntendi;
    CardView cvDuvida;
    TextView tvExibido;
    FloatingActionButton fbInterrogacao;
    Button btEntrar;
    EditText edUsuario;
    EditText edSenha;

    boolean isVisivel = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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
        tvExibido = (TextView) findViewById(R.id.tvExibido3);
        btEntendi = (Button) findViewById(R.id.btEntendi3);



        // 1. Abrir o pop-up ao clicar no botão de interrogação
        fbInterrogacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvDuvida.animate().cancel();

                // Exibe o card com efeito Fade-in
                cvDuvida.setAlpha(0f);
                cvDuvida.setVisibility(View.VISIBLE);
                cvDuvida.animate()
                        .alpha(1f)
                        .setDuration(400)
                        .setListener(null);
            }
        });

// 2. Fechar o pop-up ao clicar no botão "Entendi"
        btEntendi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Esconde o card com efeito Fade-out
                cvDuvida.animate()
                        .alpha(0f)
                        .setDuration(400)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                cvDuvida.setVisibility(View.GONE);
                            }
                        });
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
                // 1. Pegar o texto digitado
                String nomeUsuario = edUsuario.getText().toString().trim();

                // 2. Criar a Intent para abrir a TelaMenu
                Intent intent = new Intent(MainActivity.this, TelaMenu.class);

                // 3. Anexar o dado com uma chave identificadora (ex: "NOME_USUARIO")
                intent.putExtra("NOME_USUARIO", nomeUsuario);

                // 4. Iniciar a nova Activity
                startActivity(intent);
            }
        });
    }
}