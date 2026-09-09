package com.leosoares.projetoandroid;

public class Entrevista {
    private String nomeVaga;
    private String status;
    private int totalCandidatos;

    public Entrevista(String nomeVaga, String status, int totalCandidatos){
        this.nomeVaga = nomeVaga;
        this.status = status;
        this.totalCandidatos = totalCandidatos;
    }

    public String getNomeVaga() { return nomeVaga; }
    public String getStatus() { return status; }
    public int getTotalCandidatos() { return totalCandidatos; }
}
