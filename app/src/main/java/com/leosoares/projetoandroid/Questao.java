package com.leosoares.projetoandroid;

public class Questao {

    private String enunciado;
    private String resposta;
    private float nota = 0;

    public Questao(String enunciado, String resposta) {
        this.enunciado = enunciado;
        this.resposta = resposta;
    }

    public String getEnunciado() { return enunciado; }
    public String getResposta() { return resposta;}
    public float getNota() { return nota; }
    public void setNota(float nota) { this.nota = nota; }
}
