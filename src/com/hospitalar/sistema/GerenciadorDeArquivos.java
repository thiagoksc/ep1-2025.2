package com.hospitalar.sistema;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class GerenciadorDeArquivos {

    private static final String ARQUIVO_PACIENTES = "pacientes.csv";
    private static final String ARQUIVO_ESPECIALIDADES = "especialidades.csv";
    private static final String ARQUIVO_MEDICOS = "medicos.csv";
    private static final String ARQUIVO_INTERNACOES = "internacoes.csv";


    public static void salvarPacientes(List<Paciente> listaDePacientes) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PACIENTES))) {


            for (Paciente paciente : listaDePacientes) {


                String linha = paciente.getCpf() + ";" + paciente.getNome() + ";" + paciente.getIdade();

                if (paciente instanceof PacienteEspecial) {

                    PacienteEspecial pe = (PacienteEspecial) paciente;
                    linha += ";S;" + pe.getPlanoDeSaude();

                } else {
                    linha += ";N;N;/A";
                }
                writer.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar pacientes: " + e.getMessage());
        }
    }

    public static List<Paciente> carregarPacientes() {

        List<Paciente> listaDePacientes = new ArrayList<>();
        File arquivo = new File(ARQUIVO_PACIENTES);

        if (!arquivo.exists()) {
            return listaDePacientes;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {

            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");

                String cpf = dados[0];
                String nome = dados[1];
                int idade = Integer.parseInt(dados[2]);
                boolean temPlano = dados[3].equalsIgnoreCase("S");

                if (temPlano) {
                    String planoDeSaude = dados[4];
                    listaDePacientes.add(new PacienteEspecial(cpf, nome, idade, planoDeSaude));

                } else {
                    listaDePacientes.add(new Paciente(cpf, nome, idade));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar pacientes: " + e.getMessage());
        }
        return listaDePacientes;
    }

    public static void salvarEspecialidade(List<Especialidade> listaDeEspecialidades) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_ESPECIALIDADES))) {
            for (Especialidade especialidade : listaDeEspecialidades) {
                writer.println(especialidade.getNome());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar pacientes: " + e.getMessage());
        }
    }


    public static List<Especialidade> carregarEspecialidades() {
        List<Especialidade> listaDeEspecialidades = new ArrayList<>();
        File arquivo = new File(ARQUIVO_ESPECIALIDADES);
        if (!arquivo.exists()) {
            return listaDeEspecialidades;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null){
                listaDeEspecialidades.add(new Especialidade(linha));
            }
        }catch (IOException e){
            System.out.println("Erro ao carregar pacientes: " + e.getMessage());
        }
        return listaDeEspecialidades;
    }

    public static void salvarMedicos(List<Medico> listaDeMedicos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_MEDICOS))) {
            for (Medico medico : listaDeMedicos) {
                String agendaStr = String.join(";", medico.getAgendaDisponivel());
                String linha  = medico.getCrm() + ";" + medico.getNome() + ";" + medico.getEspecialidade().getNome() + ";" + medico.getCustoConsulta() + ";" + agendaStr;
                writer.println(linha);
            }
        } catch (IOException e){
            System.out.println("Erro ao salvar médicos: " + e.getMessage());
        }
    }

    public static List<Medico> carregarMedicos(List<Especialidade> listaDeEspecialidades) {
        List<Medico> listaDeMedicos = new ArrayList<>();
        File arquivo = new File(ARQUIVO_MEDICOS);
        if (!arquivo.exists()) {
            return listaDeMedicos;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                String crm =  dados[0];
                String nome =  dados[1];
                String nomeEspecialidade = dados[2];
                double custo =  Double.parseDouble(dados[3]);

                Especialidade especialidade = encontrarEspecialidadePorNome(nomeEspecialidade, listaDeEspecialidades);
                if (especialidade == null) {
                    Medico medico = new Medico (nome, crm, especialidade, custo);
                    if (dados.length > 4 && !dados[4].isEmpty()){
                        String[] horarios  = dados[4].split(";");
                        for (String horario : horarios) {
                            medico.adicionarHorario(horario);
                        }
                    }
                    listaDeMedicos.add(medico);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar medicos: " + e.getMessage());
        }
        return listaDeMedicos;
    }

    private static Especialidade encontrarEspecialidadePorNome(String nome, List<Especialidade> listaDeEspecialidades) {
        for (Especialidade e : listaDeEspecialidades) {
            if (e.getNome().equalsIgnoreCase(nome)) {
                return e;
            }
        }
        return null;
    }
}
