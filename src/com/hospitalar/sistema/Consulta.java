package com.hospitalar.sistema;

import java.time.LocalDate;

public class Consulta {
    private LocalDate data;
    private String medico;
    private String motivo;

    public Consulta(LocalDate data, String medico, String motivo) {
        this.data = data;
        this.medico = medico;
        this.motivo = motivo;
    }


    @Override
    public String toString() {
        return "Conulta [Data = " + data + ", Medico = " + medico + ", Motivo = " + motivo + "]";

    }
}