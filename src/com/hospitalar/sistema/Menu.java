package com.hospitalar.sistema;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private static final Scanner scanner = new Scanner(System.in);

    public static void executar() {
        int opcao = 0;
        while (opcao != 16) {
            exibirMenuPrincipal();
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nERRO: Por favor, digite apenas números.");
                opcao = 0;
            } finally {
                scanner.nextLine();
            }

            switch (opcao) {
                case 1: HospitalSistema.cadastrarNovoPaciente(); break;
                case 2: HospitalSistema.listarPacientes(); break;
                case 3: HospitalSistema.excluirPaciente(); break;
                case 4: HospitalSistema.cadastrarNovoMedico(); break;
                case 5: HospitalSistema.listarMedicos(); break;
                case 6: HospitalSistema.excluirMedico(); break;
                case 7: HospitalSistema.cadastrarPlanoDeSaude(); break;
                case 8: HospitalSistema.configurarDescontosPlano(); break;
                case 9: HospitalSistema.agendarNovaConsulta(); break;
                case 10: HospitalSistema.concluirConsulta(); break;
                case 11: HospitalSistema.registrarNovaInternacao(); break;
                case 12: HospitalSistema.registrarAltaPaciente(); break;
                case 13: HospitalSistema.cancelarInternacao(); break;
                case 14: HospitalSistema.listarInternacoes(); break;
                case 15: exibirMenuRelatorios(); break;
                case 16: System.out.println("Encerrando o sistema..."); break;
                default:
                    if (opcao != 16) {
                        System.out.println("Opção inválida. Tente novamente.");
                    }
            }
        }
        scanner.close();
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("--- Pacientes ---");
        System.out.println("1. Cadastrar Novo Paciente");
        System.out.println("2. Listar Pacientes");
        System.out.println("3. Excluir Paciente");
        System.out.println("--- Médicos e Planos ---");
        System.out.println("4. Cadastrar Novo Médico");
        System.out.println("5. Listar Médicos");
        System.out.println("6. Excluir Médico");
        System.out.println("7. Cadastrar Plano de Saúde");
        System.out.println("8. Configurar Descontos de um Plano");
        System.out.println("--- Atendimentos ---");
        System.out.println("9. Agendar Nova Consulta");
        System.out.println("10. Concluir Consulta e Registrar Diagnóstico");
        System.out.println("11. Registrar Nova Internação");
        System.out.println("12. Registrar Alta de Paciente");
        System.out.println("13. Cancelar Internação");
        System.out.println("14. Listar Internações");
        System.out.println("--- Sistema ---");
        System.out.println("15. Módulo de Relatórios");
        System.out.println("16. Sair");
    }

    private static void exibirMenuRelatorios() {
        int opcao = 0;
        while (opcao != 9) {
            System.out.println("\n--- MÓDULO DE RELATÓRIOS ---");
            System.out.println("1. Relatório de Pacientes (com Histórico)");
            System.out.println("2. Relatório de Pacientes Internados");
            System.out.println("3. Relatório de Médicos (com N° de Consultas)");
            System.out.println("4. Relatório de Estatísticas Gerais");
            System.out.println("5. Relatório de Consultas (com Filtros)");
            System.out.println("6. Relatório de Planos de Saúde (Uso e Economia)");
            System.out.println("9. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção de relatório: ");

            try {
                opcao = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nERRO: Opção inválida.");
                opcao = 0;
            } finally {
                scanner.nextLine();
            }

            switch (opcao) {
                case 1: HospitalSistema.gerarRelatorioPacientesComHistorico(); break;
                case 2: HospitalSistema.gerarRelatorioInternacoesAtivas(); break;
                case 3: HospitalSistema.gerarRelatorioMedicosDetalhado(); break;
                case 4: HospitalSistema.gerarRelatorioEstatisticasGerais(); break;
                case 5: HospitalSistema.gerarRelatorioConsultasComFiltros(); break; // NOVA CHAMADA
                case 6: HospitalSistema.gerarRelatorioPlanosDeSaude(); break; // NOVA CHAMADA
                case 9: System.out.println("Retornando ao menu principal..."); break;
                default: System.out.println("Opção inválida.");
            }
        }
    }
}