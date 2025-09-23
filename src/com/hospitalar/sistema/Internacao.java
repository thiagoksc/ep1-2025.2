package com.hospitalar.sistema;

import java.time.LocalDate;

public class Internacao {
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private String motivo;

    public Internacao(LocalDate dataEntrada, String motivo) {
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.motivo = null;
    }
    public void setDataSaida(LocalDate dataSaida){
        this.dataSaida = dataSaida;
    }
    @Override
    public String toString(){
        return "Internacao [Entrada =" + dataEntrada + ", Saida=" + (dataSaida == null ? "Pendente" : dataSaida) + ", Motivo=" + motivo + "]";
    }
}