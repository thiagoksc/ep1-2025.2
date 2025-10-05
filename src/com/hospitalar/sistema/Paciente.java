package com.hospitalar.sistema;

import java.util.ArrayList;
import java.util.List;

public class Paciente {
    public String getNome;
    private String nome;
    private String cpf;
    private int idade;
    private List<Consulta> historicoConsultas;
    private List<Internacao> historicoInternacoes;

    public Paciente(String nome, String cpf, int idade){
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.historicoConsultas = new ArrayList<>();
        this.historicoInternacoes = new ArrayList<>();
    }
    public String getNome() {
        return nome;
    }
    public String getCpf(){
        return cpf;
    }
    public int getIdade(){
        return idade;
    }
    public void exibirInformacoes(){
        System.out.println("Nome: " + nome);
        System.out.println("CPF: " + cpf);
        System.out.println("Idade: " + idade);
    }

    public void adicionarConsulta(Consulta consulta){
        this.historicoConsultas.add(consulta);
    }
}