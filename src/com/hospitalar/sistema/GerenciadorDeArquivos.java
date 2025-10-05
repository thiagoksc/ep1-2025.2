package com.hospitalar.sistema;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeArquivos {

    private static final String ARQUIVO_PACIENTES = "pacientes.csv";

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
                int idade  = Integer.parseInt(dados[2]);
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
}
