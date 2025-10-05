package com.hospitalar.sistema;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HospitalSistema {
    private static List<Paciente> listaDePacientes = new ArrayList<>();
    private static List<Medico> listaDeMedicos = new ArrayList<>();
    private static List<Especialidade> listaDeEspecialidades = new ArrayList<>();
    private static List<Consulta> listaDeConsultas = new ArrayList<>();

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao Sistema de Gerenciamento Hospitalar!");

        inicializarEspecialidades();

        int opcao = 0;
        while (opcao != 9) {
            exibirMenu();
            System.out.println("Escolha uma opcao: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarNovoPaciente();
                    break;
                case 2:
                    listarPacientes();
                    break;
                case 3:
                    cadastrarNovoMedico();
                    break;
                case 4:
                    listarMedicos();
                    break;
                case 5:
                    agendarNovaConsulta();
                    break;
                case 9:
                    System.out.println("Encerrando o sistema...");
                default:
                    System.out.println("Opção inválida. Tente novamente.");

            }
        }
        scanner.close();
    }

    private static void inicializarEspecialidades() {
        listaDeEspecialidades.add(new Especialidade("Cardiologia"));
        listaDeEspecialidades.add(new Especialidade("Pediatria"));
        listaDeEspecialidades.add(new Especialidade("Ortopedia"));
        listaDeEspecialidades.add(new Especialidade("Dermatologia"));
    }

    public static void exibirMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Cadastrar Paciente");
        System.out.println("2. Listar Pacientes");
        System.out.println("3. Cadastrar Novo Médico");
        System.out.println("5. Agendar Nova Consulta");
        System.out.println("4. Listar Medicos");
        System.out.println("9. Sair");
    }

    private static void agendarNovaConsulta() {

        System.out.println("\n--- Agendamento de Nova Consulta --- ");

        if (listaDePacientes.isEmpty()) {
            System.out.println("Não há pacientes cadastrados. Cadastre um paciente primeiro");
            return;
        }

        System.out.println("Selecione o paciente:");
        for (int i = 0; i < listaDePacientes.size(); i++) {
            System.out.println((i + 1) + ". " + listaDePacientes.get(i).getNome());
        }

        System.out.println("Opção: ");
        int indicePaciente = scanner.nextInt() - 1;
        scanner.nextLine();

        if (listaDeMedicos.isEmpty()) {
            System.out.println("Não há médicos. Cadastre um médico primeiro");
            return;
        }
        System.out.println("Opção: ");
        int indiceMedico = scanner.nextInt() - 1;
        scanner.nextLine();

        LocalDateTime dataHoraConsulta = null;
        while (dataHoraConsulta == null) {
            System.out.print("Digite a data da consulta (formato AAAA-MM-DD): ");
            String data = scanner.nextLine();
            System.out.print("Digite a hora consulta (formato HH:MM): ");
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

            System.out.println("\n--- Consulta Agendada com Sucesso! ----");
            novaConsulta.exibirInformacoes();

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Opção de paciente ou médico inválida. Agendamento cancelado.");
        }

    }

    public static void cadastrarNovoPaciente(){
        System.out.println("\n--- Cadastro de Novo Paciente ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine();

        System.out.print("O paciente tem plano de saúde? (S/N): ");
        String temPlano = scanner.nextLine();

        if (temPlano.equalsIgnoreCase("S")){
            System.out.print("Qual o plano de saúde?");
            String plano = scanner.nextLine();
            PacienteEspecial novoPaciente = new PacienteEspecial(nome, cpf, idade, plano);
            listaDePacientes.add(novoPaciente);
            System.out.println("Paciente ESPECIAL cadastrado com sucesso!");


        } else{
            Paciente novoPaciente = new Paciente(nome, cpf, idade);
            listaDePacientes.add(novoPaciente);
            System.out.println("Paciente cadastrado com sucesso!");
        }
    }

    private static void listarPacientes(){
        System.out.println("\n--- Lista de Pacientes Cadastrados ---");
        if  (listaDePacientes.isEmpty()){
            System.out.println("Nenhum paciente cadastrado ainda.");
            return;
        }
        for (int i = 0; i < listaDePacientes.size(); i++) {
            Paciente p =  listaDePacientes.get(i);
            System.out.println("\n--- Paciente " + (i + 1) + "---");
            p.exibirInformacoes();
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
            System.out.println((i + 1) + ". " + listaDeEspecialidades.get(i).getNome());
        }
        System.out.println((listaDeEspecialidades.size() + 1) + ". Outra (Cadastrar nova especialidade)");

        System.out.print("Digite o número da opção: ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        Especialidade especialidadeEscolhida = null;

        if (escolha > 0 && escolha <= listaDeEspecialidades.size()) {
            especialidadeEscolhida = listaDeEspecialidades.get(escolha - 1);
        } else if (escolha == listaDeEspecialidades.size() + 1) {
            System.out.print("Digite o nome da especialidade: ");
            String nomeNovaEspecialidade = scanner.nextLine();

            especialidadeEscolhida = new Especialidade(nomeNovaEspecialidade);
            listaDeEspecialidades.add(especialidadeEscolhida);
            System.out.println("Nova especialidade '" + nomeNovaEspecialidade + "' foi cadastrada!");
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
            System.out.println("Medico cadastrado com sucesso!");
        }else {
            System.out.println("Opção de Especialidade inválida. O cadastro foi cancelado");
        }

    }

    private static void listarMedicos(){
        System.out.println("\n--- Lista de Medicos Cadastrados ---");
        if  (listaDeMedicos.isEmpty()){
            System.out.println("Nenhum medico cadastrado ainda.");
            return;
        }
        for (int i = 0; i < listaDeMedicos.size(); i++) {
            Medico m =  listaDeMedicos.get(i);
            System.out.println("\n--- Medico " + (i + 1) + "---");
            m.exibirInformacoes();
        }
    }

}
