package com.giu7.mangeat.datamodels;

public class Restaurant {
    private String nome;
    private String indirizzo;
    private float minOrdine;

    public Restaurant(String nome, String indirizzo, float minOrdine) {
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.minOrdine = minOrdine;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public float getMinOrdine() {
        return minOrdine;
    }

    public void setMinOrdine(float minOrdine) {
        this.minOrdine = minOrdine;
    }
}