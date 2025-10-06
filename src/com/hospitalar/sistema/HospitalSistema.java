package com.hospitalar.sistema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HospitalSistema {


    private static List<Paciente> listaDePacientes;
    private static List<Medico> listaDeMedicos;
    private static List<Especialidade> listaDeEspecialidades;
    private static List<PlanoDeSaude> listaDePlanos;
    private static List<Consulta> listaDeConsultas = new ArrayList<>();
    private static List<Internacao> listaDeInternacoes;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao Sistema de Gerenciamento Hospitalar!");
        carregarDados();
        Menu.executar();
    }
    private static void carregarDados() {
        listaDePlanos = GerenciadorDeArquivos.carregarPlanos();
        listaDeEspecialidades = GerenciadorDeArquivos.carregarEspecialidades();
        listaDePacientes = GerenciadorDeArquivos.carregarPacientes(listaDePlanos);
        listaDeMedicos = GerenciadorDeArquivos.carregarMedicos(listaDeEspecialidades);
        listaDeInternacoes = GerenciadorDeArquivos.carregarInternacoes(listaDePacientes, listaDeMedicos);

        if (listaDeEspecialidades.isEmpty()) {
            System.out.println("INFO: Nenhuma especialidade encontrada. Criando lista padrão...");
            listaDeEspecialidades.add(new Especialidade("Cardiologia"));
            listaDeEspecialidades.add(new Especialidade("Pediatria"));
            listaDeEspecialidades.add(new Especialidade("Ortopedia"));
            GerenciadorDeArquivos.salvarEspecialidades(listaDeEspecialidades);
        }
    }


    public static void excluirMedico() {
        System.out.println("\n--- Excluir Médico ---");
        if (listaDeMedicos.isEmpty()) {
            System.out.println("Não há médicos cadastrados para excluir.");
            return;
        }

        System.out.println("Selecione o médico que deseja excluir:");
        for (int i = 0; i < listaDeMedicos.size(); i++) {
            System.out.println((i + 1) + ". " + listaDeMedicos.get(i).getNome() + " (CRM: " + listaDeMedicos.get(i).getCrm() + ")");
        }

        System.out.print("\nDigite o número do médico (ou 0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) {
            System.out.println("Operação cancelada.");
            return;
        }

        if (escolha > 0 && escolha <= listaDeMedicos.size()) {
            int indiceParaExcluir = escolha - 1;
            Medico medicoParaExcluir = listaDeMedicos.get(indiceParaExcluir);

            boolean medicoTemVinculo = false;

            for (Consulta consulta : listaDeConsultas) {
                if (consulta.getMedico().equals(medicoParaExcluir)) {
                    medicoTemVinculo = true;
                    break;
                }
            }

            if (!medicoTemVinculo) {
                for (Internacao internacao : listaDeInternacoes) {
                    if (internacao.getMedicoResponsavel().equals(medicoParaExcluir)) {
                        medicoTemVinculo = true;
                        break;
                    }
                }
            }

            if (medicoTemVinculo) {
                System.out.println("\nERRO: Não é possível excluir o Dr(a). " + medicoParaExcluir.getNome() +
                        " pois ele(a) está vinculado(a) a consultas ou internações existentes.");
            } else {
                listaDeMedicos.remove(indiceParaExcluir);
                GerenciadorDeArquivos.salvarMedicos(listaDeMedicos);
                System.out.println("Médico '" + medicoParaExcluir.getNome() + "' foi excluído com sucesso!");
            }
        } else {
            System.out.println("Opção inválida. Operação cancelada.");
        }
    }

    public static void gerarRelatorioMedicosDetalhado() {
        System.out.println("\n--- Relatório Detalhado de Médicos ---");
        if (listaDeMedicos.isEmpty()) {
            System.out.println("Nenhum médico cadastrado.");
            return;
        }
        for (Medico medico : listaDeMedicos) {
            medico.exibirInformacoes();


            int contadorConsultas = 0;
            for (Consulta consulta : listaDeConsultas) {
                if (consulta.getMedico().equals(medico)) {
                    contadorConsultas++;
                }
            }
            System.out.println("Número de Consultas Realizadas: " + contadorConsultas);
            System.out.println("==========================================");
        }
    }

    public static void gerarRelatorioEstatisticasGerais() {
        System.out.println("\n--- Relatório de Estatísticas Gerais ---");
        if (listaDeConsultas.isEmpty()) {
            System.out.println("Nenhuma consulta foi realizada para gerar estatísticas.");
            return;
        }


        Map<String, Integer> contagemMedicos = new HashMap<>();
        Map<String, Integer> contagemEspecialidades = new HashMap<>();

        for (Consulta consulta : listaDeConsultas) {
            String nomeMedico = consulta.getMedico().getNome();
            String nomeEspecialidade = consulta.getMedico().getEspecialidade().getNome();


            contagemMedicos.put(nomeMedico, contagemMedicos.getOrDefault(nomeMedico, 0) + 1);
            contagemEspecialidades.put(nomeEspecialidade, contagemEspecialidades.getOrDefault(nomeEspecialidade, 0) + 1);
        }


        String medicoMaisAtivo = "";
        int maxConsultasMedico = 0;
        for (Map.Entry<String, Integer> entry : contagemMedicos.entrySet()) {
            if (entry.getValue() > maxConsultasMedico) {
                maxConsultasMedico = entry.getValue();
                medicoMaisAtivo = entry.getKey();
            }
        }
        System.out.println("Médico que mais atendeu: Dr(a). " + medicoMaisAtivo + " (" + maxConsultasMedico + " consultas)");


        String especialidadeMaisProcurada = "";
        int maxConsultasEspecialidade = 0;
        for (Map.Entry<String, Integer> entry : contagemEspecialidades.entrySet()) {
            if (entry.getValue() > maxConsultasEspecialidade) {
                maxConsultasEspecialidade = entry.getValue();
                especialidadeMaisProcurada = entry.getKey();
            }
        }
        System.out.println("Especialidade mais procurada: " + especialidadeMaisProcurada + " (" + maxConsultasEspecialidade + " consultas)");
        System.out.println("==========================================");
    }

    public static void gerarRelatorioPacientesComHistorico() {
        System.out.println("\n--- Relatório Detalhado de Pacientes ---");
        if (listaDePacientes.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado.");
            return;
        }
        for (Paciente paciente : listaDePacientes) {
            paciente.exibirInformacoes();
            if (paciente.getHistoricoConsultas().isEmpty()) {
                System.out.println("  -> Sem histórico de consultas.");
            } else {
                System.out.println("  --- Histórico de Consultas ---");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                for (Consulta consulta : paciente.getHistoricoConsultas()) {
                    System.out.println("    - Data: " + consulta.getDataHora().format(formatter) +
                            " | Dr(a). " + consulta.getMedico().getNome() +
                            " | Status: " + consulta.getStatus());
                }
            }
            if (paciente.getHistoricoInternacoes().isEmpty()) {
                System.out.println("  -> Sem histórico de internações.");
            } else {
                System.out.println("  --- Histórico de Internações ---");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                for (Internacao internacao : paciente.getHistoricoInternacoes()) {
                    System.out.println("    - Entrada: " + internacao.getDataEntrada().format(formatter) +
                            " | Quarto: " + internacao.getQuarto() +
                            " | Status: " + internacao.getStatus());
                }
            }
            System.out.println("==========================================");
        }
    }
    public static void gerarRelatorioInternacoesAtivas() {
        System.out.println("\n--- Relatório de Pacientes Internados Atualmente ---");
        boolean encontrouAtiva = false;

        for (Internacao internacao : listaDeInternacoes) {
            if (internacao.getStatus() == StatusInternacao.ATIVA) {
                if (!encontrouAtiva) {
                    encontrouAtiva = true;
                }
                internacao.exibirInformacoes();


                long diasInternado = ChronoUnit.DAYS.between(internacao.getDataEntrada(), LocalDate.now());
                System.out.println("Tempo de Internação (até agora): " + diasInternado + " dias.");
                System.out.println("--------------------");
            }
        }

        if (!encontrouAtiva) {
            System.out.println("Nenhum paciente internado no momento.");
        }
    }

    public static void cadastrarNovoPaciente() {
        System.out.println("\n--- Cadastro de Novo Paciente ---");
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        for (Paciente p : listaDePacientes) {
            if (p.getCpf().equals(cpf)) {
                System.out.println("\nERRO: Já existe um paciente com este CPF.");
                return;
            }
        }
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine();
        System.out.print("O paciente tem plano de saúde? (S/N): ");
        String temPlano = scanner.nextLine();

        if (temPlano.equalsIgnoreCase("S")) {
            if (listaDePlanos.isEmpty()) {
                System.out.println("ERRO: Nenhum plano de saúde cadastrado. Cadastre um plano primeiro (opção 6).");
                return;
            }
            System.out.println("Selecione o plano de saúde:");
            for (int i = 0; i < listaDePlanos.size(); i++) {
                System.out.println((i + 1) + ". " + listaDePlanos.get(i).getNome());
            }
            System.out.print("Opção: ");
            int escolhaPlano = scanner.nextInt() - 1;
            scanner.nextLine();
            if (escolhaPlano >= 0 && escolhaPlano < listaDePlanos.size()) {
                PlanoDeSaude planoEscolhido = listaDePlanos.get(escolhaPlano);
                PacienteEspecial novoPaciente = new PacienteEspecial(nome, cpf, idade, planoEscolhido);
                listaDePacientes.add(novoPaciente);
                System.out.println("Paciente ESPECIAL cadastrado com sucesso!");
            } else {
                System.out.println("Opção de plano inválida. Cadastro cancelado.");
                return;
            }
        } else {
            Paciente novoPaciente = new Paciente(nome, cpf, idade);
            listaDePacientes.add(novoPaciente);
            System.out.println("Paciente COMUM cadastrado com sucesso!");
        }
        GerenciadorDeArquivos.salvarPacientes(listaDePacientes);
    }

    public static void listarPacientes() {
        System.out.println("\n--- Lista de Pacientes Cadastrados ---");
        if (listaDePacientes.isEmpty()) {
            System.out.println("Nenhum paciente cadastrado ainda.");
            return;
        }
        for (int i = 0; i < listaDePacientes.size(); i++) {
            System.out.println("\n--- Paciente " + (i + 1) + " ---");
            listaDePacientes.get(i).exibirInformacoes();
        }
    }

    public static void excluirPaciente() {
        System.out.println("\n--- Excluir Paciente ---");
        if (listaDePacientes.isEmpty()) {
            System.out.println("Não há pacientes cadastrados para excluir.");
            return;
        }
        System.out.println("Selecione o paciente que deseja excluir:");
        for (int i = 0; i < listaDePacientes.size(); i++) {
            System.out.println((i + 1) + ". " + listaDePacientes.get(i).getNome() + " (CPF: " + listaDePacientes.get(i).getCpf() + ")");
        }
        System.out.print("\nDigite o número do paciente (ou 0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();
        if (escolha == 0) {
            System.out.println("Operação cancelada.");
            return;
        }
        if (escolha > 0 && escolha <= listaDePacientes.size()) {
            int indiceParaExcluir = escolha - 1;
            String nomeExcluido = listaDePacientes.get(indiceParaExcluir).getNome();
            listaDePacientes.remove(indiceParaExcluir);
            GerenciadorDeArquivos.salvarPacientes(listaDePacientes);
            System.out.println("Paciente '" + nomeExcluido + "' foi excluído com sucesso!");
        } else {
            System.out.println("Opção inválida. Operação cancelada.");
        }
    }

    public static void cadastrarNovoMedico() {
        System.out.println("\n--- Cadastro de Novo Médico ---");
        System.out.print("Nome do Médico: ");
        String nome = scanner.nextLine();
        System.out.print("CRM: ");
        String crm = scanner.nextLine();
        System.out.print("Custo da Consulta: R$ ");
        double custo = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("\nSelecione a Especialidade:");
        for (int i = 0; i < listaDeEspecialidades.size(); i++) {
            System.out.println((i + 1) + ". " + listaDeEspecialidades.get(i));
        }
        System.out.println((listaDeEspecialidades.size() + 1) + ". Outra (Cadastrar nova especialidade)");
        System.out.print("Digite o número da opção: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();
        Especialidade especialidadeEscolhida = null;
        if (escolha > 0 && escolha <= listaDeEspecialidades.size()) {
            especialidadeEscolhida = listaDeEspecialidades.get(escolha - 1);
        } else if (escolha == listaDeEspecialidades.size() + 1) {
            System.out.print("Digite o nome da nova especialidade: ");
            String nomeNovaEspecialidade = scanner.nextLine();
            especialidadeEscolhida = new Especialidade(nomeNovaEspecialidade);
            listaDeEspecialidades.add(especialidadeEscolhida);
            GerenciadorDeArquivos.salvarEspecialidades(listaDeEspecialidades);
            System.out.println("Nova especialidade '" + nomeNovaEspecialidade + "' cadastrada!");
        }
        if (especialidadeEscolhida != null) {
            Medico novoMedico = new Medico(nome, crm, especialidadeEscolhida, custo);
            System.out.print("Deseja adicionar horários para este médico agora? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                System.out.println("Digite os horários disponíveis (ex: 14:00). Digite 'fim' para parar.");
                String horario = "";
                while (!horario.equalsIgnoreCase("fim")) {
                    System.out.print("Horário: ");
                    horario = scanner.nextLine();
                    if (!horario.equalsIgnoreCase("fim")) {
                        novoMedico.adicionarHorario(horario);
                    }
                }
            }
            listaDeMedicos.add(novoMedico);
            GerenciadorDeArquivos.salvarMedicos(listaDeMedicos);
            System.out.println("Médico cadastrado com sucesso!");
        } else {
            System.out.println("Opção de especialidade inválida. O cadastro foi cancelado.");
        }
    }

    public  static void listarMedicos() {
        System.out.println("\n--- Lista de Médicos Cadastrados ---");
        if (listaDeMedicos.isEmpty()) {
            System.out.println("Nenhum médico cadastrado ainda.");
            return;
        }
        for (int i = 0; i < listaDeMedicos.size(); i++) {
            System.out.println("\n--- Médico " + (i + 1) + " ---");
            listaDeMedicos.get(i).exibirInformacoes();
        }
    }

    public static void cadastrarPlanoDeSaude() {
        System.out.println("\n--- Cadastrar Novo Plano de Saúde ---");
        System.out.print("Nome do Plano: ");
        String nome = scanner.nextLine();
        for (PlanoDeSaude p : listaDePlanos) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                System.out.println("ERRO: Um plano com este nome já existe.");
                return;
            }
        }
        System.out.print("Este plano oferece gratuidade para internações de curta duração (< 7 dias)? (S/N): ");
        boolean cobertura = scanner.nextLine().equalsIgnoreCase("S");
        PlanoDeSaude novoPlano = new PlanoDeSaude(nome, cobertura);
        listaDePlanos.add(novoPlano);
        GerenciadorDeArquivos.salvarPlanos(listaDePlanos);
        System.out.println("Plano de saúde '" + nome + "' cadastrado com sucesso!");
    }

    public static void configurarDescontosPlano() {
        System.out.println("\n--- Configurar Descontos por Especialidade ---");
        if (listaDePlanos.isEmpty() || listaDeEspecialidades.isEmpty()) {
            System.out.println("ERRO: É preciso ter ao menos um plano e uma especialidade cadastrados.");
            return;
        }

        System.out.println("Selecione o plano de saúde para configurar:");
        for (int i = 0; i < listaDePlanos.size(); i++) {
            System.out.println((i + 1) + ". " + listaDePlanos.get(i).getNome());
        }
        System.out.print("Opção de plano: ");
        int indicePlano = scanner.nextInt() - 1;
        scanner.nextLine(); // Limpa o buffer

        System.out.println("\nSelecione a especialidade para adicionar/atualizar o desconto:");
        for (int i = 0; i < listaDeEspecialidades.size(); i++) {
            System.out.println((i + 1) + ". " + listaDeEspecialidades.get(i).getNome());
        }
        System.out.print("Opção de especialidade: ");
        int indiceEspecialidade = scanner.nextInt() - 1;
        scanner.nextLine(); // Limpa o buffer

        // --- INÍCIO DA CORREÇÃO ---
        System.out.print("Digite o percentual de desconto (ex: 0.2 ou 0,2 para 20%): ");
        String inputDesconto = scanner.nextLine();
        double percentual = -1.0;

        try {

            inputDesconto = inputDesconto.replace(',', '.');

            percentual = Double.parseDouble(inputDesconto);
        } catch (NumberFormatException e) {
            System.out.println("ERRO: Formato de número inválido. Operação cancelada.");
            return; // Sai do método
        }


        if (indicePlano >= 0 && indicePlano < listaDePlanos.size() &&
                indiceEspecialidade >= 0 && indiceEspecialidade < listaDeEspecialidades.size()) {

            PlanoDeSaude planoEscolhido = listaDePlanos.get(indicePlano);
            Especialidade especialidadeEscolhida = listaDeEspecialidades.get(indiceEspecialidade);

            planoEscolhido.adicionarOuAtualizarDesconto(especialidadeEscolhida.getNome(), percentual);
            GerenciadorDeArquivos.salvarPlanos(listaDePlanos);

            System.out.println("Desconto de " + (int)(percentual * 100) + "% para " + especialidadeEscolhida.getNome() +
                    " no plano " + planoEscolhido.getNome() + " foi configurado com sucesso.");
        } else {
            System.out.println("Opção de plano ou especialidade inválida.");
        }
    }

    public static void agendarNovaConsulta() {
        System.out.println("\n--- Agendamento de Nova Consulta ---");
        if (listaDePacientes.isEmpty() || listaDeMedicos.isEmpty()) {
            System.out.println("ERRO: É preciso ter ao menos um paciente e um médico cadastrados.");
            return;
        }
        System.out.println("Selecione o paciente:");
        for (int i = 0; i < listaDePacientes.size(); i++) {
            System.out.println((i + 1) + ". " + listaDePacientes.get(i).getNome());
        }
        System.out.print("Opção: ");
        int indicePaciente = scanner.nextInt() - 1;
        scanner.nextLine();
        System.out.println("Selecione o médico:");
        for (int i = 0; i < listaDeMedicos.size(); i++) {
            System.out.println((i + 1) + ". Dr(a). " + listaDeMedicos.get(i).getNome());
        }
        System.out.print("Opção: ");
        int indiceMedico = scanner.nextInt() - 1;
        scanner.nextLine();
        LocalDateTime dataHoraConsulta = null;
        while (dataHoraConsulta == null) {
            System.out.print("Digite a data da consulta (formato AAAA-MM-DD): ");
            String data = scanner.nextLine();
            System.out.print("Digite a hora da consulta (formato HH:MM): ");
            String hora = scanner.nextLine();
            try {
                dataHoraConsulta = LocalDateTime.parse(data + "T" + hora);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data/hora inválido. Por favor, tente novamente.");
            }
        }
        System.out.print("Digite o local da consulta (ex: Consultório 101): ");
        String local = scanner.nextLine();
        try {
            Paciente pacienteEscolhido = listaDePacientes.get(indicePaciente);
            Medico medicoEscolhido = listaDeMedicos.get(indiceMedico);
            for (Consulta consultaExistente : listaDeConsultas) {
                if (consultaExistente.getDataHora().equals(dataHoraConsulta)) {
                    if (consultaExistente.getMedico().equals(medicoEscolhido)) {
                        System.out.println("ERRO: O Dr(a). " + medicoEscolhido.getNome() + " já tem uma consulta neste horário.");
                        return;
                    }
                    if (consultaExistente.getLocal().equalsIgnoreCase(local)) {
                        System.out.println("ERRO: O local '" + local + "' já está ocupado neste horário.");
                        return;
                    }
                }
            }
            Consulta novaConsulta = new Consulta(pacienteEscolhido, medicoEscolhido, dataHoraConsulta, local);
            listaDeConsultas.add(novaConsulta);
            pacienteEscolhido.adicionarConsulta(novaConsulta);
            System.out.println("\n--- Consulta Agendada com Sucesso! ---");
            novaConsulta.exibirInformacoes();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Opção de paciente ou médico inválida. Agendamento cancelado.");
        }
    }

    public static void concluirConsulta() {
        System.out.println("\n--- Concluir Consulta e Registrar Diagnóstico ---");
        List<Consulta> consultasAgendadas = new ArrayList<>();
        for (Consulta c : listaDeConsultas) {
            if (c.getStatus() == StatusConsulta.AGENDADA) {
                consultasAgendadas.add(c);
            }
        }
        if (consultasAgendadas.isEmpty()) {
            System.out.println("Não há consultas agendadas para concluir.");
            return;
        }
        System.out.println("Selecione a consulta que deseja concluir:");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (int i = 0; i < consultasAgendadas.size(); i++) {
            Consulta c = consultasAgendadas.get(i);
            System.out.println((i + 1) + ". Paciente: " + c.getPaciente().getNome() +
                    " | Médico: " + c.getMedico().getNome() +
                    " | Data: " + c.getDataHora().format(formatter));
        }
        System.out.print("Opção: ");
        int indiceConsulta = scanner.nextInt() - 1;
        scanner.nextLine();
        try {
            Consulta consultaEscolhida = consultasAgendadas.get(indiceConsulta);
            System.out.print("Digite o diagnóstico: ");
            String diagnostico = scanner.nextLine();
            System.out.print("Digite a prescrição de medicamentos: ");
            String prescricao = scanner.nextLine();
            consultaEscolhida.registrarDiagnostico(diagnostico, prescricao);
            System.out.println("\nConsulta concluída e diagnóstico registrado com sucesso!");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Opção inválida. Operação cancelada.");
        }
    }

    public static void registrarNovaInternacao() {
        System.out.println("\n--- Registrar Nova Internação ---");
        if (listaDePacientes.isEmpty() || listaDeMedicos.isEmpty()) {
            System.out.println("ERRO: É preciso ter ao menos um paciente e um médico cadastrados.");
            return;
        }
        System.out.println("Selecione o paciente para a internação:");
        for (int i = 0; i < listaDePacientes.size(); i++) {
            System.out.println((i + 1) + ". " + listaDePacientes.get(i).getNome());
        }
        System.out.print("Opção de paciente: ");
        int indicePaciente = scanner.nextInt() - 1;
        scanner.nextLine();
        System.out.println("Selecione o médico responsável:");
        for (int i = 0; i < listaDeMedicos.size(); i++) {
            System.out.println((i + 1) + ". Dr(a). " + listaDeMedicos.get(i).getNome());
        }
        System.out.print("Opção de médico: ");
        int indiceMedico = scanner.nextInt() - 1;
        scanner.nextLine();
        try {
            Paciente pacienteEscolhido = listaDePacientes.get(indicePaciente);
            Medico medicoEscolhido = listaDeMedicos.get(indiceMedico);
            System.out.print("Digite o número do quarto: ");
            String quarto = scanner.nextLine();
            for (Internacao i : listaDeInternacoes) {
                if (i.getQuarto().equalsIgnoreCase(quarto) && i.getStatus() == StatusInternacao.ATIVA) {
                    System.out.println("\nERRO: Este quarto já está ocupado. Operação cancelada.");
                    return;
                }
            }
            System.out.print("Digite o custo da diária: R$ ");
            double custoDiaria = scanner.nextDouble();
            scanner.nextLine();
            Internacao novaInternacao = new Internacao(pacienteEscolhido, medicoEscolhido, quarto, custoDiaria);
            listaDeInternacoes.add(novaInternacao);
            GerenciadorDeArquivos.salvarInternacoes(listaDeInternacoes);
            System.out.println("Internação registrada com sucesso para " + pacienteEscolhido.getNome() + ".");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Opção de paciente ou médico inválida. Operação cancelada.");
        }
    }

    public static void registrarAltaPaciente() {
        System.out.println("\n--- Registrar Alta de Paciente ---");
        List<Internacao> ativas = new ArrayList<>();
        for (Internacao i : listaDeInternacoes) {
            if (i.getStatus() == StatusInternacao.ATIVA) {
                ativas.add(i);
            }
        }

        if (ativas.isEmpty()) {
            System.out.println("Não há internações ativas para dar alta.");
            return;
        }

        System.out.println("Selecione a internação para registrar a alta:");
        for (int i = 0; i < ativas.size(); i++) {

            System.out.println((i + 1) + ". Paciente: " + ativas.get(i).getPaciente().getNome() + ", Quarto: " + ativas.get(i).getQuarto());
        }
        System.out.print("Opção: ");
        int escolha = scanner.nextInt() - 1;
        scanner.nextLine();

        if (escolha >= 0 && escolha < ativas.size()) {
            Internacao internacaoEscolhida = ativas.get(escolha);
            double custoTotal = internacaoEscolhida.registrarAlta();
            GerenciadorDeArquivos.salvarInternacoes(listaDeInternacoes);
            System.out.println("\nAlta registrada com sucesso para o paciente " + internacaoEscolhida.getPaciente().getNome() + "!");
            System.out.println("Custo total da internação: R$ " + String.format("%.2f", custoTotal));
        } else {
            System.out.println("Opção inválida.");
        }
    }

    public static void cancelarInternacao() {
        System.out.println("\n--- Cancelar Internação ---");
        List<Internacao> ativas = new ArrayList<>();
        for (Internacao i : listaDeInternacoes) {
            if (i.getStatus() == StatusInternacao.ATIVA) {
                ativas.add(i);
            }
        }

        if (ativas.isEmpty()) {
            System.out.println("Não há internações ativas para cancelar.");
            return;
        }

        System.out.println("Selecione a internação para cancelar:");
        for (int i = 0; i < ativas.size(); i++) {

            System.out.println((i + 1) + ". Paciente: " + ativas.get(i).getPaciente().getNome() + ", Quarto: " + ativas.get(i).getQuarto());
        }
        System.out.print("Opção: ");
        int escolha = scanner.nextInt() - 1;
        scanner.nextLine();

        if (escolha >= 0 && escolha < ativas.size()) {
            Internacao internacaoEscolhida = ativas.get(escolha);
            internacaoEscolhida.cancelar();
            GerenciadorDeArquivos.salvarInternacoes(listaDeInternacoes);
            System.out.println("Internação do paciente " + internacaoEscolhida.getPaciente().getNome() + " foi cancelada com sucesso!");
        } else {
            System.out.println("Opção inválida.");
        }
    }

    public static void listarInternacoes() {
        System.out.println("\n--- Lista de Todas as Internações ---");
        if (listaDeInternacoes.isEmpty()) {
            System.out.println("Nenhuma internação registrada.");
            return;
        }
        for (Internacao internacao : listaDeInternacoes) {
            internacao.exibirInformacoes();
            System.out.println("--------------------");
        }
    }
}