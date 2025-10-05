package com.hospitalar.sistema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Consulta {
   private Paciente paciente;
   private Medico medico;
   private LocalDateTime dataHora;
   private String local;
   private StatusConsulta status;
   private double custoFinal;
   private String diagnostico;
   private String prescricao;

   private static final double DESCONTO_PLANO_SAUDE = 0.20;

   public Consulta(Paciente paciente, Medico medico, LocalDateTime dataHora, String local) {
       this.paciente = paciente;
       this.medico = medico;
       this.dataHora = dataHora;
       this.local = local;
       this.status = StatusConsulta.AGENDADA;
       this.diagnostico = "";
       this.prescricao = "";

       this.calcularCustoFinal();

   }
    private void  calcularCustoFinal() {
       double custoBase = this.medico.getCustoConsulta();
       double descontoFinal = 0.0;
       String motivoDesconto = "Nenhum";

       if (this.paciente.getIdade() >= 60) {
           descontoFinal = 0.30;
           motivoDesconto = "Idade (60+)";
       }
       else if (this.paciente instanceof PacienteEspecial) {
           PacienteEspecial pe = (PacienteEspecial) this.paciente;
           PlanoDeSaude plano = pe.getPlanoDeSaude();
           String nomeEspecialidade = this.medico.getEspecialidade().getNome();

           descontoFinal = plano.getDesconto(nomeEspecialidade);
           motivoDesconto = "Plano de Saúde (" + plano.getNome() + ")";
       }
       this.custoFinal = custoBase - (custoBase * descontoFinal);
       if(descontoFinal > 0) {
           System.out.println("INFO: Desconto de " + (int)(descontoFinal * 100) + "% aplicado. Movtivo: " + motivoDesconto);
       }
    }

    public void registrarDiagnostico(String diagnostico, String prescricao) {
       this.diagnostico = diagnostico;
       this.prescricao = prescricao;
       this.status = StatusConsulta.CONCLUIDA;
    }


    public Paciente getPaciente() {
       return paciente;
    }
    public Medico getMedico() {
       return medico;
    }
    public LocalDateTime getDataHora() {
       return dataHora;
    }
    public String getLocal() {
       return local;
    }
    public StatusConsulta getStatus() {
       return status;
    }
    public double getCustoFinal() {
       return custoFinal;
    }

    public void setStatus(StatusConsulta status) {
       this.status = status;
    }
    public void exibirInformacoes(){
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'ás' HH:mm");

       System.out.println("Paciente: " + paciente.getNome());
       System.out.println("Medico: Dr(a). " + medico.getNome() + "(CRM: " + medico.getCrm() + ")");
       System.out.println("Data/Hora: " + dataHora.format(formatter));
       System.out.println("Local: " + local);
       System.out.println("Status: " + status);
       System.out.println("Custo Final: R$ " + String.format("%.2f", custoFinal));
       if(status == StatusConsulta.CONCLUIDA){
           System.out.println("Diagnostico: " + this.diagnostico);
           System.out.println("Prescricao: " + this.prescricao);
       }
    }

}