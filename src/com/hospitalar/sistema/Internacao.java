package com.hospitalar.sistema;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Internacao {
    private Paciente paciente;
    private Medico medicoResponsavel;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private String quarto;
    private double custoDiaria;
    private StatusInternacao status;

    public Internacao(Paciente paciente, Medico medicoResponsavel, String quarto, double custoDiaria) {
        this.paciente = paciente;
        this.medicoResponsavel = medicoResponsavel;
        this.quarto = quarto;
        this.custoDiaria = custoDiaria;
        this.dataEntrada = LocalDate.now();
        this.dataSaida = null;
        this.status = StatusInternacao.ATIVA;
    }

    public Internacao(Paciente paciente, Medico medico, LocalDate dataEntrada, LocalDate dataSaida, String quarto, double custoDiaria, StatusInternacao status) {
        this.paciente = paciente;
        this.medicoResponsavel = medico;
        this.quarto = quarto;
        this.custoDiaria = custoDiaria;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.status = status;
    }

    public double registrarAlta() {
        if (this.status == StatusInternacao.ATIVA) {
            this.dataSaida = LocalDate.now();
            this.status = StatusInternacao.FINALIZADA;
            long diasInternado = ChronoUnit.DAYS.between(dataEntrada, dataSaida);
            diasInternado = diasInternado == 0 ? 1 : diasInternado;
            return diasInternado * this.custoDiaria;
        }
        return 0;
    }

    public void cancelar() {
        if (this.status == StatusInternacao.ATIVA)  {
            this.status = StatusInternacao.CANCELADA;
        }
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedicoResponsavel() {
        return medicoResponsavel;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }
    public String getQuarto() {
        return quarto;
    }
    public double getCustoDiaria() {
        return custoDiaria;
    }
    public StatusInternacao getStatus() {
        return status;
    }


    public void exibirInformacoes() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("Paciente: " + paciente.getNome() + " (CPF: " + paciente.getCpf() + ")");
        System.out.println("Médico Resp.: Dr(a). " + medicoResponsavel.getNome());
        System.out.println("Quarto: " + quarto);
        System.out.println("Status: " + status);
        System.out.println("Data de Entrada: " + dataEntrada.format(formatter));
        if (dataSaida != null) {
            System.out.println("Data de Saída: " + dataSaida.format(formatter));
        }else {
            System.out.println("Data de Saída: -");
        }
    }

}