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
        cvDuvida = (CardView) findViewById(R.id.cvDuvida2);
        tvExibido = (TextView) findViewById(R.id.tvExibido);

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TelaMenu.class);
                startActivity(intent);
            }
        }); // Enviar para próxima tela


        fbInterrogacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancela qualquer temporizador ativo para evitar bugs se clicar várias vezes
                v.animate().cancel();
                cvDuvida.animate().cancel();

                // Efeito Surgimento (Fade-in)
                cvDuvida.setAlpha(0f); // Começa totalmente invisível
                cvDuvida.setVisibility(View.VISIBLE);
                cvDuvida.animate()
                        .alpha(1f) // 100% visível
                        .setDuration(400) // Duração do efeito (400 milissegundos)
                        .setListener(null);

                // Temporizador de 4 segundos
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Efeito Sumir (Fade-out)
                        cvDuvida.animate()
                                .alpha(0f) // Ativar transparencia
                                .setDuration(400)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        cvDuvida.setVisibility(View.GONE);
                                    }
                                });
                    }
                }, 4000); // 4000 milissegundos = 4 segundos
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

            }
        });
    }
}