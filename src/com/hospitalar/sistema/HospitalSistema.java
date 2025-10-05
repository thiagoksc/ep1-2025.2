package com.hospitalar.sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HospitalSistema {
    private static List<Paciente> listaDePacientes = new ArrayList<>();
    private static List<Medico> listaDeMedicos = new ArrayList<>();
    private static List<Especialidade> ListaDeEspecialidades = new ArrayList<>();

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao Sistema de Gerenciamento Hospitalar!");

        inicializarEspecialidade();

        int opcao = 0;
        while (opcao != 9){
            exibirMenu();
            System.out.println("Escolha uma opcao: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao){
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
                case 9:
                    System.out.println("Encerrando o sistema...");
                default:
                    System.out.println("Opção inválida. Tente novamente.");

            }
        }
        scanner.close();
    }

    private static void inicializarEspecialidades(){
        listaDeEspecialidades.add(new Especialidade("Cardiologia"));
        listaDeEspecialidades.add(new Especialidade("Pediatria"));
        listaDeEspecialidades.add(new Especialidade("Ortopedia"));
        listaDeEspecialidades.add(new Especialidade("Dermatologia"));
   }

    public static void exibirMenu(){
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Cadastrar Paciente");
        System.out.println("2. Listar Pacientes");
        System.out.println("3. Cadastrar Novo Médico");
        System.out.println("4. Listar Medicos");
        System.out.println("9. Sair");
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
            System.out.println("Paciente Especial cadastrado com sucesso!");
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

    public static void cadastrarNovoMedico(){
        System.out.println("\n--- Cadastro de Novo Médico ---");
        System.out.print("Nome do Médico: ");
        String nome = scanner.nextLine();
        System.out.print("CRM: ");
        String crm = scanner.nextLine();
        System.out.print("Custo da Consulta: R$ ");
        double custo = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("\nSelecione a Especialidade:");
        for  (int i = 0; i < ListaDeEspecialidades.size(); i++) {
            System.out.println((i + 1) + ". " + ListaDeEspecialidades.get(i).getNome());
        }
        System.out.println((listaDeEspecialidades.size() + 1) + ". Outra (Cadastrar nova especialidade)");

        System.out.print("Digite o número da opção: ");
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
