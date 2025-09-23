package com.hospitalar.sistema;

public class PacienteEspecial extends Paciente {
    private String planoDeSaude;

    public PacienteEspecial(String nome, String cpf, int idade, String planoDeSaude) {
        super(nome, cpf, idade);
        this.planoDeSaude = planoDeSaude;
    }
    public String getPlanoDeSaude() {
        return planoDeSaude;
    }
    @Override
    public void exibirInformacoes(){
        super.exibirInformacoes();
        System.out.println("Plano de saude: " + planoDeSaude);
    }
}