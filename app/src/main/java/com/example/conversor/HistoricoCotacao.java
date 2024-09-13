package com.example.conversor;

public class HistoricoCotacao {
    private int id;
    private String valor;
    private String moeda_origem;
    private String moeda_final;
    private String cotacao;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getMoedaOrigem() {
        return moeda_origem;
    }

    public void setMoedaOrigem(String moeda_origem) {
        this.moeda_origem = moeda_origem;
    }

    public String getMoedaFinal() {
        return moeda_final;
    }

    public void setMoedaFinal(String moeda_final) {
        this.moeda_final = moeda_final;
    }

    public String getCotacao() {
        return cotacao;
    }

    public void setCotacao(String cotacao) {
        this.cotacao = cotacao;
    }

}
