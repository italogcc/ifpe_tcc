package com.sees.projeto.model;

public class Fatura {
    private int mesReferencia;
    private int anoReferencia;
    private float valorFatura;
    private float consumokWh;

    public Fatura() {}

    public Fatura(int mesReferencia, int anoReferencia, float valorFatura, float consumokWh) {
        this.mesReferencia = mesReferencia;
        this.anoReferencia = anoReferencia;
        this.valorFatura = valorFatura;
        this.consumokWh = consumokWh;
    }

    public int getMesReferencia() { return mesReferencia; }
    public void setMesReferencia(int mesReferencia) { this.mesReferencia = mesReferencia; }

    public int getAnoReferencia() { return anoReferencia; }
    public void setAnoReferencia(int anoReferencia) { this.anoReferencia = anoReferencia; }

    public float getValorFatura() { return valorFatura; }
    public void setValorFatura(float valorFatura) { this.valorFatura = valorFatura; }

    public float getConsumokWh() { return consumokWh; }
    public void setConsumokWh(float consumokWh) { this.consumokWh = consumokWh; }
}
