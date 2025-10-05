package com.hospitalar.sistema;

import java.util.ArrayList;
import java.util.List;

public class Medico {
    private String nome;
    private String crm;
    private Especialidade especialidade;
    private double custoConsulta;
    private List<String> agendaDisponivel;

    public  Medico(String nome, String crm, Especialidade especialidade, double custoConsulta) {
        this.nome = nome;
        this.crm = crm;
        this.especialidade = especialidade;
        this.custoConsulta = custoConsulta;
        this.agendaDisponivel = new ArrayList<>();
    }

    public void adicionarHorario(String horario) {
        this.agendaDisponivel.add(horario);
    }

    public void exibirInformacoes(){
        System.out.println("Nome: Dr(a). " + nome);
        System.out.println("CRM: " + crm);
        System.out.println("Especialidade: " + especialidade);
        System.out.println("Custo da Consulta: R$ " + String.format("%.2f", custoConsulta));
        System.out.println("Hórario Dispónivel: " +  agendaDisponivel);

    }

    public String getNome() {
        return nome;
    }

    public String getCrm() {
        return crm;
    }
    public double getCustoConsulta() {
        return custoConsulta;
    }
    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public List<String> getAgendaDisponivel() {
        return agendaDisponivel;
    }
}
