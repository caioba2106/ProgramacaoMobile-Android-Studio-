package com.example.weathernow.model;

public class Alerta {
    private String titulo;
    private String cidade;
    private int chanceChuva;
    private String antecedencia;
    private boolean ativo;

    public Alerta(String titulo, String cidade, int chanceChuva, String antecedencia, boolean ativo) {
        this.titulo = titulo;
        this.cidade = cidade;
        this.chanceChuva = chanceChuva;
        this.antecedencia = antecedencia;
        this.ativo = ativo;
    }

    public String getTitulo() { return titulo; }
    public String getCidade() { return cidade; }
    public int getChanceChuva() { return chanceChuva; }
    public String getAntecedencia() { return antecedencia; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
