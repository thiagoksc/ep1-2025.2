package com.hospitalar.sistema;

public class PacienteEspecial extends Paciente {
    private PlanoDeSaude planoDeSaude;

    public PacienteEspecial(String nome, String cpf, int idade, PlanoDeSaude planoDeSaude) {
        super(nome, cpf, idade);
        this.planoDeSaude = planoDeSaude;
    }

    public PlanoDeSaude getPlanoDeSaude() {
        return planoDeSaude;
    }

    @Override
    public void exibirInformacoes() {
        super.exibirInformacoes();
        System.out.println("Plano de Saude: " + planoDeSaude.getNome());
    }

}