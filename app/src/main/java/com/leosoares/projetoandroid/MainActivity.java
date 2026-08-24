package com.leosoares.projetoandroid;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ImageButton ibOcultar;
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
        });
    }
}