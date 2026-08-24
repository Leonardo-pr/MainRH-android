package com.leosoares.projetoandroid

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text

class TelaMenu : AppCompatActivity() {

    private lateinit var tvSaudacao: TextView
    private lateinit var tvUser: TextView
    private lateinit var tvOpcoes: TextView
    private lateinit var tvProcessoDi: TextView
    private lateinit var tvProcessoEn: TextView
    private lateinit var cvFotoPerfil: CardView
    private lateinit var ivFotoPerfil: ImageView
    private lateinit var fbDinamica: FloatingActionButton
    private lateinit var fbEntrevista: FloatingActionButton

    private fun inicializarViews() {
        tvSaudacao = findViewById(R.id.tvSaudacao)
        tvUser = findViewById(R.id.tvUser)
        tvOpcoes = findViewById(R.id.tvOpcoes)
        tvProcessoDi = findViewById(R.id.tvProcessoDi)
        tvProcessoEn = findViewById(R.id.tvProcessoEn)
        cvFotoPerfil = findViewById(R.id.cvFotoPerfil)
        ivFotoPerfil = findViewById(R.id.ivFotoPerfil)
        fbDinamica = findViewById(R.id.fbDinamica)
        fbEntrevista = findViewById(R.id.fbEntrevista)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun configurarListeners() {
        // Clique na foto de perfil
        cvFotoPerfil.setOnClickListener {
            Toast.makeText(this, "Perfil clicado!", Toast.LENGTH_SHORT).show()
            // Adicione a navegação para a tela de perfil aqui
        }

        // Clique no botão Processo Dinâmica
        fbDinamica.setOnClickListener {
            Toast.makeText(this, "Abrindo Processo Dinâmica...", Toast.LENGTH_SHORT).show()
            // Intent para a tela de dinâmica
        }

        // Clique no botão Processo Entrevista
        fbEntrevista.setOnClickListener {
            Toast.makeText(this, "Abrindo Processo Entrevista...", Toast.LENGTH_SHORT).show()
            // Intent para a tela de entrevista
        }
    }
}