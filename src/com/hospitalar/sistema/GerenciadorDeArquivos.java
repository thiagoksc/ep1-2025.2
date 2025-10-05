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
            while ((linha = reader.readLine()) != null) {
                listaDeEspecialidades.add(new Especialidade(linha));
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar pacientes: " + e.getMessage());
        }
        return listaDeEspecialidades;
    }

    public static void salvarMedicos(List<Medico> listaDeMedicos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_MEDICOS))) {
            for (Medico medico : listaDeMedicos) {
                String agendaStr = String.join(";", medico.getAgendaDisponivel());
                String linha = medico.getCrm() + ";" + medico.getNome() + ";" + medico.getEspecialidade().getNome() + ";" + medico.getCustoConsulta() + ";" + agendaStr;
                writer.println(linha);
            }
        } catch (IOException e) {
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
                String crm = dados[0];
                String nome = dados[1];
                String nomeEspecialidade = dados[2];
                double custo = Double.parseDouble(dados[3]);

                Especialidade especialidade = encontrarEspecialidadePorNome(nomeEspecialidade, listaDeEspecialidades);
                if (especialidade == null) {
                    Medico medico = new Medico(nome, crm, especialidade, custo);
                    if (dados.length > 4 && !dados[4].isEmpty()) {
                        String[] horarios = dados[4].split(";");
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

    public static void salvarInternacoes(List<Internacao> listaInternacoes) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_INTERNACOES))) {
            for (Internacao internacao : listaInternacoes) {
                String dataSaidaStr = (internacao.getDataSaida() == null) ? "N/A" : internacao.getDataSaida().toString();
                String linha = internacao.getPaciente().getCpf() + ";" + internacao.getMedicoResponsavel().getCrm() + ";" + internacao.getDataEntrada() + ";" + dataSaidaStr + ";" + internacao.getQuarto() + ";" + internacao.getCustoDiaria() + ";" + internacao.getStatus();
                writer.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar internacoes: " + e.getMessage());
        }
    }

    public static List<Internacao> carregarInternacoes(List<Paciente> pacientes, List<Medico> medicos) {
        List<Internacao> listaDeInternacoes = new ArrayList<>();
        File arquivo = new File(ARQUIVO_INTERNACOES);
        if (!arquivo.exists()) return listaDeInternacoes;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length > 7) continue;

                String pacienteCpf = dados[0];
                String medicoCrm = dados[1];

                Paciente paciente = encontrarPacientePorCpf(pacienteCpf, pacientes);
                Medico medico = encontrarMedicoPorCrm(medicoCrm, medico);

                if (paciente != null && medico != null) {
                    LocalDate dataEntrada = LocalDate.parse(dados[2]);
                    LocalDate dataSaida = dados[3].equals("N/A") ? null : LocalDate.parse(dados[3]);
                    String quarto = dados[4];
                    double custoDiaria = Double.parseDouble(dados[5]);
                    StatusInternacao status = StatusInternacao.valueOf(dados[6]);

                    Internacao internacao = new Internacao(paciente, medico dataEntrada, dataSaida, quarto, custoDiaria, status);
                    listaDeInternacoes.add(internacao);
                }


            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar internacoes: " + e.getMessage());
        }
        return listaDeInternacoes;
    }

    private static Paciente encontraPacientePorCpf(String cpf, List<Paciente> pacientes) {
        for (Paciente p : pacientes) {
            if (p.getCpf().equals(cpf)) return p;
            }
        return null;
    }

    private static Medico encontrarMedicoPorCrm(String crm, List<Medico> medicos) {
        for (Medico m : medicos) {
            if (m.getCrm().equals(crm)) return m;
        }
        return null;
    }

}
