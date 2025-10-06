package com.hospitalar.sistema;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GerenciadorDeArquivos {

    private static final String ARQUIVO_PACIENTES = "pacientes.csv";
    private static final String ARQUIVO_ESPECIALIDADES = "especialidades.csv";
    private static final String ARQUIVO_MEDICOS = "medicos.csv";
    private static final String ARQUIVO_INTERNACOES = "internacoes.csv";
    private static final String ARQUIVO_PLANOS = "planos.csv";

    // --- MÉTODOS PARA PLANOS DE SAÚDE ---
    public static void salvarPlanos(List<PlanoDeSaude> listaDePlanos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PLANOS))) {
            for (PlanoDeSaude plano : listaDePlanos) {
                StringBuilder descontosStr = new StringBuilder();
                for (Map.Entry<String, Double> entry : plano.getDescontos().entrySet()) {
                    descontosStr.append(entry.getKey()).append(":").append(entry.getValue()).append("|");
                }
                String linha = plano.getNome() + ";" + plano.isCobreInternacaoCurta() + ";" + descontosStr.toString();
                writer.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar planos de saúde: " + e.getMessage());
        }
    }

    public static List<PlanoDeSaude> carregarPlanos() {
        List<PlanoDeSaude> listaDePlanos = new ArrayList<>();
        File arquivo = new File(ARQUIVO_PLANOS);
        if (!arquivo.exists()) return listaDePlanos;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";", -1);
                String nome = dados[0];
                boolean cobreInternacao = Boolean.parseBoolean(dados[1]);
                PlanoDeSaude plano = new PlanoDeSaude(nome, cobreInternacao);
                if (dados.length > 2 && !dados[2].isEmpty()) {
                    String[] descontos = dados[2].split("\\|");
                    for (String desconto : descontos) {
                        if (desconto.contains(":")) {
                            String[] par = desconto.split(":");
                            plano.adicionarOuAtualizarDesconto(par[0], Double.parseDouble(par[1]));
                        }
                    }
                }
                listaDePlanos.add(plano);
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar planos de saúde: " + e.getMessage());
        }
        return listaDePlanos;
    }

    // --- MÉTODOS DE PACIENTES ---
    public static void salvarPacientes(List<Paciente> listaDePacientes) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PACIENTES))) {
            for (Paciente paciente : listaDePacientes) {
                String linha = paciente.getCpf() + ";" + paciente.getNome() + ";" + paciente.getIdade();
                if (paciente instanceof PacienteEspecial) {
                    PacienteEspecial pe = (PacienteEspecial) paciente;
                    linha += ";S;" + pe.getPlanoDeSaude().getNome();
                } else {
                    linha += ";N;N/A";
                }
                writer.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar pacientes: " + e.getMessage());
        }
    }

    // AQUI ESTÁ A ASSINATURA CORRETA DO MÉTODO
    public static List<Paciente> carregarPacientes(List<PlanoDeSaude> planos) {
        List<Paciente> listaDePacientes = new ArrayList<>();
        File arquivo = new File(ARQUIVO_PACIENTES);
        if (!arquivo.exists()) return listaDePacientes;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length < 4) continue;
                String cpf = dados[0];
                String nome = dados[1];
                int idade = Integer.parseInt(dados[2]);
                boolean temPlano = dados[3].equalsIgnoreCase("S");

                if (temPlano && dados.length > 4) {
                    String nomePlano = dados[4];
                    PlanoDeSaude plano = encontrarPlanoPorNome(nomePlano, planos);
                    if (plano != null) {
                        listaDePacientes.add(new PacienteEspecial(nome, cpf, idade, plano));
                    }
                } else {
                    listaDePacientes.add(new Paciente(nome, cpf, idade));
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar pacientes: " + e.getMessage());
        }
        return listaDePacientes;
    }

    // (O resto dos métodos, como salvarMedicos, etc., já estão aqui)
    // --- MÉTODOS DE ESPECIALIDADES ---
    public static void salvarEspecialidades(List<Especialidade> listaDeEspecialidades) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_ESPECIALIDADES))) {
            for (Especialidade especialidade : listaDeEspecialidades) {
                writer.println(especialidade.getNome());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar especialidades: " + e.getMessage());
        }
    }

    public static List<Especialidade> carregarEspecialidades() {
        List<Especialidade> listaDeEspecialidades = new ArrayList<>();
        File arquivo = new File(ARQUIVO_ESPECIALIDADES);
        if (!arquivo.exists()) { return listaDeEspecialidades; }
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    listaDeEspecialidades.add(new Especialidade(linha));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar especialidades: " + e.getMessage());
        }
        return listaDeEspecialidades;
    }

    // --- MÉTODOS DE MÉDICOS ---
    public static void salvarMedicos(List<Medico> listaDeMedicos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_MEDICOS))) {
            for (Medico medico : listaDeMedicos) {
                String agendaStr = String.join(",", medico.getAgendaDisponivel());
                String linha = medico.getCrm() + ";" +
                        medico.getNome() + ";" +
                        medico.getEspecialidade().getNome() + ";" +
                        medico.getCustoConsulta() + ";" +
                        agendaStr;
                writer.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar médicos: " + e.getMessage());
        }
    }

    public static List<Medico> carregarMedicos(List<Especialidade> listaDeEspecialidades) {
        List<Medico> listaDeMedicos = new ArrayList<>();
        File arquivo = new File(ARQUIVO_MEDICOS);
        if (!arquivo.exists()) { return listaDeMedicos; }
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length < 4) continue;
                String crm = dados[0];
                String nome = dados[1];
                String nomeEspecialidade = dados[2];
                double custo = Double.parseDouble(dados[3]);

                Especialidade especialidade = encontrarEspecialidadePorNome(nomeEspecialidade, listaDeEspecialidades);
                if (especialidade != null) {
                    Medico medico = new Medico(nome, crm, especialidade, custo);
                    if (dados.length > 4 && !dados[4].isEmpty()) {
                        String[] horarios = dados[4].split(",");
                        for (String horario : horarios) {
                            medico.adicionarHorario(horario);
                        }
                    }
                    listaDeMedicos.add(medico);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar médicos: " + e.getMessage());
        }
        return listaDeMedicos;
    }

    // --- MÉTODOS DE INTERNAÇÕES ---
    public static void salvarInternacoes(List<Internacao> listaDeInternacoes) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_INTERNACOES))) {
            for (Internacao internacao : listaDeInternacoes) {
                String dataSaidaStr = (internacao.getDataSaida() == null) ? "N/A" : internacao.getDataSaida().toString();
                String linha = internacao.getPaciente().getCpf() + ";" +
                        internacao.getMedicoResponsavel().getCrm() + ";" +
                        internacao.getDataEntrada() + ";" +
                        dataSaidaStr + ";" +
                        internacao.getQuarto() + ";" +
                        internacao.getCustoDiaria() + ";" +
                        internacao.getStatus();
                writer.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar internações: " + e.getMessage());
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
                if (dados.length < 7) continue;
                String pacienteCpf = dados[0];
                String medicoCrm = dados[1];

                Paciente paciente = encontrarPacientePorCpf(pacienteCpf, pacientes);
                Medico medico = encontrarMedicoPorCrm(medicoCrm, medicos);

                if (paciente != null && medico != null) {
                    LocalDate dataEntrada = LocalDate.parse(dados[2]);
                    LocalDate dataSaida = dados[3].equals("N/A") ? null : LocalDate.parse(dados[3]);
                    String quarto = dados[4];
                    double custoDiaria = Double.parseDouble(dados[5]);
                    StatusInternacao status = StatusInternacao.valueOf(dados[6]);
                    Internacao internacao = new Internacao(paciente, medico, dataEntrada, dataSaida, quarto, custoDiaria, status);
                    listaDeInternacoes.add(internacao);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar internações: " + e.getMessage());
        }
        return listaDeInternacoes;
    }

    // --- MÉTODOS AUXILIARES ---
    private static Especialidade encontrarEspecialidadePorNome(String nome, List<Especialidade> especialidades) {
        for (Especialidade e : especialidades) {
            if (e.getNome().equalsIgnoreCase(nome)) {
                return e;
            }
        }
        return null;
    }

    private static Paciente encontrarPacientePorCpf(String cpf, List<Paciente> pacientes) {
        for (Paciente p : pacientes) {
            if (p.getCpf().equals(cpf)) {
                return p;
            }
        }
        return null;
    }

    private static Medico encontrarMedicoPorCrm(String crm, List<Medico> medicos) {
        for (Medico m : medicos) {
            if (m.getCrm().equals(crm)) {
                return m;
            }
        }
        return null;
    }

    private static PlanoDeSaude encontrarPlanoPorNome(String nome, List<PlanoDeSaude> planos) {
        for (PlanoDeSaude p : planos) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }
}