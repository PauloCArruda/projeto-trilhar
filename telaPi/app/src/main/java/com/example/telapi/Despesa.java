package com.example.telapi;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Despesa implements Serializable {

    Long id;
    String descricao;
    Date vencimento;
    double valor;
    boolean pago;

    public Despesa(Long id, String descricao, double valor, Date vencimento, boolean pago) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.vencimento = vencimento;
        this.pago = pago;
    }

    public Despesa() {

    }
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;

    }
    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataFormatada = sdf.format(this.vencimento);
        return id + " - " + descricao + "\n" +
                "Valor: R$" + valor + "\n" +
                "Vencimento: " + dataFormatada;
    }
}
